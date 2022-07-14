package com.mako.patterngeneratorwfc.wfc;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Wave {

    private static class EntropyEntry implements Comparable<EntropyEntry>{
        int row;
        int col;
        double entropy;

        public EntropyEntry(int row, int col, double entropy) {
            this.row = row;
            this.col = col;
            this.entropy = entropy;
        }

        @Override
        public int compareTo(EntropyEntry o) {
            return Double.compare(this.entropy, o.entropy);
        }

        @NonNull
        @Override
        public String toString() {
            return "EntropyEntry{" +
                    "row=" + row +
                    ", col=" + col +
                    ", entropy=" + entropy +
                    '}';
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof EntropyEntry)){
                return false;
            }
            EntropyEntry e = (EntropyEntry) obj;
            return e.row == this.row && e.col == this.col;
        }
    }

    private static final String TAG = "Wave";
    private final int outputPatternGridWidth;
    private final int outputPatternGridHeight;
    private int numberOfCellsLeftToObserve;
    private boolean isCollapsed;
    private Cell[][] wave;
    private int[][] outputGrid;
    private PriorityQueue<EntropyEntry> lowestEntropyQueue;

    Wave(int outputHeight, int outputWidth, int patternSize){
        this.outputPatternGridHeight = (int) Math.ceil((outputHeight-1)/(double)(patternSize-1));
        this.outputPatternGridWidth = (int) Math.ceil((outputWidth-1)/(double)(patternSize-1));
        this.wave = new Cell[outputPatternGridHeight][outputPatternGridWidth];
        this.outputGrid = new int[outputPatternGridHeight][outputPatternGridWidth];
        this.numberOfCellsLeftToObserve = outputPatternGridHeight * outputPatternGridWidth;
        this.isCollapsed = false;

    }

    //Getters
    public boolean isCellObserved(int row, int col){
        return wave[row][col].isObserved();
    }

    public Cell getCell(int row, int col){
        return wave[row][col];
    }

    public int getOutputValue(int row, int col){
        return outputGrid[row][col];
    }

    public int[][] getOutputGrid() {
        return outputGrid;
    }

    public int getHeight() {
        return outputPatternGridHeight;
    }

    public int getWidth() {
        return outputPatternGridWidth;
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

    void addToLowestEntropyQueue(int row, int col){
        lowestEntropyQueue.add(new EntropyEntry(row, col, wave[row][col].getEntropy()));
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
