package com.example.rotory.userActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.LoadMapDtrListener;
import com.example.rotory.R;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.VO.Contents;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyRoadPage extends Fragment {
    final String TAG = "MyRoadPage";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

    RecyclerView roadlist;

    AppConstant appConstant = new AppConstant();

    private FirestoreRecyclerAdapter myRoadAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.road_activity, container,false);
        user = mAuth.getCurrentUser();

        initUi(view);

        return view;
    }

    private void initUi(ViewGroup view) {
        String curUser = user.getUid();

        roadlist = view.findViewById(R.id.roadlist);
        roadlist.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = db.collection("contents")
                .whereEqualTo("contentsType", 0)
                .whereEqualTo("uid", curUser)
                .orderBy("writeDate",Query.Direction.DESCENDING);

       FirestoreRecyclerOptions<Contents> options = new FirestoreRecyclerOptions
               .Builder<Contents>().setQuery(query, Contents.class).build();

       makeMyRoadAdapter(options);
       myRoadAdapter.startListening();
       roadlist.setAdapter(myRoadAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        myRoadAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myRoadAdapter.stopListening();
    }

    private void makeMyRoadAdapter(FirestoreRecyclerOptions<Contents> options) {
        Log.d(TAG,"어댑터 만들기 시작");
        myRoadAdapter = new FirestoreRecyclerAdapter<Contents, MyRoadViewHolder>(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG,"어댑터 시작");
            }

            @Override
            protected void onBindViewHolder(@NonNull MyRoadViewHolder holder, int position, @NonNull Contents model) {
                holder.setItem(model);
            }

            @NonNull
            @Override
            public MyRoadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.road_activity_item, parent,false);
                Log.d(TAG,"어댑터 뷰 받아옴" + view);
                return new MyRoadViewHolder(view);
            }
        };
    }

    private class MyRoadViewHolder extends RecyclerView.ViewHolder{
        TextView myRoadTitle;
        TextView myRoadTime;
        TextView myRoadWriteDate;
        ImageView mainRoadImage;
        String roadTime;
        int hour;
        int min;


        public MyRoadViewHolder(@NonNull View itemView) {
            super(itemView);
            myRoadTitle =itemView.findViewById(R.id.roadtitle);
            myRoadTime = itemView.findViewById(R.id.roadtime);
            myRoadWriteDate = itemView.findViewById(R.id.road_writetime);
            mainRoadImage = itemView.findViewById(R.id.road_thumbnail);

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
    }
}
