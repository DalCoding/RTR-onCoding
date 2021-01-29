package com.example.rotory.Search;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class setTagList extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    final String TAG = "setTagList";

    public setTagList() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("TagList", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        tagSet(editor);
    }

    public void tagSet(SharedPreferences.Editor editer){
        db.collection("tag").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                           Set<String> keyValue = new HashSet<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                Map<String, Object> document =documentSnapshot.getData();
                                Log.d(TAG, document.toString());
                                for (String key : document.keySet()){
                                    String value = String.valueOf(document.get(key));
                                    keyValue.add(value);
                                }
                            }
                            Log.d(TAG,"태그 받아왔는지 확인" + keyValue);
                            editer.putStringSet("tagList", keyValue);
                        }
                    }
                });
    }
}
