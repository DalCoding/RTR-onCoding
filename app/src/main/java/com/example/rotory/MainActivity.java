package com.example.rotory;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rotory.Interface.OnTabItemSelectedListener;

import com.example.rotory.signup.SignUpActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;
public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener {

    public static final String TAG = "MainActivity";
    public static final int loginCode = 1001;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MainPage mainPage;
    ThemePage themePage;
    SignUpActivity signUpActivity;
    BigMapPage bigMapPage;

    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    RelativeLayout userAppbarContainer;

    private FirebaseAuth mAuth;


    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigation;
    Boolean isSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        appBarLayout = findViewById(R.id.appBarLayout);
        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        mainPage = new MainPage();
        themePage = new ThemePage();
        signUpActivity = new SignUpActivity();
        bigMapPage = new BigMapPage();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainPage).commit();

        bottomNavigation = findViewById(R.id.bottom_appBar);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                isSignIn = false;
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainPage).commit();
                        setTabUnderBar(0);
                        bottomNavigation.setVisibility(View.VISIBLE);

                        return  true;
                    case R.id.theme:
                        if(isSignIn) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, themePage).commit();
                            setTabUnderBar(1);
                        } else {
                            Intent LogInIntent= new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivityForResult(LogInIntent, loginCode);
                            bottomNavigation.setVisibility(View.GONE);

                        }

                        return true;
                    case R.id.user:
                        if(isSignIn) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, themePage).commit();
                            setTabUnderBar(2);
                        } else {
                            Intent LogInIntent= new Intent(getApplicationContext(), SignUpActivity.class);
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
        if(position == 0){
            bottomNavigation.setSelectedItemId(R.id.home);
        }else if(position == 1){
            bottomNavigation.setSelectedItemId(R.id.theme);
        }else if(position ==2){
            bottomNavigation.setSelectedItemId(R.id.user);
        }

    }
    public void setTabUnderBar(int position){
        if(position == 0){
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.VISIBLE);
            bottomNavUnderbarTheme.setVisibility(View.GONE);
            bottomNavUnderbarUser.setVisibility(View.GONE);
        }else if(position == 1){
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.GONE);
            bottomNavUnderbarTheme.setVisibility(View.VISIBLE);
            bottomNavUnderbarUser.setVisibility(View.GONE);
        }else if(position ==2){
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.GONE);
            bottomNavUnderbarTheme.setVisibility(View.GONE);
            bottomNavUnderbarUser.setVisibility(View.VISIBLE);
        }

}

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}