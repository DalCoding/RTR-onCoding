package com.example.rotory;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rotory.Adapter.WriteStoryImageAdapter;
import com.example.rotory.Interface.OnContentsItemClickListener;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class Write_Story extends AppCompatActivity {
    final static String TAG = "Write_Story";

    Button AddBtn;
    Context context;
    ImageButton DeleteBtn;
    Button MainSelectBtn;
    boolean i = true;
    private RecyclerView.Adapter ImageAdapter;
    private RecyclerView WriteStoryImageListRecycler;
    private ImageView titleImage;
    private static final int REQUEST_CODE = 0;
    private ImageView smallImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_story_page);

        RecyclerView recyclerView = findViewById(R.id.writeStoryImageListRecyclerView);
        titleImage = findViewById(R.id.writeStoryMainImageView);
        smallImage = findViewById(R.id.writeStroryImageSmall);

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
//            items.add(new Imagelist());
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
                    //앨범 열기
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);




                String imageUri = "android.resource://com.example.rotory/drawable/squirrel";
                WriteStoryImageAdapter.addItem(new Imagelist(imageUri));

            }
        });


        DeleteBtn = findViewById(R.id.writeStorySetDeleteImageBtn);
        DeleteBtn.setClickable(true);
        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "X 버튼 누름");
                titleImage.setImageResource(0); //-삭제 버튼 자동 생성
            }
        });
    }
    public static Context mContext;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    titleImage.setImageBitmap(img);

                    smallImage.setImageBitmap(img);

                    Log.d(TAG, "사진 누름");
                    mContext = this;

                } catch (Exception e) {
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
}


