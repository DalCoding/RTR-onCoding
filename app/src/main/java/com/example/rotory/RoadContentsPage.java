package com.example.rotory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Contents.SetIcons;
import com.example.rotory.Contents.StoryContentsPage;
import com.example.rotory.Interface.OnUserActItemClickListener;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Contents;
import com.example.rotory.account.LogInActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.w3c.dom.Document;

import java.io.IOError;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RoadContentsPage extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    final static String TAG = "RoadContentsPage";
    AppConstant appConstant = new AppConstant();
    SetIcons setIcons = new SetIcons();

    String contentsID;
    Bundle contentsBundle = this.getArguments();
    //String contentsID = contentsBundle.getString("storyDocumentId");

    GoogleMap map;


    Button rcontentsCommBtn;
    ImageView rcontentsLinkImg;
    ImageView rcontentsScrapImg;
    ImageView rcontentsHeartImg;
    TextView rcontentsTitleText;
    ImageView rcontentsLevelImg;
    TextView rcontentsUsernameText;
    ImageView rcontentsStarImg;
    RecyclerView rCommRView;
    EditText rcontentsCommEdit;

    TextView rcontentsMapText;
    TextView rcontentsTaketimeText;
    TextView rcontentsTakewhoText;

    ArrayList<Comment> commentArrayList;

    Context context;
    OnUserActItemClickListener listener;

    TextView reportTextView;
    public static final String TAG_EVENT_DIALOG = "report";
    TextView commReportText;
    Spinner reportSpinner;

    AppBarLayout appBarLayout;

    Boolean isSignIn = false;
    private FirestoreRecyclerAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance(); //db 선언
    private FirestoreRecyclerAdapter commentAdapter;


    TextView commUsernameText;
    TextView commConText;
    TextView commTimeText;
    ImageView commLevelImg;

    CardView dtrInfo;

    EditText comment;

    ArrayList<String> dtrName = new ArrayList<>();
    ArrayList<Object> dtrLatLng = new ArrayList<>();
    ArrayList<LatLng> PolyPoints = new ArrayList<>();
    ArrayList<String> dtrAddress = new ArrayList<>();

    ArrayList<String> dtrName2 = new ArrayList<>();

    public RoadContentsPage() {}


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FirebaseUser user = mAuth.getCurrentUser();
        if (context instanceof OnUserActItemClickListener) {
            listener = (OnUserActItemClickListener) context;
        }
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (context != null) {
            context = null;
            listener = null;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.road_contents_page, container, false);

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Bundle contentsBundle = this.getArguments();
        String contentsID = contentsBundle.getString("storyDocumentId");

        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저 네임 : " + checkLogIN);
            isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
        }

        Query query = db.collection("SearchContents").whereEqualTo("contentsType", 1)
                .orderBy("contentsType", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Contents> options = new FirestoreRecyclerOptions.Builder<Contents>()
                .setQuery(query, Contents.class)
                .build();

        if (user != null) {
            initUI(rootView);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.rcontentsMap);
        mapFragment.getMapAsync(this);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        //commentAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //commentAdapter.stopListening();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initUI(ViewGroup rootView) {
        rCommRView = rootView.findViewById(R.id.rCommRView);
        rcontentsCommBtn = rootView.findViewById(R.id.rcontentsCommBtn);
        rcontentsLinkImg = rootView.findViewById(R.id.rcontentsLinkImg);
        rcontentsScrapImg = rootView.findViewById(R.id.rcontentsScrapImg);
        rcontentsHeartImg = rootView.findViewById(R.id.rcontentsHeartImg);
        rcontentsTitleText = rootView.findViewById(R.id.rcontentsTitleText);
        rcontentsLevelImg = rootView.findViewById(R.id.rcontentsLevelImg);
        rcontentsUsernameText = rootView.findViewById(R.id.rcontentsUsernameText);
        rcontentsStarImg = rootView.findViewById(R.id.rcontentsStarImg);
        rcontentsCommEdit = rootView.findViewById(R.id.rcontentsCommEdit);
        rcontentsMapText = rootView.findViewById(R.id.rcontentsMapText);
        rcontentsTaketimeText = rootView.findViewById(R.id.rcontentsTaketimeText);
        rcontentsTakewhoText = rootView.findViewById(R.id.rcontentsTakewhoText);

        Log.d(TAG, "initUI 시작, 번들 전송 잘 됐는지, pDocumentId:" + contentsID);
        loadContents(contentsID, user);

        if (user != null) {
            setIcons.getUserActivityIcon(contentsID, "myLike", rcontentsHeartImg, R.drawable.heartfilled, R.drawable.heart);
            setIcons.getUserActivityIcon(contentsID, "myScrap", rcontentsScrapImg, R.drawable.scrabtagfilled, R.drawable.scrabtag);
        }

        rcontentsCommBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "클릭 눌림");
                if (user != null) {
                    saveComment(contentsID, view);
                    Log.d(TAG, "유저 정보 존재");
                } else {
                    showToast("로그인이 필요한 서비스입니다.");
                }
            }
        });



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
       rCommRView.setLayoutManager(layoutManager);

        Query query = db.collection("contents")
                .document(contentsID).collection("comment")
                .orderBy("savedDate", Query.Direction.DESCENDING);
        Log.d(TAG, "쿼리문 확인" + query);

        FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .build();

        Log.d(TAG, "recyclerView option 확인" + options);

        makeCommentAdapter(options);
        commentAdapter.startListening();
        rCommRView.setAdapter(commentAdapter);

        String userId = user.getEmail();

    }

    private void makeCommentAdapter(FirestoreRecyclerOptions<Comment> options) {
        commentAdapter = new FirestoreRecyclerAdapter<Comment, CommentViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, "어댑터 작동");
            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
                return new CommentViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment model) {
                Log.d(TAG, "onBindViewHolder 작동 " + holder.itemView.toString());
                holder.setCommentItems(model);
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this.getActivity());
        map = googleMap;

        db.collection("contents")
                .document(contentsID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> contents = documentSnapshot.getData();
                        dtrName = (ArrayList<String>) contents.get("dtrName");
                        dtrAddress = (ArrayList<String>) contents.get("dtrAddress");
                        dtrLatLng = (ArrayList<Object>) contents.get("dtrLatLng");

                        Map<String, Object> forPopUp = new HashMap<>();


                        MarkerOptions markerOptions = new MarkerOptions();

                        LatLng latLng;

                        for (int i = 0; i < dtrLatLng.size(); i++) {
                            Map<String, Double> list = (Map<String, Double>) dtrLatLng.get(i);
                            Log.d(TAG, "for문 안 확인" + list);
                            double latitude = list.get("latitude");
                            double longitude = list.get("longitude");

                            Log.d(TAG, "latitude : longitude" + latitude + ":" + longitude);

                            latLng = new LatLng(latitude, longitude);


                            markerOptions.position(latLng);
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.acorn2));
                            map.addMarker(markerOptions);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

                            ArrayList<String> infoList = new ArrayList<>();
                            infoList.add(dtrName.get(i));
                            infoList.add(dtrAddress.get(i));

                            ArrayList<String> infoName = new ArrayList<>();
                            infoName.add(dtrName.get(i));

                            Log.d(TAG,"도토리 이름, 도토리 주소 정보 확인" + infoList);

                            HashMap<String, Double> dtrLatLngList = (HashMap<String, Double>) dtrLatLng.get(i);
                            String key = String.valueOf(dtrLatLngList.get("latitude"));
                            Log.d(TAG, "key 값 확인" + key);
                            forPopUp.put(key, infoList);

                            Log.d(TAG, "해쉬맵 정보 잘들어가는지 확인" + forPopUp);

                            PolyPoints.add(latLng);
                            drawPoly(map, PolyPoints);



                        }

                            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    String key = String.valueOf(marker.getPosition().latitude);
                                    Log.d(TAG, "마커에서 받은 정보 : 해쉬맵에서 받은 정보" + key+  " : " + forPopUp.keySet());
                                    ArrayList<String> infoList = (ArrayList<String>) forPopUp.get(key);

                                    Log.d(TAG,"infoList 확인" + infoList + "\n" + forPopUp.get(key));


                                    TextView dtrInfoSignText = getView().findViewById(R.id.dinfoSignText);
                                    dtrInfo = getView().findViewById(R.id.dtrInfo);

                                    TextView dtrInfoAdText = getView().findViewById(R.id.dinfoAdText);
                                    Button dtrInfoMoveBtn = getView().findViewById(R.id.dinfoMoveBtn);

                                    dtrInfoAdText.clearComposingText();

                                    dtrInfo.setVisibility(View.VISIBLE);

                                    dtrInfoSignText.setText(infoList.get(0));
                                    dtrInfoAdText.setText(infoList.get(1));
                                    dtrInfoMoveBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getActivity().getApplicationContext(), "아쉽게도 이야기가 없네요.", Toast.LENGTH_SHORT).show();

                                            /*Intent intent = new Intent(getActivity(), LoadStoryItem.class);
                                            intent.putExtra("documentId", contentsID);
                                            startActivity(intent);*/
                                        }
                                    });

                                    return false;
                                }
                            });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    public void drawPoly(GoogleMap map, ArrayList<LatLng> polyPoints) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(5).color(Color.argb(128, 255, 51, 0));

        for (int i = 0; i < polyPoints.size(); i++) {
            LatLng polyPoint = polyPoints.get(i);
            polylineOptions.add(polyPoint);

            map.addPolyline(polylineOptions);
        }
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setCommentItems(Comment comment) {
            Log.d(TAG, "set CommentItems 시작");
            commUsernameText = view.findViewById(R.id.commUsernameText);
            commConText = view.findViewById(R.id.commConText);
            commTimeText = view.findViewById(R.id.commTimeText);
            commReportText = view.findViewById(R.id.commReportText);
            commLevelImg = view.findViewById(R.id.commLevelImg);

            String pDocumentId = String.valueOf(comment.getPersonId());
            Log.d(TAG, "pDocumentID 확인" + pDocumentId);

            db.collection("person").document(pDocumentId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Log.d(TAG, "자료 불러옴" + task.getResult().getId());
                    Map<String, Object> document = new HashMap<>();
                    document = task.getResult().getData();
                    if (document != null) {
                        Log.d(TAG, String.valueOf(document));
                    } else {
                        Log.d(TAG, String.valueOf(document) + "값없음");
                    }

                    String userName = document.get("userName").toString();
                    String userLevel = document.get("userLevel").toString();
                    commUsernameText.setText(userName);
                    commLevelImg.setImageResource(appConstant.getUserLevelImage(userLevel));
                    String commentedUser = task.getResult().get("userId").toString();
                    Log.d(TAG, "댓글단사람" + commentedUser);
                    Log.d(TAG, "현재 사용자 확인" + user.getEmail());
                    if (user != null) {
                        if (user.getEmail().equals(commentedUser)) {
                            commReportText.setVisibility(View.INVISIBLE);
                            commReportText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //    deleteComment();
                                    //showToast("댓글을 삭제하셨습니다.");
                                }
                            });
                        } else {
                            commReportText.setText("신고");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            commReportText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    openReportDialog(builder);
                                }

                            });
                        }
                    } else {
                        commReportText.setText("신고");
                        commReportText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showToast("로그인이 필요한 서비스입니다");
                            }
                        });
                    }
                    commConText.setText(comment.getComment());
                    commTimeText.setText(comment.getSavedDate());
                    Log.d(TAG, "정보 입력 확인" + commConText.getText().toString() + "=>" +
                            comment.getComment());
                }
            });
        }
    }

    private void openReportDialog(AlertDialog.Builder dialog) {
        LayoutInflater inflater = getLayoutInflater();


        View viewDialog = inflater.inflate(R.layout.report, null);
        dialog.setView(viewDialog);
        dialog.setTitle("신고하기");


        reportTextView = viewDialog.findViewById(R.id.reportTextView);
        reportSpinner = viewDialog.findViewById(R.id.reportSpinner);

        reportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                reportTextView.setText(reportSpinner.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                reportTextView.setText("");
            }
        });
        dialog.setPositiveButton("제출", new DialogInterface.OnClickListener()
                //showToast("댓글을 신고하셨습니다.");
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {//제출 버튼..
                dialog.dismiss();

                FirebaseUser user = mAuth.getCurrentUser();
                String userEmail = user.getEmail();

                reportTextView = viewDialog.findViewById(R.id.reportTextView);
                String reportText = reportTextView.getText().toString();  //reportTextView에서 선택된 신고 내역 받기

                Date currentDate = new Date();
                String reported_date = appConstant.dateFormat.format(currentDate);

                Map<String, Object> ReportData = new HashMap<>();
                ReportData.put("userId", userEmail); // 신고자 아이디
                ReportData.put("reportContents", reportText);  // 신고 내용
                ReportData.put("reportedId", ""); // 해당 코멘트 아이디
                ReportData.put("reportedDate", reported_date); // 신고 날짜

                db.collection("report").add(ReportData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentreference) {
                                showToast("댓글을 신고하셨습니다.");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error Report document", e);
                            }
                        });
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }

        });


        alertDialog.show();
    }


    private void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void loadContents(String contentsID, FirebaseUser user) {
        // 해당글의 아이디 -> 해당 글의 정보 받아오려면 아이디로 다시 검색 필요!
        DocumentReference docRef = db.collection("contents").document(contentsID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "자료 받아오기 완료");
                        Map<String, Object> ContentsList = new HashMap<>();
                        ContentsList = document.getData();
                        Log.d(TAG, "title확인" + ContentsList.get("title"));
                        db.collection("person").whereEqualTo("uid", ContentsList.get("uid")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()){
                                    setIcons.getUserActivityIcon(snapshot.getId(), "myStar", rcontentsStarImg, R.drawable.starfilled, R.drawable.star);
                                }
                            }
                        });
                        setContents(ContentsList);
                        clickUserActIcon(contentsID, ContentsList, user);


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

        private void clickUserActIcon(String contentsID, Map<String, Object> contentsList,
                FirebaseUser user) {
            // 로그인 정보 없을경우 다이얼로그 먼저 띄워주기 " 로그인이 필요한 서비스 입니다"
            Log.d(TAG, "ClickActIcon 시작");
            String writerUid = contentsList.get("uid").toString();
            if (user != null) {
                rcontentsHeartImg.setClickable(true);
                rcontentsHeartImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (writerUid.equals(user.getUid())) {
                            Toast.makeText(getContext(), user.getDisplayName() + " 님의 도토리! 자신의 도토리는 담을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "하트 아이콘 클릭");
                            setIcons.isInList(contentsID, contentsList, "myScrap", user,
                                    rcontentsScrapImg, listener, context);
                        }
                    }
                });
                rcontentsStarImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (writerUid.equals(user.getUid())) {
                            Toast.makeText(getContext(), user.getDisplayName() + "님의 도토리! 자신의 도토리는 담을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            setFavoriteAct(contentsList, user);
                            Log.d(TAG, "스타아이콘 클릭");
                        }
                    }
                });
                rcontentsScrapImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (writerUid.equals(user.getUid())) {
                            Toast.makeText(getContext(), user.getDisplayName() + "님의 도토리! 자신의 도토리는 담을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            setIcons.isInList(contentsID, contentsList, "myScrap", user,
                                    rcontentsScrapImg, listener, context);
                        Log.d(TAG, "태그 아이콘 클릭");
                        }
                    }
                });
            } else {
                rcontentsHeartImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent logInIntent = new Intent(getContext(), LogInActivity.class);
                        startActivity(logInIntent);
                    }
                });
                rcontentsStarImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent logInIntent = new Intent(getContext(), LogInActivity.class);
                        startActivity(logInIntent);
                    }
                });
                rcontentsScrapImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent logInIntent = new Intent(getContext(), LogInActivity.class);
                        startActivity(logInIntent);
                    }
                });
            }
        }

        private void saveComment(String contentsId, View v) {
            String userEmail = user.getEmail();
            String comment = rcontentsCommEdit.getText().toString();
            Log.d(TAG, "saveComment" + comment);

            HashMap<String, Object> result = new HashMap<>();
            result.put("comment", comment);
            result.put("contentsId", contentsId);
            result.put("CommentType", "댓글");
            result.put("uid", user.getUid());
            result.put("savedDate", appConstant.dateFormat.format(new Date()));

            Log.d(TAG, "UserEmail 받아옴" + userEmail);

            db.collection("person").whereEqualTo("userId", userEmail)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    String personId = documentSnapshot.getId();
                                    Log.d(TAG, "personId 정보?" + personId);
                                    result.put("personId", personId);
                                    writerNewUser(contentsId, result);
                                }
                            }
                        }
                    }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    Log.d(TAG, "save Comment onsuccessListener 확인" + queryDocumentSnapshots.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "saveComment 유저 정보 불러오기 실패 : " + e.toString());
                }
            });
        }

    private void writerNewUser(String documentId, HashMap<String, Object> commentData) {
        //User user = new User(name, email);

        db.collection("contents").document(documentId).collection("comment")
                .add(commentData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "데이터저장성공");
                        Log.d(TAG, "코멘트 정보 => " + documentReference);
                        showToast("글이 추가되었습니다");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "저장 실패..." + e.toString());
                    }
                });
    }

    private void setFavoriteAct(Map<String, Object> contentsList, FirebaseUser user){
    String userId = user.getEmail();

    db.collection("person").whereEqualTo("uid", contentsList.get("uid")).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot writerDocument : task.getResult()) {
                            String savedDocumentId = writerDocument.getId();
                            String savedUserId = writerDocument.get("userId").toString();
                            if(savedUserId.equals(userId)) {
                                Toast.makeText(getContext(), "자신을 관심 목록에 넣을 수 없습니다." , Toast.LENGTH_SHORT).show();
                            } else {
                                setIcons.isInList(savedDocumentId, contentsList, "myStar", user, rcontentsStarImg
                                        ,listener, context);
                            }
                        }
                    } else {
                        Log.d(TAG, "setFavoriteAct : 즐겨찾기 등록할 사용자 정보 찾기 실패");
                    }
                }
            });
    }

    // 해당 글의 내용을 뿌려줌
    private void setContents(Map<String, Object> contentsList) {
    Log.d(TAG,"title 확인" + contentsList.get("title"));
    Map<String, Object> imageCommentList = (Map<String, Object>) contentsList.get("imageComment");
    String userLevel = contentsList.get("userLevel").toString();
    rcontentsTitleText.setText(contentsList.get("title").toString());
    if (contentsList.get("dtrName") != null) {
        rcontentsMapText.setText(contentsList.get("dtrName").toString());
    }
    rcontentsTaketimeText.setText(contentsList.get("hour").toString());
    rcontentsTakewhoText.setText(contentsList.get("isPartner").toString());
    rcontentsUsernameText.setText(contentsList.get("userName").toString());
    rcontentsLevelImg.setImageResource(appConstant.getUserLevelImage(userLevel));
    }


}



//https://machine-woong.tistory.com/53 스피너