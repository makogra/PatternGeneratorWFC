package com.mako.patterngeneratorwfc.ui;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.TileSetViewModel;

public class TileSetFragment extends Fragment {

    private TileSetViewModel mViewModel;

    private Button test_button;



    public static TileSetFragment newInstance() {
        return new TileSetFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tile_set, container, false);
        test_button = view.findViewById(R.id.fragment_tile_set_test_btn);
        test_button.setOnClickListener(v -> test_button.setText(mViewModel.getString()));
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TileSetViewModel.class);
        // TODO: Use the ViewModel
    }

}