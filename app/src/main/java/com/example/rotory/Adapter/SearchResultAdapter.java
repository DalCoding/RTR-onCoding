package com.example.rotory.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;
import com.example.rotory.SearchResultItem;
/*import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;*/

public class SearchResultAdapter/* extends FirestoreRecyclerAdapter<SearchResultItem, SearchResultAdapter.ViewHolder> */{

    /*public SearchResultAdapter(@NonNull FirestoreRecyclerOptions<SearchResultItem> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position, @NonNull SearchResultItem model) {
        viewHolder.searchResultUserLevel.setImageResource(model.getUserLevelImage());
        viewHolder.searchResultListImg.setImageResource(model.getTitleImage());
        viewHolder.searchResultFavoriteIcon.setImageResource(model.getLikedImage());
        viewHolder.searchResultUserName.setText(model.getUserName());
        viewHolder.searchResultListType.setText(model.getContentsType());
        viewHolder.searchResultListTitle.setText(model.getStoryContent());
        viewHolder.searchResultFavoriteNumber.setText(model.getLiked());


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_result_item, viewGroup, false);

        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView searchResultUserLevel;
        ImageView searchResultListImg;
        ImageView searchResultFavoriteIcon;
        TextView searchResultUserName;
        TextView searchResultListType;
        TextView searchResultListTitle;
        TextView searchResultListContents;
        TextView searchResultFavoriteNumber;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            searchResultUserLevel = itemView.findViewById(R.id.searchResultUserLevel);
            searchResultListImg = itemView.findViewById(R.id.searchResultListImg);
            searchResultFavoriteIcon = itemView.findViewById(R.id.searchResultFavoriteIcon);
            searchResultUserName = itemView.findViewById(R.id.searchResultUserName);
            searchResultListType = itemView.findViewById(R.id.searchResultListType);
            searchResultListTitle = itemView.findViewById(R.id.searchResultListTitle);
            searchResultListContents = itemView.findViewById(R.id.searchResultListContents);
            searchResultFavoriteNumber = itemView.findViewById(R.id.searchResultFavoriteNumber);
        }
    }*/
}
