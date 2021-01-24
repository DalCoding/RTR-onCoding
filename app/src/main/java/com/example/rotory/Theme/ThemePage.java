package com.example.rotory.Theme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.MainActivity;
import com.example.rotory.MyPage;
import com.example.rotory.R;
import com.example.rotory.VO.Information;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ThemePage extends AppCompatActivity {
    final static String TAG = "ThemePage";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;
    BottomNavigationView bottomNavigation;


    Button tagSelectBtn;
    RecyclerView themeRView;
    ThemePickPage themePickPage;
    CardView themeCardView;
    ImageView tcardThemeImg;
    TextView tcardThemeText;

    Information information;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_page);

        tagSelectBtn = findViewById(R.id.tagSelectBtn);
        tagSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    public void setBottomNavigation(BottomNavigationView bottomNavigation) {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(ThemePage.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        setTabUnderBar(0);
                        bottomNavigation.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.theme:
                        return true;
                    case R.id.user:
                        Intent myPageIntent = new Intent(ThemePage.this, MyPage.class);
                        myPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myPageIntent);
                        setTabUnderBar(2);
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
}

