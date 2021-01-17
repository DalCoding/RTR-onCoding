package com.example.rotory.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
<<<<<<< HEAD
<<<<<<< HEAD:app/src/main/java/com/example/rotory/Account/SignUpActivity.java
<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java
=======
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718:app/src/main/java/com/example/rotory/account/SignUpActivity.java
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
<<<<<<< HEAD
=======
import android.widget.Toast;
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.MainActivity;
import com.example.rotory.R;
<<<<<<< HEAD
import com.example.rotory.VO.AppConstruct;
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
import com.example.rotory.VO.Person;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
<<<<<<< HEAD
<<<<<<< HEAD:app/src/main/java/com/example/rotory/Account/SignUpActivity.java
<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java
import com.google.firebase.firestore.DocumentReference;

=======
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java
=======
import com.google.firebase.firestore.DocumentReference;

>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718:app/src/main/java/com/example/rotory/account/SignUpActivity.java
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
=======
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {
<<<<<<< HEAD
<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
=======
    //닉네임 중복검사 -> 진행중.. 과정중에 한번 브레이크가 걸려야 중복검사 가능.. 어떤식으로? 고민중... 0115


    AppConstruct appConstruct;
>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java
=======

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
    private static final String TAG = "SignUpActivity";
    private final String REGEX_PATTERN = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
    private final String REGEX_NUMBER = "^(?=.*[0-9])[0-9]{9,12}$";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
<<<<<<< HEAD
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718

    EditText signin_id_edittext;
    EditText signin_pw_edittext;
    EditText signin_pwcheck_edittext;
    EditText signin_nicname_edittext;
    EditText signin_mobile;
    EditText signin_email_edittext;

    ImageButton signUpBackImageButton;
    Button signUpCheckBtn;
    TextView signUpTitlewithBtnTextView;
    TextView signin_pwcheck_check;
    TextView signin_id_check;
    TextView signin_userName_check;
    TextView signin_mobile_check;

<<<<<<< HEAD
    String userId;
    String pw;
=======

    String userId;
    String pw ;
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
    String pwCheck;
    String userName;
    String mobile;
    String email;
<<<<<<< HEAD
<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java

    Person persons = new Person();
    boolean checkUserName = true;
=======
    String signUpDate = new Date().toString();

    Person persons = new Person();
    boolean checkUserName = true;

>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java
=======

    Person persons = new Person();
    boolean checkUserName = true;
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPref = getSharedPreferences("person Document Id List", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
<<<<<<< HEAD
        FirebaseUser user = mAuth.getCurrentUser();

       /* String data = sharedPref.getString("#0", "");
        Log.d(TAG, "회원가입 시작 모든 데이터 삭제전 -> " + data);
        editor.clear();
        editor.commit();

        Log.d(TAG, "회원가입 시작 모든 데이터 삭제? -> " + data);*/
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718

        signin_id_edittext = findViewById(R.id.signin_id_edittext);
        signin_pw_edittext = findViewById(R.id.signin_pw_edittext);
        signin_pwcheck_edittext = findViewById(R.id.signin_pwcheck_edittext);
        signin_nicname_edittext = findViewById(R.id.signin_nicname_edittext);
        signin_mobile = findViewById(R.id.signin_mobile);
        signin_email_edittext = findViewById(R.id.signin_email_edittext);

        signUpTitlewithBtnTextView = findViewById(R.id.signUpTitlewithBtnTextView);
        signUpBackImageButton = findViewById(R.id.signUpBackImageButton);
        signUpCheckBtn = findViewById(R.id.signUpCheckBtn);


        signin_userName_check = findViewById(R.id.signin_userName_check);
        signin_userName_check.setVisibility(View.GONE);
        signin_mobile_check = findViewById(R.id.signin_mobile_check);
        signin_mobile_check.setVisibility(View.GONE);
        signin_id_check = findViewById(R.id.signin_id_check);
<<<<<<< HEAD
<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java
        signin_pwcheck_check = findViewById(R.id. signin_pwcheck_check);
=======
        signin_pwcheck_check = findViewById(R.id.signin_pwcheck_check);
>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java
=======
        signin_pwcheck_check = findViewById(R.id. signin_pwcheck_check);
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718

        signUpTitlewithBtnTextView.setText("회원가입");
        signUpBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goMain();
                //finish();


                userName = signin_nicname_edittext.getText().toString();
                db.collection("person")
                        .whereEqualTo("userName",  userName )
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            int i = 0;
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){

                                editor.putString("#"+ i, documentSnapshot.getId());
                                Log.d(TAG,i + ":" + documentSnapshot.getId());
                                i++;
                                //Log.d(TAG, documentSnapshot.getId());
                            }
                            editor.commit();

                        }else{
                            Log.d("TAG", "Error getting documents: " +task.getException().toString(), task.getException());
                        }
                    }
                });

