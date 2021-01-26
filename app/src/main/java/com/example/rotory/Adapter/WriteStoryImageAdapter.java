package com.example.rotory.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnContentsItemClickListener;
import com.example.rotory.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WriteStoryImageAdapter extends RecyclerView.Adapter<WriteStoryImageAdapter.writestroyHolder>
        implements OnContentsItemClickListener{
    private final String TAG = "Write_Story";
    public ArrayList<Uri> albumImgList;
    public Context mContext;
    Bitmap bitmap = null;

    OnContentsItemClickListener listener;
    int itemPosition;


    //생성자 정의
    public WriteStoryImageAdapter(ArrayList<Uri> albumImgList, Context mContext, OnContentsItemClickListener listener){
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

//        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            removeItem(position);
//            }
//        });




    }
//

    @Override
    public int getItemCount() {
        return albumImgList.size();

    }

    public void removeItem(int position) {

        albumImgList.remove(position);

    }

    public Uri mainItem(int itemPosition) {

      return albumImgList.get(itemPosition);

    }


    public class writestroyHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public ImageView mainimageView;
        public ImageView titleImage;
        public EditText  comment; // 이미지 코멘트
        public View deleteBtn;
        int itemPosition;


        public writestroyHolder(@NonNull View view) {
            super(view);
            imageView = itemView.findViewById(R.id.writeStroryImageSmall);
            titleImage = imageView.findViewById(R.id.writeStoryMainImageView);


            itemView.setOnClickListener(new View.OnClickListener() {
                Uri uri;

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(writestroyHolder.this,view,position);
                        notifyItemChanged(position) ;
                    }
                }
            });
        }
        public int getItemPosition(){
            int getItemPosition = itemPosition;
            return  getItemPosition;
        }

    }





    public void setOnItemClickListener(OnContentsItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onItemClick(writestroyHolder writestroyHolder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(writestroyHolder, view, position);
        }
    }
//
//    @Override
//    public void onItemDelete(int position) {
//        if (listener !=null) {
//            listener.onItemDelete(position);
//        }
//    }


    public Uri getItem(int position) {
        return albumImgList.get(position);
    }

}
