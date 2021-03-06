package com.example.rotory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rotory.Interface.OnTabItemSelectedListener;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Person;
import com.example.rotory.account.ProfileEditPage;
import com.example.rotory.userActivity.MyFavoriteActivity;
import com.example.rotory.userActivity.MyLikeActivity;
import com.example.rotory.userActivity.MyScrapActivity;
import com.example.rotory.userActivity.Scrap;
import com.example.rotory.userActivity.UserActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MyPage extends AppCompatActivity implements OnTabItemSelectedListener {

    AppConstant appConstant = new AppConstant();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    final static String TAG = "MyPage";
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public static final int REQUEST_CODE_GALLERY = 101;
    public static final int MainCode = 1000;
    public static final int ThemeCode = 2000;

    RelativeLayout relativeLayout1;
    RelativeLayout relativeLayout2;

    FrameLayout profileEditContainer;
    FrameLayout myScrapLayout;
    Button myLevelOutBtn;
    TextView userActivityTextView;
    TextView myNickTextView;
    TextView myLevelTextView;
    TextView myFavoriteTextView;
    TextView myLikeTextView;
    TextView personIDText;
    TextView myExpTextView;

    ImageView myProfileImg;
    ImageView myEditImg;
    ImageView myLevelImg;
    ImageView myFavoriteImg;
    ImageView myLikeImg;
    Button myScrapBtn;
    TextView myAskTextView;
    RecyclerView myRecyclerView;
    CardView myScrapCardView;
    ProgressBar myLevelProgressBar;

    //Context imgContext;
    MainPage mainPage;
    ThemePage themePage;
    ProfileEditPage profileEditPage = new ProfileEditPage();

    private FirestoreRecyclerAdapter adapter;

    // 하단탭
    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    RelativeLayout userAppbarContainer;

    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigation;
    Boolean isSignIn;

    ProgressDialogs progressDialogs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);

        progressDialogs = new ProgressDialogs(this);
        progressDialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        relativeLayout1 = findViewById(R.id.myRelativeLayout1);
        relativeLayout2 = findViewById(R.id.myRelativeLayout2);
        profileEditContainer = findViewById(R.id.profileEditContainer);

        myNickTextView = findViewById(R.id.myNickTextView);


        user=mAuth.getCurrentUser();
        String userEmail = user.getEmail(); // e-mail 형식

        myProfileImg = findViewById(R.id.myProfileImg);
        loadScrapList();
        getProfileImg(userEmail, myProfileImg);
