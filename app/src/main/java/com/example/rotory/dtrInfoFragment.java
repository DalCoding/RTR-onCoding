package com.example.rotory;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class dtrInfoFragment extends Fragment {
    TextView placeName;
    TextView address;
    Button moveStory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.dtr_info_page, container, false);

        placeName = rootView.findViewById(R.id.dinfoSignText);
        address = rootView.findViewById(R.id.dinfoAdText);



        moveStory = rootView.findViewById(R.id.dinfoMoveBtn);
        moveStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
            }
        });


        return rootView;
    }
}
