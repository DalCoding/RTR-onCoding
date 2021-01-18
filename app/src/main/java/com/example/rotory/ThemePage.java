package com.example.rotory;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.VO.Contents;
import com.example.rotory.VO.Information;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ThemePage extends Fragment {
    public static final int loginCode = 3000;
    final static String TAG = "ThemePage";

    RecyclerView themeRView;
    ThemePickPage themePickPage;

    CardView themeCardView;
    ImageView tcardThemeImg;
    TextView tcardThemeText;

    Information information;

    Boolean isSignIn = false;
    private FirestoreRecyclerAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.theme_page, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저네임 : " + checkLogIN);
            //isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
            // isSignIn = false;
        }

/*

        Query query = db.collection("contents").orderBy("theme", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<SearchContents> options = new FirestoreRecyclerOptions.Builder<SearchContents>()
                .setQuery(query, SearchContents.class)
                .build();

        themeRView = findViewById(R.id.themeRView);
        themeRView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FirestoreRecyclerAdapter<SearchContents, themeViewHolder>(options);

        @Override
        public void onDataChanged () {
            super.onDataChanged();
            Log.d(TAG, "어댑터 작동");
        }

        @Override
        protected void onBindViewHolder (@NonNull themeViewHolder holder,int position,
        @NonNull SearchContents model){
            holder.setContentsItems(model);
        }

        @NonNull
        @Override
        public themeViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_page_card, parent, false);
            return new themeViewHolder(view);
        }
    }

    ;

    themeRView.setAdapter(adapter);
}
@Override
protected void onStart() {
    super.onStart();
    adapter.startListening();
}

@Override
protected void onStop() {
    super.onStop();
    if (adapter != null) {
        adapter.stopListenig();
    }
}

public class themViewHolder extends RecyclerView.ViewHolder {
    private View view;

    public favoriteViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void set




        return rootView;

    }
*/return rootView;
    }
}

