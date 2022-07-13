package com.mako.patterngeneratorwfc.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.ui.SettingsTileSetFragment;
import com.mako.patterngeneratorwfc.ui.TileSetFragment;

public class ScreenSlidePager extends FragmentActivity {

    /**
     * The number of pages (wizard steps) to show
     */
    private static final int NUM_PAGES = 3;
    private static final String TAG = "ScreenSlidePager";

    /**
     * The pager widget, which handles animation and allows swapping
     * horizontally to access previous and next wizard steps.
     */
    private ViewPager2 mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter pagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide_pager);

        // Instance a ViewPager and a PagerAdapter
        mPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), getLifecycle());
        mPager.setAdapter(pagerAdapter);
        // different page swap animation.
        //mPager.setPageTransformer(new DepthPageTransform());
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0){
            // If the user is currently looking at the first step allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    /**
     * A simple pager adapter that represents 5  ScreenSlidePageFragment (in my case TileSetFragment) objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter{

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return TileSetFragment.newInstance();
                    //TODO Add next pages
                case 1:
                    return SettingsTileSetFragment.newInstance();
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}