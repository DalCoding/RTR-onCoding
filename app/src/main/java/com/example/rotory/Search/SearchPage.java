package com.example.rotory.Search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;


import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.rotory.MainPage;
import com.example.rotory.R;
import com.example.rotory.Theme.Tags;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.account.SignUpActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class SearchPage extends AppCompatActivity {

    private static final String TAG = "SearchPage";
    private static final String REQUEST_CODE = "0000";

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

    SearchTagAdapter adapter;

    ImageButton backBtn;
    ImageButton removeBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        Bundle bundle = new Bundle();
        ArrayList<String> tagList = bundle.getStringArrayList("tagList");
        Log.d(TAG, "tagList 확인" + tagList);

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

        tagSet();

        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);

        mainPage = new MainPage();
        themePage = new ThemePage();

        searchTagList = findViewById(R.id.search1List);
        searchTagList.setLayoutManager(new GridLayoutManager(this, 3));
        backBtn = findViewById(R.id.searchBackBtn);
        removeBtn = findViewById(R.id.searchRemoveBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 화면으로 돌아가기
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEdit.setText(null);
            }
        });

        searchEdit = findViewById(R.id.searchIdEdit);

    }


    public void tagSet(){
        db.collection("tag").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            ArrayList<String> keyValue = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                Map<String, Object> document =documentSnapshot.getData();
                                Log.d(TAG, document.toString());
                                for (String key : document.keySet()) {
                                    String value = String.valueOf(document.get(key));
                                    keyValue.add(value);
                                }
                            }
                            Log.d(TAG,"태그 받아왔는지 확인" + keyValue);
                            ArrayList<Tags> tagList = new ArrayList<>();
                            for (int i = 0 ; i < keyValue.size(); i++){
                                tagList.add(new Tags(keyValue.get(i)));
                            }
                            Log.d(TAG, "ArrayList<Tags>확인" + tagList);

                            adapter = new SearchTagAdapter(tagList, getApplicationContext());
                            searchTagList = findViewById(R.id.search1List);
                            searchTagList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                            searchTagList.setAdapter(adapter);

                            searchEdit.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    adapter.getFilter().filter(charSequence);
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    searchTagList.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                });
    }
}
