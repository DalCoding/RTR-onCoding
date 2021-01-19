package com.example.rotory.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.LoadStoryItem;
import com.example.rotory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


public class FindIdActivity extends AppCompatActivity {
    private final static String TAG = "FindIdActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    EditText mobile;
    Button checkMobileButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id_page);

        mobile = findViewById(R.id.findid_phone_edittext);
        checkMobileButton = findViewById(R.id.findid_phonebutton);

        checkMobileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("person").document("mobileList")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    Map<String, Object> checkMobileList = task.getResult().getData();
                                    Log.d(TAG,"mobile 변수 체크" +mobile.getText());
                                    if (mobile.getText().equals("") || mobile.getText() == null){
                                        Toast.makeText(getApplicationContext(),"번호를 입력해 주세요", Toast.LENGTH_SHORT);
                                    }else {
                                        Log.d(TAG, "회원가입된 번호 존재" + mobile.getText());
                                        if (checkMobileList.containsKey(mobile.getText())) {
                                            Log.d(TAG, "회원가입된 번호 존재" + mobile.getText());
                                            getIdDialog(checkMobileList.get(mobile).toString());
                                        }
                                    }
                                }
                            }
                        });
            }
        });



    }

    private void getIdDialog(String userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("회원님의 계정은 " + userId + "입니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNeutralButton("비밀번호 찾기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(FindIdActivity.this, FindPWActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

}
