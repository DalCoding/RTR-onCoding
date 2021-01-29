package com.example.rotory.Contents;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rotory.Interface.OnContentsItemClickListener;
import com.example.rotory.Interface.OnUserActItemClickListener;
import com.example.rotory.R;
import com.example.rotory.RoadContentsPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SetIcons {
    private final static String TAG = "setIcons";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    public void getUserActivityIcon(String cDocumentId, String userCollection, ImageView imageView,
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
                                if (userCollection.equals("myStar")){
                                 db.collection("person").document(pDocumentId).collection(userCollection)
                                 .whereEqualTo("personId", cDocumentId)
                                 .get()
                                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                     @Override
                                     public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                                 }) ;
                                }else {
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
                    }
                });
    }

    public void isInList(String contentsID, Map<String, Object> contentsList, String userCollection, FirebaseUser user,
                         ImageView imageView, OnUserActItemClickListener listeners, Context context) {

        OnUserActItemClickListener listener = (OnUserActItemClickListener) listeners;
        Log.d(TAG, "인리스트 메서드 작동 : 이미지 뷰 = " + imageView.toString());
        String writerUid = contentsList.get("uid").toString();
        db.collection("person").whereEqualTo("userId", user.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot pDocument : task.getResult()) {
                                String pDocumentId = pDocument.getId();
                                Log.d(TAG, "isInList : 해당 유저 고유 번호 받아옴" + pDocumentId);
                                CollectionReference userCollectionRef = db.collection("person").
                                        document(pDocumentId).collection(userCollection);
                                if (userCollection.equals("myStar")) {
                                    userCollectionRef
                                            .whereEqualTo("personId", contentsID)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {

                                                        imageView.setImageResource(R.drawable.starfilled);
                                                        for (QueryDocumentSnapshot thisLikeDocument : task.getResult()) {
                                                            String userActDocId = thisLikeDocument.getId();
                                                            Log.d(TAG, "해당 다큐먼츠 찾기 => id" + userActDocId);
                                                            userCollectionRef.document(userActDocId).delete();
                                                            imageView.setImageResource(R.drawable.star);
                                                            Toast.makeText(context, "관심 있는 이웃 취소", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                        Log.d(TAG, "사용자 활동리스트 정보 리스트에 넣기");
                                                        imageView.setImageResource(R.drawable.star);
                                                        listener.OnStarClicked(contentsID, user.getEmail());
                                                        Toast.makeText(context, "관심있는 이웃 다람쥐로 등록", Toast.LENGTH_SHORT).show();
                                                        imageView.setImageResource(R.drawable.starfilled);
                                                        return;
                                                    }
                                                }
                                            });
                                } else if (userCollection.equals("myScrap")) {
                                    Log.d(TAG, "isInList " + userCollection + " 호출 성공");
                                    userCollectionRef
                                            .whereEqualTo("contentsId", contentsID)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "엔트리셋?" + contentsID);
                                                        for (QueryDocumentSnapshot thisLikeDocument : task.getResult()) {
                                                            Log.d(TAG, "사용자 활동리스트 정보 리스트에서 빼기");
                                                            String userActDocId = thisLikeDocument.getId();
                                                            Log.d(TAG, "해당 다큐먼츠 찾기 => id" + userActDocId);
                                                            userCollectionRef.document(userActDocId).delete();
                                                            imageView.setImageResource(R.drawable.scrabtag);
                                                            Toast.makeText(context, "이 도토리를 버립니다.", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                            listener.OnFlagClicked(contentsID, contentsList, user.getEmail());
                                                            Log.d(TAG, "사용자 활동리스트 정보 리스트에 넣기");
                                                            Toast.makeText(context, "이 도토리를 좋아합니다.", Toast.LENGTH_SHORT).show();
                                                            imageView.setImageResource(R.drawable.scrabtagfilled);
                                                        return;
                                                    }
                                                }
                                            });
                                } else if (userCollection.equals("myLike")) {
                                    Log.d(TAG, "isInList " + userCollection + " 호출 성공");
                                    userCollectionRef
                                            .whereEqualTo("contentsId", contentsID)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "엔트리셋?" + contentsID);
                                                        for (QueryDocumentSnapshot thisLikeDocument : task.getResult()) {
                                                            Log.d(TAG, "사용자 활동리스트 정보 리스트에서 빼기");
                                                            String userActDocId = thisLikeDocument.getId();
                                                            Log.d(TAG, "해당 다큐먼츠 찾기 => id" + userActDocId);
                                                            userCollectionRef.document(userActDocId).delete();
                                                            imageView.setImageResource(R.drawable.heart);
                                                            Toast.makeText(context, "이 도토리를 버립니다.", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                        listener.OnLikeClicked(contentsID, contentsList, user.getEmail());
                                                        Log.d(TAG, "사용자 활동리스트 정보 리스트에 넣기");
                                                        Toast.makeText(context, "이 도토리를 좋아합니다.", Toast.LENGTH_SHORT).show();
                                                        imageView.setImageResource(R.drawable.heartfilled);
                                                        return;

                                                    }
                                                }
                                            });

                                }
                            }
                        }
                    }
                });
    }
}
