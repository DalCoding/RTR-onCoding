package com.example.rotory;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.VO.AppConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AskPage extends AppCompatActivity {
    AppConstant appConstant = new AppConstant();

    private static final String TAG = "AskPage";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Button askBackBtn;
    EditText askEditText;
    Button askSendBtn;
    TextView askCountTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.askpage);
        FirebaseUser user = mAuth.getCurrentUser();


        askBackBtn = findViewById(R.id.askBackBtn);
        askBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askEditText = findViewById(R.id.askEditText);
                String askContents = askEditText.getText().toString();
                if (askContents.length() == 0) {
                    finish();
                } else {
                    twoBtnDialog();
                }

            }

        });

        askEditText = findViewById(R.id.askEditText);
        askEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = askEditText.getText().toString();
                askCountTextView = findViewById(R.id.askCountTextView);
                askCountTextView.setText("("+input.length()+"/500자)");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        askSendBtn = findViewById(R.id.askSendBtn);
        askSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAsk();
            }
        });
    }

    public void oneBtnDialog(){}
    public void twoBtnDialog(){
        AlertDialog.Builder AskBack = new AlertDialog.Builder(this);
        AskBack.setTitle("해당 페이지를 벗어나시겠습니까?");

        AskBack.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        AskBack.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AskBack.show();
        }

    public void sendAsk(){

        AlertDialog.Builder AskBack = new AlertDialog.Builder(this);
        AskBack.setTitle("문의를 남기시겠습니까?");

        AskBack.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseUser user = mAuth.getCurrentUser();
                String userEmail = user.getEmail();

                askEditText = findViewById(R.id.askEditText);
                String askContents = askEditText.getText().toString();

                Date currentDate = new Date();
                String asked_date = appConstant.dateFormat.format(currentDate);

                Map<String, Object> AskData = new HashMap<>();
                AskData.put("userId", userEmail);
                AskData.put("askContents", askContents);
                AskData.put("checked", "0");
                AskData.put("checked_date", "");
                AskData.put("asked_date", asked_date);

                db.collection("ask").add(AskData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Toast.makeText(getApplicationContext(), "문의를 남겼습니다.", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });




                dialog.dismiss();
                finish();
            }
        });

        AskBack.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AskBack.show();
    }
}


//해당 페이지를 벗어나시겠습니까? 예, 아니오 => null이면 그냥 이동   O
//문의를 남기시겠습니까? 예, 아니오 O