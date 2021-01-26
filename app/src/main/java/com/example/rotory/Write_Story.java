package com.example.rotory;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.text.Transliterator;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Adapter.WriteStoryImageAdapter;
import com.example.rotory.Interface.OnContentsItemClickListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Write_Story extends AppCompatActivity  {
    private final String TAG = "Write_Story";
    Button addbtn;
    Button mainbtn;
    ImageButton DeleteBtn;
    RecyclerView recyclerView;
    ImageView titleImage;
    int CODE_ALBUM_REQUEST = 111;
    OnContentsItemClickListener listener;
    Spinner spinner;
    public int imagePosition;
    //private ArrayAdapter spinnerAdapter;
    ArrayList<Uri> uriList = new ArrayList<>();
    WriteStoryImageAdapter adapter = new WriteStoryImageAdapter(uriList, Write_Story.this, listener);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_story_page);
        titleImage = findViewById(R.id.writeStoryMainImageView);
        mainbtn = findViewById(R.id.writeStorySetMainImageBtn);
        DeleteBtn = findViewById(R.id.writeStorySetDeleteImageBtn);
        spinner = findViewById(R.id.writeStoryPreFixSpinner);
        addbtn= findViewById(R.id.writeStoryImageAddBtn);
        recyclerView= findViewById(R.id.writeStoryImageListRecyclerView);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setReverseLayout(true);
//        mLayoutManager.setStackFromEnd(true);
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int position, long id) {
                Log.d(TAG, "SELECT");
                // TODO Auto-generated method stub
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "NOSELECT");
                // TODO Auto-generated method stub

            }
        });
        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Uri uri = uriList.set();
//                int position = Integer.parseInt(Uri,toString());
            }
        });



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
        DeleteBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (imagePosition >= 0) {
                    titleImage.setImageResource(0);
                    adapter.removeItem(imagePosition);
                    adapter.notifyDataSetChanged(); //갱신
                    Log.d(TAG, "사진 삭제" + imagePosition);
                }

                if (imagePosition > 0) {
                    imagePosition = imagePosition - 1;
                    titleImage.setImageURI(adapter.getItem(imagePosition));
                    Log.d(TAG, "사진 전에꺼 보여주기 " + imagePosition);
                }
//                Log.d(TAG, "사진 삭제" + imagePosition);
            }
        });
               mainbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                Uri uri = adapter.getItem(imagePosition);
                String mainImage = mainImageString(uri);         //메인이미지 스트링바꿈
//
//                adapter.getItem(imagePosition);
//                Drawable drawable = getResources().getDrawable(R.drawable.mainimageselect);
//
//
//
//                Log.d(TAG, "메인 이미지 확인" );
            }
        });



    } //end of onCreate]
//
//    private Drawable mainImageDrawable() {
//
//        Drawable drawable = getResources().getDrawable(R.drawable.mainimageselect);
//
//
//        return drawable;
//    }


    private String mainImageString(Uri uri) { //메인 이미지 스트링 바꿈.
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] bytes =baos.toByteArray();
        String stringIamge = Base64.encodeToString(bytes, Base64.DEFAULT);
        return  stringIamge;

    }





    private Map<String, Object> changeUritoBITmap() {
        Map<String, Bitmap> bitmapImageList = new HashMap<>();
        Map<String, Object> stringImageList = new HashMap<>();
        Bitmap bitmap = null;
        for (int i =0; i <uriList.size(); i++){  /// uri를 Bitmap으로 변환
            Uri uri = uriList.get(i);

            try {
//                            uri 주소를 Bitmap으로 변환한다.
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmapImageList.put("image"+(i+1), bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Log.d(TAG,"비트맵 변경 성공?" + bitmapImageList);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int count = 0;
        for (int i =0; i < bitmapImageList.size(); i++){  //Bitmap을 string으로 변환
            String keyString = "image"+String.valueOf(i+1);
            Bitmap imageBitmap;
            imageBitmap = bitmapImageList.get(keyString);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
            byte[] bytes = baos.toByteArray();
            String stringImage = Base64.encodeToString(bytes, Base64.DEFAULT);
            stringImageList.put(keyString, stringImage);
            count ++;
        }
        Log.d(TAG,"스트링 변경 성공?" + stringImageList.size() + "=>" + stringImageList);

        return  stringImageList;

    }

//    public String getBase64String(Bitmap bitmap)
//    {
//                               스트링으로 바꾸는 다른 방식
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//
//        byte[] imageBytes = byteArrayOutputStream.toByteArray();
//        byteArrayOutputStream.put()
//
//        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
//    }



    //앨범 이미지 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //갤러리 이미지 가져오기

        if (requestCode == CODE_ALBUM_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                if (clipData.getItemCount() > 10) { // 10개 초과하여 이미지를 선택한 경우
                    Toast.makeText(Write_Story.this, "사진은 10개까지 선택가능 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (clipData.getItemCount() == 1) { //멀티선택에서 하나만 선택한 경우
                    Uri filePath = clipData.getItemAt(0).getUri();
                    uriList.add(0, filePath);
                } else if (clipData.getItemCount() > 1 && clipData.getItemCount() <= 10) { //1개초과  10개 이하의 이미지선택한 경우
                    for (int i = 0; i < clipData.getItemCount(); i++) {

                        uriList.add(0, clipData.getItemAt(i).getUri());

                    }
                }

            }

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


            //리사이클러뷰에 보여주기

        }

        //end of onActivityResult
        Map<Integer, String>imageComment = new HashMap<>();
        EditText editText;
        editText = findViewById(R.id.writeStoryImageCommentEditText);

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnContentsItemClickListener() {

            @Override
            public void onItemClick(WriteStoryImageAdapter.writestroyHolder writestroyHolder, View view, int position) {
                Log.d(TAG, "사진 URI확인");
                Uri uri = adapter.getItem(position);
                titleImage.setImageURI(uri);
                imagePosition = position;
                editText.setText(imageComment.get(position));
                Log.d(TAG,"작동확인" + imageComment.get(position));


            }

        });

        editText.addTextChangedListener(new TextWatcher() {
            // edittext 마다 다르게 코멘트
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0 && !editable.toString().equals("")){
                    imageComment.put(imagePosition, editable.toString());

                }else{
                }
            }
        });

    }
}


