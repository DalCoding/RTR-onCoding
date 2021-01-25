package com.example.rotory;

import android.content.DialogInterface;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Adapter.LocationAdapter;
import com.example.rotory.WriteContents.WriteRoadPage;
import com.example.rotory.kakao.ApiClient;
import com.example.rotory.kakao.ApiInterface;
import com.example.rotory.kakao.BusProvider;
import com.example.rotory.kakao.CategoryResult;
import com.example.rotory.kakao.Document;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.otto.Bus;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.net.URLEncoder;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WriteMapPage extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.POIItemEventListener {
    private static final String TAG = "WriteMapPage";

    Bus bus = BusProvider.getInstance();


    ArrayList<String> dtrName = new ArrayList<>();
    ArrayList<String> dtrLatLng = new ArrayList<>();

    MapView mapView;
    ViewGroup mapViewContainer;
    Button writeMapAddBtn;
    boolean MarkerExists = false;
    ArrayList<MapPoint> PolyPoints = new ArrayList<>();
    ArrayList<Document> documentArrayList = new ArrayList<>();
    EditText mapSearchEditText;

    String DtrName;

    private double searchLng = -1;
    private double searchLat = -1;
    private String searchName;

    RecyclerView recyclerView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_map_page);
        bus.register(this);

        mapView = new MapView(this);

        mapSearchEditText = findViewById(R.id.writeMapSearchEditText);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.writeMapContainer);
        mapViewContainer.addView(mapView);
        recyclerView = findViewById(R.id.searchLocationRecyclerView);
        LocationAdapter locationAdapter = new LocationAdapter(documentArrayList, getApplicationContext(),
                mapSearchEditText, recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(locationAdapter);


        mapSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2){
                    documentArrayList.clear();
                    locationAdapter.clear();
                    locationAdapter.notifyDataSetChanged();
                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<CategoryResult> call = null;

                        call = apiInterface.getSearchLocation(getString(R.string.restapi_key),s.toString(), 15);

                    Log.d(TAG,URLEncoder.encode(s.toString()));
                    call.enqueue(new Callback<CategoryResult>() {
                        @Override
                        public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {
                            Log.d(TAG,response.toString());
                            if (response.isSuccessful()){
                                Log.d(TAG,"응답 받기 성공");
                                Log.d(TAG, response.body().getDocuments().toString());
                                assert response.body() != null;
                                for (Document document : response.body().getDocuments()) {
                                    locationAdapter.addItem(document);
                                    searchName = document.getPlaceName();
                                    searchLng = Double.parseDouble(document.getX());
                                    searchLat = Double.parseDouble(document.getY());
                                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(searchLat, searchLng), true);
                                }
                                locationAdapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onFailure(Call<CategoryResult> call, Throwable t) {
                            Log.d(TAG, "텍스트 변화 인지 실패");
                        }
                    });
                }else {
                    if (s.length() <= 0){
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mapSearchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    // Log.d(TAG,"Focus changeListener On : 검색창 누름 인식");
                }else {
                    // Log.d(TAG,"Focus changeListener Off : 검색창 꺼짐");
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

        mapView.setPOIItemEventListener(this);
        mapView.setMapViewEventListener(this);
        moveMyLocation(mapView);

    }


    public void moveMyLocation(MapView mapView){

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
                DtrName = DtrNameEditText.getText().toString();
                dialog.dismiss();     //닫기

                // 도토리 추가
                MapPOIItem dtrMarker = new MapPOIItem();
                dtrMarker.setItemName(DtrName);
                dtrMarker.setTag(6);
                dtrMarker.setMapPoint(mapPoint);
                dtrMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                dtrMarker.setCustomImageResourceId(R.drawable.acorn2);
                dtrMarker.setCustomImageAutoscale(false);
                dtrMarker.setCustomImageAnchor(0.5f, 1.0f);
                dtrMarker.setShowCalloutBalloonOnTouch(false);
                mapView.addPOIItem(dtrMarker);

                dtrName.add(DtrName);
                dtrLatLng.add(mapPoint.toString());

                PolyPoints.add(mapPoint);
                drawPoly(mapView, PolyPoints);

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
    }

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

        recyclerView.setVisibility(View.GONE);

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

        String dtrName = mapPOIItem.getItemName();
        Toast.makeText(this, dtrName, Toast.LENGTH_SHORT).show();

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

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "경로가 저장 되었습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(WriteMapPage.this, WriteRoadPage.class);
        intent.putStringArrayListExtra("dtrName", dtrName);
        intent.putStringArrayListExtra("dtrLatLng", dtrLatLng);
        startActivityForResult(intent, 0);
    }
}




