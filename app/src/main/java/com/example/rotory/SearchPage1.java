package com.example.rotory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Contents;
import com.example.rotory.userActivity.MyFavoriteActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class SearchPage1 extends AppCompatActivity {

    final static String TAG = "SearchPage1";

    //search_page_1.xml : 바탕
    TextView searchIdText;
    RecyclerView search1List;

    //search_result_item.xml : 아이템
    TextView searchResultListTitle;
    TextView searchResultListContents;
    ImageView searchResultFavoriteIcon;
    TextView searchResultFavoriteNumber;
    ImageView searchResultListImg;

    Boolean isSignIn = false;
    private FirestoreRecyclerAdapter searchPage1Adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    AppConstant appConstant = new AppConstant();

    String uid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page_1);
        FirebaseUser user = mAuth.getCurrentUser();  //로그인하고있는유저

        Intent favoriteIntent = getIntent();
       uid = favoriteIntent.getStringExtra("uid");
        //uid = "vMafjaj28WQiCFczxLVeLmJFKDd2";

        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저 네임 : " + checkLogIN);
            isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
            isSignIn = false;
        }

        //getUserWrite();

        searchIdText = findViewById(R.id.searchIdText);
        db.collection("person")
                .whereEqualTo("uid", uid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Log.d(TAG,documentSnapshot.get("userName").toString());
                        searchIdText.setText(documentSnapshot.get("userName").toString());
                    }
                }
            }
        });

        Query query = db.collection("contents")
                .whereEqualTo("uid", uid)
                .orderBy("writeDate", Query.Direction.DESCENDING);
        Log.d(TAG, "쿼리문 확인" + query);

        TextView searchIdText = findViewById(R.id.searchIdEdit);
        RecyclerView search1List = findViewById(R.id.search1List);

        FirestoreRecyclerOptions<Contents> options = new FirestoreRecyclerOptions.Builder<Contents>()
                .setQuery(query, Contents.class)
                .build();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        search1List.setLayoutManager(layoutManager);

        makeSearchPage1Adapter(options);
        searchPage1Adapter.startListening();
        search1List.setAdapter(searchPage1Adapter);


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (searchPage1Adapter != null){
            searchPage1Adapter.stopListening();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SearchPage1.this, MyFavoriteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void makeSearchPage1Adapter(FirestoreRecyclerOptions<Contents> options) {
            searchPage1Adapter = new FirestoreRecyclerAdapter<Contents, SearchPage1ViewHolder>(options) {

                @NonNull
                @Override
                public SearchPage1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
                    return new SearchPage1ViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull SearchPage1ViewHolder holder, int position, @NonNull Contents model) {
                    Log.d(TAG, "onBindViewHolder 작동" + holder.itemView.toString());
                    holder.setSearchPage1Items(model);
                }

                @Override
                public void onDataChanged() {
                    super.onDataChanged();
                    Log.d(TAG, "어댑터 작동");
                }
            };
        }

        public class SearchPage1ViewHolder extends RecyclerView.ViewHolder {
            private View view;
            TextView searchResultListType;
            ImageView searchResultUserLevel;
            TextView searchResultUserName;

            public SearchPage1ViewHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView;
            }

            public void setSearchPage1Items(Contents searchPage1) {
                Log.d(TAG, "set SearchPage1Items 시작");

                searchResultUserName = view.findViewById(R.id.searchResultUserName);
                searchResultUserLevel = view.findViewById(R.id.searchResultUserLevel);
                searchResultListType = view.findViewById(R.id.searchResultListType);
                searchResultListTitle = view.findViewById(R.id.searchResultListTitle);
                searchResultListContents = view.findViewById(R.id.searchResultListContents);
                searchResultFavoriteIcon = view.findViewById(R.id.searchResultFavoriteIcon);
                searchResultFavoriteNumber = view.findViewById(R.id.searchResultFavoriteNumber);
                searchResultListImg = view.findViewById(R.id.searchResultListImg);
                searchResultListImg.setVisibility(View.VISIBLE);

                searchResultUserName.setText(searchPage1.getUserName());
                searchResultUserLevel.setImageResource(appConstant.getUserLevelImage(searchPage1.getUserLevel()));
                searchResultListTitle.setText(searchPage1.getTitle());
                if (searchPage1.getArticle() != null){
                searchResultListContents.setText(searchPage1.getArticle());
                } else {
                    searchResultListContents.setText("");
                } // 만약 article이 비어있다면 "" 빈칸으로 두기

                if (searchPage1.getContentsType() == 0) {
                    searchResultListType.setText("길");
                    if (searchPage1.getTag1() != null){
                        appConstant.getThemeImage(searchPage1.getTag1(),searchResultListImg,getApplicationContext());
                    }
                }else if (searchPage1.getContentsType() == 1) {
                    searchResultListType.setText("이야기");

                    Bitmap bitmap = appConstant.StringToBitmap(searchPage1.getTitleImage());
                    searchResultListImg.setImageBitmap(bitmap);
                }

                if (searchPage1.getLiked() != null){
                    searchResultFavoriteNumber.setText(searchPage1.getLiked());
                }else {
                    searchResultFavoriteNumber.setText("");
                }

                }

    }
}

/*
        db.collection("contents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot : task.getResult()){

                            Query query = db.collection("contents");

                            FirestoreRecyclerOptions<>
                        }
                    }
                });

    }*/

