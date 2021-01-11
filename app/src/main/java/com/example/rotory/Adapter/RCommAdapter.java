/*
package com.example.rotory.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Interface.OnUserListener;

public class RCommAdapter extends RecyclerView.Adapter<RoadAdapter.ViewHolder>
        implements OnUserListener {

    ArrayList<RComment> items = new ArrayList<RComment>();

    OnUserListener listener;
    int layoutType = 0;

    @NonNull
    @Override
    public RoadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ViewGroup.getContext());
        View itemView = inflater.inflate(R.layout.comment, viewGroup, false);

        return new ViewHolder(itemView, this, layoutType);
    }

    @Override
    public void onBindViewHolder(@NonNull RoadAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void openUserDocument() {

    }

    @Override
    public void saveUserDocument() {

    }

}
*/