<<<<<<< HEAD
               /* String data = sharedPref.getString("#0", "");
                Log.d(TAG, "모든 데이터 삭제전 -> " + data);
                editor.clear();
                editor.commit();

                Log.d(TAG, "모든 데이터 삭제? -> " + data);

                resetNotiMsg();
                if (checkValidation2(sharedPref, editor)){
                    Log.d(TAG, "회원가입진행");
                }else{
                    Log.d(TAG, "데이터 적정성 검사 통과 안됨");
                }*/
            }

        });
<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java
=======
            }

        });
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
        String data = sharedPref.getString("#"+ 0,"");
        if(data != null){
            Log.d(TAG,"모든 데이터 삭제전 -> " + data);
            checkUserName = true;
            editor.clear();
            editor.commit();
            data = sharedPref.getString("#"+ 0,"");
            Log.d(TAG,"모든 데이터 삭제? -> " + data);
        }else {
            checkUserName = false;
        }


<<<<<<< HEAD
=======
>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java

        userId = signin_id_edittext.getText().toString().trim();
        pw = signin_pw_edittext.getText().toString().trim();
=======

        userId = signin_id_edittext.getText().toString().trim();
        pw =signin_pw_edittext.getText().toString().trim();
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
        pwCheck = signin_pwcheck_edittext.getText().toString().trim();
        userName = signin_nicname_edittext.getText().toString().trim();
        mobile = signin_mobile.getText().toString().trim();
        email = signin_email_edittext.getText().toString().trim();

        signUpCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
                List<String> userNameList = new ArrayList<>();
                FirebaseAuth firebaseAuth = mAuth;
                if (firebaseAuth == null){
                    Log.e(TAG,"firebaseauth 연결 안됨");
                }
                FirebaseUser user = mAuth.getCurrentUser();

                resetNotiMsg();
                if(checkValidation()) {
                    setNewAccount(persons);
                    signUp(userId, pw, persons, user);
                }


                /*

                List<String> userNameList = new ArrayList<>();
                db.collection("person")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Log.e(TAG, "Listen failed.", error);
                                }
                                loop:
                                for (QueryDocumentSnapshot documentSnapshot : value) {
                                    if (documentSnapshot.get("userName") != null) {
                                        userNameList.add(documentSnapshot.getString("userName"));
                                        if (userNameList.contains(userName)) {
                                            signin_userName_check.setVisibility(View.VISIBLE);
                                            Log.d(TAG, "Duplicated Username"+ userName + ", Check userNameList : " + userNameList);
                                            break loop;
                                        } else {
                                            Log.d(TAG,"new Username" + userName);
                                            existMobile(mobile);
<<<<<<< HEAD
=======
                signin_userName_check.setVisibility(View.GONE);
                signin_pwcheck_check.setVisibility(View.GONE);
                signin_id_check.setVisibility(View.GONE);
                signin_mobile_check.setVisibility(View.GONE);

                FirebaseAuth firebaseAuth = mAuth;
                if (firebaseAuth == null) {
                    Log.e(TAG, "firebaseauth 연결 안됨");
                }
>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java

             /*   String data = sharedPref.getString("#0", "");
                Log.d(TAG, "모든 데이터 삭제전 -> " + data);
                editor.clear();
                editor.commit();

<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java
=======

                                        }
                                    } else {
                                    }
                                }

>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
                            }
                        });*/
            }
        });
    }

    private void resetData() {
    }

    public void resetNotiMsg(){
        signin_userName_check.setVisibility(View.GONE);
        signin_pwcheck_check.setVisibility(View.GONE);
        signin_id_check.setVisibility(View.GONE);
        signin_mobile_check.setVisibility(View.GONE);
<<<<<<< HEAD
=======
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
>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718

    }

    private final void signUp(final String userId, final String password,
<<<<<<< HEAD
<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java
                              Person persons, FirebaseUser user){
=======
                              Person persons, FirebaseUser user) {

        String UserName = persons.getUserName();
>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java
=======
                              Person persons, FirebaseUser user){
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718

        String UserName = persons.getUserName();
        mAuth.createUserWithEmailAndPassword(userId, password).addOnCompleteListener(
                SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
<<<<<<< HEAD
                        if (task.isSuccessful()) {
                            Log.d(TAG, "등록버튼" + userId + ", " + password);
<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java
=======
                        if(task.isSuccessful()){
                            Log.d(TAG, "등록버튼" + userId + ", " + password);
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
                            String userId = user.getEmail();
                            String uid = user.getUid();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(UserName)
                                    .build();
                            user.updateProfile(profileUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                                Log.d(TAG, "user profile update");
                                            saveUserAccount(userId, uid, persons, user);
                                        }
                                    });

                        }else {
                            Log.e(TAG, "signUp Failed : " + userId + ", " + password + ","
                                    + task.getException().toString());
                            checkValidation();
                            signin_id_check = findViewById(R.id.signin_id_check);
<<<<<<< HEAD
=======
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
>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
                            signin_id_check.setVisibility(View.VISIBLE);
                        }
                    }
                });
