package com.example.rotory.userActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.LoadRoadItem;
import com.example.rotory.MyPage;
import com.example.rotory.R;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.LoadStoryItem;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyScrapActivity extends AppCompatActivity {
    // 좋아요가 이미 되어있는 경우 작동 안되도록 설정!
    private static final String TAG = "MyScrapActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    private FirestoreRecyclerAdapter scrapAdapter;

    AppConstant appConstant = new AppConstant();

    TextView profileTextView;
    TextView myScrapLevel;
    TextView myScrapKind;
    TextView myScrapTitle;
    TextView myScrapContents;
    TextView myScrapSave;
    ImageView myScrapPreImg;
    ImageView myScrapLevelImg;
    TextView userActivityTextView;


    CardView myScrapItem;
    RecyclerView myScrapRecyclerView;
    String userId;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (scrapAdapter != null){
            scrapAdapter.startListening();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_scrap_page);
        db = FirebaseFirestore.getInstance();

        userActivityTextView = findViewById(R.id.userActivityTextView);
        userActivityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                finish();
            }
        });


        profileTextView = findViewById(R.id.profileTextView);
        profileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        myScrapRecyclerView = findViewById(R.id.myScrabRecyclerView);
        myScrapRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (user != null) {
            userId = user.getEmail();
            //loadScrapItem()
        } else {
            Toast.makeText(getApplicationContext(), "사용자 인식 오류", Toast.LENGTH_SHORT);
        }

        db.collection("person")
                .whereEqualTo("userId", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot pDocument : task.getResult()) {
                                Query query = db.collection("person")
                                        .document(pDocument.getId()).collection("myScrap")
                                        .whereNotEqualTo("savedDate","")
                                        .orderBy("savedDate", Query.Direction.DESCENDING);

                                Log.d(TAG, "다큐먼트 아이디" + pDocument.getId());

                                FirestoreRecyclerOptions<Scrap> options = new FirestoreRecyclerOptions.Builder<Scrap>()
                                        .setQuery(query, Scrap.class)
                                        .build();

                                makeScrapAdapter(options);
                                scrapAdapter.startListening();
                                myScrapRecyclerView.setAdapter(scrapAdapter);

                            }
                        }

                    }
                });
    }

    private void makeScrapAdapter(FirestoreRecyclerOptions<Scrap> options) {
        scrapAdapter = new FirestoreRecyclerAdapter<Scrap, scrapViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, " 어댑터 작동");
            }

            @Override
            protected void onBindViewHolder(@NonNull scrapViewHolder holder, int position, @NonNull Scrap model) {
                holder.setUserItems(model);
                holder.bind(model.getContentsType(), model.getContentsId());
            }

            @NonNull
            @Override
            public scrapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_scrap_item, parent, false);
                return new scrapViewHolder(view);
            }
        };
    }

    private class scrapViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public scrapViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            myScrapItem = findViewById(R.id.myScrabItem);

        }

        public void setUserItems(Scrap scrap) {
            myScrapTitle = view.findViewById(R.id.myScrabTitleTextView);
            myScrapKind = view.findViewById(R.id.myScrabKindTextView);
            myScrapLevel = view.findViewById(R.id.myScrabLevelTextView);
            myScrapContents = view.findViewById(R.id.myScrabContentsTextView);
            myScrapSave = view.findViewById(R.id.myScrabSaveTextView);
            myScrapPreImg = view.findViewById(R.id.myScrapPreImg);
            myScrapLevelImg = view.findViewById(R.id.myScrabLevelImg);
            myScrapContents = view.findViewById(R.id.myScrabContentsTextView);

            String contentsType;
            if (scrap.getContentsType() == 1) {
                contentsType = "다람쥐 이야기";

                String titleImage = scrap.getTitleImage();
                Log.d(TAG,titleImage);
                if(titleImage.equals("2131230836")){

                }else {
                    Bitmap titleImageBitmap = appConstant.StringToBitmap(titleImage);
                    Log.d(TAG, titleImageBitmap.toString());
                    myScrapPreImg.setMinimumWidth(120);
                    myScrapPreImg.setMinimumHeight(100);
                    myScrapPreImg.setImageBitmap(titleImageBitmap);
                }
                if (scrap.getArticle() != null) {
                    myScrapContents.setText(scrap.getArticle());
                }else{
                    myScrapContents.setText("");
                }


            } else {
                contentsType = "도토리 길";
                if (scrap.getTag1() != null) {
                    appConstant.getThemeImage(scrap.getTag1(), myScrapPreImg, getApplicationContext());
                }
                if (scrap.getDtrName() != null) {
                    String dtrName =scrap.getDtrName();
                   // ArrayList<String> dtrName = (ArrayList<String>) scrap.getDtrName();
                    Log.d(TAG,"도토리 이름 확인" + dtrName);
                    String dtrNameText = "";
                   /* for (int i = 0; i < dtrName.size(); i ++){
                        if (i == dtrName.size()-1){
                            dtrNameText = dtrNameText + dtrName.get(i);
                        }else {
                            dtrNameText =  dtrNameText + (dtrName.get(i) + " → ");
                        }
                        Log.d(TAG, "확인 : " + dtrNameText);
                    }*/
                    Log.d(TAG, "풀버전 확인 : " + dtrNameText);
                    myScrapContents.setText(dtrName);
                }
            }

            db.collection("person")
                    .whereEqualTo("userId",user.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userLevel = String.valueOf(document.get("userLevel"));
                                Log.d(TAG, userLevel);
                                myScrapLevel.setText(userLevel);
                                myScrapLevelImg.setImageResource(appConstant.getUserLevelImage(userLevel));
                            }
                        }
                    });
            myScrapTitle.setText(scrap.getTitle());
            myScrapKind.setText(contentsType);
            myScrapSave.setText(scrap.getSavedDate());


        }
        public void bind(int contentsType, String cDocumentID){
            myScrapItem = view.findViewById(R.id.myScrabItem);
            myScrapItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contentsType == 0){
                        Intent intent = new Intent(MyScrapActivity.this, LoadRoadItem.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("documentId", cDocumentID);
                        startActivity(intent);
                    } else if (contentsType == 1){
                        Intent intent = new Intent(MyScrapActivity.this, LoadStoryItem.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("documentId", cDocumentID);
                        startActivity(intent);
                    }
                }
            });
        }

    }
}
