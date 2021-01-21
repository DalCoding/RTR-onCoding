package com.example.rotory.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.example.rotory.MyPage;
import com.example.rotory.R;
import com.example.rotory.VO.AppConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class SetNewPassword extends AppCompatActivity {
    AppConstant appConstant;
    LogInActivity logInActivity = new LogInActivity();
    private static final String TAG = "SetNewPassword";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    SharedPreferences sharedPreferences;

    EditText findPwNewPw;
    EditText findPwNewPwCheck;

    TextView pwCheckMsg;

    Button newPwSubmitBtn;

    Context mContext;
    Intent intent;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_pw_new_pw_page);
        mContext = this;
        String emailUrl;
        sharedPreferences = getSharedPreferences("FindAccountUserId", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

             intent =getIntent();

             if (intent.getData() != null) {
                 emailUrl = intent.getData().toString();
                 Log.d(TAG, "intent에 들어있는 url확인!" + emailUrl);
                 Log.d(TAG, "유알엘 정보 잘 받아왔나요?" + emailUrl);
                 Log.d(TAG, "아이디 정보 잘 받아왔나요?" + userId);

                 logInActivity.LogInWithAccount(mAuth, user, emailUrl, userId);
             }


             Log.d(TAG, "유저존재여부 확인" + user);

        findPwNewPw = findViewById(R.id.findpw_check_edittext1);
        findPwNewPwCheck = findViewById(R.id.findpw_check_edittext2);

        pwCheckMsg = findViewById(R.id.findpw_check_check);
/*
        String newPw = findPwNewPw.getText().toString();
        String newPwCheck = findPwNewPwCheck.getText().toString();*/

        newPwSubmitBtn = findViewById(R.id.findpw_check_button);
        newPwSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, findPwNewPw.getText().toString() + "  => " + findPwNewPwCheck.getText().toString());
                if (checkValidation(findPwNewPw.getText().toString(),findPwNewPwCheck.getText().toString())){
                    reNewPasswordInfo(userId,findPwNewPw.getText().toString());


                }
            }
        });

    }

    private void reNewPasswordInfo(String userID, String password) {
        Log.d(TAG,userID+"자료 받아오기");
      db.collection("person").whereEqualTo("userId",userID)
              .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
             for (QueryDocumentSnapshot pDocument: task.getResult()){
                 String pDocumentID = pDocument.getId();
                 Map<String, Object> reNewPw = new HashMap<>();
                 reNewPw.put("password", password);
                 db.collection("person").document(pDocumentID)
                         .set(reNewPw, SetOptions.merge())
                         .addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 Log.d(TAG,"비밀번호 변경 성공" +password+"=>"+reNewPw.get("password"));
                                 user = mAuth.getCurrentUser();
                                 user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         if (task.isSuccessful()){
                                             Log.d(TAG,"비밀번호 업데이트 끝" + password);

                                             showToast("비밀번호가 변경되었습니다.");

                                             Intent mainIntent = new Intent(SetNewPassword.this, MyPage.class);
                                             mainIntent.addFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                             startActivity(mainIntent);

                                         }
                                     }
                                 });
                             }
                         }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Log.d(TAG, "파일정보 업로딩 실패");
                        showToast("정보수정에 실패하였습니다. 잠시후 다시 시도해주세요.");
                     }
                 });

             }

          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              showToast("계정정보를 받아올 수 없습니다. 인증여부를 확인해 주세요");
          }
      });

    }


    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), s,Toast.LENGTH_SHORT).show();
    }

    private boolean checkValidation(String newPw, String newPwCheck) {

        boolean pwPattern = Pattern.matches(AppConstant.REGEX_PATTERN, newPw);
        pwCheckMsg.setVisibility(View.INVISIBLE);
        if (!pwPattern) {
            pwCheckMsg.setText("비밀번호 양식을 확인해주세요");
            Log.d(TAG,"비밀번호 양식확인 - 비밀번호? " + newPw + "양식확인" + AppConstant.REGEX_PATTERN);
            pwCheckMsg.setVisibility(View.VISIBLE);
            return false;
        } else if (!newPwCheck.equals(newPw)) {
            pwCheckMsg.setText("비밀번호 불일치");
            pwCheckMsg.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }
}
