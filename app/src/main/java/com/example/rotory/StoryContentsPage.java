package com.example.rotory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Adapter.LocationAdapter;
import com.example.rotory.Adapter.SCommAdapter;
import com.example.rotory.Interface.OnUserActItemClickListener;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Person;
import com.example.rotory.account.LogInActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
/*
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;*/

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.http.HEAD;

public class StoryContentsPage extends Fragment {
    final static String TAG = "StoryContentsPage";
    AppConstant appConstant = new AppConstant();

    /*
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;*/

    TextView scontentsGroupText;
    ImageView scontentsLinkImg;
    ImageView scontentsScrapImg;
    ImageView scontentsHeartImg;
    ImageView scontentsStarImg;
    TextView scontentsLocText;
    TextView scontentsTitleText;
    ImageView scontentsLevelImg;
    TextView scontentsUsernameText;
    ImageView scontentsBigImg;
    TextView scontentsMentText;
    RecyclerView scontentsThumbnailRView;
    TextView scontentsTextText;
    ImageView scontentsNextImg;
    EditText scontentsCommEdit;
    Button scontentsCommBtn;
    RecyclerView sCommRView;

    FirebaseFirestore db = FirebaseFirestore.getInstance(); //db 선언
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();  //로그인하고있는유저
    private FirestoreRecyclerAdapter commentAdapter;
    //String userEmail = user.getEmail();

    ArrayList<Comment> commentArrayList;

    Context context;
    OnUserActItemClickListener listener;

    TextView commReportText;
    Spinner reportSpinner;

    //Comment comments = new Comment();
    Person persons = new Person();

    TextView commUsernameText;
    TextView commConText;
    TextView commTimeText;
    ImageView commLevelImg;

    EditText comment;


