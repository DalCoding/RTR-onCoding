package com.example.rotory.Search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.R;
import com.example.rotory.Theme.Tags;

import java.util.ArrayList;
import java.util.Collection;


public class SearchTagAdapter extends RecyclerView.Adapter<SearchTagAdapter.tagViewHolder> implements Filterable {

    ArrayList <Tags> tagsList  = new ArrayList<Tags>();
    ArrayList <Tags> tagList = new ArrayList<>();
    Context mContext;


    public SearchTagAdapter(ArrayList<Tags> tagsList, Context context) {
        this.tagsList = tagsList;
        tagList = new ArrayList<>(tagsList);
        mContext = context;
    }

    public void dataSetChanged(ArrayList<Tags> exList) {
        tagList = exList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public tagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent,false);
        return new tagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tagViewHolder holder, int position) {
        holder.setTagText(tagsList.get(position));
        holder.onButtonClick(tagsList.get(position).getTag());

    }


    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    @Override
    public Filter getFilter() {
        return exFilter;
    }

    private Filter exFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Tags> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                //filteredList.addAll(tagList);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Tags item : tagList) {
                    if (item.getTag().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            tagsList.clear();
            tagsList.addAll((Collection<? extends Tags>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class tagViewHolder extends RecyclerView.ViewHolder{
        Button tagText;

        public tagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagText = itemView.findViewById(R.id.tagBtn);
        }
        public void setTagText(Tags item){
            tagText.setText(item.getTag());
        }
        public void onButtonClick(String tag){
            tagText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setIntent(tag);
                }
            });
        }
    }

    private void setIntent(String tag) {
        Intent intent = new Intent(mContext, SearchTagResultPage.class);
        intent.putExtra("tag", tag);
        mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

}
