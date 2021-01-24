package com.example.rotory.Theme;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.R;
import com.example.rotory.VO.Tag;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class ThemePickPage extends AppCompatActivity {
    private final static String TAG = "ThemePickPage";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    TextView activity;
    TextView feel;
    TextView mood;
    TextView place;
    TextView season;
    TextView time;
    TextView tagListSize;

    ArrayList<String> tagSelecteList = new ArrayList<>();

    RecyclerView activityTagRecyclerView;
    RecyclerView feelTagRecyclerView;
    RecyclerView moodTagRecyclerView;
    RecyclerView placeTagRecyclerView;
    RecyclerView seasonTagRecyclerView;
    RecyclerView timeTagRecyclerView;

    ThemePicker themePicker = new ThemePicker();
/*    // 그리드 설치
    myRecyclerView = findViewById(R.id.myRecyclerView);
    GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        myRecyclerView.setLayoutManager(layoutManager);

        MyPage 참고*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_pick_card);
        tagListSize = findViewById(R.id.tagListSize);

        activity = findViewById(R.id.tpickGroupText);
        activity.setText("활동");
        activityTagRecyclerView = findViewById(R.id.activityTagRecyclerView);
        themePicker.getTagList("activity",
                activityTagRecyclerView,ThemePickPage.this,
                tagListSize);

        feel = findViewById(R.id.tpickGroupText2);
        feel.setText("기분");
        feelTagRecyclerView = findViewById(R.id.feelTagRecyclerView);
        themePicker.getTagList("feel", feelTagRecyclerView, ThemePickPage.this
                         , tagListSize);


        mood = findViewById(R.id.tpickGroupText3);
        mood.setText("분위기");
        moodTagRecyclerView = findViewById(R.id.moodTagRecyclerView);
        themePicker.getTagList("mood", moodTagRecyclerView, ThemePickPage.this,
                tagListSize);

        place = findViewById(R.id.tpickGroupText4);
        place.setText("장소");
        placeTagRecyclerView = findViewById(R.id.placeTagRecyclerView);
        themePicker.getTagList("place", placeTagRecyclerView, ThemePickPage.this
                , tagListSize);

        season = findViewById(R.id.tpickGroupText5);
        season.setText("계절");
        seasonTagRecyclerView = findViewById(R.id.seasonTagRecyclerView);
        themePicker.getTagList("season", seasonTagRecyclerView, ThemePickPage.this
                , tagListSize);

        time = findViewById(R.id.tpickGroupText6);
        time.setText("시간");
        timeTagRecyclerView = findViewById(R.id.timeTagRecyclerView);
        themePicker.getTagList("time", timeTagRecyclerView, ThemePickPage.this
                , tagListSize);




    }



    private void updateUI() {
    }


}
