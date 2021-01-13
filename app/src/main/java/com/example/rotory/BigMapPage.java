package com.example.rotory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;

import net.daum.mf.map.api.MapView;

public class BigMapPage extends Fragment {

    public static BigMapPage newInstance() {
        return new BigMapPage();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.big_map_page, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(ViewGroup rootView) {

        //FirebaseFirestore db = FirebaseFirestore.getInstance();

        MapView bigMapView = new MapView(getContext());
        ViewGroup mapView = (ViewGroup) rootView.findViewById(R.id.bigMapLayout);
        mapView.addView(bigMapView);

        Button thisBigMapBtn = rootView.findViewById(R.id.thisBigMapBtn);
        ImageButton bigMapMyLocationBtn = rootView.findViewById(R.id.bigMapMyLocationBtn);


    }
}
