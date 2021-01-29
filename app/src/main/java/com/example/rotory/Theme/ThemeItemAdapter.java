package com.example.rotory.Theme;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;
import com.example.rotory.VO.AppConstant;
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

    ArrayList<Tags> themeList;
    public ThemeItemAdapter(ArrayList<Tags> getThemeList,Context context, Display display) {
        user = mAuth.getCurrentUser();
       themeList = getThemeList;
       mContext = context;
       mDisplay = display;

    }

    @NonNull
    @Override
    public themeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_page_card, parent,false);
        return new themeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull themeViewHolder holder, int position) {
        holder.setThemeCard(themeList.get(position));

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


        public themeViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tcardThemeImg =itemView.findViewById(R.id.tcardThemeImg);
            tcardThemeText = itemView.findViewById(R.id.tcardThemeText);
            themeCardView = itemView.findViewById(R.id.themeCardView);
        }

        public void setThemeCard(Tags item){

            Point point = new Point();
            mDisplay.getSize(point);
            themeCardView.setMinimumWidth(point.x/2);

            tcardThemeText.setText(item.getTag());
            appConstant.getThemeImage(item.getTag(), tcardThemeImg, mContext);

        }

    }
}
