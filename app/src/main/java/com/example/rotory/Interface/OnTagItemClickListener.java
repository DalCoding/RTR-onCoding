package com.example.rotory.Interface;

import android.view.View;

import com.example.rotory.Theme.TagItemAdapter;


public interface OnTagItemClickListener {
    public void onItemClick(TagItemAdapter.tagItemViewHolder holder, View view, int position);

    public void onItemSelected(String tag);

}
