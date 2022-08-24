package com.mako.patterngeneratorwfc.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.datamodels.ResultViewModel;

import java.util.List;

public class ResultFragment {

    private static final String TAG = "ResultFragment";
    private final List<Integer[][]> patternList;

    private int overlap;
    private int[][] outputGrid;
    private int patternSize;
    private int height;
    private int width;


    public ResultFragment(int[][] outputGrid, int patternSize, int height, int width, int overlap, List<Integer[][]> patternList) {
        this.outputGrid = outputGrid;
        this.patternSize = patternSize;
        this.height = height;
        this.width = width;
        this.overlap = overlap;
        this.patternList = patternList;
    }

    public int getWidth() {
        return width;
    }

    public int[][] getOutputGrid() {
        return outputGrid;
    }

    public int getHeight() {
        return height;
    }

    public int getPatternSize() {
        return patternSize;
    }

    public int getOverlap() {
        return overlap;
    }

    public List<Integer[][]> getPatternList() {
        return patternList;
    }
}
