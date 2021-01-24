package com.example.rotory.Theme;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.R;
import com.example.rotory.Search.TagRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TagItemAdapter extends RecyclerView.Adapter<TagItemAdapter.tagItemViewHolder>
        implements OnTagItemClickListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    private final static String TAG = "TagItemAdapter";
    public Context context;
    public ArrayList<Tags> tagItemList;
    OnTagItemClickListener listener;
    TextView  tagListSize;
    View view;

    public TagItemAdapter(Context context, ArrayList<Tags> tagItemList,
                          OnTagItemClickListener listener, TextView tagListSize) {
        this.context = context;
        this.tagItemList = tagItemList;
        this.tagListSize = tagListSize;

    }

    @NonNull
    @Override
    public tagItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_pick_item, parent,false);
        return new tagItemViewHolder(view/*,this::onItemClick*/);
    }

    @Override
    public void onBindViewHolder(@NonNull tagItemViewHolder holder, int position) {
        holder.setTagItems(tagItemList.get(position));
        holder.onBind();
    }

    @Override
    public int getItemCount() {
        return tagItemList.size();
    }

    public class tagItemViewHolder extends  RecyclerView.ViewHolder {
        View view;
        RecyclerView recyclerView;
        TextView tagBtn;
        Map<String, Object> tagList = new HashMap<String, Object>(5);
        int isSelected = 0;



        public tagItemViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.activityTagRecyclerView);
            view= itemView;
            tagBtn = itemView.findViewById(R.id.tagText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =getAdapterPosition();
                    if (listener != null){
                        listener.onItemClick(tagItemViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setTagItems(Tags item){
            tagBtn.setText(item.getTag());
            isInList(item.getTag());

        }

        private void isInList(String tag) {
            db.collection("person").whereEqualTo("userId", user.getEmail())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot pDocument : task.getResult()) {
                            String pDocumentId = pDocument.getId();
                            db.collection("person").document(pDocumentId)
                                    .collection("myTag")
                                    .document("myTagList")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task){
                                            Map<String, Object> myTagList = new HashMap<>();
                                            myTagList = task.getResult().getData();
                                            if (myTagList.containsKey(tag)) {
                                                tagBtn.setTextColor(Color.RED);
                                                tagBtn.setTextSize(16);
                                            } else {
                                                tagBtn.setTextColor(Color.BLACK);
                                            }
                                        }
                                    });
                        }
                    }
                }
            });
        }

        public void onBind(){
            String tagText = tagBtn.getText().toString();

            tagBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        /*    if (isSelected >= 5) {
                                Toast.makeText(context, "선택가능한 태그 개수는 5개 입니다.", Toast.LENGTH_SHORT).show();
                            } else {*/
                    if (tagListSize.getText().toString().equals("5")) {
                            if (tagBtn.getCurrentTextColor() == Color.RED) {
                                removeFromList();
                                setTagDb(tagText, false);
                            } else {
                                Toast.makeText(context, "선택가능한 태그 개수는 5개 입니다.", Toast.LENGTH_SHORT).show();
                            }
                    }else {
                                if (tagBtn.getCurrentTextColor() == Color.RED) {
                                    removeFromList();
                                    setTagDb(tagText, false);
                                }
                                else if (tagBtn.getCurrentTextColor() == Color.BLACK) {
                                    tagBtn.setTextColor(Color.RED);
                                    tagBtn.setTextSize(16);
                                    Log.d(TAG, tagBtn.getText().toString() + " 선택됨");
                                    Toast.makeText(context, "태그 선택 : " + tagBtn.getText().toString(), Toast.LENGTH_SHORT).show();
                                    setTagDb(tagText, true);

                                    int tagSize = Integer.parseInt(tagListSize.getText().toString());
                                    String chantedSize = String.valueOf(tagSize + 1);
                                    tagListSize.setText(chantedSize);
                                    Log.d(TAG, "선택 개수 변화 확인" + tagSize + " =>" + tagListSize.getText().toString());
                                }
                            }
                        }

                });
        }

        private void removeFromList() {
            String tagText = tagBtn.getText().toString();
            tagBtn.setTextColor(Color.BLACK);
            tagBtn.setTextSize(14);
            Log.d(TAG, tagText + " 선택 취소됨" + tagList.size() + "/5");
            Log.d(TAG, "선택 개수 확인" + isSelected);
            int tagSize = Integer.parseInt(tagListSize.getText().toString());
            tagListSize.setText(String.valueOf(tagSize - 1));
            Log.d(TAG, "선택 개수 변화 확인" + tagSize + " =>" + tagListSize.getText().toString());
        }

    public void setTagDb(String tagText,boolean isAdd){

        Log.d(TAG, "리스트 추가 들어옴");

        db.collection("person").whereEqualTo("userId", user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot pDocument : task.getResult()) {
                        String pDocumentId = pDocument.getId();
                        if (isAdd) {
                            tagList.put(tagText, tagText);
                            addTagList(tagList,pDocumentId);

                        }else{
                            db.collection("person").document(pDocumentId)
                                    .collection("myTag")
                                    .document("myTagList")
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Map<String, Object> userTagList = new HashMap<>();
                                    userTagList = task.getResult().getData();
                                    userTagList.remove(tagText);
                                    updateTagList(userTagList, pDocumentId);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

            private void updateTagList(Map<String, Object> userTagList, String pDocumentId) {
                Log.d(TAG, "리스트 삭제 들어옴");
                db.collection("person").document(pDocumentId)
                        .collection("myTag")
                        .document("myTagList")
                        .set(userTagList)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, userTagList + " 저장");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "태그 저장 실패 = > " + e.toString());
                    }
                });
            }

            private void addTagList(Map<String, Object> userTagList, String pDocumentId) {
                db.collection("person").document(pDocumentId)
                        .collection("myTag")
                        .document("myTagList")
                        .set(userTagList, SetOptions.merge())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, userTagList + " 저장");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "태그 저장 실패 = > " + e.toString());
                    }
                });
            }
        }

        public void setOnTagItemClickListener(OnTagItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onItemClick(tagItemViewHolder holder, View view, int position) {
            if (listener != null){
                listener.onItemClick(holder, view, position);
            }
        }

        @Override
        public void onItemSelected(String tag) {

        }

        public Tags getItem(int position){
            return tagItemList.get(position);
        }

    }

