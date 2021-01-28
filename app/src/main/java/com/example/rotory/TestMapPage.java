package com.example.rotory;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestMapPage extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final String TAG = "Test";

    String contentsID = "ivKwpuWoyGnB6v7FvsEm";

    GoogleMap map;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<String> dtrName = new ArrayList<>();
    ArrayList<Object> dtrLatLng = new ArrayList<>();
    ArrayList<LatLng> PolyPoints = new ArrayList<>();
    ArrayList<String> dtrAddress = new ArrayList<>();



    public TestMapPage() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.road_contents_page, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.rcontentsMap);
        mapFragment.getMapAsync(this);


        return viewGroup;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this.getActivity());

        map = googleMap;

        db.collection("contents")
                .document(contentsID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> contents = documentSnapshot.getData();
                        dtrName = (ArrayList<String>) contents.get("dtrName");
                        dtrAddress = (ArrayList<String>) contents.get("dtrAddress");
                        dtrLatLng = (ArrayList<Object>) contents.get("dtrLatLng");


                        MarkerOptions markerOptions = new MarkerOptions();

                        for (int i = 0; i < dtrLatLng.size(); i++) {
                            Map<String, Double> list = (Map<String, Double>) dtrLatLng.get(i);
                            Log.d(TAG, "for문 안 확인" + list);
                            double latitude = list.get("latitude");
                            double longitude = list.get("longitude");

                            Log.d(TAG, "latitude : longitude" + latitude + ":" + longitude);

                            LatLng latLng = new LatLng(latitude, longitude);

                            markerOptions.position(latLng);
                            markerOptions.title(String.valueOf(dtrName));
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.acorn2));
                            map.addMarker(markerOptions);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

                            PolyPoints.add(latLng);
                            drawPoly(map, PolyPoints);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
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

}


