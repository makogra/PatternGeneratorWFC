package com.mako.patterngeneratorwfc.wfc;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;
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
    private final Propagator propagator;
    private int numberOfCellsLeftToObserve;
    private boolean isCollapsed;
    private Cell[][] wave;
    private int[][] outputPatternGrid;
    private PriorityQueue<EntropyEntry> lowestEntropyQueue;

    Wave(int outputHeight, int outputWidth, int patternSize ){
        this.outputPatternGridHeight = (int) Math.ceil((outputHeight-1)/(double)(patternSize-1));
        this.outputPatternGridWidth = (int) Math.ceil((outputWidth-1)/(double)(patternSize-1));
        this.wave = new Cell[outputPatternGridHeight][outputPatternGridWidth];
        this.outputPatternGrid = new int[outputPatternGridHeight][outputPatternGridWidth];
        this.numberOfCellsLeftToObserve = outputPatternGridHeight * outputPatternGridWidth;
        this.isCollapsed = false;
        this.propagator = new Propagator(this);

    }

    public Wave(int outputHeight, int outputWidth, int patternSize, List<List<List<Integer>>> defaultPatternEnablers, int numberOfPossiblePatterns, double[] relativeFrequency) {
        this.outputPatternGridHeight = (int) Math.ceil((outputHeight-1)/(double)(patternSize-1));
        this.outputPatternGridWidth = (int) Math.ceil((outputWidth-1)/(double)(patternSize-1));
        this.propagator = new Propagator(this);

        initGrid(defaultPatternEnablers, numberOfPossiblePatterns, relativeFrequency);
        initOutputPatternGrid();
        initLowestEntropyQueue();

    }

    //Getters
    public boolean isCellObserved(int row, int col){
        return wave[row][col].isObserved();
    }

    public Cell getCell(int row, int col){
        return wave[row][col];
    }

    public int getOutputValue(int row, int col){
        return outputPatternGrid[row][col];
    }

    public int[][] getOutputPatternGrid() {
        return outputPatternGrid;
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

    private void initGrid(List<List<List<Integer>>> defaultPatternEnablers, int numberOfPossiblePatterns, double[] relativeFrequency){
        wave = new Cell[outputPatternGridHeight][outputPatternGridWidth];

        for (int height = 0; height < outputPatternGridHeight; height++) {
            for (int width = 0; width < outputPatternGridWidth; width++) {
                wave[height][width] = new Cell(height, width, defaultPatternEnablers, numberOfPossiblePatterns, relativeFrequency, propagator);
            }
        }
    }

    private void initOutputPatternGrid(){
        outputPatternGrid = new int[outputPatternGridHeight][outputPatternGridWidth];

        for (int row = 0; row < outputPatternGridHeight; row++) {
            for (int col = 0; col < outputPatternGridWidth; col++) {
                outputPatternGrid[row][col] = -1;
            }
        }
    }

    private void initLowestEntropyQueue(){
        lowestEntropyQueue =  new PriorityQueue<EntropyEntry>(){
            @Override
            public boolean contains(Object o) {
                if (!(o instanceof EntropyEntry))
                    return false;

                EntropyEntry e = (EntropyEntry) o;

                for( EntropyEntry entry : this){
                    if( entry.row == e.row && entry.col == e.col)
                        return true;
                }
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean add(EntropyEntry entropyEntry) {
                this.removeIf(entry -> entry.equals(entropyEntry) && entry.entropy > entropyEntry.entropy);

                return super.add(entropyEntry);
            }
        };
        for (int row = 0; row < wave.length; row++) {
            for (int col = 0; col < wave[0].length; col++) {
                lowestEntropyQueue.add(new EntropyEntry(row, col, wave[row][col].getEntropy()));
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Wave{" +
                "numberOfCellsLeftToObserve=" + numberOfCellsLeftToObserve +
                ", isCollapsed=" + isCollapsed +
                ", wave=" + Arrays.toString(wave) +
                ", outputGrid=" + Arrays.toString(outputPatternGrid) +
                '}';
    }
}