// 유저 정보 세팅
        db.collection("person")
                .whereEqualTo("userId", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG,"userId check" + userEmail);
                            Log.d(TAG,"userDocument check" + task.getResult());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String personId = document.getId();
                                likeCount(personId);
                                FavoriteCount(personId);

                                String userName1 = String.valueOf(document.get("userName"));
                                myNickTextView.setText(userName1);
                                myEditImg = findViewById(R.id.myEditImg);
                                myEditImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showPWDialog(document, document.getId());
                                    }
                                });
                                //    Log.d("firebase", document.getId() + " => " + personId);
                                int userPoint = Integer.parseInt(String.valueOf(document.get("userPoint")));
                                myExpTextView = findViewById(R.id.myExpTextView);
                                String expText = "(Exp. "+userPoint+"p)";
                                myExpTextView.setText(expText);
                                setPointToProgress(userPoint, personId);

                                String userLevel = String.valueOf(document.get("userLevel"));
                                myLevelTextView = findViewById(R.id.myLevelTextView);
                                myLevelTextView.setText(userLevel);
                                myLevelImg = findViewById(R.id.myLevelImg);
                                myLevelImg.setImageResource(appConstant.getUserLevelImage(userLevel));

                            }
                        } else {
                            Log.d("firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });



        userActivityTextView = findViewById(R.id.userActivityTextView);
        userActivityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myProfileImg = findViewById(R.id.myProfileImg);
        myProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGalleryActivity();
            }
        });

        myLevelImg = findViewById(R.id.myLevelImg);
        myLevelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout myLevelInfoLayout = findViewById(R.id.myLevelInfoLayout);
                myLevelInfoLayout.setVisibility(View.VISIBLE);
            }
        });

        myLevelOutBtn = findViewById(R.id.myLevelOutBtn);
        myLevelOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout myLevelInfoLayout = findViewById(R.id.myLevelInfoLayout);
                myLevelInfoLayout.setVisibility(View.INVISIBLE);
            }
        });


        myFavoriteImg = findViewById(R.id.myFavoriteImg);
        myFavoriteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyFavoriteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        myLikeImg = findViewById(R.id.myLikeImg);
        myLikeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyLikeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        myScrapBtn = findViewById(R.id.myScrabBtn);
        myScrapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyScrapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        myAskTextView = findViewById(R.id.myAskTextView);
        myAskTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AskPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // 그리드 설치
        myRecyclerView = findViewById(R.id.myRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        myRecyclerView.setLayoutManager(layoutManager);



        //하단탭
        appBarLayout = findViewById(R.id.appBarLayout);
        bottomNavUnderbarHome = findViewById(R.id.bottomNavUnderbarHome);
        bottomNavUnderbarTheme = findViewById(R.id.bottomNavUnderbarTheme);
        bottomNavUnderbarUser = findViewById(R.id.bottomNavUnderbarUser);

        mainPage = new MainPage();
        themePage = new ThemePage();

        bottomNavigation = findViewById(R.id.bottom_appBar);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                isSignIn = true;
                switch (item.getItemId()){
                    case R.id.home:
                        Intent MainIntent= new Intent(getApplicationContext(), MainActivity.class);
                        MainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(MainIntent);
                        finish();
                        bottomNavigation.setVisibility(View.VISIBLE);
                        return  true;

                    case R.id.theme:
                       // if(isSignIn) {
                        progressDialogs = new ProgressDialogs(MyPage.this);
                        progressDialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        progressDialogs.show();
                        startTagsLoading(300);
                            Intent ThemeIntent= new Intent(getApplicationContext(), ThemePage.class);
                            startActivityForResult(ThemeIntent, ThemeCode);
                            bottomNavigation.setVisibility(View.VISIBLE);

                        return true;

                    case R.id.user:

                        return true;
                }
                return false;
            }

            private void startTagsLoading(int millis){
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

    private void setPointToProgress(int userPoint, String personId) {

        myLevelProgressBar = findViewById(R.id.myLevelProgressBar);
        if (userPoint <100) {
            int userPoint1 = new Integer(Math.round(userPoint/100*20));
            myLevelProgressBar.setProgress(userPoint1);
            String userLevel1 = "아기 다람쥐";
            setUserLevel(userLevel1, personId);
        }
        if(100<= userPoint && userPoint < 600){
            int userPoint1 = new Integer(Math.round(userPoint/700*20));
            myLevelProgressBar.setProgress(20+userPoint1);
            String userLevel1 = "어린 다람쥐";
            setUserLevel(userLevel1, personId);
        }
        if(600<=userPoint && userPoint < 3000){
            int userPoint1 = new Integer(Math.round(userPoint/3600*20));
            myLevelProgressBar.setProgress(40+userPoint1);
            String userLevel1 = "학생 다람쥐";
            setUserLevel(userLevel1, personId);
        }
        if(3000<=userPoint && userPoint < 10000){
            int userPoint1 = new Integer(Math.round(userPoint/13000*20));
            myLevelProgressBar.setProgress(60+userPoint1);
            String userLevel1 = "어른 다람쥐";
            setUserLevel(userLevel1, personId);
        }
        if(10000<=userPoint && userPoint < 50000){
            int userPoint1 = new Integer(Math.round(userPoint/60000*20));
            myLevelProgressBar.setProgress(80+userPoint1);
            String userLevel1 = "박사 다람쥐";
            setUserLevel(userLevel1, personId);

        }
        if(50000 <= userPoint ){
            myLevelProgressBar.setProgress(100);
            String userLevel1 = "다람쥐의 신";
        }


    }

    private void setUserLevel(String userLevel1, String personId) {

        Map<String, Object> levelData = new HashMap<>();
        levelData.put("userLevel", userLevel1);

        db.collection("person").document(personId).set(levelData, SetOptions.merge());

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadScrapList();
       /* user=mAuth.getCurrentUser();
        String userEmail = user.getEmail(); // e-mail 형식
        myProfileImg = findViewById(R.id.myProfileImg);
        getProfileImg(userEmail, myProfileImg); */
    }

    private void FavoriteCount(String personId) {

                        db.collection("person").document(personId).collection("myStar")
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int count = 0;
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        count++;
                                    }
                                    myFavoriteTextView = findViewById(R.id.myFavoriteTextView);
                                    myFavoriteTextView.setText("즐겨찾기 "+count);
                                }
                            }
                        });
                    }


    private void likeCount(String personId) {

        db.collection("person").document(personId).collection("myLike")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count = 0;
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        count++;
                    }
                    myLikeTextView = findViewById(R.id.myLikeTextView);
                    myLikeTextView.setText("좋아요 "+count);
                }
            }
        });
    }



    @Override
    public void OnTabSelected(int position) {
        if(position == 0){
            bottomNavigation.setSelectedItemId(R.id.home);
        }else if(position == 1){
            bottomNavigation.setSelectedItemId(R.id.theme);
        }else if(position ==2){
            bottomNavigation.setSelectedItemId(R.id.user);
        }

    }

    public void setTabUnderBar(int position){
        if(position == 0){
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.VISIBLE);
            bottomNavUnderbarTheme.setVisibility(View.GONE);
            bottomNavUnderbarUser.setVisibility(View.GONE);
        }else if(position == 1){
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.INVISIBLE);
            bottomNavUnderbarTheme.setVisibility(View.VISIBLE);
            bottomNavUnderbarUser.setVisibility(View.INVISIBLE);
        }else if(position ==2){
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavUnderbarHome.setVisibility(View.GONE);
            bottomNavUnderbarTheme.setVisibility(View.GONE);
            bottomNavUnderbarUser.setVisibility(View.VISIBLE);
        }

    }

    /* onCreate 이후 기타 메소드들 */

    private void MyAdapter(FirestoreRecyclerOptions<Scrap> options) {

        adapter = new FirestoreRecyclerAdapter<Scrap, MyPage.myViewHolder>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, " 어댑터 작동");
            }

            @Override
            protected void onBindViewHolder(@NonNull MyPage.myViewHolder holder, int position,
                                            @NonNull Scrap model) {
                holder.setScrapItems(model);
                holder.bind(model.getContentsType(), model.getContentsId());

            }

            @NonNull
            @Override
            public MyPage.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_page_item, parent,false);
                return new MyPage.myViewHolder(view);
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
        if(adapter != null){
            adapter.stopListening();
        }
    }


    public class myViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
         /*   itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView myScrapId1 = itemView.findViewById(R.id.myScrabId);
                    String Id = myScrapId1.getText().toString();
                    Toast.makeText(getApplicationContext(), Id, Toast.LENGTH_SHORT).show();
                }
            }); */

        }

        public void setScrapItems(Scrap item) {
            TextView myScrapTitle;
            TextView myScrapPlace;
            TextView myScrapSave;
            TextView myScrapId;


            ImageView myScrapImg = itemView.findViewById(R.id.myScrabImg);//myScrapImg.setAlpha(50);
            myScrapTitle = itemView.findViewById(R.id.myScrabTitle);
            //     TextView myScrapSave = findViewById(R.id.myScrabSave);
            myScrapPlace = itemView.findViewById(R.id.myScrabPlace);
            myScrapId = itemView.findViewById(R.id.myScrabId);

            // myScrapImg.setImageURI(Uri.parse(uri));
            myScrapTitle.setText(item.getTitle());
            //  myScrapSave.setText(item.getSavedDate());
            if (item.address != null) {
                myScrapPlace.setText(item.getAddress().get(0));
            }
            myScrapId.setText(item.getContentsId());
            myScrapImg.setAlpha(70);

            if (item.contentsType == 0) {
                if (item.getTag1() != null) {
                    appConstant.getThemeImage(item.getTag1(), myScrapImg, getApplicationContext());
                }

            }else if (item.contentsType == 1){

                String titleImage = item.getTitleImage();
                Log.d(TAG,titleImage);
                if(titleImage.equals("2131230836")){

                }else {
                    Bitmap titleImageBitmap = appConstant.StringToBitmap(titleImage);
                    Log.d(TAG, titleImageBitmap.toString());
                    myScrapImg.setMinimumWidth(120);
                    myScrapImg.setMinimumHeight(100);
                    myScrapImg.setImageBitmap(titleImageBitmap);
                }

            }


//Title, Article, ContentsAddress, ContentsType, TitleImage , ContentsId, , SavedDate, Uid
            // 이중 해결해야할건 이미지, 시간 YY-mm-dd 형식으로 ('20.12.28 저장')
        }


        public void bind(int contentsType, String cDocumentID) {
            myScrapCardView = itemView.findViewById(R.id.myScrapCardView);
            myScrapCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contentsType == 0) {
                        Intent intent = new Intent(MyPage.this, LoadRoadItem.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("documentId", cDocumentID);
                        startActivity(intent);

                        //Log.d("인텐트", cDocumentID);
                    } else if (contentsType == 1) {
                        Intent intent = new Intent(MyPage.this, LoadStoryItem.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("documentId", cDocumentID);
                        startActivity(intent);

                       // Log.d("인텐트", cDocumentID);
                    }
                }
            });
        }


    }

