package com.example.rotory.Contents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;
import com.example.rotory.Theme.Tags;

import java.util.ArrayList;

public class RoadTagAdapter extends RecyclerView.Adapter<RoadTagAdapter.roadTagViewHolder> {
    ArrayList<Tags> roadTags;
    Context mContext;

    public RoadTagAdapter(ArrayList<Tags> roadTags, Context mContext) {
        this.roadTags = roadTags;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public roadTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        return new roadTagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull roadTagViewHolder holder, int position) {
        holder.setRoadTag(roadTags.get(position));

    }

    @Override
    public int getItemCount() {
        return roadTags.size();
    }

    public class roadTagViewHolder extends RecyclerView.ViewHolder {
        Button tagBtn;
        public roadTagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagBtn = itemView.findViewById(R.id.tagBtn);
        }

        public void setRoadTag(Tags tags) {
            tagBtn.setText(tags.getTag());
            tagBtn.setWidth(70);
            tagBtn.setHeight(45);
            tagBtn.setTextSize(14);
            tagBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //서치페이지 해당 태그 검색 페이지로 이동
                }
            });
        }
    }
}
