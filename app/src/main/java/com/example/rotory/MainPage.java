package com.example.rotory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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


import android.widget.ImageButton;

import android.widget.ImageView;

import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Adapter.WriteStoryImageAdapter;
import com.example.rotory.Contents.StoryImageAdapter;
import com.example.rotory.Interface.OnContentsItemClickListener;


import com.example.rotory.Search.SearchPage;

import com.example.rotory.VO.AppConstant;

import com.example.rotory.Search.SearchPage;

import com.example.rotory.VO.Contents;
import com.example.rotory.VO.NearPin;
import com.example.rotory.account.LogInActivity;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import com.google.firebase.firestore.Query;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class MainPage extends Fragment

{
    final static String TAG = "MainPage";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    private FirestoreRecyclerAdapter storyAdapter;

    GoogleMap map;
    LocationManager manager;
    // GPSListener gpsListener;


    Marker myMarker;

    MarkerOptions myLocationMarker;
    Marker Marker1;
    MarkerOptions MarkerOps1;


    Button mainSearchBtn;

    TextView mainSearchEdit;

    RecyclerView mainRoadList;
    RecyclerView mainStoryRecyclerView;
    FloatingActionButton mainFloatingBtn;

    private Animation fab_open, fab_close;
    private boolean isFabOpen = true;

    ImageButton popFloatingBtn;
    ImageButton pop2FloatingBtn;


    @Override
    public void onStop() {
        super.onStop();
        if (storyAdapter!=null){
            storyAdapter.stopListening();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        user = mAuth.getCurrentUser();

        if (context instanceof Activity)
            context = (Activity) context;
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


    private void initUI(ViewGroup rootView) {
        setContentView(R.layout.main_page);

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainSearchEdit = rootView.findViewById(R.id.mainSearchEdit);
        mainSearchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchPage.class);
                startActivity(intent);
            }
        });



        mainSearchEdit = rootView.findViewById(R.id.mainSearchEdit);
        mainSearchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchPage.class);
                startActivity(intent);
            }
        });


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
                                                loadDtr(center, rootView);
                                                int zoomLevel = (int) map.getCameraPosition().zoom;
                                                if (zoomLevel >= 18) {
                                                    loadDtrLine(center, rootView);
                                                } else {
                                                    loadDtr(center, rootView);
                                                }

                                            }
                                        });
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


            }
        });*/

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
                    loadDtr(curPoint, rootView); // 도토리 보여주기
                   // return;

            }
        }, 1000);                                 // 1000 = 1초


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

        mainStoryRecyclerView = rootView.findViewById(R.id.mainStoryList);
        LinearLayoutManager storyLayout = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        mainStoryRecyclerView.setLayoutManager(storyLayout);
        Query storyQuery = db.collection("contents").whereEqualTo("contentsType", 1)
                .orderBy("liked", Query.Direction.DESCENDING);
        Log.d(TAG,"query확인" + storyQuery);

        FirestoreRecyclerOptions<Contents> options = new FirestoreRecyclerOptions.Builder<Contents>()
                .setQuery(storyQuery,Contents.class)
                .build();
        makeStoryAdapter(options);
        storyAdapter.startListening();
        mainStoryRecyclerView.setAdapter(storyAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (storyAdapter != null){
            storyAdapter.startListening();
        }
    }


    private void makeStoryAdapter(FirestoreRecyclerOptions<Contents> options) {
        storyAdapter = new FirestoreRecyclerAdapter<Contents, StoryViewHolder>(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, " 어댑터 작동");
            }

            @Override
            protected void onBindViewHolder(@NonNull StoryViewHolder holder, int position, @NonNull Contents model) {
                holder.setItem(model);
                holder.setClickItem(model);
            }

            @NonNull
            @Override
            public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_story_item, parent, false);
                return new StoryViewHolder(view);
            }

        };
    }
    class StoryViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView mainStoryImg;
        TextView mainStoryTitle;
        AppConstant appConstant = new AppConstant();
        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            mainStoryImg = itemView.findViewById(R.id.mainStoryImg);
            mainStoryTitle = itemView.findViewById(R.id.mainStoryTitle);
        }

        public void setItem(Contents model) {
            Bitmap storyImageBitmap = appConstant.StringToBitmap(model.getTitleImage());
            //Log.d(TAG, storyImageBitmap.toString());
            mainStoryImg.setImageBitmap(storyImageBitmap);
            mainStoryTitle.setText(model.getTitle());

        }
        public void setClickItem(Contents model){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("contents")
                            .whereEqualTo("uid", model.getUid())
                            .whereEqualTo("writeDate", model.getWriteDate())
                            .whereEqualTo("title", model.getTitle())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot document : task.getResult()){
                                            Log.d(TAG,"스토리 아이템 다큐먼트 아이디 확인" + document.getId());
                                            String documentId = document.getId();
                                            Intent intent = new Intent(getContext(), LoadStoryItem.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("documentId", documentId);
                                            startActivity(intent);
                                        }
                                    };
                                }
                            });
                }
            });

        }

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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
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
    public void loadDtr(LatLng point, ViewGroup rootView) {

        map.clear();
        showMyLocationMarker();

        db.collection("contents")
                .whereEqualTo("contentsType", 0)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<NearPin> nearPin2 = new ArrayList<>();   // 거리차이, 위경도점(첫번째), 문서아이디
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document != null) {
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
                            Log.d("지도 DB", "문서 id : " + contentsId + "/ 거리: " + dist);
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

                        if(nearPin2.size() < 6){
                            for (int k = 0; k <nearPin2.size(); k++) {
                                // DB에서 핀들의 정보 (이름, 하단팝업정보 등) 가져와야함)
                                LatLng point1 = nearPin2.get(k).getPoint();
                                MarkerOps1 = new MarkerOptions();
                                MarkerOps1.position(point1);
                                //    myLocationMarker.title("●내위치\n");
                                //    myLocationMarker.snippet("●GPS로확인한위치");
                                int[] dtrImageName = {R.drawable.acorn_number1, R.drawable.acorn_number2, R.drawable.acorn_number3, R.drawable.acorn_number4, R.drawable.acorn_number5, R.drawable.acorn_number6};
                                MarkerOps1.icon(BitmapDescriptorFactory.fromResource(dtrImageName[k]));
                                Marker1 = map.addMarker(MarkerOps1);
              // 핀 표시 -> 표시할 개수 선정
                            }

                        }else {
                            // 핀 표시 -> 표시할 개수 선정
                            for (int k = 0; k < 6; k++) {
                                // DB에서 핀들의 정보 (이름, 하단팝업정보 등) 가져와야함)
                                LatLng point1 = nearPin2.get(k).getPoint();
                                MarkerOps1 = new MarkerOptions();
                                MarkerOps1.position(point1);
                                //    myLocationMarker.title("●내위치\n");
                                //    myLocationMarker.snippet("●GPS로확인한위치");
                                int[] dtrImageName = {R.drawable.acorn_number1, R.drawable.acorn_number2, R.drawable.acorn_number3, R.drawable.acorn_number4, R.drawable.acorn_number5, R.drawable.acorn_number6};
                                MarkerOps1.icon(BitmapDescriptorFactory.fromResource(dtrImageName[k]));
                                Marker1 = map.addMarker(MarkerOps1);

                            }
                        }

                        loadRoadList(nearPin2, rootView);
                    }
                }
            }
        });

    }

    private void loadRoadList(ArrayList<NearPin> nearPin2, ViewGroup rootView) {

        MyAdapter adapter = new MyAdapter();
        mainRoadList = rootView.findViewById(R.id.mainRoadList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mainRoadList.setLayoutManager(layoutManager);

        if(nearPin2.size() < 6){
            for (int k = 0; k < nearPin2.size(); k++) {
                String documentId = nearPin2.get(k).getDocumentId();
                int finalK = k;
                db.collection("contents").document(documentId)
                        .get().addOnCompleteListener(new OnCompleteListener() {

                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                            String contentsId = document.getId();
                            String title = (String) document.get("title");
                            String tag1 = (String) document.get("tag1");
                            String hour = (String) document.get("hour");
                            String min = (String) document.get("min");

                            adapter.addItem(new Contents((finalK + 1) + ". " + title, tag1, hour + "시간 " + min + "분", contentsId));
                              /*  MyAdapter(options, rootView, contentsId);
                                adapter.startListening();
                                mainRoadList.setAdapter(adapter); */
                            mainRoadList.setAdapter(adapter);

                        }
                    }
                });
            }
        }else {
            for (int k = 0; k < 6; k++) {
                String documentId = nearPin2.get(k).getDocumentId();
                int finalK = k;
                db.collection("contents").document(documentId)
                        .get().addOnCompleteListener(new OnCompleteListener() {

                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                            String contentsId = document.getId();
                            String title = (String) document.get("title");
                            String tag1 = (String) document.get("tag1");
                            String hour = (String) document.get("hour");
                            String min = (String) document.get("min");

                            adapter.addItem(new Contents((finalK + 1) + ". " + title, tag1, hour + "시간 " + min + "분", contentsId));
                              /*  MyAdapter(options, rootView, contentsId);
                                adapter.startListening();
                                mainRoadList.setAdapter(adapter); */
                            mainRoadList.setAdapter(adapter);

                        }
                    }
                });
            }
        }

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements OnContentsItemClickListener {

        OnContentsItemClickListener listener;
        ArrayList<Contents> items = new ArrayList<Contents>();

        public void setOnItemClickListener(OnContentsItemClickListener listener)
        {this.listener = listener;}

        @NonNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = inflater.inflate(R.layout.main_road_item, viewGroup, false);
            return new ViewHolder(itemView, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
            Contents item = items.get(position);
            holder.setItem(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
        public void addItem(Contents item){items.add(item);}
        public void setItems(ArrayList<Contents> items){this.items = items;}
        public Contents getItem(int position){return items.get(position);}
        public void setItem(int position, Contents item){items.set(position, item);}

        @Override
        public void onItemClick(WriteStoryImageAdapter.writestroyHolder writestroyHolder, View view, int position) {

        }

        @Override
        public void onItemClick(StoryImageAdapter.writestroyHolder writestroyHolder, View view, int position) {

        }

        @Override
        public void onItemClick(ViewHolder holder, View view, int position) {
            if (listener != null)
            {listener.onItemClick(holder, view, position);}

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView mainRoadTitle;
            TextView mainRoadTag;
            TextView mainRoadPeriod;
            TextView mainRoadId;

            public ViewHolder(View itemView, OnContentsItemClickListener listener) {
                super(itemView);
                mainRoadTitle = itemView.findViewById(R.id.mainRoadTitle);
                mainRoadTag = itemView.findViewById(R.id.mainRoadTag);
                mainRoadPeriod = itemView.findViewById(R.id.mainRoadPeriod);
                mainRoadId = itemView.findViewById(R.id.mainRoadId);

                itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        String cDocumentID = mainRoadId.getText().toString();
                        Intent intent = new Intent(getContext(), LoadRoadItem.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("documentId", cDocumentID);
                        startActivity(intent);

                        Log.d("아이템 클릭", "아이템 번호: "+ cDocumentID);
                    }
                    });
            }

        public void setItem(Contents item){

                mainRoadTitle.setText(item.getTitle());
            mainRoadTag.setText(item.getTag1());
            mainRoadPeriod.setText(item.getTime());
            mainRoadId.setText(item.getDocumentId());

            }
        }
    }


    // 도토리 경로들 + 선 로딩

    public void loadDtrLine(LatLng point, ViewGroup rootView) {

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

/* public class myViewHolder extends RecyclerView.ViewHolder {
    private View view;

    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
         /*   itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView myScrapId1 = itemView.findViewById(R.id.myScrabId);
                    String Id = myScrapId1.getText().toString();
                    Toast.makeText(getApplicationContext(), Id, Toast.LENGTH_SHORT).show();
                }
            });

    }


    public void setRoadItems(Contents item, ViewGroup rootView) {

        mainRoadTitle = rootView.findViewById(R.id.mainRoadTitle);
        mainRoadTag = rootView.findViewById(R.id.mainRoadTag);
        mainRoadPeriod = rootView.findViewById(R.id.mainRoadPeriod);

        mainRoadTitle.setText(item.getTitle());
        mainRoadTag.setText(item.getTag1());
        mainRoadTitle.setText(item.getTitle());


    }


    public void bind(int contentsType, String cDocumentID) {
        LinearLayout mainRoadLayout = itemView.findViewById(R.id.mainRoadList);
        mainRoadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(getContext(), LoadRoadItem.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("documentId", cDocumentID);
                    startActivity(intent);

                    //Log.d("인텐트", cDocumentID);
                }

        });
    }


}  */



}







