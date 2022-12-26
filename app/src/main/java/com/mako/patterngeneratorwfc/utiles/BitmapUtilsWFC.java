package com.mako.patterngeneratorwfc.utiles;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.datamodels.ResultViewModel;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class BitmapUtilsWFC {

    private static final String TAG = BitmapUtilsWFC.class.getName();
    private Fragment fragment;
    private ResultViewModel resultViewModel;
    private final long DELAY_IN_MILLIS = 40L;
    private final AtomicLong frames = new AtomicLong(1L);
    //TODO move to another class
    private LinkedBlockingQueue<Bitmap> updateQueue;


    public BitmapUtilsWFC(Fragment fragment) {
        this.fragment = fragment;
        resultViewModel = new ViewModelProvider(fragment.requireActivity()).get(ResultViewModel.class);
        //TODO move to another class
        updateQueue = new LinkedBlockingQueue<>();
    }

    public void attacheScaledBitmap(Bitmap bitmap) {
        attacheScaledBitmapSmooth(bitmap);
    }

    public void attacheScaledBitmapSmooth(Bitmap bitmap){
        waitForView();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            int     height,
                    width,
                    ratio;
            frames.getAndDecrement();
            if (fragment.getView() == null)
                return;
            FrameLayout frameLayout = fragment.requireView().findViewById(R.id.fragment_wfc_image_frame_layout);
            ImageView imageView = fragment.requireView().findViewById(R.id.fragment_wfc_image_view);
            ViewGroup.LayoutParams params = frameLayout.getLayoutParams();
            ratio = Math.min(frameLayout.getHeight() / bitmap.getHeight(), frameLayout.getWidth() / bitmap.getWidth());
            height = bitmap.getHeight() * ratio;
            width = bitmap.getWidth() * ratio;
            params.height = height;
            params.width = width;
            frameLayout.setLayoutParams(params);

            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false));
        }, frames.getAndIncrement()*DELAY_IN_MILLIS);
    }

    //TODO move to another class
    public void addToQueueUpdate(Bitmap bitmap){
        updateQueue.offer(bitmap);
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
        frames.set(1);
        updateQueue.clear();
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
