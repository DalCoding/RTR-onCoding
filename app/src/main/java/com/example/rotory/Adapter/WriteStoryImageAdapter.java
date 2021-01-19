package com.example.rotory.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;

import java.util.ArrayList;

public class WriteStoryImageAdapter extends RecyclerView.Adapter<WriteStoryImageAdapter.writestroyHolder> {

        public ArrayList<Uri> albumImgList;
        public Context mContext;

        //생성자 정의
        public WriteStoryImageAdapter(ArrayList<Uri> albumImgList,Context mContext){
            this.albumImgList = albumImgList;
            this.mContext = mContext;
        }

        @NonNull
        @Override
        public WriteStoryImageAdapter.writestroyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.write_story_image_list_item,parent,false);
            WriteStoryImageAdapter.writestroyHolder viewHolder = new WriteStoryImageAdapter.writestroyHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull WriteStoryImageAdapter.writestroyHolder holder, int position) {
            //앨범에서 가져온 이미지 표시
            holder.imageView.setImageURI(albumImgList.get(position));
        }

        @Override
        public int getItemCount() {
            return albumImgList.size();
        }

        public class writestroyHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;

            public writestroyHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.writeStroryImageSmall);
            }
        }

}
