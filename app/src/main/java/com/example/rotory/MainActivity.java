package com.example.rotory;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.Interface.OnTabItemSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener {
    MainPage mainPage;
    ThemePage themePage;
    SignupPage signupPage;

    BottomNavigationView bottomNavigation;
    Boolean isSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPage = new MainPage();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainPage).commit();

        bottomNavigation = findViewById(R.id.bottom_appBar);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                isSignIn = false;
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainPage).commit();
                        return  true;
                    case R.id.theme:
                        if(true) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, themePage).commit();
                        } else {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,  signupPage).commit();
                        }

                        return true;
                    case R.id.user:
                        if(true) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, themePage).commit();
                        } else {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,  signupPage).commit();
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
}