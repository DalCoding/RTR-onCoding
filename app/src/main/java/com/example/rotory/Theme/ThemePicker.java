package com.example.rotory.Theme;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.VO.Tag;
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

    OnTagItemClickListener listener;

    void getTagList(String tagList, RecyclerView recyclerView, Context context, TextView  tagListSize) {
        themePickPage = new ThemePickPage();

        GridLayoutManager tagLayoutManager = new GridLayoutManager(context, 4);
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
                    tagItemAdapter = new TagItemAdapter(context,tagsItemList,listener, tagListSize);
                    recyclerView.setAdapter(tagItemAdapter);
                    Log.d(TAG,"리스너 작동 전");

                   /* tagItemAdapter.setOnTagItemClickListener(new OnTagItemClickListener() {
                        @Override
                        public void onItemClick(TagItemAdapter.tagItemViewHolder holder, View view, int position) {
                            Log.d(TAG,"리스너 확인");
                            Tags item = tagItemAdapter.getItem(position);
                            Log.d(TAG,"아이템 글귀부터 확인" + item.getTag().toLowerCase());
                            Toast.makeText(context, "아이템 선택 : " + item.getTag(),Toast.LENGTH_SHORT).show();
                        }
                    });*/


                    Log.d(TAG,"list 확인" + tagItemList);

                }
            }
        });


    }



}
