package com.example.rotory.Theme;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ThemePickPage extends AppCompatActivity {
    private final static String TAG = "ThemePickPage";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    public FirestoreRecyclerAdapter tagListAdapter;
    public FirestoreRecyclerAdapter tagItemAdapter;

    RecyclerView activityTagRecyclerView;

    Button tagBtn;


/*    // 그리드 설치
    myRecyclerView = findViewById(R.id.myRecyclerView);
    GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        myRecyclerView.setLayoutManager(layoutManager);

        MyPage 참고*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_pick_card);

        activityTagRecyclerView = findViewById(R.id.activityTagRecyclerView);
        GridLayoutManager tagLayoutManager = new GridLayoutManager(this, 3);
        activityTagRecyclerView.setLayoutManager(tagLayoutManager);

        Query query = db.collection("tag")
                .whereNotEqualTo(FieldPath.documentId(), "tagList");

        FirestoreRecyclerOptions<Tags> options = new FirestoreRecyclerOptions.Builder<Tags>()
                .setQuery(query, Tags.class)
                .build();

        makeTagItemsAdapter(options);
        tagItemAdapter.startListening();
        activityTagRecyclerView.setAdapter(tagItemAdapter);


    }

    private void makeTagItemsAdapter(FirestoreRecyclerOptions<Tags> options) {
        tagItemAdapter = new FirestoreRecyclerAdapter<Tags, tagItemViewHolder>(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG,"각 태그에 대한 어댑터 시작");
            }

            @NonNull
            @Override
            public tagItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent,false );
                return new tagItemViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull tagItemViewHolder holder, int position, @NonNull Tags model) {
                holder.setTagItems(model);
            }
        };
    }

    private class tagItemViewHolder extends  RecyclerView.ViewHolder{
        View view;

        public tagItemViewHolder(@NonNull View itemView) {
            super(itemView);
            view= itemView;
            tagBtn = itemView.findViewById(R.id.tagBtn);
        }

        public void setTagItems(Tags item){
            tagBtn.setText(item.getTag());

        }
    }
}
