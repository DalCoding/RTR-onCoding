package com.example.rotory;


import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Adapter.WriteStoryImageAdapter;
import com.example.rotory.Interface.OnContentsItemClickListener;

import java.io.InputStream;
import java.util.ArrayList;

public class Write_Story extends AppCompatActivity  {
    private final String TAG = "Write_Story";
    Button addbtn;
    ImageButton DeleteBtn;
    RecyclerView recyclerView;
    ImageView titleImage;
    int CODE_ALBUM_REQUEST = 111;
    OnContentsItemClickListener listener;
    Spinner spinner;
    private ArrayAdapter spinnerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_story_page);
        titleImage = findViewById(R.id.writeStoryMainImageView);
        DeleteBtn = findViewById(R.id.writeStorySetDeleteImageBtn);
        spinner = findViewById(R.id.writeStoryPreFixSpinner);
        addbtn= findViewById(R.id.writeStoryImageAddBtn);
        recyclerView= findViewById(R.id.writeStoryImageListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

//        Spinner spinner =findViewById(R.id.writeStoryPreFixSpinner);
//        spinnerAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, );
//        ArrayList<String> spinnerList = new ArrayList<>();
//        spinner.setAdapter(spinnerAdapter);
//
//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText( Write_Story.this,"선택 " + spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
//            }
//        });

//        Spinner spinner = findViewById(R.id.writeStoryPreFixSpinner);
//        try {
//            Field popup = Spinner.class.getDeclaredField("mPopup");
//            ListPopupWindow popupWindow = (ListPopupWindow) popup.get(spinner);
//            popupWindow.setHeight(500);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }

        //String prefixId = spinner.getSelectedItemPosition().toString();  //말머리 String
        String prefixId = spinner.getSelectedItem().toString(); //말머리 String

        //버튼 클릭했을 때 갤러리 연다
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, CODE_ALBUM_REQUEST);
            }
        });


        DeleteBtn.setClickable(true);
        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleImage.setImageResource(0); //-삭제 버튼 자동 생성
            }
        });
        
        // changeUritoBITmap();

    } //end of onCreate()

//    private Map<String, Bitmap> changeUritoBITmap() {
//        Bitmap titleimagebitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri);
//    }


    //앨범 이미지 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //갤러리 이미지 가져오기
        ArrayList<Uri> uriList = null;
        if (requestCode == CODE_ALBUM_REQUEST && resultCode == RESULT_OK && data != null) {
            uriList = new ArrayList<>();
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                if (clipData.getItemCount() > 10) { // 10개 초과하여 이미지를 선택한 경우
                    Toast.makeText(Write_Story.this, "사진은 10개까지 선택가능 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (clipData.getItemCount() == 1) { //멀티선택에서 하나만 선택한 경우
                    Uri filePath = clipData.getItemAt(0).getUri();
                    uriList.add(filePath);
                } else if (clipData.getItemCount() > 1 && clipData.getItemCount() <= 10) { //1개초과  10개 이하의 이미지선택한 경우
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        uriList.add(clipData.getItemAt(i).getUri());
                    }
                }
            }
            //리사이클러뷰에 보여주기
            WriteStoryImageAdapter adapter = new WriteStoryImageAdapter(uriList, Write_Story.this, listener);
            recyclerView.setAdapter(adapter);

//           adapter.setOnItemClickListener(new OnContentsItemClickListener() {
//            @Override
//            public void onItemClick(WriteStoryImageAdapter.writestroyHolder writestroyHolder, View view, int position) {
//               Log.d(TAG, "사진 URI확인");
//                Uri uri = adapter.getItem(position);
//                titleImage.setImageURI(uri);
//                
//            }
//        });

            if (requestCode == CODE_ALBUM_REQUEST) {
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream in = getContentResolver().openInputStream(data.getData());

                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();
                        titleImage.setImageBitmap(img);


                    } catch (Exception e) {
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                }

            }
        } //end of onActivityResult

    }


}


