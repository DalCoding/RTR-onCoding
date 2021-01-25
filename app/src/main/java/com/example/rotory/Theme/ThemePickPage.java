package com.example.rotory.Theme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.ProgressDialogs;
import com.example.rotory.R;
import com.example.rotory.VO.Tag;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThemePickPage extends Activity {
<<<<<<< HEAD
    private final static String TAG = "ThemePickPage";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    TagItemAdapter tagItemAdapter;

    OnTagItemClickListener listener;

    TextView activity;
    TextView feel;
    TextView mood;
    TextView place;
    TextView season;
    TextView time;
    TextView tagListSize;

    RecyclerView activityTagRecyclerView;
    RecyclerView feelTagRecyclerView;
    RecyclerView moodTagRecyclerView;
    RecyclerView placeTagRecyclerView;
    RecyclerView seasonTagRecyclerView;
    RecyclerView timeTagRecyclerView;

    ProgressDialogs progressDialogs;

    Button checkBtn;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        tagItemAdapter = null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.theme_pick_card);

        progressDialogs = new ProgressDialogs(this);
        progressDialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialogs.show();

        tagListSize = findViewById(R.id.tagListSize);
        checkBtn = findViewById(R.id.tagCheckBtn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        user = mAuth.getCurrentUser();
        setTagNum();

        activity = findViewById(R.id.tpickGroupText);
        activity.setText("활동");
        activityTagRecyclerView = findViewById(R.id.activityTagRecyclerView);
         getTagList("activity", activityTagRecyclerView);

        feel = findViewById(R.id.tpickGroupText2);
        feel.setText("기분");
        feelTagRecyclerView = findViewById(R.id.feelTagRecyclerView);
        getTagList("feel", feelTagRecyclerView);

        mood = findViewById(R.id.tpickGroupText3);
        mood.setText("분위기");
        moodTagRecyclerView = findViewById(R.id.moodTagRecyclerView);
       getTagList("mood", moodTagRecyclerView);

        place = findViewById(R.id.tpickGroupText4);
        place.setText("장소");
        placeTagRecyclerView = findViewById(R.id.placeTagRecyclerView);
        getTagList("place", placeTagRecyclerView);

        season = findViewById(R.id.tpickGroupText5);
        season.setText("계절");
        seasonTagRecyclerView = findViewById(R.id.seasonTagRecyclerView);
        getTagList("season", seasonTagRecyclerView);

        time = findViewById(R.id.tpickGroupText6);
        time.setText("시간");
        timeTagRecyclerView = findViewById(R.id.timeTagRecyclerView);
        getTagList("time", timeTagRecyclerView);

        startTagsLoading();

    }

    private void startTagsLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               progressDialogs.dismiss();
            }
        }, 2000);
    }

    private void setTagNum() {
        db.collection("person").whereEqualTo("userId", user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        String pDocumentId = documentSnapshot.getId();
                        db.collection("person").document(pDocumentId)
                                .collection("myTag").document("myTagList")
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                Map<String, Object> myTagList = task.getResult().getData();
                                int tagSize = myTagList.size();
                                tagListSize.setText(String.valueOf(tagSize));
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"태그개수 가져오기 실패" + e.toString());
            }
        });
    }
    void getTagList(String tagList, RecyclerView recyclerView) {

        GridLayoutManager tagLayoutManager = new GridLayoutManager(ThemePickPage.this, 4);
        recyclerView.setLayoutManager(tagLayoutManager);


        db.collection("tag").document(tagList)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Map<String, Object> tagItemsMap = new HashMap<>();
                    tagItemsMap = task.getResult().getData();
                    Collection<Object> tagItemCollection =tagItemsMap.values();
                    ArrayList<Object> tagItemList = new ArrayList<>(tagItemCollection);
                    ArrayList<Tags> tagsItemList = new ArrayList<>();
                    for (int i = 0 ; i < tagItemList.size(); i++){
                        tagsItemList.add(new Tags(tagItemList.get(i).toString()));
                    }

                    Log.d(TAG,"태그 아이템 리스트 뽑은거 확인" + tagsItemList);
                    tagItemAdapter = new TagItemAdapter(ThemePickPage.this,tagsItemList, listener, tagListSize);
                    recyclerView.setAdapter(tagItemAdapter);
                    Log.d(TAG,"리스너 작동 전");


                    Log.d(TAG,"list 확인" + tagItemList);

                }
            }
        });
    }

}
=======
        private final static String TAG = "ThemePickPage";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user;

        TagItemAdapter tagItemAdapter;

        OnTagItemClickListener listener;

        TextView activity;
        TextView feel;
        TextView mood;
        TextView place;
        TextView season;
        TextView time;
        TextView tagListSize;


        RecyclerView activityTagRecyclerView;
        RecyclerView feelTagRecyclerView;
        RecyclerView moodTagRecyclerView;
        RecyclerView placeTagRecyclerView;
        RecyclerView seasonTagRecyclerView;
        RecyclerView timeTagRecyclerView;
        ProgressDialogs progressDialogs;

        Button checkBtn;

        @Override
        protected void onStart() {
            super.onStart();

        }

        @Override
        protected void onStop() {
            super.onStop();
            tagItemAdapter = null;
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.theme_pick_card);

            progressDialogs = new ProgressDialogs(this);
            progressDialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialogs.show();

            tagListSize = findViewById(R.id.tagListSize);
            checkBtn = findViewById(R.id.tagCheckBtn);
            checkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            user = mAuth.getCurrentUser();
            setTagNum();

            activity = findViewById(R.id.tpickGroupText);
            activity.setText("활동");
            activityTagRecyclerView = findViewById(R.id.activityTagRecyclerView);
            getTagList("activity", activityTagRecyclerView);


            feel = findViewById(R.id.tpickGroupText2);
            feel.setText("기분");
            feelTagRecyclerView = findViewById(R.id.feelTagRecyclerView);
            getTagList("feel", feelTagRecyclerView);

            mood = findViewById(R.id.tpickGroupText3);
            mood.setText("분위기");
            moodTagRecyclerView = findViewById(R.id.moodTagRecyclerView);
            getTagList("mood", moodTagRecyclerView);

            place = findViewById(R.id.tpickGroupText4);
            place.setText("장소");
            placeTagRecyclerView = findViewById(R.id.placeTagRecyclerView);
            getTagList("place", placeTagRecyclerView);

            season = findViewById(R.id.tpickGroupText5);
            season.setText("계절");
            seasonTagRecyclerView = findViewById(R.id.seasonTagRecyclerView);
            getTagList("season", seasonTagRecyclerView);

            time = findViewById(R.id.tpickGroupText6);
            time.setText("시간");
            timeTagRecyclerView = findViewById(R.id.timeTagRecyclerView);
            getTagList("time", timeTagRecyclerView);

            startTagsLoading();

        }

        private void startTagsLoading() {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialogs.dismiss();
                }
            }, 2000);
        }

        private void setTagNum() {
            db.collection("person").whereEqualTo("userId", user.getEmail())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                            String pDocumentId = documentSnapshot.getId();
                            db.collection("person").document(pDocumentId)
                                    .collection("myTag").document("myTagList")
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Map<String, Object> myTagList = task.getResult().getData();
                                    int tagSize = myTagList.size();
                                    tagListSize.setText(String.valueOf(tagSize));
                                }
                            });
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"태그개수 가져오기 실패" + e.toString());
                }
            });
        }
        void getTagList(String tagList, RecyclerView recyclerView) {

            GridLayoutManager tagLayoutManager = new GridLayoutManager(ThemePickPage.this, 4);
            recyclerView.setLayoutManager(tagLayoutManager);

            db.collection("tag").document(tagList)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        Map<String, Object> tagItemsMap = new HashMap<>();
                        tagItemsMap = task.getResult().getData();
                        Collection<Object> tagItemCollection =tagItemsMap.values();
                        ArrayList<Object> tagItemList = new ArrayList<>(tagItemCollection);
                        ArrayList<Tags> tagsItemList = new ArrayList<>();
                        for (int i = 0 ; i < tagItemList.size(); i++){
                            tagsItemList.add(new Tags(tagItemList.get(i).toString()));
                        }

                        Log.d(TAG,"태그 아이템 리스트 뽑은거 확인" + tagsItemList);
                        tagItemAdapter = new TagItemAdapter(ThemePickPage.this,tagsItemList, listener, tagListSize);
                        recyclerView.setAdapter(tagItemAdapter);
                        Log.d(TAG,"리스너 작동 전");


                        Log.d(TAG,"list 확인" + tagItemList);

                    }
                }
            });
        }

    }

>>>>>>> origin/master
