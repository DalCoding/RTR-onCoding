package com.example.rotory.story;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rotory.R;
import com.google.android.material.tabs.TabLayout;

public class StoryFindLocationPage extends AppCompatActivity {
    private static final String TAG = "StoryFindLocationPage";

    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SearchOnMapFragment searchOnMapFragment;
    private SearchOnMyRoadFragment searchOnMyRoadFragment;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_find_location_page);

        createFragment();
        createViewPager();
        setTabLayout();

    }

    public void createFragment() {
        searchOnMapFragment = new SearchOnMapFragment();
        searchOnMyRoadFragment = new SearchOnMyRoadFragment();
    }

    public void createViewPager() {
        viewPager = (ViewPager2) findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPagerAdapter.addFragment(searchOnMapFragment);
        viewPagerAdapter.addFragment(searchOnMyRoadFragment);

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setUserInputEnabled(false);
    }

    public void setTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.searchTabs);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
