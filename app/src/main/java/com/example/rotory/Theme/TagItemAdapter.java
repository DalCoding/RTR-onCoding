package com.example.rotory.Theme;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TagItemAdapter extends RecyclerView.Adapter<TagItemAdapter.tagItemViewHolder>
        implements OnTagItemClickListener {
    private final static String TAG = "TagItemAdapter";
    public Context context;
    public ArrayList<Tags> tagItemList;
    OnTagItemClickListener listener;
    TextView  tagListSize;

    public TagItemAdapter(Context context, ArrayList<Tags> tagItemList,
                          OnTagItemClickListener listener, TextView tagListSize) {
        this.context = context;
        this.tagItemList = tagItemList;
        this.tagListSize = tagListSize;
        //this.listener = listener;
    }

    @NonNull
    @Override
    public tagItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_pick_item, parent,false);
        return new tagItemViewHolder(view/*,this::onItemClick*/);
    }

    @Override
    public void onBindViewHolder(@NonNull tagItemViewHolder holder, int position) {
        holder.setTagItems(tagItemList.get(position));
        holder.onBind();
    }

    @Override
    public int getItemCount() {
        return tagItemList.size();
    }




    public class tagItemViewHolder extends  RecyclerView.ViewHolder {
        View view;
        RecyclerView recyclerView;
        TextView tagBtn;
        Map<String, String> tagList = new HashMap<String, String>(5);
        int isSelected = 0;


        public tagItemViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.activityTagRecyclerView);
            view= itemView;
            tagBtn = itemView.findViewById(R.id.tagText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =getAdapterPosition();
                    if (listener != null){
                        listener.onItemClick(tagItemViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setTagItems(Tags item){
            tagBtn.setText(item.getTag());

        }

        public void onBind(){
            String tagText = tagBtn.getText().toString();
                    tagBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        /*    if (isSelected >= 5) {
                                Toast.makeText(context, "선택가능한 태그 개수는 5개 입니다.", Toast.LENGTH_SHORT).show();
                            } else {*/
                            if (tagListSize.getText().toString().equals("5")){
                                if (tagBtn.getCurrentTextColor() == Color.MAGENTA){
                                    removeFromList();
                                }else{
                                    Toast.makeText(context, "선택가능한 태그 개수는 5개 입니다.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (tagBtn.getCurrentTextColor() == Color.MAGENTA) {
                                    removeFromList();
                                } else if (tagBtn.getCurrentTextColor() == Color.BLACK) {
                                    tagBtn.setTextColor(Color.MAGENTA);
                                    tagBtn.setTextSize(16);
                                    Log.d(TAG, tagBtn.getText().toString() + " 선택됨");
                                    Toast.makeText(context, "태그 선택 : " + tagBtn.getText().toString(), Toast.LENGTH_SHORT).show();

                                    int tagSize = Integer.parseInt(tagListSize.getText().toString());
                                    String chantedSize = String.valueOf(tagSize + 1);
                                    tagListSize.setText(chantedSize);
                                    Log.d(TAG, "선택 개수 변화 확인" + tagSize + " =>" + tagListSize.getText().toString());
                                }
                            }
                        }
                    });


            Log.d(TAG,"선택 개수 확인" + tagListSize.getText().toString());

        }

        private void removeFromList() {
            String tagText = tagBtn.getText().toString();
            tagBtn.setTextColor(Color.BLACK);
            tagBtn.setTextSize(14);
            Log.d(TAG, tagText + " 선택 취소됨" + tagList.size() + "/5");
            Log.d(TAG, "선택 개수 확인" + isSelected);
            int tagSize = Integer.parseInt(tagListSize.getText().toString());
            tagListSize.setText(String.valueOf(tagSize - 1));
            Log.d(TAG, "선택 개수 변화 확인" + tagSize + " =>" + tagListSize.getText().toString());
        }
    }
    public void setOnTagItemClickListener(OnTagItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(tagItemViewHolder holder, View view, int position) {
        if (listener != null){
            listener.onItemClick(holder, view, position);
        }
    }
    public Tags getItem(int position){
        return tagItemList.get(position);
    }
}
