package com.example.rotory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.rotory.Adapter.RCommAdapter;
import com.example.rotory.Interface.OnUserActItemClickListener;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Contents;
import com.example.rotory.account.LogInActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RoadContentsPage extends Fragment {
    final static String TAG = "RoadContentsPage";
    AppConstant appConstant = new AppConstant();


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

    TextView commReportText;
    Spinner reportSpinner;

    AppBarLayout appBarLayout;

    Boolean isSignIn = false;
    private FirestoreRecyclerAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance(); //db 선언
    private FirestoreRecyclerAdapter commentAdapter;

    MapView mapView;
    ViewGroup mapContainer;

    TextView commUsernameText;
    TextView commConText;
    TextView commTimeText;
    ImageView commLevelImg;

    EditText comment;


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

     /*   adapter = new FirestoreRecyclerAdapter<SearchContents, roadcontentsViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, "어댑터 작동");
            }

            @Override
            protected void onBindViewHolder(@NonNull roadcontentsViewHolder holder, int position, @NonNull SearchContents model) {
                holder.setContentsItems(model);
            }

*//*            @NonNull
            @Override
            public roadcontentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.road_contents_page);
                return ;
            }
         };*//*

        rCommRView.setAdapter(adapter);

        }

        @Override
        protected void onStart(){
            super.onStart();
            adapter.startListening();
        }

        @Override
        protected void onStop() {
            super.onStop();
            if(adapter != null) {
                adapter.stopListening();
            }
        }


        //정보추가recyclerView
        public class roadcontentsViewHolder extends RecyclerView.ViewHolder{
            private View view;

            public roadcontentsViewHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView;
            }

            public void setContentsItems(SearchContents comment) {
                commLevelImg
                commUsernameText
                commConText
                commTimeText
                commReportText


            }
        }
*/

        if (user != null) {
            initUI(rootView);
        }
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


    private void initUI(ViewGroup rootView) {
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
        rCommRView = rootView.findViewById(R.id.rCommRView);
        mapContainer = rootView.findViewById(R.id.rcontentsMap);

       /* mapView = new MapView(getActivity());
        mapContainer.addView(mapView);
*/
        Bundle contentsBundle = this.getArguments();
        String contentsID = contentsBundle.getString("storyDocumentId");
        //String contentsID = "kWgSA53rxrk5bMMemdVd";

        Log.d(TAG, "initUI 시작, 번들 전송 잘 됐는지, pDocumentId:" + contentsID);
        loadContents(contentsID, user);

        if (user != null) {
            getUserActivityIcon(contentsID, "myLike", rcontentsHeartImg,
                    R.drawable.heartfilled, R.drawable.heart);
            getUserActivityIcon(contentsID, "myScrap", rcontentsScrapImg,
                    R.drawable.scrabtagfilled, R.drawable.scrabtag);
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

        getUserActivityIcon(contentsID, "myStar", rcontentsStarImg,
                R.drawable.starfilled, R.drawable.star);

        rCommRView.setLayoutManager(new LinearLayoutManager(getContext()));

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rCommRView.setLayoutManager(layoutManager);

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
                        if (user.getEmail().equals(commentedUser)){
                            commReportText.setText("삭제");
                            commReportText.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view) {
                                    showToast("댓글을 삭제하셨습니다.");
                                }
                            });
                        } else {
                            commReportText.setText("신고");
                            commReportText.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view) {
                                    showToast("댓글을 신고하셨습니다.");
                                }
                            });
                        }
                    } else {
                        commReportText.setText("신고");
                        commReportText.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
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
    private void openReportDialog(QueryDocumentSnapshot pDocument, String pDocumentId) {
        reportSpinner = getView().findViewById(R.id.reportSpinner);

        ArrayAdapter reportAdapter = ArrayAdapter.createFromResource(getContext(), R.array.reportList, android.R.layout.simple_spinner_dropdown_item);

        reportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportSpinner.setAdapter(reportAdapter);

        reportSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

/*        db.collection("road")
                .whereEqualTo("contentsType", 0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "initUI : 스토리에서 받아오기 성공" + document.getId());
                                if (user != null) {
                                    getUserActivityIcon(document, "myLike", rcontentsHeartImg, R.drawable.heartfilled, R.drawable.heart);
                                    getUserActivityIcon(document, "myStar", rcontentsStarImg, R.drawable.starfilled, R.drawable.star);
                                    getUserActivityIcon(document, "myScrap", rcontentsScrapImg, R.drawable.scrabtagfilled, R.drawable.scrabtag);
                                }
                                loadContents(document, user);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents : ", task.getException());
                        }
                    }
                });
    }*/

    private void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void deleteComment(String documentId) {

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

    private void getUserActivityIcon(String cDocumentId, String userCollection, ImageView imageView,
                                     int listIn, int listOut) {
        db.collection("person").whereEqualTo("userId", user.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "person에서 사용자의 정보 받아오기 성공");
                            for (QueryDocumentSnapshot pDocument : task.getResult()) {
                                String pDocumentId = pDocument.getId();
                                db.collection("person").document(pDocumentId).collection(userCollection)
                                        .whereEqualTo("contentsId", cDocumentId)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                Log.d(TAG, userCollection + " getUserActivity 사용자의 활동리스트 정보 받아오기 성공 " + cDocumentId);
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot snapshot = task.getResult();
                                                    List userActList = new ArrayList();
                                                    userActList = snapshot.getDocuments();
                                                    if (userActList.size() > 0) {
                                                        imageView.setImageResource(listIn);
                                                    } else {
                                                        imageView.setImageResource(listOut);
                                                    }
                                                }
                                            }
                                        });
                            }
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
                            isInList(contentsID, contentsList, "myLike", user,
                                    rcontentsHeartImg, R.drawable.heartfilled, R.drawable.heart);
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
                            isInList(contentsID, contentsList, "myScrap", user,
                                    rcontentsScrapImg, R.drawable.scrabtagfilled, R.drawable.scrabtag);
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

        Log.d(TAG, "setFavoriteAct : " + contentsList.get("pDocumentId").toString());
        if (contentsList.get("pDocumentId").toString().equals(userId)) {
            Toast.makeText(getContext(), "자신을 관심 목록에 넣을 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            isInList(contentsList.get("pDocumentId").toString(), contentsList, "myStar", user, rcontentsStarImg,
                    R.drawable.starfilled, R.drawable.star);
        }

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
                                isInList(savedDocumentId, contentsList, "myStar", user, rcontentsStarImg, R.drawable.starfilled, R.drawable.star);
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
    rcontentsMapText.setText(contentsList.get("article").toString());
    rcontentsTaketimeText.setText(contentsList.get("hour").toString());
    rcontentsTakewhoText.setText(contentsList.get("isPartner").toString());
    rcontentsUsernameText.setText(contentsList.get("userName").toString());
    rcontentsLevelImg.setImageResource(getUserLevelImage(userLevel));
    }


    private void isInList(String contentsID, Map<String, Object> contentsList, String userCollection, FirebaseUser user,
                          ImageView imageView, int listIn, int listOut) {
        Log.d(TAG, "인리스트 메서드 작동 : 이미지 뷰 = " + imageView.toString());
        String writerUid = contentsList.get("uid").toString();
        db.collection("person").whereEqualTo("userId", user.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot pDocument : task.getResult()) {
                                String pDocumentId = pDocument.getId();
                                Log.d(TAG,"isInList : 해당 유저 고유 번호 받아옴" +  pDocumentId);
                                CollectionReference userCollectionRef = db.collection("person").document(pDocumentId).collection((userCollection));
                                if (userCollection.equals("myStar")){
                                    userCollectionRef
                                            .whereEqualTo("personId", contentsID)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "엔트리셋?" + contentsID);
                                                        Log.d(TAG, "사용자 활동리스트 정보 리스트에서 빼기");
                                                        imageView.setImageResource(listIn);
                                                        for (QueryDocumentSnapshot thisLikeDocument : task.getResult()) {
                                                            String userActDocId = thisLikeDocument.getId();
                                                            Log.d(TAG, "해당 다큐먼츠 찾기 => id" + userActDocId);
                                                            userCollectionRef.document(userActDocId).delete();
                                                            imageView.setImageResource(listOut);
                                                            Toast.makeText(getContext(), "관심 있는 이웃 취소", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                        Log.d(TAG, "사용자 활동리스트 정보 리스트에 넣기");
                                                        imageView.setImageResource(listOut);
                                                        listener.OnStarClicked(contentsID, user.getEmail());
                                                        Toast.makeText(getContext(), "관심있는 이웃 다람쥐로 등록", Toast.LENGTH_SHORT).show();
                                                        imageView.setImageResource(listIn);
                                                        return;
                                                    }
                                                }
                                            });
                                }
                                else {
                                    if (userCollection.contains(userCollection)) {
                                        Log.d(TAG, "isInList " + userCollection + " 호출 성공");
                                        userCollectionRef
                                                .whereEqualTo("contentsID", contentsID)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "엔트리셋?" + contentsID);
                                                            Log.d(TAG, "사용자 활동리스트 정보 리스트에서 빼기");
                                                            imageView.setImageResource(listIn);
                                                            for (QueryDocumentSnapshot thisLikeDocument : task.getResult()) {
                                                                String userActDocId = thisLikeDocument.getId();
                                                                Log.d(TAG, "해당 다큐먼츠 찾기 => id" + userActDocId);
                                                                userCollectionRef.document(userActDocId).delete();
                                                                imageView.setImageResource(listOut);
                                                                Toast.makeText(getContext(), "이 도토리를 버립니다.", Toast.LENGTH_SHORT).show();
                                                                return;
                                                            }
                                                            Log.d(TAG, "사용자 활동리스트 정보 리스트에 넣기");
                                                            imageView.setImageResource(listOut);
                                                            if (userCollection.equals("myLike")) {
                                                                listener.OnLikeClicked(contentsID, contentsList, user.getEmail());
                                                                Toast.makeText(getContext(), "이 도토리를 좋아합니다.", Toast.LENGTH_SHORT).show();
                                                            } else if (userCollection.equals("myScrap")) {
                                                                //스크랩 구현 아직 안함
                                                                listener.OnFlagClicked(contentsID, contentsList, user.getEmail());
                                                                Toast.makeText(getContext(), "이 도토리를 담아갑니다.", Toast.LENGTH_SHORT).show();
                                                            }
                                                            imageView.setImageResource(listIn);
                                                            return;
                                                        }
                                                    }
                                                });
                                    }

                                }
                            }
                        }
                    }
                });

    }

    //다람쥐 레벨용 이미지는 프로그램 내부에 넣어놓고, 유저레벨에 따라 불러서 사용
    private int getUserLevelImage(String userLevel) {
        switch (userLevel) {
            case "어린다람쥐":
                return R.drawable.level2;
            case "학생다람쥐":
                return R.drawable.level3;
            case "어른다람쥐":
                return R.drawable.level4;
            case "박사다람쥐":
                return R.drawable.level5;
            case "다람쥐의 신":
                return R.drawable.level6;
            default:
                return R.drawable.level1;
        }
    }
}

/*

            DocumentReference docRef = db.collection("contents").document(contentsID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "자료 받아오기 성공");
                            Map<String, Object> ContentsList = new HashMap<>();
                            ContentsList = document.getData();
                            setContents(ContentsList);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }

                private void setContents(Map<String, Object> contentsList) {
                }
            });
        }

        private void setContents(Map<String, Object> contentsList) {
            rcontentsTitleText.setText(contentsList.get("roadTitle").toString());
            rcontentsTakeTimeText.setText(contentsList.get("roadTaketime").toString());
            rcontentsTakewhoText.setText(contentsList.get("roadTakewho").toString());

        }

    }
*/

//https://machine-woong.tistory.com/53 스피너