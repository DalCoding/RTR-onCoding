package com.example.rotory;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.annotation.Nullable;

public class SearchPage1 extends AppCompatActivity {
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
