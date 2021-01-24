package com.example.rotory.WriteContents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.R;
import com.example.rotory.Theme.Tags;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WriteStoryTagAdapter extends RecyclerView.Adapter<WriteStoryTagAdapter.tagItemViewHolder> {
    private final static String TAG = "WriteStoryTagAdapter";
    public Context context;
    public ArrayList<Tags> tagItemList;
    View view;

    public WriteStoryTagAdapter(Context context, ArrayList<Tags> tagItemList) {
        this.context = context;
        this.tagItemList = tagItemList;
    }

    @NonNull
    @Override
    public tagItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_pick_item, parent, false);
        return new tagItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tagItemViewHolder holder, int position) {
        holder.setTagItems(tagItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return tagItemList.size();
    }


    public class tagItemViewHolder extends RecyclerView.ViewHolder {
        View view;
        RecyclerView recyclerView;
        TextView tagBtn;
        Map<String, Object> tagList = new HashMap<String, Object>(5);
        int isSelected = 0;


        public tagItemViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            tagBtn = itemView.findViewById(R.id.tagText);
        }

        public void setTagItems(Tags item) {
            tagBtn.setText(item.getTag());
            //isInList(item.getTag());

        }


        public void onBind() {
            String tagText = tagBtn.getText().toString();

            tagBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });
        }


        public Tags getItem(int position) {
            return tagItemList.get(position);
        }

    }
}
