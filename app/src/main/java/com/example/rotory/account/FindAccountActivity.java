package com.example.rotory.account;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.TextWatcher;
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

import com.example.rotory.MainActivity;
import com.example.rotory.R;
import com.example.rotory.VO.AppConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/*

* */
public class FindAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "FindIdActivity";
    Context mContext;
    SharedPreferences userIdShared;
    SharedPreferences.Editor editor;
    Counter mobileCounter = new Counter();
    AppConstant appConstant = new AppConstant();

    String authNum;
    PhoneAuthCredential credential;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
   // FirebaseAuthSettings firebaseAuthSettings = mAuth.getFirebaseAuthSettings();
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    FirebaseUser user;

    TextView findAccountTextView;
    TextView findPwTextView;
    TextView findIdMobileCounter;
    TextView findpw_phone_check;
    TextView findPw_id_check;
    TextView findpw_pin_check;


    EditText mobile;
    EditText findpw_phone_pin;
    EditText findpw_phone_edittext;
    EditText findpw_id_email_edittext;
    EditText findpw_id_phone_edittext;

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

    /* phonepwcheck에서 userId가 존재하는지 확인하기
       인증성공하면 로그인 하고 비번찾는페이지로 이동
    */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_account_page);
        mContext = this;

        findAccount = findViewById(R.id.findIdContainer);
        findPw = findViewById(R.id.findPwContainer);

        pwUnderBar = findViewById(R.id.myUnderImg3);
        accountUnderBar = findViewById(R.id.myUnderImg2);

        mobile = findViewById(R.id.findid_phone_edittext);
        pwFindRadios = findViewById(R.id.pwFindRadioGroup);
        withMobile = findViewById(R.id.findpw_phone_radiobutton);
        withEmail = findViewById(R.id.findpw_emill_radiobutton);

        findIdMobileCounter = findViewById(R.id.findIdMobileCount);

        findpw_phone_pin = findViewById(R.id.findpw_phone_pin);
        findpw_pin_button = findViewById(R.id.findpw_pin_button);
        findpw_phone_edittext = findViewById(R.id.findpw_phone_edittext);
        findpw_id_email_edittext = findViewById(R.id.findpw_id_email_edittext);
        findpw_id_phone_edittext = findViewById(R.id.findpw_id_phone_edittext);
        findpw_phone_check = findViewById(R.id.findpw_phone_check);
        findpw_pin_check = findViewById(R.id.findpw_pin_check);
        findPwWithMobile = findViewById(R.id.findpw_phone_pramelayout);
        findPwWithEmail = findViewById(R.id.findpw_email_pramelayout);
        findPw_id_check = findViewById(R.id.findpw_id_check);


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

        findPwTextView = findViewById(R.id.findPwTextView);
        findPwTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTab(1);
            }
        });

        withMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPwWithEmail.setVisibility(View.GONE);
                findPwWithMobile.setVisibility(View.VISIBLE);
                //mobileCounter.stopCounter(findIdMobileCounter);

            }
        });
        withEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPwWithMobile.setVisibility(View.GONE);
                findPwWithEmail.setVisibility(View.VISIBLE);
                //mobileCounter.stopCounter(findIdMobileCounter);
            }
        });

        //requestSms = new RequestSms(mContext,findIdMobileCounter);

        findpw_phone_button.setOnClickListener(this);
        findpw_pin_button.setOnClickListener(this);
        findpw_email_button.setOnClickListener(this);


    }

    private void getPwWithMobile(String phoneNum) {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
        mobileCounter.countDownTimer(findIdMobileCounter);

        findpw_phone_pin.setEnabled(true);

       // firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNum,"123456");

        Log.d(TAG, "입력한 핸드폰 번호 " + phoneNum);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "인증코드 전송 성공");
                showToast("인증번호가 발송되었습니다. 60초 이내에 입력해주세요");
                credential = phoneAuthCredential;
                authNum = phoneAuthCredential.getSmsCode();
                Log.d(TAG,"onVerificationCode에서 나오는  phoneAuthCredential" + authNum);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "인증실패" + e.getMessage());
                showToast("인증실패");
            }

            @Override
            public void onCodeSent(@NonNull String authNum, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(authNum, forceResendingToken);

                Log.d(TAG, "onCodeSent의 authnum : forceResendingToken" + authNum + " : " + forceResendingToken);

                //mobileCounter.countDownTimer(findIdMobileCounter);

            }
        };
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNum)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

        Log.d(TAG,"인증번호 보냄 " + authNum);
    }

    @Override
    public void onClick(View v) {
        String checkedPhone = findpw_id_phone_edittext.getText().toString();
        switch (v.getId()){
           case R.id.findpw_phone_edittext:

               break;
           case  R.id.findpw_pin_button:
                if (findIdMobileCounter.getText() == "00:00"){
                    Toast.makeText(this, "인증 시간이 초과되었습니다.",Toast.LENGTH_SHORT).show();
                } else {
                    findpw_pin_check.setVisibility(View.INVISIBLE);
                    Log.d(TAG,"인증번호 : " + authNum );
                    String userCode = findpw_phone_pin.getText().toString();
                    Log.d(TAG, "입력코드 : " + userCode);
                    if (!userCode.equals(authNum)){
                        Log.d(TAG,"인증번호 불일치"+ userCode + ":" + authNum);
                        //showToast("인증번호가 틀렸습니다");
                        findpw_pin_check.setVisibility(View.VISIBLE);
                    }else {
                        showToast("인증 성공");
                        LogInAfterPhoneAuth(checkedPhone);
                       //LogInWithPhoneAuthCredential(mAuth,mContext,credential);
                    }
                }
                break;
            case R.id.findpw_phone_button:

                Log.d(TAG, "휴대전화로 찾기 아이디 입력 " + checkedPhone);
                checkExistEmail(checkedPhone, 0,findpw_phone_check);
                String number = PhoneNumberUtils.formatNumber(findpw_phone_edittext.getText().toString(),
                        Locale.getDefault().getCountry());
                if (number == null ||number.length()<8){
                    showToast("핸드폰 번호를 확인해주세요");
                }else {
                    String phoneNum = "+82 " + number.substring(1);
                    Log.d(TAG, "인증번호 보낼 핸드폰 번호 확인 : " + phoneNum);
                    getPwWithMobile(phoneNum);
                    findpw_pin_button.setEnabled(true);
                    mobileCounter.countDownTimer(findIdMobileCounter).start();

                }
                break;
            case R.id.findpw_email_button :
                String checkedEmail = findpw_id_email_edittext.getText().toString();
                checkExistEmail(checkedEmail, 1,findPw_id_check); /*0은 휴대폰인증 1은 이메일 인증*/
        }

    }

    private void checkExistEmail(String checkedEmail, int findType, TextView errorMsg) {/* findType 0은 휴대폰인증 1은 이메일 인증*/
        errorMsg.setVisibility(View.INVISIBLE);
        Log.d(TAG, "CHECKEEXISTEMAIL : 아이디 찾기 시작" );
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
                                errorMsg.setVisibility(View.VISIBLE);
                                Log.d(TAG,checkedEmail +"없음 그대로 유지");
                            } else {
                                Log.d(TAG,checkedEmail + "존재, 메일 전송 시작");

                                //내부저장소에 입력된 계정 저장 -> 이후 비밀번호 변경 페이지로 전송하기 위한 과정
                                userIdShared = getSharedPreferences("FindAccountUserId", Context.MODE_PRIVATE);
                                editor  = userIdShared.edit();
                               editor.remove("userId");
                                editor.commit();
                                editor.putString("userId", checkedEmail);
                                Log.d(TAG, checkedEmail+"내부 저장소에 저장");
                                editor.commit();
                                Log.d(TAG, "내부 저장소 확인" + userIdShared.getString("userId",null));

                                //저장 이후 비밀번호 찾기 유형에 따라 다른 행동으로 이어짐 0은 휴대폰 찾기 1을 이메일 찾기
                                if (findType == 1){
                                    sendVerifyEmail(checkedEmail);
                                }else if(findType == 0){
                                    findpw_phone_button.setEnabled(true);
                                    mobileCounter.countDownTimer(findIdMobileCounter);

                                }
                            }
                        }
                    }
                });
    }

  public void LogInAfterPhoneAuth(String userId) {
        Log.d(TAG, "휴대폰 인증 이후 유저아이디 확인" + userId);
        db.collection("person").whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"인증정보 유무 확인 " + task.getResult().getDocuments());
                            for (QueryDocumentSnapshot pDocument : task.getResult()){
                                String password = pDocument.get("password").toString();
                                Log.d(TAG,"인증 넘기기 전 비밀번호 확인" + password);
                                logInUser(userId, password, mAuth);
                            }
                        }else{
                         Log.d(TAG,"휴대폰 인증 후 인증정보 확인 실패");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"휴대폰 인증 후 로그인 실패 : " + e.toString());
                showToast("계정정보를 찾지 못했습니다. \n 잠시후 다시 시도해주세요");
            }
        });

    }
    public void logInUser(String userId, String password,FirebaseAuth firebaseAuth){
        firebaseAuth.signInWithEmailAndPassword(userId, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                            firebaseAuth.addAuthStateListener(firebaseAuthListener);

                        } else{
                            showToast("아이디 혹은 비밀 번호가 일치하지 않습니다");
                        }

                        if (!task.isSuccessful()){
                            showToast( "로그인 요청 실패");
                        }
                    }
                });
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Intent setNewPWIntent = new Intent(getApplicationContext(), SetNewPassword.class);
                    setNewPWIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(setNewPWIntent);
                }else {
                    Log.d(TAG,"AuthStateChangeListener, 유저 불러오기 실패");
                }
            }
        };
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
        if (findpw_pin_button.isEnabled()) {
            mobileCounter.stopCounter(findIdMobileCounter);
        }
    }


    private void showToast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
