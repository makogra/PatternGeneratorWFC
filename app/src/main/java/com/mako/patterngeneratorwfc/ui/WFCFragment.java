package com.mako.patterngeneratorwfc.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
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
        FrameLayout frameLayout = requireView().findViewById(R.id.fragment_wfc_image_frame_layout);
        frameLayout.post(() -> {
            Log.d(TAG, "onStart: frame width = " + frameLayout.getWidth() + " or params = " + frameLayout.getLayoutParams().width);
            Log.d(TAG, "onStart: frame height = " + frameLayout.getHeight() + " or params = " + frameLayout.getLayoutParams().height);
        });
        ImageView imageView = requireView().findViewById(R.id.fragment_wfc_image_view);
        imageView.post(() -> {
            Log.d(TAG, "onStart: image width = " + imageView.getWidth() + " or measure = " + imageView.getMeasuredWidth());
            Log.d(TAG, "onStart: image height = " + imageView.getHeight() + " or measure = " + imageView.getMeasuredHeight());
        });
        Log.d(TAG, "onStart: frame width = " + frameLayout);
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
                clearPreviousBitmap();
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
                    Result result = new Result(outputGrid, patternSize, height, width, overlap, wfc.getPatternList());

                    //new Handler(requireContext().getMainLooper()).post(() -> showResultFragment(resultFragment));

                    showResult(result, wfc.getInputValueMap());
                }
            }).start();
            //displayResult();
        });
        return view;
    }

    private void clearPreviousBitmap() {
        ImageView imageView = requireView().findViewById(R.id.fragment_wfc_image_view);
        new Handler(Looper.getMainLooper()).post(() -> imageView.setImageBitmap(null));
    }


    private void displayResult() {
        if (resultViewModel.getBitmap() == null)
            return;
        //Bitmap bitmap = createScaledBitmap(resultViewModel.getBitmap());
        attacheScaledBitmap(resultViewModel.getBitmap());
    }

    private Bitmap createScaledBitmap(Bitmap bitmap) {
        int[] size = maxAvailableSizeForBitmap();
        return createScaledBitmap(bitmap,  size[0], size[1]);
    }

    private int[] maxAvailableSizeForBitmap() {
        int height = resultViewModel.getHeight();
        int width = resultViewModel.getWidth();
        //TODO w , h
        return new int[]{width, height};
    }

    private Bitmap createScaledBitmap(Bitmap bitmap, int width, int height) {
        //TODO delete
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    private void showResult(Result result, List<String> inputValueMap) {
        Log.d(TAG, "showResultFragment: started");



        /*if (resultViewModel.getHeight() == 0){
            resultViewModel.setHeight();
        }
        if (resultViewModel.getWidth() == 0)
            resultViewModel.setWidth(500);*/
        int[][] patternGrid = result.getOutputGrid();
        int     patternSize = result.getPatternSize(),
                outputHeight = result.getHeight(),
                outputWidth = result.getWidth(),
                overlap = result.getOverlap(),
                height = resultViewModel.getHeight(),
                width = resultViewModel.getWidth();
        List<Integer[][]> patternList = result.getPatternList();
        Bitmap outputBitmap;
        resultViewModel.setBitmap(Bitmap.createBitmap(outputWidth, outputHeight, Bitmap.Config.ARGB_8888));
        outputBitmap = resultViewModel.getBitmap();
        makeBitmap(inputValueMap, patternGrid, patternSize, overlap, patternList, outputBitmap);
        resultViewModel.setBitmap(outputBitmap);


       // Bitmap scaledBitmap = createScaledBitmap(outputBitmap, width, height);

        attacheScaledBitmap(outputBitmap);
        Log.d(TAG, "showResult: finished");

    }

    private void attacheScaledBitmap(Bitmap scaledBitmap) {

        new Handler(Looper.getMainLooper()).post(() -> {
            int     height,
                    width,
                    ratio;
            FrameLayout frameLayout = requireView().findViewById(R.id.fragment_wfc_image_frame_layout);
            ImageView imageView = requireView().findViewById(R.id.fragment_wfc_image_view);
            Log.d(TAG, "showResultFragment: width = " + frameLayout.getWidth() + " height = " + frameLayout.getHeight());
            Log.d(TAG, "showResultFragment: width = " + frameLayout.getLayoutParams().width + " height = " + frameLayout.getLayoutParams().width);
            ViewGroup.LayoutParams params = frameLayout.getLayoutParams();
            ratio = Math.min(frameLayout.getHeight() / scaledBitmap.getHeight(), frameLayout.getWidth() / scaledBitmap.getWidth());
            height = scaledBitmap.getHeight() * ratio;
            width = scaledBitmap.getWidth() * ratio;
            params.height = height;
            params.width = width;
            frameLayout.setLayoutParams(params);

            imageView.setImageBitmap(Bitmap.createScaledBitmap(scaledBitmap, width, height, false));


            Log.d(TAG, "showResultFragment: width = " + frameLayout.getLayoutParams().width + " height = " + frameLayout.getLayoutParams().width);
            imageView.post(() -> {
                Log.d(TAG, "showResultFragment: POST width = " + imageView.getWidth() + " height = " + imageView.getHeight());
            });
            Log.d(TAG, "showResultFragment: ended");
        });
    }

    /**
     * Fill bitmap accordingly to result of WFC
     * @param patternGrid - result of a WFC
     * @param patternList - list of all patterns
     * @param outputBitmap - bitmap that will be filled accordingly to patternGrid
     */
    private void makeBitmap(List<String> inputValueMap, int[][] patternGrid, int patternSize, int overlap, List<Integer[][]> patternList, Bitmap outputBitmap) {

        int patternId;
        int x;
        int y;
        Integer valueId;
        int color;
        Integer[][] pattern;
        // For each row of patterns
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