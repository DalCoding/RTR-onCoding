package com.example.rotory;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.fragment.app.Fragment;

import com.example.rotory.Contents.StoryContentsPage;
import com.example.rotory.Interface.OnTabItemSelectedListener;


import com.example.rotory.Interface.OnUserActItemClickListener;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.VO.AppConstant;

import com.example.rotory.account.SignUpActivity;
import com.example.rotory.account.LogInActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener, OnUserActItemClickListener
        /*, AutoPermissionsListener */{

    public static final String TAG = "MainActivity";
    AppConstant appConstant;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    MainPage mainPage;
    ThemePage themePage;
    StoryContentsPage storyContentsPage;
    RoadContentsPage roadContentsPage=new RoadContentsPage();

    BigMapPage bigMapPage;
    SignUpActivity signUpActivity;

    ImageButton mainMapExtendBtn;

    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    RelativeLayout userAppbarContainer;

    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigation;
    Boolean isSignIn = false;

    FloatingActionButton mainFloatingBtn;

    private Animation fab_open, fab_close;
    private boolean isFabOpen = true;

    ImageButton popFloatingBtn;
    ImageButton pop2FloatingBtn;
    ProgressDialogs progressDialogs;


    @Override
    protected void onStart() {
        super.onStart();
        //setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPage = new MainPage();
        themePage = new ThemePage();
        bigMapPage = new BigMapPage();

        user = mAuth.getCurrentUser();


        if (user != null) {
            String checkLogIN = user.getEmail();
            Log.d(TAG, "로그인 정보 유저네임 : " + checkLogIN);
            isSignIn = true;
        } else {
            Log.d(TAG, "로그인 실패");
            isSignIn = false;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainPage).commit();


        // 아래부분 이후 옮김 -> 로그아웃 여부 실험!
        Button mainAlarmBtn = findViewById(R.id.mainAlarmBtn);
        mainAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null){
                    mAuth.signOut();
                    Log.d(TAG, "로그아웃");
                    finish();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                else {

                }
            }
        });

        TextView pageTitleTextView = findViewById(R.id.pageTitleTextView);
        pageTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoadStoryItem.class);
                startActivity(intent);

            }
        });



        appBarLayout = findViewById(R.id.appBarLayout);
        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);


        mainPage = new MainPage();
        themePage = new ThemePage();
        bigMapPage = new BigMapPage();
        storyContentsPage = new StoryContentsPage();


      //  getSupportFragmentManager().beginTransaction().replace(R.id.container, mainPage).commit();


        bottomNavigation = findViewById(R.id.bottom_appBar);
        setBottomNavigation(bottomNavigation, isSignIn);


        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        pop2FloatingBtn = findViewById(R.id.pop2FloatingBtn2);
        pop2FloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    Intent writeStoryIntent = new Intent(getApplicationContext(), Write_Story.class);
                    writeStoryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(writeStoryIntent);
                } else {
                    goLogInPage();

                }
            }
        });
        popFloatingBtn = findViewById(R.id.popFloatingBtn2);
        popFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    Intent writeRoadIntent = new Intent(getApplicationContext(), WriteRoadPage.class);
                    writeRoadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(writeRoadIntent);
                } else {
                    goLogInPage();

                }

            }
        });

        popFloatingBtn.startAnimation(fab_close);
        pop2FloatingBtn.startAnimation(fab_close);

        mainFloatingBtn =findViewById(R.id.mainFloatingBtn2);
        mainFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabOpen) {
                    popFloatingBtn.startAnimation(fab_open);
                    pop2FloatingBtn.setClickable(true);
                    pop2FloatingBtn.startAnimation(fab_open);
                    pop2FloatingBtn.setClickable(true);
                    isFabOpen = false;
                } else {
                    popFloatingBtn.startAnimation(fab_close);
                    pop2FloatingBtn.setClickable(false);
                    pop2FloatingBtn.startAnimation(fab_close);
                    pop2FloatingBtn.setClickable(false);
                    isFabOpen = true;
                }

            }
        });


        //AutoPermissions.Companion.loadAllPermissions(this, 101);

    }
    private void goLogInPage() {
        Intent LoginIntent = new Intent(this, LogInActivity.class);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(LoginIntent);

    }


