package com.example.rotory.Interface;

import android.view.View;

import com.example.rotory.Theme.TagItemAdapter;


public interface OnTagItemClickListener {
    public void onItemClick(TagItemAdapter.tagItemViewHolder holder, View view, int position);
<<<<<<< HEAD
    public void onItemSelected(String tag);
=======

    void onItemSelected(String toString);
>>>>>>> origin/master
}