<<<<<<< HEAD
<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java

    }

=======
>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java

    }


    private boolean checkValidation2() {
        userId = signin_id_edittext.getText().toString().trim();
        pw = signin_pw_edittext.getText().toString().trim();
=======

    }



    private boolean checkValidation(){

        userId = signin_id_edittext.getText().toString().trim();
        pw =signin_pw_edittext.getText().toString().trim();
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
        pwCheck = signin_pwcheck_edittext.getText().toString().trim();
        userName = signin_nicname_edittext.getText().toString().trim();
        mobile = signin_mobile.getText().toString().trim();
        email = signin_email_edittext.getText().toString().trim();

<<<<<<< HEAD
<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java

        boolean pwPattern = Pattern.matches(REGEX_PATTERN, pw);
=======
>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java
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
            signin_pwcheck_check.setText("비밀번호를 확인해주세요");
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
        } /*else if (signin_userName_check.getVisibility() == View.VISIBLE) {
                Log.d(TAG, "중복된 이름" + userName);
                return false;
        }*/

        return true;
    }


    public Person setNewAccount(Person persons) {
        Uri userImageUri = Uri.parse("android.resource://com.example.rotory/drawable/squirrel");
        Uri userLevelImageUri = Uri.parse("android.resource://com.example.rotory/drawable/level1");

        persons.setUserId(userId);
        persons.setPassword(pw);
        persons.setMobile(mobile);
        persons.setUserImage(userImageUri.toString());
        persons.setEmail(email);
        persons.setUserLevel("아기다람쥐");
        persons.setUserLevelImage(userLevelImageUri.toString());
        persons.setUserName(userName);
        persons.setSignUpDate(signUpDate);
        return persons;


<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java
=======
    }


