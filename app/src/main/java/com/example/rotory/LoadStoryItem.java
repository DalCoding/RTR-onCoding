package com.example.rotory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class LoadStoryItem extends AppCompatActivity {

    final static String TAG = "LoadStoryItem";
    ImageButton backImageButton;

    StoryContentsPage storyContentsPage = new StoryContentsPage();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_story_contents);

        onStart();


        getSupportFragmentManager().beginTransaction().replace(R.id.storyContainer, storyContentsPage).commit();
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String documentID = intent.getStringExtra("documentId");
        Log.d(TAG, documentID);

        Bundle pDocumentIdBundle = new Bundle();
        pDocumentIdBundle.putString("storyDocumentId", documentID);
        Log.d(TAG, pDocumentIdBundle.getString("storyDocumentId"));
        storyContentsPage.setArguments(pDocumentIdBundle);
    }
}
