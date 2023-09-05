package com.mako.patterngeneratorwfc.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mako.patterngeneratorwfc.Config;
import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.Result;
import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.datamodels.ResultViewModel;
import com.mako.patterngeneratorwfc.datamodels.SettingsTileSetViewModel;
import com.mako.patterngeneratorwfc.datamodels.TileSetViewModel;
import com.mako.patterngeneratorwfc.datamodels.WFCViewModel;
import com.mako.patterngeneratorwfc.utiles.BitmapUtilsWFC;
import com.mako.patterngeneratorwfc.utiles.ToastUtilsWFC;
import com.mako.patterngeneratorwfc.wfc.Cell;
import com.mako.patterngeneratorwfc.wfc.WFC;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WFCFragment extends Fragment {

    private static final String TAG = "WFCFragment";
    private static ViewModelProvider sViewModelProvider;
    private ViewModelProvider viewModelProvider;
    private TileSetViewModel tileSetViewModel;
    private SettingsTileSetViewModel settingsTileSetViewModel;
    private WFCViewModel wfcViewModel;
    private ResultViewModel resultViewModel;
    private List<String> inputValueMap;
    private List<Integer[][]> patternList;
    private BitmapUtilsWFC bitmapUtilsWFC;
    private ToastUtilsWFC toastUtilsWFC;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public WFCFragment() {
    }

    public static WFCFragment newInstance(ViewModelProvider viewModelProvider) {
        WFCFragment.sViewModelProvider = viewModelProvider;
        return new WFCFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultViewModel = new ViewModelProvider(requireActivity()).get(ResultViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViewModels();
        initBitMapUtilsWFC();
        //displayResult();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_w_f_c, container, false);
        Button startButton = view.findViewById(R.id.fragment_wfc_start_button);
        startButton.setOnClickListener(v -> {
            //TODO find way to (check and) destroy already running WFC or disable option for starting a new one if one already exists
            toastUtilsWFC.displayWFCStarted();
            new Thread(() -> {
                bitmapUtilsWFC.clearPreviousBitmap();
                int patternSize = settingsTileSetViewModel.getValue(0);
                int outputHeight = settingsTileSetViewModel.getValue(1);
                int outputWidth = settingsTileSetViewModel.getValue(2);
                boolean rotation = settingsTileSetViewModel.getRotation();
                boolean reflection = settingsTileSetViewModel.getReflection();
                TileSet input = tileSetViewModel.getCurrentTileSet();
                Log.d(TAG, "onCreateView: tileSet = " + input);

                bitmapUtilsWFC.createEmptyBitmap(outputHeight, outputWidth);

                WFC wfc = new WFC(input, patternSize, outputHeight, outputWidth, rotation, reflection);
                inputValueMap = wfc.getInputValueMap();
                patternList = wfc.getPatternList();
                wfc.observe(this);
                wfc.run(30);
                if (wfc.isCollapsed()){
                    int[][] outputGrid = wfc.getOutputGrid();
                    //Result result = new Result(outputGrid, patternSize, outputHeight, outputWidth, wfc.getPatternList());

                    //showResult(result, wfc.getInputValueMap());
                    toastUtilsWFC.displayWFCFinished(true);
                    if (Config.IS_LOGGABLE) {
                        Bitmap rBitmap = resultViewModel.getBitmap();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < rBitmap.getHeight(); i++) {
                            for (int j = 0; j < rBitmap.getWidth(); j++) {
                                sb.append(rBitmap.getPixel(j, i));
                            }
                            sb.append("\n");
                        }
                        Log.i(TAG, "onCreateView: resultBitmap = \n" + sb.toString());
                    }
                } else {
                    toastUtilsWFC.displayWFCFinished(false);
                }
            }).start();
        });
        return view;
    }

    private void initBitMapUtilsWFC(){
        if (bitmapUtilsWFC == null){
            bitmapUtilsWFC = new BitmapUtilsWFC(this);
        }
    }


    private void displayResult() {
        if (resultViewModel.getBitmap() == null)
            return;
        bitmapUtilsWFC.attacheScaledBitmap(resultViewModel.getBitmap());
    }

    private void showResult(Result result, List<String> inputValueMap) {
        int[][] patternGrid = result.getOutputGrid();
        int     patternSize = result.getPatternSize(),
                outputHeight = result.getHeight(),
                outputWidth = result.getWidth();
        List<Integer[][]> patternList = result.getPatternList();
        Bitmap outputBitmap;
        resultViewModel.setBitmap(Bitmap.createBitmap(outputWidth, outputHeight, Bitmap.Config.ARGB_8888));
        outputBitmap = resultViewModel.getBitmap();
        bitmapUtilsWFC.makeBitmap(inputValueMap, patternGrid, patternSize, patternList, outputBitmap);
        resultViewModel.setBitmap(outputBitmap);

        bitmapUtilsWFC.attacheScaledBitmap(outputBitmap);

    }



    @Override
    public void onResume() {
        super.onResume();
        updateViewModels();
        testMSettingsViewModel();
        displayResult();
        toastUtilsWFC = new ToastUtilsWFC(this.getContext());

    }

    private void initViewModels() {
        this.viewModelProvider = new ViewModelProvider(requireActivity());
        updateViewModels();
    }

    private void updateViewModels(){
        tileSetViewModel = viewModelProvider.get(TileSetViewModel.class);
        settingsTileSetViewModel = viewModelProvider.get(SettingsTileSetViewModel.class);
        wfcViewModel = viewModelProvider.get(WFCViewModel.class);
    }

    //TODO move this method to tests
    private void testMSettingsViewModel(){
        if (settingsTileSetViewModel == null){
            Log.d(TAG, "testMSettingsViewModel: is null");
            return;
        }
        Log.d(TAG, "testMSettingsViewModel: isn't null");

        if (settingsTileSetViewModel.isNotValueInited())
            Log.d(TAG, "testMSettingsViewModel: isNotValueInited");
        if (settingsTileSetViewModel.isNotMinMaxInnited())
            Log.d(TAG, "testMSettingsViewModel: isNotMinMaxInited");
    }

    public void updateResult(Cell cell, int value) {
        Bitmap resultBitmap = resultViewModel.getBitmap();
        if (resultBitmap == null){
            Log.w(TAG, "updateResult: updating Null bitmap", new NullPointerException());
            return;
        }
        Integer[][] pattern = patternList.get(value);
        int patternSize = pattern.length;
        int row = cell.getRow() * patternSize;
        int col = cell.getCol() * patternSize;
        //TODO change to better naming and be consistent with it, and maybe write it in the documentation
        int height = resultBitmap.getHeight();
        int width = resultBitmap.getWidth();


        int color;
        for (int patternRow = 0; patternRow < patternSize && (patternRow + row) < height; patternRow++) {
            for (int patternCol = 0; patternCol < patternSize && (patternCol + col) < width; patternCol++) {
                // For each item in pattern
                color = bitmapUtilsWFC.getColorOfAPixel(pattern[patternRow][patternCol], inputValueMap);
                resultBitmap.setPixel(patternCol + col, patternRow + row, color);
            }
        }

        executor.execute(() -> bitmapUtilsWFC.attacheScaledBitmapSmooth(resultBitmap.copy(Bitmap.Config.ARGB_8888, false)));
        resultViewModel.setBitmap(resultBitmap);
    }

    public void clearResult(int outputHeight, int outputWidth){
        //bitmapUtilsWFC.resetFrameLayout();
        bitmapUtilsWFC.createEmptyBitmap(outputHeight, outputWidth);
    }
}