    // 스피너
/*    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reportSpinner = getView().findViewById(R.id.reportSpinner);

        ArrayAdapter reportAdapter = ArrayAdapter.createFromResource(this, R.array.reportList, android.R.layout.simple_spinner_dropdown_item);
        reportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportSpinner.setAdapter(reportAdapter);

        reportSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.story_contents_page, container, false);

/*      ActionBar ab = getSupportActionBar();
        ab.setTitle("ActionBar Title by setTitle()");*/
        //상단바 제목 바꾸기

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            initUI(rootView);
        }
        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
        commentAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        commentAdapter.stopListening();
    }

    private void initUI(ViewGroup rootView) {
        scontentsCommBtn = rootView.findViewById(R.id.scontentsCommBtn);
        scontentsLinkImg = rootView.findViewById(R.id.scontentsLinkImg);
        scontentsScrapImg = rootView.findViewById(R.id.scontentsScrapImg);
        scontentsHeartImg = rootView.findViewById(R.id.scontentsHeartImg);
        scontentsTitleText = rootView.findViewById(R.id.scontentsTitleText);
        scontentsLevelImg = rootView.findViewById(R.id.scontentsLevelImg);
        scontentsUsernameText = rootView.findViewById(R.id.scontentsUsernameText);
        scontentsStarImg = rootView.findViewById(R.id.scontentsStarImg);
        scontentsCommEdit = rootView.findViewById(R.id.rcontentsCommEdit);
        sCommRView = rootView.findViewById(R.id.sCommRView);
        scontentsBigImg = rootView.findViewById(R.id.scontentsBigImg);
        scontentsMentText = rootView.findViewById(R.id.scontentsMentText);
        scontentsThumbnailRView = rootView.findViewById(R.id.scontentsThumbnailRView);
        scontentsTextText = rootView.findViewById(R.id.scontentsTextText);
        scontentsLocText = rootView.findViewById(R.id.scontentsLocText);

        Bundle contentsBundle = this.getArguments();
        //String contentsID = contentsBundle.getString("storyDocumentId");

       String contentsID = "LLNVEsSg2hzVa75gEIvw";

        Log.d(TAG, "initUi 시작, 번들 전송 잘됐는지 확인, pDocumentId :" + contentsID);
        loadContents(contentsID, user);

        if (user != null) {
            getUserActivityIcon(contentsID, "myLike", scontentsHeartImg,
                    R.drawable.heartfilled, R.drawable.heart);
            getUserActivityIcon(contentsID, "myScrap", scontentsScrapImg,
                    R.drawable.scrabtagfilled, R.drawable.scrabtag);

        }

        getUserActivityIcon(contentsID, "myStar", scontentsStarImg,
                R.drawable.starfilled, R.drawable.star);

        sCommRView.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = db.collection("contents")
                .document(contentsID).collection("comment")
                .orderBy("savedDate", Query.Direction.DESCENDING);
        Log.d(TAG, "쿼리문 확인" + query);

        FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .build();

        Log.d(TAG, "recyclerView options 확인" + options);

        makeCommentAdapter(options);
        //commentAdapter.startListening();
        sCommRView.setAdapter(commentAdapter);

/*        db.collection("person")
                .whereEqualTo("userId", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String personId = document.getId();

                               Comment(personId);

                                String userName1 = String.valueOf(document.get("userName"));
                                commUsernameText = getView().findViewById(R.id.commUsernameText);
                                commUsernameText.setText(userName1);
                                String userText = String.valueOf(document.get("userText"));
                                commConText = getView().findViewById(R.id.commConText);
                                commConText.setText(userText);
                                userLevelImg = getView().findViewById(R.id.userLevelImg);
                                userLevelImg.setImageResource(appConstant.getUserLevelImage(userLevelImg));
                                commReportText = getView().findViewById(R.id.commReportText);
                                commReportText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showReportDialog(document, document.getId());
                                        //신고창 띄우기
                                    }
                                });
                            }
                        } else {
                            Log.d("firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });*/

/*
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("person")
                .whereEqualTo("userId", user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSussessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String personId = document.getId();

                    }
            }
        });
*/

    }

    private void makeCommentAdapter(FirestoreRecyclerOptions<Comment> options) {
        commentAdapter = new FirestoreRecyclerAdapter<Comment, CommentViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, " 어댑터 작동");
            }
            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
                return new CommentViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment model) {
                Log.d(TAG, "onBindViewHolder 작동" + holder.itemView.toString());
                holder.setCommentItems(model);
            }
        };
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private View view;


        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            //comment = itemView.findViewById(R.id.commConText);
        }

        public void setCommentItems(Comment comment) {
            Log.d(TAG,"set CommentItems 시작");
            commUsernameText = view.findViewById(R.id.commUsernameText);
            commConText = view.findViewById(R.id.commConText);
            commTimeText = view.findViewById(R.id.commTimeText);
            commReportText = view.findViewById(R.id.commReportText);
            commLevelImg = view.findViewById(R.id.commLevelImg);
            Log.d(TAG,"컨텐츠 확인" + comment.getPersonId() + ":" + user.getEmail());
            db.collection("person")
                    .whereEqualTo("userId", user.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userLevel = String.valueOf(document.get("userLevel"));
                                Log.d(TAG, userLevel);
                                commLevelImg.setImageResource(appConstant.getUserLevelImage(userLevel));
                            }
                        }
                    });
            String pDocumentId =
                    "32RlqqiGlefRWRXSA16c";
 //String.valueOf(comment.getPersonId());


            db.collection("person").document(pDocumentId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Log.d(TAG, "자료불러옴" + task.getResult().getId());

                  HashMap<String, Object> document = (HashMap<String, Object>) task.getResult().getData();
                   if (document != null) {
                       Log.d(TAG, String.valueOf(document));
                   }else{
                       Log.d(TAG, String.valueOf(document) + "값없음");
                   }
                        String userName = document.get("userName").toString();
                        String userLevel = document.get("userLevel").toString();
                        commUsernameText.setText(userName);
                        commLevelImg.setImageResource(appConstant.getUserLevelImage(userLevel));
                        String commentedUser = task.getResult().get("userId").toString();
                        if (user != null){
                            if (user.getEmail().equals(commentedUser)){
                                commReportText.setText("삭제");
                                commReportText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        deleteComment();
                                        showToast("댓글을 삭제하셨습니다.");
                                    }
                                });
                            }else{
                                commReportText.setText("신고");
                                commReportText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        openReportDialog();
                                        showToast("댓글을 신고하셨습니다.");
                                    }
                                });
                            }
                        }
                }
            });

            commConText.setText(comment.getComment().toString());
            commTimeText.setText(comment.getSavedDate().toString());
            Log.d(TAG, "정보 입력 확인" + commConText.getText().toString()+
                    "=>" + comment.getComment());
        }
    }

    private void openReportDialog() {
    }

    private void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void deleteComment() {
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
        // 해당 글의 아이디 받아오는 방식으로 바꾼 후에 디비 연결문 수정! -> QueryDocumentSnapshot 대신 contentsId(=해당 다큐먼트의 아이디)
        // 받아 cDocument 입력되는 자리에 넣고  contentsId 키와 비교
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
                                                Log.d(TAG, userCollection + " getUserActivity 사용자의 활동리스트 정보 받아오기 성공" + cDocumentId);
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot snapshot = task.getResult();
                                                    List userActList = new ArrayList();
                                                    userActList = snapshot.getDocuments();
                                                    //Log.d(TAG, userCollection + "리스트 사이즈는?" + userActList.size() + "\n리스트 아이템?"/* + userActList.get(0)*/);
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


    private void clickUserActIcon(String contentsId, Map<String, Object> contentsList,
                                  FirebaseUser user) {
        // 로그인 정보 없을경우 다이얼로그 먼저 띄워주기 " 로그인이 필요한 서비스 입니다"
        Log.d(TAG, "ClickActIcon 시작");
        String writerUid = contentsList.get("uid").toString();
        if (user != null) {
            scontentsHeartImg.setClickable(true);
            scontentsHeartImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (writerUid.equals(user.getUid())) {
                        showToast(user.getDisplayName() +"님의 도토리! 자신의 도토리는 담을 수 없습니다.");
                    } else {
                        Log.d(TAG, "하트아이콘 클릭");
                        isInList(contentsId, contentsList, "myLike", user,
                                scontentsHeartImg, R.drawable.heartfilled, R.drawable.heart);
                    }
                }
            });
            scontentsStarImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (writerUid.equals(user.getUid())){
                        Toast.makeText(getContext(), user.getDisplayName() +"님의 도토리! 자신의 도토리는 담을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        setFavoriteAct(contentsList, user);
                        Log.d(TAG, "스타아이콘 클릭");
                    }
                }
            });
            scontentsScrapImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (writerUid.equals(user.getUid())) {
                        Toast.makeText(getContext(), user.getDisplayName() +"님의 도토리! 자신의 도토리는 담을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        isInList(contentsId, contentsList, "myScrap", user,
                                scontentsScrapImg, R.drawable.scrabtagfilled, R.drawable.scrabtag);
                        Log.d(TAG, "태그아이콘 클릭");
                    }

                }
            });
        } else {
            scontentsHeartImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent logInIntent = new Intent(getContext(), LogInActivity.class);
                    startActivity(logInIntent);
                }
            });
            scontentsStarImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent logInIntent = new Intent(getContext(), LogInActivity.class);
                    startActivity(logInIntent);
                }
            });
            scontentsScrapImg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent logInIntent = new Intent(getContext(), LogInActivity.class);
                    startActivity(logInIntent);
                }
            });

            //댓글 저장 버튼 누르면 저장되도록
            scontentsCommBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    /*String getUserName  = ;
                    String getUserLevel = ;
                    String getTime= ;*/

                    HashMap<String, Object> result = new HashMap<>();
                    result.put("comment", ""/*gettext()*/); //EditText 적힌 내용 가져오기

                   /* String getUserName  = ;
                    String getUserLevel = ;
                    String getTime= ;

                    HashMap<String, Object> result = new HashMap<>();
                    result.put("comment", gettext()); //EditText 적힌 내용 가져오기
                    result.put("contentsId", contentsId);
                    result.put("contentsType","댓글");
                    //result.put("personId", "아이디 넣기!" _) // db. collection person , whereEqualto "userId", user,getEmail . get task.getresult -> getid

                    //person collection db 한번 돌리고 onSuccess  안에서 result.put"personId"


                     result.put("savedDate", new Date().toString());
                     result.put("uid", user.getUid());


                    writerNewUser(contentsId,result);*/

                }
            });
        }
    }

    //댓글 달면 데이터 추가
    private void writerNewUser(String documentId, HashMap<String,Object> commentData) {
        //User user = new User(name, email);

        db.collection("contents").document(documentId).collection("comment")
                .add(commentData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,"데이터저장성공");
                        showToast("글이 추가되었습니다");
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "저장 실패...");
                    }
                });
    }


    //사용자의 아이디와 글쓴이의 아이디를 비교해 같을경우 즐겨찾기를 할 수 없도록 설정(자기 자신 즐겨찾기 못함)
    private void setFavoriteAct(Map<String, Object> contentsList, FirebaseUser user) {
        String userId = user.getEmail();

        Log.d(TAG,"setFavoriteAct : " + contentsList.get("pDocumentId").toString());
        if (contentsList.get("pDocumentId").toString().equals(userId)){
            Toast.makeText(getContext(), "자신을 관심 목록에 넣을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
        else {
            isInList(contentsList.get("pDocumentId").toString(), contentsList, "myStar", user,  scontentsStarImg,
                    R.drawable.starfilled, R.drawable.star);
        }

        db.collection("person").whereEqualTo("uid", contentsList.get("uid")).get() //글쓴이 정보 검색
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot writerDocument : task.getResult()) {
                                String savedDocumentId = writerDocument.getId();
                                String savedUserId = writerDocument.get("userId").toString();
                                if (savedUserId.equals(userId)) { //글쓴이의 아이디와 비교
                                    Toast.makeText(getContext(), "자신을 관심 목록에 넣을 수 없습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    isInList(savedDocumentId, contentsList, "myStar", user,  scontentsStarImg, R.drawable.starfilled, R.drawable.star);
                                }
                            }


                        } else {
                            Log.d(TAG, "setFavoriteAct : 즐겨찾기 등록할 사용자 정보 찾기 실패");
                        }
                    }
                });

    }

    //해당 글의 내용을 뿌려줌
    private void setContents(Map<String, Object> contentsList) {
        Log.d(TAG, "title확인" + contentsList.get("title"));
        Map<String, Object> imageCommentList = (Map<String, Object>) contentsList.get("imageComment");
        String userLevel =contentsList.get("userLevel").toString();
        scontentsTitleText.setText(contentsList.get("title").toString());
        //scontentsBigImg.setImage(contentsList.get("titleImage").toString());
        scontentsMentText.setText(imageCommentList.get("image1").toString());
        scontentsTextText.setText(contentsList.get("article").toString());
        scontentsLocText.setText(contentsList.get("address").toString());
        scontentsUsernameText.setText(contentsList.get("userName").toString());
        scontentsLevelImg.setImageResource(appConstant.getUserLevelImage(userLevel));

    }

    // 사용자의 각 사용자행동리스트에서 해당 글을 저장했는지 여부를 확인할때,
    // 해당 글쓴이의 uid와 리스트에 저장된 uid를 비교해 찾음(사용자 행동 리스트에 넣을때 uid 항목 포함)
    // 저장여부에따라 아이콘 띄우고 이후행동,
    //클릭시 이 메서드 호출
    private void isInList(String contentsId, Map<String, Object> contentsList, String userCollection, FirebaseUser user,
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
                                Log.d(TAG, "isInList : 해당 유저 고유번호 받아옴" + pDocumentId);
                                CollectionReference userCollectionRef = db.collection("person").document(pDocumentId).collection(userCollection);
                                if (userCollection.equals("myStar")){
                                    userCollectionRef
                                            .whereEqualTo("personId", contentsId)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "엔트리셋?" + contentsId);
                                                        Log.d(TAG, "사용자 활동리스트 정보 리스트에서 빼기");
                                                        imageView.setImageResource(listIn);
                                                        for (QueryDocumentSnapshot thisLikeDocument : task.getResult()) {
                                                            String userActDocId = thisLikeDocument.getId();
                                                            Log.d(TAG, "해당 다큐먼츠 찾기 => id" + userActDocId);
                                                            userCollectionRef.document(userActDocId).delete();
                                                            imageView.setImageResource(listOut);
                                                            Toast.makeText(getContext(), "관심있는 이웃 취소", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                        Log.d(TAG, "사용자 활동리스트 정보 리스트에 넣기");
                                                        imageView.setImageResource(listOut);
                                                        listener.OnStarClicked(contentsId, user.getEmail());
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
                                                .whereEqualTo("contentsId", contentsId)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "엔트리셋?" + contentsId);
                                                            Log.d(TAG, "사용자 활동리스트 정보 리스트에서 빼기");
                                                            imageView.setImageResource(listIn);
                                                            for (QueryDocumentSnapshot thisLikeDocument : task.getResult()) {
                                                                String userActDocId = thisLikeDocument.getId();
                                                                Log.d(TAG, "해당다큐먼츠 찾기 => id" + userActDocId);
                                                                userCollectionRef.document(userActDocId).delete();
                                                                imageView.setImageResource(listOut);
                                                                Toast.makeText(getContext(), "이 도토리를 버립니다.", Toast.LENGTH_SHORT).show();
                                                                return;
                                                            }
                                                            Log.d(TAG, "사용자 활동리스트 정보 리스트에 넣기");
                                                            imageView.setImageResource(listOut);
                                                            if (userCollection.equals("myLike")) {
                                                                listener.OnLikeClicked(contentsId, contentsList, user.getEmail());
                                                                Toast.makeText(getContext(), "이 도토리를 좋아합니다.", Toast.LENGTH_SHORT).show();
                                                            } else if (userCollection.equals("myScrap")) {
                                                                //스크랩 구현 아직 안함
                                                                listener.OnFlagClicked(contentsId, contentsList, user.getEmail());
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



    // comment 시간 표시(n분 전...)    : TextView commTimeText;
    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    public static String formatTimeString(long regTime) {
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - regTime) /1000;
        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC) {
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }


/*    public void showReportDialog(QueryDocumentSnapshot pDocument, String pDocumentId){
        reportReport.setAdmitButton("확인", new Dialog)
            //report 제출 버튼
        }*/

}

    /*
    private void clickLikeImage(String contentsID,String userId) {
        //좋아요 버튼 누르면 해당 글 정보 받아가기
                Log.d(TAG, "user에서 아이디 잘 받아옴?" + userId);

                db.collection("contents")
                        .document(contentsID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "콘텐츠 페이지에서 해당 다큐먼트 받아오기 성공");
                                    Map<String, Object> contents = new HashMap<>();
                                    contents = task.getResult().getData();
                                    Map<String, Object> myLike = new HashMap<>();
                                    myLike.put("contentsId",contentsID);
                                    myLike.put("contentsType", contents.get("contentsType"));
                                    myLike.put("title", contents.get("title").toString());
                                    myLike.put("titleImage", contents.get("titleImage").toString());
                                    myLike.put("likedDate", new Date().toString());
                                    Log.d(TAG, "맵에 잘 들어갔니?" + myLike.get("title"));
                                    saveMyLike(userId, myLike);

                                }
                            }
                        });
            }



    private void saveMyLike(String userId, Map<String, Object> myLike) {
        //1. userId-> 현재사용자에게서 받아온 사용자아이디로 사용자의 고유번호 찾기
        //2. 해당 고유번호 이용해서 사용자 자료 아래에 좋아요 폴더 생성
        db.collection("person")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "유저 아이디로 사용자 찾기 성공");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userDId = document.getId();
                                Log.d(TAG, "아이디 확인" + document.getId() + "==>" + userDId);
                                if (userDId != null) {
                                    db.collection("person").document(userDId)
                                            .collection("myLike").document()
                                            .set(myLike)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "myLike 목록에 저장 성공");

                                                    } else {
                                                        Log.d(TAG, "테이블에 저장 실패");
                                                    }
                                                }

                                            });
                                } else {
                                    Log.d(TAG, "userId 없음" + userDId);
                                }
                            }

                        }
                    }
                });
    }

        scontentsCommBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    saveComment();
                } else if() {
                    modifyComment();
                }
            }
        });
        scontentsScrapImg.setOnUserActItemClickListener(new View.OnUserActItemClickListener(){
            @Override
            public void onFlagClicked(View v) {
                if (listener != null) {
                    saveScrap();
                } else if() {
                    modifyScrap();
                }
            }

            public void onLikeClicked(View v) {
                if (listener != null) {
                    saveLike();
                } else if() {
                    modifyLike();
                }
            }

            public void onStarClicked(View v) {
                if (listener != null) {
                    saveStar();
            } else if() {
                    modifyStar();
                }
        })
*/

    /*
     db.collection("contents")
        .whereEqualTo("uid", "2qrzhnM2B1XQWtQZfBlKkImsbBq2")
         .get()
         .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
         @Override
         public void onComplete(@NonNull Task<QuerySnapshot> task) {
          if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
               Log.d(TAG, "컨텐츠 받아오기 성공");
              //Map<String, Object> contentsList = new HashMap<>();
                String documentId = document.getId();
                Log.d(TAG, document.getId() + "==>" + documentId);
             }
         }

 }

 });*/