package com.example.rotory.signup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.Interface.DtrDialogListener;
import com.example.rotory.MainActivity;
import com.example.rotory.R;
import com.example.rotory.VO.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.util.Util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements DtrDialogListener {
    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";
    private final String REGEX = "0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|" +
            "가-힣|@\\-\\_\\.\\;\\·\u318D\u119E\u11A2\u2022\u2025a\u00B7\uFE55]*";


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

    String userId;
    String pw ;
    String pwCheck;
    String userName;
    String mobile;
    String email;

    Person persons = new Person();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        signUpTitlewithBtnTextView.setText("회원가입");
        signUpBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                goMain();
               /* LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_main, null);
                BottomNavigationView bottomNavigation;
                bottomNavigation = view.findViewById(R.id.bottom_appBar);
                bottomNavigation.setVisibility(View.VISIBLE);*/

                finish();

            }
        });
        mAuth = FirebaseAuth.getInstance();

        userId = signin_id_edittext.getText().toString().trim();
        pw =signin_pw_edittext.getText().toString().trim();
        pwCheck = signin_pwcheck_edittext.getText().toString().trim();
        userName = signin_nicname_edittext.getText().toString().trim();
        mobile = signin_mobile.getText().toString().trim();
        email = signin_email_edittext.getText().toString().trim();

        signUpCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidation()) {
                    setNewAccount(persons);
                    signUp(userId, pw, persons);
                }
            }
        });
    }

    private final void signUp(final String userId, final String password,
                              Person persons){
        FirebaseAuth firebaseAuth = this.mAuth;
        if (firebaseAuth == null){
            Log.e(TAG,"firebaseauth 연결 안됨");
        }
        FirebaseUser user = mAuth.getCurrentUser();
        final ProgressDialog pDialog= new ProgressDialog(SignUpActivity.this);
        pDialog.setMessage("가입중...");

        mAuth.createUserWithEmailAndPassword(userId, password).addOnCompleteListener(
                SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "등록버튼" + userId + ", " + password);
                            pDialog.show();
                            String userId = user.getEmail();
                            String uid = user.getUid();
                            saveUserAccount(userId, uid, persons);
                            pDialog.dismiss();


                        }else {
                          Toast.makeText(getApplicationContext(),"회원가입 실패", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "signUp Failed : " + userId + ", " + password + "," + user.getUid());
                            return;
                        }
                    }
                });
    }

    private void saveUserAccount(String userId, String uid, Person persons) {

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
        person.put("signUpDate", "2021-01-12");

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
    private boolean checkValidation(){
        userId = signin_id_edittext.getText().toString().trim();
        pw =signin_pw_edittext.getText().toString().trim();
        pwCheck = signin_pwcheck_edittext.getText().toString().trim();
        userName = signin_nicname_edittext.getText().toString().trim();
        mobile = signin_mobile.getText().toString().trim();
        email = signin_email_edittext.getText().toString().trim();

        signin_id_check = findViewById(R.id.signin_id_check);
        signin_pwcheck_check = findViewById(R.id. signin_pwcheck_check);

        if(userId.equals("") || userId.length() < 4 ||
                userName.equals("")|| userName.length()==0 ||
                mobile.equals("") || mobile.length() <= 9 ||
                pw.equals("") || pw.length()<6 ||
                pwCheck.equals("") || pwCheck.length() <6){

            signin_id_check.setText("이메일 양식으로 작성해주세요");
            signin_id_check.setVisibility(View.VISIBLE);
            signin_pwcheck_check.setVisibility(View.VISIBLE);

            return false;
        } else if(!pw.matches(REGEX)){
           
            return false;

        } else if(!pwCheck.equals(pw)){
            signin_pwcheck_check.setVisibility(View.VISIBLE);

            return false;
        }else if(!userId.contains("@")){
            signin_id_check.setText("이메일 양식으로 작성해주세요");
            signin_id_check.setVisibility(View.VISIBLE);

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
    return persons;
    }

    @Override
    public void oneBtnDialog() {

    }

    @Override
    public void twoBtnDialog() {

    }
}

