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
import com.example.rotory.userActivity.Scrap;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class BigMapPage extends AppCompatActivity implements OnTabItemSelectedListener, LoadMapDtrListener {


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
    Marker Marker1;
    Marker Marker2;
    Marker MarkerSelected;
    MarkerOptions MarkerOps1;
    MarkerOptions MarkerOps2;
    MarkerOptions MarkerSelectedOps;

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

    @SuppressLint("MissingPermission")
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
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(SeoulPoint, 13));
                map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener(){

                    @Override
                    public void onCameraIdle() {
                        LatLng center = map.getCameraPosition().target;   // 중앙점 https://stackoverflow.com/questions/13904505/how-to-get-center-of-map-for-v2-android-maps
                        loadDtr(center);
                      int zoomLevel = (int) map.getCameraPosition().zoom;
                      if(zoomLevel >= 18) {
                        loadDtrLine(center);
                      } else {
                          loadDtr(center);
                      }

                    }
                });
                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        showDtrInfo(marker);
                        return true;
                    }
                });
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        tranlateUpAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_up);
                        tranlateDownAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_down);

                        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
                        tranlateUpAnim.setAnimationListener(animListener);
                        tranlateDownAnim.setAnimationListener(animListener);

                        container = findViewById(R.id.container);
                        container.startAnimation(tranlateDownAnim);
                    }
                });
            }
        });

        LocationManager manager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);// LocationManager 객체 참조하기
        // 이전에 확인햿던 위치 정보 가져오기
        String locationProvider = LocationManager.NETWORK_PROVIDER;
     //   Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

     /*   if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            //  String message = "최근 위치-> Latitude : " + latitude + "\nLongitude:" + longitude;
            //  LatLng curPoint = new LatLng(latitude, longitude);
            //   CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(curPoint);
            //   map.moveCamera(cameraUpdate);
        } */

        GPSListener gpsListener = new GPSListener(); // 10초마다위치갱신되게끔
        long minTime = 1000;
        float minDistance = 0;
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, gpsListener);
       // manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                // 3초 후에 현재위치를 받아오도록 설정 , 바로 시작 시 에러납니다.

                Location location = manager.getLastKnownLocation(locationProvider);

                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();
                String message = "내위치-> Latitude : "+ latitude + "\nLongitude:"+ longitude;
                Log.d("Map", message);

                showCurrentLocation(latitude, longitude); // 카메라움직여지도에띄우기
                LatLng curPoint = new LatLng(latitude, longitude);
                showMyLocationMarker(); // 현재위치 보여주기
                loadDtr(curPoint); // 도토리 보여주기
            }
        }, 2000); // 1000 = 1초


        bigMapBackBtn = findViewById(R.id.bigMapBackBtn);
        bigMapBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this.removeAllViews();
                finish();

            }
        });


        // '내위치에서 찾기' 기능
        bigMapMyLocationBtn = findViewById(R.id.bigMapMyLocationBtn);
        bigMapMyLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = manager.getLastKnownLocation(locationProvider);
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();
                showCurrentLocation(latitude, longitude);
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
                switch (item.getItemId()) {
                    case R.id.home:
                       /* finish();
                        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        bottomNavigation.setVisibility(View.VISIBLE);
                        setTabUnderBar(0); */
                        return true;

                    case R.id.theme:
                        if (isSignIn) {

                            Intent ThemeIntent = new Intent(getApplicationContext(), ThemePage.class);
                            startActivityForResult(ThemeIntent, ThemeCode);
                            bottomNavigation.setVisibility(View.VISIBLE);
                            setTabUnderBar(1);

                        } else {

                            Intent LogInIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivityForResult(LogInIntent, LoginCode);
                            bottomNavigation.setVisibility(View.GONE);

                        }
                        return true;

                    case R.id.user:
                        if (isSignIn) {

                            Intent MyIntent = new Intent(getApplicationContext(), MyPage.class);
                            startActivityForResult(MyIntent, ThemeCode);
                            bottomNavigation.setVisibility(View.VISIBLE);
                            setTabUnderBar(2);
                        } else {

                            Intent LogInIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivityForResult(LogInIntent, LoginCode);
                            bottomNavigation.setVisibility(View.GONE);
                        }
                        return true;
                }
                return false;
            }
        });




    }

    private void showDtrInfo(Marker marker) {

        // DB에서 도토리 정보 받아서 넣기
        LatLng point2 = marker.getPosition();
        MarkerSelectedOps = new MarkerOptions();
        MarkerSelectedOps.position(point2);
        //    myLocationMarker.title("●내위치\n");
        //    myLocationMarker.snippet("●GPS로확인한위치");
        MarkerSelectedOps.icon(BitmapDescriptorFactory.fromResource(R.drawable.acornlined2));
        MarkerSelected = map.addMarker(MarkerSelectedOps);
      //  marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.acorn2));

        tranlateUpAnim = AnimationUtils.loadAnimation(this,R.anim.translate_up);
        tranlateDownAnim = AnimationUtils.loadAnimation(this,R.anim.translate_down);

        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        tranlateUpAnim.setAnimationListener(animListener);
        tranlateDownAnim.setAnimationListener(animListener);

        container = findViewById(R.id.container);
        container.startAnimation(tranlateUpAnim);
        container.setVisibility(View.VISIBLE);

        mapPickThisRoadBtn = findViewById(R.id.mapPickThisRoadBtn);
        mapPickThisRoadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*      Intent intent = new Intent(getApplicationContext(), RoadContentsPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent); */
            }
        });
    }

    public void showMyLocationMarker() {
        LocationManager manager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);// LocationManager 객체 참조하기
        // 이전에 확인햿던 위치 정보 가져오기
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        @SuppressLint("MissingPermission") Location location = manager.getLastKnownLocation(locationProvider);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            //  String message = "최근 위치-> Latitude : " + latitude + "\nLongitude:" + longitude;
            //  LatLng curPoint = new LatLng(latitude, longitude);
            //   CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(curPoint);
            //   map.moveCamera(cameraUpdate);
            LatLng curPoint = new LatLng(latitude, longitude);

                myLocationMarker = new MarkerOptions();
                myLocationMarker.position(curPoint);
                //    myLocationMarker.title("●내위치\n");
                //    myLocationMarker.snippet("●GPS로확인한위치");
                myLocationMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.squirrel3));
                map.addMarker(myLocationMarker);

    }



    static class GPSListener implements LocationListener { // 변화감지(위도, 경도)
        public void onLocationChanged(Location location) {
           /* Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            String message = "내위치-> Latitude : "+ latitude + "\nLongitude:"+ longitude;
            Log.d("Map", message);
            showCurrentLocation(latitude, longitude); // 카메라움직여지도에띄우기
            LatLng curPoint = new LatLng(latitude, longitude);
            showMyLocationMarker(); // 현재위치 보여주기
            loadDtr(curPoint); // 도토리 보여주기 */
            }

        public void onProviderDisabled(String provider) { }
            public void onProviderEnabled(String provider) { }
            public void onStatusChanged(String provider, int status, Bundle extras) { }}

            private void showCurrentLocation(Double latitude, Double longitude) {
                LatLng curPoint = new LatLng(latitude, longitude); // 현재위치의좌표로LatLng 객체생성하기
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 16));
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



    // 첫 도토리만 로딩
    @Override
    public void loadDtr(LatLng point) {

        db.collection("contents")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String personId = document.getId();
                        ArrayList<LatLng> dtrLatLng = (ArrayList<LatLng>) document.get("dtrLatLng");
                       // HashMap<String, Object> =

                        dtrLatLng.get(0);



                    }
                }
            }
        });

       map.clear();
       showMyLocationMarker();

        ArrayList<LatLng> manyPins = new ArrayList<LatLng>();
        //DB의 MapPoint 다 넣기 LatLng(latitude, longitude)
        manyPins.add(new LatLng(37.73512333583128, 127.06135012282921));
        manyPins.add(new LatLng(37.73651945074978, 127.0612405606333));
        manyPins.add(new LatLng(37.73617747243228, 127.06364545969836));
        manyPins.add(new LatLng( 37.7352838937455, 127.06131688927474));
        manyPins.add(new LatLng(37.735869347144586, 127.0617996868873));
        manyPins.add(new LatLng( 37.736997816721335, 127.06188551757396));

        ArrayList<NearPin> nearPin = new ArrayList<NearPin>();
        // 근사값 배열 구하기
        for (int j = 0; j < manyPins.size(); j++) {
            //DB의 MapPoint를 위경도로 반환
            LatLng latLng = manyPins.get(j);
            double latlat = latLng.latitude;
            double longlong = latLng.longitude;

            //기준값
            LatLng latLng1 = point;
            double latlat1 = latLng1.latitude;
            double longlong1 = latLng1.longitude;

            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(latlat - latlat1);
            double dLng = Math.toRadians(longlong - longlong1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(latlat1)) * Math.cos(Math.toRadians(latlat)) *
                            Math.sin(dLng / 2) * Math.sin(dLng / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double dist = (double) (earthRadius * c);
            // String dist1 = Double.toString(dist);
            //  Log.d ("배열", dist1);  // 여기까진 확인 OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
            // double + MapPoint형 배열
            nearPin.add(new NearPin(dist, manyPins.get(j)));
        }

        Collections.sort(nearPin, new Comparator<NearPin>() {
            @Override
            public int compare(NearPin o1, NearPin o2) {
                if (o1.getDistance() == o2.getDistance()) {
                    return 0;
                } else if (o1.getDistance() < o2.getDistance()) {
                    return -1;
                } else {
                    return 1;
                }

            }
        });


        for (int k=0; k<4; k++) {
            // DB에서 핀들의 정보 (이름, 하단팝업정보 등) 가져와야함)
            LatLng point1 = nearPin.get(k).getPoint();
            MarkerOps1 = new MarkerOptions();
            MarkerOps1.position(point1);
            //    myLocationMarker.title("●내위치\n");
            //    myLocationMarker.snippet("●GPS로확인한위치");
            MarkerOps1.icon(BitmapDescriptorFactory.fromResource(R.drawable.acorn2));
            Marker1 = map.addMarker(MarkerOps1);

        }
    }

    // 도토리 경로들 + 선 로딩
    @Override
    public void loadDtrLine(LatLng point) {

        map.clear();
        showMyLocationMarker();

        ArrayList<LatLng> manyPins = new ArrayList<LatLng>();
        //DB의 MapPoint 다 넣기 LatLng(latitude, longitude)
        manyPins.add(new LatLng(37.73512333583128, 127.06135012282921));
        manyPins.add(new LatLng(37.736145391876796, 127.0614203671098));
        manyPins.add(new LatLng(37.73680720072044, 127.06154911313982));
        manyPins.add(new LatLng( 37.73592478761425, 127.06283657343994));
        manyPins.add(new LatLng(37.7362132699792, 127.06116287504977));
        manyPins.add(new LatLng( 37.73567872823825, 127.06111995970643));

        ArrayList<NearPin> nearPin = new ArrayList<NearPin>();
        // 근사값 배열 구하기
        for (int j = 0; j < manyPins.size(); j++) {
            //DB의 MapPoint를 위경도로 반환
            LatLng latLng = manyPins.get(j);
            double latlat = latLng.latitude;
            double longlong = latLng.longitude;

            //기준값
            LatLng latLng1 = point;
            double latlat1 = latLng1.latitude;
            double longlong1 = latLng1.longitude;

            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(latlat - latlat1);
            double dLng = Math.toRadians(longlong - longlong1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(latlat1)) * Math.cos(Math.toRadians(latlat)) *
                            Math.sin(dLng / 2) * Math.sin(dLng / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double dist = (double) (earthRadius * c);
            // String dist1 = Double.toString(dist);
            //  Log.d ("배열", dist1);  // 여기까진 확인 OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
            // double + MapPoint형 배열
            nearPin.add(new NearPin(dist, manyPins.get(j)));
        }

        Collections.sort(nearPin, new Comparator<NearPin>() {
            @Override
            public int compare(NearPin o1, NearPin o2) {
                if (o1.getDistance() == o2.getDistance()) {
                    return 0;
                } else if (o1.getDistance() < o2.getDistance()) {
                    return -1;
                } else {
                    return 1;
                }

            }
        });

        ArrayList<LatLng> PolyPoints = new ArrayList<>();

        for (int k=0; k<4; k++) {
            // DB에서 핀들의 정보 (이름, 하단팝업정보 등) 가져와야함)
            LatLng point1 = nearPin.get(k).getPoint();
            MarkerOps2 = new MarkerOptions();
            MarkerOps2.position(point1);
            //    myLocationMarker.title("●내위치\n");
            //    myLocationMarker.snippet("●GPS로확인한위치");
            MarkerOps2.icon(BitmapDescriptorFactory.fromResource(R.drawable.acorn2));
            Marker2 = map.addMarker(MarkerOps2);
            PolyPoints.add(point1);

        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.argb(128, 255, 51, 0))
                        .width(5)
                        .geodesic(true);

        for(int l=0; l<PolyPoints.size(); l++) {
            polylineOptions
                    .add(PolyPoints.get(l));
        }

        Polyline polyline = map.addPolyline(polylineOptions);

    }


        private class SlidingPageAnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {
            if(isPageOpen){
                MarkerSelected.remove();
            }

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





