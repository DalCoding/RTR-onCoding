package com.example.rotory.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnContentsItemClickListener;
import com.example.rotory.R;

import java.util.ArrayList;

public class WriteStoryImageAdapter extends RecyclerView.Adapter<WriteStoryImageAdapter.writestroyHolder>{

    public ArrayList<Uri> albumImgList;
    public Context mContext;
    Bitmap bitmap = null;
   // OnContentsItemClickListener listener;

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

    }

    @Override
    public int getItemCount() {
        return albumImgList.size();

    }


    public class writestroyHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageView mainimageView;
        public EditText  comment; // 이미지 코멘트
        OnContentsItemClickListener listener;


        public writestroyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.writeStroryImageSmall);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    //uri를 이용해 이미지를 비트맵으로 변환한다.


//                    if (listener != null) {
//                        listener.onItemClick(writestroyHolder.this, view, position);
//                    }try {
//                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
//                    } catch (FileNotFoundException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }


                }
            });
        }

    }



    public Uri getItem(int position) {
        return albumImgList.get(position);
    }
}
