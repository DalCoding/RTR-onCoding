package com.example.rotory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rotory.VO.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class SignupPage extends Fragment {
    MainActivity mainActivity;
    Context context;
    private FirebaseAuth mAuth;
    private static final String TAG = "SingUpPage";

    EditText signin_id_edittext;
    EditText signin_pw_edittext;
    EditText signin_pwcheck_edittext;
    EditText signin_nicname_edittext;
    EditText signin_number;
    EditText signin_email_edittext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sign_up_page, container, false);
        initUI(rootView);
        return  rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        context = this.context;

    }


    @Override
    public void onDetach() {
        super.onDetach();
        if(context != null){
            context = null;
        }
    }

    private void initUI(ViewGroup rootView) {
        mainActivity.pageTitleTextView.setVisibility(View.GONE);
        mainActivity.withBackBtnContainer.setVisibility(View.VISIBLE);
        mainActivity.pageTitlewithBtnTextView.setText("회원가입");
        mainActivity.backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogInActivity.class);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        signin_id_edittext = rootView.findViewById(R.id.signin_id_edittext);
        signin_pw_edittext = rootView.findViewById(R.id.signin_pw_edittext);
        signin_pwcheck_edittext = rootView.findViewById(R.id.signin_pwcheck_edittext);
        signin_email_edittext = rootView.findViewById(R.id.signin_email_edittext);
        signin_nicname_edittext = rootView.findViewById(R.id.signin_nicname_edittext);
        signin_number = rootView.findViewById(R.id.signin_number);

        mainActivity.checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userId= signin_id_edittext.getText().toString().trim();
                String pw = signin_pw_edittext.getText().toString().trim();
                String pwCheck = signin_pwcheck_edittext.getText().toString().trim();

                if(pw.equals(pwCheck)){
                    Log.d(TAG, "등록버튼" + userId + ", " + pw);

                    mAuth.createUserWithEmailAndPassword(userId, pw)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userId = user.getEmail();
                                String uid = user.getUid();
                                String name = signin_nicname_edittext.getText().toString().trim();
                                String email = signin_email_edittext.getText().toString();
                                String mobile= signin_number.getText().toString();


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
            }
        });





    }
}
