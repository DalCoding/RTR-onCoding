package com.example.rotory.userActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.rotory.MainActivity;
import com.example.rotory.MainPage;
import com.example.rotory.MyPage;
import com.example.rotory.R;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Person;
import com.example.rotory.account.LogInActivity;
import com.example.rotory.account.SignUpActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MyLikeActivity extends AppCompatActivity {
    // 좋아요가 이미 되어있는 경우 작동 안되도록 설정!
    public static final int loginCode = 3000;
    final static String TAG = "MyLikedActivity";

    RecyclerView myLikeRecyclerView;
    MainPage mainPage;
    ThemePage themePage;

    BigMapPage bigMapPage;
    SignUpActivity signUpActivity;

    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    TextView profileTextView;

    RelativeLayout userAppbarContainer;

    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigation;

    Boolean isSignIn = false;
    private FirestoreRecyclerAdapter likedAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db;
    FirebaseUser user;

    AppConstant appConstant = new AppConstant();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_like_page);
        db = FirebaseFirestore.getInstance();

        user = mAuth.getCurrentUser();

        profileTextView = findViewById(R.id.profileTextView);

        appBarLayout =findViewById(R.id.appBarLayout);
        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);


        mainPage = new MainPage();
        themePage = new ThemePage();
        bigMapPage = new BigMapPage();


        bottomNavigation =findViewById(R.id.bottom_appBar);
       /* setBottomNavigation(bottomNavigation, isSignIn, loginCode,
                mainPage, themePage);*/
        myLikeRecyclerView = findViewById(R.id.myLikeRecyclerView);
        myLikeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String userUid = user.getUid();

        db.collection("person")
                .whereEqualTo("uid", userUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                String personId = snapshot.getId();
                                Log.d(TAG, personId);
                                setLikeAdapter(personId);
                                likedAdapter.startListening();
                                myLikeRecyclerView.setAdapter(likedAdapter);

                            }
                        }

                    }
                });

        profileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyLikeActivity.this, MyPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }

    private void setLikeAdapter(String personId) {
        Query query = db.collection("person").document(personId).collection("myLike")
                .orderBy("contentsId", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Liked> options = new FirestoreRecyclerOptions.Builder<Liked>()
                .setQuery(query, Liked.class)
                .build();

        likedAdapter = new FirestoreRecyclerAdapter<Liked, likeViewHolder>(options) {


            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, " 어댑터 작동");
            }

            @Override
            protected void onBindViewHolder(@NonNull likeViewHolder holder, int position, @NonNull Liked model) {
                holder.setLikedItems(model);
                holder.clickIcon();
                profileTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       holder.setIcon(model);
                       Intent intent = new Intent(MyLikeActivity.this, MyPage.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public likeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_like_item, parent,false);
                return new likeViewHolder(view);
            }
        };

    }


  @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (likedAdapter != null) {
            likedAdapter.stopListening();
        }
    }

    public class likeViewHolder extends RecyclerView.ViewHolder {
        private View view;
        ImageView myLikeIcon;
        Boolean removeItem = false;

        public likeViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            myLikeIcon = view.findViewById(R.id.myLikeEditImg1);
        }

        public void clickIcon(){
                myLikeIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!removeItem) {
                            myLikeIcon.setImageResource(R.drawable.heart);
                            removeItem = true;
                        }else {
                            myLikeIcon.setImageResource(R.drawable.heartfilled);
                            removeItem = false;
                        }
                    }
                });
        }

        public void setLikedItems(Liked items) {
            ImageView myLikePreImg = view.findViewById(R.id.myLikePreImg);
            TextView myLikeKindTextView = view.findViewById(R.id.myLikeKindTextView);
            TextView myLikeTagTextView = view.findViewById(R.id.myLikeTagTextView);
            TextView myLikeTitleTextView = view.findViewById(R.id.myLikeTitleTextView);

            String contentsType = getContentsType(items.getContentsType());

            myLikeTitleTextView.setText(items.getTitle());
            myLikeKindTextView.setText(contentsType);
            if (items.contentsType == 0) {
                if (items.getTag1() == null || items.getTag1().equals("")) {
                    myLikeTagTextView.setText("");
                } else {
                    myLikeTagTextView.setText(items.getTag1());
                    appConstant.getThemeImage(items.getTag1(), myLikePreImg, getApplicationContext());
                }
            }else if (items.contentsType == 1){
                myLikeTagTextView.setText("");
                String titleImage = items.getTitleImage();
                Log.d(TAG,titleImage);
                Bitmap titleImageBitmap = appConstant.StringToBitmap(items.getTitleImage());
                Log.d(TAG,titleImageBitmap.toString());
                myLikePreImg.setImageBitmap(titleImageBitmap);

            }

        }
        public void setIcon(Liked item){
            if (removeItem) {
                db.collection("person").whereEqualTo("userId", user.getEmail())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String pDocumentId = documentSnapshot.getId();
                                CollectionReference userCollectionRef = db.collection("person")
                                        .document(pDocumentId).collection("myLike");
                                userCollectionRef.whereEqualTo("contentsId", item.getContentsId())
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot thisLikeDocument : task.getResult()) {
                                                String userActDocId = thisLikeDocument.getId();
                                                userCollectionRef.document(userActDocId).delete();
                                                return;
                                            }

                                        }
                                    }
                                });

                            }
                        }
                    }
                });
            }else{

            }
        }

    }

    private String getContentsType(int contentsType) {
        String cTypeName;
        if (contentsType == 0){
            cTypeName= "도토리 길";

        }else {
            cTypeName = "다람쥐이야기";
        }
        return cTypeName;
    }

    public void setBottomNavigation(BottomNavigationView bottomNavigation, boolean isSignIn, int loginCode, MainPage mainPage, ThemePage themePage) {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent LogInIntent = new Intent(MyLikeActivity.this, LogInActivity.class);
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivityForResult(LogInIntent, loginCode);
                        setTabUnderBar(0);
                        bottomNavigation.setVisibility(View.VISIBLE);

                        return true;
                    case R.id.theme:
                        if (isSignIn) {
                            Intent myPageIntent = new Intent(MyLikeActivity.this, ThemePage.class);
                            myPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(myPageIntent);
                            setTabUnderBar(1);
                        } else {

                            startActivityForResult(LogInIntent, loginCode);
                            bottomNavigation.setVisibility(View.GONE);
                        }

                        return true;
                    case R.id.user:
                        if (isSignIn) {
                            Intent myPageIntent = new Intent(MyLikeActivity.this, MyPage.class);
                            startActivity(myPageIntent);
                            setTabUnderBar(2);
                        } else {
                            startActivityForResult(LogInIntent, loginCode);
                            bottomNavigation.setVisibility(View.GONE);
                        }

                        return true;
                }

                return false;
            }
        });
    }

    public void setTabUnderBar(int position) {
        if (position == 0) {
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.VISIBLE);
            bottomNavUnderbarTheme.setVisibility(View.GONE);
            bottomNavUnderbarUser.setVisibility(View.GONE);
        } else if (position == 1) {
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.GONE);
            bottomNavUnderbarTheme.setVisibility(View.VISIBLE);
            bottomNavUnderbarUser.setVisibility(View.GONE);
        } else if (position == 2) {
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.GONE);
            bottomNavUnderbarTheme.setVisibility(View.GONE);
            bottomNavUnderbarUser.setVisibility(View.VISIBLE);
        }
    }
    /*  @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.my_like_page, container,false);
        initUi(rootView);

        return rootView;
    }

    private void initUi(ViewGroup rootView) {

    }*/
      /* public void setMyFavoriteNickTextView(String userId){
            myFavoriteNickTextView.setText(userId);
        }

        public TextView getMyFavoriteLevelTextView() {
            return myFavoriteLevelTextView;
        }

        public void setMyFavoriteLevelTextView(String  level) {
            myFavoriteLevelTextView.setText(level);

        }*/
}
