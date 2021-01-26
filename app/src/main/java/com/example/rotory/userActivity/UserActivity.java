package com.example.rotory.userActivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.R;
import com.example.rotory.VO.AppConstant;

public class UserActivity extends AppCompatActivity {
    UserActivityPage userActivityPage = new UserActivityPage();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_container);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.userActContainer, userActivityPage).commit();


    }
}
