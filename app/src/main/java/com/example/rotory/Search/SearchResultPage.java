package com.example.rotory.Search;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.MergeAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;
import com.google.api.Distribution;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResultPage extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_page);






    }

}






    /*private void getSearchResultList() {
        Query query = db.collection("contents");

        FirestoreRecyclerOptions<SearchResultStoryItem> items = new FirestoreRecyclerOptions.Builder<SearchResultStoryItem>()
                .setQuery(query, SearchResultStoryItem.class)
                .build();
        searchResultAdapter = new FirestoreRecyclerAdapter<SearchResultStoryItem, ViewHolder>(items) {
            @Override
            public void onBindViewHolder(ViewHolder viewHolder, int position, SearchResultStoryItem model) {
                //viewHolder.searchResultUserLevel.setImageResource(Integer.parseInt(model.getUserLevelImage()));
                //viewHolder.searchResultListImg.setImageResource(Integer.parseInt(model.getTitleImage()));
                //viewHolder.searchResultFavoriteIcon.setImageResource(Integer.parseInt(model.getLikedImage()));
                viewHolder.searchResultUserName.setText(model.getUserName());
                //viewHolder.searchResultListType.setText(Long.toString(model.getContentsType()));
                viewHolder.searchResultListTitle.setText(model.getStoryContent());
                viewHolder.searchResultFavoriteNumber.setText(model.getLiked());
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.search_result_item, viewGroup, false);

                return new ViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        searchResultAdapter.notifyDataSetChanged();
        searchResultList.setAdapter(searchResultAdapter);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        *//*ImageView searchResultUserLevel;
        ImageView searchResultListImg;
        ImageView searchResultFavoriteIcon;*//*
        TextView searchResultUserName;
        //TextView searchResultListType;
        TextView searchResultListTitle;
        TextView searchResultListContents;
        TextView searchResultFavoriteNumber;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            *//*searchResultUserLevel = itemView.findViewById(R.id.searchResultUserLevel);
            searchResultListImg = itemView.findViewById(R.id.searchResultListImg);
            searchResultFavoriteIcon = itemView.findViewById(R.id.searchResultFavoriteIcon);*//*
            searchResultUserName = itemView.findViewById(R.id.searchResultUserName);
            //searchResultListType = itemView.findViewById(R.id.searchResultListType);
            searchResultListTitle = itemView.findViewById(R.id.searchResultListTitle);
            searchResultListContents = itemView.findViewById(R.id.searchResultListContents);
            searchResultFavoriteNumber = itemView.findViewById(R.id.searchResultFavoriteNumber);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        searchResultAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        searchResultAdapter.stopListening();
    }
}*/
