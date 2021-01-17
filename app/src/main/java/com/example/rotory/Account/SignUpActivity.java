package com.example.rotory.Account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.rotory.VO.Person;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String TAG = "SignUpActivity";
    private final String REGEX_PATTERN = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
    private final String REGEX_NUMBER = "^(?=.*[0-9])[0-9]{9,12}$";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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


    String userId;
    String pw ;
    String pwCheck;
    String userName;
    String mobile;
    String email;

    Person persons = new Person();
    boolean checkUserName = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPref = getSharedPreferences("person Document Id List", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

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
        signin_pwcheck_check = findViewById(R.id. signin_pwcheck_check);

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

            }

        });
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



        userId = signin_id_edittext.getText().toString().trim();
        pw =signin_pw_edittext.getText().toString().trim();
        pwCheck = signin_pwcheck_edittext.getText().toString().trim();
        userName = signin_nicname_edittext.getText().toString().trim();
        mobile = signin_mobile.getText().toString().trim();
        email = signin_email_edittext.getText().toString().trim();

        signUpCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                                        }
                                    } else {
                                    }
                                }

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

    }

    private final void signUp(final String userId, final String password,
                              Person persons, FirebaseUser user){

        String UserName = persons.getUserName();
        mAuth.createUserWithEmailAndPassword(userId, password).addOnCompleteListener(
                SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "등록버튼" + userId + ", " + password);
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
                            signin_id_check.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }



    private boolean checkValidation(){

        userId = signin_id_edittext.getText().toString().trim();
        pw =signin_pw_edittext.getText().toString().trim();
        pwCheck = signin_pwcheck_edittext.getText().toString().trim();
        userName = signin_nicname_edittext.getText().toString().trim();
        mobile = signin_mobile.getText().toString().trim();
        email = signin_email_edittext.getText().toString().trim();


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
        //Date (그날날짜) 받아와서 다시저장

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
    }


    public void goMain(){
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);

    }

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