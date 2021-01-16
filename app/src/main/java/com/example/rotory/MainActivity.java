package com.example.rotory;

import android.content.Intent;
import android.media.MediaDrm;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import androidx.fragment.app.Fragment;
import androidx.transition.Transition;

import com.example.rotory.Interface.OnTabItemSelectedListener;


import com.example.rotory.VO.AppConstruct;

import com.example.rotory.account.SignUpActivity;
import com.example.rotory.account.LogInActivity;


import com.example.rotory.userActivity.MyFavoriteActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener{

    public static final String TAG = "MainActivity";
    AppConstruct appConstruct;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MainPage mainPage;
    ThemePage themePage;
    StoryContentsPage storyContentsPage;

    BigMapPage bigMapPage;
    SignUpActivity signUpActivity;


    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    RelativeLayout userAppbarContainer;

    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigation;
    Boolean isSignIn = false;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //인텐트 전달 왜안됨?
  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == appConstruct.themeCode){
            if (resultCode == RESULT_OK){
                getSupportFragmentManager().beginTransaction().replace(R.id.container, themePage).commit();
            }
        }else if(requestCode == appConstruct.mainCode){
            if (resultCode==RESULT_OK){
                //getSupportFragmentManager().beginTransaction().replace(R.id.container, mainPage).commit();
            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPage = new MainPage();
        themePage = new ThemePage();
        bigMapPage = new BigMapPage();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저네임 : " + checkLogIN);
            isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
            isSignIn = false;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainPage).commit();

        // 아래부분 이후 옮김 -> 로그아웃 여부 실험!
        Button mainAlarmBtn = findViewById(R.id.mainAlarmBtn);
        mainAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null){
                    mAuth.signOut();
                    Log.d(TAG, "로그아웃");
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {

                }
            }
        });

        TextView pageTitleTextView = findViewById(R.id.pageTitleTextView);
        pageTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 즐겨찾기 리스트 띄우는 엑티비티 실험(MyFavoriteActivity)
                /*if (user != null) {
                    Intent intent = new Intent(MainActivity.this, MyFavoriteActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(intent);
                }*/

                getSupportFragmentManager().beginTransaction().replace(R.id.container,storyContentsPage );

            }
        });
        appBarLayout = findViewById(R.id.appBarLayout);
        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);


        mainPage = new MainPage();
        themePage = new ThemePage();
        bigMapPage = new BigMapPage();
        storyContentsPage = new StoryContentsPage();


        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainPage).commit();


        bottomNavigation = findViewById(R.id.bottom_appBar);
        setBottomNavigation(bottomNavigation, isSignIn, appConstruct.loginCode,
                mainPage, themePage);

    }


    @Override
    public void OnTabSelected(int position) {
        if (position == 0) {
            bottomNavigation.setSelectedItemId(R.id.home);
        } else if (position == 1) {
            bottomNavigation.setSelectedItemId(R.id.theme);
        } else if (position == 2) {
            bottomNavigation.setSelectedItemId(R.id.user);
        }
    }


    public void setBottomNavigation(BottomNavigationView bottomNavigation, boolean isSignIn, int loginCode, MainPage mainPage, ThemePage themePage) {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainPage).commit();
                        setTabUnderBar(0);
                        bottomNavigation.setVisibility(View.VISIBLE);

                        return true;
                    case R.id.theme:
                        if (isSignIn) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, storyContentsPage).commit();
                            setTabUnderBar(1);
                        } else {
                            Intent LogInIntent = new Intent(getApplicationContext(), LogInActivity.class);
                            startActivityForResult(LogInIntent, loginCode);
                            bottomNavigation.setVisibility(View.GONE);
                        }

                        return true;
                    case R.id.user:
                        if (isSignIn) {
                            Intent myPageIntent = new Intent(MainActivity.this, MyPage.class);
                            startActivity(myPageIntent);
                            setTabUnderBar(2);
                        } else {
                            Intent LogInIntent = new Intent(getApplicationContext(), LogInActivity.class);
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

    public void replaceFragment(Fragment fragment) {
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
        }

  /*  private void reload() {
        mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mAuth.getCurrentUser();
                    Toast.makeText(getApplicationContext(),
                            "Reload successful!",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "reload 후 로그인 정보" + mAuth.getCurrentUser().getEmail());
                } else {
                    Log.e(TAG, "reload", task.getException());
                    Toast.makeText(getApplicationContext(),
                            "Failed to reload user.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/
       /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == loginCode) {
                    if (resultCode == RESULT_OK) {
                        String tokenInfo = data.getStringExtra("token");
                        Log.d(TAG, "onActivityResult, token 받아옴 : " + tokenInfo);
                        logIn(tokenInfo);
                    }

        }
    }

    private void logIn(String tokenInfo) {
        mAuth.signInWithCustomToken(tokenInfo).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithCustomToken:success");
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCustomToken:success");

                    }
                }
            }
        });
    }
*/
}

