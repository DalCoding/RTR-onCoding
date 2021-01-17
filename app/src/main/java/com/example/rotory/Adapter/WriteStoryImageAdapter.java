package com.example.rotory.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Imagelist;
import com.example.rotory.Interface.OnContentsItemClickListener;
import com.example.rotory.R;

import java.util.ArrayList;

public class WriteStoryImageAdapter extends RecyclerView.Adapter<com.example.rotory.Adapter.WriteStoryImageAdapter.writestroyHolder> implements OnContentsItemClickListener {
    OnContentsItemClickListener listener;
    private Context context;
    private static ArrayList<Imagelist>items;

    public WriteStoryImageAdapter(Context context, ArrayList<Imagelist> items) {
        this.context= context;
        this.items = items;
    }

    @NonNull
    @Override
    public com.example.rotory.Adapter.WriteStoryImageAdapter.writestroyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.write_story_image_list_item, parent, false);
//
//        writestroyHolder holder = new writestroyHolder(view);
//        return holder;

        return new com.example.rotory.Adapter.WriteStoryImageAdapter.writestroyHolder(LayoutInflater.from(context).inflate(R.layout.write_story_image_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.rotory.Adapter.WriteStoryImageAdapter.writestroyHolder holder, int position) {
        Uri ImageUri= Uri.parse(items.get(position).getSmallimage());
        holder.itemImage.setImageURI(ImageUri);

    }

    @Override
    public int getItemCount() {
        return items.size();
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



    public class writestroyHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private ImageButton mainImage;
        private int prePosition = -1;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();


        public writestroyHolder(@NonNull View View) {
            super(View);
            itemImage = View.findViewById(R.id.writeStroryImageSmall);
            mainImage = View.findViewById(R.id.writeStoryMainImageView);

            itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view ) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(writestroyHolder.this, view, position);
                    }
                    if (selectedItems.get(position)) {
                        // 펼쳐진 Item을 클릭 시
                        selectedItems.delete(position);
                    }
                }
            });
        }

    }
    public static void addItem(Imagelist item) {
        items.add(item);

    }
    public Imagelist getItem(int position) {
       return items.get(position);
    }
}
