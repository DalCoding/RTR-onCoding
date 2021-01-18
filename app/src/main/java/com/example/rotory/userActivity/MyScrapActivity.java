package com.example.rotory.userActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;
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

public class MyScrapActivity extends AppCompatActivity {
    private static final String TAG = "MyScrapActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirestoreRecyclerAdapter scrapAdapter;

    TextView myScrapLevel;
    TextView  myScrapKind;
    TextView myScrapTitle;
    TextView  myScrapContents;
    TextView myScrapSave;
    ImageView myScrapPreImg;
    ImageView myScrapLevelImg;

    RecyclerView myScrapRecyclerView;
    String userId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.my_scrap_page);

        FirebaseUser user = mAuth.getCurrentUser();

        myScrapRecyclerView = findViewById(R.id.myScrabRecyclerView);
        myScrapRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (user != null){
            userId  = user.getEmail();
            //loadScrapItem()
        }else {
            Toast.makeText(getApplicationContext(), "사용자 인식 오류", Toast.LENGTH_SHORT);
        }

      db.collection("person")
              .whereEqualTo("userId", userId)
              .get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if (task.isSuccessful()){
                          for (QueryDocumentSnapshot pDocument : task.getResult()) {
                              Query query = db.collection("person")
                                      .document(pDocument.getId()).collection("myScrap")
                                      .orderBy("savedDate", Query.Direction.DESCENDING);

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
        scrapAdapter =  new FirestoreRecyclerAdapter<Scrap, scrapViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, " 어댑터 작동");
            }
            @Override
            protected void onBindViewHolder(@NonNull scrapViewHolder holder, int position, @NonNull Scrap model) {
                holder.setUserItems(model);
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

        }
        public void setUserItems(Scrap scrap){
            myScrapTitle = view.findViewById(R.id.myScrabTitle);
            myScrapKind = view.findViewById(R.id.myScrabKindTextView);
            myScrapLevel = view.findViewById(R.id.myScrabLevelTextView);
            myScrapContents= view.findViewById(R.id.myScrabContentsTextView);
            myScrapSave= view.findViewById(R.id.myScrabSaveTextView);
            myScrapPreImg = view.findViewById(R.id.myScrapPreImg);
            myScrapLevelImg =view.findViewById(R.id.myScrabLevelImg);

            String contentsType;
            if (Integer.valueOf(scrap.getContentsType()) == 1){
                contentsType = "다람쥐 이야기";
            }else {
                contentsType = "도토리 길";
            }

            


    }
}


}
