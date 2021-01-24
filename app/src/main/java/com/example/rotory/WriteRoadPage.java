package com.example.rotory;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import java.util.List;

public class WriteRoadPage extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private static final String TAG = "WriteRoadPage";
    private static final int REQUEST_CODE = 4000;

    FirebaseFirestore db;
    
    EditText writeRoadTitleEditText;
    EditText writeRoadReviewEditText;
    EditText writeRoadTagEditText;

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

        getTime();
    }

    private void getTime() {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        startLocationService();
        showCurrentLocation(latitude, longitude);

        map.setOnMapClickListener(this);

        Intent intent = getIntent();
        dtrName = intent.getStringArrayListExtra("dtrName");
        dtrLatLng = intent.getStringArrayListExtra("dtrLatLng");

        LatLng dtrLatLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(dtrLatLng);
        map.addMarker(markerOptions);

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
        LatLng curPoint = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

    }

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
