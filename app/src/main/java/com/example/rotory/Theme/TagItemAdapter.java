package com.example.rotory.Theme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;

import java.util.ArrayList;

public class TagItemAdapter extends RecyclerView.Adapter<TagItemAdapter.tagItemViewHolder> {
    Context context;
    ArrayList<Tags> tagItemList;

    public TagItemAdapter(Context context, ArrayList<Tags> tagItemList) {
        this.context = context;
        this.tagItemList = tagItemList;
    }


    @NonNull
    @Override
    public tagItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_pick_item, parent,false);
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

    public static class tagItemViewHolder extends  RecyclerView.ViewHolder{
        View view;
        RecyclerView recyclerView;
        Button tagBtn;

        public tagItemViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.activityTagRecyclerView);
            view= itemView;
            tagBtn = itemView.findViewById(R.id.tagBtn);
        }

        public void setTagItems(Tags item){
            tagBtn.setText(item.getTag());

        }
    }
}
