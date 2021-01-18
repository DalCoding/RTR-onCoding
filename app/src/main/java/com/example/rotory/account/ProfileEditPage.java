package com.example.rotory.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.rotory.MyPage;
import com.example.rotory.R;
import com.example.rotory.VO.AppConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.example.rotory.R.id.pageTitlewithBtnTextView;

public class ProfileEditPage extends Fragment {
    private static final String TAG = "ProfileEditPage";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    AppConstant appConstant;

    EditText profileNick;
    EditText profilePwd;
    EditText profilePwdCheck;
    EditText profileEmail;
    EditText profileMobile;

    TextView profileTiltleText;
    TextView profilePwdCheckNoti;
    TextView profileNickCheck;

    Button profilecheckBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.profile_edit_page, container,false);
        initUi(rootView);
        return rootView;
    }

    private void initUi(ViewGroup rootView) {
        Bundle pDocumentBundle = getArguments();
        String pDocumentId = pDocumentBundle.getString("pDocumentId");
        Log.d(TAG, "initUi 시작, 번들 전송 잘됐는지 확인, pDocumentId :" + pDocumentId);

        profileNick = rootView.findViewById(R.id.profileNickEditText);
        profileEmail = rootView.findViewById(R.id.profileEmailEditText);
        profileMobile = rootView.findViewById(R.id.profilePhoneEditText);
        profilePwd = rootView.findViewById(R.id.profilePwdEditText);
        profilePwdCheck = rootView.findViewById(R.id.profilePwdEditText1);
        profilePwdCheckNoti = rootView.findViewById(R.id.profilePwdTextView);
        profileNickCheck = rootView.findViewById(R.id.profileNickCheck);

        profileTiltleText = rootView.findViewById(R.id.pageTitlewithBtnTextView);
        profileTiltleText.setText("프로필 설정");

        db.collection("person").document(pDocumentId).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot userDocument = task.getResult();
                    Log.d(TAG, "initUI 필요한 정보 받아왔는지 확인 :" + userDocument);
                    setUserInfo(userDocument,rootView);
                    }
                });

        profilecheckBtn = rootView.findViewById(R.id.profilecheckBtn);
        profilecheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePwdCheckNoti.setVisibility(View.GONE);
                profileNickCheck.setVisibility(View.GONE);
                if (checkValidation())
                    modifiedUserData(pDocumentId);
            }
        });
    }

    private boolean checkValidation() {
        String newUserName = profileNick.getText().toString();
        String newMobile = profileMobile.getText().toString();
        String newPw = profilePwd.getText().toString();
        String newPwCheck = profilePwdCheck.getText().toString();
        boolean mobilePattern = Pattern.matches(appConstant.REGEX_NUMBER, newMobile);
        boolean pwPattern = Pattern.matches(appConstant.REGEX_PATTERN, newPw);

        if (!pwPattern) {
           profilePwdCheckNoti.setText("비밀번호를 확인해주세요");
           profilePwdCheckNoti.setVisibility(View.VISIBLE);
            return false;
        } else if (!newPwCheck.equals(newPw)) {
            profilePwdCheckNoti.setText("비밀번호 불일치");
            profilePwdCheckNoti.setVisibility(View.VISIBLE);
            return false;
        } else if (newUserName.equals("") || newUserName.length() < 1) {
            profileNickCheck.setText("닉네임을 입력해 주세요");
            profileNickCheck.setVisibility(View.VISIBLE);
            return false;
        } else if (!mobilePattern) {
            return false;
        }
        return true;
    }

    private void modifiedUserData(String pDocumentId) {
        Map<String, Object> modifiedData = new HashMap<>();
        modifiedData.put("userName", profileNick.getText().toString());
        modifiedData.put("email", profileNick.getText().toString());
        modifiedData.put("password",profilePwd.getText().toString());
        modifiedData.put("mobile", profileMobile.getText().toString());

        db.collection("person").document(pDocumentId).set(modifiedData, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG,"modifiedUserData : 확인 " + modifiedData.get("userName")
                                + "/n 해당 다큐먼트 아이디 : " + pDocumentId);

                        Toast.makeText(getContext(),"사용자 정보 변경 완료", Toast.LENGTH_SHORT).show();
                        ((MyPage)getActivity()).closeProfileEditor("profileEdit");


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"사용자 정보 변경 실패");
            }
        });
    }

    private void setUserInfo(DocumentSnapshot userDocument, ViewGroup rootView) {
        profileNick.setText(userDocument.get("userName").toString());
        profileEmail.setText(userDocument.get("email").toString());
        profileMobile.setText(userDocument.get("mobile").toString());

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

}
