package com.example.rotory.Theme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;

import java.util.ArrayList;

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.TagListViewHolder> {
    private Context context;
    private ArrayList<TagList> TagList;

    public TagListAdapter(Context context, ArrayList<TagList> tagList) {
        this.context = context;
        TagList = tagList;
    }


    @NonNull
    @Override
    public TagListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_pick_card, parent,false);
        return new TagListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagListViewHolder holder, int position) {
        if (holder instanceof TagListViewHolder){
            holder.recyclerView.setAdapter(new TagItemAdapter(context, TagList.get(position).tags));
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false));
            holder.recyclerView.setHasFixedSize(true);
            holder.tagListTitle.setText(TagList.get(position).tagListTitle);

        }

    }

    @Override
    public int getItemCount() {
        return TagList.size();
    }

    public class TagListViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView recyclerView;
        public TextView tagListTitle;

        public TagListViewHolder(@NonNull View itemView) {
            super(itemView);
            tagListTitle = itemView.findViewById(R.id.tpickGroupText);
        }
        public void setTagListTitle(TagList tagList){
            tagListTitle.setText(tagList.getTagListTitle());
        }
    }
}
