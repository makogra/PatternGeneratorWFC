package com.mako.patterngeneratorwfc.adapters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.ui.ResultFragment;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.Holder> {

    private static final String TAG = "ResultAdapter";
    private final int height;
    private final int width;
    private final int patternSize;
    private final int overlap;
    private final List<Integer[][]> patternList;
    private int[][] grid;

    public ResultAdapter(ResultFragment resultFragment) {
        this.grid = resultFragment.getOutputGrid();
        this.height = resultFragment.getHeight();
        this.width = resultFragment.getWidth();
        this.patternSize = resultFragment.getPatternSize();
        this.overlap = resultFragment.getOverlap();
        this.patternList = resultFragment.getPatternList();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_result, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        // Create random color to test if this function is working correctly
        int r,g,b;
        Bitmap bitmap = Bitmap.createBitmap(patternSize, patternSize,Bitmap.Config.ARGB_8888);


        r = 255  - (position % 255) /2;
        g = 0;
        b = position % 255;
        for (int i = 0; i < patternSize; i++) {
            for (int j = 0; j < patternSize; j++) {
                bitmap.setPixel(i,j, Color.rgb(r,g,b));
            }
        }

        Log.d(TAG, "onBindViewHolder: position = " + position);
        holder.imageView.setImageBitmap(bitmap);
    }

    private int pixelAt(int position){
        // Get pixels at
        int[] patternIndex = getPatternIndexAt(position);
        int patternId = grid[patternIndex[0]][patternIndex[1]];
        Integer[][] pattern = patternList.get(patternId);
        return 0;
    }

    private int[] getPatternIndexAt(int position) {
        // pattern =
        // position = 17
        // width = 16
        // patternSize = 3
        // overlap = 1
        // patternRowIndex should equals 0
        // (33 % 16) / (3 - 1)
        // 1 / 2 = 0

        int patternRowIndex = (position + 1) / width / (patternSize - overlap);
        int patternColIndex = (position % width) / (patternSize - overlap);
        return new int[]{patternRowIndex, patternColIndex};
    }

    @Override
    public int getItemCount() {
        return height * width;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.card_view_result_image_view);
        }
    }
}
