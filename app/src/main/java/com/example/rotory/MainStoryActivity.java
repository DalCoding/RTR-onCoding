package com.example.rotory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Contents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainStoryActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter mainStoryAdapter;
    RecyclerView mainStoryRecyclerView;
    AppConstant appConstant = new AppConstant();
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_extend_story_list2);


        mainStoryRecyclerView = findViewById(R.id.mainStoryReccler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mainStoryRecyclerView.setLayoutManager(layoutManager);

        Query query = db.collection("contents")
                .whereEqualTo("contentsType", 1)
                .orderBy("liked", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Contents> options = new FirestoreRecyclerOptions.Builder<Contents>()
                .setQuery(query, Contents.class).build();

        makeAdapter(options);
        mainStoryAdapter.startListening();
        mainStoryRecyclerView.setAdapter(mainStoryAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mainStoryAdapter != null){
            mainStoryAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mainStoryAdapter != null){
            mainStoryAdapter.stopListening();
        }
    }

    private void makeAdapter(FirestoreRecyclerOptions<Contents> options) {

        mainStoryAdapter = new FirestoreRecyclerAdapter<Contents, MainStoryViewHolder>(options) {
            @NonNull
            @Override
            public MainStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_extend_story_list_item, parent, false);
                return new MainStoryViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MainStoryViewHolder holder, int position, @NonNull Contents model) {
                holder.setItem(model);
                holder.setClickItem(model);
            }
        };
    }

    public class MainStoryViewHolder extends RecyclerView.ViewHolder{
        ImageView mainExtendStoryImg;
        TextView mainExtendStoryTitle;
        TextView mainExtendStoryContents;
        TextView mainExtendStoryUserName;
        ImageView mainExtendStoryUserLevel;
        View view;

        public MainStoryViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            mainExtendStoryImg = itemView.findViewById(R.id.mainExtendStoryImg);
            mainExtendStoryTitle = itemView.findViewById(R.id.mainExtendStoryTitle);
            mainExtendStoryContents = itemView.findViewById(R.id.mainExtendStoryContents);
            mainExtendStoryUserName = itemView.findViewById(R.id.mainExtendStoryUserName);
            mainExtendStoryUserLevel = itemView.findViewById(R.id.mainExtendStoryUserLevel);
        }

        public void setItem(Contents model) {
            mainExtendStoryUserName.setText(model.getUserName());
            Bitmap bitmap = appConstant.StringToBitmap(model.getTitleImage());
            mainExtendStoryImg.setImageBitmap(bitmap);
            mainExtendStoryTitle.setText(model.getTitle());
            if (model.getArticle() != null) {
                mainExtendStoryContents.setText(model.getArticle());
            }
            mainExtendStoryUserLevel.setImageResource(appConstant.getUserLevelImage(model.getUserLevel()));
        }
        public void setClickItem(Contents model){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("contents")
                            .whereEqualTo("uid", model.getUid())
                            .whereEqualTo("writeDate", model.getWriteDate())
                            .whereEqualTo("title", model.getTitle())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot document : task.getResult()){
                                           
                                            String documentId = document.getId();
                                            Intent intent = new Intent(getApplicationContext(), LoadStoryItem.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("documentId", documentId);
                                            startActivity(intent);
                                        }
                                    };
                                }
                            });
                }
            });

        }
    }
}
