package com.example.rotory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rotory.kakao.Document;
import com.example.rotory.kakao.IntentKey;

public class dtrInfo extends AppCompatActivity {
    TextView placeName;
    TextView address;
    Button moveStory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtr_info_page);

        placeName = findViewById(R.id.dinfoSignText);
        address = findViewById(R.id.dinfoAdText);
        moveStory = findViewById(R.id.dinfoMoveBtn);

        Intent dtrIntent = getIntent();
        Document document = dtrIntent.getParcelableExtra(IntentKey.MARKER_INFO);
        placeName.setText(document.getPlaceName());
        address.setText(document.getAddressName());

        moveStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 해당 도토리 DB 가져와서 이야기로 이동
                //Intent intent = new Intent(dtrInfo.this, StoryContentsPage.class);
            }
        });
    }
}
