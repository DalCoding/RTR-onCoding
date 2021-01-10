package com.example.rotory;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Adapter.TagAdapter;
import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.VO.Tag;

import java.util.ArrayList;
import java.util.List;


public class SearchPage extends AppCompatActivity {
    TagAdapter tagAdapter;
    List<Tag> tag;
    EditText searchEdit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        setUpRecyclerView();

        searchEdit = findViewById(R.id.searchEdit);
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tagAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tagAdapter.setOnItemClickListener(new OnTagItemClickListener() {
            @Override
            public void onItemClick(TagAdapter.ViewHolder holder, View view, int position) {
                Tag item = tagAdapter.getItem(position);

            }
        });
    }

    private void setUpRecyclerView() {

        RecyclerView tagRecyclerView = findViewById(R.id.searchTagList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        tagRecyclerView.setLayoutManager(layoutManager);

        tag = new ArrayList<>();
        fillData();
        tagAdapter = new TagAdapter(tag);
        tagRecyclerView.setAdapter(tagAdapter);


        //데이터셋변경시
        //tagAdapter.dataSetChanged(exList);

    }

    private void fillData() {
        tag = new ArrayList<>(); //샘플데이터
        tag.add(new Tag("#서울"));
        tag.add(new Tag("#서울여행"));
        tag.add(new Tag("#강서구"));
        tag.add(new Tag("#화곡"));
        tag.add(new Tag("#서현역"));
        tag.add(new Tag("#서울맛집"));
        tag.add(new Tag("#핫플"));
        tag.add(new Tag("#홍대"));
        tag.add(new Tag("#신촌"));
    }
}
