package com.example.rotory.Interface;

import android.view.View;

import com.example.rotory.Adapter.WriteStoryImageAdapter;
import com.example.rotory.Contents.StoryImageAdapter;


public interface OnContentsItemClickListener {
   void onItemClick(WriteStoryImageAdapter.writestroyHolder writestroyHolder, View view, int position);
   void onItemClick(StoryImageAdapter.writestroyHolder writestroyHolder, View view, int position);

}
