package com.example.rotory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.LoadMapDtrListener;
import com.example.rotory.VO.Contents;
import com.example.rotory.VO.NearPin;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class MainPage extends Fragment
        //implements MapView.MapViewEventListener
{
    final static String TAG = "MainPage";

    Button mainFloatingBtn;
    Button mainSearchBtn;
    EditText mainSearchEdit;

    RecyclerView mainRoadList;
    Button mainStoryNextBtn;
    RecyclerView mainStoryList;
    Button mainRoadNextBtn;

    FrameLayout mainMapLayout;
    Button mainMapExtendBtn;

    private Context context;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    Button popFloatingBtn;
    Button pop2FloatingBtn;


    private FirestoreRecyclerAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //MapView mapView;
   // ViewGroup rootView;

    @Override
    public void onStop() {
        super.onStop();
        getActivity().finish();

    }



/*
    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    } */

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity)
            context = (Activity)context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_page, container, false);
        FirebaseUser user = mAuth.getCurrentUser();

           initUI(rootView);

        return rootView;

    }

    private void setContentView(int main_page) {
    }

    // public void showWrite(){}

   /* public void onContentsListener (contentsAdapter.ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onContentsListener(holder, view, position);
        }
    }*/


    private void initUI(ViewGroup rootView) {

        //mapView = new MapView(getActivity());
        ViewGroup mapViewContainer = (ViewGroup) rootView.findViewById(R.id.mainMapLayout);
        //mapViewContainer.addView(mapView);

        // mapView.setPOIItemEventListener(this);
        //mapView.setMapViewEventListener(this);

        // moveMyLocation(mapView);

        setContentView(R.layout.main_page);


        // 플로팅버튼 참고 https://re-build.tistory.com/31
<<<<<<< HEAD
      /*  Context = new context(getActivity());

        fab_open = AnimationUtils.loadAnimation(Context, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(Context, R.anim.fab_open); */
=======
<<<<<<< HEAD
        /*Context = new context(getActivity());

        fab_open = AnimationUtils.loadAnimation(Context, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(Context, R.anim.fab_open);*/
=======
        /*Context = new Context(getActivity());

        fab_open = AnimationUtils.loadAnimation(Context, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(Context, R.anim.fab_open);*/

        Context context = getActivity().getApplicationContext();
>>>>>>> 2843abc6325c5bf84df8ca8e0a1099003dd3454c

        fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(context, R.anim.fab_open);
>>>>>>> 10c85c702a1f544ffad32b9f8844231a43a94482

      /*  mainFloatingBtn = rootView.findViewById(R.id.mainFloatingBtn);
        popFloatingBtn = rootView.findViewById(R.id.popFloatingBtn);
        pop2FloatingBtn = rootView.findViewById(R.id.pop2FloatingBtn);

        mainFloatingBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });
        popFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
                showToast("이야기 작성 페이지로 이동합니다.");
            }
        });

        pop2FloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
                showToast("이야기 작성 페이지로 이동합니다.");
            }

        });
*/
        /*Button button = rootView.findViewById(R.id.mainFloatingBtn);
        button.setOnClickListener(new View.OnClickListener(){*/
/*            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.mainFloatingBtn:
                        toggleFab();
                        break;
                    case R.id.popFloatingBtn:
                    toggleFab();
                        showToast("길 작성 페이지로 이동합니다.");
                    break;
                    case R.id.pop2FloatingBtn:
                    toggleFab();
                        showToast("이야기 작성 페이지로 이동합니다.");
                    break;
                }
            }*/
/*            private void showWrite() {
            }
            private void showToast(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });*/

/*        ImageButton mainMapExtendBtn = rootView.findViewById(R.id.mainMapExtendBtn);
        mainMapExtendBtn.bringToFront();
        mainMapExtendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity)getActivity()).replaceFragment(BigMapPage.newInstance());
               // mapViewContainer.removeView(mapView);
                Intent intent = new Intent(getActivity(), BigMapPage.class);
                startActivity(intent);
               // getActivity().finish();
            }
        });*/


      /*  FirebaseUser user = mAuth.getCurrentUser();
    private void initUI(ViewGroup rootView, FirebaseUser user) {
        db.collection("contents")
                .whereEqualTo("uid", user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String contentsId = document.getId();
                        Query query = db.collection("contents")
                                .document(contentsId).collection("title")
                                .orderBy("");
                        FirestoreRecyclerOptions<Contents> options = new FirestoreRecyclerOptions.Builder<Contents>()
                                .setQuery(query, Contents.class)
                                .build();
                        makeAdapter(options);
                    }
                }
            }
        });*/

        //mainRoadList.setAdapter(adapter);
    }

    private void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }
    private void toggleFab(){
        if (isFabOpen) {
            //mainFloatingBtn.setImageResource(R.drawable.ic_add);
            popFloatingBtn.startAnimation(fab_close);
            pop2FloatingBtn.startAnimation(fab_close);
            popFloatingBtn.setClickable(false);
            pop2FloatingBtn.setClickable(false);
            isFabOpen = false;

        } else {
            //mainFloatingBtn.setImageResource(R.drawable.ic_close);
            popFloatingBtn.startAnimation(fab_open);
            pop2FloatingBtn.startAnimation(fab_open);
            popFloatingBtn.setClickable(true);
            pop2FloatingBtn.setClickable(true);
            isFabOpen = true;
        }
    }
