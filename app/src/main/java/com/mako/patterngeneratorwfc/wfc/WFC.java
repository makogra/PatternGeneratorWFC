package com.mako.patterngeneratorwfc.wfc;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class WFC {

    private static final String TAG = "WFC";
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

    public List<Integer[][]> getPatternList() {
        return patternList;
    }

    private void initWave(){
        wave = new Wave(outputHeight, outputWidth, patternSize, defaultPatternEnablers, inputHandler.getTotalNumberOfPatterns(), inputHandler.getRelativeFrequency());
    }

    public void run(int maxPossibleTries){
        Log.d(TAG, "run: started...");
        int currentTrieCount = 0;
        int collapseCount = 0;
        displayPatterns();
        int numberOfOutcomes = 1;
        long start;
        long finish;
        long time;
        long totalTime = 0L;
        long[] times = new long[numberOfOutcomes];
        for (int i = 0; i < numberOfOutcomes; i++) {
            initWave();
            start = System.currentTimeMillis();


            while (currentTrieCount < maxPossibleTries){
                try {
                    while (!wave.isCollapsed()){
                        collapseCount++;
                        wave.collapse();
                        wave.propagate();
                        Log.d(TAG, "run: processing ...");
                    }
                    Log.d(TAG, "Success " + "collapse count: " + collapseCount);

                    displayResult();
                    break;
                } catch (Exception e){
                    collapseCount = 0;
                    Log.d(TAG, "Failed " + "tried " + currentTrieCount + "times\n" + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
                    initWave();
                    currentTrieCount++;
                }
            }

            finish = System.currentTimeMillis();
            time = finish - start;
            times[i] = time;
            totalTime += time;
        }

        Log.d(TAG, Arrays.toString(times));
        Log.d(TAG, "avg time " + totalTime/numberOfOutcomes);

    }

    void displayResult(){
        int[][] patternGrid = wave.getOutputPatternGrid();
        Log.d(TAG, Arrays.deepToString(patternGrid));
        Log.d(TAG, "===============================================");
        int patternId;
        int valueId;
        Integer[][] pattern;
        StringBuilder stringBuilder;

        // For each row of patterns
        for (int patternRow = 0; patternRow < patternGrid.length; patternRow++) {
            //for each row in pattern - overlap
            for (int i = 0; i < patternSize-1; i++) {
                stringBuilder = new StringBuilder();
                // Horizontal (rows) out of bound check
                if (patternRow * (patternSize-1) + i > outputHeight)
                    break;
                // for each col of patterns
                for (int patternCol = 0; patternCol < patternGrid[0].length; patternCol++) {
                    patternId = patternGrid[patternRow][patternCol];
                    pattern = patternList.get(patternId);
                    // for each col in pattern
                    for (int j = 0; j < patternSize-1; j++) {
                        // Vertical (columns) Out of bound check
                        if (patternCol * (patternSize-1) + j > outputWidth) {
                            break;
                        }
                        valueId = pattern[i][j];
                        //stringBuilder.append(stringWithColor(valueId));
                        stringBuilder.append(valueToString(valueId));
                    }
                }

                Log.i(TAG, stringBuilder.toString());
            }
        }

/*
        for (int gridRow = 0; gridRow < outputHeight; gridRow++) {
            for (int gridCol = 0; gridCol < outputWidth; gridCol++) {
                patternId = patternGrid[gridRow][gridCol];
                pattern = patternList.get(patternId);
                valueId = pattern[0][0];
                System.out.print(stringWithColor(valueId));
            }

            System.out.print("\n");
        }*/
    }

    void displayRules(List<List<List<Integer>>> defaultPatternEnablers){
        for (int i = 0; i < defaultPatternEnablers.size(); i++) {
            Log.d(TAG, "-----------------------------------------");
            Log.d(TAG, "For patternId " + i);
            for (int j = 0; j < defaultPatternEnablers.get(i).size(); j++) {
                Log.d(TAG, "\t For Direction: " + j);
                Log.d(TAG, "\t \t " + defaultPatternEnablers.get(i).get(j).toString());
            }
        }
        Log.d(TAG, "#######################################");
    }

    void displayPatterns(){

        Integer[][] pattern;
        int valueId;

        StringBuilder stringBuilder;

        for (int i = 0; i < patternSize+1; i++) {
            stringBuilder = new StringBuilder();
            for (int j = 0; j < patternList.size(); j++) {
                if (i == 0)
                    stringBuilder.append(" " + j + " |");
                else {
                    stringBuilder.append(" ");
                    pattern = patternList.get(j);
                    for (int k = 0; k < patternSize; k++) {
                        valueId = pattern[i-1][k];
                        //stringBuilder.append(stringWithColor(valueId));
                        stringBuilder.append(valueToString(valueId));
                    }
                    stringBuilder.append(" ");
                }
            }
            Log.d(TAG, stringBuilder.toString());
        }
    }

    private String stringWithColor(int valueId){
        String ANSI_GREEN_BACKGROUND = "\u001B[42m";
        String ANSI_BLUE_BACKGROUND = "\u001B[44m";
        String ANSI_YELLOW_BACKGROUND = "\u001B[103m";
        String ANSI_GRAY_BACKGROUND = "\u001B[100m";
        String[] colors = new String[]{ANSI_GRAY_BACKGROUND, ANSI_GREEN_BACKGROUND, ANSI_YELLOW_BACKGROUND, ANSI_BLUE_BACKGROUND};
        String ANSI_RESET = "\u001B[0m";

        return colors[valueId] + inputValueMap.get(valueId) + ANSI_RESET;
    }

    private String valueToString(int valueId){
        return inputValueMap.get(valueId);
    }

    public int[][] getOutputGrid(){
        return wave.getOutputPatternGrid();
    }

    public boolean isCollapsed(){
        return wave.isCollapsed();
    }

}
