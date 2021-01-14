package com.example.rotory.userActivity;

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

import com.example.rotory.R;
import com.example.rotory.VO.Person;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyFavoriteActivity  extends AppCompatActivity {
    final static String TAG = "MyFavoriteActivity";
    FirebaseFirestore db;
    RecyclerView myFavoriteRecyclerView;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_favorite_page);
        db = FirebaseFirestore.getInstance();

        Query query = db.collection("person")
                .orderBy("signUpDate", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Person> options = new FirestoreRecyclerOptions.Builder<Person>()
                .setQuery(query, Person.class)
                .build();

        myFavoriteRecyclerView = findViewById(R.id.myFavoriteRecyclerView);
        myFavoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FirestoreRecyclerAdapter<Person, favoriteViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, " 어댑터 작동");
            }

            @Override
            protected void onBindViewHolder(@NonNull favoriteViewHolder holder, int position,
                                            @NonNull Person model) {
                holder.setUserItems(model);
            }

            @NonNull
            @Override
            public favoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_favorite_item, parent,false);
               return new favoriteViewHolder(view);
            }
        };

        myFavoriteRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }

    public class favoriteViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public favoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }


        public void setUserItems(Person user) {
            ImageView myFavoriteImg;
            TextView myFavoriteNickTextView;
            TextView myFavoriteLevelTextView;
            ImageView myFavoriteLevelImg;
            myFavoriteImg = itemView.findViewById(R.id.myFavoriteImg);
            myFavoriteLevelImg = itemView.findViewById(R.id.myFavoriteLevelImg);
            myFavoriteNickTextView = itemView.findViewById(R.id.myFavoriteNickTextView);
            myFavoriteLevelTextView = itemView.findViewById(R.id.myFavoriteLevelTextView);


            int levelImg = getUserLevelImage(user.getUserLevel());
            myFavoriteLevelImg.setImageResource(levelImg);
            myFavoriteNickTextView.setText(user.getUserName());
            myFavoriteLevelTextView.setText(user.getUserLevel());
            //myFavoriteImg.setImageURI(Uri.parse(uri));

        }

    }
       /* public void setMyFavoriteNickTextView(String userId){
            myFavoriteNickTextView.setText(userId);
        }

        public TextView getMyFavoriteLevelTextView() {
            return myFavoriteLevelTextView;
        }

        public void setMyFavoriteLevelTextView(String  level) {
            myFavoriteLevelTextView.setText(level);

        }*/



    private int getUserLevelImage(String userLevel) {
        switch (userLevel){
            case "어린다람쥐":
                return R.drawable.level2;
            case "학생다람쥐":
                return R.drawable.level3;
            case "어른다람쥐" :
                return R.drawable.level4;
            case "박사다람쥐" :
                return R.drawable.level5;
            case "다람쥐의 신":
                return R.drawable.level6;
            default:
                return R.drawable.level1;
        }
    }
}
