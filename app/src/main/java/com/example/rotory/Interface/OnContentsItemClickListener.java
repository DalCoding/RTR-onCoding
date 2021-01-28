package com.example.rotory.Interface;

import android.view.View;

import com.example.rotory.Adapter.WriteStoryImageAdapter;
import com.example.rotory.Contents.StoryImageAdapter;
import com.example.rotory.MainPage;


public interface OnContentsItemClickListener {
   void onItemClick(WriteStoryImageAdapter.writestroyHolder writestroyHolder, View view, int position);
   void onItemClick(StoryImageAdapter.writestroyHolder writestroyHolder, View view, int position);
   void onItemClick(MainPage.MyAdapter.ViewHolder holder, View view, int position);

}
