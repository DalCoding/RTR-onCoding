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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.BigMapPage;
import com.example.rotory.MainActivity;
import com.example.rotory.MainPage;
import com.example.rotory.MyPage;
import com.example.rotory.R;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Person;
import com.example.rotory.account.LogInActivity;
import com.example.rotory.account.SignUpActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MyFavoriteActivity  extends AppCompatActivity  {
    AppConstant appConstant = new AppConstant();
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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_favorite_page);

        user = mAuth.getCurrentUser();

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
        setBottomNavigation(bottomNavigation, isSignIn, appConstant.loginCode,
                mainPage, themePage);
        myFavoriteRecyclerView = findViewById(R.id.myFavoriteRecyclerView);
        myFavoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db.collection("person")
                .whereEqualTo("userId", user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String personId = document.getId();

                        Query query = db.collection("person")
                                .document(personId).collection("myStar")
                                .orderBy("savedDate", Query.Direction.ASCENDING);

                        FirestoreRecyclerOptions<Person> options = new FirestoreRecyclerOptions.Builder<Person>()
                                .setQuery(query, Person.class)
                                .build();
                        makeAdapter(options);
                        adapter.startListening();
                        myFavoriteRecyclerView.setAdapter(adapter);

                    }
                }
            }
        });



    }

    private void makeAdapter(FirestoreRecyclerOptions<Person> options) {

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

    }

    @Override
    protected void onStart() {
        super.onStart();

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


            int levelImg = appConstant.getUserLevelImage(user.getUserLevel());
            myFavoriteLevelImg.setImageResource(levelImg);
            myFavoriteNickTextView.setText(user.getUserName());
            myFavoriteLevelTextView.setText(user.getUserLevel());

        }

    }

    //하단바 설정
    public void setBottomNavigation(BottomNavigationView bottomNavigation, boolean isSignIn, int loginCode, MainPage mainPage, ThemePage themePage) {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent LogInIntent = new Intent(MyFavoriteActivity.this, LogInActivity.class);
                Intent mainIntent = new Intent(MyFavoriteActivity.this, MainActivity.class);
                switch (item.getItemId()) {
                    case R.id.home:

                        startActivityForResult(mainIntent, appConstant.mainCode);
                        setTabUnderBar(0);
                        bottomNavigation.setVisibility(View.VISIBLE);

                        return true;
                    case R.id.theme:
                        if (isSignIn) {
                            startActivityForResult(mainIntent, appConstant.themeCode);
                            setTabUnderBar(1);
                        } else {

                            startActivityForResult(LogInIntent, loginCode);
                            bottomNavigation.setVisibility(View.GONE);
                        }

                        return true;
                    case R.id.user:
                        if (isSignIn) {
                            Intent myPageIntent = new Intent(MyFavoriteActivity.this, MyPage.class);
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
