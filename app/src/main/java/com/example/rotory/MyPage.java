package com.example.rotory;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.rotory.Interface.OnTabItemSelectedListener;
import com.example.rotory.Theme.ThemePage;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Person;
import com.example.rotory.account.ProfileEditPage;
import com.example.rotory.userActivity.MyFavoriteActivity;
import com.example.rotory.userActivity.MyLikeActivity;
import com.example.rotory.userActivity.MyScrapActivity;
import com.example.rotory.userActivity.Scrap;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MyPage extends AppCompatActivity implements OnTabItemSelectedListener {

    AppConstant appConstant = new AppConstant();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    Person persons = new Person();
    final static String TAG = "MyPage";

    public static final int REQUEST_CODE_GALLERY = 101;
    public static final int MainCode = 1000;
    public static final int ThemeCode = 2000;
    // public static final int LoginCode = 3000;

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

    ImageView myProfileImg;
    ImageView myEditImg;
    ImageView myLevelImg;
    ImageView myFavoriteImg;
    ImageView myLikeImg;
    Button myScrapBtn;
    TextView myAskTextView;
    RecyclerView myRecyclerView;
    CardView myScrapCardView;

    MainPage mainPage;
    ThemePage themePage;
    ProfileEditPage profileEditPage = new ProfileEditPage();

    // 개인정보 설정
    /* TextView myNickTextView;
    TextView myLevelTextView;
    TextView myExpTextView; // 이걸 프로그레스로 적용할땐 나눈값을 반올림하던가 해야할듯
    TextView myFavoriteTextView;
    TextView myLikeTextView; */
    private FirestoreRecyclerAdapter adapter;

    // 하단탭
    RelativeLayout bottomNavUnderbarHome;
    RelativeLayout bottomNavUnderbarTheme;
    RelativeLayout bottomNavUnderbarUser;

    RelativeLayout userAppbarContainer;

    AppBarLayout appBarLayout;
    BottomNavigationView bottomNavigation;
    Boolean isSignIn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);

        relativeLayout1 = findViewById(R.id.myRelativeLayout1);
        relativeLayout2 = findViewById(R.id.myRelativeLayout2);
        profileEditContainer = findViewById(R.id.profileEditContainer);

        myNickTextView = findViewById(R.id.myNickTextView);


        user=mAuth.getCurrentUser();
        String userEmail = user.getEmail(); // e-mail 형식

        loadScrapList();
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
                                String userLevel = String.valueOf(document.get("userLevel"));
                                myLevelTextView = findViewById(R.id.myLevelTextView);
                                myLevelTextView.setText(userLevel);
                                myLevelImg = findViewById(R.id.myLevelImg);
                                myLevelImg.setImageResource(appConstant.getUserLevelImage(userLevel));
                                myEditImg = findViewById(R.id.myEditImg);
                                myEditImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showPWDialog(document, document.getId());
                                    }
                                });
                                //    Log.d("firebase", document.getId() + " => " + personId);

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
                //Intent intent = new Intent(getApplicationContext(), userActivityPage.class);
                //startActivity(intent);
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
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        myScrapBtn = findViewById(R.id.myScrabBtn);
        myScrapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyScrapActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                        bottomNavigation.setVisibility(View.VISIBLE);
                        setTabUnderBar(0);
                        return  true;

                    case R.id.theme:
                       // if(isSignIn) {
                            Intent ThemeIntent= new Intent(getApplicationContext(), ThemePage.class);
                            startActivityForResult(ThemeIntent, ThemeCode);
                            bottomNavigation.setVisibility(View.VISIBLE);
                             setTabUnderBar(1);
                            //여기선 필요없을듯
                       /* } else  {
                            Intent LogInIntent= new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivityForResult(LogInIntent, LoginCode);
                            bottomNavigation.setVisibility(View.GONE);

                        }*/
                        return true;

                    case R.id.user:
                     /*if(isSignIn) {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        } else {
                            Intent LogInIntent= new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivityForResult(LogInIntent, LoginCode);
                            bottomNavigation.setVisibility(View.GONE);
                        }*/
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadScrapList();
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
            bottomNavUnderbarHome.setVisibility(View.GONE);
            bottomNavUnderbarTheme.setVisibility(View.VISIBLE);
            bottomNavUnderbarUser.setVisibility(View.GONE);
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


            //     ImageView myScrapImg = findViewById(R.id.myScrabImg);//myScrapImg.setAlpha(50);
            myScrapTitle = itemView.findViewById(R.id.myScrabTitle);
            //     TextView myScrapSave = findViewById(R.id.myScrabSave);
            myScrapPlace = itemView.findViewById(R.id.myScrabPlace);
            myScrapId = itemView.findViewById(R.id.myScrabId);

            // myScrapImg.setImageURI(Uri.parse(uri));
            myScrapTitle.setText(item.getTitle());
            //  myScrapSave.setText(item.getSavedDate());
            myScrapPlace.setText(item.getContentsAddress());
            myScrapId.setText(item.getContentsId());


//Title, Article, ContentsAddress, ContentsType, TitleImage , ContentsId, , SavedDate, Uid
            // 이중 해결해야할건 이미지, 시간 YY-mm-dd 형식으로 ('20.12.28 저장')
        }


        public void bind(int contentsType, String cDocumentID) {
            myScrapCardView = itemView.findViewById(R.id.myScrapCardView);
            myScrapCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contentsType == 0) {
                        Intent intent = new Intent(MyPage.this, RoadContentsPage.class);
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
            Uri uri = data.getData();
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
            myProfileImg.setImageBitmap(bitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 40, baos);
            byte[] bytes = baos.toByteArray();
            String temp = Base64.encodeToString(bytes, Base64.DEFAULT);

            Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
            // String DB로 보내기
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
                                .orderBy("savedDate", Query.Direction.ASCENDING).limit(8);

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


//showPWDialog O  (https://dhparkdh.tistory.com/103)
// :  기타 이동 버튼(활동내역 ,즐겨찾기, 좋아요, 스크랩리스트, 문의하기) O
// 상단탭 넣기  / 하단탭 코드 O

//남은것 : DB작업 => 1.불러와서 개인정보 담기(좋아요 수, EXP 포함), 2.스크랩리스트부르기
