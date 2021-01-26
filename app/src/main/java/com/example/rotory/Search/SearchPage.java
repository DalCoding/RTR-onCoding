package com.example.rotory.Search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;


import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


//import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.MainPage;
import com.example.rotory.R;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.VO.Tag;
import com.example.rotory.account.SignUpActivity;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;



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
    CollectionReference collectionReference;

    FirestoreRecyclerAdapter tagAdapter;
    //TagRecyclerAdapter adapter;


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
        searchTagList.setAdapter(tagAdapter);

        searchEdit = findViewById(R.id.searchEdit);
        searchEdit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String searchText = searchEdit.getText().toString();

                    goToSearch(searchText);

                    return true;
                }

                return false;
            }
        });



        /*db.collection("tag")
               .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                          for (QueryDocumentSnapshot snapshot : task.getResult()){
                              //Log.d(TAG, String.valueOf(snapshot.getData()));
                              Map<String, Object> tagList = new HashMap<>();
                              tagList = snapshot.getData();
                              String date = String.valueOf(tagList.get("date"));
                              Log.d(TAG, date);

                          }
                        }
                    }
                });*/



        /*db.collection("tag").document("taglist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                Map<String, Object> tagList = new HashMap<>();
                                tagList = snapshot.getData();
                                String taglist =
                                setTagAdapter(taglist);
                                tagAdapter.startListening();
                                searchTagList.setAdapter(tagAdapter);
                                //Log.d(TAG, String.ValueOf(snapshot.getData()));

                            }
                        }
                    }
                });*/

        db.collection("tag")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        EditText searchEdit = findViewById(R.id.searchEdit);
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d(TAG, "searchbox has changed to : " + s.toString());
                /*db.collection("tag")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                                        Log.d(TAG, String.valueOf(snapshot.getData()));
                                        Map<String, Object> tagList = new HashMap<>();
                                        tagList = snapshot.getData();
                                        String date = String.valueOf(tagList.get("date"));
                                        Log.d(TAG, date);

                                    }
                                }
                            }
                        });
                adapter.updateOptions(options);*/
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*Log.d(TAG, "searchbox has changed to: " + s.toString());

                Query query = db.collection("tag");
                FirestoreRecyclerOptions<Tag> options = new FirestoreRecyclerOptions.Builder<Tag>()
                        .setQuery(query, Tag.class)
                        .build();*/
                //adapter.updateOptions(options);

            }
        });


        Query query = db.collection("tag");
        FirestoreRecyclerOptions<com.example.rotory.VO.Tag> options = new FirestoreRecyclerOptions.Builder<com.example.rotory.VO.Tag>()
                .setQuery(query, com.example.rotory.VO.Tag.class)
                .build();

        tagAdapter = new FirestoreRecyclerAdapter<com.example.rotory.VO.Tag, tagViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull tagViewHolder holder, int position, @NonNull com.example.rotory.VO.Tag model) {
                holder.setTagItems(model);
            }

            @NonNull
            @Override
            public tagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
                return new tagViewHolder(view);
            }
        };
    }

    private void goToSearch(String searchText) {
        Intent intent = new Intent(SearchPage.this, SearchResultPage.class);
        intent.putExtra("searchText", searchText);
        startActivity(intent);
    }


       /* private void setTagAdapter (String tagList) {
            Query query = db.collection("tag");
            FirestoreRecyclerOptions<com.example.rotory.VO.Tag> options = new FirestoreRecyclerOptions.Builder<com.example.rotory.VO.Tag>()
                    .setQuery(query, com.example.rotory.VO.Tag.class)
                    .build();

            tagAdapter = new FirestoreRecyclerAdapter<com.example.rotory.VO.Tag, tagViewHolder>(options) {

                @Override
                protected void onBindViewHolder(@NonNull tagViewHolder holder, int position, @NonNull com.example.rotory.VO.Tag model) {
                    holder.setTagItems(model);
                }

                @NonNull
                @Override
                public tagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
                    return new tagViewHolder(view);
                }
            };
        }
    }*/


    @Override
    protected void onStart() {
        Log.d(TAG, "어댑터 작동");
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public class tagViewHolder extends RecyclerView.ViewHolder {
        View view;

        public tagViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setTagItems(Tag items) {
            Button tagBtn = view.findViewById(R.id.tagBtn);
            tagBtn.setText(items.getTag());
           // String date = (String) tagList.get("date");
            //Log.d(TAG, String.valueOf(tagList));
            //tagBtn.setText();

        }
    }
}
