package com.mako.patterngeneratorwfc.datamodels;

import androidx.lifecycle.ViewModel;

public class ResultViewModel extends ViewModel {


    private int[][] outputGrid;
    private int patternSize;
    private int height;
    private int width;

    public void setOutputGrid(int[][] outputGrid) {
        this.outputGrid = outputGrid;
    }

    public void setPatternSize(int patternSize) {
        this.patternSize = patternSize;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getPatternSize() {
        return patternSize;
    }
}
