package com.example.rotory.google;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rotory.Interface.OnBackPressedListener;
import com.example.rotory.MapItem;
import com.example.rotory.R;
import com.example.rotory.WriteRoadPage;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import net.daum.mf.map.api.MapPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class WriteMapPage extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, AutoPermissionsListener, OnBackPressedListener {

    ArrayList<MapItem> items = new ArrayList<>();

    private static final String TAG = "WriteMapPage";

    private static final String apiKey = "AIzaSyAf5Zp2t2IQyKHlMtWpkaKvsRsOEBDnVIs";

    Double latitude;
    Double longitude;

    Button writeMapAddBtn;
    boolean MarkerExists = false;
    String DtrName;
    EditText mapSearchEditText;

    GoogleMap map;
    MapView mapView;

    MarkerOptions markerOptions;

    ArrayList<String> dtrName = new ArrayList<>();
    ArrayList<String> dtrLatLng = new ArrayList<>();
    ArrayList<LatLng> PolyPoints = new ArrayList<>();

    ImageButton backBtn;

    public WriteMapPage() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.write_map_page, container, false);

        writeMapAddBtn = (Button) rootView.findViewById(R.id.writeMapAddBtn);
        mapSearchEditText = (EditText) rootView.findViewById(R.id.writeMapSearchEditText);
        backBtn = rootView.findViewById(R.id.backImageButton);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backWritePage();
            }
        });


        mapView = (MapView) rootView.findViewById(R.id.writeMapContainer);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);


        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), apiKey);
        }

        PlacesClient placesClient = Places.createClient(getContext());

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                LatLng latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                map.addMarker(new MarkerOptions().position(latLng).title(place.getName()).snippet(place.getAddress()));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        return rootView;

    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
        Log.d(TAG, "onLowMemory");
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        Log.d(TAG, "onDestroy");
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this.getActivity());
        map = googleMap;
        startLocationService();
        //showCurrentLocation(latitude, longitude);

        if (MarkerExists) {
            writeMapAddBtn.setVisibility(View.INVISIBLE);
        } else {
            map.setOnMapClickListener(this);
        }

        AutoPermissions.Companion.loadAllPermissions(getActivity(), 101);
    }

    public void startLocationService() {
        LocationManager manager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);

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

    @Override
    public void onMapClick(LatLng latLng) {
        markerOptions = new MarkerOptions();

        writeMapAddBtn.setVisibility(View.VISIBLE);
        writeMapAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                markerOptions.position(new LatLng(latitude, longitude));
                showDtrDialog(map, latLng);
                writeMapAddBtn.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void showDtrDialog(GoogleMap map, LatLng latLng) {

        AlertDialog.Builder DtrDialog = new AlertDialog.Builder(getContext());
        DtrDialog.setTitle("장소 이름을 입력하세요");
        final EditText DtrNameEditText = new EditText(getContext());
        DtrDialog.setView(DtrNameEditText);

        DtrDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DtrName = DtrNameEditText.getText().toString();
                dialogInterface.dismiss();

                markerOptions.title(DtrName);
                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;
                LatLng latLng = new LatLng(latitude, longitude);
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.acorn2));

                map.addMarker(markerOptions);

                dtrName.add(DtrName);
                dtrLatLng.add(latLng.toString());

                PolyPoints.add(latLng);
                drawPoly(map, PolyPoints);
            }
        });

        DtrDialog.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        DtrDialog.show();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(WriteMapPage.this).commit();
        fragmentManager.popBackStack();
    }


    class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            String message = "내 위치 -> Latitude : " + latitude + "\nLongitude : " + longitude;
            Log.d(TAG, message);
            //showCurrentLocation(latitude, longitude);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) { }

        @Override
        public void onProviderEnabled(String s) { }

        @Override
        public void onProviderDisabled(String s) { }
    }

   /* private void showCurrentLocation(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        showMyLocationMarker(curPoint);
    }*/

    private void showMyLocationMarker(LatLng curPoint) {
        if (markerOptions == null) {
            markerOptions = new MarkerOptions();
            markerOptions.position(curPoint);
            markerOptions.title("내 위치");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.squirrel_small));
            map.addMarker(markerOptions);
        } else {
            markerOptions.position(curPoint);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(getActivity(), requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, String[] permissions) {
        Log.d(TAG, "permissions denied : " + permissions.length);
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
        Log.d(TAG, "permissions granted : " + permissions.length);
    }

    public void backWritePage() {
        Toast.makeText(getContext(), "경로가 저장 되었습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), WriteRoadPage.class);
        intent.putExtra("dtrName", dtrName);
        intent.putExtra("dtrLatLng", dtrLatLng);
        startActivity(intent);
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