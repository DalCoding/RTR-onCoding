package com.example.rotory.Contents;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
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
import com.example.rotory.Interface.OnContentsItemClickListener;
import com.example.rotory.Interface.OnUserActItemClickListener;
import com.example.rotory.R;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Contents;
import com.example.rotory.VO.Person;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Map;


public class StoryContentsPage extends Fragment {
    final static String TAG = "StoryContentsPage";
    static AppConstant appConstant = new AppConstant();
    SetIcons setIcons = new SetIcons();
    OnContentsItemClickListener imageListener;

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
    TextView reportTextView;
    TextView scontentsTextText;

    RecyclerView scontentsThumbnailRView;

    EditText scontentsCommEdit;
    Button scontentsCommBtn;
    RecyclerView sCommRView;

    FirebaseFirestore db = FirebaseFirestore.getInstance(); //db 선언
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();  //로그인하고있는유저

    StoryImageAdapter imageAdapter;

    String contentsID;

    Context context;
    OnUserActItemClickListener listener;

    static TextView commReportText;
    Spinner reportSpinner;


    static TextView commUsernameText;
    static TextView commConText;
    static TextView commTimeText;
    static ImageView commLevelImg;

    Boolean isSignIn = false;

    private FirestoreRecyclerAdapter commentAdapter;

    @Override
    public void onResume() {
        super.onResume();
        updateUI();

    }

    private void updateUI() {

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

        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저 네임 : " + checkLogIN);
            isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
        }

        Query query = db.collection("SearchContents").whereEqualTo("contentsType", 0)
                .orderBy("contentsType", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Contents> options = new FirestoreRecyclerOptions.Builder<Contents>()
                .setQuery(query, Contents.class)
                .build();

        if (user != null) {
            initUI(rootView);
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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
        commentAdapter.startListening();
        sCommRView.setAdapter(commentAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        sCommRView.setLayoutManager(layoutManager);

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
                                    //showToast("댓글을 신고하셨습니다.");
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
                        showToast(user.getDisplayName() + "님의 도토리! 자신의 도토리는 담을 수 없습니다.");
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
        String userLevel = contentsList.get("userLevel").toString();

        scontentsTitleText.setText(contentsList.get("title").toString());
        scontentsTextText.setText(contentsList.get("article").toString());
        scontentsLocText.setText(contentsList.get("address").toString());
        scontentsUsernameText.setText(contentsList.get("userName").toString());
        scontentsLevelImg.setImageResource(appConstant.getUserLevelImage(userLevel));

        setImageRecyclerView(contentsList);

    }

    private void setImageRecyclerView(Map<String, Object> contentsList) {
        Map<String, Object> stringImageMap = new HashMap<>();
        stringImageMap  = (Map<String, Object>) contentsList.get("smallImage");
        ArrayList<Bitmap> bitmapImageList = new ArrayList<>();
        for (int i = 0; i < stringImageMap.size(); i ++){
            String key = "image"+(i+1);
            Bitmap imageBitmap = appConstant.StringToBitmap(String.valueOf(stringImageMap.get(key)));
            bitmapImageList.add(imageBitmap);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        scontentsThumbnailRView.setLayoutManager(layoutManager);
        imageAdapter = new StoryImageAdapter(bitmapImageList, getContext(),imageListener);
        scontentsThumbnailRView.setAdapter(imageAdapter);

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


