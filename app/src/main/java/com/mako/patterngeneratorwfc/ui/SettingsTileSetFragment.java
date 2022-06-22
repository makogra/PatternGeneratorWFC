package com.mako.patterngeneratorwfc.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.datamodels.SettingsTileSetViewModel;
import com.mako.patterngeneratorwfc.datamodels.TileSetViewModel;

import java.util.Objects;

public class SettingsTileSetFragment extends Fragment {

    private SettingsTileSetViewModel settingsTileSetViewModel;
    private TileSetViewModel tileSetViewModel;
    private View mainView;


    public SettingsTileSetFragment() {
    }

    public static SettingsTileSetFragment newInstance() {
        return new SettingsTileSetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        tileSetViewModel = viewModelProvider.get(TileSetViewModel.class);
        settingsTileSetViewModel = viewModelProvider.get(Integer.toString(tileSetViewModel.getCurrentId()), SettingsTileSetViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings_tile_set, container, false);

        //TextView textView = view.findViewById(R.id.fragment_settings_tile_set_test_text_view);
        //textView.setText("" + tileSetViewModel.getCurrentId());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView textView = this.requireView().findViewById(R.id.fragment_settings_tile_set_test_text_view);
        textView.setText("" + tileSetViewModel.getCurrentId());
    }
}