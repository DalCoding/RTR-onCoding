package com.example.rotory.WriteContents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;
import com.example.rotory.Theme.Tags;

import java.util.ArrayList;

public class WriteRoadTagAdapter extends RecyclerView.Adapter<WriteRoadTagAdapter.tagItemViewHolder> {
    private final static String TAG = "WriteStoryTagAdapter";
    public Context context;
    public ArrayList<Tags> tagItemList = new ArrayList<>();
    View view;

    public WriteRoadTagAdapter(Context context) {
        this.context = context;
    }

    public WriteRoadTagAdapter(Context context, ArrayList<Tags> tagItemList) {
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
        Tags item = tagItemList.get(position);
        holder.setTagItems(tagItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return tagItemList.size();
    }

    public void addItem(Tags item){
        tagItemList.add(item);
    }

    public ArrayList<Tags> getItemList(){
        return tagItemList;
    }

    public void removeItem(ArrayList<Tags> tagItemList){
        tagItemList.removeAll(tagItemList);
    }


    public Tags getItem(int position){
        return tagItemList.get(position);
    }

    public class tagItemViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tagBtn;



        public tagItemViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            tagBtn = itemView.findViewById(R.id.tagText);
        }

        public void setTagItems(Tags item) {
            tagBtn.setText(item.getTag());

        }


        public Tags getItem(int position) {
            return tagItemList.get(position);
        }

    }
}
