package com.example.rotory.WriteContents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.Interface.OnTagItemClickListener;
import com.example.rotory.R;
import com.example.rotory.Theme.TagItemAdapter;
import com.example.rotory.Theme.ThemePickPage;
import com.google.firebase.firestore.FirebaseFirestore;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Map;

public class WriteRoadPage extends AppCompatActivity {
    private static final String TAG = "WriteRoadPage";
    private static final int REQUEST_CODE = 4000;

    FirebaseFirestore db;

    MapView mapView;
    ViewGroup mapContainer;
    
    EditText writeRoadTitleEditText;
    EditText writeRoadReviewEditText;
    EditText writeRoadTagEditText;

    ArrayList<String> dtrName;
    ArrayList<String> dtrLatLng;

    MapPOIItem dtrMarker;

    Map<String, Object> roadContents;
    ArrayList<String> tagItems;

    String tagText;
    ThemePickPage themePickPage;

    Button chooseTagBtn;
    Button tagInputBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.write_road_page);

        Intent mapIntent = getIntent();
        mapIntent.getStringArrayListExtra("dtrName");
        mapIntent.getStringArrayListExtra("dtrLatLng");

        /*List<String> mapItem = new ArrayList<>(dtrName);
        mapItem.addAll(dtrLatLng);*/

       /* mapView = new MapView(this);
        mapContainer = findViewById(R.id.writeRoadMap);
        mapContainer.addView(mapView);

        mapView.addPOIItem(dtrMarker);
        mapView.getPOIItems();


        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WriteRoadPage.this, WriteMapPage.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });*/

        tagItems = new ArrayList<>();
        chooseTagBtn = findViewById(R.id.writeRoadChooseTagBtn);
        chooseTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagSelectDialog.listener = new OnTagItemClickListener() {
                    @Override
                    public void onItemClick(TagItemAdapter.tagItemViewHolder holder, View view, int position) {
                    }

                    @Override
                    public void onItemSelected(String tag) {
                        tagText = tag;
                        if (tagItems.contains(tagText)){
                            tagItems.remove(tagText);
                        }else {
                            tagItems.add(tagText);
                        }
                        Log.d(TAG,"들어갔는지 확인" + tagItems);
                    }
                };

                Intent intent = new Intent(getApplicationContext(), TagSelectDialog.class);
                startActivity(intent);

            }
        });

        tagInputBtn=findViewById(R.id.writeRoadTagTextInputBtn);
        tagInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeRoadTagEditText = findViewById(R.id.writeRoadTagEditText);
                String addedCustomTag = writeRoadTagEditText.getText().toString();
                if (addedCustomTag.contains("#")) {
                    tagItems.add(addedCustomTag);
                }else{
                    String withHash = "#"+addedCustomTag;
                    tagItems.add(withHash);
                }

                writeRoadTagEditText.setText("");
                Log.d(TAG,"들어갔는지 확인" + tagItems);

            }
        });

        getTime();
    }

    private void getTime() {
        themePickPage = new ThemePickPage();
    }
}
