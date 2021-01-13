package com.example.rotory.Interface;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Adapter.ContentsAdapter;
import com.example.rotory.Search.SearchResultRoadAdapter;
import com.example.rotory.Search.SearchResultStoryAdapter;
import com.example.rotory.Search.SearchResultStoryItem;


public interface OnContentsItemClickListener {
   public void onItemClick(SearchResultStoryAdapter.ViewHolder holder, View view, int position);
   public void onItemClick(SearchResultRoadAdapter.ViewHolder holder, View view, int position);
}
