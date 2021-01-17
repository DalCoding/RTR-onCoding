package com.example.rotory.Interface;

import android.view.View;

import com.example.rotory.Search.TagRecyclerAdapter;


public interface OnTagItemClickListener {
    public void onItemClick(TagRecyclerAdapter.TagViewHolder holder, View view, int position);
}
