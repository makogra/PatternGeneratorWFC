package com.mako.patterngeneratorwfc;

import java.util.List;

public class Result {

    //TODO change to viewModel

    private static final String TAG = "Result";
    private final List<Integer[][]> patternList;

    private int[][] outputGrid;
    private int patternSize;
    private int height;
    private int width;


    public Result(int[][] outputGrid, int patternSize, int height, int width, List<Integer[][]> patternList) {
        this.outputGrid = outputGrid;
        this.patternSize = patternSize;
        this.height = height;
        this.width = width;
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

    public List<Integer[][]> getPatternList() {
        return patternList;
    }


}