/*
    private void moveMyLocation(MapView mapView) {
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
            // lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }*/

    private void makeAdapter(FirestoreRecyclerOptions<Contents> options) {
      /*  adapter = new FirestoreRecyclerOptions<SearchContents>(options) {
               @Override
                public void onDataChanged() {
                    super.onDataChanged();
                    Log.d(TAG, "어댑터 작동");
                }
                @Override
                protected void onBindViewHolder(@NonNull contentsViewHolder holder, int position,
                                                    @NonNull SearchContents model) {
                        holder.setUserItems(model);
                    }
                }
        }*/
    }


/*    public void loadDtr(MapView mapView, MapPoint point) {
        int zoomLevel = mapView.getZoomLevel();
        if (zoomLevel <= 0){
            // 모든 경로 표시
            //  } else {
            ArrayList<MapPoint> manyPins = new ArrayList<MapPoint>();
            //DB의 MapPoint 다 넣기
            manyPins.add(MapPoint.mapPointWithGeoCoord(37.54238496760114, 126.85109815011367));
            manyPins.add(MapPoint.mapPointWithGeoCoord(37.53890481231618, 126.82940817621092));
            manyPins.add(MapPoint.mapPointWithGeoCoord(37.540725844063566, 126.83696139065235));
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
                customMarker3.setTag(2);
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
        polyline.setTag(500);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.
        // Polyline 좌표 지정.
        for (int i=0; i < PolyPoints.size(); i++) {
            MapPoint PolyPoint = PolyPoints.get(i);
            polyline.addPoint(PolyPoint);
        }
// Polyline 지도에 올리기.
        mapView.addPolyline(polyline);
    }*/



    /*@Override
    public void onMapViewInitialized(MapView mapView) {
    }
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
    }
    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        MapPoint centerPoint = mapView.getMapCenterPoint();
        //loadDtr(mapView, centerPoint);
    }
    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
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
    }*/
/*
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        String DtrName = mapPOIItem.getItemName();
        Toast.makeText(getContext(), DtrName, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
    }
    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {
    }  */

    public class contentsViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public contentsViewHolder(@NonNull View itemView) {
            super(itemView);
        }

          /*  public contentsViewHolder(NonNull View itemView) {
                super(itemView);
                view = itemView;
            }*/
    }



    // 디버그 키 해시 구하기 (카카오 맵 API 연동 시 필요), 맵 코드 주석 처리 후 실행! (애뮬에서 돌리면 실행 오류 날 수 있음)
        /*try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo("com.example.rotory", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String str = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("Key Hash : ", str);
                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/

}






