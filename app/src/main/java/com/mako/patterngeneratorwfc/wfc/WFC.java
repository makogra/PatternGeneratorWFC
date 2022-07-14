package com.mako.patterngeneratorwfc.wfc;

public class WFC {


    private final int[][] valueGrid;
    private final int patternSize;
    private final int tilesOverLap;
    private final int outputHeight;
    private final int outputWidth;
    private final Propagator propagator;
    private final Wave wave;

    public WFC(int[][] valueGrid, int patternSize, int tilesOverLap, int outputHeight, int outputWidth) {
        this.valueGrid = valueGrid;
        this.patternSize = patternSize;
        this.tilesOverLap = tilesOverLap;
        this.outputHeight = outputHeight;
        this.outputWidth = outputWidth;
        this.wave = new Wave(outputHeight, outputWidth, patternSize);
        //TODO fill propagator constructor

        this.propagator = new Propagator(wave);
    }

    //Getters
    public int[][] getValueGrid() {
        return valueGrid;
    }

    public int getPatternSize() {
        return patternSize;
    }

    public int getTilesOverLap() {
        return tilesOverLap;
    }

    public int getOutputHeight() {
        return outputHeight;
    }

    public int getOutputWidth() {
        return outputWidth;
    }
}