>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java
    private void saveUserAccount(String userId, String uid, Person persons, FirebaseUser user) {
        // 이후에 계정으로 저장과 계정 정보 수정으로 나누어서 정리, 합침

        persons.setUserId(userId);
        persons.setUid(uid);

        HashMap<Object, String> person = new HashMap<>();
        person.put("email", persons.getEmail());
        person.put("userId", persons.getUserId());
        person.put("userName", persons.getUserName());
        person.put("password", persons.getPassword());
        person.put("mobile", persons.getMobile());
        person.put("uid", persons.getUid());
        person.put("userLevel", persons.getUserLevel());
        person.put("userImage", persons.getUserImage());
        person.put("userLevelImage", persons.getUserLevelImage());
        person.put("signUpDate", persons.getSignUpDate());
=======

        boolean pwPattern = Pattern.matches(REGEX_PATTERN, pw);
        boolean mobilePattern = Pattern.matches(REGEX_NUMBER, mobile);


      if( userName.equals("")|| userName.length()<1) {
            signin_userName_check.setText("닉네임을 입력해 주세요");
            signin_userName_check.setVisibility(View.VISIBLE);
            return false;
        }else if(!pwPattern){
            Toast.makeText(getApplicationContext(),"비밀번호 양식을 확인해주세요", Toast.LENGTH_SHORT).show();
           //다이얼로그 넣기
            return false;

        } else if(!pwCheck.equals(pw)){
            signin_pwcheck_check.setVisibility(View.VISIBLE);

            return false;

        }else if(!userId.contains("@")){

            signin_id_check.setText("이메일 양식으로 작성해주세요");
            signin_id_check.setVisibility(View.VISIBLE);

            return false;
        }else if(!mobilePattern){
            return false;
        }

        return true;

    }


    public Person setNewAccount(Person persons){
    persons.setUserId(userId);
    persons.setPassword(pw);
    persons.setMobile(mobile);
    persons.setUserImage(String.valueOf(R.drawable.squirrel));
    persons.setEmail(email);
    persons.setUserLevel("아기다람쥐");
    persons.setUserName(userName);
    persons.setUserLevelImage(String.valueOf(R.drawable.level1));
    return persons;
    }



    private void saveUserAccount(String userId, String uid, Person persons, FirebaseUser user) {
        // 이후에 계정으로 저장과 계정 정보 수정으로 나누어서 정리, 합침
        persons.setUserId(userId);
        persons.setPerson_id(uid);

        HashMap<Object, String> person = new HashMap<>();
        person.put("userEmail", persons.getEmail());
        person.put("email", persons.getUserId());
        person.put("userName", persons.getUserName());
        person.put("password", persons.getPassword());
        person.put("mobile", persons.getMobile());
        person.put("uid", persons.getPerson_id());
        person.put("userLevel", persons.getUserLevel());
        person.put("userImage", persons.getUserImage());
        person.put("userLevelImage", persons.getUserLevelImage());
        person.put("signUpDate", new Date().toString());
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
        //Date (그날날짜) 받아와서 다시저장

        FirebaseFirestore db = FirebaseFirestore.getInstance();

<<<<<<< HEAD
       /* db.collection("person")
=======
        db.collection("person")
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
                .add(person)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Person add with id" + documentReference.getId());
<<<<<<< HEAD
                        //goMain();
=======
                        goMain();
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding user", e);
                    }
                });
<<<<<<< HEAD
*/
        Map<String, String> addUserName = new HashMap<>();
        addUserName.put(userName, userName);
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


    }


    public void goMain() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(mainIntent,appConstruct.mainCode);

    }

    private void checkUser(String userName) {

        DocumentReference reference = db.collection("person").document("userNameList");
        reference
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                Map<String, Object> userNameList = new HashMap<>();
                                userNameList = snapshot.getData();
                                if (userNameList.get(userName) != null) {
                                    String existUserName = userNameList.get(userName).toString();
                                    if (existUserName.equals(userName)) {
                                        Log.d(TAG, "Exist userName" + existUserName);
                                        signin_userName_check.setVisibility(View.VISIBLE);

                                    } else {
                                        Log.d(TAG, "new userName" + userName);
                                        signin_userName_check.setVisibility(View.INVISIBLE);
                                    }

                                } else {
                                    Log.d(TAG, "checkUser Failed");
                                }
                            } else {
                                Log.d(TAG, "db 불러오기 실패");

                            }
                        }
                    }
                });

    }

<<<<<<< HEAD:app/src/main/java/com/example/rotory/account/SignUpActivity.java
=======
    }

>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718

    public void goMain(){
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);

    }

