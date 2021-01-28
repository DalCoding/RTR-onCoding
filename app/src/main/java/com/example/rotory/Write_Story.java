package com.example.rotory;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Adapter.WriteStoryImageAdapter;
import com.example.rotory.Contents.StoryImageAdapter;
import com.example.rotory.Interface.OnContentsItemClickListener;

import com.example.rotory.VO.Story;
import com.example.rotory.story.SearchOnMyRoadFragment;
import com.example.rotory.story.StoryFindLocationPage;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.internal.InternalTokenProvider;

import com.example.rotory.VO.AppConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Write_Story extends AppCompatActivity  {
    private final String TAG = "Write_Story";
    private static final int Map_RESULT_CODE = 5200;
    private static final int Road_RESULT_CODE = 5300;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    RelativeLayout writeStory;

    Button addbtn;
    Button mainbtn;
    Button checkmarkBtn;

    RadioButton publicRadioButton;
    RadioButton privateRadioButton;

    TextView mainImagetext;
    ImageButton DeleteBtn;
    RecyclerView recyclerView;
    ImageView titleImage;

    EditText writeStoryEditText;
    TextView writeStoryLocation;
    EditText writeStoryImageCommentEditText;
    EditText writeStoryTitle;

    Spinner spinner;
    LinearLayout mainImageSelect;


    private InputMethodManager keyboardManager;
    int CODE_ALBUM_REQUEST = 111;
    OnContentsItemClickListener listener;

    private ArrayAdapter spinnerAdapter;

    public int imagePosition;
    //private ArrayAdapter spinnerAdapter;
    ArrayList<Uri> uriList = new ArrayList<>();

    WriteStoryImageAdapter adapter = new WriteStoryImageAdapter(uriList, Write_Story.this, listener);

    Map<String, Bitmap> bitmapImageList = new HashMap<>();
    Map<String, Object> stringImageList = new HashMap<>();
    Map<String, String>imageComment = new HashMap<>();

  Map<String, Object> imageList = new HashMap<>();

    //Map<String, Bitmap> imageList = new HashMap<>();
    Map<String, Object> DBStoryContents = new HashMap<>();

   //Bitmap mainImage;
    String mainImage;
    String prefixId;
    String storyaddress;
    String title;
    String article;


    Boolean isChecked;

    int isPublic;
    int checkPosition;

    AppConstant appConstant = new AppConstant();


    StoryFindLocationPage findLocationPage;


    public Write_Story() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_story_page);

        user = mAuth.getCurrentUser(); //관리자에게 유저 권한을 받아옴.

        isPublic = 1;

        writeStory = findViewById(R.id.writeStory);
        keyboardManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        writeStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboardManager.hideSoftInputFromWindow(writeStoryImageCommentEditText.getWindowToken(), 0);
            }
        });


        titleImage = findViewById(R.id.writeStoryMainImageView);
        mainbtn = findViewById(R.id.writeStorySetMainImageBtn);
        DeleteBtn = findViewById(R.id.writeStorySetDeleteImageBtn);
        spinner = findViewById(R.id.writeStoryPreFixSpinner);
        addbtn= findViewById(R.id.writeStoryImageAddBtn);
        recyclerView= findViewById(R.id.writeStoryImageListRecyclerView);
        mainImageSelect = findViewById(R.id.mainImageSelect);

        writeStoryImageCommentEditText = findViewById(R.id.writeStoryImageCommentEditText);
        writeStoryEditText = findViewById(R.id.writeStoryEditText);
        writeStoryTitle = findViewById(R.id.writeStoryTitle);

       //storyaddress = writeStoryLocation.getText().toString();
       title = writeStoryTitle.getText().toString();
       article = writeStoryEditText.getText().toString();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        AlertDialog.Builder mainImageDialog = new AlertDialog.Builder(this);


        saveDialog .setTitle("다람쥐 이야기 작성");
        saveDialog .setMessage("작성을 완료하시겠습니까?");
        saveDialog .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    setDB();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        saveDialog .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = saveDialog.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }

        });


        mainImagetext = findViewById(R.id.mainImagetext);
        mainImageDialog.setTitle("썸네일 지정");
        mainImageDialog.setMessage("메인 사진으로 지정 하시겠습니까?");
        mainImageDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();

                    Uri uri = adapter.getItem(imagePosition);
                try {
                    mainImage = mainImageString(uri); //메인이미지 스트링바꿈
                } catch (IOException e) {
                    e.printStackTrace();
                }
                checkPosition = imagePosition;
                    mainImagetext.setVisibility(View.VISIBLE);
            }
        });
        mainImageDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog mainImageAlertDialog =  mainImageDialog.create();
        mainImageAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                mainImageAlertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                mainImageAlertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }

        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int position, long id) {

                prefixId = spinner.getSelectedItem().toString();
                Log.d(TAG, "SELECT" + prefixId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "NOSELECT");
                // TODO Auto-generated method stub

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
                if (imagePosition == checkPosition){
                    mainImagetext.setVisibility(View.GONE);
                }
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

                if (uriList.size() > 0) {
                    mainImageAlertDialog.show();
                }


            }
        });

        publicRadioButton=findViewById(R.id.publicRadioButton2);
        publicRadioButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                isPublic=1;

            }
        });

        privateRadioButton=findViewById(R.id.privateRadioButton2);
        privateRadioButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                isPublic=0;
            }
        });



        checkmarkBtn = findViewById(R.id.checkmarkBtn);
        checkmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    alertDialog.show();

                } else if (mainImage == null) {
                    Toast.makeText(getApplicationContext(), "V 를 눌러 메인사진을 지정해주세요.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "필수 입력 사항을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
         });

        //장소 검색 페이지 띄우기
        //findLocationPage = new StoryFindLocationPage();
        writeStoryLocation=findViewById(R.id.writeStoryLocation);
        writeStoryLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(Write_Story.this,StoryFindLocationPage.class);
                startActivity(intent);
            }
        });

        Intent placeTextIntent=getIntent();
        String placeText=placeTextIntent.getStringExtra("placeText");

        Intent placeNameIntent=getIntent();
        String placeName=placeNameIntent.getStringExtra("placeName");


        if(placeText==null){
            writeStoryLocation.setText(placeName);
        }else{
            writeStoryLocation.setText(placeText);
        }

        } //end of onCreate]


