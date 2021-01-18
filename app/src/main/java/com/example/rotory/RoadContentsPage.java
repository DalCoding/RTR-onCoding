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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.rotory.Adapter.RCommAdapter;
import com.example.rotory.Interface.OnUserActItemClickListener;
import com.example.rotory.VO.Contents;
import com.example.rotory.account.LogInActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RoadContentsPage extends Fragment {
    public static final int loginCode = 3000;
    final static String TAG = "RoadContentsPage";

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
    //RCommAdapter adapter;

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
    FirebaseFirestore db;

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

        Query query = db.collection("Contents").whereEqualTo("contentsType", 1)
                .orderBy("contentsType", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Contents> options = new FirestoreRecyclerOptions.Builder<Contents>()
                .setQuery(query, Contents.class)
                .build();

     /*   adapter = new FirestoreRecyclerAdapter<Contents, roadcontentsViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, "어댑터 작동");
            }

            @Override
            protected void onBindViewHolder(@NonNull roadcontentsViewHolder holder, int position, @NonNull Contents model) {
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

            public void setContentsItems(Contents comment) {
                commLevelImg
                commUsernameText
                commConText
                commTimeText
                commReportText


            }
        }
*/
        initUI(rootView);
        return rootView;
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rCommRView.setLayoutManager(layoutManager);

      /*  adapter = new RCommAdapter();

        rCommRView.setAdapter(adapter);
*/

        String userId = user.getEmail();

        db.collection("road")
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
    }

    private void loadContents(QueryDocumentSnapshot contentsData, FirebaseUser user) {
        String contentsID = contentsData.getId();
        DocumentReference docRef = db.collection("road").document(contentsID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "자료 받아오기 완료");
                        Map<String, Object> ContentsList = new HashMap<>();
                        ContentsList = document.getData();
                        Log.d(TAG, "title 확인" + ContentsList.get("title"));
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

    private void getUserActivityIcon(QueryDocumentSnapshot cDocument, String userCollection, ImageView imageView, int listIn, int listOut) {
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
                                                Log.d(TAG, userCollection + " getUserActivity 사용자의 활동리스트 정보 받아오기 성공 " + cDocument.get("uid").toString());
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
                rcontentsHeartImg.setClickable(true);
                rcontentsHeartImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (writerUid.equals(user.getUid())) {
                            Toast.makeText(getContext(), user.getDisplayName() + " 님의 도토리! 자신의 도토리는 담을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "하트 아이콘 클릭");
                            isInList(contentsId, contentsList, "myLike", user, rcontentsHeartImg, R.drawable.heartfilled, R.drawable.heart);
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
                            isInList(contentsId, contentsList, "myScrap", user, rcontentsScrapImg, R.drawable.scrabtagfilled, R.drawable.scrabtag);
                        }
                        Log.d(TAG, "태그 아이콘 클릭");
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
                                Log.d(TAG,"isInList : 해당 유저 고유 번호 받아옴" +  pDocumentId);
                                CollectionReference userCollectionRef = db.collection("person").document(pDocumentId).collection((userCollection));
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
                                                            Toast.makeText(getContext(), "관심 있는 이웃 취소", Toast.LENGTH_SHORT).show();
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
                                                                Log.d(TAG, "해당 다큐먼츠 찾기 => id" + userActDocId);
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