package com.example.rotory;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nullable;

public class SearchPage1 extends AppCompatActivity {


    Boolean isSignIn = false;
    private FirestoreRecyclerAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        //setUpRecyclerView;
    }

    private void setUpRecyclreView() {
        RecyclerView tagRecyclerView = findViewById(R.id.searchTagList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        tagRecyclerView.setLayoutManager(layoutManager);
    }

    public int loadMoreContents() {

        return 8;

    }

}
