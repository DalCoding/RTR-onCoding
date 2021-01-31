package com.example.rotory.Search;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.BigMapPage;
import com.example.rotory.LoadRoadItem;
import com.example.rotory.LoadStoryItem;
import com.example.rotory.MainPage;
import com.example.rotory.R;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.VO.AppConstant;
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

public class SearchTagResultPage extends AppCompatActivity {
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
    FirebaseUser user;


    LinearLayout searchBox;
    LinearLayout list;
    FrameLayout searchContainer;

    Query query;

    SearchPage searchPage;

    CardView searchResultView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_tag_result_page);


        searchPage = new SearchPage();

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




        Intent intent = getIntent();
        String tag = intent.getStringExtra("tag");

        TextView searchTagResultEdit = findViewById(R.id.searchTagResultEdit);
        searchTagResultEdit.setText(tag.substring(1));

        searchTagResultEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(SearchTagResultPage.this, SearchPage.class);
                intent2.putExtra("tag", tag.substring(1));
                startActivity(intent2);
            }
        });



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

                        }
                    }
                });


        Query query;

        if (tag != null){
            query = db.collection("contents")
                    .whereEqualTo("tag1", tag)
                    .orderBy("writeDate", Query.Direction.DESCENDING);
        }else{
            query = db.collection("contents");
        }

        FirestoreRecyclerOptions<SearchContents> options = new FirestoreRecyclerOptions.Builder<SearchContents>()
                .setQuery(query, SearchContents.class)
                .build();
        setSearchTagResultAdapter(options);
        searchTagResultAdapter.startListening();
        searchTagResultRecyclerView.setAdapter(searchTagResultAdapter);

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


    public class SearchTagResultViewHolder extends RecyclerView.ViewHolder {
        View view;

        public SearchTagResultViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }


        public void setContentsItems(SearchContents items) {

            db.collection("contents")
                    .whereEqualTo("title", items.getTitle())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId());
                                    String documentId = document.getId();
                                    view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (items.contentsType == 0) {
                                                roadIntent(documentId);
                                                Log.d(TAG, documentId);
                                            } else if (items.contentsType == 1) {
                                                storyIntent(documentId);
                                                Log.d(TAG, documentId);
                                            }
                                        }
                                    });
                                }
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });


            ImageView userLevel = itemView.findViewById(R.id.searchResultUserLevel);
            ImageView likedIcon = itemView.findViewById(R.id.searchResultFavoriteIcon);
            ImageView titleImg = itemView.findViewById(R.id.searchResultListImg);
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
                    titleImg.setVisibility(View.GONE);
                    break;
                case "이야기":
                    /*titleImg.setVisibility(View.VISIBLE);
                    titleImg.setImageURI(Uri.parse(uri));*/

                    String titleImage = items.getTitleImage();
                    Log.d(TAG, titleImage);
                    if (titleImage.equals("2131230836")) {
                        Bitmap titleImageBitmap = appConstant.StringToBitmap(titleImage);
                        Log.d(TAG, titleImageBitmap.toString());
                        titleImg.setImageBitmap(titleImageBitmap);
                    }
                    break;
            }
        }
    }

    private void storyIntent(String documentId) {
        Intent intent = new Intent(SearchTagResultPage.this, LoadStoryItem.class);
        intent.putExtra("documentId", documentId);
        startActivity(intent);
        finish();
    }

    private void roadIntent(String documentId) {
        Intent intent = new Intent(SearchTagResultPage.this, LoadRoadItem.class);
        intent.putExtra("documentId", documentId);
        startActivity(intent);
        finish();
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
