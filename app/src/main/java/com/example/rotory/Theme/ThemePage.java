package com.example.rotory.Theme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnTabItemSelectedListener;
import com.example.rotory.MainActivity;
import com.example.rotory.MyPage;
import com.example.rotory.ProgressDialogs;
import com.example.rotory.R;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Information;
import com.example.rotory.account.LogInActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThemePage extends AppCompatActivity implements OnTabItemSelectedListener {
    final static String TAG = "ThemePage";

    AppConstant appConstant = new AppConstant();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;
    BottomNavigationView bottomNavigation;


    Button tagSelectBtn;
    Button themeRefreshBtn;
    RecyclerView themeRView;
    ThemePickPage themePickPage;

    Display display;

    Information information;

    ThemeItemAdapter adapter;
    ArrayList<Tags> tagsArrayList = new ArrayList<>();

    ProgressDialogs progressDialogs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_page);


        progressDialogs = new ProgressDialogs(this);
        progressDialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialogs.show();
        startTagsLoading(2000);


        user = mAuth.getCurrentUser();

        themeRefreshBtn = findViewById(R.id.themeRefreshBtn);
        themeRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(ThemePage.this, ThemePage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        setUserTheme();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

        tagSelectBtn = findViewById(R.id.tagSelectBtn);
        tagSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialogs.show();
                startTagsLoading(300);
                Intent tagSelectIntent = new Intent(getApplicationContext(), ThemePickPage.class);
                startActivity(tagSelectIntent);
            }
        });

        bottomNavigation = findViewById(R.id.bottom_appBar);
        setBottomNavigation(bottomNavigation);
        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);


    }

    private void setUserTheme() {
        themeRView = findViewById(R.id.themeRView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ThemePage.this,2);
        themeRView.setLayoutManager(gridLayoutManager);


        db.collection("person").whereEqualTo("userId", user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot pDocument : task.getResult()){
                        String personId = pDocument.getId();
                        db.collection("person").document(personId).collection("myTag")
                                .document("myTagList").get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Map<String, Object> tagList = task.getResult().getData();
                                          if (tagList != null) {
                                              Set<String> tagKeySet = tagList.keySet();
                                              ArrayList<String> tagKeyArrayList = new ArrayList<>(tagKeySet);
                                              if (tagList.size() > 0) {
                                                  for (int i = 0; i < tagList.size()+1; i++) {
                                                      if (i > 0) {
                                                          tagsArrayList.add(new Tags(tagKeyArrayList.get(i-1)));
                                                      } else {
                                                          tagsArrayList.add(new Tags(tagKeyArrayList.get(i)));
                                                      }
                                                  }
                                                  int randomTagCount = 7 - tagList.size();
                                                  Log.d(TAG, "태그갯수? =>" + tagList.size() + ":" + randomTagCount);
                                                  setRandomTheme(randomTagCount, tagsArrayList);
                                              }else if (tagList.size() == 0) {
                                                  setRandomTheme(8, tagsArrayList);
                                              }
                                          }else{
                                              setRandomTheme(8, tagsArrayList);
                                          }

                                               /* adapter = new ThemeItemAdapter(tagsArrayList, ThemePage.this, display);
                                                themeRView.setAdapter(adapter);*/
                                             /*   }else{

                                                setRandomTheme(randomTagCount);
                                                //안에서 어댑터 처리
                                            }*/
                                        }
                                    }
                                });
                    }
                }
            }
        });

    }

    public void startTagsLoading(int millis) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                progressDialogs.dismiss();
            }
        }, millis);
    }

    private void setRandomTheme(int randomTagCount, ArrayList<Tags> tagsArrayList) {
        db.collection("tag").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Map<String, Object> tagMap = new HashMap<>();
                    ArrayList<String> tagItemList = new ArrayList<>();
                    ArrayList<String> randomTagList = new ArrayList<>();
                    for (QueryDocumentSnapshot tagDocument : task.getResult()){
                        tagMap = tagDocument.getData();
                        for (String mapKey : tagMap.keySet()){
                            tagItemList.add(tagMap.get(mapKey).toString());
                        }
                        }
                    Log.d(TAG, "전체 태그 받아옴" + tagItemList);
                    for (int i = 0; i <randomTagCount; i++) {
                        double randomValue = Math.random();
                        int random = (int) ((randomValue*tagItemList.size())-1);
                        randomTagList.add(tagItemList.get(random));
                    }
                    Log.d(TAG,"랜덤 리스트 받아옴" + randomTagList);
                    for (int i = 0; i < randomTagCount; i++){
                        tagsArrayList.add(new Tags(randomTagList.get(i)));
                    }
                    double randomValue = Math.random();
                    int random = (int) ((randomValue*tagItemList.size())-1);
                    tagsArrayList.set(0, new Tags(tagItemList.get(random)));
                    Log.d(TAG,"어댑터로 넘어갈 리스트" + tagsArrayList);
                    adapter = new ThemeItemAdapter(tagsArrayList, ThemePage.this, display);
                    themeRView.setAdapter(adapter);
                }
            }
        });
    }


    public void setBottomNavigation(BottomNavigationView bottomNavigation) {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        progressDialogs.show();
                        startTagsLoading(300);
                        Intent intent = new Intent(ThemePage.this, ThemePage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        bottomNavigation.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.theme:
                        return true;
                    case R.id.user:
                        progressDialogs.show();
                        startTagsLoading(300);
                        Intent myPageIntent = new Intent(ThemePage.this, MyPage.class);
                        myPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myPageIntent);
                        return true;
                }
                return false;
            }
        });
    }

    public void setTabUnderBar(int position) {
        if (position == 0) {
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.VISIBLE);
            bottomNavUnderbarTheme.setVisibility(View.GONE);
            bottomNavUnderbarUser.setVisibility(View.GONE);
        } else if (position == 1) {
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.INVISIBLE);
            bottomNavUnderbarTheme.setVisibility(View.VISIBLE);
            bottomNavUnderbarUser.setVisibility(View.INVISIBLE);
        } else if (position == 2) {
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.GONE);
            bottomNavUnderbarTheme.setVisibility(View.GONE);
            bottomNavUnderbarUser.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnTabSelected(int position) {
        if(position == 0){
            bottomNavigation.setSelectedItemId(R.id.home);
        }else if(position == 1){
            bottomNavigation.setSelectedItemId(R.id.theme);
        }else if(position ==2){
            bottomNavigation.setSelectedItemId(R.id.user);
        }

    }
}

    /*
        Query query = db.collection("contents").orderBy("theme", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<SearchContents> options = new FirestoreRecyclerOptions.Builder<SearchContents>()
                .setQuery(query, SearchContents.class)
                .build();
        themeRView = findViewById(R.id.themeRView);
        themeRView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FirestoreRecyclerAdapter<SearchContents, themeViewHolder>(options);
        @Override
        public void onDataChanged () {
            super.onDataChanged();
            Log.d(TAG, "어댑터 작동");
        }
        @Override
        protected void onBindViewHolder (@NonNull themeViewHolder holder,int position,
        @NonNull SearchContents model){
            holder.setContentsItems(model);
        }
        @NonNull
        @Override
        public themeViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_page_card, parent, false);
            return new themeViewHolder(view);
        }
    }
    ;
    themeRView.setAdapter(adapter);
}
@Override
protected void onStart() {
    super.onStart();
    adapter.startListening();
}
@Override
protected void onStop() {
    super.onStop();
    if (adapter != null) {
        adapter.stopListening();
    }
}
public class themViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public favoriteViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }
    public void set
        return rootView;
*/
