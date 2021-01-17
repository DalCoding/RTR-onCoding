package com.example.rotory;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;

import com.example.rotory.Adapter.WriteStoryImageAdapter;
import com.example.rotory.Interface.OnContentsItemClickListener;

import java.util.ArrayList;

public class Write_Story extends AppCompatActivity {
    final static String TAG = "Write_Story";

    Button AddBtn;
    Context context;
    Button DeleteBtn;
    Button MainSelectBtn;
    boolean i = true;
    private RecyclerView.Adapter ImageAdapter;
    private RecyclerView WriteStoryImageListRecycler;
    private ImageView writeStoryMainImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_story_page);

        RecyclerView recyclerView = findViewById(R.id.writeStoryImageListRecyclerView);
        ImageView imageView = findViewById(R.id.writeStoryMainImageView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
//        WriteStoryImageListRecycler.setLayoutManager(linearLayoutManager);

        ArrayList<Imagelist> items = new ArrayList<>();
        WriteStoryImageAdapter adapter = new WriteStoryImageAdapter(this, items);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnContentsItemClickListener() {
            @Override
            public void onItemClick(WriteStoryImageAdapter.writestroyHolder writestroyHolder, View view, int position) {
                Imagelist item = adapter.getItem(position);
                Log.d(TAG,"아이템 선택됨."+item.getSmallimage());
            }
        });

//        for (int i = 0; i < 10; i++) {
//
//            adapter.notifyDataSetChanged();
//        }
        MainSelectBtn = findViewById(R.id.writeStorySetMainImageBtn);
        MainSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.writeStoryMainImageView).setBackground(
                       getDrawable(R.drawable.mainimageselect)

                );
            }
        });

        AddBtn = findViewById(R.id.writeStoryImageAddBtn);
       // AddBtn.setClickable(true);
        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "플러스 버튼 누름");
                String imageUri = "android.resource://com.example.rotory/drawable/squirrel";
                WriteStoryImageAdapter.addItem(new Imagelist(imageUri));
            }
        });
        DeleteBtn = findViewById(R.id.writeStoryImageAddBtn);
        DeleteBtn.setClickable(true);
        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // imageView.setImageResource(0); -삭제 버튼 자동 생성
            }
        });
    }
}


