package com.example.rotory.Search;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnContentsItemClickListener;
import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.R;
import com.example.rotory.VO.Contents;

import java.util.ArrayList;

public class SearchResultStoryAdapter extends RecyclerView.Adapter<SearchResultStoryAdapter.ViewHolder> {
    ArrayList<SearchResultStoryItem> items = new ArrayList<SearchResultStoryItem>();
    OnContentsItemClickListener contentsListener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View contentsItem = inflater.inflate(R.layout.search_result_item, viewGroup, false);

        return new ViewHolder(contentsItem, contentsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        SearchResultStoryItem item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userLevelImage;
        ImageView titleImage;
        ImageView likedIcon;
        TextView userName;
        TextView story;
        TextView title;
        TextView content;
        TextView liked;

        public ViewHolder(@NonNull View itemView, final OnContentsItemClickListener contentsListener) {
            super(itemView);

            userLevelImage = itemView.findViewById(R.id.searchResultUserLevel);
            titleImage = itemView.findViewById(R.id.searchResultListImg);
            likedIcon = itemView.findViewById(R.id.searchResultFavoriteIcon);
            userName = itemView.findViewById(R.id.searchResultUserName);
            story = itemView.findViewById(R.id.searchResultListType);
            title = itemView.findViewById(R.id.searchResultListTitle);
            content = itemView.findViewById(R.id.searchResultListContents);
            liked = itemView.findViewById(R.id.searchResultFavoriteNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (contentsListener != null) {
                        contentsListener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(SearchResultStoryItem item) {
            userLevelImage.setImageResource(R.drawable.level1);
            titleImage.setImageResource(R.drawable.bridge);
            likedIcon.setImageResource(R.drawable.favorite_icon);
            userName.setText(item.getUserName());
            story.setText(item.getStory());
            title.setText(item.getTitle());
            content.setText(item.getContent());
            liked.setText(item.getLiked());
        }
    }

    public void addItem(SearchResultStoryItem item) {
        items.add(item);
    }

    public void setItems(ArrayList<SearchResultStoryItem> items) {
        this.items = items;
    }

    public SearchResultStoryItem getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, SearchResultStoryItem item) {
        items.set(position, item);
    }
}
