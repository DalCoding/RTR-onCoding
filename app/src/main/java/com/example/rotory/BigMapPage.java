
package com.example.rotory;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.Interface.LoadMapDtrListener;
import com.example.rotory.Interface.OnTabItemSelectedListener;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.VO.NearPin;
import com.example.rotory.account.SignUpActivity;
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


public class BigMapPage extends AppCompatActivity implements OnTabItemSelectedListener, MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener {



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

        MapView mapView = new MapView(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.bigMapLayout);
        mapViewContainer.addView(mapView);
        mapView.setPOIItemEventListener(this);
        mapView.setMapViewEventListener(this);

        moveMyLocation(mapView);

       bigMapBackBtn = findViewById(R.id.bigMapBackBtn);
       bigMapBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // this.removeAllViews();
                mapViewContainer.removeView(mapView);
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
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                MapPoint centerPoint = mapView.getMapCenterPoint();
                loadDtr(mapView, centerPoint);

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        // 3초 후에 현재위치를 받아오도록 설정 , 바로 시작 시 에러납니다.

                        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                    }
                }, 2000); // 1000 = 1초
            }
        });

        // '이 지도에서 찾기' 기능
        thisBigMapBtn = findViewById(R.id.thisBigMapBtn);
        thisBigMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapPoint centerPoint = mapView.getMapCenterPoint();
                loadDtr(mapView, centerPoint);

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


    public void loadDtr(MapView mapView, MapPoint point) {
        int zoomLevel = mapView.getZoomLevel();
        if (zoomLevel <= 0){

            // 모든 경로 표시

      //  } else {
            ArrayList<MapPoint> manyPins = new ArrayList<MapPoint>();
            //DB의 MapPoint 다 넣기
            manyPins.add(MapPoint.mapPointWithGeoCoord(37.73512333583128, 127.06135012282921));
            manyPins.add(MapPoint.mapPointWithGeoCoord(37.73651945074978, 127.0612405606333));
            manyPins.add(MapPoint.mapPointWithGeoCoord(37.73617747243228, 127.06364545969836));
            // manyPins.add(MapPoint.mapPointWithGeoCoord(37.734617832068224, 127.06367895894984));

            ArrayList<NearPin> nearPin = new ArrayList<NearPin>();
            // 근사값 배열 구하기
            for (int j = 0; j < manyPins.size(); j++) {
                //DB의 MapPoint를 위경도로 반환
                MapPoint.GeoCoordinate latLng = manyPins.get(j).getMapPointGeoCoord();
                double latlat = latLng.latitude;
                double longlong = latLng.longitude;

                //기준값
                MapPoint.GeoCoordinate latLng1 = point.getMapPointGeoCoord();
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
            ArrayList<MapPoint> PolyPoints = new ArrayList<>();

            // 가까운 핀들 맵뷰에 박기
               for (int k=0; k<2; k++) {
            // DB에서 핀들의 정보 (이름, 하단팝업정보 등) 가져와야함)
            MapPoint Pin = nearPin.get(k).getPoint();
            MapPOIItem customMarker3 = new MapPOIItem();
            customMarker3.setItemName("DB정보"); // 이게 필수로 들어가야하는데 => 말풍선 안보이게 가능할듯
            customMarker3.setTag(3);
            customMarker3.setMapPoint(Pin);
            customMarker3.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
            customMarker3.setCustomImageResourceId(R.drawable.acorn2); // 마커 이미지.
            customMarker3.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            customMarker3.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
            customMarker3.setShowCalloutBalloonOnTouch(false);
            mapView.addPOIItem(customMarker3);
            PolyPoints.add(Pin);

              }

               loadDtrLine(mapView, PolyPoints);
        } else {
            mapView.removeAllPOIItems();
            mapView.removeAllPolylines();
        }
    }


    public void loadDtrLine(MapView mapView, ArrayList<MapPoint> PolyPoints) {

        MapPolyline polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

        // Polyline 좌표 지정.
        for (int i=0; i < PolyPoints.size(); i++) {
            MapPoint PolyPoint = PolyPoints.get(i);
            polyline.addPoint(PolyPoint);
        }

// Polyline 지도에 올리기.
        mapView.addPolyline(polyline);
    }



    public void moveMyLocation (MapView mapView){

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

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

        MapPoint centerPoint = mapView.getMapCenterPoint();
        loadDtr(mapView, centerPoint);

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

        tranlateUpAnim = AnimationUtils.loadAnimation(this,R.anim.translate_up);
        tranlateDownAnim = AnimationUtils.loadAnimation(this,R.anim.translate_down);

        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        tranlateUpAnim.setAnimationListener(animListener);
        tranlateDownAnim.setAnimationListener(animListener);

        container = findViewById(R.id.container);
        container.startAnimation(tranlateDownAnim);
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

        // DB에서 도토리 정보 받아서 넣기

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
                Intent intent = new Intent(getApplicationContext(), RoadContentsPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

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
