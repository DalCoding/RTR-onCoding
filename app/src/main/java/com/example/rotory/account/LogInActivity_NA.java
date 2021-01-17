package com.example.rotory.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.MainActivity;
import com.example.rotory.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity_NA extends AppCompatActivity implements View.OnClickListener {
    private final int RC_SIGN_IN = 3000;
    private static final String TAG = "LoginAcitivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    //private String mCustomToken;
    private TokenReceiver tokenReceiver;

    private EditText login_id_edittext;
    private EditText login_pw_edittext;
    private Button login_button;
    private TextView login_join;
    String logInId;
    String logInPw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        tokenReceiver = new TokenReceiver() {
            @Override
            public void onNewToken(String token) {
                Log.d(TAG, "onNewToken: " + token);
               // setCustomToken(token);
            }
        };

        firebaseAuth = FirebaseAuth.getInstance();
        login_id_edittext = findViewById(R.id.login_id_edittext);
        login_pw_edittext = findViewById(R.id.login_pw_edittext);

        login_join = findViewById(R.id.login_join);
        login_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity_NA.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logInId = login_id_edittext.getText().toString();
               logInPw = login_pw_edittext.getText().toString();
               if (checkValidation(logInId, logInPw)){
                   Toast.makeText(LogInActivity_NA.this, "계정과 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
               }else {
                   //logInUser(logInId, logInPw);
               }

            }
        });

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){

                    Intent intent = new Intent(LogInActivity_NA.this, MainActivity.class);

                    startActivityForResult(intent, 3000);
                    finish();
                }else {

                }
            }
        };
    }

    private void startLogIn() {
    }
    /*private void setCustomToken(String token) {

        mCustomToken = token;
        String status;
        if(mCustomToken != null){
            status = "Token:" + mCustomToken;
        }else {
            status = "Token: null";
        }
    }

    public void logInUser(String userId, String password){
        firebaseAuth.signInWithCustomToken(mCustomToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LogInActivity_NA.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);

                        } else{
                            Toast.makeText(LogInActivity_NA.this, "아이디 혹은 비밀 번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
*/
    private void updateUI(FirebaseUser user) {
        if(user != null){

        }
    }


    public boolean checkValidation(String id, String pw){
        if(id.equals("") || !id.contains("@")|| pw.equals("")){
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.login_button){
            startLogIn();
        }
    }


}
