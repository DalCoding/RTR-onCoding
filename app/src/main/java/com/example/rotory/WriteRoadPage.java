package com.example.rotory;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.Theme.TagItemAdapter;
import com.example.rotory.Theme.Tags;
import com.example.rotory.Theme.ThemePickPage;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.WriteContents.TagSelectDialog;
import com.example.rotory.WriteContents.WriteRoadTagAdapter;
import com.example.rotory.google.WriteMapPage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
<<<<<<< HEAD
import com.google.protobuf.DoubleValue;
=======
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
>>>>>>> origin/master

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteRoadPage extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    ArrayList<MapItem> items = new ArrayList<>();

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

    WriteMapPage fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_road_page);
        user = mAuth.getCurrentUser();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.writeRoadMap);
        mapFragment.getMapAsync(this);

        isPublic = 1;
        /*Intent intent = getIntent();
        intent.getStringArrayListExtra("dtrName");
        intent.getStringArrayListExtra("dtrLatLng");*/




        fragment = new WriteMapPage();

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

                roadTagAdapter.removeItem(roadTagAdapter.getItemList());
                roadTagAdapter.notifyDataSetChanged();

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
                            if (tagItems.size()==8){
                                Toast.makeText(getApplicationContext(),"태그는 8개 까지 선택가능합니다.",Toast.LENGTH_SHORT).show();
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
                String addedCustomTag = writeRoadTag.getText().toString();
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

              }else {
                  Toast.makeText(getApplicationContext(),"필수 사항을 입력해 주세요", Toast.LENGTH_SHORT).show();
              }
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
            roadContents.put("hour", writeRoadHour.getText().toString());
            roadContents.put("min", writeRoadMin.getText().toString());
            roadContents.put("isPartner", companySpinner.getSelectedItem().toString());
            roadContents.put("dtrRating", ratingNum);
            roadContents.put("ratingComment", writeRoadReview.getText().toString());
            roadContents.put("isPublic", isPublic);
            roadContents.put("writeDate", writtenDate);
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
        }

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        startLocationService();
        //showCurrentLocation(latitude, longitude);

        map.setOnMapClickListener(this);

        /*Intent intent = getIntent();
        ArrayList<String> dtrName = (ArrayList<String>) intent.getSerializableExtra("dtrName");
        Serializable name = intent.getSerializableExtra("dtrName");
        ArrayList<String> dtrLatLng = (ArrayList<String>) intent.getSerializableExtra("dtrLatLng");
        Serializable latlng = intent.getSerializableExtra("dtrLatLng");

<<<<<<< HEAD
=======
       /* LatLng dtrLatLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(dtrLatLng);
        map.addMarker(markerOptions);*/
>>>>>>> origin/master

        for (int i = 0; i < items.size(); i++) {
            // 가져온 데이터로 맵에 마커 적용
        }*/
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
/*
    private void showCurrentLocation(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

    }*/

    @Override
    public void onMapClick(LatLng latLng) {
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bigMapContainer, fragment);
        fragmentTransaction.commit();
    }
}
