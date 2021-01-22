package com.example.rotory;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WriteRoadMapPage extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private FragmentActivity context;
    private static final String TAG = "WriteRoadMapPage";
    private MapView mapView = null;
    private Marker currentMarker = null;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Location currentLocation;

    private final LatLng defaultLocation = new LatLng(37.56, 126.97);
    private static final int default_zoom = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000 * 60 * 1;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 1000 * 30;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    public WriteRoadMapPage() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            CameraPosition cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        View view = inflater.inflate(R.layout.write_map_page, container, false);
        mapView = (MapView) view.findViewById(R.id.writeMapContainer);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MapsInitializer.initialize(context);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setDefaultLocation();
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }

        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                currentLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    private void setDefaultLocation() {
        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(defaultLocation);
        markerOptions.title("위치정보 가져올 수 없음");
        markerOptions.snippet("위치 퍼미션과 GPS 활성 여부 확인하세요.");
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = map.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(defaultLocation, 15);
        map.moveCamera(cameraUpdate);
    }

    String getCurrentAddress(LatLng latLng) {
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            Toast.makeText(context, "위치로부터 주소를 인식할 수 없습니다. 네트워크가 연결되어 있는지 확인하세요.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return "주소 인식 불가";
        }

        if (addressList.size() < 1) {
            return "해당 위치에 주소 없음";
        }

        Address address = addressList.get(0);
        StringBuilder addressStringBuilder = new StringBuilder();
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            addressStringBuilder.append(address.getAddressLine(i));
            if (i < address.getMaxAddressLineIndex())
                addressStringBuilder.append("\n");
        }

        return addressStringBuilder.toString();
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                Location location = locationList.get(locationList.size() - 1);

                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude()) + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "Time :" + CurrentTime() + " onLocationResult : " + markerSnippet);

                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);
                currentLocation = location;
            }
        }
    };

    private String CurrentTime() {
        Date today = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
        return time.format(today);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if (currentMarker != null) currentMarker.remove();

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        currentMarker = map.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        map.moveCamera(cameraUpdate);
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }

        updateLocationUI();
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        if (fusedLocationProviderClient != null) {
            Log.d(TAG, "onStop : removeLocationUpdates");
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        /*if (locationPermissionGranted) {
            Log.d(TAG, "onResume : requestLocationUpdates");
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            if (map != null) {
                mapView.setMyLocationEnabled(true);
            }
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (fusedLocationProviderClient != null) {
            Log.d(TAG, "onDestroyView : removeLocationUpdates");
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


}
