package com.example.rotory.Theme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ThemePicker {
    private final static String TAG = "ThemePicker";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    ThemePickPage themePickPage;
    TagItemAdapter tagItemAdapter;

    void getTagList(String tagList, RecyclerView recyclerView, Context context) {
        themePickPage = new ThemePickPage();

        GridLayoutManager tagLayoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(tagLayoutManager);


        db.collection("tag").document(tagList)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Map<String, Object> tagItemsMap = new HashMap<>();
                    tagItemsMap = task.getResult().getData();
                    Collection<Object> tagItemCollection =tagItemsMap.values();
                    ArrayList<Object> tagItemList = new ArrayList<>(tagItemCollection);
                    ArrayList<Tags> tagsItemList = new ArrayList<>();
                    for (int i = 0 ; i < tagItemList.size(); i++){
                        tagsItemList.add(new Tags(tagItemList.get(i).toString()));
                    }

                    Log.d(TAG,"태그 아이템 리스트 뽑은거 확인" + tagsItemList);
                    tagItemAdapter = new TagItemAdapter(context,tagsItemList);
                    recyclerView.setAdapter(tagItemAdapter);

                    Log.d(TAG,"list 확인" + tagItemList);

                }
            }
        });
    }

}
