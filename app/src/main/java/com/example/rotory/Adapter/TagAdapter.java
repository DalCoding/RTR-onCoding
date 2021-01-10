package com.example.rotory.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.R;
import com.example.rotory.VO.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> implements OnTagItemClickListener, Filterable {
    private List<Tag> tagList;
    private List<Tag> tagListAll;
    private OnTagItemClickListener tagListener;

    public TagAdapter(List<Tag> items) {
        tagList = items;
        tagListAll = new ArrayList<>(items);
    }

    public void dataSetChanged(List<Tag> exList) {
        tagList = exList;
        notifyDataSetChanged();
    }


/*    ArrayList<Tag> items = new ArrayList<Tag>();

    OnTagItemClickListener tagListener;*/


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View tagItem = inflater.inflate(R.layout.tag_item, viewGroup, false);

        return new ViewHolder(tagItem, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Tag item = tagList.get(position);
        viewHolder.setItem(item);

       /* Tag item = items.get(position);
        viewHolder.setItem(item);*/
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    @Override
    public Filter getFilter() {
        return exFilter;
    }

    private Filter exFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Tag> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(tagListAll);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Tag item : tagListAll) {
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
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tagList.clear();
            tagList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public void setOnItemClickListener(OnTagItemClickListener tagListener) {
        this.tagListener = tagListener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (tagListener != null) {
            tagListener.onItemClick(holder, view, position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button tagBtn;

        public ViewHolder(View tagItem, final OnTagItemClickListener tagListener) {
            super(tagItem);

            tagBtn = itemView.findViewById(R.id.tagBtn);

            tagItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (tagListener != null) {
                        tagListener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });

        }

        public void setItem(Tag item) {
            tagBtn.setText(item.getTag());
        }
    }

    public void addItem(Tag item) {
        tagList.add(item);
    }

    public void setItems(ArrayList<Tag> items) {
        this.tagList = items;
    }

    public Tag getItem(int position) {
        return tagList.get(position);
    }

    public void setItem(int position, Tag item) {
        tagList.set(position, item);
    }
}
