package com.mako.patterngeneratorwfc.wfc;

import androidx.annotation.NonNull;

import java.util.ArrayList;
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

        initStaticCell(defaultPatternEnablers, numberOfPossiblePatterns, relativeFrequency);
        initGrid();
        initOutputPatternGrid();
        initLowestEntropyQueue();

    }

    //Getters
    public boolean isCellObserved(int row, int col){
        return wave[row][col].isObserved();
    }

    public Propagator getPropagator() {
        return propagator;
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

    private EntropyEntry getLowestEntropyEntry() {
        if( lowestEntropyQueue.size() == 0){
            checkIfIsCollapse();
            if (isCollapsed)
                return null;
        }
        return lowestEntropyQueue.poll();
    }

    void decreaseNumberOfCellsLeftToObserve(){
        numberOfCellsLeftToObserve--;

    }

    void addToLowestEntropyQueue(int row, int col){
        lowestEntropyQueue.add(new EntropyEntry(row, col, wave[row][col].getEntropy()));
    }

    private void initStaticCell(List<List<List<Integer>>> defaultPatternEnablers, int numberOfPossiblePatterns, double[] relativeFrequency) {
        Cell.setsPropagator(propagator);
        Cell.setsDefaultPatternEnablers(defaultPatternEnablers);
        Cell.setsRelativeFrequency(relativeFrequency);
        Cell.setsTotalNumberOfPossiblePatterns(numberOfPossiblePatterns);
    }

    private void initGrid(){
        wave = new Cell[outputPatternGridHeight][outputPatternGridWidth];


        for (int height = 0; height < outputPatternGridHeight; height++) {
            for (int width = 0; width < outputPatternGridWidth; width++) {
                wave[height][width] = new Cell(height, width);
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


            @Override
            public boolean add(EntropyEntry entropyEntry) {
                EntropyEntry entryToRemove = null;
                for (EntropyEntry entry : this){
                    if (entry.equals(entropyEntry) && entry.entropy > entropyEntry.entropy)
                        entryToRemove = entry;
                }
                if (null != entryToRemove)
                    this.remove(entryToRemove);
                return super.add(entropyEntry);
            }
        };
        for (int row = 0; row < wave.length; row++) {
            for (int col = 0; col < wave[0].length; col++) {
                lowestEntropyQueue.add(new EntropyEntry(row, col, wave[row][col].getEntropy()));
            }
        }
    }

    public void collapse() {
        EntropyEntry lowestEntropyEntry;
        int row;
        int col;
        int patternValue;

        do {
            lowestEntropyEntry = getLowestEntropyEntry();
            if (lowestEntropyEntry == null)
                return;
            row = lowestEntropyEntry.row;
            col = lowestEntropyEntry.col;
        } while (wave[row][col].isObserved());

        patternValue = wave[row][col].observe();

        switch (patternValue) {
            case -1 :
                throw new IllegalStateException("Contradiction");
            case -2 :
                wave[row][col].update();
                if ((wave[row][col].isObserved() && outputPatternGrid[row][col] == -1) || (wave[row][col].getNumberOfPossiblePatterns() == 0 && !wave[row][col].isObserved())) // second argument is unnecessary, because it's already checked
                    throw new IllegalStateException("Contradiction");
                return;
            case -3 :
                throw new IllegalStateException("ERROR IDK WTF");
        }


        this.outputPatternGrid[row][col] = patternValue;
        propagator.addToPropagate(row, col, patternValue, false);
    }

    private void checkIfIsCollapse() {
        boolean collapsed = true;
        for (Cell[] row : wave){
            for (Cell item : row)
                if (!item.isObserved()){
                    collapsed = false;
                    if (item.getNumberOfPossiblePatterns() == 0)
                        throw new IllegalStateException("Contradiction");
                    else
                        lowestEntropyQueue.add(new EntropyEntry(item.getRow(), item.getCol(), item.getEntropy()));
                }
        }
        isCollapsed = collapsed;
    }

    void propagate(){
        propagator.propagate();
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
