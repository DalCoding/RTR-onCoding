package com.example.rotory;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "SingUpActivity";

    EditText signin_id_edittext;
    EditText signin_pw_edittext;
    EditText signin_pwcheck_edittext;
    EditText signin_nicname_edittext;
    EditText signin_mobile;
    EditText signin_email_edittext;

    ImageButton signUpBackImageButton;
    Button signUpCheckBtn;
    TextView signUpTitlewithBtnTextView;

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
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_main, null);

                BottomNavigationView bottomNavigation;
                bottomNavigation = view.findViewById(R.id.bottom_appBar);
                bottomNavigation.setVisibility(View.VISIBLE);

                finish();

            }
        });
        mAuth = FirebaseAuth.getInstance();
        String userId = signin_id_edittext.getText().toString().trim();
        String pw =signin_pw_edittext.getText().toString().trim();
        String pwCheck = signin_pwcheck_edittext.getText().toString().trim();
        String userName = signin_nicname_edittext.getText().toString().trim();
        String mobile = signin_mobile.getText().toString().trim();
        String email = signin_email_edittext.getText().toString().trim();

      signUpCheckBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Log.d(TAG, "등록버튼" + userId + ", " + pw);

              mAuth.createUserWithEmailAndPassword(userId, pw)
                      .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                          @Override
                          public void onComplete(@NonNull Task<AuthResult> task) {
                              if(task.isSuccessful()){
                                  FirebaseUser user = mAuth.getCurrentUser();
                                  String userId = user.getEmail();
                                  String uid = user.getUid();

                                  HashMap<Object, String> person = new HashMap<>();
                                  person.put("userEmail", email);
                                  person.put("email", userId);
                                  person.put("name", userName);
                                  person.put("uid", uid);
                                  person.put("mobile", mobile);

                                  FirebaseFirestore db = FirebaseFirestore.getInstance();
                                  db.collection("person")
                                          .add(person)
                                          .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                              @Override
                                              public void onSuccess(DocumentReference documentReference) {
                                                  Log.d(TAG, "Person add with id" + documentReference.getId());

                                                  Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                  startActivity(mainIntent);
                                              }
                                          })
                                          .addOnFailureListener(new OnFailureListener() {
                                              @Override
                                              public void onFailure(@NonNull Exception e) {
                                                  Log.w(TAG, "Error adding user", e);
                                              }
                                          });

                              }


                          }
                      });

          }
      });

    }
    Boolean checkUserIDRule(String userId){
        TextView signin_id_check = findViewById(R.id.signin_id_check);
        signin_id_check.setText("이메일 형식의 아이디를 사용하세요");
        if(userId.matches(".*@.*") && userId.length()>5){
            /*if(userId.matches(".*[A-Z].*") ||
                    userId.matches(".*[a-z].*") || userId.matches(".*[0-9].*")) */{
                signin_id_check.setVisibility(View.GONE);
                return true;
            }
        }else {

            signin_id_check.setVisibility(View.VISIBLE);
        }
        return false;
    }

    Boolean checkPwRule(String pw){
        if (pw.length() >= 8){
            if(pw.matches(".*[0-9].*")){
                            return true;

            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void saveUserAccount(String userId, String pw, String pwCheck, String email, String name, String mobile) {

            //if(checkPwRule(pw)){
                Log.d(TAG, "등록버튼" + userId + ", " + pw);

                mAuth.createUserWithEmailAndPassword(userId, pw)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String userId = user.getEmail();
                                    String uid = user.getUid();

                                    HashMap<Object, String> person = new HashMap<>();
                                    person.put("userEmail", email);
                                    person.put("email", userId);
                                    person.put("name", name);
                                    person.put("uid", uid);
                                    person.put("mobile", mobile);

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("person")
                                            .add(person)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d(TAG, "Person add with id" + documentReference.getId());

                                                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(mainIntent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding user", e);
                                                }
                                            });

                                }


                            }
                        });
            /*}else{
                TextView signIn_pwCheck_check = findViewById(R.id.signin_pwcheck_check);
                signIn_pwCheck_check.setText("비밀번호 양식 확인");
                signIn_pwCheck_check.setVisibility(View.VISIBLE);

            }*/
        }

}
