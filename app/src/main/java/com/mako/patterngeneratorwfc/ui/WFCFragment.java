package com.mako.patterngeneratorwfc.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.mako.patterngeneratorwfc.wfc.Cell;
import com.mako.patterngeneratorwfc.wfc.WFC;

import java.util.List;

public class WFCFragment extends Fragment {

    private static final String TAG = "WFCFragment";
    private static ViewModelProvider sViewModelProvider;
    private ViewModelProvider mViewModelProvider;
    private TileSetViewModel mTileSetViewModel;
    private SettingsTileSetViewModel mSettingsTileSetViewModel;
    private WFCViewModel mWFCViewModel;
    private ResultViewModel mResultViewModel;
    private Bitmap mResultBitmap = null;
    private List<String> mInputValueMap;
    private List<Integer[][]> mPatternList;

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
        mResultViewModel = new ViewModelProvider(requireActivity()).get(ResultViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViewModels();
        //displayResult();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_w_f_c, container, false);
        Log.d(TAG, "onCreateView() called with: inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
        Button startButton = view.findViewById(R.id.fragment_wfc_start_button);
        startButton.setOnClickListener(v -> {
            displayWFCStarted();
            new Thread(() -> {
                clearPreviousBitmap();
                int patternSize = mSettingsTileSetViewModel.getValue(0);
                int outputHeight = mSettingsTileSetViewModel.getValue(1);
                int outputWidth = mSettingsTileSetViewModel.getValue(2);
                int tilesOverlap = mSettingsTileSetViewModel.getValue(3);
                boolean rotation = mSettingsTileSetViewModel.getRotation();
                boolean reflection = mSettingsTileSetViewModel.getReflection();
                TileSet input = mTileSetViewModel.getCurrentTileSet();
                Log.d(TAG, "onCreateView: tileSet = " + input);
                String[][] tempInputGrid =  new String[][]{
                        {"G","G","G","G","C","S","S","S"},
                        {"G","G","G","C","C","S","S","S"},
                        {"G","G","G","C","S","S","S","S"},
                        {"G","G","G","C","S","S","S","S"},
                        {"G","G","G","C","S","S","S","S"}
                };

                //TODO create new bitmap
                createEmptyBitmap(outputHeight, outputWidth);

                WFC wfc = new WFC(input, patternSize, tilesOverlap, outputHeight, outputWidth, rotation, reflection);
                mInputValueMap = wfc.getInputValueMap();
                mPatternList = wfc.getPatternList();
                wfc.observe(this);
                wfc.run(30);
                if (wfc.isCollapsed()){
                    int[][] outputGrid = wfc.getOutputGrid();
                    Result result = new Result(outputGrid, patternSize, outputHeight, outputWidth, tilesOverlap, wfc.getPatternList());

                    //showResult(result, wfc.getInputValueMap());
                    //TODO display finish
                }
                //TODO else display failure
            }).start();
        });
        return view;
    }

    private void clearPreviousBitmap() {
        mResultBitmap = null;
        ImageView imageView = requireView().findViewById(R.id.fragment_wfc_image_view);
        new Handler(Looper.getMainLooper()).post(() -> imageView.setImageBitmap(null));
    }


    private void displayResult() {
        if (mResultViewModel.getBitmap() == null)
            return;
        attacheScaledBitmap(mResultViewModel.getBitmap());
    }

    private void showResult(Result result, List<String> inputValueMap) {
        int[][] patternGrid = result.getOutputGrid();
        int     patternSize = result.getPatternSize(),
                outputHeight = result.getHeight(),
                outputWidth = result.getWidth(),
                overlap = result.getOverlap();
        List<Integer[][]> patternList = result.getPatternList();
        Bitmap outputBitmap;
        mResultViewModel.setBitmap(Bitmap.createBitmap(outputWidth, outputHeight, Bitmap.Config.ARGB_8888));
        outputBitmap = mResultViewModel.getBitmap();
        makeBitmap(inputValueMap, patternGrid, patternSize, overlap, patternList, outputBitmap);
        mResultViewModel.setBitmap(outputBitmap);

        attacheScaledBitmap(outputBitmap);

    }

    private int getColorOfAPixel(Integer valueId, List<String> inputValueMap) {
        int color;
        String value = inputValueMap.get(valueId);
        switch (value){
            case "G":
                color = Color.GREEN;
                break;
            case "C":
                color = Color.YELLOW;
                break;
            case "S":
                color = Color.BLUE;
                break;
            case "M":
                color = Color.GRAY;
                break;
            default:
                color = Color.BLACK;
        }
        return color;
    }

    private void displayWFCStarted() {
        Log.d(TAG, "displayWFCStarted: started wfc " + Thread.currentThread().getName());
        Toast.makeText(requireActivity().getApplicationContext(), "WFC started...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateViewModels();
        Log.d(TAG, "onResume() called ");
        testMSettingsViewModel();
        displayResult();

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

    //TODO move this method to tests
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

    public void updateResult(Cell cell, int value) {
        if (mResultBitmap == null){
            Log.w(TAG, "updateResult: updating Null bitmap", new NullPointerException());
        }
        //TODO
        int row = cell.getRow();
        int col = cell.getCol();
        int height = mSettingsTileSetViewModel.getValue(1);
        int width = mSettingsTileSetViewModel.getValue(2);
        int xRatio = mResultBitmap.getWidth()/width;
        int yRatio = mResultBitmap.getHeight()/height;
        Integer[][] pattern = mPatternList.get(value);
        //int color = getColorOfAPixel(value, mInputValueMap);
        ImageView imageView = requireView().findViewById(R.id.fragment_wfc_image_view);

        imageView.post(() -> {
            int color;
            for (int patternRow = 0; patternRow < pattern.length; patternRow++) {
                for (int patternCol = 0; patternCol < pattern[0].length; patternCol++) {
                    // For each item in pattern
                    color = getColorOfAPixel(pattern[patternRow][patternCol], mInputValueMap);
                    for (int x = (row + patternRow) * xRatio; x < (row + patternRow + 1) * xRatio; x++) {
                        for (int y = (col + patternCol) * yRatio; y < (col + patternCol + 1) * yRatio; y++) {
                            mResultBitmap.setPixel(x, y, color);
                        }
                    }
                }
            }

            //imageView.setImageBitmap(mResultBitmap);
        });


        // get bitmap

    }
}