package com.mako.patterngeneratorwfc.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.datamodels.SettingsTileSetViewModel;
import com.mako.patterngeneratorwfc.datamodels.TileSetViewModel;
import com.mako.patterngeneratorwfc.datamodels.WFCViewModel;

public class WFCFragment extends Fragment {

    private static final String TAG = "WFCFragment";
    private static ViewModelProvider sViewModelProvider;
    private ViewModelProvider mViewModelProvider;
    private TileSetViewModel mTileSetViewModel;
    private SettingsTileSetViewModel mSettingsTileSetViewModel;
    private WFCViewModel mWFCViewModel;

    public WFCFragment() {
    }

    public static WFCFragment newInstance(ViewModelProvider viewModelProvider) {
        WFCFragment.sViewModelProvider = viewModelProvider;
        return new WFCFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        mTileSetViewModel = viewModelProvider.get(TileSetViewModel.class);
        mSettingsTileSetViewModel = viewModelProvider.get(mTileSetViewModel.getCurrentId(), SettingsTileSetViewModel.class);
        mWFCViewModel = viewModelProvider.get(mTileSetViewModel.getCurrentId(), WFCViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_w_f_c, container, false);

        return view;
    }
}