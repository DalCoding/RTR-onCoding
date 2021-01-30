package com.example.rotory.userActivity;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.rotory.BigMapPage;
import com.example.rotory.MainActivity;
import com.example.rotory.MainPage;
import com.example.rotory.MyPage;
import com.example.rotory.R;
import com.example.rotory.SearchPage1;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Person;
import com.example.rotory.account.LogInActivity;
import com.example.rotory.account.SignUpActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class MyFavoriteActivity  extends FragmentActivity {
    AppConstant appConstant = new AppConstant();
    final static String TAG = "MyFavoriteActivity";

    RecyclerView myFavoriteRecyclerView;
    MainPage mainPage;
    ThemePage themePage;

    ViewPager2 buttonBox;

    BigMapPage bigMapPage;
    SignUpActivity signUpActivity;

    TextView userActivityTextView;
    TextView myFavoriteCount;
    TextView profileTextView;

    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    RelativeLayout userAppbarContainer;

    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigation;

    Boolean removeItem =false;
    Boolean isSignIn = false;
    private FirestoreRecyclerAdapter favoriteAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_favorite_page);

        user = mAuth.getCurrentUser();

        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저네임 : " + checkLogIN);
            isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
            isSignIn = false;
        }

        userActivityTextView = findViewById(R.id.userActivityTextView);
        userActivityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        appBarLayout = findViewById(R.id.appBarLayout);
        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);

        mainPage = new MainPage();
        themePage = new ThemePage();
        bigMapPage = new BigMapPage();


        bottomNavigation = findViewById(R.id.bottom_appBar);
        setBottomNavigation(bottomNavigation, isSignIn, appConstant.loginCode,
                mainPage, themePage);
        myFavoriteRecyclerView = findViewById(R.id.myFavoriteRecyclerView);
        myFavoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myFavoriteCount = findViewById(R.id.myFavoriteCountTextView2);

        db.collection("person")
                .whereEqualTo("userId", user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String personId = document.getId();

                        db.collection("person").document(personId).collection("myStar")
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    int count = 0;
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        count++;
                                    }
                                    myFavoriteCount.setText(count + " 명");
                                }
                            }
                        });

                        Query query = db.collection("person")
                                .document(personId).collection("myStar")
                                .orderBy("savedDate", Query.Direction.ASCENDING);

                        FirestoreRecyclerOptions<Person> options = new FirestoreRecyclerOptions.Builder<Person>()
                                .setQuery(query, Person.class)
                                .build();
                        makeAdapter(options);
                        favoriteAdapter.startListening();
                        myFavoriteRecyclerView.setAdapter(favoriteAdapter);

                    }
                }
            }
        });

        profileTextView = findViewById(R.id.profileTextView);
        profileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFavoriteActivity.this, MyPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    private void makeAdapter(FirestoreRecyclerOptions<Person> options) {

        favoriteAdapter = new FirestoreRecyclerAdapter<Person, favoriteViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, " 어댑터 작동");
            }

            @Override
            protected void onBindViewHolder(@NonNull favoriteViewHolder holder, int position,
                                            @NonNull Person model) {
                holder.setUserItems(model);
                holder.onEachItemClick(model);
                holder.clickIcon();
                profileTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.setIcon(model);
                        Intent intent = new Intent(MyFavoriteActivity.this, MyPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(intent);
                        finish();
                    }
                });

            }

            @NonNull
            @Override
            public favoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_favorite_item, parent,false);
                return new favoriteViewHolder(view);
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
        if(favoriteAdapter != null){
            favoriteAdapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (favoriteAdapter != null){
            favoriteAdapter.startListening();
        }
    }

    public class favoriteViewHolder extends RecyclerView.ViewHolder {
        private View view;
        CardView favorietCard;
        ImageView starred;
        ImageView myFavoriteImg;
        TextView myFavoriteNickTextView;
        TextView myFavoriteLevelTextView;
        ImageView myFavoriteLevelImg;

        public favoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            myFavoriteLevelImg = itemView.findViewById(R.id.myFavoriteLevelImg);
            myFavoriteNickTextView = itemView.findViewById(R.id.myFavoriteNickTextView);
            myFavoriteLevelTextView = itemView.findViewById(R.id.myFavoriteLevelTextView);
            starred = itemView.findViewById(R.id.myFavoriteEditImg);
            favorietCard = itemView.findViewById(R.id.favorietCard);
        }
        public void setUserItems(Person user) {

            starred.setImageResource(R.drawable.starfilled);
            //myFavoriteImg.setImageResource(R.drawable.squirrel);

            db.collection("person").whereEqualTo("uid", user.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot pDocument : task.getResult()){
                            String userId = (String) pDocument.get("userId");
                            getProfileImg(userId);
                        }
                    }
                }
            });

            int levelImg = appConstant.getUserLevelImage(user.getUserLevel());
            myFavoriteLevelImg.setImageResource(levelImg);
            myFavoriteNickTextView.setText(user.getUserName());
            myFavoriteLevelTextView.setText(user.getUserLevel());

        }
        public void clickIcon(){

            starred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!removeItem) {
                        Log.d(TAG, "아이콘 눌림" + removeItem.toString());
                        starred.setImageResource(R.drawable.star);
                        removeItem = true;
                    }else {
                        Log.d(TAG, "아이콘 눌림" + removeItem.toString());
                        starred.setImageResource(R.drawable.starfilled);
                        removeItem = false;
                    }
                }
            });
        }
        public void onEachItemClick(Person user){
            favorietCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent userIntent = new Intent(MyFavoriteActivity.this, SearchPage1.class);
                    userIntent.putExtra("uid", user.getUid());
                    startActivity(userIntent);
                }
            });
        }
        public void setIcon(Person item){
            Log.d(TAG, "삭제 진행?" + removeItem.toString());
            if (removeItem) {
                db.collection("person").whereEqualTo("userId", user.getEmail())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String pDocumentId = documentSnapshot.getId();
                                CollectionReference userCollectionRef = db.collection("person")
                                        .document(pDocumentId).collection("myStar");
                                userCollectionRef.whereEqualTo("uid", item.getUid())
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot thisLikeDocument : task.getResult()) {
                                                String userActDocId = thisLikeDocument.getId();
                                                userCollectionRef.document(userActDocId).delete();
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

        public void getProfileImg(String Email) {
            myFavoriteImg = itemView.findViewById(R.id.myFavoriteImg);
            Log.d(TAG,"아이디 확인" + Email);
            String path = "profiles/"+ Email +".jpg";
            storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d(TAG,"storage에서 이미지 가져오기 성공" +uri);
                    Glide.with(getApplicationContext())
                            .load(uri)
                            .into(myFavoriteImg);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    /*String path ="profiles/squirrel.png";
                    storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG,"storage에서 기본 이미지 가져오기 성공" +uri);
                            Glide.with(getApplicationContext())
                                    .load(uri)
                                    .into(imageView);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG,"storage에서 이미지 가져오기 실패" +e.toString() );
                        }
                    });*/
                    //imageView.setImageResource(R.drawable.squirrel);

                }
            });


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    //하단바 설정
    public void setBottomNavigation(BottomNavigationView bottomNavigation, boolean isSignIn, int loginCode, MainPage mainPage, ThemePage themePage) {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent LogInIntent = new Intent(MyFavoriteActivity.this, LogInActivity.class);
                Intent mainIntent = new Intent(MyFavoriteActivity.this, MainActivity.class);
                switch (item.getItemId()) {
                    case R.id.home:

                        startActivityForResult(mainIntent, appConstant.mainCode);
                        setTabUnderBar(0);
                        bottomNavigation.setVisibility(View.VISIBLE);

                        return true;
                    case R.id.theme:
                        if (isSignIn) {
                            startActivityForResult(mainIntent, appConstant.themeCode);
                            setTabUnderBar(1);
                        } else {

                            startActivityForResult(LogInIntent, loginCode);
                            bottomNavigation.setVisibility(View.GONE);
                        }

                        return true;
                    case R.id.user:
                        if (isSignIn) {
                            Intent myPageIntent = new Intent(MyFavoriteActivity.this, MyPage.class);
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
