package com.example.rotory.account;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.R;
import com.example.rotory.VO.AppConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;


public class FindAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "FindIdActivity";
    Context mContext;
    SharedPreferences userIdShared;
    SharedPreferences.Editor editor;
    AppConstant appConstant;
    Counter mobileCounter = new Counter();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    TextView findAccountTextView;
    TextView findPwTextView;
    TextView findIdMobileCounter;
    TextView findId_id_check;

    EditText mobile;
    EditText findpw_phone_pin;
    EditText findpw_phone_edittext;
    EditText findpw_id_email_edittext;


    Button checkMobileButton;
    Button findpw_phone_button;
    Button findpw_pin_button;
    Button findpw_email_button;


    ImageButton backImageButton;
    RadioGroup pwFindRadios;
    RadioButton withEmail;
    RadioButton withMobile;
    FrameLayout findPwWithMobile;
    FrameLayout findPwWithEmail;

    ImageView pwUnderBar;
    ImageView accountUnderBar;

    LinearLayout findAccount;
    LinearLayout findPw;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id_page);
        mContext = this;

        findAccount = findViewById(R.id.findIdContainer);
        findPw = findViewById(R.id.findPwContainer);

        findId_id_check = findViewById(R.id.findId_id_check);
        mobile = findViewById(R.id.findid_phone_edittext);
        pwFindRadios = findViewById(R.id.pwFindRadioGroup);
        withMobile = findViewById(R.id.findpw_phone_radiobutton);
        withEmail = findViewById(R.id.findpw_emill_radiobutton);
        findPwWithMobile = findViewById(R.id.findpw_phone_pramelayout);
        findPwWithEmail = findViewById(R.id.findpw_email_pramelayout);
        pwUnderBar = findViewById(R.id.myUnderImg3);
        accountUnderBar = findViewById(R.id.myUnderImg2);
        findIdMobileCounter = findViewById(R.id.findIdMobileCount);
        findpw_phone_pin = findViewById(R.id.findpw_phone_pin);
        findpw_pin_button = findViewById(R.id.findpw_pin_button);
        findpw_id_email_edittext = findViewById(R.id.findpw_id_email_edittext);

        findpw_email_button = findViewById(R.id.findpw_email_button);
        findpw_phone_button = findViewById(R.id.findpw_phone_button);

        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logInIntent = new Intent(FindAccountActivity.this, LogInActivity.class);
                logInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(logInIntent);
            }
        });

        findAccountTextView = findViewById(R.id.findAccountTextView);
        findAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTab(0);
            }
        });
        findPwTextView = findViewById(R.id.findPwTextView);
        findPwTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTab(1);
            }
        });

        checkMobileButton = findViewById(R.id.findid_phonebutton);
        checkMobileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("person").document("mobileList")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> checkMobileList = task.getResult().getData();
                                    String checkMobiletext = mobile.getText().toString();
                                    Log.d(TAG, "mobile 변수 체크" + checkMobiletext + " => " + checkMobileList);
                                    if (checkMobiletext.equals("") || checkMobiletext == null) {
                                        Toast.makeText(getApplicationContext(), "번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.d(TAG, "회원가입된 번호 존재" + checkMobileList.keySet());
                                        //checkMobileList.keySet().contains(mobile.getText().toString());
                                        if (checkMobileList.keySet().contains(checkMobiletext)) {
                                            Log.d(TAG, "회원가입된 번호 존재" + checkMobiletext);
                                            getIdDialog("계정 찾기 성공!",checkMobileList.get(checkMobiletext).toString()).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
            }
        });

        withMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPwWithEmail.setVisibility(View.GONE);
                findPwWithMobile.setVisibility(View.VISIBLE);
                mobileCounter.stopCounter(findIdMobileCounter);

            }
        });
        withEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPwWithMobile.setVisibility(View.GONE);
                findPwWithEmail.setVisibility(View.VISIBLE);
                mobileCounter.stopCounter(findIdMobileCounter);
            }
        });

        getPwWithEmail();

        findpw_pin_button.setOnClickListener(this);
        findpw_phone_button.setOnClickListener(this);
        findpw_email_button.setOnClickListener(this);

    }

    private void getPwWithEmail() {
        mobileCounter.countDownTimer(findIdMobileCounter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.findpw_pin_button:
                if (findIdMobileCounter.getText() == "00:00"){
                    Toast.makeText(this, "인증 시간이 초과되었습니다.",Toast.LENGTH_SHORT).show();
                } else {

                }
                break;
            case R.id.findpw_phone_button:
                mobileCounter.stopCounter(findIdMobileCounter);
                mobileCounter.countDownTimer(findIdMobileCounter).start();
                break;
            case R.id.findpw_email_button :
                String checkedEmail = findpw_id_email_edittext.getText().toString();
                checkExistEmail(checkedEmail);
        }

    }

    private void checkExistEmail(String checkedEmail) {

        findId_id_check.setVisibility(View.INVISIBLE);
        db.collection("person").whereEqualTo("userId", checkedEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()){
                                count++;
                            }
                            Log.d(TAG, "비밀번호 찾기 다큐먼트 수" + count);
                            if (count == 0){
                                findId_id_check.setVisibility(View.VISIBLE);
                                Log.d(TAG,checkedEmail +"없음 그대로 유지");
                            } else {
                                Log.d(TAG,checkedEmail + "존재, 메일 전송 시작");
                                sendVerifyEmail(checkedEmail);


                                userIdShared = getSharedPreferences("FindAccountUserId", Context.MODE_PRIVATE);
                                editor  = userIdShared.edit();
                               editor.remove("userId");
                                editor.commit();
                                editor.putString("userId", checkedEmail);
                                Log.d(TAG, checkedEmail+"내부 저장소에 저장");
                                editor.commit();
                                Log.d(TAG, "내부 저장소 확인" + PreferenceManager.getString(mContext,"userId"));

                            }
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void sendVerifyEmail(String checkEmail) {
        FirebaseDynamicLinks.getInstance();
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("https://rotory2021.firebaseapp.com")
                .setHandleCodeInApp(true)
                .setAndroidPackageName("com.example.rotory",
                        false,
                        "12")
                .build();

        /*mAuth.sendPasswordResetEmail(checkEmail,actionCodeSettings)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "이메일 보내기 성공");
                        Toast.makeText(getApplicationContext(), "인증 메일 전송", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "이메일 전송 실패", e);
                // 인증회수 초과 토스트 추가하기
            }
        });*/
      mAuth.sendSignInLinkToEmail(checkEmail, actionCodeSettings)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "이메일 보내기 성공");
                        Toast.makeText(getApplicationContext(), "인증 메일 전송", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "이메일 전송 실패", e);
                // 인증회수 초과 토스트 추가하기
            }
        });

    }

    private AlertDialog getIdDialog(String title, String userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);
        builder.setMessage(userId);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("비밀번호 찾기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTab(1);
                dialog.dismiss();
            }

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(AlertDialog.BUTTON1).setTextColor(Color.BLACK);
                alertDialog.getButton(AlertDialog.BUTTON3).setTextColor(Color.rgb(245, 127, 23));
            }
        });

        return alertDialog;
    }

    public void moveTab(int page) {
        if (page == 0) {
            findPw.setVisibility(View.INVISIBLE);
            findAccount.setVisibility(View.VISIBLE);
            pwUnderBar.setVisibility(View.INVISIBLE);
            accountUnderBar.setVisibility(View.VISIBLE);
        } else if (page == 1){
            findAccount.setVisibility(View.INVISIBLE);
            findPw.setVisibility(View.VISIBLE);
            accountUnderBar.setVisibility(View.INVISIBLE);
            pwUnderBar.setVisibility(View.VISIBLE);
        }
        mobileCounter.stopCounter(findIdMobileCounter);
    }
}
