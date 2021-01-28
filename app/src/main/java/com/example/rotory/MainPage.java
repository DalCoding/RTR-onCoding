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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.example.rotory.VO.NearPin;
import com.example.rotory.account.LogInActivity;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class MainPage extends Fragment implements LoadMapDtrListener, AutoPermissionsListener
        //implements MapView.MapViewEventListener
{
    final static String TAG = "MainPage";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;



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

    FloatingActionButton mainFloatingBtn;

    private Animation fab_open, fab_close;
    private boolean isFabOpen = true;

    ImageButton popFloatingBtn;
    ImageButton pop2FloatingBtn;

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

        user = mAuth.getCurrentUser();

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
                //AutoPermissions.Companion.loadAllPermissions(getActivity(), 101);

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
        long minTime = 500;
        float minDistance = 0;
        //manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, gpsListener);
        // manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);

        Handler mHandler = new Handler();
                                        mHandler.postDelayed(new Runnable() {
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
                                        }, 1000); // 1000 = 1초


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

        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);

        pop2FloatingBtn = rootView.findViewById(R.id.pop2FloatingBtn);
        pop2FloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    /*Intent writeStoryIntent = new Intent(getActivity(), Write_Story.class);
                    //writeStoryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(writeStoryIntent);*/
                } else {
                    goLogInPage();

                }
            }
        });
        popFloatingBtn = rootView.findViewById(R.id.popFloatingBtn);
        popFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    Intent writeRoadIntent = new Intent(getActivity(), WriteRoadPage.class);
                    //writeRoadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(writeRoadIntent);
                } else {
                    goLogInPage();

                }

            }
        });

        popFloatingBtn.startAnimation(fab_close);
        pop2FloatingBtn.startAnimation(fab_close);

        mainFloatingBtn = rootView.findViewById(R.id.mainFloatingBtn);
        mainFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabOpen) {
                    popFloatingBtn.startAnimation(fab_open);
                    pop2FloatingBtn.setClickable(true);
                    pop2FloatingBtn.startAnimation(fab_open);
                    pop2FloatingBtn.setClickable(true);
                    isFabOpen = false;
                } else {
                    popFloatingBtn.startAnimation(fab_close);
                    //pop2FloatingBtn.setClickable(false);
                    pop2FloatingBtn.startAnimation(fab_close);
                    //pop2FloatingBtn.setClickable(false);
                    isFabOpen = true;
                }

            }
        });

    }

    private void goLogInPage() {
        Intent LoginIntent = new Intent(getActivity(), LogInActivity.class);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(LoginIntent);

    }
    private void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
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

        db.collection("contents")
                .whereEqualTo("contentsType", 0)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<NearPin> nearPin2 = new ArrayList<>();   // 거리차이, 위경도점(첫번째), 문서아이디
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String contentsId = document.getId();
                        ArrayList<HashMap> dtrLatLng = (ArrayList<HashMap>) document.get("dtrLatLng");
                        // 도토리 배열 : [{latitude=37.422083928086955, longitude=-122.08573322743177}, {latitu... + null


                        HashMap latLng = dtrLatLng.get(0);

                        double latlat = (double) latLng.get("latitude");
                        double longlong = (double) latLng.get("longitude");
                        LatLng latLng2 = new LatLng(latlat, longlong);

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

                        nearPin2.add(new NearPin(dist, latLng2, contentsId));
                        //   dtrLatLng.get(0);
                        Log.d("지도 DB", "문서 id : " + contentsId + "/ 거리: " +dist);
                    }

                    Collections.sort(nearPin2, new Comparator<NearPin>() {
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

                    // 핀 표시 -> 표시할 개수 선정
                    for (int k=0; k<6; k++) {
                        // DB에서 핀들의 정보 (이름, 하단팝업정보 등) 가져와야함)
                        LatLng point1 = nearPin2.get(k).getPoint();
                        MarkerOps1 = new MarkerOptions();
                        MarkerOps1.position(point1);
                        //    myLocationMarker.title("●내위치\n");
                        //    myLocationMarker.snippet("●GPS로확인한위치");
                        int[] dtrImageName= {R.drawable.acorn_number1, R.drawable.acorn_number2, R.drawable.acorn_number3, R.drawable.acorn_number4, R.drawable.acorn_number5, R.drawable.acorn_number6};
                        MarkerOps1.icon(BitmapDescriptorFactory.fromResource(dtrImageName[k]));
                        Marker1 = map.addMarker(MarkerOps1);

                    }
                }
            }
        });

    }


    // 도토리 경로들 + 선 로딩
    @Override
    public void loadDtrLine(LatLng point) {

        map.clear();
        showMyLocationMarker();

        db.collection("contents")
                .whereEqualTo("contentsType", 0)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<NearPin> nearPin2 = new ArrayList<>();   // 거리차이, 위경도점(첫번째), 문서아이디
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String contentsId = document.getId();
                        ArrayList<HashMap> dtrLatLng = (ArrayList<HashMap>) document.get("dtrLatLng");
                        // 도토리 배열 : [{latitude=37.422083928086955, longitude=-122.08573322743177}, {latitu... + null

                        HashMap latLng = dtrLatLng.get(0);

                        double latlat = (double) latLng.get("latitude");
                        double longlong = (double) latLng.get("longitude");
                        LatLng latLng2 = new LatLng(latlat, longlong);

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

                        nearPin2.add(new NearPin(dist, latLng2, contentsId));
                        //   dtrLatLng.get(0);
                        Log.d("지도 DB", "문서 id : " + contentsId + "/ 거리: " +dist);
                    }

                    Collections.sort(nearPin2, new Comparator<NearPin>() {
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

                    // 핀 표시 + 선연결 -> 표시할 개수 선정
                    for (int k=0; k<6; k++) {

                        String documentId = nearPin2.get(k).getDocumentId();
                        db.collection("contents").document(documentId)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    ArrayList<HashMap> dtrLatLngs = (ArrayList<HashMap>) document.get("dtrLatLng");

                                    ArrayList<LatLng> PolyPoints = new ArrayList<>();

                                    for(int l=0; l<dtrLatLngs.size(); l++) {
                                        HashMap latLng = dtrLatLngs.get(l);
                                        double latlat = (double) latLng.get("latitude");
                                        double longlong = (double) latLng.get("longitude");
                                        LatLng latLng2 = new LatLng(latlat, longlong);

                                        //마커 표시
                                        //  MarkerOps1 = new MarkrOptions();
                                        MarkerOps1.position(latLng2);
                                        //    myLocationMarker.title("●내위치\n");
                                        //    myLocationMarker.snippet("●GPS로확인한위치");
                                        MarkerOps1.icon(BitmapDescriptorFactory.fromResource(R.drawable.acorn2));
                                        Marker1 = map.addMarker(MarkerOps1);
                                        PolyPoints.add(latLng2);
                                    }
                                    PolylineOptions polylineOptions = new PolylineOptions();
                                    polylineOptions.color(Color.argb(128, 255, 51, 0))
                                            .width(7)
                                            .geodesic(true);

                                    for(int l=0; l<PolyPoints.size(); l++) {
                                        polylineOptions
                                                .add(PolyPoints.get(l));
                                    }

                                    Polyline polyline = map.addPolyline(polylineOptions);
                                }
                            }
                        });


                    }
                }
            }
        });

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






