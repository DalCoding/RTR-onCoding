package com.example.rotory;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.rotory.Interface.LoadMapDtrListener;
import com.example.rotory.Interface.OnTabItemSelectedListener;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.VO.NearPin;
import com.example.rotory.account.SignUpActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class BigMapPage2 extends AppCompatActivity implements OnTabItemSelectedListener, LoadMapDtrListener {


    private static final String TAG = "BigMapPage";
    public static final int MainCode = 1000;
    public static final int ThemeCode = 2000;
    public static final int LoginCode = 2000;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Button thisBigMapBtn;
    ImageButton bigMapMyLocationBtn;
    //ViewGroup mapViewContainer;
    RelativeLayout container;
    Button mapPickThisRoadBtn;
    Animation tranlateUpAnim;
    Animation tranlateDownAnim;
    boolean isPageOpen = false;
    Button bigMapBackBtn;

    GoogleMap map;
    LocationManager manager;
   // GPSListener gpsListener;

    SupportMapFragment mapFragment;

    Marker myMarker;
    MarkerOptions myLocationMarker;
    MarkerOptions myLocationMarker1;

    // 하단탭
    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    RelativeLayout userAppbarContainer;

    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigation;
    Boolean isSignIn;


 /*   public static BigMapPage newInstance() {
        return new BigMapPage();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_map_page);

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저네임 : " + checkLogIN);
            isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
            isSignIn = false;
        }


        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("Map", "지도준비됨.");
                map = googleMap;
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
              //  map.setMyLocationEnabled(true);
                LatLng SeoulPoint = new LatLng(37.55626036672879, 126.97217466067063);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(SeoulPoint, 12));
            }
        });

       LocationManager manager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);// LocationManager 객체 참조하기
        // 이전에 확인햿던 위치 정보 가져오기

        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                //  String message = "최근 위치-> Latitude : " + latitude + "\nLongitude:" + longitude;
              //  LatLng curPoint = new LatLng(latitude, longitude);
             //   CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(curPoint);
             //   map.moveCamera(cameraUpdate);
            }

        GPSListener gpsListener = new GPSListener(); // 10초마다위치갱신되게끔
         long minTime = 10000000;
         float minDistance = 0;
         manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);


       bigMapBackBtn = findViewById(R.id.bigMapBackBtn);
       bigMapBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // this.removeAllViews();
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });


        // '내위치에서 찾기' 기능
        bigMapMyLocationBtn = findViewById(R.id.bigMapMyLocationBtn);
        bigMapMyLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsListener.onLocationChanged(location);
            }
        });

        // '이 지도에서 찾기' 기능  => 없어질것같음
        thisBigMapBtn = findViewById(R.id.thisBigMapBtn);
        thisBigMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //하단탭
        appBarLayout = findViewById(R.id.appBarLayout);
        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);


        bottomNavigation = findViewById(R.id.bottom_appBar);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                isSignIn = true;
                switch (item.getItemId()){
                    case R.id.home:
                       /* finish();
                        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        bottomNavigation.setVisibility(View.VISIBLE);
                        setTabUnderBar(0); */
                        return  true;

                    case R.id.theme:
                         if(isSignIn) {

                        Intent ThemeIntent= new Intent(getApplicationContext(), ThemePage.class);
                        startActivityForResult(ThemeIntent, ThemeCode);
                        bottomNavigation.setVisibility(View.VISIBLE);
                        setTabUnderBar(1);

                        } else  {

                            Intent LogInIntent= new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivityForResult(LogInIntent, LoginCode);
                            bottomNavigation.setVisibility(View.GONE);

                        }
                        return true;

                    case R.id.user:
                     if(isSignIn) {

                             Intent MyIntent= new Intent(getApplicationContext(), MyPage.class);
                             startActivityForResult(MyIntent, ThemeCode);
                             bottomNavigation.setVisibility(View.VISIBLE);
                             setTabUnderBar(2);
                        } else {

                            Intent LogInIntent= new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivityForResult(LogInIntent, LoginCode);
                            bottomNavigation.setVisibility(View.GONE);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    class GPSListener implements LocationListener { // 변화감지(위도, 경도)
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            String message = "내위치-> Latitude : "+ latitude + "\nLongitude:"+ longitude;
            Log.d("Map", message);
            showCurrentLocation(latitude, longitude); // 카메라움직여지도에띄우기
            LatLng curPoint = new LatLng(latitude, longitude);
            showMyLocationMarker(curPoint);
            loadDtr(curPoint);
            }

        private void showMyLocationMarker(LatLng curPoint) {
            if (myLocationMarker == null) {
                myLocationMarker = new MarkerOptions();
                myLocationMarker.position(curPoint);
            //    myLocationMarker.title("●내위치\n");
            //    myLocationMarker.snippet("●GPS로확인한위치");
                myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.squirrel3));
                map.addMarker(myLocationMarker);
            } else {
                myLocationMarker.position(curPoint);}

        }

        public void onProviderDisabled(String provider) { }
            public void onProviderEnabled(String provider) { }
            public void onStatusChanged(String provider, int status, Bundle extras) { }}
            private void showCurrentLocation(Double latitude, Double longitude) {
                LatLng curPoint = new LatLng(latitude, longitude); // 현재위치의좌표로LatLng 객체생성하기
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
            }
        // 지정한위치의지도영역보여주기(최대19~21까지자세히보여줄수있음)// showMyLocationMarker(curPoint);}
    @Override
    public void OnTabSelected(int position) {
        if(position == 0){
            bottomNavigation.setSelectedItemId(R.id.home);
        }else if(position == 1){
            bottomNavigation.setSelectedItemId(R.id.theme);
        }else if(position ==2){
            bottomNavigation.setSelectedItemId(R.id.user);
        }

    }

    public void setTabUnderBar(int position){
        if(position == 0){
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.VISIBLE);
            bottomNavUnderbarTheme.setVisibility(View.GONE);
            bottomNavUnderbarUser.setVisibility(View.GONE);
        }else if(position == 1){
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.GONE);
            bottomNavUnderbarTheme.setVisibility(View.VISIBLE);
            bottomNavUnderbarUser.setVisibility(View.GONE);
        }else if(position ==2){
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.GONE);
            bottomNavUnderbarTheme.setVisibility(View.GONE);
            bottomNavUnderbarUser.setVisibility(View.VISIBLE);
        }

    }

    // 구현 기능 : 1. 이지도에서찾기  2. 내위치에서 찾기  3. 줌레벨변경시 경로보이기  4. 도토리클릭시 하단팝업





    @Override
    public void loadDtr(LatLng point) {


        ArrayList<LatLng> manyPins = new ArrayList<LatLng>();
        //DB의 MapPoint 다 넣기 LatLng(latitude, longitude)
        manyPins.add(new LatLng(37.73512333583128, 127.06135012282921));
        manyPins.add(new LatLng(37.73651945074978, 127.0612405606333));
        manyPins.add(new LatLng(37.73617747243228, 127.06364545969836));


        for (int k=0; k<3; k++) {
            // DB에서 핀들의 정보 (이름, 하단팝업정보 등) 가져와야함)
            LatLng point1 = manyPins.get(k);
            myLocationMarker1 = new MarkerOptions();
            myLocationMarker1.position(point1);
            //    myLocationMarker.title("●내위치\n");
            //    myLocationMarker.snippet("●GPS로확인한위치");
            myLocationMarker1.icon(BitmapDescriptorFactory.fromResource(R.drawable.acorn2));
            map.addMarker(myLocationMarker1);

        }
    }

    @Override
    public void loadDtrLine(MapView mapView, ArrayList<MapPoint> Points) {

    }


        private class SlidingPageAnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {

        }
        public void onAnimationEnd(Animation animation){
            if(isPageOpen){
                container = findViewById(R.id.container);
                container.setVisibility(View.INVISIBLE);
                isPageOpen = false;

            }else{
               isPageOpen = true; } }
                @Override
                public void onAnimationRepeat(Animation animation) { } }


}


        // 기본마커 설정
        /*MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);

        // 커스텀마커 설정
      MapPOIItem customMarker = new MapPOIItem();
      //  customMarker.setItemName("Custom Marker");
       customMarker.setTag(1);

        mapView.addPOIItem(marker);

        mapView.addPOIItem(marker);


        // 커스텀마커 설정
        MapPOIItem customMarker = new MapPOIItem();
      //  customMarker.setItemName("Custom Marker");
        customMarker.setTag(1);
      //  customMarker.setMapPoint(MARKER_POINT);
        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        customMarker.setCustomImageResourceId(R.drawable.squirrel_small); // 마커 이미지.
        customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.



        mapView.addPOIItem(customMarker);  *//*

        mapView.addPOIItem(customMarker);
        mapView.addPOIItem(customMarker);*/


        //moveMyLocation(mapView);

        /*bigMapMyLocationBtn = findViewById(R.id.bigMapMyLocationBtn);
        bigMapMyLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveMyLocation(mapView);

            }
        });
    }*/

    /*public void moveMyLocation(MapView mapView){
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                // 3초 후에 현재위치를 받아오도록 설정 , 바로 시작 시 에러납니다.

                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
            }
        }, 2000); // 1000 = 1초
        // lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }


     */
/*   mapView.setMapViewEventListener(this);
        *//*mapView.setMapViewEventListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();

        }   *//*

        }*//*
        }*/






    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        mapViewContainer.removeAllViews();
    }

 */
/*   public void moveMyLocation() {

   public void moveMyLocation() {
        LocationManager manager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);// LocationManager 객체 참조하기
        try {   // 이전에 확인햿던 위치 정보 가져오기
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
              //  String message = "최근 위치-> Latitude : " + latitude + "\nLongitude:" + longitude;
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } */
