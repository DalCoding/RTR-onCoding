package com.example.rotory.story;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rotory.R;
import com.example.rotory.VO.Story;
import com.example.rotory.Write_Story;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class StoryFindLocationPage extends AppCompatActivity {
    private static final String TAG = "StoryFindLocationPage";

    ViewPager2 viewPager;
    TabLayout tabs;
    SearchOnMapFragment searchOnMapFragment;
    SearchOnMyRoadFragment searchOnMyRoadFragment;

    ImageButton backBtn;

    public StoryFindLocationPage() { }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_find_location_page);

        createFragment();
        createViewPager();
        setTabLayout();


        backBtn = findViewById(R.id.backImageButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoryFindLocationPage.this, Write_Story.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void createFragment() {
        searchOnMapFragment = new SearchOnMapFragment();
        searchOnMyRoadFragment = new SearchOnMyRoadFragment();
    }

    public void createViewPager() {
        viewPager = (ViewPager2) findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPagerAdapter.addFragment(searchOnMapFragment);
        viewPagerAdapter.addFragment(searchOnMyRoadFragment);

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setUserInputEnabled(false);
    }

    public void setTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.searchTabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
