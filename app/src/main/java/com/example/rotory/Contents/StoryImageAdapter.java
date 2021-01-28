package com.example.rotory.Contents;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Adapter.WriteStoryImageAdapter;
import com.example.rotory.Interface.OnContentsItemClickListener;
import com.example.rotory.MainPage;
import com.example.rotory.R;

import java.util.ArrayList;

public class StoryImageAdapter extends RecyclerView.Adapter<StoryImageAdapter.writestroyHolder>
        implements OnContentsItemClickListener{
    private final String TAG = "StoryImageAdapter";
    public ArrayList<Bitmap> albumImgList;
    public Context mContext;
    OnContentsItemClickListener listener;


    //생성자 정의
    public StoryImageAdapter(ArrayList<Bitmap> albumImgList, Context mContext, OnContentsItemClickListener listener){
        this.albumImgList = albumImgList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public StoryImageAdapter.writestroyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.write_story_image_list_item,parent,false);
        StoryImageAdapter.writestroyHolder viewHolder = new StoryImageAdapter.writestroyHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoryImageAdapter.writestroyHolder holder, int position) {
        //앨범에서 가져온 이미지 표시
        holder.imageView.setImageBitmap(albumImgList.get(position));
    }
    @Override
    public int getItemCount() {
        return albumImgList.size();

    }

    public Bitmap mainItem(int itemPosition) {

      return albumImgList.get(itemPosition);

    }

    @Override
    public void onItemClick(WriteStoryImageAdapter.writestroyHolder writestroyHolder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(writestroyHolder, view, position);
        }
    }

    @Override
    public void onItemClick(writestroyHolder writestroyHolder, View view, int position) {

    }
    @Override
    public void onItemClick(MainPage.MyAdapter.ViewHolder holder, View view, int position) {

    }


    public class writestroyHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public ImageView titleImage;
        int itemPosition;
       LinearLayout mainImageSelect;

        public writestroyHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.writeStroryImageSmall);
            mainImageSelect = view.findViewById(R.id.mainImageSelect);


            view.setOnClickListener(new View.OnClickListener() {

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


    public Bitmap getItem(int position) {
        return albumImgList.get(position);
    }


}
