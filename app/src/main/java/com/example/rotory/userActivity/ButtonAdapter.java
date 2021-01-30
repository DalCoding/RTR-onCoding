package com.example.rotory.userActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.rotory.R;

public class ButtonAdapter extends FragmentStateAdapter {

    FavoriteActButton favoriteActButton;
    Context mContext;

    public ButtonAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new FavoriteActButton();
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
