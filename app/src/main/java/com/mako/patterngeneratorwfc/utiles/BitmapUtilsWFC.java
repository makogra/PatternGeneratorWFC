package com.mako.patterngeneratorwfc.utiles;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.datamodels.ResultViewModel;

import java.util.List;

public class BitmapUtilsWFC {

    private static final String TAG = BitmapUtilsWFC.class.getName();
    private Fragment fragment;
    private ResultViewModel resultViewModel;

    public BitmapUtilsWFC(Fragment fragment) {
        this.fragment = fragment;
        resultViewModel = new ViewModelProvider(fragment.requireActivity()).get(ResultViewModel.class);
    }

    public void attacheScaledBitmap(Bitmap scaledBitmap) {
        waitForView();
        new Handler(Looper.getMainLooper()).post(() -> {
            int     height,
                    width,
                    ratio;
            //Somehow view is null here
            FrameLayout frameLayout = fragment.requireView().findViewById(R.id.fragment_wfc_image_frame_layout);
            ImageView imageView = fragment.requireView().findViewById(R.id.fragment_wfc_image_view);
            ViewGroup.LayoutParams params = frameLayout.getLayoutParams();
            ratio = Math.min(frameLayout.getHeight() / scaledBitmap.getHeight(), frameLayout.getWidth() / scaledBitmap.getWidth());
            height = scaledBitmap.getHeight() * ratio;
            width = scaledBitmap.getWidth() * ratio;
            params.height = height;
            params.width = width;
            frameLayout.setLayoutParams(params);

            imageView.setImageBitmap(Bitmap.createScaledBitmap(scaledBitmap, width, height, false));
            //resultViewModel.setBitmap(temp);
        });
    }

    private void waitForView() {
        while (true){
            if (fragment.getView() != null)
                return;
        }
    }


    /**
     * Fill bitmap accordingly to result of WFC
     * @param patternGrid - result of a WFC
     * @param patternList - list of all patterns
     * @param outputBitmap - bitmap that will be filled accordingly to patternGrid
     */
    public void makeBitmap(List<String> inputValueMap, int[][] patternGrid, int patternSize, List<Integer[][]> patternList, Bitmap outputBitmap) {

        int patternId;
        int x;
        int y;
        Integer valueId;
        int color;
        Integer[][] pattern;
        // For each row of patterns
        for (int patternRow = 0; patternRow < patternGrid.length; patternRow++) {
            //for each row in pattern - overlap
            for (int i = 0; i < patternSize - 1; i++) {
                // Horizontal (rows) out of bound check
                y = patternRow * (patternSize - 1) + i;
                if (y >= outputBitmap.getHeight())
                    break;
                // for each col of patterns
                for (int patternCol = 0; patternCol < patternGrid[0].length; patternCol++) {
                    patternId = patternGrid[patternRow][patternCol];
                    pattern = patternList.get(patternId);
                    // for each col in pattern
                    for (int j = 0; j < patternSize - 1; j++) {
                        // Vertical (columns) Out of bound check
                        x = patternCol * (patternSize - 1) + j;
                        if (x >= outputBitmap.getWidth()) {
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

    public void createEmptyBitmap(int outputHeight, int outputWidth){
        Bitmap emptySmallBitmap = Bitmap.createBitmap(outputWidth, outputHeight, Bitmap.Config.ARGB_8888);
        resultViewModel.setBitmap(emptySmallBitmap);
        attacheScaledBitmap(emptySmallBitmap);
    }


    public void clearPreviousBitmap() {
        //createEmptyBitmap(resultViewModel.getBitmap().getHeight(), resultViewModel.getBitmap().getWidth());
        /*
        resultViewModel.setBitmap(null);
        ImageView imageView = fragment.requireView().findViewById(R.id.fragment_wfc_image_view);
        ne
        w Handler(Looper.getMainLooper()).post(() -> imageView.setImageBitmap(null));
         */
    }

    public int getColorOfAPixel(Integer valueId, List<String> inputValueMap) {
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


}
