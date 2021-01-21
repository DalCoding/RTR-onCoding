package com.example.rotory.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.MainActivity;
import com.example.rotory.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity  {
    private final int RC_SIGN_IN = 3000;
    private static final String TAG = "LoginAcitivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private EditText login_id_edittext;
    private EditText login_pw_edittext;
    private Button login_button;
    private TextView login_join;
    TextView login_find;

    CheckBox login_auto;
    String logInId;
    String logInPw;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        login_id_edittext = findViewById(R.id.login_id_edittext);
        login_pw_edittext = findViewById(R.id.login_pw_edittext);
        login_find = findViewById(R.id.login_find);
        login_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movePage(FindAccountActivity.class);
            }
        });
        login_join = findViewById(R.id.login_join);
        login_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               movePage(SignUpActivity.class);
            }
        });

        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInUser(login_id_edittext.getText().toString(),
                        login_pw_edittext.getText().toString());
            }
        });


       
        firebaseAuth = FirebaseAuth.getInstance();
        
        
        
        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logInId = login_id_edittext.getText().toString();
               logInPw = login_pw_edittext.getText().toString();
               if (checkValidation(logInId, logInPw)){
                   Toast.makeText(LogInActivity.this, "계정과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
               }else {
                   logInUser(logInId, logInPw);
               }

            }
        });

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    /*user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                intent.putExtra("token", idToken);
                                startActivityForResult(intent, RC_SIGN_IN);
                            } else {
                                    Log.d(TAG, "Fail to get Token");
                            }
                        }
                    });
*/               movePage(MainActivity.class);

                }else {
                    Log.d(TAG,"AuthStateChangeListener, 유저 불러오기 실패");
                }
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null){

        }
    }


    public void logInUser(String userId, String password){
        firebaseAuth.signInWithEmailAndPassword(userId, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LogInActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            firebaseAuth.addAuthStateListener(firebaseAuthListener);

                        } else{
                            Toast.makeText(LogInActivity.this, "아이디 혹은 비밀 번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        }

                        if (!task.isSuccessful()){
                            Toast.makeText(LogInActivity.this, "로그인 요청 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createAccount() {
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);

    }
    private  void movePage(Class className){
        Intent intent = new Intent(LogInActivity.this, className);
        startActivity(intent);
    }


    public boolean checkValidation(String id, String pw){
        if(id.equals("") || !id.contains("@")|| pw.equals("")){
            return true;
        }
        return false;
    }

    public FirebaseUser LogInWithAccount(FirebaseAuth mAuth, FirebaseUser user,String emailUrl, String userId) {

        if (mAuth.isSignInWithEmailLink(emailUrl)){
            mAuth.signInWithEmailLink(userId, emailUrl).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG,"받아온 계정으로 로그인 성공");
                }
            });
        }else{
            Log.d(TAG,"로그인 실패");
        }
        if (user != null) {
            user = mAuth.getCurrentUser();
            Log.d(TAG,"사용자 받아옴 " + user.getEmail());

        }else{
            Log.d(TAG,"사용자 여전히 null");
        }
        return user;

    }
}
