package com.mako.patterngeneratorwfc.wfc;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class Wave {

    private static final String TAG = "Wave";
    private final int outputPatternGridWidth;
    private final int outputPatternGridHeight;
    private int numberOfCellsLeftToObserve;
    private boolean isCollapsed;
    private Cell[][] wave;
    private int[][] outputGrid;

    Wave(int outputHeight, int outputWidth, int patternSize){
        this.outputPatternGridHeight = (int) Math.ceil((outputHeight-1)/(double)(patternSize-1));
        this.outputPatternGridWidth = (int) Math.ceil((outputWidth-1)/(double)(patternSize-1));
        this.wave = new Cell[outputPatternGridHeight][outputPatternGridWidth];
        this.outputGrid = new int[outputPatternGridHeight][outputPatternGridWidth];
        this.numberOfCellsLeftToObserve = outputPatternGridHeight * outputPatternGridWidth;
        this.isCollapsed = false;

    }

    //Getters
    boolean isCellObserved(int row, int col){
        return wave[row][col].isObserved();
    }

    Cell getCell(int row, int col){
        return wave[row][col];
    }

    int getOutputValue(int row, int col){
        return outputGrid[row][col];
    }

    public int[][] getOutputGrid() {
        return outputGrid;
    }

    boolean isCollapsed(){
        return isCollapsed;
    }

    void decreaseNumberOfCellsLeftToObserve(){
        numberOfCellsLeftToObserve--;

    }

    private void checkIfCollapsed(){
        if (numberOfCellsLeftToObserve == 0){
            isCollapsed = true;
        }
        if (numberOfCellsLeftToObserve < 0){
            Log.w(TAG, "checkIfCollapsed: numberOfCellsLeftToObserve is lover than 0: " + numberOfCellsLeftToObserve );
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Wave{" +
                "numberOfCellsLeftToObserve=" + numberOfCellsLeftToObserve +
                ", isCollapsed=" + isCollapsed +
                ", wave=" + Arrays.toString(wave) +
                ", outputGrid=" + Arrays.toString(outputGrid) +
                '}';
    }
}
