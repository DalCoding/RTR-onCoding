package com.example.rotory.Contents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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


import com.example.rotory.Comment;
import com.example.rotory.Interface.OnUserActItemClickListener;
import com.example.rotory.R;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;


public class StoryContentsPage extends Fragment  {
    final static String TAG = "StoryContentsPage";
    static AppConstant appConstant = new AppConstant();

    SetIcons setIcons = new SetIcons();
    CommentAdapter commentAdapter;
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

    static FirebaseFirestore db = FirebaseFirestore.getInstance(); //db 선언
    static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static FirebaseUser user = mAuth.getCurrentUser();  //로그인하고있는유저
    FirestoreRecyclerOptions<Comment> options;

    //String userEmail = user.getEmail();

    CommentViewHolder commentViewHolder;
    ArrayList<Comment> commentArrayList;
    String contentsID;

    static Context context;
    OnUserActItemClickListener listener;

    static TextView commReportText;
    Spinner reportSpinner;

    //Comment comments = new Comment();
    Person persons = new Person();

    static TextView commUsernameText;
    static TextView commConText;
    static TextView commTimeText;
    static ImageView commLevelImg;

    EditText comment;

    //private FirestoreRecyclerAdapter commentAdapter;


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
    public void onResume() {
        super.onResume();
        updateUI();

    }

    private void updateUI() {

        if (commentAdapter == null) {
            Log.d(TAG,"코멘트 업댑터 = null");
            commentAdapter = new CommentAdapter(options, contentsID);
            commentAdapter.startListening();


        }else {
            Log.d(TAG,"코멘트 업댑터 존재, 데이터 변경 확인");
            commentAdapter.notifyDataSetChanged();
        }

    }

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

        commentAdapter.stopListening();
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

