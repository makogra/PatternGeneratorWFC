package com.mako.patterngeneratorwfc.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.ui.SettingsTileSetFragment;
import com.mako.patterngeneratorwfc.ui.TileSetFragment;
import com.mako.patterngeneratorwfc.ui.WFCFragment;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class ScreenSlidePager extends FragmentActivity {

    /**
     * The number of pages (wizard steps) to show
     */
    private static final int NUM_PAGES = 3;
    private static final String TAG = "ScreenSlidePager";
    private static ViewPager2 INSTANCE;

    /**
     * The pager widget, which handles animation and allows swapping
     * horizontally to access previous and next wizard steps.
     */
    private ViewPager2 pager2;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter pagerAdapter;

    public static ViewPager2 getInstance(){
        return INSTANCE;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide_pager);

        // Instance a ViewPager and a PagerAdapter
        pager2 = findViewById(R.id.pager);
        INSTANCE = pager2;
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), getLifecycle(), this);
        pager2.setAdapter(pagerAdapter);
        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager2(pager2);
        // different page swap animation.
        //mPager.setPageTransformer(new DepthPageTransform());
    }

    @Override
    public void onBackPressed() {
        if (pager2.getCurrentItem() == 0){
            // If the user is currently looking at the first step allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            pager2.setCurrentItem(pager2.getCurrentItem() - 1);
        }
    }


    /**
     * A simple pager adapter that represents 5  ScreenSlidePageFragment (in my case TileSetFragment) objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter{

        private ViewModelProvider viewModelProvider;

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Activity activity) {
            super(fragmentManager, lifecycle);
            this.viewModelProvider = new ViewModelProvider((ViewModelStoreOwner) activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return TileSetFragment.newInstance(viewModelProvider);
                case 1:
                    return SettingsTileSetFragment.newInstance(viewModelProvider);
                case 2:
                    return WFCFragment.newInstance(viewModelProvider);
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}