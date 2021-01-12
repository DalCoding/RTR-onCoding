package com.example.rotory;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Adapter.ContentsAdapter;
import com.example.rotory.Adapter.SearchResultAdapter;
import com.example.rotory.Interface.OnContentsItemClickListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResultPage extends AppCompatActivity {
    FirebaseFirestore db;
    FirestoreRecyclerAdapter searchResultAdapter;
    RecyclerView searchResultList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_page);

        db =FirebaseFirestore.getInstance();

        searchResultList = findViewById(R.id.searchResultList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        searchResultList.setLayoutManager(layoutManager);

        getSearchResultList();

    }

    private void getSearchResultList() {
        Query query = db.collection("contents");

        FirestoreRecyclerOptions<SearchResultItem> items = new FirestoreRecyclerOptions.Builder<SearchResultItem>()
                .setQuery(query, SearchResultItem.class)
                .build();
        searchResultAdapter = new FirestoreRecyclerAdapter<SearchResultItem, ViewHolder>(items) {
            @Override
            public void onBindViewHolder(ViewHolder viewHolder, int position, SearchResultItem model) {
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
        /*ImageView searchResultUserLevel;
        ImageView searchResultListImg;
        ImageView searchResultFavoriteIcon;*/
        TextView searchResultUserName;
        //TextView searchResultListType;
        TextView searchResultListTitle;
        TextView searchResultListContents;
        TextView searchResultFavoriteNumber;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /*searchResultUserLevel = itemView.findViewById(R.id.searchResultUserLevel);
            searchResultListImg = itemView.findViewById(R.id.searchResultListImg);
            searchResultFavoriteIcon = itemView.findViewById(R.id.searchResultFavoriteIcon);*/
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
}
