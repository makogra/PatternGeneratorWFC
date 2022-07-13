package com.mako.patterngeneratorwfc.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.adapters.SettingsTileSetAdapter;
import com.mako.patterngeneratorwfc.database.TileSetRepository;
import com.mako.patterngeneratorwfc.datamodels.SettingsTileSetViewModel;
import com.mako.patterngeneratorwfc.datamodels.TileSetViewModel;

import kotlin.NotImplementedError;

public class SettingsTileSetFragment extends Fragment {

    private static final int MAX_OUTPUT_HEIGHT = 512;
    private static final int MAX_OUTPUT_WIDTH = 512;
    private static final int MIN_OUTPUT_HEIGHT = 12;
    private static final int MIN_OUTPUT_WIDTH = 12;
    private static final String TAG = "SettingsTileSetFragment";
    private static ViewModelProvider sViewModelProvider;
    private static TileSetRepository sTileSetRepository;
    private ViewModelProvider mViewModelProvider;
    private SettingsTileSetViewModel settingsTileSetViewModel;
    private TileSetViewModel tileSetViewModel;
    private RecyclerView recyclerView;
    private TileSetRepository tileSetRepository;


    public SettingsTileSetFragment() {
    }

    public static SettingsTileSetFragment newInstance(ViewModelProvider viewModelProvider) {
        SettingsTileSetFragment.sViewModelProvider = viewModelProvider;
        return new SettingsTileSetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModelProvider = new ViewModelProvider(requireActivity());
        this.tileSetRepository = TileSetRepository.getInstance(requireActivity().getApplication());
        tileSetViewModel = viewModelProvider.get(TileSetViewModel.class);
        Log.d(TAG, tileSetViewModel.getCurrentId());
        settingsTileSetViewModel = viewModelProvider.get(tileSetViewModel.getCurrentId(), SettingsTileSetViewModel.class);
        settingsTileSetViewModel.setNewInstance(false);
        SettingsTileSetViewModel.setSettingsArr(getResources().getStringArray(R.array.settings_tile_set_arr));
        settingsTileSetViewModel.initMinMaxValue();

        tileSetRepository.getTileSet(tileSetViewModel.getCurrentId()).observe(this, this::updateSettings);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_tile_set, container, false);
        this.recyclerView = view.findViewById(R.id.fragment_settings_tile_set_recycler_view);
        recyclerView.setAdapter(new SettingsTileSetAdapter(settingsTileSetViewModel));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        settingsTileSetViewModel = viewModelProvider.get(tileSetViewModel.getCurrentId(), SettingsTileSetViewModel.class);
        if (settingsTileSetViewModel.isNewInstance()) {
            settingsTileSetViewModel.setNewInstance(false);
            if (SettingsTileSetViewModel.isNotListAlreadyInitialized())
                SettingsTileSetViewModel.setSettingsArr(getResources().getStringArray(R.array.settings_tile_set_arr));
            settingsTileSetViewModel.initMinMaxValue();
        }
        SettingsTileSetAdapter adapter = new SettingsTileSetAdapter(settingsTileSetViewModel);
        this.recyclerView.setAdapter(adapter);
        tileSetRepository.getTileSet(tileSetViewModel.getCurrentId()).observe(this, tileSet -> {
            if (tileSet == null)
                return;
            updateSettings(tileSet);
            adapter.notifyDataSetChanged();
        });
        TextView textView = this.requireView().findViewById(R.id.fragment_settings_tile_set_test_text_view);
        textView.setText(tileSetViewModel.getCurrentId());
    }

    private void updateSettings(TileSet currentTileSet) {
        if (currentTileSet == null)
            return;
        int min, max, average;

        Log.d(TAG, "updateSettings: length: " + currentTileSet.getTileSetLength() + " height: " + currentTileSet.getTileSetHeight());

        for (int i = 0; i < SettingsTileSetViewModel.getSettingsLength(); i++) {
            min = 1;
            switch (i){
                case 0: // pattern size
                    max = Math.min(currentTileSet.getTileSetLength(), currentTileSet.getTileSetHeight());
                    break;
                case 1: // output height
                    max = MAX_OUTPUT_HEIGHT;
                    min = MIN_OUTPUT_HEIGHT;
                    break;
                case 2: // output width
                    max = MAX_OUTPUT_WIDTH;
                    min = MIN_OUTPUT_WIDTH;
                    break;
                case 3: // tiles overlap
                    // max same as in case 0, that mean max = patternSize;
                    max = settingsTileSetViewModel.getMax(0);
                    break;
                default:
                    throw new NotImplementedError("add modules in SettingsTileSetFragment");
            }
            average = (min + max) / 2;

            settingsTileSetViewModel.setMin(min, i);
            settingsTileSetViewModel.setMax(max, i);
            //If current value can be
            if (settingsTileSetViewModel.setValue(settingsTileSetViewModel.getValue(i), i))
                return;
            settingsTileSetViewModel.setValue(average, i);
        }
    }





}