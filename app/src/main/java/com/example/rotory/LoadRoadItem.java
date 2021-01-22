package com.example.rotory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.Interface.OnUserActItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class LoadRoadItem extends AppCompatActivity implements OnUserActItemClickListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    final static String TAG = "LoadStoryItem";
    ImageButton backImageButton;

    RoadContentsPage roadContentsPage = new RoadContentsPage();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_story_contents);

        onStart();


        getSupportFragmentManager().beginTransaction().replace(R.id.storyContainer, roadContentsPage).commit();
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();

        //String documentID = intent.getStringExtra("documentId");
        String documentID = "kWgSA53rxrk5bMMemdVd";
        Log.d(TAG, documentID);

        Bundle pDocumentIdBundle = new Bundle();
        pDocumentIdBundle.putString("storyDocumentId", documentID);
        Log.d(TAG, pDocumentIdBundle.getString("storyDocumentId"));
        roadContentsPage.setArguments(pDocumentIdBundle);
    }


    @Override
    public void OnStarClicked(String savedUserId, String myUserId) {
        Log.d(TAG, "onStarClicked : " + savedUserId);

        db.collection("person")
                .document(savedUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"onStared : 저장할 사용자 정보 받아오기 성공");
                            Map<String, Object> persons = new HashMap<>();
                            persons = task.getResult().getData();
                            Map<String, Object> myStar = new HashMap<>();
                            myStar.put("personId", savedUserId);
                            myStar.put("savedDate", new Date().toString());
                            myStar.put("userName", persons.get("userName"));
                            myStar.put("userLevel", persons.get("userLevel"));
                            myStar.put("userImage", persons.get("userImage"));
                            myStar.put("uid", persons.get("uid")); //이후 리스트에 포함되어있는지 여부를 찾기 위해 해당 항목 사용
                            Log.d(TAG, "onStared  맵에 잘 들어갔나?" + persons.get("userName"));
                            String userCollection = "myStar";
                            saveUserAct(myUserId, myStar,userCollection);
                        }
                    }
                });
    }

    @Override
    public void OnLikeClicked(String contentsId, Map<String, Object> contentsList, String userId) {
        Log.d(TAG, "onClicked: user에서 아이디 잘 받아옴?" + userId);
        Map<String, Object> myLike = new HashMap<>();
        myLike.put("contentsId",contentsId);
        myLike.put("contentsType", contentsList.get("contentsType"));
        myLike.put("title",contentsList.get("title").toString());
        myLike.put("titleImage",contentsList.get("titleImage").toString());
        myLike.put("savedDate", new Date().toString());
        myLike.put("uid",contentsList.get("uid").toString());//이후 리스트에 포함되어있는지 여부를 찾기 위해 해당 항목 사용
        String userCollection = "myLike";
        saveUserAct(userId, myLike, userCollection);


    }
    @Override
    public void OnFlagClicked(String contentsId, Map<String, Object> contentsList, String userId) {

        Map<String, Object> myScrap = new HashMap<>();
        myScrap.put("contentsId",contentsId);
        myScrap.put("contentsType", contentsList.get("contentsType"));
        myScrap.put("title", contentsList.get("title").toString());
        myScrap.put("titleImage", contentsList.get("titleImage").toString());
        myScrap.put("article", contentsList.get("article").toString());
        myScrap.put("contentsAddress", contentsList.get("address").toString());
        myScrap.put("savedDate", new Date().toString());
        myScrap.put("uid", contentsList.get("uid").toString());//이후 리스트에 포함되어있는지 여부를 찾기 위해 해당 항목 사용
        String userCollection = "myScrap";
        saveUserAct(userId, myScrap, userCollection);

    }
    //star, Like, Flag 폴더에 저장하는 메서드
    private void saveUserAct(String userId, Map<String, Object> myLike, String userCollection) {
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
                                            .collection(userCollection).document()
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

    @Override
    public void OnLinkClicked() {

    }

}
