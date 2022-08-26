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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.datamodels.ResultViewModel;
import com.mako.patterngeneratorwfc.datamodels.SettingsTileSetViewModel;
import com.mako.patterngeneratorwfc.datamodels.TileSetViewModel;
import com.mako.patterngeneratorwfc.datamodels.WFCViewModel;
import com.mako.patterngeneratorwfc.wfc.WFC;

import java.util.List;

public class WFCFragment extends Fragment {

    private static final String TAG = "WFCFragment";
    private static ViewModelProvider sViewModelProvider;
    private ViewModelProvider mViewModelProvider;
    private TileSetViewModel mTileSetViewModel;
    private SettingsTileSetViewModel mSettingsTileSetViewModel;
    private WFCViewModel mWFCViewModel;
    private ResultViewModel resultViewModel;

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
        Log.d(TAG, "onStart() called");
        initViewModels();
        displayResult();
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
                    int overlap = wfc.getTilesOverLap();
                    Log.d(TAG, "onCreateView: outputGrid.length= " + outputGrid.length + " outputGrid[0].length = " + outputGrid[0].length + " width = " + width + " height = " + height );
                    ResultFragment resultFragment = new ResultFragment(outputGrid, patternSize, height, width, overlap, wfc.getPatternList());

                    //new Handler(requireContext().getMainLooper()).post(() -> showResultFragment(resultFragment));
                    showResultFragment(resultFragment, wfc.getInputValueMap());
                }
            }).start();
            displayResult();
        });
        return view;
    }



    private void displayResult() {
        if (resultViewModel.getBitmap() == null)
            return;
        Bitmap bitmap = createScaledBitmap(resultViewModel.getBitmap());
        attacheScaledBitmap(bitmap);
    }

    private Bitmap createScaledBitmap(Bitmap bitmap) {
        return createScaledBitmap(bitmap, 400, 400);
    }

    private Bitmap createScaledBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    private void showResultFragment(Result result, List<String> inputValueMap) {
        Log.d(TAG, "showResultFragment: started");



        // For each row of patterns
        if (resultViewModel.getHeight() == 0)
            resultViewModel.setHeight(500);
        if (resultViewModel.getWidth() == 0)
            resultViewModel.setWidth(500);
        int[][] patternGrid = resultFragment.getOutputGrid();
        int     patternSize = resultFragment.getPatternSize(),
                outputHeight = resultFragment.getHeight(),
                outputWidth = resultFragment.getWidth(),
                overlap = resultFragment.getOverlap(),
                height = resultViewModel.getHeight(),
                width = resultViewModel.getWidth();
        List<Integer[][]> patternList = resultFragment.getPatternList();
        Bitmap outputBitmap;
        if (resultViewModel.getBitmap() == null){
            resultViewModel.setBitmap(Bitmap.createBitmap(outputWidth, outputHeight, Bitmap.Config.ARGB_8888));
            outputBitmap = resultViewModel.getBitmap();
            makeBitmap(inputValueMap, patternGrid, patternSize, overlap, patternList, outputBitmap);
            resultViewModel.setBitmap(outputBitmap);
        } else
            outputBitmap = resultViewModel.getBitmap();


        Bitmap scaledBitmap = createScaledBitmap(outputBitmap, width, height);

        attacheScaledBitmap(scaledBitmap);
        Log.d(TAG, "showResultFragment: finished");

    }

    private void attacheScaledBitmap(Bitmap scaledBitmap) {
        new Handler(Looper.getMainLooper()).post(() -> {
            FrameLayout frameLayout = requireView().findViewById(R.id.fragment_wfc_image_frame_layout);
            ImageView imageView = requireView().findViewById(R.id.fragment_wfc_image_view);
            Log.d(TAG, "showResultFragment: width = " + frameLayout.getWidth() + " height = " + frameLayout.getHeight());
            Log.d(TAG, "showResultFragment: width = " + frameLayout.getLayoutParams().width + " height = " + frameLayout.getLayoutParams().width);
            ViewGroup.LayoutParams params = frameLayout.getLayoutParams();
            params.height = scaledBitmap.getHeight();
            params.width = scaledBitmap.getWidth();
            frameLayout.setLayoutParams(params);

            imageView.setImageBitmap(scaledBitmap);


            Log.d(TAG, "showResultFragment: width = " + frameLayout.getLayoutParams().width + " height = " + frameLayout.getLayoutParams().width);
            imageView.post(() -> {
                Log.d(TAG, "showResultFragment: POST width = " + imageView.getWidth() + " height = " + imageView.getHeight());
            });
            Log.d(TAG, "showResultFragment: ended");
        });
    }

    private void makeBitmap(List<String> inputValueMap, int[][] patternGrid, int patternSize, int overlap, List<Integer[][]> patternList, Bitmap outputBitmap) {
        //
        int patternId;
        int x;
        int y;
        Integer valueId;
        int color;
        Integer[][] pattern;
        for (int patternRow = 0; patternRow < patternGrid.length; patternRow++) {
            //for each row in pattern - overlap
            for (int i = 0; i < patternSize - overlap; i++) {
                // Horizontal (rows) out of bound check
                x = patternRow * (patternSize - overlap) + i;
                if (x > outputBitmap.getHeight())
                    break;
                // for each col of patterns
                for (int patternCol = 0; patternCol < patternGrid[0].length; patternCol++) {
                    patternId = patternGrid[patternRow][patternCol];
                    pattern = patternList.get(patternId);
                    // for each col in pattern
                    for (int j = 0; j < patternSize - overlap; j++) {
                        // Vertical (columns) Out of bound check
                        y = patternCol * (patternSize - overlap) + j;
                        if (y > outputBitmap.getWidth()) {
                            break;
                        }
                        valueId = pattern[i][j];
                        color = getColorOfAPixel(valueId, inputValueMap);
                        outputBitmap.setPixel(x,y,color);
                    }
                }
            }
        }
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