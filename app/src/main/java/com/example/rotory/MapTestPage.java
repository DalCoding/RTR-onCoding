package com.example.rotory;

import android.content.DialogInterface;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.VO.NearPin;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MapTestPage extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener {

    MapView mapView;
    ViewGroup mapViewContainer;
    Button writeMapAddBtn;
    // Button writeMapRemoveBtn;
    boolean MarkerExists = false;
    public static MapPoint mapPointNow;
    ArrayList<MapPoint> PolyPoints = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_map_page);

        MapView mapView = new MapView(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.writeMapContainer);
        mapViewContainer.addView(mapView);

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
      //  customMarker.setMapPoint(MARKER_POINT);
        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        customMarker.setCustomImageResourceId(R.drawable.squirrel_small); // 마커 이미지.
        customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        mapView.addPOIItem(customMarker); */
        mapView.setPOIItemEventListener(this);
        mapView.setMapViewEventListener(this);
        moveMyLocation(mapView);
        setManyPins(mapView);


    }

    public void setManyPins(MapView mapView) {
        // 표본넣기
        ArrayList<MapPoint> manyPins = new ArrayList<MapPoint>();
        manyPins.add(MapPoint.mapPointWithGeoCoord(37.54238496760114, 126.85109815011367));
        manyPins.add(MapPoint.mapPointWithGeoCoord(37.53890481231618, 126.82940817621092));
        manyPins.add(MapPoint.mapPointWithGeoCoord(37.540725844063566, 126.83696139065235)); // 기준값
        manyPins.add(MapPoint.mapPointWithGeoCoord(37.53077032753395, 126.83629793262709));
        manyPins.add(MapPoint.mapPointWithGeoCoord(37.5374074862284, 126.83588965076538));
        manyPins.add(MapPoint.mapPointWithGeoCoord(37.536914917126545, 126.8488438436127));
        manyPins.add(MapPoint.mapPointWithGeoCoord(37.53885740124661, 126.84935419593981));

        ArrayList<NearPin> nearPin = new ArrayList<NearPin>();
        // 근사값 배열 구하기
        for (int j = 0; j < manyPins.size(); j++) {

         /*   MapPoint Pin2 = manyPins.get(j);

            MapPOIItem customMarker4 = new MapPOIItem();
            customMarker4.setItemName("가까운핀"); // 이게 필수로 들어가야하는데 => 말풍선 안보이게 가능할듯
            customMarker4.setTag(7);
            customMarker4.setMapPoint(Pin2);
            customMarker4.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
            customMarker4.setCustomImageResourceId(R.drawable.acorn2); // 마커 이미지.
            customMarker4.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            customMarker4.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
            customMarker4.setShowCalloutBalloonOnTouch(false);
            mapView.addPOIItem(customMarker4);    // 여기까진 확인 OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO */


            MapPoint.GeoCoordinate latLng = manyPins.get(j).getMapPointGeoCoord();
            double latlat = latLng.latitude;
            double longlong = latLng.longitude;

            MapPoint target = MapPoint.mapPointWithGeoCoord(37.540725844063566, 126.83696139065235);//기준값
            MapPoint.GeoCoordinate latLng1 = target.getMapPointGeoCoord();
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
            String dist1 = Double.toString(dist);

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
        // String dist2 = nearPin.getKey();
        double dis111 = nearPin.get(0).getDistance();
        String dis1111 = Double.toString(dis111);
        double dis222 = nearPin.get(1).getDistance();
        String dis2222 = Double.toString(dis222);
        double dis333 = nearPin.get(2).getDistance();
        String dis3333 = Double.toString(dis333);
        double dis444 = nearPin.get(3).getDistance();
        String dis4444 = Double.toString(dis444);

        Log.d("배열", dis1111 + " / " + dis2222 + " / " + dis3333 + " / " + dis4444);


        //표본 2개만 핀 박기

        MapPoint Pin1 = nearPin.get(0).getPoint();
        MapPOIItem customMarker3 = new MapPOIItem();
        customMarker3.setItemName("가까운핀"); // 이게 필수로 들어가야하는데 => 말풍선 안보이게 가능할듯
        customMarker3.setTag(7);
        customMarker3.setMapPoint(Pin1);
        customMarker3.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        customMarker3.setCustomImageResourceId(R.drawable.acorn2); // 마커 이미지.
        customMarker3.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker3.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        customMarker3.setShowCalloutBalloonOnTouch(false);
        mapView.addPOIItem(customMarker3);

        MapPoint Pin2 = nearPin.get(1).getPoint();
        MapPOIItem customMarker4 = new MapPOIItem();
        customMarker4.setItemName("가까운핀1"); // 이게 필수로 들어가야하는데 => 말풍선 안보이게 가능할듯
        customMarker4.setTag(7);
        customMarker4.setMapPoint(Pin2);
        customMarker4.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        customMarker4.setCustomImageResourceId(R.drawable.acorn2); // 마커 이미지.
        customMarker4.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker4.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        customMarker4.setShowCalloutBalloonOnTouch(false);
        mapView.addPOIItem(customMarker4);


        //NearPin nearPin1 = new NearPin(dist, manyPins.get(j));

            /* LinkedHashMap<Double, MapPoint> selectPin = new LinkedHashMap<Double, MapPoint>();
            selectPin.put(dist, manyPins.get(j));  // 해시맵에 (거리, MapPoint) 넣었음
            Object[] mapkey = selectPin.keySet().toArray(); // 키값에 따라 정렬
            Arrays.sort(mapkey);


                for (Map.Entry<Double, MapPoint> elem : selectPin.entrySet()) {

                    String key = Double.toString(elem.getKey());

                    MapPoint.GeoCoordinate value = elem.getValue().getMapPointGeoCoord();
                    double value1 = value.latitude;  String value11 = Double.toString(value1);
                    double value2 = value.longitude;  String value22 = Double.toString(value2);
                    //String value1 = MapPoint.toString(elem.getValue());

                    Log.d("해시맵", "거리: " + key + "/ 좌표: " + value11 + " + " + value22 );

                } */

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


    public void showDtrDialog(MapView mapView, MapPoint mapPoint){

        AlertDialog.Builder DtrDialog = new AlertDialog.Builder(this);
        DtrDialog.setTitle("장소 이름을 입력하세요");       // 제목 설정
        // EditText 삽입하기
        final EditText DtrNameEditText = new EditText(this);
        DtrDialog.setView(DtrNameEditText);

        // 확인 버튼 설정
        DtrDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Text 값 받아서 처리
                String DtrName = DtrNameEditText.getText().toString();
                dialog.dismiss();     //닫기
                //addDtr();
                // 도토리 추가
                MapPOIItem customMarker2 = new MapPOIItem();
                customMarker2.setItemName(DtrName); // 이게 필수로 들어가야하는데 => 말풍선 안보이게 가능할듯
                customMarker2.setTag(6);
                customMarker2.setMapPoint(mapPoint);
                customMarker2.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                customMarker2.setCustomImageResourceId(R.drawable.acorn2); // 마커 이미지.
                customMarker2.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                customMarker2.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
                customMarker2.setShowCalloutBalloonOnTouch(false);
              //  customMarker2.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

                mapView.addPOIItem(customMarker2);
                // 이미 만들어진 콘텐츠id 에다가 집어넣음

                PolyPoints.add(mapPoint);
                drawPoly(mapView, PolyPoints);

            }
        });

      /*  DtrDialog.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Text 값 받아서 처리
                String DtrName = DtrNameEditText.getText().toString();
                dialog.dismiss();     //닫기

            }
        }); */

        DtrDialog.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Text 값 받아서 처리
                dialog.dismiss();     //닫기

            }
        });

        DtrDialog.show();
    }


