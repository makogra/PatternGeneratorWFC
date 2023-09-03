package com.mako.patterngeneratorwfc.wfc;

import android.util.Log;

import com.mako.patterngeneratorwfc.DisplayWFC;
import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.ui.WFCFragment;

import java.util.Arrays;
import java.util.List;

public class WFC {

    private static final String TAG = "WFC";
    private final int patternSize;
    private final int outputHeight;
    private final int outputWidth;
    private Wave wave;
    private final List<String> inputValueMap;
    private final List<Integer[][]> patternList;
    private final InputHandler inputHandler;
    private final List<List<List<Integer>>> defaultPatternEnablers;
    private final DisplayWFC displayWFC;

    public WFC(TileSet tileSet, int patternSize, int outputHeight, int outputWidth, boolean rotation, boolean reflection) {
        this.patternSize = patternSize;
        this.outputHeight = outputHeight;
        this.outputWidth = outputWidth;
        this.wave = new Wave(outputHeight, outputWidth, patternSize);
        this.inputHandler = new InputHandler(tileSet.getValueGrid(), patternSize, rotation, reflection);
        AdjacencyRules adjacencyRules = new AdjacencyRules(inputHandler.getPatternList());
        this.defaultPatternEnablers = adjacencyRules.getDefaultPatternEnablers();
        patternList = inputHandler.getPatternList();
        inputValueMap = tileSet.getValueToStringPath();

        displayWFC = new DisplayWFC(patternSize, wave, outputHeight, outputWidth, patternList, inputValueMap);
        displayWFC.displayRules(defaultPatternEnablers);
    }

    //Getters
    public int getPatternSize() {
        return patternSize;
    }

    public int getOutputHeight() {
        return outputHeight;
    }

    public int getOutputWidth() {
        return outputWidth;
    }

    public List<Integer[][]> getPatternList() {
        return patternList;
    }

    private void initWave(){
        wave = new Wave(outputHeight, outputWidth, patternSize, defaultPatternEnablers, inputHandler.getTotalNumberOfPatterns(), inputHandler.getRelativeFrequency());
    }

    public void run(int maxPossibleTries){
        int currentTrieCount = 0;
        int collapseCount = 0;
        displayWFC.displayPatterns();
        Cell observedCell;
        initWave();
        displayWFC.setWave(wave);

        while (currentTrieCount < maxPossibleTries){
            try {
                while (wave.isRunning()){
                    collapseCount++;
                    observedCell = wave.collapse();
                    displayWFC.notifyResultUpdate(observedCell);
                    wave.propagate();
                }
                displayWFC.displayResult();
                break;
            } catch (IllegalStateException | IllegalArgumentException | IndexOutOfBoundsException  e){
                collapseCount = 0;
                initWave();
                displayWFC.setWave(wave);
                displayWFC.clearResult(outputHeight, outputWidth);
                currentTrieCount++;
            } finally {
                wave.finish();
            }
        }

    }

    public int[][] getOutputGrid(){
        return wave.getOutputPatternGrid();
    }

    public boolean isCollapsed(){
        return wave.isCollapsed();
    }

    public List<String> getInputValueMap() {
        return inputValueMap;
    }

    public void observe(WFCFragment wfcFragment) {
        displayWFC.observe(wfcFragment);
    }
}
