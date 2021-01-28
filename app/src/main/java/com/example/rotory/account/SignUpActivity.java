package com.example.rotory.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.MainActivity;
import com.example.rotory.R;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Person;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {

    Counter mobileCounter = new Counter();

    AppConstant appConstant;
    private static final String TAG = "SignUpActivity";
    private final String REGEX_PATTERN = appConstant.REGEX_PATTERN;
    private final String REGEX_NUMBER = appConstant.REGEX_NUMBER;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseAuthSettings firebaseAuthSettings = mAuth.getFirebaseAuthSettings();
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    PhoneAuthCredential credential;
    String authNum;

    EditText signin_id_edittext;
    EditText signin_pw_edittext;
    EditText signin_pwcheck_edittext;
    EditText signin_nicname_edittext;
    EditText signin_mobile;
    EditText signin_pin;

    ImageButton signUpBackImageButton;
    Button signUpCheckBtn;
    Button signin_phone_button;
    Button signin_pin_button;

    TextView signUpTitlewithBtnTextView;
    TextView signin_pwcheck_check;
    TextView signin_id_check;
    TextView signin_userName_check;
    TextView signin_mobile_check;
    TextView mobileTimeCountText;
    TextView signup_pw_check;

    String userId;
    String pw;
    String pwCheck;
    String userName;
    String mobile;
    String email;
    String signUpDate = new Date().toString();

    Person persons = new Person();
    boolean checkUserName = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPref = getSharedPreferences("person Document Id List", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        FirebaseUser user = mAuth.getCurrentUser();

       /* String data = sharedPref.getString("#0", "");
        Log.d(TAG, "회원가입 시작 모든 데이터 삭제전 -> " + data);
        editor.clear();
        editor.commit();
        Log.d(TAG, "회원가입 시작 모든 데이터 삭제? -> " + data);*/

        signin_id_edittext = findViewById(R.id.signin_id_edittext);
        signin_pw_edittext = findViewById(R.id.signin_pw_edittext);
        signin_pwcheck_edittext = findViewById(R.id.signin_pwcheck_edittext);
        signin_nicname_edittext = findViewById(R.id.signin_nicname_edittext);
        signin_mobile = findViewById(R.id.signin_mobile);

        signUpTitlewithBtnTextView = findViewById(R.id.signUpTitlewithBtnTextView);
        signUpBackImageButton = findViewById(R.id.signUpBackImageButton);
        signUpCheckBtn = findViewById(R.id.signUpCheckBtn);

        signin_userName_check = findViewById(R.id.signin_userName_check);
        signin_userName_check.setVisibility(View.GONE);
        signin_mobile_check = findViewById(R.id.signin_mobile_check);
        signin_mobile_check.setVisibility(View.GONE);
        signin_id_check = findViewById(R.id.signin_id_check);
        signin_pwcheck_check = findViewById(R.id.signin_pwcheck_check);
        mobileTimeCountText = findViewById(R.id.mobileTimeCountText);
        signin_pin = findViewById(R.id.signin_pin);
        signup_pw_check = findViewById(R.id.signup_pw_check);

        signin_phone_button = findViewById(R.id.signin_phone_button);
        signin_pin_button = findViewById(R.id.signin_phone_button2);

        signin_pin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobileTimeCountText.getText() == "00:00"){
                    Toast.makeText(getApplicationContext(), "인증 시간이 초과되었습니다.",Toast.LENGTH_SHORT).show();
                } else {
                    signup_pw_check.setVisibility(View.INVISIBLE);
                    Log.d(TAG,"인증번호 : " + authNum );
                    String userCode = signin_pin.getText().toString();
                    Log.d(TAG, "입력코드 : " + userCode);
                    if (!userCode.equals(authNum)){
                        Log.d(TAG,"인증번호 불일치"+ userCode + ":" + authNum);
                        //showToast("인증번호가 틀렸습니다");
                        signup_pw_check.setVisibility(View.VISIBLE);
                    }else {
                        showToast("인증 성공");
                        //LogInWithPhoneAuthCredential(mAuth,mContext,credential);
                    }
                }
            }
        });
        signin_phone_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkedPhone = signin_mobile.getText().toString();
                Log.d(TAG, "휴대전화 인증 " + checkedPhone);

                String number = PhoneNumberUtils.formatNumber(signin_mobile.getText().toString(),
                        Locale.getDefault().getCountry());
                if (number == null ||number.length()<8){
                    showToast("핸드폰 번호를 확인해주세요");
                }else {
                    String phoneNum = "+82 " + number.substring(1);
                    Log.d(TAG, "인증번호 보낼 핸드폰 번호 확인 : " + phoneNum);
                    getPwWithMobile(phoneNum);
                    signin_mobile.setEnabled(true);
                    mobileCounter.countDownTimer(mobileTimeCountText).start();

                }
            }
        });

        signUpTitlewithBtnTextView.setText("회원가입");
        signUpBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMain();
                finish();
            }

        });

        userId = signin_id_edittext.getText().toString().trim();
        pw = signin_pw_edittext.getText().toString().trim();
        pwCheck = signin_pwcheck_edittext.getText().toString().trim();
        userName = signin_nicname_edittext.getText().toString().trim();
        mobile = signin_mobile.getText().toString().trim();

        signUpCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin_userName_check.setVisibility(View.GONE);
                signin_pwcheck_check.setVisibility(View.GONE);
                signin_id_check.setVisibility(View.GONE);
                signin_mobile_check.setVisibility(View.GONE);

                FirebaseAuth firebaseAuth = mAuth;
                if (firebaseAuth == null) {
                    Log.e(TAG, "firebaseauth 연결 안됨");
                }

             /*   String data = sharedPref.getString("#0", "");
                Log.d(TAG, "모든 데이터 삭제전 -> " + data);
                editor.clear();
                editor.commit();

                Log.d(TAG, "모든 데이터 삭제? -> " + data);*/

                /*if (userId.equals("") || userId.length() == 0) {
                    signin_id_check.setText("아이디를 입력해 주세요");
                    signin_id_check.setVisibility(View.VISIBLE);
                    return;
                } else*/
                if (checkValidation2()) {
                    setNewAccount(persons);
                    //checkUser(userName);
                    boolean userNameCheck;
                    if (signin_userName_check.getVisibility() == View.VISIBLE){
                        userNameCheck = true;
                    }else{
                        userNameCheck = false;
                    }
                    if(userNameCheck){
                        Log.d(TAG, "중복된 이름" + userName);

                    }else{
                        signUp(userId, pw, persons, user);
                        Log.d(TAG, "회원가입진행");
                    }
                } else {
                    Log.d(TAG, "데이터 적정성 검사 통과 안됨");
                }
            }
        });
    }

    private String checkExistData(SharedPreferences sharedPref, SharedPreferences.Editor editor) {

        userName = signin_nicname_edittext.getText().toString();
        db.collection("person")
                .whereEqualTo("userName", userName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int i = 0;
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        editor.putString("#" + i, documentSnapshot.getId());
                        Log.d(TAG, i + ":" + documentSnapshot.getId());
                        i++;
                        //Log.d(TAG, documentSnapshot.getId());
                    }
                    editor.commit();

                } else {
                    Log.d("TAG", "Error getting documents: " + task.getException().toString(), task.getException());
                }
            }
        });
        String checkResultList = sharedPref.getString("#0", "");
        return checkResultList;

    }

    private final void signUp(final String userId, final String password,
                              Person persons, FirebaseUser user) {

        String UserName = persons.getUserName();

        mAuth.createUserWithEmailAndPassword(userId, password).addOnCompleteListener(
                SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "등록버튼" + userId + ", " + password);
                            FirebaseUser users = mAuth.getCurrentUser();
                            String userId = users.getEmail();
                            String uid = users.getUid();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(UserName)
                                    .build();
                            users.updateProfile(profileUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                Log.d(TAG, "user profile update");
                                        }
                                    });
                            saveUserAccount(userId, uid, persons, users);
                            goMain();

                        } else {
                            Log.e(TAG, "signUp Failed : " + userId + ", " + password + ","
                                    + task.getException().toString());
                            checkValidation2();
                            signin_id_check.setText("아이디 중복");
                            signin_id_check.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }


    private boolean checkValidation2() {
        userId = signin_id_edittext.getText().toString().trim();
        pw = signin_pw_edittext.getText().toString().trim();
        pwCheck = signin_pwcheck_edittext.getText().toString().trim();
        userName = signin_nicname_edittext.getText().toString().trim();
        mobile = signin_mobile.getText().toString().trim();

        boolean mobilePattern = Pattern.matches(REGEX_NUMBER, mobile);
        boolean pwPattern = Pattern.matches(REGEX_PATTERN, pw);
        Log.d(TAG,"visibility 확인" + signin_userName_check.getVisibility());
        if (userId.equals("") || userId.length() == 0) {
            signin_id_check.setText("아이디를 입력해 주세요");
            signin_id_check.setVisibility(View.VISIBLE);
            return false;
        } else if (!userId.contains("@")) {
            signin_id_check.setText("이메일 양식으로 작성해주세요");
            signin_id_check.setVisibility(View.VISIBLE);
            return false;
        } else if (!pwPattern) {
            signin_pwcheck_check.setText("비밀번호 양식을 확인해주세요");
            signin_pwcheck_check.setVisibility(View.VISIBLE);
            return false;

        } else if (!pwCheck.equals(pw)) {
            signin_pwcheck_check.setText("비밀번호 불일치");
            signin_pwcheck_check.setVisibility(View.VISIBLE);
            return false;
        } else if (userName.equals("") || userName.length() < 1) {
            signin_userName_check.setText("닉네임을 입력해 주세요");
            signin_userName_check.setVisibility(View.VISIBLE);
            return false;
        } else if (!mobilePattern) {
            return false;
        } else if (!signin_pin_button.isEnabled()){
            return false;

            /*else if (signin_userName_check.getVisibility() == View.VISIBLE) {
                Log.d(TAG, "중복된 이름" + userName);
                return false;
        }*/
        }

        return true;
    }


    public Person setNewAccount(Person persons) {
        Uri userImageUri = Uri.parse("android.resource://com.example.rotory/drawable/squirrel");
        Uri userLevelImageUri = Uri.parse("android.resource://com.example.rotory/drawable/level1");

        persons.setUserId(userId);
        persons.setPassword(pw);
        persons.setMobile(mobile);
        persons.setUserImage(userImageUri.toString());
        persons.setUserLevel("아기다람쥐");
        persons.setUserLevelImage(userLevelImageUri.toString());
        persons.setUserName(userName);
        persons.setSignUpDate(signUpDate);
        return persons;


    }


    private void saveUserAccount(String userId, String uid, Person persons, FirebaseUser user) {
        // 이후에 계정으로 저장과 계정 정보 수정으로 나누어서 정리, 합침

        persons.setUserId(userId);
        persons.setUid(uid);

        HashMap<String, Object> person = new HashMap<>();

        person.put("userId", persons.getUserId());
        person.put("userName", persons.getUserName());
        person.put("password", persons.getPassword());
        person.put("mobile", persons.getMobile());
        person.put("uid", persons.getUid());
        person.put("userLevel", persons.getUserLevel());
        person.put("userImage", persons.getUserImage());
        person.put("userLevelImage", persons.getUserLevelImage());
        person.put("userPoint",0);
        person.put("signUpDate", persons.getSignUpDate());
        //Date (그날날짜) 받아와서 다시저장


        db.collection("person")
                .add(person)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Person add with id" + documentReference.getId());
                        setScrapItem(persons.getUserId().toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding user", e);
                    }
                });

        Map<String, String> addUserName = new HashMap<>();
        addUserName.put(userId, userName);
        db.collection("person").document("userNameList")
                .set(addUserName, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG,"adduserList Success" + userName);
                }else {
                    Log.d(TAG,"adduserList Failed");
                }
            }
        });

        Map<String, String> addMobileName = new HashMap<>();
        addMobileName.put(mobile, userId);
        db.collection("person").document("mobileList")
                .set(addMobileName, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG,"adduserList Success" + mobile);
                }else {
                    Log.d(TAG,"adduserList Failed");
                }
            }
        });


    }

    private void setScrapItem(String userId) {
        db.collection("person").whereEqualTo("userId", userId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        String personId;
                        Map<String, Object> defaultScrapSet = new HashMap<>();
                        for (QueryDocumentSnapshot pDocument : task.getResult()){
                            personId = pDocument.getId();
                            defaultScrapSet.put(" title", "");
                            defaultScrapSet.put("contentsAddress","");
                            defaultScrapSet.put("savedDate", "");
                            defaultScrapSet.put("contentsId", "");
                            defaultScrapSet.put("titleImage", String.valueOf(R.drawable.black_scrap));
                            for (int i = 0; i < 8; i++) {
                                db.collection("person").document(personId).collection("myScrap")
                                        .add(defaultScrapSet).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Log.d(TAG, "비어있는 스크랩 저장");
                                    }
                                });
                            }
                        }
                    }
            }
        });
    }


    public void goMain() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(mainIntent, appConstant.mainCode);

    }

    private void checkMobile(String checkMobile) {

        DocumentReference reference = db.collection("person").document("mobileList");
        reference
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                Map<String, Object> mobileList = new HashMap<>();
                                mobileList = snapshot.getData();
                                if (mobileList.containsValue(checkMobile)) {
                                        Log.d(TAG, "Exist mobile" + checkMobile);
                                        signin_mobile_check.setText("이미 존재하는 번호입니다.");
                                        signin_mobile_check.setVisibility(View.VISIBLE);

                                    } else {
                                        Log.d(TAG, "new mobile" + checkMobile);
                                        signin_userName_check.setVisibility(View.INVISIBLE);
                                    }

                                } else {
                                    Log.d(TAG, "checkUser Failed");
                                }
                            } else {
                                Log.d(TAG, "db 불러오기 실패");

                            }
                        }

                });

    }

    private void getPwWithMobile(String phoneNum) {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
        mobileCounter.countDownTimer(mobileTimeCountText); // 카운트다운 될 택스트 선택

        signin_pin_button.setEnabled(true); //인증확인 버튼

        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNum,"123456");

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


    private void showToast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}