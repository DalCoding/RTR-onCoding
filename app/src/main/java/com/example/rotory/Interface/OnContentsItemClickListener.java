package com.example.rotory.Interface;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Adapter.ContentsAdapter;
import com.example.rotory.Adapter.WriteStoryImageAdapter;
import com.example.rotory.Search.SearchResultRoadAdapter;
import com.example.rotory.Search.SearchResultStoryAdapter;
import com.example.rotory.Search.SearchResultStoryItem;


public interface OnContentsItemClickListener {
   public void onItemClick(SearchResultStoryAdapter.ViewHolder holder, View view, int position);
   void onItemClick(WriteStoryImageAdapter.writestroyHolder writestroyHolder, View view, int position);

   void onItemClick(SearchResultRoadAdapter.ViewHolder viewHolder, View view, int position);
}
