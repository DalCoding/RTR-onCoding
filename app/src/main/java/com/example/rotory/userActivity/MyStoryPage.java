package com.example.rotory.userActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Contents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import javax.annotation.Nullable;

public class MyStoryPage extends Fragment {
    final String TAG = "MyStoryPage";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    RecyclerView storylist;

    AppConstant appConstant = new AppConstant();

    private FirestoreRecyclerAdapter myStoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.story_activity, container,false);
        user = mAuth.getCurrentUser();

        initUi(view);

        return view;
    }

    private void initUi(ViewGroup view) {
        String curUser = user.getUid();

        storylist = view.findViewById(R.id.storylist);
        storylist.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = db.collection("contents")
                .whereEqualTo("contentsType", 1)
                .whereEqualTo("uid", curUser)
                .orderBy("writeDate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Contents> options = new FirestoreRecyclerOptions
                .Builder<Contents>().setQuery(query, Contents.class).build();

        makeMyStoryAdapter(options);
        myStoryAdapter.startListening();
        storylist.setAdapter(myStoryAdapter);

    }

    @Override
    public void onStart() {
    super.onStart();
    myStoryAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myStoryAdapter.stopListening();
    }

    private void makeMyStoryAdapter(FirestoreRecyclerOptions<Contents> options) {
        Log.d(TAG, "어댑터 만들기 시작");
        myStoryAdapter = new FirestoreRecyclerAdapter<Contents, MyStoryViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, "어댑터 시작");
            }

            @Override
            protected void onBindViewHolder(@NonNull MyStoryViewHolder holder, int position, @NonNull Contents model) {
                holder.setItem(model);
            }

            @NonNull
            @Override
            public MyStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_activity_item, parent, false);
                Log.d(TAG, "어댑터 뷰 받아옴" + view);
                return new MyStoryViewHolder(view);
            }
        };
    }

    private class MyStoryViewHolder extends RecyclerView.ViewHolder {
        TextView myStoryTitle;
        TextView myStoryWriteDate;
        ImageView mainStoryImage;

        public MyStoryViewHolder(@NonNull View itemView) {
            super(itemView);
            myStoryTitle = itemView.findViewById(R.id.storytitle);
            myStoryWriteDate = itemView.findViewById(R.id.story_writetime);
            mainStoryImage = itemView.findViewById(R.id.story_thumbnail);

        }

        public void setItem(Contents contents) {
            myStoryTitle.setText(contents.getTitle());
            myStoryWriteDate.setText(contents.getWriteDate());
            //myStoryContents.setText(contents.getArticle()); 내용 어디 들어가는지 확인
            Log.d(TAG,"이미지 이름" + contents.getTag1());
            if (contents.getTag1() != null) {
                appConstant.getThemeImage(contents.getTag1(), mainStoryImage, getContext());
            }

        }
    }
}




