package com.example.rotory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Adapter.ContentsAdapter;
import com.example.rotory.Interface.OnContentsItemClickListener;

import java.util.ArrayList;

public class SearchResultPage extends AppCompatActivity {
    RecyclerView searchResultList;
    ContentsAdapter contentsAdapter;
    ArrayList<ContentsAdapter.SearchResult> searchResult;
    EditText searchResultEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_page);

        searchResultList = findViewById(R.id.searchResultList);
        searchResultEdit = findViewById(R.id.searchEdit);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchResultList.setLayoutManager(layoutManager);

        contentsAdapter = new ContentsAdapter(searchResult);
        searchResultList.setAdapter(contentsAdapter);


        contentsAdapter.setOnItemClickListener(new OnContentsItemClickListener() {
            @Override
            public void onItemClick(ContentsAdapter.ViewHolder holder, View view, int position) {
                ContentsAdapter.SearchResult items = contentsAdapter.getItem(position);
            }
        });
    }
}
