package com.example.rotory.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Comment;
import com.example.rotory.Interface.OnCommItemClickListener;
import com.example.rotory.R;

import java.util.ArrayList;

public class SCommAdapter extends RecyclerView.Adapter<SCommAdapter.CommentViewHolder>{

    private ArrayList<Comment> arrayList;
    private Context context;
    ArrayList<Comment> items = new ArrayList<Comment>();
    OnCommItemClickListener listener;

    public SCommAdapter(ArrayList<Comment> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment, viewGroup, false);
        CommentViewHolder holder = new CommentViewHolder(view,listener);
        return holder;
        /* LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.comment, viewGroup, false);

        return new CommentViewHolder(itemView, this);*/
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
/*        Glide.with(holder.itemView)
                .load(arrayList.get(position).getLevelImage())
                .into(holder.commLevelImg);
        holder.commUsernameText.setText(arrayList.get(position).getUsername());
        holder.commTimeText.setText(arrayList.get(position).getTimeText());
        holder.commConText.setText(arrayList.get(position).getConText());*/
        /*Comment item = items.get(position);
        viewHolder.setItem(item);*/
    }

/*    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Comment item) {
        items.add(item);
    }

    public void setItem(ArrayList<Comment> items) {
        this.items = items;
    }

    public Comment getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Comment item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnCommItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(CommentViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }*/

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView commLevelImg;
        TextView commUsernameText;
        TextView commTimeText;
        TextView commConText;
        TextView commReportText;

        public CommentViewHolder(View itemView, final OnCommItemClickListener listener) {
            super(itemView);
            //this.commLevelImg = itemView.findViewById(R.id.commLevelImage);
            this.commUsernameText = itemView.findViewById(R.id.commUsernameText);
            this.commTimeText = itemView.findViewById(R.id.commTimeText);
            this.commConText = itemView.findViewById(R.id.commConText);
            this.commReportText = itemView.findViewById(R.id.commReportText);
        }
    }
/*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(CommentViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Comment item) {
            //commLevelImg.setImage(item.getLevelImg());
            commUsernameText.setText(item.getUserName());
            commTimeText.setText(item.getTime());
            commConText.setText(item.getCommCon());
            commReportText.setText(item.getReport());
        }*/
        @Override
        public int getItemCount() {
            return(arrayList != null ? arrayList.size() : 0);
        }
    }