    private void initUI(ViewGroup rootView) {
        scontentsCommBtn = rootView.findViewById(R.id.scontentsCommBtn);
        scontentsLinkImg = rootView.findViewById(R.id.scontentsLinkImg);
        scontentsScrapImg = rootView.findViewById(R.id.scontentsScrapImg);
        scontentsHeartImg = rootView.findViewById(R.id.scontentsHeartImg);
        scontentsTitleText = rootView.findViewById(R.id.scontentsTitleText);
        scontentsLevelImg = rootView.findViewById(R.id.scontentsLevelImg);
        scontentsUsernameText = rootView.findViewById(R.id.scontentsUsernameText);
        scontentsStarImg = rootView.findViewById(R.id.scontentsStarImg);
        scontentsCommEdit = rootView.findViewById(R.id.scontentsCommEdit);
        sCommRView = rootView.findViewById(R.id.sCommRView);
        scontentsBigImg = rootView.findViewById(R.id.scontentsBigImg);
        scontentsMentText = rootView.findViewById(R.id.scontentsMentText);
        scontentsThumbnailRView = rootView.findViewById(R.id.scontentsThumbnailRView);
        scontentsTextText = rootView.findViewById(R.id.scontentsTextText);
        scontentsLocText = rootView.findViewById(R.id.scontentsLocText);

        Bundle contentsBundle = this.getArguments();
        contentsID = contentsBundle.getString("storyDocumentId");
        contentsID = "LLNVEsSg2hzVa75gEIvw";
        Log.d(TAG, "initUi 시작, 번들 전송 잘됐는지 확인, pDocumentId :" + contentsID);
        loadContents(contentsID, user);

        if (user != null) {
            setIcons.getUserActivityIcon(contentsID, "myLike", scontentsHeartImg,
                    R.drawable.heartfilled, R.drawable.heart);
            setIcons.getUserActivityIcon(contentsID, "myScrap", scontentsScrapImg,
                    R.drawable.scrabtagfilled, R.drawable.scrabtag);

        }

        scontentsCommBtn.setOnClickListener(new View.OnClickListener() {
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

        setIcons.getUserActivityIcon(contentsID, "myStar", scontentsStarImg,
                R.drawable.starfilled, R.drawable.star);

        sCommRView.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = db.collection("contents")
                .document(contentsID).collection("comment")
                .orderBy("savedDate", Query.Direction.DESCENDING);
        Log.d(TAG, "쿼리문 확인" + query);

        options = new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .build();

        Log.d(TAG, "recyclerView options 확인" + options);

        updateUI();
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

     static class CommentViewHolder extends RecyclerView.ViewHolder {
        public View view;


        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setCommentItems(Comment comment) {
            Log.d(TAG, "set CommentItems 시작");
            String pDocumentId = String.valueOf(comment.getPersonId());
            Log.d(TAG, "pDocumentID 확인" + pDocumentId);
            Log.d(TAG,"set CommentItems 시작");
            commUsernameText = view.findViewById(R.id.commUsernameText);
            commConText = view.findViewById(R.id.commConText);
            commTimeText = view.findViewById(R.id.commTimeText);
            commReportText = view.findViewById(R.id.commReportText);
            commLevelImg = view.findViewById(R.id.commLevelImg);
            //Log.d(TAG,"컨텐츠 확인" + comment.getPersonId() + ":" + user.getEmail());

            commConText.setText(comment.getComment());
            commTimeText.setText(comment.getSavedDate());

            db.collection("person").document(pDocumentId)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.d(TAG, "자료불러옴" + task.getResult().getId());
                         HashMap<String, Object> document = (HashMap<String, Object>) task.getResult().getData();
                       if (document != null) {
                           Log.d(TAG, String.valueOf(document));
                       }else{
                           Log.d(TAG, "값없음");
                           String userName = document.get("userName").toString();
                           String userLevel = document.get("userLevel").toString();
                           commUsernameText.setText(userName);
                           commLevelImg.setImageResource(appConstant.getUserLevelImage(userLevel));
                       }

                        String commentedUser = document.get("userId").toString();
                        Log.d(TAG, "댓글단사람" + commentedUser);
                        Log.d(TAG, "현재 사용자 확인" + user.getEmail());
                        if (user != null) {
                            if (user.getEmail().equals(commentedUser)) {
                                commReportText.setText("삭제");
                                commReportText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //deleteComment();
                                        showToast("댓글을 삭제하셨습니다.");

                                    }
                                });
                            } else {
                                commReportText.setText("신고");
                                commReportText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //openReportDialog();
                                        showToast("댓글을 신고하셨습니다.");
                                    }
                                });
                            }
                        } else {
                            commReportText.setText("신고");
                            commReportText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showToast("로그인이 필요한 서비스입니다.");
                                }
                            });
                        }

                    }
            });

        }


       /* private void openReportDialog(QueryDocumentSnapshot pDocument, String pDocumentId) {
            reportSpinner = getView().findViewById(R.id.reportSpinner);

            ArrayAdapter reportAdapter = ArrayAdapter.createFromResource(context, R.array.reportList, android.R.layout.simple_spinner_dropdown_item);

        }*/

    }


    //다이얼로그 만들기+띄우기
    //다이얼로그 메세지 누르면 받아오기 String report...
    //report 콜렉션에 들어갈 정보 만들기 Map.put....(reportText, personId, reportedId, reported_Date)


    private static void showToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    private void deleteComment(String documentId) {
        // db.collection("contents").document(documentId).collection("comment")
        //.whereEqualTo("comment",scontentsCommEdit.getText().toString() )

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
                        showToast(user.getDisplayName() + "님의 도토리! 자신의 도토리는 담을 수 없습니다.");
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
                    if (writerUid.equals(user.getUid())) {
                        showToast(user.getDisplayName() + "님의 도토리! 자신의 도토리는 담을 수 없습니다.");
                    } else {
                        setFavoriteAct(contentsList, user);
                        Log.d(TAG, "스타아이콘 클릭");
                    }
                }
            });
            scontentsScrapImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (writerUid.equals(user.getUid())) {
                       showToast( user.getDisplayName() + "님의 도토리! 자신의 도토리는 담을 수 없습니다.");
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
        }
    }


    private void saveComment(String contentsId, View v) {
        //FirebaseUser user = mAuth.getCurrentUser();
        String userEmail = user.getEmail();
        String comment = scontentsCommEdit.getText().toString();
        Log.d(TAG, "saveComment" + comment);

        HashMap<String, Object> result = new HashMap<>();
        result.put("comment", comment); //EditText 적힌 내용 가져오기
        result.put("contentsId", contentsId);
        result.put("commentType", "댓글");
        result.put("uid", user.getUid());
        result.put("savedDate", appConstant.dateFormat.format(new Date()));
        // db. collection person , whereEqualto "userId", user,getEmail . get task.getresult -> getid

        Log.d(TAG, "UserEmail받아옴" + userEmail);

        db.collection("person").whereEqualTo("userId", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String personId = documentSnapshot.getId();
                                Log.d(TAG, "personId 정보? " + personId);
                                result.put("personId", personId);
                                writerNewUser(contentsId, result);
                            }
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "saveCommnet onsuccessListener 확인 " + queryDocumentSnapshots.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "saveCommnet 유저 정보 불러오기 실패 : " + e.toString());
            }
        });
    }


    //댓글 달면 데이터 추가
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

    //사용자의 아이디와 글쓴이의 아이디를 비교해 같을경우 즐겨찾기를 할 수 없도록 설정(자기 자신 즐겨찾기 못함)
    private void setFavoriteAct(Map<String, Object> contentsList, FirebaseUser user) {
        String userId = user.getEmail();

        Log.d(TAG, "setFavoriteAct : " + contentsList.get("pDocumentId").toString());
        if (contentsList.get("pDocumentId").toString().equals(userId)) {
            showToast("자신을 관심 목록에 넣을 수 없습니다.");
        } else {
            isInList(contentsList.get("pDocumentId").toString(), contentsList, "myStar", user, scontentsStarImg,
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
                                    showToast("자신을 관심 목록에 넣을 수 없습니다.");

                                } else {
                                    isInList(savedDocumentId, contentsList, "myStar", user, scontentsStarImg, R.drawable.starfilled, R.drawable.star);
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
        String userLevel = contentsList.get("userLevel").toString();
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
                                if (userCollection.equals("myStar")) {
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
                                                            showToast("관심있는 이웃 취소");
                                                            return;
                                                        }
                                                        Log.d(TAG, "사용자 활동리스트 정보 리스트에 넣기");
                                                        imageView.setImageResource(listOut);
                                                        listener.OnStarClicked(contentsId, user.getEmail());
                                                        showToast("관심있는 이웃 다람쥐로 등록");
                                                        imageView.setImageResource(listIn);
                                                        return;
                                                    }
                                                }
                                            });

                                } else {
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
                                                                showToast("이 도토리를 버립니다.");
                                                                return;
                                                            }
                                                            Log.d(TAG, "사용자 활동리스트 정보 리스트에 넣기");
                                                            imageView.setImageResource(listOut);
                                                            if (userCollection.equals("myLike")) {
                                                                listener.OnLikeClicked(contentsId, contentsList, user.getEmail());
                                                                showToast("이 도토리를 좋아합니다.");
                                                            } else if (userCollection.equals("myScrap")) {
                                                                //스크랩 구현 아직 안함
                                                                listener.OnFlagClicked(contentsId, contentsList, user.getEmail());
                                                                showToast("이 도토리를 담아갑니다.");
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
}



    // comment 시간 표시(n분 전...)    : TextView commTimeText;
/*    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
        }*/

    //마지막에 적용하기...!
/*    public static String formatTimeString(long regTime) {
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
    }*/


