package com.mako.patterngeneratorwfc.wfc;

import java.util.List;

public class WFC {

    private final int patternSize;
    private final int tilesOverLap;
    private final int outputHeight;
    private final int outputWidth;
    private final Propagator propagator;
    private Wave wave;
    private final List<String> inputValueMap;
    private final List<Integer[][]> patternList;
    private final InputHandler inputHandler;
    private final List<List<List<Integer>>> defaultPatternEnablers;

    public WFC(String[][] inputGrid, int patternSize, int tilesOverLap, int outputHeight, int outputWidth) {
        this.patternSize = patternSize;
        this.tilesOverLap = tilesOverLap;
        this.outputHeight = outputHeight;
        this.outputWidth = outputWidth;
        this.wave = new Wave(outputHeight, outputWidth, patternSize);
        this.inputHandler = new InputHandler(inputGrid, patternSize);
        AdjacencyRules adjacencyRules = new AdjacencyRules(inputHandler.getPatternList(), inputHandler.getTotalNumberOfPatterns());
        this.defaultPatternEnablers = adjacencyRules.getDefaultPatternEnablers();
        patternList = inputHandler.getPatternList();
        inputValueMap = inputHandler.getInputToValueMap();
        //TODO fill propagator constructor

        this.propagator = new Propagator(wave);
    }

    //Getters
    /*public int[][] getValueGrid() {
        return valueGrid;
    }*/

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

    private void initWave(){
        wave = new Wave(outputHeight, outputWidth, patternSize, defaultPatternEnablers, inputHandler.getTotalNumberOfPatterns(), inputHandler.getRelativeFrequency());
    }
}
