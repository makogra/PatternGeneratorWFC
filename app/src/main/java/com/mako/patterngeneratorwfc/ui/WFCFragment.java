package com.mako.patterngeneratorwfc.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.adapters.ResultAdapter;
import com.mako.patterngeneratorwfc.datamodels.SettingsTileSetViewModel;
import com.mako.patterngeneratorwfc.datamodels.TileSetViewModel;
import com.mako.patterngeneratorwfc.datamodels.WFCViewModel;
import com.mako.patterngeneratorwfc.wfc.WFC;

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
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        initViewModels();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_w_f_c, container, false);
        //updateUI(view);
        Log.d(TAG, "onCreateView() called with: inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
        //testMSettingsViewModel();
        Button startButton = view.findViewById(R.id.fragment_wfc_start_button);
        startButton.setOnClickListener(v -> {
            displayWFCStarted();
            new Thread(() -> {
                String[][] tempInputGrid =  new String[][]{
                        {"G","G","G","G","C","S","S","S"},
                        {"G","G","G","C","C","S","S","S"},
                        {"G","G","G","C","S","S","S","S"},
                        {"G","G","G","C","S","S","S","S"},
                        {"G","G","G","C","S","S","S","S"}
                };

                WFC wfc = new WFC(tempInputGrid, 3, 1, 16, 16);
                wfc.run(30);
                if (wfc.isCollapsed()){
                    int[][] outputGrid = wfc.getOutputGrid();
                    int patternSize = wfc.getPatternSize();
                    int height = wfc.getOutputHeight();
                    int width = wfc.getOutputWidth();
                    Log.d(TAG, "onCreateView: outputGrid.length= " + outputGrid.length + " outputGrid[0].length = " + outputGrid[0].length + " width = " + width + " height = " + height );
                    ResultFragment resultFragment = new ResultFragment(outputGrid, patternSize, height, width);

                    new Handler(requireContext().getMainLooper()).post(() -> showResultFragment(resultFragment));
                }
            }).start();

        });
        return view;
    }

    private void showResultFragment(ResultFragment resultFragment) {
        RecyclerView recyclerView = requireView().findViewById(R.id.fragment_wfc_recycler_view);
        ResultAdapter resultAdapter = new ResultAdapter(resultFragment.getOutputGrid(), resultFragment.getWidth(), resultFragment.getHeight(), resultFragment.getPatternSize());
        recyclerView.setAdapter(resultAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), resultFragment.getWidth()));

        //new Handler(requireContext().getMainLooper()).post(() -> layout.addView(gridView));
    }

    private void displayWFCStarted() {
        Log.d(TAG, "displayWFCStarted: started wfc " + Thread.currentThread().getName());
        Toast.makeText(requireActivity().getApplicationContext(), "WFC started...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateViewModels();
        updateUI(requireView());
        Log.d(TAG, "onResume() called ");
        testMSettingsViewModel();

    }

    private void updateUI(View view){
        TextView idTextView = view.findViewById(R.id.fragment_wfc_tile_set_id_text_view);
        TextView settingsTextView = view.findViewById(R.id.fragment_wfc_settings_text_view);
        TextView resultTextView = view.findViewById(R.id.fragment_wfc_result_text_view);

        testMSettingsViewModel();
        idTextView.setText(mTileSetViewModel.getCurrentId());
        settingsTextView.setText(mSettingsTileSetViewModel.toString());
        resultTextView.setText(mWFCViewModel.getTestConnection());
    }

    private void initViewModels() {
        this.mViewModelProvider = new ViewModelProvider(requireActivity());
        updateViewModels();
    }

    private void updateViewModels(){
        mTileSetViewModel = mViewModelProvider.get(TileSetViewModel.class);
        mSettingsTileSetViewModel = mViewModelProvider.get(SettingsTileSetViewModel.class);
        mWFCViewModel = mViewModelProvider.get(WFCViewModel.class);
    }

    private void testMSettingsViewModel(){
        if (mSettingsTileSetViewModel == null){
            Log.d(TAG, "testMSettingsViewModel: is null");
            return;
        }
        Log.d(TAG, "testMSettingsViewModel: isn't null");

        if (mSettingsTileSetViewModel.isNotValueInited())
            Log.d(TAG, "testMSettingsViewModel: isNotValueInited");
        if (mSettingsTileSetViewModel.isNotMinMaxInnited())
            Log.d(TAG, "testMSettingsViewModel: isNotMinMaxInnited");
    }
}