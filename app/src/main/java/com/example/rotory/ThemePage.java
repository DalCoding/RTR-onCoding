package com.example.rotory;

<<<<<<< HEAD
public class ThemePage {
=======
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rotory.VO.Information;

public class ThemePage extends Fragment {
    Information information;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.theme_page, container, false);
        return rootView;

    }
>>>>>>> fe620317389bf22018f858fd4bcad1993a6e7156
}