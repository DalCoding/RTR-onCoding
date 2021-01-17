<<<<<<< HEAD
package com.example.rotory.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
=======
package com.example.rotory.Account;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.MainActivity;
import com.example.rotory.R;
<<<<<<< HEAD

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

public class LogInActivity extends AppCompatActivity  {
    private final int RC_SIGN_IN = 3000;
    private static final String TAG = "LoginAcitivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

=======
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity  extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
    private EditText login_id_edittext;
    private EditText login_pw_edittext;
    private Button login_button;
    private TextView login_join;
<<<<<<< HEAD

    CheckBox login_auto;
    String logInId;
    String logInPw;


=======
    String logInId;
    String logInPw;

>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

<<<<<<< HEAD
        login_id_edittext = findViewById(R.id.login_id_edittext);
        login_pw_edittext = findViewById(R.id.login_pw_edittext);
=======
        firebaseAuth = FirebaseAuth.getInstance();
        login_id_edittext = findViewById(R.id.login_id_edittext);
        login_pw_edittext = findViewById(R.id.login_pw_edittext);

>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
        login_join = findViewById(R.id.login_join);
        login_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
               createAccount();
=======
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
            }
        });

        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                logInUser(login_id_edittext.getText().toString(),
                        login_pw_edittext.getText().toString());
            }
        });


       
        firebaseAuth = FirebaseAuth.getInstance();
        
        
        
        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
=======
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
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
<<<<<<< HEAD
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
*/                 moveMainPage();

                }else {
                    Log.d(TAG,"AuthStateChangeListener, 유저 불러오기 실패");
=======
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);

                    startActivity(intent);
                    finish();
                }else {

>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
                }
            }
        };
    }
<<<<<<< HEAD


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


=======
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
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
<<<<<<< HEAD

                        if (!task.isSuccessful()){
                            Toast.makeText(LogInActivity.this, "로그인 요청 실패", Toast.LENGTH_SHORT).show();
                        }
=======
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
                    }
                });
    }

<<<<<<< HEAD
    private void createAccount() {
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);

    }
    private  void moveMainPage(){
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
    }
=======
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9


    public boolean checkValidation(String id, String pw){
        if(id.equals("") || !id.contains("@")|| pw.equals("")){
            return true;
        }
        return false;
    }
<<<<<<< HEAD


=======
>>>>>>> 1cabbe21ada2c288a0ae57f0e38b9b6dfe7394e9
}
