package com.example.rotory.Search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;
import com.example.rotory.VO.Tag;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Locale;

class TagRecyclerAdapter extends FirestoreRecyclerAdapter<Tag, TagRecyclerAdapter.TagViewHolder> {

    @Override
    protected void onBindViewHolder(@NonNull TagRecyclerAdapter.TagViewHolder holder, int position, @NonNull Tag model) {
        holder.tagBtn.setText((CharSequence)model.getTag());
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final OnItemClickListener listener;

    TagRecyclerAdapter(FirestoreRecyclerOptions<Tag> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    TagRecyclerAdapter(FirestoreRecyclerOptions<Tag> options) {
        super(options);
        this.listener = null;
    }

    class TagViewHolder extends RecyclerView.ViewHolder {
        Button tagBtn;

        TagViewHolder(View view) {
            super(view);
            view = view;
            tagBtn = view.findViewById(R.id.tagBtn);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);

        return new TagViewHolder(v);
    }

}
