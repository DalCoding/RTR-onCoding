package com.example.rotory.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.MainActivity;
import com.example.rotory.MainPage;
import com.example.rotory.R;
import com.example.rotory.Theme.Tags;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.account.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class SearchPage extends AppCompatActivity {

    private static final String TAG = "SearchPage";
    private static final String REQUEST_CODE = "0000";

    RecyclerView searchTagList;

    EditText searchEdit;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    SearchTagAdapter adapter;

    ImageButton backBtn;
    ImageButton removeBtn;

    String tagText;

    ArrayList<Tags> tagList = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        Bundle bundle = new Bundle();
        ArrayList<String> tagList = bundle.getStringArrayList("tagList");
        Log.d(TAG, "tagList 확인" + tagList);

        db = FirebaseFirestore.getInstance();

        tagSet();

        searchTagList = findViewById(R.id.search1List);
        searchTagList.setLayoutManager(new GridLayoutManager(this, 3));
        backBtn = findViewById(R.id.searchBackBtn);
        removeBtn = findViewById(R.id.searchRemoveBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchPage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEdit.setText(null);
            }
        });


        searchEdit = findViewById(R.id.searchIdEdit);
        Intent intent = getIntent();
        tagText = intent.getStringExtra("tag");

        if (intent == null) {
            searchEdit.setText(null);
        } else if (intent != null) {
            searchEdit.setText(tagText);
        }

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        Toast.makeText(getApplicationContext(), "버튼을 눌러주세요", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        searchEdit.requestFocus();
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void tagSet(){
        db.collection("tag")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            ArrayList<String> keyValue = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                Map<String, Object> document =documentSnapshot.getData();
                                Log.d(TAG, document.toString());
                                for (String key : document.keySet()) {
                                    String value = String.valueOf(document.get(key));
                                    keyValue.add(value);
                                }
                            }
                            Log.d(TAG,"태그 받아왔는지 확인" + keyValue);
                            tagList = new ArrayList<>();
                            for (int i = 0 ; i < keyValue.size(); i++){
                                tagList.add(new Tags(keyValue.get(i)));
                            }

                            adapter = new SearchTagAdapter(tagList, getApplicationContext());
                            searchTagList = findViewById(R.id.search1List);
                            searchTagList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                            searchTagList.setAdapter(adapter);

                            searchEdit.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    adapter.getFilter().filter(charSequence);
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    searchTagList.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                });
    }
}
