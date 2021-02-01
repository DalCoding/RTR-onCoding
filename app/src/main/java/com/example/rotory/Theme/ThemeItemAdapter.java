package com.example.rotory.Theme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.LoadRoadItem;
import com.example.rotory.R;
import com.example.rotory.Search.SearchTagResultPage;
import com.example.rotory.VO.AppConstant;
import com.example.rotory.userActivity.MyLikeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ThemeItemAdapter  extends RecyclerView.Adapter<ThemeItemAdapter.themeViewHolder> {
    private final static String TAG = "ThemeItemAdapter";

    Context mContext;
    AppConstant appConstant = new AppConstant();
    View view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;

   RelativeLayout themeCardView;
    ImageView tcardThemeImg;
    TextView tcardThemeText;
    Display mDisplay;
    int randomSize;

    ArrayList<Tags> themeList;
    public ThemeItemAdapter(ArrayList<Tags> getThemeList,Context context, Display display, int size) {
        user = mAuth.getCurrentUser();
       themeList = getThemeList;
       mContext = context;
       mDisplay = display;
       randomSize = size;

    }

    @NonNull
    @Override
    public themeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_page_card, parent,false);
        return new themeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull themeViewHolder holder, int position) {
        holder.setThemeCard(themeList.get(position), position);
        holder.eachItemClick(themeList.get(position));

    }


    @Override
    public int getItemCount() {
        if (themeList != null) {
            return themeList.size();
        }else {
            return 0;
        }
    }

    public class themeViewHolder extends RecyclerView.ViewHolder{
        private View view;
        Button myTagBtn;

        public themeViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tcardThemeImg =itemView.findViewById(R.id.tcardThemeImg);
            tcardThemeText = itemView.findViewById(R.id.tcardThemeText);
            themeCardView = itemView.findViewById(R.id.themeCardView);
            myTagBtn = itemView.findViewById(R.id.myTagBtn);
        }

        public void setThemeCard(Tags item, int position){

            Point point = new Point();
            mDisplay.getSize(point);
            themeCardView.setMinimumWidth(point.x/2);

            if (position > 0) {
                if (position <= (8-randomSize)){
                    myTagBtn.setVisibility(View.INVISIBLE);
                }else {
                    Log.d(TAG, String.valueOf(8-randomSize));
                    myTagBtn.setVisibility(View.VISIBLE);
                    tcardThemeText.setText(item.getTag());
                    appConstant.getThemeImage(item.getTag(), tcardThemeImg, mContext);
                }

            }else {
                tcardThemeText.setText("다람쥐 탐험");
                tcardThemeImg.setImageResource(R.drawable.daramwithcity4);
            }

        }
        public void eachItemClick(Tags item){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent tagIntent = new Intent(mContext, SearchTagResultPage.class);
                    tagIntent.putExtra("tag", item.getTag());
                    tagIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mContext.startActivity(tagIntent);
                }
            });
        }

    }
}
