package com.example.rotory.Search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;


import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


//import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.MainPage;
import com.example.rotory.R;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.VO.Tag;
import com.example.rotory.account.SignUpActivity;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class SearchPage extends Fragment {

    private static final String TAG = "SearchPage";
    private static final String REQUEST_CODE = "0000";

    RecyclerView searchTagList;
    MainPage mainPage;
    ThemePage themePage;

    EditText searchEdit;

    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    SignUpActivity signUpActivity;

    BottomNavigationView bottomNavigation;

    Boolean isSignIn = false;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    CollectionReference collectionReference;

    FirestoreRecyclerAdapter tagAdapter;
    //TagRecyclerAdapter adapter;

    public SearchPage() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.search_page, container, false);

        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저네임 : " + checkLogIN);
            isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
            isSignIn = false;
        }

        bottomNavUnderbarHome = rootView.findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = rootView.findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = rootView.findViewById(R.id.bottomNavUnderbarUser);

        mainPage = new MainPage();
        themePage = new ThemePage();


        searchEdit = rootView.findViewById(R.id.searchIdEdit);
        searchEdit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String searchText = searchEdit.getText().toString();

                    goToSearch(searchText);

                    return true;
                }

                return false;
            }
        });

        initUI(rootView);

        return rootView;
    }

    private void initUI(ViewGroup rootView) {
        searchTagList = rootView.findViewById(R.id.search1List);
        searchTagList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        searchTagList.setAdapter(tagAdapter);

    }

    private void goToSearch(String searchText) {
        Intent intent = new Intent(getActivity(), SearchResultPage.class);
        intent.putExtra("searchText", searchText);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (tagAdapter != null) {
            tagAdapter.stopListening();
        }
    }
}
