package com.example.rotory.userActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.BigMapPage;
import com.example.rotory.MainPage;
import com.example.rotory.MyPage;
import com.example.rotory.R;
import com.example.rotory.ThemePage;
import com.example.rotory.VO.Person;
import com.example.rotory.account.LogInActivity;
import com.example.rotory.account.SignUpActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyLikeActivity extends AppCompatActivity  {
    public static final int loginCode = 3000;
    final static String TAG = "MyFavoriteActivity";

    RecyclerView myFavoriteRecyclerView;
    MainPage mainPage;
    ThemePage themePage;

    BigMapPage bigMapPage;
    SignUpActivity signUpActivity;


    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    RelativeLayout userAppbarContainer;

    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigation;

    Boolean isSignIn = false;
    private FirestoreRecyclerAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_favorite_page);
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저네임 : " + checkLogIN);
            isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
            isSignIn = false;
        }

        appBarLayout = findViewById(R.id.appBarLayout);
        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);


        mainPage = new MainPage();
        themePage = new ThemePage();
        bigMapPage = new BigMapPage();


        bottomNavigation = findViewById(R.id.bottom_appBar);
        setBottomNavigation(bottomNavigation, isSignIn, loginCode,
                mainPage, themePage);


        Query query = db.collection("person")
                .orderBy("signUpDate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Person> options = new FirestoreRecyclerOptions.Builder<Person>()
                .setQuery(query, Person.class)
                .build();

        myFavoriteRecyclerView = findViewById(R.id.myFavoriteRecyclerView);
        myFavoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FirestoreRecyclerAdapter<Person, favoriteViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, " 어댑터 작동");
            }

            @Override
            protected void onBindViewHolder(@NonNull favoriteViewHolder holder, int position,
                                            @NonNull Person model) {
                holder.setUserItems(model);
            }

            @NonNull
            @Override
            public favoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_favorite_item, parent,false);
               return new favoriteViewHolder(view);
            }
        };

        myFavoriteRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }

    public class favoriteViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public favoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }


        public void setUserItems(Person user) {
            ImageView myFavoriteImg;
            TextView myFavoriteNickTextView;
            TextView myFavoriteLevelTextView;
            ImageView myFavoriteLevelImg;
            myFavoriteImg = itemView.findViewById(R.id.myFavoriteImg);
            myFavoriteLevelImg = itemView.findViewById(R.id.myFavoriteLevelImg);
            myFavoriteNickTextView = itemView.findViewById(R.id.myFavoriteNickTextView);
            myFavoriteLevelTextView = itemView.findViewById(R.id.myFavoriteLevelTextView);


            int levelImg = getUserLevelImage(user.getUserLevel());
            myFavoriteLevelImg.setImageResource(levelImg);
            myFavoriteNickTextView.setText(user.getUserName());
            myFavoriteLevelTextView.setText(user.getUserLevel());
            //myFavoriteImg.setImageURI(Uri.parse(uri));

        }

    }


    //다람쥐 레벨용 이미지는 프로그램 내부에 넣어놓고, 유저레벨애 따라 불러서 사용
    private int getUserLevelImage(String userLevel) {
        switch (userLevel){
            case "어린다람쥐":
                return R.drawable.level2;
            case "학생다람쥐":
                return R.drawable.level3;
            case "어른다람쥐" :
                return R.drawable.level4;
            case "박사다람쥐" :
                return R.drawable.level5;
            case "다람쥐의 신":
                return R.drawable.level6;
            default:
                return R.drawable.level1;
        }
    }

    //하단바 설정
    public void setBottomNavigation(BottomNavigationView bottomNavigation, boolean isSignIn, int loginCode, MainPage mainPage, ThemePage themePage) {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent LogInIntent = new Intent(MyLikeActivity.this, LogInActivity.class);
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivityForResult(LogInIntent, loginCode);
                        setTabUnderBar(0);
                        bottomNavigation.setVisibility(View.VISIBLE);

                        return true;
                    case R.id.theme:
                        if (isSignIn) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, themePage).commit();
                            setTabUnderBar(1);
                        } else {

                            startActivityForResult(LogInIntent, loginCode);
                            bottomNavigation.setVisibility(View.GONE);
                        }

                        return true;
                    case R.id.user:
                        if (isSignIn) {
                            Intent myPageIntent = new Intent(MyLikeActivity.this, MyPage.class);
                            startActivity(myPageIntent);
                            setTabUnderBar(2);
                        } else {
                            startActivityForResult(LogInIntent, loginCode);
                            bottomNavigation.setVisibility(View.GONE);
                        }

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
            bottomNavUnderbarHome.setVisibility(View.GONE);
            bottomNavUnderbarTheme.setVisibility(View.VISIBLE);
            bottomNavUnderbarUser.setVisibility(View.GONE);
        } else if (position == 2) {
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.GONE);
            bottomNavUnderbarTheme.setVisibility(View.GONE);
            bottomNavUnderbarUser.setVisibility(View.VISIBLE);
        }
    }
      /* public void setMyFavoriteNickTextView(String userId){
            myFavoriteNickTextView.setText(userId);
        }

        public TextView getMyFavoriteLevelTextView() {
            return myFavoriteLevelTextView;
        }

        public void setMyFavoriteLevelTextView(String  level) {
            myFavoriteLevelTextView.setText(level);

        }*/
}
