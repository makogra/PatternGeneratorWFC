package com.mako.patterngeneratorwfc.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.datamodels.SettingsTileSetViewModel;
import com.mako.patterngeneratorwfc.datamodels.TileSetViewModel;

import java.util.Objects;

public class SettingsTileSetFragment extends Fragment {

    private SettingsTileSetViewModel settingsTileSetViewModel;
    private TileSetViewModel tileSetViewModel;
    private View mainView;
    private ViewModelProvider viewModelProvider;


    public SettingsTileSetFragment() {
    }

    public static SettingsTileSetFragment newInstance() {
        return new SettingsTileSetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModelProvider = new ViewModelProvider(requireActivity());
        tileSetViewModel = viewModelProvider.get(TileSetViewModel.class);
        System.out.println(tileSetViewModel.getCurrentId());
        settingsTileSetViewModel = viewModelProvider.get(Integer.toString(tileSetViewModel.getCurrentId()), SettingsTileSetViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings_tile_set, container, false);
        Button sampleIncrease = view.findViewById(R.id.settings_tile_set_test_increase_btn);
        sampleIncrease.setOnClickListener(v -> {
            settingsTileSetViewModel.increaseSample();
            updateSample();
        });
        Button sampleDecrease = view.findViewById(R.id.settings_tile_set_test_decrease_btn);
        sampleDecrease.setOnClickListener(v -> {
            settingsTileSetViewModel.decreaseSample();
            updateSample();
        });
        //TextView textView = view.findViewById(R.id.fragment_settings_tile_set_test_text_view);
        //textView.setText("" + tileSetViewModel.getCurrentId());
        return view;
    }

    private void updateSample(){
        TextView sample = requireView().findViewById(R.id.test_number);
        sample.setText(Integer.toString(settingsTileSetViewModel.getSample()));
    }

    @Override
    public void onResume() {
        super.onResume();
        settingsTileSetViewModel = viewModelProvider.get(Integer.toString(tileSetViewModel.getCurrentId()), SettingsTileSetViewModel.class);
        TextView textView = this.requireView().findViewById(R.id.fragment_settings_tile_set_test_text_view);
        textView.setText("" + tileSetViewModel.getCurrentId());
        updateSample();
    }
}