/*    TextView commReportText = (commReportText)findViewById(R.id.commReportText);
    commReportText.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            dial();
        }
    });

    AlertDialog.Builder aBuilder = new AlertDialog.Builder(MainActivity.this);
    View mView = getLayoutInflater().inflate(R.layout.report, null);

    final Spinner sp = (Spinner)mView.findViewById(R.id.spinner);

    ArrayAdapter reportAdapter = ArrayAdapter.createFromResource(this, R.array.location, R.layout.simple_spinner_item);
    sp.setAdapter(reportAdapter);

    aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
        @Override
                public void onClick(DialogInterface dialog, int which) {
            String value = et.getText().toString();

            TextView text = (TextView)findViewById(R.id.test);
            text.setText(value);
            dialog.dismiss();  //닫기
        }
    });

    aBuilder.setView(mView);
    AlertDialog dialog = aBuilder.create();
    dialog.show();


    @Override
    public void OnTabSelected(int position) {
        if (position == 0) {
            bottomNavigation.setSelectedItemId(R.id.home);
        } else if (position == 1) {
            bottomNavigation.setSelectedItemId(R.id.theme);
        } else if (position == 2) {
            bottomNavigation.setSelectedItemId(R.id.user);
        }
    }*/


    public void setBottomNavigation(BottomNavigationView bottomNavigation, boolean isSignIn) {


        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        setTabUnderBar(0);
                        bottomNavigation.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.theme:
                        if(isSignIn) {
                            progressDialogs = new ProgressDialogs(MainActivity.this);
                            progressDialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            progressDialogs.show();
                            startTagsLoading(300);
                            Intent myPageIntent = new Intent(MainActivity.this, ThemePage.class);
                            startActivity(myPageIntent);
                            setTabUnderBar(1);
                        } else {
                            Intent LogInIntent = new Intent(getApplicationContext(), LogInActivity.class);
                            startActivity(LogInIntent);
                            bottomNavigation.setVisibility(View.GONE);
                        }
                        return true;
                    case R.id.user:
                        if (user != null) {
                            Intent myPageIntent = new Intent(MainActivity.this, MyPage.class);
                            myPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(myPageIntent);
                            setTabUnderBar(2);
                        } else {
                            Intent LogInIntent = new Intent(getApplicationContext(), LogInActivity.class);
                            LogInIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(LogInIntent);
                            bottomNavigation.setVisibility(View.GONE);
                            finish();
                        }
                        return true;
                }
                return false;
            }

            private void startTagsLoading(int millis) {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            progressDialogs.dismiss();
                        }
                    }, millis);
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

    public void replaceFragment(Fragment fragment) {
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
        }


    @Override
    public void OnStarClicked(String savedUserId, String myUserId) {
        Log.d(TAG, "onStarClicked : " + savedUserId);

        db.collection("person")
                .document(savedUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"onStared : 저장할 사용자 정보 받아오기 성공");
                            Map<String, Object> persons = new HashMap<>();
                            persons = task.getResult().getData();
                            Map<String, Object> myStar = new HashMap<>();
                            myStar.put("personId", savedUserId);
                            myStar.put("savedDate", new Date().toString());
                            myStar.put("userName", persons.get("userName"));
                            myStar.put("userLevel", persons.get("userLevel"));
                            myStar.put("userImage", persons.get("userImage"));
                            myStar.put("uid", persons.get("uid")); //이후 리스트에 포함되어있는지 여부를 찾기 위해 해당 항목 사용
                            Log.d(TAG, "onStared  맵에 잘 들어갔나?" + persons.get("userName"));
                            String userCollection = "myStar";
                            saveUserAct(myUserId, myStar,userCollection);
                        }
                    }
                });
    }

    @Override
    public void OnLikeClicked(String contentsId, Map<String, Object> contentsList, String userId) {
        Log.d(TAG, "onClicked: user에서 아이디 잘 받아옴?" + userId);
        Map<String, Object> myLike = new HashMap<>();
        myLike.put("contentsId",contentsId);
        myLike.put("contentsType", contentsList.get("contentsType"));
        myLike.put("title",contentsList.get("title").toString());
        myLike.put("titleImage",contentsList.get("titleImage").toString());
        myLike.put("savedDate", new Date().toString());
        myLike.put("uid",contentsList.get("uid").toString());//이후 리스트에 포함되어있는지 여부를 찾기 위해 해당 항목 사용
        String userCollection = "myLike";
        saveUserAct(userId, myLike, userCollection);


    }
    @Override
    public void OnFlagClicked(String contentsId, Map<String, Object> contentsList, String userId) {

        Map<String, Object> myScrap = new HashMap<>();
        myScrap.put("contentsId",contentsId);
        myScrap.put("contentsType", contentsList.get("contentsType"));
        myScrap.put("title", contentsList.get("title").toString());
        myScrap.put("titleImage", contentsList.get("titleImage").toString());
        myScrap.put("article", contentsList.get("article").toString());
        myScrap.put("contentsAddress", contentsList.get("address").toString());
        myScrap.put("savedDate", new Date().toString());
        myScrap.put("uid", contentsList.get("uid").toString());//이후 리스트에 포함되어있는지 여부를 찾기 위해 해당 항목 사용
        String userCollection = "myScrap";
        saveUserAct(userId, myScrap, userCollection);

    }
    //star, Like, Flag 폴더에 저장하는 메서드
    private void saveUserAct(String userId, Map<String, Object> myLike, String userCollection) {
        //1. userId-> 현재사용자에게서 받아온 사용자아이디로 사용자의 고유번호 찾기
        //2. 해당 고유번호 이용해서 사용자 자료 아래에 좋아요 폴더 생성
        db.collection("person")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "유저 아이디로 사용자 찾기 성공");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userDId = document.getId();
                                Log.d(TAG, "아이디 확인" + document.getId() + "==>" + userDId);
                                if (userDId != null) {
                                    db.collection("person").document(userDId)
                                            .collection(userCollection).document()
                                            .set(myLike)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "myLike 목록에 저장 성공");

                                                    } else {
                                                        Log.d(TAG, "테이블에 저장 실패");
                                                    }
                                                }

                                            });
                                } else {
                                    Log.d(TAG, "userId 없음" + userDId);
                                }
                            }

                        }
                    }
                });
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }*/

    @Override
    public void OnLinkClicked() {

    }

    @Override
    public void OnTabSelected(int position) {

    }

  /*  @Override
    public void onDenied(int requestCode, String[] permissions) {
        Log.d(TAG, "permissions denied : " + permissions.length);
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
        Log.d(TAG, "permissions granted : " + permissions.length);
    }*/

}

