package com.example.rotory.account;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rotory.MainActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class RequestSms {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuthSettings firebaseAuthSettings =mAuth.getFirebaseAuthSettings();
    private final static String TAG = "FindId_RequestSms";
    Counter mobileCounter = new Counter();
    FindAccountActivity accountActivity = new FindAccountActivity();

    Context mContext;
    TextView countdownText;
    String authNum;

    public RequestSms(Context mContext, TextView countdownText) {
        this.mContext = mContext;
        this.countdownText = countdownText;
    }

    public PhoneAuthProvider.OnVerificationStateChangedCallbacks getCallback(String phoneNum) {
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNum,"123456");

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

        Log.d(TAG, "입력한 핸드폰 번호 " + phoneNum);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "인증코드 전송 성공");
                showToast("인증번호가 발송되었습니다. 60초 이내에 입력해주세요");
                authNum = phoneAuthCredential.getSmsCode();
                Log.d(TAG,"onVerificationCode에서 나오는  phoneAuthCredential" + authNum);
                setAuthNum(authNum);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "인증실패" + e.getMessage());
                showToast("인증실패");
            }

            @Override
            public void onCodeSent(@NonNull String authNum, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(authNum, forceResendingToken);

                Log.d(TAG, "onCodeSent의 authnum : forceResendingToken" + authNum + " : " + forceResendingToken);

                mobileCounter.countDownTimer(countdownText);

            }
        };

        return mCallbacks;
    }


    private void showToast(String s) {
        Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
    }

    public String getAuthNum() {
        return authNum;
    }

    public void setAuthNum(String authNum) {
        this.authNum = authNum;
    }
}
