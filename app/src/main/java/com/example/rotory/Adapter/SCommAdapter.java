package com.example.rotory.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Comment;
import com.example.rotory.Interface.OnCommItemClickListener;
import com.example.rotory.R;

import java.util.ArrayList;

public class SCommAdapter extends RecyclerView.Adapter<SCommAdapter.ViewHolder>
                            implements OnCommItemClickListener {
    ArrayList<Comment> items = new ArrayList<Comment>();

    OnCommItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.comment, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Comment item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
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
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //ImageView commLevelImg;
        TextView commUsernameText;
        TextView commTimeText;
        TextView commConText;
        TextView commReportText;

        public ViewHolder(View itemView, final OnCommItemClickListener listener) {
            super(itemView);

            //commLevelImg = itemView.findViewById(R.id.commLevelImage);
            commUsernameText = itemView.findViewById(R.id.commUsernameText);
            commTimeText = itemView.findViewById(R.id.commTimeText);
            commConText = itemView.findViewById(R.id.commConText);
            commReportText = itemView.findViewById(R.id.commReportText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
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
        }
    }
}
