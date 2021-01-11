package com.example.rotory;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.Interface.OnTabItemSelectedListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener {

    public static final int loginCode = 1001;

    MainPage mainPage;
    ThemePage themePage;
    SignupPage signupPage;
    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;
    RelativeLayout withBackBtnContainer;
    RelativeLayout userAppbarContainer;


    TextView pageTitleTextView;
    TextView pageTitlewithBtnTextView;

    Button mainAlarmBtn;
    Button checkBtn;
    ImageButton backImageButton;

    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigation;
    Boolean isSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBarLayout = findViewById(R.id.appBarLayout);
        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);

        withBackBtnContainer = findViewById(R.id.withBackBtnContainer);
        userAppbarContainer = findViewById(R.id.userAppbarContainer);

        pageTitleTextView = findViewById(R.id.pageTitleTextView);
        pageTitlewithBtnTextView = findViewById(R.id.pageTitlewithBtnTextView);

        mainAlarmBtn = findViewById(R.id.mainAlarmBtn);
        checkBtn = findViewById(R.id.checkBtn);
        backImageButton = findViewById(R.id.backImageButton);


        mainPage = new MainPage();
        themePage = new ThemePage();
        signupPage = new SignupPage();

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

                        return  true;
                    case R.id.theme:
                        if(true) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, themePage).commit();
                            setTabUnderBar(1);
                        } else {
                            Intent LogInIntent= new Intent(getApplicationContext(), LogInActivity.class);
                            startActivityForResult(LogInIntent, loginCode);
                            bottomNavigation.setVisibility(View.GONE);

                        }

                        return true;
                    case R.id.user:
                        if(true) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, themePage).commit();
                            setTabUnderBar(2);
                        } else {
                            Intent LogInIntent= new Intent(getApplicationContext(), LogInActivity.class);
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
}