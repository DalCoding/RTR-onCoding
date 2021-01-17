package com.example.rotory.Interface;

import android.view.View;

import com.example.rotory.Adapter.WriteStoryImageAdapter;


public interface OnContentsItemClickListener {
   void onItemClick(WriteStoryImageAdapter.writestroyHolder writestroyHolder, View view, int position);
}