/*    public void showDtrDialog1(MapView mapView, MapPOIItem mapPOIItem){

        AlertDialog.Builder DtrDialog = new AlertDialog.Builder(this);
        DtrDialog.setTitle("장소 이름을 입력하세요");       // 제목 설정
        // EditText 삽입하기
        final EditText DtrNameEditText2 = new EditText(this);
        String ExistName = mapPOIItem.getItemName();
        DtrNameEditText2.setText(ExistName);
        DtrDialog.setView(DtrNameEditText2);

        // 확인 버튼 설정
        DtrDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    // Text 값 받아서 처리
                    String DtrName = DtrNameEditText2.getText().toString();
                    dialog.dismiss();     //닫기
                    //addDtr();
                    // 도토리 추가
                    MapPOIItem customMarker3 = new MapPOIItem();
                    customMarker3.setItemName(DtrName); // 이게 필수로 들어가야하는데 => 말풍선 안보이게 가능할듯
                    customMarker3.setTag(7);
                    MapPoint mapPoint = mapPOIItem.getMapPoint();
                    customMarker3.setMapPoint(mapPoint);
                    customMarker3.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                    customMarker3.setCustomImageResourceId(R.drawable.acorn2); // 마커 이미지.
                    customMarker3.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                    customMarker3.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
                    customMarker3.setShowCalloutBalloonOnTouch(false);

                    mapView.addPOIItem(customMarker3);

            }
        });

        DtrDialog.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Text 값 받아서 처리
                String DtrName = DtrNameEditText.getText().toString();
                dialog.dismiss();     //닫기

            }
        });

        DtrDialog.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Text 값 받아서 처리
                dialog.dismiss();     //닫기

            }
        });

        DtrDialog.show();
    }  */

    public void drawPoly(MapView mapView, ArrayList<MapPoint> PolyPoints){

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapViewContainer.removeAllViews();
    }

 /*   public void moveMyLocation() {
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

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

      /*  MapPOIItem marker = new MapPOIItem();
       marker.setItemName("Default Marker");
        marker.setTag(5);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.
        //marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker); */

        if(MarkerExists) {
            MapPOIItem ExistItem = mapView.findPOIItemByTag(5);
            mapView.removePOIItem(ExistItem);
            MarkerExists=false;
            writeMapAddBtn = findViewById(R.id.writeMapAddBtn);
            writeMapAddBtn.setVisibility(View.INVISIBLE);

        } else {
            MapPOIItem customMarker = new MapPOIItem();
            customMarker.setItemName(""); // 이게 필수로 들어가야하는데 => 말풍선 안보이게 가능할듯
            customMarker.setTag(5);
            customMarker.setMapPoint(mapPoint);
            customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
            customMarker.setCustomImageResourceId(R.drawable.acorn2); // 마커 이미지.
            customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
            customMarker.setShowCalloutBalloonOnTouch(false);
        //    customMarker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);
            mapView.addPOIItem(customMarker);

            MarkerExists=true;
            writeMapAddBtn = findViewById(R.id.writeMapAddBtn);
            writeMapAddBtn.setVisibility(View.VISIBLE);


        }

        writeMapAddBtn = findViewById(R.id.writeMapAddBtn);
        writeMapAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDtrDialog(mapView, mapPoint);
            }
        });
        //  writeMapRemoveBtn = findViewById(R.id.writeMapRemoveBtn);
        //  writeMapRemoveBtn.setVisibility(View.INVISIBLE);

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


        // showDtrDialog1(mapView, mapPOIItem);
        String DtrName = mapPOIItem.getItemName();
        Toast.makeText(getApplicationContext(), DtrName, Toast.LENGTH_SHORT).show();

     /*  삭제버튼
        writeMapAddBtn = findViewById(R.id.writeMapAddBtn);
        writeMapAddBtn.setVisibility(View.INVISIBLE);
        writeMapRemoveBtn = findViewById(R.id.writeMapRemoveBtn);
        writeMapRemoveBtn.setVisibility(View.VISIBLE);
        writeMapRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.removePOIItem(mapPOIItem);
            }
        });  */


        // String DtrName = mapPOIItem.getItemName();
        // showDtrDialog(DtrName);


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
}




