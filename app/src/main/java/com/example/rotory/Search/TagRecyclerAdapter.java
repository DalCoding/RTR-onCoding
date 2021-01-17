package com.example.rotory.Search;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.R;
import com.example.rotory.VO.Tag;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.rotory.VO.AppConstruct.searchCode;
import static com.example.rotory.VO.AppConstruct.themeCode;

public class TagRecyclerAdapter extends FirestoreRecyclerAdapter<Tag, TagRecyclerAdapter.TagViewHolder> implements OnTagItemClickListener {

    OnTagItemClickListener listener;


    @Override
    protected void onBindViewHolder(@NonNull TagRecyclerAdapter.TagViewHolder holder, int position, @NonNull Tag model) {
        holder.tagBtn.setText(model.getTag());
        if (listener != null) {
        }
    }

    @Override
    public void onItemClick(TagViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }


    TagRecyclerAdapter(FirestoreRecyclerOptions<Tag> options, OnTagItemClickListener listener) {
        super(options);
        this.listener = listener;
    }


    public void setOnItemClickListener(OnTagItemClickListener listener) {
        this.listener = listener;
    }


    TagRecyclerAdapter(FirestoreRecyclerOptions<Tag> options) {
        super(options);
        this.listener = null;
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {
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
