package com.mako.patterngeneratorwfc.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mako.patterngeneratorwfc.R;

import java.util.Random;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.Holder> {

    private static final String TAG = "ResultAdapter";
    private final int height;
    private final int width;
    private final int patternSize;
    private int[][] grid;

    public ResultAdapter(int[][] grid, int width, int height, int patternSize) {
        this.grid = grid;
        this.height = height;
        this.width = width;
        this.patternSize = patternSize;
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
        holder.imageView.setImageBitmap(bitmap);
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
