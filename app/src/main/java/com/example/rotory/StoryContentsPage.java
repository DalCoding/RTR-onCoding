package com.example.rotory;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnUserActItemClickListener;
import com.example.rotory.VO.Comment;
import com.example.rotory.account.LogInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryContentsPage extends Fragment {
    final static String TAG = "StoryContentsPage";

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
    FirebaseUser user = mAuth.getCurrentUser();

    ArrayList<Comment> commentArrayList;

    Context context;
    OnUserActItemClickListener listener;



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

        initUI(rootView);
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
        scontentsCommEdit = rootView.findViewById(R.id.rcontentsCommEdit);
        sCommRView = rootView.findViewById(R.id.sCommRView);
        scontentsBigImg = rootView.findViewById(R.id.scontentsBigImg);
        scontentsMentText = rootView.findViewById(R.id.scontentsMentText);
        scontentsThumbnailRView = rootView.findViewById(R.id.scontentsThumbnailRView);
        scontentsTextText = rootView.findViewById(R.id.scontentsTextText);
        scontentsLocText = rootView.findViewById(R.id.scontentsLocText);

        db.collection("contents").whereEqualTo("contentsType", 1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (user != null) {
                                    getUserActivityIcon(document, "myLike", scontentsHeartImg, R.drawable.heartfilled, R.drawable.heart);
                                    getUserActivityIcon(document, "myStar", scontentsStarImg, R.drawable.starfilled, R.drawable.star);
                                    getUserActivityIcon(document, "myScrap", scontentsScrapImg, R.drawable.scrabtagfilled, R.drawable.scrabtag);
                                }
                                loadContents(document, user);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents : ", task.getException());
                        }
                    }
                });
    }

    private void loadContents(QueryDocumentSnapshot contentsData, FirebaseUser user) {
        String contentsID = contentsData.getId();
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
                        findWriter(ContentsList.get("uid").toString());
                        setContents(ContentsList);
                        clickUserActIcon(contentsData, ContentsList, user);


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    private void getUserActivityIcon(QueryDocumentSnapshot cDocument, String userCollection, ImageView imageView,
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
                                        .whereEqualTo("uid", cDocument.get("uid"))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                Log.d(TAG, userCollection + "사용자의 활동리스트 정보 받아오기 성공" + cDocument.get("uid").toString());
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot snapshot = task.getResult();
                                                    List userActList = new ArrayList();
                                                    userActList = snapshot.getDocuments();
                                                    Log.d(TAG, userCollection + "리스트 사이즈는?" + userActList.size() + "\n리스트 아이템?"/* + userActList.get(0)*/);
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


    private void clickUserActIcon(QueryDocumentSnapshot contentsData, Map<String, Object> contentsList,
                                  FirebaseUser user) {
        // 로그인 정보 없을경우 다이얼로그 먼저 띄워주기 " 로그인이 필요한 서비스 입니다"
        String writerUid = contentsData.get("uid").toString();
        if (user != null) {
            scontentsHeartImg.setClickable(true);
            scontentsHeartImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (writerUid.equals(user.getUid())) {
                        Toast.makeText(getContext(), "자신의 글을 저장할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        isInList(contentsData.getId(), "myLike", user, writerUid,
                                scontentsHeartImg, R.drawable.heartfilled, R.drawable.heart);

                    }
                }
            });

            scontentsStarImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFavoriteAct(contentsList.get("uid").toString(), user);
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
        }
    }

    //글쓴이의 정보를 뿌려줌
    private void findWriter(String uid) {

        db.collection("person").whereEqualTo("uid", uid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userName = document.get("userName").toString();
                        String userLevel = document.get("userLevel").toString();
                        scontentsUsernameText.setText(userName);
                        scontentsLevelImg.setImageResource(getUserLevelImage(userLevel));
                    }
                } else {
                    Log.d(TAG, "findWriter : 글쓴이 정보 받아오기 실패");
                }
            }
        });
    }

    //사용자의 아이디와 글쓴이의 아이디를 비교해 같을경우 즐겨찾기를 할수 없도록 설정(자기 자신 즐겨찾기 못함)
    private void setFavoriteAct(String uid, FirebaseUser user) {
        String userId = user.getEmail();
        db.collection("person")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String savedDocumentId = document.getId();
                                String savedUserId = document.get("userId").toString();
                                if (savedUserId.equals(userId)) {
                                    Toast.makeText(getContext(), "자신을 즐겨찾기 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    listener.OnStarClicked(savedDocumentId, userId);
                                    Toast.makeText(getContext(), "이 사용자를 즐겨찾기합니다.", Toast.LENGTH_SHORT).show();
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

        scontentsTitleText.setText(contentsList.get("title").toString());

        //scontentsBigImg.setImage(contentsList.get("titleImage").toString());
        // scontentsMentText.setText(contentsList.get("storyMent").toString());
        scontentsTextText.setText(contentsList.get("storyText").toString());
        scontentsLocText.setText(contentsList.get("storyAddress").toString());

    }

    // 사용자의 각 사용자행동리스트에서 해당 글을 저장했는지 여부를 확인할때,
    // 해당 글쓴이의 uid와 리스트에 저장된 uid를 비교해 찾음(사용자 행동 리스트에 넣을때 uid 항목 포함)
    // 저장여부에따라 아이콘 띄우고 이후행동,
    //클릭시 이 메서드 호출
    private void isInList(String contentsId, String userCollection, FirebaseUser user, String writerUid,
                          ImageView imageView, int listIn, int listOut) {
        Log.d(TAG, "인리스트 메서드 작동 : 이미지 뷰 = " + imageView.toString());

        db.collection("person").whereEqualTo("userId", user.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String pDocumentId = document.getId();
                                db.collection("person").document(pDocumentId).collection(userCollection)
                                        .whereEqualTo("uid", writerUid)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot document = task.getResult();
                                                    List docList = new ArrayList();
                                                    docList = document.getDocuments();
                                                    Log.d(TAG, "리스트 사이즈는?" + docList.size());
                                                    Log.d(TAG, "사용자 활동리스트 정보 리스트에 넣기");
                                                    if (docList.size() > 0) {
                                                        imageView.setImageResource(listIn);

                                                    } else {
                                                        imageView.setImageResource(listOut);
                                                        if (userCollection.equals("myLike")) {
                                                            listener.OnLikeClicked(contentsId, user.getEmail());
                                                            Toast.makeText(getContext(), "이 글을 좋아합니다.", Toast.LENGTH_SHORT).show();
                                                        } else if (userCollection.equals("myScrap")) {
                                                            //스크랩 구현 아직 안함
                                                            listener.OnFlagClicked();
                                                            Toast.makeText(getContext(), "이 글을 스크랩합니다.", Toast.LENGTH_SHORT).show();
                                                        }
                                                        imageView.setImageResource(listIn);
                                                    }
                                                } else {
                                                    Log.d(TAG, "사용자의 활동리스트 접근 실패");
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    //다람쥐 레벨용 이미지는 프로그램 내부에 넣어놓고, 유저레벨애 따라 불러서 사용
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
                                    myLike.put("contentsType", contents.get("contentsType").toString());
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