private boolean isValidate(){

        storyaddress=writeStoryLocation.getText().toString();
        title=writeStoryTitle.getText().toString();

        if(title.equals("")||title==null){
        return false;
        }else if(mainImage==null){
        return false;

        }else if(storyaddress.equals("")||storyaddress==null){
        return false;
        }


        return true;


        }

    private void setDB() throws IOException {
        imageList = changeUritoBITmap();


        ArrayList<String> address = new ArrayList<>();
        address.add(storyaddress);

        DBStoryContents.put("contentsType", 1);
        DBStoryContents.put("smallImage", imageList);
        DBStoryContents.put("titleImage", mainImage);
        DBStoryContents.put("imageComment", imageComment);
        DBStoryContents.put("prefixId", prefixId);
        DBStoryContents.put("address", address);
        DBStoryContents.put("title", title);
        DBStoryContents.put("article", article);
        DBStoryContents.put("isPublic", isPublic);
        DBStoryContents.put("writeDate",appConstant.dateFormat.format(new Date()));

        setUserDB();
    }

    private void setUserDB() {
        db.collection("person").whereEqualTo("userId", user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG,"데이터 받아오기 시작");
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot pDocument : task.getResult()){
                        Log.d(TAG,"pDocument 존재 확인" + pDocument.getData());
                        String userName = pDocument.get("userName").toString();
                        String userLevel =pDocument.get("userLevel").toString();
                        String uid = pDocument.get("uid").toString();

                        DBStoryContents.put("userName",userName);
                        DBStoryContents.put("userLevel", userLevel);
                        DBStoryContents.put("uid", uid);

                        saveDB(DBStoryContents);

                    }

                }
            }
        });

    }

    private void saveDB(Map<String, Object> dbStoryContents) {
        db.collection("contents").add(dbStoryContents).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Log.d(TAG, "정보제대로 들어갔는지 확인 : " +dbStoryContents);

                Intent mainIntent = new Intent(Write_Story.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK); //SINGLE_TOP는 쌓아주는것(돌고돌게)을 전것을 제거 해주고, NEW_TASK는 다시 시작해줌.
                startActivity(mainIntent);
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "저장 성공 : " );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "저장 실패..."  + e.toString());
            }
        });

    }


    private String mainImageString(Uri uri) throws IOException { //메인 이미지 스트링 바꿈.
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] bytes =baos.toByteArray();
        String stringIamge = Base64.encodeToString(bytes, Base64.NO_WRAP);
        Log.d(TAG,"타이틀 이미지 스트링 변경 성공?" + stringIamge);

        baos.flush();
        return  stringIamge;


    }
   /* private Bitmap mainImageString(Uri uri) { //메인 이미지 스트링 바꿈.
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  bitmap;

    }*/


    private Map<String, Object> changeUritoBITmap() throws IOException {
        Uri uri;
        Bitmap bitmap = null;
        for (int i =0; i <uriList.size(); i++){  /// uri를 Bitmap으로 변환


            try {
//                            uri 주소를 Bitmap으로 변환한다.
                uri = uriList.get(i);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmapImageList.put("image"+(i+1), bitmap);
                Log.d(TAG,"bitmap값 확인" + bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Log.d(TAG,"비트맵 변경 성공?" + bitmapImageList);



        for (int i =0; i < bitmapImageList.size(); i++){  //Bitmap을 string으로 변환
            //ArrayList<ByteArrayOutputStream> baosList = new ArrayList<>();
           // baosList.add(new ByteArrayOutputStream());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            String keyString = "image"+String.valueOf(i+1);
            Bitmap imageBitmap;
            imageBitmap = bitmapImageList.get(keyString);
            if (imageBitmap != null) {
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] bytes = stream.toByteArray();
                String stringImage = Base64.encodeToString(bytes, Base64.NO_WRAP);
                stringImageList.put(keyString, stringImage);
                Log.d(TAG,"스트링 변경 성공?" +keyString+ "=>" + stringImage);

            }else{
                Log.d(TAG,"image"+i + "null임 : " + imageBitmap);
            }

            stream.flush();
           // baosList.get(i).flush();
            //Log.d(TAG,"스트링 변경 성공?" +keyString+ "=>" + stringImage);

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

             if (requestCode == Map_RESULT_CODE) {
                 if (resultCode == RESULT_OK) {
                 }
             }

            //리사이클러뷰에 보여주기

        }

        //end of onActivityResult

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnContentsItemClickListener() {

            @Override
            public void onItemClick(WriteStoryImageAdapter.writestroyHolder writestroyHolder, View view, int position) {
                Log.d(TAG, "사진 URI확인");
                Uri uri = adapter.getItem(position);
                titleImage.setImageURI(uri);
                imagePosition = position;

                writeStoryImageCommentEditText.setText(imageComment.get(String.valueOf(position)));
                Log.d(TAG,"작동확인" + imageComment.get(position));

                if (String.valueOf(checkPosition) != null) {
                    if (adapter.getItem(checkPosition) == uri) {
                        mainImagetext.setVisibility(View.VISIBLE);

                    } else {
                        mainImagetext.setVisibility(View.GONE);

                    }
                }

            }

            @Override
            public void onItemClick(StoryImageAdapter.writestroyHolder writestroyHolder, View view, int position) {

            }
            @Override
            public void onItemClick(MainPage.MyAdapter.ViewHolder holder, View view, int position) {

            }

        });


        writeStoryImageCommentEditText.addTextChangedListener(new TextWatcher() {
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
                    imageComment.put(String.valueOf(imagePosition), editable.toString());

                }else{
                }
            }
        });

        if (uriList.size()>0){
            Log.d(TAG, "사진 제대로 받아옴");
            Uri uri = uriList.get(0);
            try {
                mainImage = mainImageString(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}