<<<<<<< HEAD
=======
>>>>>>> master:app/src/main/java/com/example/rotory/signup/SignUpActivity.java
=======
>>>>>>> 731d08cec0ed8fb196e108f03b5c546e446cb718
}
 /* private final void signUp(final String userId, final String password,
                              String email, String name,String mobile){
        FirebaseAuth firebaseAuth = this.mAuth;
        if (firebaseAuth == null){
            Log.e(TAG,"firebaseauth 연결 안됨");
        }
        mAuth.createUserWithEmailAndPassword(userId, password).addOnCompleteListener(
                SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "등록버튼" + userId + ", " + password);
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userId = user.getEmail();
                    String uid = user.getUid();

                    saveUserAccount(userId, pw, email,name, mobile, uid);

                }else {
                    Toast.makeText(getApplicationContext(),"회원가입 실패",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

  /*  private void saveUserAccount(String userId, String pw, String email,
                                 String name,String mobile, String Uid) {

        HashMap<Object, String> person = new HashMap<>();
        person.put("userEmail", email);
        person.put("email", userId);
        person.put("name", name);
        person.put("password", pw);
        person.put("mobile", mobile);
        person.put("uid", Uid);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("person")
                .add(person)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Person add with id" + documentReference.getId());
                        goMain();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding user", e);
                    }
                });

    }*/

  /*  private void existUserName() {
        List<String> userNameList = new ArrayList<>();

            db.collection("person")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.w(TAG, "Listen failed.", error);
                            }
                            boolean exist = true;
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                if (documentSnapshot.get("userName") != null) {
                                    userNameList.add(documentSnapshot.getString("userName"));
                                    if (userNameList.contains("hello")) {
                                        signin_userName_check.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "Duplicated Username"+ userName + ", Check userNameList : " + userNameList);

                                    } else {
                                        Log.d(TAG,"new Username" + userName);
                                    }
                                } else {
                                }
                            }

                        }

                    });

    }*/

   /* private void existOrNot(int tf){
        if (tf == 0){
            exist = 1;
        }else
            exist = 0;
    }*/

 /* private boolean existMobile2(String mobile){
        List<String> mobileList = new ArrayList<>();
        userId = signin_id_edittext.getText().toString().trim();
        pw =signin_pw_edittext.getText().toString().trim();
        signin_mobile_check.setVisibility(View.INVISIBLE);

        db.collection("person")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(TAG, "Listen failed", error);
                        }
                        loop:
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            if (documentSnapshot.get("mobile") != null) {
                                mobileList.add(documentSnapshot.getString("mobile"));
                                if (mobileList.contains(mobile)) {
                                    signin_mobile_check.setVisibility(View.VISIBLE);
                                    Log.d(TAG, "mobileList : " + mobileList);
                                    break loop;

                                } else {
                                    Log.d(TAG,"new mobile");

                                }
                            } else {

                            }

                        }
                    }
                });
        Toast.makeText(getApplicationContext(),"중복확인",Toast.LENGTH_SHORT).show();
        return false;
    }
    private void existMobile(String mobile, FirebaseUser user){
        List<String> mobileList = new ArrayList<>();
        userId = signin_id_edittext.getText().toString().trim();
        pw =signin_pw_edittext.getText().toString().trim();
        signin_mobile_check.setVisibility(View.INVISIBLE);

            db.collection("person")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.w(TAG, "Listen failed", error);
                            }
                            loop:
                            for (QueryDocumentSnapshot documentSnapshot : value) {
                                if (documentSnapshot.get("mobile") != null) {
                                    mobileList.add(documentSnapshot.getString("mobile"));
                                    if (mobileList.contains(mobile)) {
                                        signin_mobile_check.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "mobileList : " + mobileList);
                                        break loop;

                                    } else {
                                        Log.d(TAG,"new mobile");
                                        if(checkValidation()) {
                                            setNewAccount(persons);
                                            signUp(userId, pw, persons, user);
                                        }
                                    }
                                } else {

                                }

                            }
                        }
                    });
            Toast.makeText(getApplicationContext(),"중복확인",Toast.LENGTH_SHORT).show();
    }*/