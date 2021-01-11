package com.example.rotory;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class MyPage extends AppCompatActivity {

    public static final int REQUEST_CODE_GALLERY = 101;
    ImageView myProfileImg;
    ImageView myEditImg;
    ImageView myLevelImg;
    FrameLayout myScrapLayout;
    Button myLevelOutBtn;

    //활동내역, 즐겨찾기, 좋아요, 스크랩리스트, 문의하기 이동동

   @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);

        myProfileImg = findViewById(R.id.myProfileImg);
        myProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGalleryActivity();
            }
        });

        myEditImg = findViewById(R.id.myEditImg);
        myEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPWDialog();
            }
        });

        myLevelImg = findViewById(R.id.myLevelImg);
        myLevelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout myLevelInfoLayout = findViewById(R.id.myLevelInfoLayout);
                myLevelInfoLayout.setVisibility(View.VISIBLE);
            }
        });

        myLevelOutBtn = findViewById(R.id.myLevelOutBtn);
        myLevelOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout myLevelInfoLayout = findViewById(R.id.myLevelInfoLayout);
                myLevelInfoLayout.setVisibility(View.INVISIBLE);
            }
        });
        loadPersonInfo();
        loadScrapList();
    }

    /* onCreate 이후 기타 메소드들 */

    public void loadPersonInfo() {

        // 전체적인인 정보 수정 + 레벨 레이아웃에 exp 도 담기
    }

    public void showGalleryActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY) {
            Uri selectedImageUri = data.getData();
            myProfileImg.setImageURI(selectedImageUri);
        }
    }

    public void showPWDialog(){
        AlertDialog.Builder PWCheck = new AlertDialog.Builder(this);
        PWCheck.setTitle("비밀번호 확인");       // 제목 설정
        // EditText 삽입하기
        final EditText PWCheckEditText = new EditText(this);
        PWCheck.setView(PWCheckEditText);

        // 확인 버튼 설정
        PWCheck.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Text 값 받아서 처리
                String PWCheckEditText1 = PWCheckEditText.getText().toString();
                dialog.dismiss();     //닫기

                if (PWCheckEditText1=="1234") {
                    //Intent intent = new Intent(getApplicationContext(), ProfileEditPage.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        PWCheck.show();
    }

    public void loadScrapList(){
        //8개 불러와서 최근 순서대로 이미지,제목,저장날짜,장소 담기
        ImageView myScrapImg = findViewById(R.id.myScrabImg); myScrapImg.setAlpha(50);
        TextView myScrapTitle = findViewById(R.id.myScrabTitle);
        TextView myScrapSave = findViewById(R.id.myScrabSave);
        TextView myScrapPlace = findViewById(R.id.myScrabPlace);

        myScrapImg.setImageResource(R.drawable.acorn);
        myScrapTitle.setText("스크랩 제목");
        myScrapSave.setText("21.01.11 저장");
        myScrapPlace.setText("서울시 화곡동");
        myScrapLayout = findViewById(R.id.myScrabLayout);
        myScrapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadScrapItem();
                // 파라미터로 int contents_id
            }


        });
    }

    public void loadScrapItem(){
        Toast.makeText(getApplicationContext(), "X번째 레이아웃 선택", Toast.LENGTH_SHORT).show();
        // 파라미터로 int contents_id
    }


}

// showGalleryActivity O
//onActivityResult O
//showPWDialog O  (https://dhparkdh.tistory.com/103)
//loadscrapList O
//loadscrapItem O
//userExp O

//남은것 : DB불러와서 개인정보 담기(좋아요 수 포함), 스크랩리스트부르기
// :  기타 이동 버튼(활동내역 ,즐겨찾기, 좋아요, 스크랩리스트, 문의하기)
// :  상단탭 넣기  / 하단탭 코드