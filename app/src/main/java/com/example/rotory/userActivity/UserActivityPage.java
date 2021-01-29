package com.example.rotory.userActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.LoadRoadItem;
import com.example.rotory.LoadStoryItem;
import com.example.rotory.R;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Contents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserActivityPage extends Fragment {
    final String TAG = "UserActivityPage";
    ImageButton road_backbutton;
    ImageButton story_backbutton;

    MyRoadPage myRoadPage = new MyRoadPage();
    MyStoryPage myStoryPage = new MyStoryPage();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    RecyclerView story_recylerview;
    RecyclerView road_recylerview;

    AppConstant appConstant = new AppConstant();

    private FirestoreRecyclerAdapter userStoryActivityAdapter;
    private FirestoreRecyclerAdapter userRoadActivityAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.user_activity, container, false);
        user = mAuth.getCurrentUser();

        initUi(view);

        return view;
    }

    private void initUi(ViewGroup view) {
        String curUser = user.getUid();

        story_recylerview = view.findViewById(R.id.story_recylerview);
        story_recylerview.setLayoutManager(new LinearLayoutManager(getContext()));
        story_backbutton = view.findViewById(R.id.story_backbutton);
        story_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(myStoryPage);
            }
        });
        Query query = db.collection("contents")
                .whereEqualTo("contentsType", 1)
                .whereEqualTo("uid", curUser)
                //.whereEqualTo("article", 1)
                .orderBy("writeDate", Query.Direction.DESCENDING).limit(3);

        FirestoreRecyclerOptions<Contents> options = new FirestoreRecyclerOptions
                .Builder<Contents>().setQuery(query, Contents.class).build();
        makeUserStoryActivityAdapter(options);

        userStoryActivityAdapter.startListening();
        story_recylerview.setAdapter(userStoryActivityAdapter);

    /////

        road_recylerview = view.findViewById(R.id.road_recylerview);
        road_recylerview.setLayoutManager(new LinearLayoutManager(getContext()));
        road_backbutton = view.findViewById(R.id.road_backbutton);
        road_backbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                replaceFragment(myRoadPage);
            }
        });
        Query query2 = db.collection("contents")
                .whereEqualTo("contentsType", 0)
                .whereEqualTo("uid", curUser)
                .orderBy("writeDate", Query.Direction.DESCENDING).limit(3);

        FirestoreRecyclerOptions<Contents> options2 = new FirestoreRecyclerOptions
                .Builder<Contents>().setQuery(query2, Contents.class).build();
        makeUserRoadActivityAdapter(options2);

        userRoadActivityAdapter.startListening();
        road_recylerview.setAdapter(userRoadActivityAdapter);

    }

    //길
    private void makeUserRoadActivityAdapter(FirestoreRecyclerOptions<Contents> options2) {
        Log.d(TAG, "로드 어댑터 만들기 시작");
        userRoadActivityAdapter = new FirestoreRecyclerAdapter<Contents, UserRoadActivityViewHolder>(options2) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, "로드 어댑터 시작");
            }

            @Override
            protected void onBindViewHolder(@NonNull UserRoadActivityViewHolder holder, int position, @NonNull Contents model) {
                holder.setItem(model);
                holder.onEachItemClick(model);
            }

            @NonNull
            @Override
            public UserRoadActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.road_activity_item, parent, false);
                Log.d(TAG, "어댑터 뷰 받아옴" + view);
                return new UserRoadActivityViewHolder(view);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        userStoryActivityAdapter.startListening();
        userRoadActivityAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userStoryActivityAdapter.stopListening();
        userRoadActivityAdapter.stopListening();
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.replace(R.id.userActContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private class UserRoadActivityViewHolder extends RecyclerView.ViewHolder {
        TextView myRoadTitle;
        TextView myRoadTime;
        TextView myRoadWriteDate;
        ImageView mainRoadImage;
        String roadTime;
        CardView road_cardview;
        int hour;
        int min;

        public UserRoadActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            myRoadTitle =itemView.findViewById(R.id.roadtitle);
            myRoadTime = itemView.findViewById(R.id.roadtime);
            myRoadWriteDate = itemView.findViewById(R.id.road_writetime);
            mainRoadImage = itemView.findViewById(R.id.road_thumbnail);
            road_cardview = itemView.findViewById(R.id.road_cardview);

        }

        public void setItem(Contents contents){
            hour = Integer.valueOf(contents.getHour());
            min =Integer.valueOf(contents.getMin());
            myRoadTitle.setText(contents.getTitle());

            if (hour >0 && min >0){
                roadTime = hour + "시간 " + min +"분";
            }else if (hour == 0){
                roadTime = min +"분";
            }else if (min == 0){
                roadTime = hour +"시간";
            }

            myRoadTime.setText(String.valueOf(roadTime));
            myRoadWriteDate.setText(contents.getWriteDate());
            Log.d(TAG,"이미지 이름" + contents.getTag1());
            if (contents.getTag1() != null) {
                appConstant.getThemeImage(contents.getTag1(), mainRoadImage, getContext());
            }
        }
        public void onEachItemClick(Contents contents) {
            road_cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.collection("contents")
                            .whereEqualTo("writeDate", contents.getWriteDate())
                            .whereEqualTo("uid", contents.getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    String doucumentId = documentSnapshot.getId();
                                    Intent documentIntent = new Intent(getContext(), LoadRoadItem.class);
                                    documentIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    documentIntent.putExtra("documentId", doucumentId);
                                    startActivity(documentIntent);

                                }
                            }
                        }
                    });
                }
            });
        }
    }


    //이야기
    private void makeUserStoryActivityAdapter(FirestoreRecyclerOptions<Contents> options) {
        Log.d(TAG, "스토리 어댑터 만들기 시작");
        userStoryActivityAdapter = new FirestoreRecyclerAdapter<Contents, UserStoryActivityViewHolder>(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG, "스토리 어댑터 시작");
            }

            @Override
            protected void onBindViewHolder(@NonNull UserStoryActivityViewHolder holder, int position, @NonNull Contents model) {
                holder.setItem(model);
                holder.onEachItemClick(model);
            }

            @NonNull
            @Override
            public UserStoryActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_activity_item, parent, false);
                Log.d(TAG, "어댑터 뷰 받아옴" + view);
                return new UserStoryActivityViewHolder(view);
            }
        };
    }


        private class UserStoryActivityViewHolder extends RecyclerView.ViewHolder {
            TextView myStoryTitle;
            TextView myStoryWriteDate;
            ImageView mainStoryImage;
            CardView story_cardview;


        public UserStoryActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            myStoryTitle = itemView.findViewById(R.id.storytitle);
            myStoryWriteDate = itemView.findViewById(R.id.story_writetime);
            mainStoryImage = itemView.findViewById(R.id.story_thumbnail);
            story_cardview = itemView.findViewById(R.id.story_cardview);

        }

            public void setItem(Contents contents){

                Log.d(TAG, "제목" + contents.getTitle());
                myStoryTitle.setText(contents.getTitle());
                myStoryWriteDate.setText(contents.getWriteDate());
                if (contents.getTag1() != null) {
                    appConstant.getThemeImage(contents.getTag1(), mainStoryImage, getContext());
                }else {
                    mainStoryImage.setImageBitmap(appConstant.StringToBitmap(contents.getTitleImage()));
                }

            }
            public void onEachItemClick(Contents contents){
                story_cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("contents")
                                .whereEqualTo("writeDate", contents.getWriteDate())
                                .whereEqualTo("uid", contents.getUid())
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        String doucumentId = documentSnapshot.getId();
                                        Intent documentIntent = new Intent(getContext(), LoadStoryItem.class);
                                        documentIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        documentIntent.putExtra("documentId",doucumentId);
                                        startActivity(documentIntent);

                                    }
                                }
                            }
                        });
                    }
                });
            }

        }
    }
