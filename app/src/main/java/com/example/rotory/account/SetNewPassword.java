package com.example.rotory.account;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class SetNewPassword extends AppCompatActivity {
    private static final String TAG = "SetNewPassword";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_pw_new_pw_page);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null){
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.d(TAG, deepLink.toString());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "dynamikLink get failed");
            }
        });

        user = mAuth.getCurrentUser();
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("https://rotory2021.firebaseapp.com")
                .setHandleCodeInApp(true)
                .setAndroidPackageName("com.example.rotory",
                        true,
                        "12")
                //.setDynamicLinkDomain("https://rotory2021.firebaseapp.com")
                .build();
        mAuth.setLanguageCode("kr");
        mAuth.sendPasswordResetEmail("qhdo9778@naver.com",actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "EMAIL SENT...");
                        }
                    }
                });


      /*  user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "EMAIL sent?...");
                    }
                });*/
    }
}
