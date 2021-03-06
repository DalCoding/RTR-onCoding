package com.example.rotory.WriteContents;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnContentsItemClickListener;
import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.MainActivity;
import com.example.rotory.R;
import com.example.rotory.Theme.TagItemAdapter;
import com.example.rotory.Theme.Tags;
import com.example.rotory.VO.AppConstant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.internal.bind.ObjectTypeAdapter;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteRoadPage extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private static final String TAG = "WriteRoadPage";
    private static final int REQUEST_CODE = 4000;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    RecyclerView tagsRecyclerView;
    EditText writeRoadTitle;
    EditText writeRoadReview;
    EditText writeRoadTag;

    TextView writeRoadHour;
    TextView writeRoadMin;

    Map<String, Object> roadContents = new HashMap<>();
    ArrayList<String> tagItems;

    String tagText;
    String ratingResult;
    float ratingNum;
    String companionText;
    String addedCustomTag;

    Button chooseTagBtn;
    Button tagInputBtn;
    Button setReviewScoreBtn;
    Button checkmarkBtn;

    Spinner companySpinner;
    RelativeLayout timeSelectLayout;
    RadioButton publicRadioButton;
    RadioButton privateRadioButton;

    WriteRoadTagAdapter roadTagAdapter;
    AppConstant appConstant = new AppConstant();

    float num;
    int isPublic;

    GoogleMap map;

    Double latitude;
    Double longitude;

    Boolean isMap = false;

    WriteMapPage fragment;
    NestedScrollView writeRoadContainer;

    ArrayList<String> dtrName = new ArrayList<>();
    ArrayList<LatLng> dtrLatLng = new ArrayList<>();
    ArrayList<LatLng> PolyPoints = new ArrayList<>();
    ArrayList<String> dtrAddress = new ArrayList<>();

    private InputMethodManager keyboardManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_road_page);
        user = mAuth.getCurrentUser();

        keyboardManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        writeRoadContainer = findViewById(R.id.writeRoadContainer);
        writeRoadContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboardManager.hideSoftInputFromWindow(writeRoadReview.getWindowToken(),0);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.writeRoadMap);
        mapFragment.getMapAsync(this);

        isPublic = 1;

        fragment = new WriteMapPage();


        ImageButton backBtn = findViewById(R.id.backImageButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMap) {
                    Toast.makeText(getBaseContext(), "경로가 저장 되었습니다.", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    dtrName = fragment.getDtrName();
                    Log.d(TAG, dtrName.toString());
                    dtrLatLng = fragment.getDtrLatLng();
                    Log.d(TAG, dtrLatLng.toString());
                    dtrAddress = fragment.getDtrAddress();
                    Log.d(TAG, dtrAddress.toString());
                    Log.d(TAG, "마커 들고옴 : " + dtrLatLng.toString());
                    setLoadMarkers();
                    isMap = false;

                } else {
                    deleteCustomTag();
                    Toast.makeText(getApplicationContext(), "작성을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WriteRoadPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        writeRoadTitle = findViewById(R.id.writeRoadTitleEditText);
        writeRoadReview = findViewById(R.id.writeRoadReviewEditText);

        tagItems = new ArrayList<>();

        roadTagAdapter = new WriteRoadTagAdapter(this);
        tagsRecyclerView = findViewById(R.id.writeRoadTagRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tagsRecyclerView.setLayoutManager(layoutManager);
        tagsRecyclerView.setAdapter(roadTagAdapter);


        chooseTagBtn = findViewById(R.id.writeRoadChooseTagBtn);
        chooseTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (roadTagAdapter.getItemList() != null) {
                    roadTagAdapter.removeItem(roadTagAdapter.getItemList());
                    roadTagAdapter.notifyDataSetChanged();
                }

                Log.d(TAG,"삭제 확인한기"+ roadTagAdapter.getItemCount());

                TagSelectDialog.listener = new OnTagItemClickListener() {
                    @Override
                    public void onItemClick(TagItemAdapter.tagItemViewHolder holder, View view, int position) {
                    }

                    @Override
                    public void onItemSelected(String tag) {
                        tagText = tag;

                        if (tagItems.contains(tagText)){
                            tagItems.remove(tagText);
                        }else {
                            if (tagItems.size()==10){
                                Toast.makeText(getApplicationContext(),"태그는 10개 까지 선택가능합니다.",Toast.LENGTH_SHORT).show();
                            }{
                                tagItems.add(tagText);
                            }
                        }
                        Log.d(TAG,"들어갔는지 확인" + tagItems);

                    }
                };

                Intent intent = new Intent(getApplicationContext(), TagSelectDialog.class);
                intent.putExtra("selectedTag", tagItems);
                startActivity(intent);

            }
        });

        tagInputBtn=findViewById(R.id.writeRoadTagTextInputBtn);
        tagInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeRoadTag = findViewById(R.id.writeRoadTagEditText);
                addedCustomTag = writeRoadTag.getText().toString();
                if (addedCustomTag.length() < 1) {

                } else {
                    if (addedCustomTag.contains("#")) {
                        tagItems.add(addedCustomTag);
                    } else {
                        String withHash = "#" + addedCustomTag;
                        tagItems.add(withHash);
                    }
                    roadTagAdapter.addItem(new Tags(addedCustomTag));
                    roadTagAdapter.notifyDataSetChanged();
                    writeRoadTag.setText("");

                    Log.d(TAG, "들어갔는지 확인" + tagItems);
                    saveCustomTag();

                }
            }
        });

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        setReviewScoreBtn = findViewById(R.id.setReviewScoreBtn);
        setReviewScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRatingDialog(dialogBuilder);
            }
        });

        companySpinner = findViewById(R.id.companySpinner);
        getCompany();
        Log.d(TAG, "리스너 밖에서 선택된 텍스트 가져왔는지 확인" + companionText);

        writeRoadHour = findViewById(R.id.writeRoadHour);
        writeRoadMin = findViewById(R.id.writeRoadMin);

        timeSelectLayout = findViewById(R.id.timeSelectLayout);
        timeSelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime();
            }
        });

        publicRadioButton = findViewById(R.id.publicRadioButton);
        publicRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPublic = 1;

            }
        });

        privateRadioButton = findViewById(R.id.privateRadioButton);
        privateRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPublic = 0;
            }
        });
        checkmarkBtn = findViewById(R.id.checkmarkBtn);
        checkmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()){
                    setUserInfo();
                    Intent intent = new Intent(WriteRoadPage.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else {
                    Toast.makeText(getApplicationContext(),"필수 사항을 입력해 주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void deleteCustomTag() {
        db.collection("tag").document("customTag").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Map<String, Object> customTagList = task.getResult().getData();
                        if (tagItems != null) {
                            for (int i = 0; i < tagItems.size(); i++){
                                if (customTagList.containsKey(tagItems.get(i))){
                                    DocumentReference docRef = db.collection("tag").document("customTag");
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put(tagItems.get(i), FieldValue.delete());
                                    docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG,"삭제 정보 확인" + docRef);
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
    }

    private void saveCustomTag() {
        Map<String, String> customTag = new HashMap<>();
        addedCustomTag ="#"+addedCustomTag;
        customTag.put(addedCustomTag, addedCustomTag);
        db.collection("tag").whereEqualTo(addedCustomTag, addedCustomTag)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                db.collection("tag").document("customTag").set(customTag, SetOptions.merge())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        roadTagAdapter.notifyDataSetChanged();

        if (tagItems.size()>0) {
            for (int i = 0; i < tagItems.size(); i++) {
                roadTagAdapter.addItem(new Tags(tagItems.get(i)));
            }
        }
    }

    private void getCompany() {
        companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                companionText = parent.getSelectedItem().toString();
                Log.d(TAG, "선택된 텍스트 가져왔는지 확인" + companionText);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setRatingDialog(AlertDialog.Builder dialog) {
        LayoutInflater inflater = getLayoutInflater();

        View viewDialog = inflater.inflate(R.layout.dotory_rating, null);
        dialog.setView(viewDialog);
        dialog.setTitle("도토리 별점 선택");

        RatingBar ratingBar = viewDialog.findViewById(R.id.rating);

        ratingBar.setIsIndicator(false);
        ratingBar.setStepSize(1f);
        ratingBar.setOnRatingBarChangeListener(ratClick);
        if (ratingNum >0){
            ratingBar.setRating(ratingNum);
        }

        dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                roadContents.put("dtrRating", Float.valueOf(ratingResult));
                Log.d(TAG,"평점 잘 들어갔는지 확인" + ratingResult);
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });

        alertDialog.show();


    }

    RatingBar.OnRatingBarChangeListener ratClick = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            num = rating;
            float st = 5.0f/ratingBar.getNumStars();
            ratingResult = String.format("%.1f", st*rating);
            ratingNum = rating;
            Log.d(TAG,"별점 설정 끝 " + ratingResult);
        }
    };

    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            writeRoadHour.setText(String.valueOf(hourOfDay));
            writeRoadMin.setText(String.valueOf(minute));
            Log.d(TAG,"선택된 시간 확인" + hourOfDay + " : " + minute);

        }
    };

    private void getTime() {
        TimePickerDialog dialog = new TimePickerDialog(WriteRoadPage.this, android.R.style.Theme_Holo_Dialog_NoActionBar, listener,
                00,00, true);
        dialog.setTitle("소요시간");
        dialog.show();

    }
    private void setSavingData(){
        String writtenDate = appConstant.dateFormat.format(new Date());

        roadContents.put("contentsType", 0);
        roadContents.put("title", writeRoadTitle.getText().toString());
        for (int i = 0; i < tagItems.size(); i++) {
            String tagKey = "tag" + (i + 1);
            roadContents.put(tagKey, tagItems.get(i));
        }
        roadContents.put("tagList",tagItems);
        roadContents.put("hour", writeRoadHour.getText().toString());
        roadContents.put("min", writeRoadMin.getText().toString());
        roadContents.put("isPartner", companySpinner.getSelectedItem().toString());
        roadContents.put("dtrRating", ratingNum);
        roadContents.put("ratingComment", writeRoadReview.getText().toString());
        roadContents.put("isPublic", isPublic);
        roadContents.put("writeDate", writtenDate);
        roadContents.put("dtrName", dtrName);
        roadContents.put("dtrLatLng", dtrLatLng);
        roadContents.put("dtrAddress", dtrAddress);
        roadContents.put("liked", "0");
        Log.d(TAG, "입력될 내용 확인" + roadContents);

    }
    private void saveContents() {
        db.collection("contents").add(roadContents)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Log.d(TAG,"모든 정보 저장" + roadContents);
                    }
                });
    }

    private void setUserInfo(){
        String userEmail = user.getEmail();
        String userUid = user.getUid();
        db.collection("person").whereEqualTo("userId", userEmail)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot pDocument : task.getResult()){
                        String userLevel = pDocument.get("userLevel").toString();
                        String userName = pDocument.get("userName").toString();
                        Map<String, Object> addUserInfo = new HashMap<>();
                        roadContents.put("userLevel", userLevel);
                        roadContents.put("userName", userName);
                        roadContents.put("uid", userUid);

                        setSavingData();
                        saveContents();

                        Log.d(TAG,"저장할 사용자 정보" + roadContents);


                    }
                }
            }
        });

    }

    private boolean isValidate() {
        if (writeRoadTitle.getText().toString().length() < 1){
            return false;
        }else if (writeRoadHour.getText().toString().length()<1
                && writeRoadMin.getText().toString().length() <1) {
            return false;
        }else if (dtrName.size() == 0 || dtrName.size() < 1){
            return false;
        }else if (dtrLatLng == null || dtrLatLng.size() < 1){
            return false;
        }else if (dtrAddress.size() == 0 || dtrAddress.size() <1) {
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        startLocationService();
        showCurrentLocation(latitude, longitude);
        map.setOnMapClickListener(this);

    }

    public void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                String message = "내 위치 -> Latitude : " + latitude + "\nLongitude : " + longitude;
                Log.d(TAG, message);
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void showCurrentLocation(Double latitude, Double longitude) {
        if (latitude == null) {

        } else {
            LatLng curPoint = new LatLng(latitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        replaceFragment(fragment);
        isMap = true;

    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.bigMapContainer, fragment).commit();
    }

    public void drawPoly(GoogleMap map, ArrayList<LatLng> polyPoints) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(5).color(Color.argb(128, 255, 51, 0));

        for (int i = 0; i < polyPoints.size(); i++) {
            LatLng polyPoint = polyPoints.get(i);
            polylineOptions.add(polyPoint);

            map.addPolyline(polylineOptions);
        }
    }

    public void setLoadMarkers() {
        Log.d(TAG, "시작함");
        MarkerOptions smallMapMarker = new MarkerOptions();

        for (int i = 0; i < dtrLatLng.size(); i++) {
            double dtrLat = dtrLatLng.get(i).latitude;
            double dtrLng = dtrLatLng.get(i).longitude;

            LatLng dtrLatLng2 = new LatLng(dtrLat, dtrLng);

            Log.d(TAG, "좌표" + dtrLatLng.get(i).toString());

            smallMapMarker.position(dtrLatLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.acorn2));
            map.addMarker(smallMapMarker);

            PolyPoints.add(dtrLatLng2);
            drawPoly(map, PolyPoints);

        }
    }
}
