package com.example.rotory.Search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnContentsItemClickListener;
import com.example.rotory.R;

import java.util.ArrayList;

public class SearchResultRoadAdapter extends RecyclerView.Adapter<SearchResultRoadAdapter.ViewHolder> {
    ArrayList<SearchResultRoadItem> items = new ArrayList<SearchResultRoadItem>();
    OnContentsItemClickListener contentsListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View contentsItem = inflater.inflate(R.layout.search_result_item_noimg, viewGroup, false);

        return new ViewHolder(contentsItem, contentsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultRoadAdapter.ViewHolder viewHolder, int position) {
        SearchResultRoadItem roadItem = items.get(position);
        viewHolder.setItem(roadItem);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userLevelImage;
        ImageView likedIcon;
        TextView userName;
        TextView road;
        TextView title;
        TextView content;
        TextView liked;

        public ViewHolder(@NonNull View itemView, final OnContentsItemClickListener contentsListener) {
            super(itemView);

            userLevelImage = itemView.findViewById(R.id.searchResultNoImgUserLevel);
            likedIcon = itemView.findViewById(R.id.searchResultNoImgFavoriteIcon);
            userName = itemView.findViewById(R.id.searchResultNoImgUserName);
            road = itemView.findViewById(R.id.searchResultNoImgListType);
            title = itemView.findViewById(R.id.searchResultNoImgListTitle);
            content = itemView.findViewById(R.id.searchResultNoImgListContents);
            liked = itemView.findViewById(R.id.searchResultNoImgFavoriteNumber);

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

        public void setItem(SearchResultRoadItem roadItem) {
            userLevelImage.setImageResource(R.drawable.level1);
            likedIcon.setImageResource(R.drawable.favorite_icon);
            userName.setText(roadItem.getUserName());
            road.setText(roadItem.getRoad());
            title.setText(roadItem.getTitle());
            content.setText(roadItem.getContent());
            liked.setText(roadItem.getLiked());
        }
    }

    public void addItem(SearchResultRoadItem item) {
        items.add(item);
    }

    public void setItems(ArrayList<SearchResultRoadItem> items) {
        this.items = items;
    }

    public SearchResultRoadItem getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, SearchResultRoadItem item) {
        items.set(position, item);
    }

}
