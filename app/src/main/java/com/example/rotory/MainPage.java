package com.example.rotory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.LoadMapDtrListener;
import com.example.rotory.Search.SearchContents;
import com.example.rotory.VO.Contents;
import com.example.rotory.VO.NearPin;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import retrofit2.http.HEAD;


public class MainPage extends Fragment implements LoadMapDtrListener
        //implements MapView.MapViewEventListener
{
    final static String TAG = "MainPage";


    FloatingActionButton mainFloatingBtn;

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


    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }

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

    public void showWrite(){}

/*    public void onContentsListener (contentsAdapter.ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onContentsListener(holder, view, position);
        }
    }*/


    private void initUI(ViewGroup rootView) {

        //mapView = new MapView(getActivity());
        ViewGroup mapViewContainer = (ViewGroup) rootView.findViewById(R.id.mainMapLayout);
        //mapViewContainer.addView(mapView);

        setContentView(R.layout.main_page);

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mainGoogleMap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        Log.d("Map", "지도준비됨.");
                                        map = googleMap;
                                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {

                                            @Override
                                            public void onCameraIdle() {
                                                LatLng center = map.getCameraPosition().target;   // 중앙점 https://stackoverflow.com/questions/13904505/how-to-get-center-of-map-for-v2-android-maps
                                                loadDtr(center);
                                                int zoomLevel = (int) map.getCameraPosition().zoom;
                                                if (zoomLevel >= 18) {
                                                    loadDtrLine(center);
                                                } else {
                                                    loadDtr(center);
                                                }

                                            }
                                        });

          /*      map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
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

                        BigMapPage.SlidingPageAnimationListener animListener = new BigMapPage.SlidingPageAnimationListener();
                        tranlateUpAnim.setAnimationListener(animListener);
                        tranlateDownAnim.setAnimationListener(animListener);

                        container = findViewById(R.id.container);
                        container.startAnimation(tranlateDownAnim);
                    }
                });
            }
        }); */
                                    }
                                });

                                        LocationManager manager = (LocationManager)
                                                getContext().getSystemService(Context.LOCATION_SERVICE);// LocationManager 객체 참조하기
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

                                        BigMapPage.GPSListener gpsListener = new BigMapPage.GPSListener(); // 10초마다위치갱신되게끔
                                        long minTime = 1000;
                                        float minDistance = 0;
                                        //manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, gpsListener);
                                        // manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);

                                        Handler mHandler = new Handler();
                                      /*  mHandler.postDelayed(new Runnable() {
                                            public void run() {
                                                // 3초 후에 현재위치를 받아오도록 설정 , 바로 시작 시 에러납니다.

                                                @SuppressLint("MissingPermission") Location location = manager.getLastKnownLocation(locationProvider);

                                                Double latitude = location.getLatitude();
                                                Double longitude = location.getLongitude();
                                                String message = "내위치-> Latitude : "+ latitude + "\nLongitude:"+ longitude;
                                                Log.d("Map", message);

                                                showCurrentLocation(latitude, longitude); // 카메라움직여지도에띄우기
                                                LatLng curPoint = new LatLng(latitude, longitude);
                                                showMyLocationMarker(); // 현재위치 보여주기
                                                loadDtr(curPoint); // 도토리 보여주기
                                            }
                                        }, 2000); // 1000 = 1초*/


                                        ImageButton mainMapExtendBtn = rootView.findViewById(R.id.mainMapExtendBtn);
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
                                        });


                                        // 플로팅버튼은 https://re-build.tistory.com/31 참고하여 fragment 형식에 맞춰 코드 작성
                                        // 실행시 Activity Null point Exception 문제가 발생

        /*Context = new context(getActivity());

        fab_open = AnimationUtils.loadAnimation(Context, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(Context, R.anim.fab_open);*/



     /*  mainFloatingBtn = rootView.findViewById(R.id.mainFloatingBtn);
        popFloatingBtn = rootView.findViewById(R.id.popFloatingBtn);
        pop2FloatingBtn = rootView.findViewById(R.id.pop2FloatingBtn);

        mainFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });
        popFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
                showToast("길 작성 페이지로 이동합니다.");

            }
        });

        pop2FloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
                showToast("이야기 작성 페이지로 이동합니다.");
            }

        });


        Button button = rootView.findViewById(R.id.mainFloatingBtn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                switch (v.getId()) {
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
            }


                                    private void showToast(String s) {
                                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                                    }


                                    FirebaseUser user = mAuth.getCurrentUser();

                                    private void toggleFab() {
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
                                    } */

    }

    private void showCurrentLocation(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude); // 현재위치의좌표로LatLng 객체생성하기
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 16));
    }


    public void showMyLocationMarker() {
        LocationManager manager = (LocationManager)
                getContext().getSystemService(Context.LOCATION_SERVICE);// LocationManager 객체 참조하기
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


    // 첫 도토리만 로딩
    @Override
    public void loadDtr(LatLng point) {

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
            int[] dtrImageName= {R.drawable.acorn_number1, R.drawable.acorn_number2, R.drawable.acorn_number3, R.drawable.acorn_number4, R.drawable.acorn_number5, R.drawable.acorn_number6};
            MarkerOps1.icon(BitmapDescriptorFactory.fromResource(dtrImageName[k]));
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
        polylineOptions.color(Color.RED)
                .width(10)
                .geodesic(true);

        for(int l=0; l<PolyPoints.size(); l++) {
            polylineOptions
                    .add(PolyPoints.get(l));
        }

        Polyline polyline = map.addPolyline(polylineOptions);

    }
}
           /* private void initUI (ViewGroup rootView, FirebaseUser user){

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
            });

            //mainRoadList.setAdapter(adapter);
        }*/


       /* private void makeAdapter(FirestoreRecyclerOptions<Contents> options) {
       adapter = new FirestoreRecyclerOptions<SearchContents>(options) {

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

   /* public class contentsViewHolder extends RecyclerView.ViewHolder {
        private View view;

            public contentsViewHolder(NonNull View itemView) {
                super(itemView);
                view = itemView;

            }
        }*/






