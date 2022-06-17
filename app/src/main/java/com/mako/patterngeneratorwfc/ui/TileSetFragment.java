package com.mako.patterngeneratorwfc.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.TileSetViewModel;

public class TileSetFragment extends Fragment {

    private TileSetViewModel mViewModel;

    public static TileSetFragment newInstance() {
        return new TileSetFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tile_set, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TileSetViewModel.class);
        // TODO: Use the ViewModel
    }

}