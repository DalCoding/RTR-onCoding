package com.example.rotory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotory.Adapter.RCommAdapter;
import com.example.rotory.Interface.OnContentsListener;
import com.example.rotory.Interface.OnDeleteListener;
import com.example.rotory.Interface.OnUserActItemClickListener;
import com.example.rotory.Interface.OnUserListener;

public class RoadContentsPage extends AppCompatActivity {

    Button rcontentsCommBtn;
    ImageView rcontentsLinkImg;
    ImageView rcontentsScrapImg;
    ImageView rcontentsHeartImg;
    TextView rcontentsTitleText;
    ImageView rcontentsLevelImg;
    TextView rcontentsUsernameText;
    ImageView rcontentsStarImg;
    RecyclerView rCommRView;
    RCommAdapter adapter;

    Context context;
    OnUserActItemClickListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.road_contents_page);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.road_contents_page, container, false);

        initUI(rootView);
        return rootView;
    }

    private void initUI(ViewGroup rootView) {

        rCommRView = rootView.findViewById(R.id.rCommRView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rCommRView.setLayoutManager(layoutManager);

        adapter = new RCommAdapter();

        rCommRView.setAdapter(adapter);

    }

}
