package com.mako.patterngeneratorwfc.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.adapters.SettingsTileSetAdapter;
import com.mako.patterngeneratorwfc.datamodels.SettingsTileSetViewModel;
import com.mako.patterngeneratorwfc.datamodels.TileSetViewModel;

import java.util.Objects;

import kotlin.NotImplementedError;

public class SettingsTileSetFragment extends Fragment {

    private static final int MAX_OUTPUT_HEIGHT = 512;
    private static final int MAX_OUTPUT_WIDTH = 512;
    private SettingsTileSetViewModel settingsTileSetViewModel;
    private TileSetViewModel tileSetViewModel;
    private ViewModelProvider viewModelProvider;
    private RecyclerView recyclerView;


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
        if (settingsTileSetViewModel.isNewInstance()) {
            settingsTileSetViewModel.setNewInstance(false);
            SettingsTileSetViewModel.setSettingsArr(getResources().getStringArray(R.array.settings_tile_set_arr));
            settingsTileSetViewModel.initMinMaxValue();
            initSettings();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings_tile_set, container, false);
        this.recyclerView = view.findViewById(R.id.fragment_settings_tile_set_recycler_view);
        recyclerView.setAdapter(new SettingsTileSetAdapter(settingsTileSetViewModel));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        settingsTileSetViewModel = viewModelProvider.get(Integer.toString(tileSetViewModel.getCurrentId()), SettingsTileSetViewModel.class);
        if (settingsTileSetViewModel.isNewInstance()) {
            settingsTileSetViewModel.setNewInstance(false);
            if (SettingsTileSetViewModel.isNotListAlreadyInitialized())
                SettingsTileSetViewModel.setSettingsArr(getResources().getStringArray(R.array.settings_tile_set_arr));
            settingsTileSetViewModel.initMinMaxValue();
            initSettings();
        }
        this.recyclerView.setAdapter(new SettingsTileSetAdapter(settingsTileSetViewModel));
        TextView textView = this.requireView().findViewById(R.id.fragment_settings_tile_set_test_text_view);
        textView.setText("" + tileSetViewModel.getCurrentId());
    }

    private void initSettings() {
        int min, max, average;
        TileSet currentTileSet = tileSetViewModel.getTileSetList().get(tileSetViewModel.getCurrentIndex());

        for (int i = 0; i < SettingsTileSetViewModel.getSettingsLength(); i++) {
            min = 0;
            switch (i){
                case 0: // pattern size
                    max = Math.min(currentTileSet.getTileSetLength(), currentTileSet.getTileSetHeight());
                    break;
                case 1: // output height
                    max = MAX_OUTPUT_HEIGHT;
                    min = settingsTileSetViewModel.getMin(0);
                    break;
                case 2: // output width
                    max = MAX_OUTPUT_WIDTH;
                    min = settingsTileSetViewModel.getMin(0);
                    break;
                case 3: // tiles overlap
                    // max same as in case 0, that mean max = patternSize;
                    max = settingsTileSetViewModel.getMax(0);
                    min = 1;
                    break;
                default:
                    throw new NotImplementedError("add modules in SettingsTileSetFragment");
            };
            average = (min + max) / 2;

            settingsTileSetViewModel.setMin(min, i);
            settingsTileSetViewModel.setMax(max, i);
            settingsTileSetViewModel.setValue(average, i);
        }
    }


}