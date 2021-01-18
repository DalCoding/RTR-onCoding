package com.example.rotory;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class LoadStoryItem extends AppCompatActivity {
    ImageButton backImageButton;

    StoryContentsPage storyContentsPage = new StoryContentsPage();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_story_contents);

        getSupportFragmentManager().beginTransaction().replace(R.id.storyContainer, storyContentsPage).commit();
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
