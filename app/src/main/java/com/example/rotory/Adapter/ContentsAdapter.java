package com.example.rotory.Adapter;

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

import java.util.ArrayList;
import java.util.List;

public class ContentsAdapter extends RecyclerView.Adapter<ContentsAdapter.ViewHolder> {
    public static Object SearchResult;
    ArrayList<SearchResult> items = new ArrayList<SearchResult>();
    OnContentsItemClickListener contentsListener;

    public ContentsAdapter(ArrayList<SearchResult> items) {
        this.items = items;
    }

    public void dataSetChanged(List<SearchResult> items) {
        items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View searchResultItem = inflater.inflate(R.layout.search_result_item, viewGroup, false);

        return new ViewHolder(searchResultItem, contentsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        SearchResult item  = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void setOnItemClickListener(OnContentsItemClickListener contentsListener) {
        this.contentsListener = contentsListener;
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

        public ViewHolder(@NonNull View searchResultItem, final OnContentsItemClickListener contentsListener) {
            super(searchResultItem);

            searchResultUserLevel = itemView.findViewById(R.id.searchResultUserLevel);
            searchResultListImg = itemView.findViewById(R.id.searchResultListImg);
            searchResultFavoriteIcon = itemView.findViewById(R.id.searchResultFavoriteIcon);
            searchResultUserName = itemView.findViewById(R.id.searchResultUserName);
            searchResultListType = itemView.findViewById(R.id.searchResultListType);
            searchResultListTitle = itemView.findViewById(R.id.searchResultListTitle);
            searchResultListContents = itemView.findViewById(R.id.searchResultListContents);
            searchResultFavoriteNumber = itemView.findViewById(R.id.searchResultFavoriteNumber);


            searchResultItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (contentsListener != null) {
                        contentsListener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(SearchResult item) {
            searchResultUserLevel.setImageResource(R.drawable.level1);
            searchResultListImg.setImageResource(R.drawable.bridge);
            searchResultFavoriteIcon.setImageResource(R.drawable.favorite_icon);
            searchResultUserName.setText(item.getUserName());
            searchResultListType.setText(item.getContentsType());
            searchResultListTitle.setText(item.getRoadTitle());
            searchResultListTitle.setText(item.getStoryTitle());
            searchResultListContents.setText(item.getStoryContent());
            searchResultListContents.setText((CharSequence) item.getDtrName());
            searchResultFavoriteNumber.setText((CharSequence) item.getLiked());
        }
    }

    public void addItem(SearchResult item) {
        items.add(item);
    }

    public void setItems(ArrayList<SearchResult> items) {
        this.items = items;
    }

    public SearchResult getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, SearchResult item) {
        items.set(position, item);
    }


    public class SearchResult {
        String contentsType;
        String	roadTitle;
        ArrayList<String>	dtrName;
        String	storyTitle;
        String	storyAddress;
        String	titleImage;
        String  storyContent;
        ArrayList<String> liked;
        int	userLevel;
        String	userName;

        public SearchResult(String contentsType, String roadTitle, ArrayList<String> dtrName, String storyTitle,
                            String storyAddress, String titleImage, String storyContent, ArrayList<String> liked,
                            int userLevel, String userName) {
            this.contentsType = contentsType;
            this.roadTitle = roadTitle;
            this.dtrName = dtrName;
            this.storyTitle = storyTitle;
            this.storyAddress = storyAddress;
            this.titleImage = titleImage;
            this.storyContent = storyContent;
            this.liked = liked;
            this.userLevel = userLevel;
            this.userName = userName;
        }

        public String getContentsType() {
            return contentsType;
        }

        public void setContentsType(String contentsType) {
            this.contentsType = contentsType;
        }

        public String getRoadTitle() {
            return roadTitle;
        }

        public void setRoadTitle(String roadTitle) {
            this.roadTitle = roadTitle;
        }

        public ArrayList<String> getDtrName() {
            return dtrName;
        }

        public void setDtrName(ArrayList<String> dtrName) {
            this.dtrName = dtrName;
        }

        public String getStoryTitle() {
            return storyTitle;
        }

        public void setStoryTitle(String storyTitle) {
            this.storyTitle = storyTitle;
        }

        public String getStoryAddress() {
            return storyAddress;
        }

        public void setStoryAddress(String storyAddress) {
            this.storyAddress = storyAddress;
        }

        public String getTitleImage() {
            return titleImage;
        }

        public void setTitleImage(String titleImage) {
            this.titleImage = titleImage;
        }

        public String getStoryContent() {
            return storyContent;
        }

        public void setStoryContent(String storyContent) {
            this.storyContent = storyContent;
        }

        public ArrayList<String> getLiked() {
            return liked;
        }

        public void setLiked(ArrayList<String> liked) {
            this.liked = liked;
        }

        public int getUserLevel() {
            return userLevel;
        }

        public void setUserLevel(int userLevel) {
            this.userLevel = userLevel;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

}
