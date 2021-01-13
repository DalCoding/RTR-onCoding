package com.example.rotory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


<<<<<<< HEAD
import com.example.rotory.account.LogInActivity;
import androidx.fragment.app.Fragment;

import com.example.rotory.Interface.OnTabItemSelectedListener;

import com.example.rotory.account.SignUpActivity;
=======
<<<<<<< HEAD
import com.example.rotory.account.LogInActivity;
import com.example.rotory.account.LogInActivity_NA;
=======
import com.example.rotory.Account.LogInActivity;
>>>>>>> 9e88d5ab87ffd04bf9157d143ea44986e3fb0210

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rotory.Interface.OnTabItemSelectedListener;

>>>>>>> 1e57c27aace6b28417c22caf7e357564c0b206ad
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener {

    public static final String TAG = "MainActivity";
    public static final int loginCode = 3000;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MainPage mainPage;
    ThemePage themePage;
<<<<<<< HEAD

    BigMapPage bigMapPage;

=======
<<<<<<< HEAD
   BigMapPage bigMapPage;
=======
>>>>>>> 1e57c27aace6b28417c22caf7e357564c0b206ad
    SignUpActivity signUpActivity;
    BigMapPage bigMapPage;
>>>>>>> 9e88d5ab87ffd04bf9157d143ea44986e3fb0210


    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    RelativeLayout userAppbarContainer;

    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigation;
    Boolean isSignIn = false;
    FirebaseAuth mAuth;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
<<<<<<< HEAD
        if (requestCode == loginCode) {
            if (resultCode == RESULT_OK) {
=======
        if (requestCode == loginCode){
            if(resultCode == RESULT_OK){
>>>>>>> 1e57c27aace6b28417c22caf7e357564c0b206ad
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
<<<<<<< HEAD
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithCustomToken:success");
=======
                if (task.isSuccessful()){
                    Log.d(TAG,"signInWithCustomToken:success");
>>>>>>> 1e57c27aace6b28417c22caf7e357564c0b206ad

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = mAuth.getInstance().getCurrentUser();

<<<<<<< HEAD
        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저네임 : " + checkLogIN);
            isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
            isSignIn = false;
        }
=======
       if (user != null){
           String checkLogIN = user.getEmail();
           Log.d(TAG,"로그인 정보 유저네임 : " + checkLogIN);
           isSignIn = true;
       }else{
           Log.d(TAG,"로그인 실패" );
           isSignIn = false;
       }
>>>>>>> 1e57c27aace6b28417c22caf7e357564c0b206ad


        appBarLayout = findViewById(R.id.appBarLayout);
        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);


        mainPage = new MainPage();
        themePage = new ThemePage();

        bigMapPage = new BigMapPage();
<<<<<<< HEAD
=======


        bigMapPage = new BigMapPage();


>>>>>>> 1e57c27aace6b28417c22caf7e357564c0b206ad

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainPage).commit();

        bottomNavigation = findViewById(R.id.bottom_appBar);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
<<<<<<< HEAD
                switch (item.getItemId()) {
=======
                switch (item.getItemId()){
>>>>>>> 1e57c27aace6b28417c22caf7e357564c0b206ad
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainPage).commit();
                        setTabUnderBar(0);
                        bottomNavigation.setVisibility(View.VISIBLE);

                        return true;
                    case R.id.theme:
                        if (isSignIn) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, themePage).commit();
                            setTabUnderBar(1);
                        } else {
<<<<<<< HEAD
                            Intent LogInIntent = new Intent(getApplicationContext(), LogInActivity.class);
=======
                            Intent LogInIntent= new Intent(getApplicationContext(), LogInActivity.class);
>>>>>>> 1e57c27aace6b28417c22caf7e357564c0b206ad
                            startActivityForResult(LogInIntent, loginCode);
                            bottomNavigation.setVisibility(View.GONE);

                        }

                        return true;
                    case R.id.user:
                        if (isSignIn) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, themePage).commit();
                            setTabUnderBar(2);
                        } else {
<<<<<<< HEAD
                            Intent LogInIntent = new Intent(getApplicationContext(), LogInActivity.class);
=======
                            Intent LogInIntent= new Intent(getApplicationContext(), LogInActivity.class);
>>>>>>> 1e57c27aace6b28417c22caf7e357564c0b206ad
                            startActivityForResult(LogInIntent, loginCode);
                            bottomNavigation.setVisibility(View.GONE);
                        }

                        return true;
                }

                return false;
            }
        });
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
}

<<<<<<< HEAD




=======
<<<<<<< HEAD


    public void replaceFragment(Fragment fragment) {
=======
>>>>>>> 1e57c27aace6b28417c22caf7e357564c0b206ad
    /*public void replaceFragment(Fragment fragment) {

>>>>>>> 9e88d5ab87ffd04bf9157d143ea44986e3fb0210
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
<<<<<<< HEAD

    }

    }*/
=======
<<<<<<< HEAD
    }


=======
    }*/
>>>>>>> 9e88d5ab87ffd04bf9157d143ea44986e3fb0210
}
>>>>>>> 1e57c27aace6b28417c22caf7e357564c0b206ad
