package com.example.rotory.Search;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.BigMapPage;
import com.example.rotory.MainPage;
import com.example.rotory.MyPage;
import com.example.rotory.R;
import com.example.rotory.ThemePage;
import com.example.rotory.VO.Road;
import com.example.rotory.VO.Story;
import com.example.rotory.account.LogInActivity;
import com.example.rotory.account.SignUpActivity;
//import com.example.rotory.model.research.SearchResult;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

//import static com.example.rotory.MainActivity.loginCode;


public class SearchResultPage extends AppCompatActivity {
    private static final String uri = "android.resource://com.example.rotory/drawable/bridge";
    private static final String TAG = "SearchResultPage";

    RecyclerView searchResultRecyclerView;
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

    private FirestoreRecyclerAdapter adapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_page);
        db = FirebaseFirestore.getInstance();

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


        mainPage = new MainPage();
        themePage = new ThemePage();
        bigMapPage = new BigMapPage();


        /*bottomNavigation = findViewById(R.id.bottom_appBar);
        setBottomNavigation(bottomNavigation, isSignIn, loginCode, mainPage, themePage);*/


        /*Query query = db.collection("story")
                .orderBy("modifiedDate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Story> options = new FirestoreRecyclerOptions.Builder<Story>()
                .setQuery(query, Story.class)
                .build();

        searchResultRecyclerView = findViewById(R.id.searchResultList);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FirestoreRecyclerAdapter<Story, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Story model) {
                holder.setStoryItems(model);
            }
        };

        searchResultRecyclerView.setAdapter(adapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setStoryItems(Story item) {
            ImageView userLevelImage;
            ImageView titleImage;
            ImageView likedIcon;
            TextView userName;
            TextView story;
            TextView title;
            TextView content;
            TextView liked;

            userLevelImage = itemView.findViewById(R.id.searchResultUserLevel);
            titleImage = itemView.findViewById(R.id.searchResultListImg);
            likedIcon = itemView.findViewById(R.id.searchResultFavoriteIcon);
            userName = itemView.findViewById(R.id.searchResultUserName);
            story = itemView.findViewById(R.id.searchResultListType);
            title = itemView.findViewById(R.id.searchResultListTitle);
            content = itemView.findViewById(R.id.searchResultListContents);
            liked = itemView.findViewById(R.id.searchResultFavoriteNumber);

            int levelImg = getUserLevelImage(item.getUserLevel());
            userLevelImage.setImageResource(levelImg);
            titleImage.setImageURI(Uri.parse(uri));
            likedIcon.setImageResource(R.drawable.favorite_icon);
            userName.setText(item.getUserName());
            story.setText(item.getTextStory());
            title.setText(item.getStoryTitle());
            content.setText(item.getStoryContents());
            liked.setText(item.getLiked());

        }
    }*/

        Query query = db.collection("road")
                .orderBy("modifiedDate", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Road> options = new FirestoreRecyclerOptions.Builder<Road>()
                .setQuery(query, Road.class)
                .build();

        searchResultRecyclerView = findViewById(R.id.searchResultList);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FirestoreRecyclerAdapter<Road, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item_noimg, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Road model) {
                holder.setRoadItems(model);
            }
        };

        searchResultRecyclerView.setAdapter(adapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;


        }

        public void setRoadItems(Road item) {
            ImageView userLevelImage;
            ImageView likedIcon;
            TextView userName;
            TextView road;
            TextView title;
            //TextView content;
            TextView liked;

            userLevelImage = itemView.findViewById(R.id.searchResultNoImgUserLevel);
            likedIcon = itemView.findViewById(R.id.searchResultNoImgFavoriteIcon);
            userName = itemView.findViewById(R.id.searchResultNoImgUserName);
            road = itemView.findViewById(R.id.searchResultNoImgListType);
            title = itemView.findViewById(R.id.searchResultNoImgListTitle);
            //content = itemView.findViewById(R.id.searchResultNoImgListContents);
            liked = itemView.findViewById(R.id.searchResultNoImgFavoriteNumber);

            int levelImg = getUserLevelImage(item.getUserLevel());
            userLevelImage.setImageResource(levelImg);
            likedIcon.setImageResource(R.drawable.favorite_icon);
            userName.setText(item.getUserName());
            road.setText(item.getTextRoad());
            title.setText(item.getRoadTitle());
            //content.setText((CharSequence) item.getDtrRoadLine());
            liked.setText(item.getLiked());

        }
    }


    private int getUserLevelImage(String userLevel) {
        switch (userLevel) {
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
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}