package com.mako.patterngeneratorwfc.wfc;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Wave {

    /*
    private static class EntropyEntry implements Comparable<EntropyEntry>{
        Cell cell;
        double entropy;

        public EntropyEntry(Cell cell, double entropy) {
        this.cell = cell;
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
                    "row=" + cell.getRow() +
                    ", col=" + cell.getRow() +
                    ", entropy=" + entropy +
                    '}';
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof EntropyEntry)){
                return false;
            }
            EntropyEntry e = (EntropyEntry) obj;
            return e.cell.getRow() == this.cell.getRow() && e.cell.getCol() == this.cell.getCol();
        }
    }

     */

    private static final String TAG = "Wave";
    private final int outputPatternGridWidth;
    private final int outputPatternGridHeight;
    private final Propagator propagator;
    private int numberOfCellsLeftToObserve;
    private boolean isCollapsed;
    private Cell[][] wave;
    private int[][] outputPatternGrid;
    private PriorityQueue<Cell> lowestEntropyCellQueue;

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

    boolean isRunning(){
        return !isCollapsed;
    }

    private Cell getCellWithLowestEntropy() {
        if( lowestEntropyCellQueue.size() == 0){
            checkIfIsCollapse();
            if (isCollapsed)
                return null;
        }
        return lowestEntropyCellQueue.poll();
    }

    void decreaseNumberOfCellsLeftToObserve(){
        numberOfCellsLeftToObserve--;

    }

    void addToLowestEntropyQueue(Cell cell){
        lowestEntropyCellQueue.offer(cell);
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
        lowestEntropyCellQueue =  new PriorityQueue<Cell>(){
            @Override
            public boolean contains(Object o) {
                for( Cell cell : this){
                    if( cell.equals(o))
                        return true;
                }
                return false;
            }


            @Override
            public boolean add(Cell cell) {
                if (null == cell){
                    Log.w(TAG, "add: null", new NullPointerException());
                    return false;
                }
                Cell cellToRemove = null;
                for (Cell entry : this){
                    if (entry.equals(cell) && entry.getEntropy() > cell.getEntropy())
                        cellToRemove = entry;
                }
                if (null != cellToRemove)
                    this.remove(cellToRemove);
                return super.add(cell);
            }
        };
        for (Cell[] cells : wave) {
            lowestEntropyCellQueue.addAll(Arrays.asList(cells).subList(0, wave[0].length));
        }
    }

    public Cell collapse() throws IllegalStateException, IllegalArgumentException, IndexOutOfBoundsException{
        Cell cellWithLowestEntropy;
        int row;
        int col;
        int patternValue;

        do {
            cellWithLowestEntropy = getCellWithLowestEntropy();
            if (cellWithLowestEntropy == null)
                return null;
        } while (cellWithLowestEntropy.isObserved());
        row = cellWithLowestEntropy.getRow();
        col = cellWithLowestEntropy.getCol();


        try {
            patternValue = cellWithLowestEntropy.observe();
        } catch (IllegalArgumentException e){
            cellWithLowestEntropy.update();
            if ((cellWithLowestEntropy.isObserved() && outputPatternGrid[row][col] == -1) || (wave[row][col].getNumberOfPossiblePatterns() == 0 && !cellWithLowestEntropy.isObserved())) // second argument is unnecessary, because it's already checked
                throw e;
            return cellWithLowestEntropy;
        } //others exceptions are rethrown


        this.outputPatternGrid[row][col] = patternValue;
        propagator.addToPropagate(row, col, patternValue, false);

        return cellWithLowestEntropy;
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
                        lowestEntropyCellQueue.add(item);
                }
        }
        isCollapsed = collapsed;
    }

    void propagate(){
        propagator.propagate();
    }

    public void finish() {
        propagator.finish();
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
