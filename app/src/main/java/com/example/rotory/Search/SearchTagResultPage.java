package com.example.rotory.Search;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.BigMapPage;
import com.example.rotory.Interface.LoadMoreContentsListener;
import com.example.rotory.MainPage;
import com.example.rotory.R;
import com.example.rotory.ThemePage;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Road;
import com.example.rotory.account.SignUpActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchTagResultPage extends AppCompatActivity implements LoadMoreContentsListener {
    AppConstant appConstant;

    private static final String uri = "android.resource://com.example.rotory/drawable/bridge";
    private static final String TAG = "SearchTagResultPage";

    MainPage mainPage;
    ThemePage themePage;
    BigMapPage bigMapPage;

    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    SignUpActivity signUpActivity;

    RelativeLayout userAppbarContainer;

    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigation;

    Boolean isSignIn = false;


    RecyclerView searchTagResultRecyclerView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter searchTagResultAdapter;

    Query query;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_tag_result_page);

        mainPage = new MainPage();
        themePage = new ThemePage();
        bigMapPage = new BigMapPage();


        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저네임 : " + checkLogIN);
            isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
            isSignIn = false;
        }

        appBarLayout = findViewById(R.id.appBarLayout);
        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);

        TextView searchResultTextView = findViewById(R.id.searchResultTextView);
        searchResultTextView.setVisibility(View.GONE);

        EditText searchResultEdit = findViewById(R.id.searchResultEdit);
        Button expandBtn = findViewById(R.id.searchResultExpandBtn);


        searchTagResultRecyclerView = findViewById(R.id.searchTagResultList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        searchTagResultRecyclerView.setLayoutManager(layoutManager);


        db.collection("contents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult());

                            Query query = db.collection("contents");

                            FirestoreRecyclerOptions<SearchContents> options = new FirestoreRecyclerOptions.Builder<SearchContents>()
                                    .setQuery(query, SearchContents.class)
                                    .build();
                            setSearchTagResultAdapter(options);
                            searchTagResultAdapter.startListening();
                            searchTagResultRecyclerView.setAdapter(searchTagResultAdapter);
                        }
                    }
                });

    }


    public void setSearchTagResultAdapter(FirestoreRecyclerOptions options) {

        searchTagResultAdapter = new FirestoreRecyclerAdapter<SearchContents, SearchTagResultViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, " 어댑터 작동");
            }

            @NonNull
            @Override
            public SearchTagResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
                return new SearchTagResultViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

            @Override
            protected void onBindViewHolder(@NonNull SearchTagResultViewHolder holder, int position, @NonNull SearchContents model) {
                holder.setContentsItems(model);
            }
        };
    }

    @Override
    public void loadMoreContents() {

    }


    public class SearchTagResultViewHolder extends RecyclerView.ViewHolder {
        View view;

        public SearchTagResultViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }


        public void setContentsItems(SearchContents items) {
            ImageView userLevel = itemView.findViewById(R.id.searchResultUserLevel);
            ImageView likedIcon = itemView.findViewById(R.id.searchResultFavoriteIcon);
            ImageView titleImage = itemView.findViewById(R.id.searchResultListImg);
            TextView userName = itemView.findViewById(R.id.searchResultUserName);
            TextView listType = itemView.findViewById(R.id.searchResultListType);
            TextView title = itemView.findViewById(R.id.searchResultListTitle);
            TextView article = itemView.findViewById(R.id.searchResultListContents);
            TextView liked = itemView.findViewById(R.id.searchResultFavoriteNumber);


            int levelImg = getUserLevelImage(items.getUserLevel());
            userLevel.setImageResource(levelImg);
            likedIcon.setImageResource(R.drawable.favorite_icon);
            userName.setText(items.getUserName());
            title.setText(items.getTitle());
            article.setText(items.getArticle());
            liked.setText(items.getLiked());
            String contentsType = getContentsType(items.getContentsType());
            listType.setText(contentsType);

            switch (contentsType) {
                case "길":
                    titleImage.setVisibility(View.GONE);
                    break;
                case "이야기":
                    titleImage.setVisibility(View.VISIBLE);
                    titleImage.setImageURI(Uri.parse(uri));
                    break;
            }
        }
    }

    private String getContentsType(int contentsType) {
        String listName;
        if (contentsType == 0) {
            listName = "길";
        } else {
            listName = "이야기";
        }
        return listName;
    }

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

    @Override
    public void onStart() {
        Log.d(TAG, "어댑터 작동 시작");
        super.onStart();
        //searchTagResultAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(searchTagResultAdapter != null){
            searchTagResultAdapter.stopListening();
        }
        //searchTagResultAdapter.stopListening();
    }
}
