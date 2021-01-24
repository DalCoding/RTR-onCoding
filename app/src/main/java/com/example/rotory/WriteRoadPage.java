package com.example.rotory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

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
import com.example.rotory.WriteContents.TagSelectDialog;
import com.example.rotory.WriteContents.WriteRoadTagAdapter;
import com.example.rotory.google.WriteMapPage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteRoadPage extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private static final String TAG = "WriteRoadPage";
    private static final int REQUEST_CODE = 4000;

    FirebaseFirestore db;

    RecyclerView tagsRecyclerView;
    EditText writeRoadTitleEditText;
    EditText writeRoadReviewEditText;
    EditText writeRoadTagEditText;

    Map<String, Object> roadContents = new HashMap<>();
    ArrayList<String> tagItems;

    String tagText;
    String ratingResult;
    float ratingNum;
    ThemePickPage themePickPage;

    Button chooseTagBtn;
    Button tagInputBtn;
    Button setReviewScoreBtn;

    WriteRoadTagAdapter roadTagAdapter;

    float num;

    ArrayList<String> dtrName;
    ArrayList<String> dtrLatLng;

    GoogleMap map;

    Double latitude;
    Double longitude;

    WriteMapPage fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_road_page);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.writeRoadMap);
        mapFragment.getMapAsync(this);


        /*Intent intent = getIntent();
        intent.getStringArrayListExtra("dtrName");
        intent.getStringArrayListExtra("dtrLatLng");*/

        fragment = new WriteMapPage();

        roadTagAdapter = new WriteRoadTagAdapter(this);
        tagsRecyclerView = findViewById(R.id.writeRoadTagRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tagsRecyclerView.setLayoutManager(layoutManager);
        tagsRecyclerView.setAdapter(roadTagAdapter);

        tagItems = new ArrayList<>();
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
                startActivity(intent);

            }
        });

        tagInputBtn=findViewById(R.id.writeRoadTagTextInputBtn);
        tagInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeRoadTagEditText = findViewById(R.id.writeRoadTagEditText);
                String addedCustomTag = writeRoadTagEditText.getText().toString();
                if (addedCustomTag.length() < 1) {

                } else {
                    if (addedCustomTag.contains("#")) {
                        tagItems.add(addedCustomTag);
                    } else {
                        String withHash = "#" + addedCustomTag;
                        tagItems.add(withHash);
                    }
                    roadTagAdapter.addItem(new Tags(addedCustomTag));
                    writeRoadTagEditText.setText("");
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


        getTime();
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

    private void getTime() {
        themePickPage = new ThemePickPage();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        startLocationService();
        //showCurrentLocation(latitude, longitude);

        map.setOnMapClickListener(this);

        Intent intent = getIntent();
        dtrName = intent.getStringArrayListExtra("dtrName");
        dtrLatLng = intent.getStringArrayListExtra("dtrLatLng");

       /* LatLng dtrLatLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(dtrLatLng);
        map.addMarker(markerOptions);*/

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