/*    public void setScrapItems2() {

        FirebaseUser user = mAuth.getCurrentUser();
        // String userEmail = user.getEmail(); // e-mail 형식
        //8개 불러와서 최근 순서대로 이미지,제목,저장날짜,장소 담기
        db.collection("person")
                .whereEqualTo("userId", user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String personId = document.getId();


                        db.collection("person").document(personId).collection("myScrap")
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int count = 0;
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        count++;

                                    }
                                    //  String count1 = Integer.toString(count);
                                    // Log.d("count 횟수", count1);

                                    if (count < 8) {
                                        int count1 = 8 - count; //실행횟수
                                        String resName = "@drawable/noimage2";
                                        for (int j = 0; j < count1; j++) {


                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

*/
    public void showGalleryActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY) {
            if(data==null){
                Intent intent = new Intent(getApplicationContext(), MyPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else {
                Uri uri = data.getData();
                String userEmail = user.getEmail();

            //    appConstant.setProfileImg(uri, userEmail);
                Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

                appConstant.setProfileImg(bitmap, userEmail);

                Handler nHandler = new Handler();
                nHandler.postDelayed(new Runnable() {
                    public void run() {
                        getProfileImg(userEmail, myProfileImg);
                    }
                }, 2000);

            }
        }
    }

    public void showPWDialog(QueryDocumentSnapshot pDocument, String pDocumentId){
        AlertDialog.Builder PWCheck = new AlertDialog.Builder(this);
        PWCheck.setTitle("비밀번호 확인");       // 제목 설정
        // EditText 삽입하기
        final EditText PWCheckEditText = new EditText(this);
        PWCheckEditText.setWidth(100);
        PWCheckEditText.setGravity(View.TEXT_ALIGNMENT_CENTER);
        PWCheckEditText.setTextSize(18);
        PWCheck.setView(PWCheckEditText);

        // 확인 버튼 설정
        PWCheck.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG,"showPwDialog"+pDocument.get("password").toString());

                // Text 값 받아서 처리
                String PWCheckEditText1 = PWCheckEditText.getText().toString();
                  //닫기

                if (PWCheckEditText1.equals(pDocument.get("password").toString())) {
                    loadPersonInfo();
                    Bundle pDocumentIdBundle = new Bundle();
                    pDocumentIdBundle.putString("pDocumentId", pDocumentId);

                    profileEditPage.setArguments(pDocumentIdBundle);

                    Log.d(TAG,"비밀번호 일치, 페이지 변경");
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        PWCheck.show();
    }
    // 설정창으로 이동
    private void loadPersonInfo() {


        relativeLayout1.setVisibility(View.GONE);
        relativeLayout2.setVisibility(View.GONE);
        bottomNavigation.setVisibility(View.GONE);

        profileEditContainer.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.profileEditContainer, profileEditPage).commit();

        // 전체적인인 정보 수정 + 레벨 레이아웃에 exp 도 담기
    }
    public void closeProfileEditor(){

        relativeLayout1.setVisibility(View.VISIBLE);
        relativeLayout2.setVisibility(View.VISIBLE);
        bottomNavigation.setVisibility(View.VISIBLE);

        profileEditContainer.setVisibility(View.GONE);

    }

    public void getProfileImg(String Email, ImageView imageView) {

        String path = "profiles/"+ Email +".jpg";
        storageReference.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG,"storage에서 이미지 가져오기 성공" +uri);
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"storage에서 이미지 가져오기 실패" +e.toString() );
            }
        });


    }

    public void loadScrapList(){

        FirebaseUser user = mAuth.getCurrentUser();
       // String userEmail = user.getEmail(); // e-mail 형식
        //8개 불러와서 최근 순서대로 이미지,제목,저장날짜,장소 담기
        db.collection("person")
                .whereEqualTo("userId", user.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String personId = document.getId();

                        Query query = db.collection("person")
                                .document(personId).collection("myScrap")
                                .orderBy("savedDate", Query.Direction.DESCENDING).limit(8);

                        FirestoreRecyclerOptions<Scrap> options = new FirestoreRecyclerOptions.Builder<Scrap>()
                                .setQuery(query, Scrap.class)
                                .build();
                        MyAdapter(options);
                        adapter.startListening();
                        myRecyclerView.setAdapter(adapter);

                    }
                }
            }
        });
    }
}
