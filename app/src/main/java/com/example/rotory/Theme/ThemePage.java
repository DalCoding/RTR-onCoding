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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.MainActivity;
import com.example.rotory.MyPage;
import com.example.rotory.R;
import com.example.rotory.VO.Information;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ThemePage extends AppCompatActivity {
    final static String TAG = "ThemePage";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    private FirestoreRecyclerAdapter themeAdapter;

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

        user = mAuth.getCurrentUser();

        setUserTheme();

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

    private void setUserTheme() {
        themeRView = findViewById(R.id.themeRView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ThemePage.this,2);
        themeRView.setLayoutManager(gridLayoutManager);
        db.collection("person").whereEqualTo("userId", user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot pDocument : task.getResult()){
                        String personId = pDocument.getId();
                        Query query = db.collection("person").document(personId)
                                .collection("myTag");

                        FirestoreRecyclerOptions<Tags> options = new FirestoreRecyclerOptions.Builder<Tags>()
                                                                    .setQuery(query, Tags.class)
                                                                    .build();

                        setFirebaseAdapter(options);




                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"유저 정보 불러오기 실패" +e.toString());
            }
        });
    }

    private void setFirebaseAdapter(FirestoreRecyclerOptions options) {
        themeAdapter = new FirestoreRecyclerAdapter(options, themeViewHolder) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {

            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };

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

