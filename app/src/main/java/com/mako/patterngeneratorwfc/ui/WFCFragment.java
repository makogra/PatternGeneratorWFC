package com.mako.patterngeneratorwfc.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

public class WFCFragment extends Fragment {

    private static final String TAG = "WFCFragment";
    private static ViewModelProvider sViewModelProvider;
    private ViewModelProvider viewModelProvider;
    private TileSetViewModel tileSetViewModel;
    private SettingsTileSetViewModel settingsTileSetViewModel;
    private WFCViewModel wfcViewModel;
    private ResultViewModel resultViewModel;
    private Bitmap resultBitmap = null;
    private List<String> inputValueMap;
    private List<Integer[][]> patternList;
    private BitmapUtilsWFC bitmapUtilsWFC;
    private ToastUtilsWFC toastUtilsWFC;

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
        Log.d(TAG, "onCreateView() called with: inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
        Button startButton = view.findViewById(R.id.fragment_wfc_start_button);
        startButton.setOnClickListener(v -> {
            toastUtilsWFC.displayWFCStarted();
            new Thread(() -> {
                bitmapUtilsWFC.clearPreviousBitmap();
                int patternSize = settingsTileSetViewModel.getValue(0);
                int outputHeight = settingsTileSetViewModel.getValue(1);
                int outputWidth = settingsTileSetViewModel.getValue(2);
                int tilesOverlap = settingsTileSetViewModel.getValue(3);
                boolean rotation = settingsTileSetViewModel.getRotation();
                boolean reflection = settingsTileSetViewModel.getReflection();
                TileSet input = tileSetViewModel.getCurrentTileSet();
                Log.d(TAG, "onCreateView: tileSet = " + input);
                String[][] tempInputGrid =  new String[][]{
                        {"G","G","G","G","C","S","S","S"},
                        {"G","G","G","C","C","S","S","S"},
                        {"G","G","G","C","S","S","S","S"},
                        {"G","G","G","C","S","S","S","S"},
                        {"G","G","G","C","S","S","S","S"}
                };

                //TODO create new bitmap
                bitmapUtilsWFC.createEmptyBitmap(outputHeight, outputWidth);

                WFC wfc = new WFC(input, patternSize, tilesOverlap, outputHeight, outputWidth, rotation, reflection);
                inputValueMap = wfc.getInputValueMap();
                patternList = wfc.getPatternList();
                wfc.observe(this);
                wfc.run(30);
                if (wfc.isCollapsed()){
                    int[][] outputGrid = wfc.getOutputGrid();
                    Result result = new Result(outputGrid, patternSize, outputHeight, outputWidth, tilesOverlap, wfc.getPatternList());

                    //showResult(result, wfc.getInputValueMap());
                    //TODO display finish
                    //TODO if result isn't visible display toast about finish
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
                outputWidth = result.getWidth(),
                overlap = result.getOverlap();
        List<Integer[][]> patternList = result.getPatternList();
        Bitmap outputBitmap;
        resultViewModel.setBitmap(Bitmap.createBitmap(outputWidth, outputHeight, Bitmap.Config.ARGB_8888));
        outputBitmap = resultViewModel.getBitmap();
        bitmapUtilsWFC.makeBitmap(inputValueMap, patternGrid, patternSize, overlap, patternList, outputBitmap);
        resultViewModel.setBitmap(outputBitmap);

        bitmapUtilsWFC.attacheScaledBitmap(outputBitmap);

    }



    @Override
    public void onResume() {
        super.onResume();
        updateViewModels();
        Log.d(TAG, "onResume() called ");
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
        if (resultBitmap == null){
            Log.w(TAG, "updateResult: updating Null bitmap", new NullPointerException());
        }
        //TODO
        int row = cell.getRow();
        int col = cell.getCol();
        int height = settingsTileSetViewModel.getValue(1);
        int width = settingsTileSetViewModel.getValue(2);
        int xRatio = resultBitmap.getWidth()/width;
        int yRatio = resultBitmap.getHeight()/height;
        Integer[][] pattern = patternList.get(value);
        //int color = getColorOfAPixel(value, mInputValueMap);
        ImageView imageView = requireView().findViewById(R.id.fragment_wfc_image_view);

        imageView.post(() -> {
            int color;
            for (int patternRow = 0; patternRow < pattern.length; patternRow++) {
                for (int patternCol = 0; patternCol < pattern[0].length; patternCol++) {
                    // For each item in pattern
                    color = bitmapUtilsWFC.getColorOfAPixel(pattern[patternRow][patternCol], inputValueMap);
                    for (int x = (row + patternRow) * xRatio; x < (row + patternRow + 1) * xRatio; x++) {
                        for (int y = (col + patternCol) * yRatio; y < (col + patternCol + 1) * yRatio; y++) {
                            resultBitmap.setPixel(x, y, color);
                        }
                    }
                }
            }

            //imageView.setImageBitmap(mResultBitmap);
        });


        // get bitmap

    }
}