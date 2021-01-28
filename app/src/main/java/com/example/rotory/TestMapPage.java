package com.example.rotory;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class TestMapPage extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final String TAG = "Test";

    GoogleMap map;
    MapView mapView;

    Double latitude;
    Double longitude;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference collRef = db.collection("contentsType").document("1Icyko95eOyx1atvpcbL");

    ArrayList<String> dtrName = new ArrayList<>();
    ArrayList<LatLng> dtrLatLng = new ArrayList<>();
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

        String contentsID = "1Icyko95eOyx1atvpcbL";

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.rcontentsMap);
        mapFragment.getMapAsync(this);



        db.collection("contents")
                .document(contentsID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, String.valueOf(documentSnapshot.getData()));
                        Map<String, Object> Name = documentSnapshot.getData();
                        Map<String, Object> LatLng = documentSnapshot.getData();
                        Map<String, Object> Address = documentSnapshot.getData();
                        for (Map.Entry<String, Object> entryName : Name.entrySet()) {
                            for (Map.Entry<String, Object> entryLatLng : LatLng.entrySet()) {
                                for (Map.Entry<String, Object> entryAddress : Address.entrySet()) {
                                    if (entryName.getKey().equals("dtrName") && entryLatLng.getKey().equals("dtrLatLng") && entryAddress.getKey().equals("dtrAddress")) {
                                        dtrName.add(entryName.toString());
                                        Log.d(TAG, dtrName.toString());
                                        dtrAddress.add(entryAddress.toString());
                                        Log.d(TAG, dtrAddress.toString());
                                        ArrayList<LatLng> entryList = (ArrayList<com.google.android.gms.maps.model.LatLng>) entryLatLng.getValue();
                                        dtrLatLng = entryList;
                                        Log.d(TAG, dtrLatLng.toString());
                                    }
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });

        return viewGroup;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this.getActivity());

        map = googleMap;


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(String.valueOf(dtrName));


    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

}
