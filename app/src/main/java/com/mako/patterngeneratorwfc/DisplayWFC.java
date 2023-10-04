package com.mako.patterngeneratorwfc;

import android.util.Log;

import com.mako.patterngeneratorwfc.ui.WFCFragment;
import com.mako.patterngeneratorwfc.wfc.Cell;
import com.mako.patterngeneratorwfc.wfc.Wave;

import java.util.Arrays;
import java.util.List;

public class DisplayWFC {

    private static final String TAG =  DisplayWFC.class.getName();
    private int patternSize;
    private Wave wave;
    private int outputHeight;
    private int outputWidth;
    private final List<Integer[][]> patternList;
    private WFCFragment wfcFragmentObserver;
    private final List<String> inputValueMap;


    public DisplayWFC(List<Integer[][]> patternList, List<String> inputValueMap) {
        this.patternList = patternList;
        this.inputValueMap = inputValueMap;
    }

    public DisplayWFC(int patternSize, Wave wave, int outputHeight, int outputWidth, List<Integer[][]> patternList, List<String> inputValueMap) {
        this.patternSize = patternSize;
        this.wave = wave;
        this.outputHeight = outputHeight;
        this.outputWidth = outputWidth;
        this.patternList = patternList;
        this.inputValueMap = inputValueMap;
    }

    public void setWave(Wave wave) {
        this.wave = wave;
    }

    public void notifyResultUpdate(Cell cell) {
        if (cell == null) return;
        //int x = (cell.getRow() / patternSize) + (cell.getRow() % patternSize);
        //int y = (cell.getCol() / patternSize) + (cell.getCol() % patternSize);

        int x = cell.getRow(); // patternSize;
        int y = cell.getCol(); // patternSize;

        int value = wave.getOutputValue(x, y);
        wfcFragmentObserver.updateResult(cell, value);
    }

    public void displayResult(){
        if (!Config.IS_LOGGABLE){
            return;
        }
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

    public void displayRules(List<List<List<Integer>>> defaultPatternEnablers){
        if (!Config.IS_LOGGABLE){
            return;
        }
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

    public void displayPatterns(){
        if (!Config.IS_LOGGABLE){
            return;
        }

        Integer[][] pattern;
        int valueId;

        StringBuilder stringBuilder;

        for (int i = 0; i < patternSize+1; i++) {
            stringBuilder = new StringBuilder();
            for (int j = 0; j < patternList.size(); j++) {
                if (i == 0)
                    stringBuilder.append(" ").append(j).append(" |");
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

    public void observe(WFCFragment wfcFragmentObserver){
        this.wfcFragmentObserver = wfcFragmentObserver;
    }

    public void clearResult(int outputHeight, int outputWidth) {
        wfcFragmentObserver.clearResult(outputHeight, outputWidth);
    }
}
