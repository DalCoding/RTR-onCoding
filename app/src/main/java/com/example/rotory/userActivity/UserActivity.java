package com.example.rotory.userActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.Interface.OnTabItemSelectedListener;
import com.example.rotory.MainActivity;
import com.example.rotory.MainPage;
import com.example.rotory.MyPage;
import com.example.rotory.ProgressDialogs;
import com.example.rotory.R;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.VO.AppConstant;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserActivity extends AppCompatActivity implements OnTabItemSelectedListener {
    UserActivityPage userActivityPage = new UserActivityPage();
    TextView profileTextView;

    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    RelativeLayout userAppbarContainer;

    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigation;
    ProgressDialogs progressDialogs;
    MainPage mainPage;
    ThemePage themePage;
    public static final int REQUEST_CODE_GALLERY = 101;
    public static final int MainCode = 1000;
    public static final int ThemeCode = 2000;
    Boolean isSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_container);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.userActContainer, userActivityPage).commit();

        appBarLayout = findViewById(R.id.appBarLayout);
        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);

        mainPage = new MainPage();
        themePage = new ThemePage();

        profileTextView = findViewById(R.id.profileTextView);
        profileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPage.class);
                startActivity(intent);
                finish();
            }
        });

        progressDialogs = new ProgressDialogs(this);
        progressDialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        bottomNavigation = findViewById(R.id.bottom_appBar);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                isSignIn = true;
                switch (item.getItemId()){
                    case R.id.home:
                        Intent MainIntent= new Intent(getApplicationContext(), MainActivity.class);
                        MainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(MainIntent);
                        finish();
                        bottomNavigation.setVisibility(View.VISIBLE);
                        setTabUnderBar(0);
                        return  true;

                    case R.id.theme:
                        // if(isSignIn) {
                        progressDialogs = new ProgressDialogs(UserActivity.this);
                        progressDialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        progressDialogs.show();
                        startTagsLoading(300);
                        Intent ThemeIntent= new Intent(getApplicationContext(), ThemePage.class);
                        startActivityForResult(ThemeIntent, ThemeCode);
                        bottomNavigation.setVisibility(View.VISIBLE);
                        setTabUnderBar(1);
                        //여기선 필요없을듯
                       /* } else  {
                            Intent LogInIntent= new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivityForResult(LogInIntent, LoginCode);
                            bottomNavigation.setVisibility(View.GONE);

                        }*/
                        return true;

                    case R.id.user:
                     /*if(isSignIn) {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        } else {
                            Intent LogInIntent= new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivityForResult(LogInIntent, LoginCode);
                            bottomNavigation.setVisibility(View.GONE);
                        }*/
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

    public void startTagsLoading(int millis){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                progressDialogs.dismiss();
            }
        }, millis);
    }

    public void setTabUnderBar(int position){
        if(position == 0){
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.VISIBLE);
            bottomNavUnderbarTheme.setVisibility(View.GONE);
            bottomNavUnderbarUser.setVisibility(View.GONE);
        }else if(position == 1){
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.INVISIBLE);
            bottomNavUnderbarTheme.setVisibility(View.VISIBLE);
            bottomNavUnderbarUser.setVisibility(View.INVISIBLE);
        }else if(position ==2){
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.GONE);
            bottomNavUnderbarTheme.setVisibility(View.GONE);
            bottomNavUnderbarUser.setVisibility(View.VISIBLE);
        }

    }
}
