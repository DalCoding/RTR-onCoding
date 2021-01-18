package com.example.rotory.Search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


//import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.MainPage;
import com.example.rotory.R;
import com.example.rotory.ThemePage;
import com.example.rotory.VO.Tag;
import com.example.rotory.account.SignUpActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;



public class SearchPage extends AppCompatActivity {


    private static final String TAG = "SearchPage";

    RecyclerView searchTagList;
    MainPage mainPage;
    ThemePage themePage;

    EditText searchEdit;

    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    SignUpActivity signUpActivity;

    BottomNavigationView bottomNavigation;

    Boolean isSignIn = false;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    TagRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저네임 : " + checkLogIN);
            isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
            isSignIn = false;
        }

        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);

        mainPage = new MainPage();
        themePage = new ThemePage();


        searchTagList = findViewById(R.id.searchTagList);
        searchTagList.setLayoutManager(new GridLayoutManager(this, 3));

        Query query = db.collection("road");
        FirestoreRecyclerOptions<Tag> options = new FirestoreRecyclerOptions.Builder<Tag>()
                .setQuery(query, Tag.class)
                .build();

        adapter = new TagRecyclerAdapter(options);
        searchTagList.setAdapter(adapter);

        EditText searchEdit = findViewById(R.id.searchEdit);
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "searchbox has changed to : " + s.toString());

                adapter.updateOptions(options);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "searchbox has changed to: " + s.toString());

                Query query = db.collection("tag");
                FirestoreRecyclerOptions<Tag> options = new FirestoreRecyclerOptions.Builder<Tag>()
                        .setQuery(query, Tag.class)
                        .build();
                //adapter.updateOptions(options);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
