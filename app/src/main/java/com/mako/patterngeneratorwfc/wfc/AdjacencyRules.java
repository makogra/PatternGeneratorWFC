package com.mako.patterngeneratorwfc.wfc;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyRules {

    private final int totalNumberOfPatterns;
    // defaultPatternEnablers.get(patternIndex).get(directionIndex).get(patternEnabler)
    // List that contains possible PatternEnablers for each direction for each pattern
    private List<List<List<Integer>>> defaultPatternEnablers;
    private final List<Integer[][]> patternList;
    private final int patternSize;
    private final int OVERLAP = 1;

    public AdjacencyRules(List<Integer[][]> patternList){
        this.patternList = patternList;
        this.totalNumberOfPatterns = patternList.size();
        this.patternSize = patternList.get(0).length;
    }

    public List<List<List<Integer>>> getDefaultPatternEnablers() {
        if (defaultPatternEnablers == null)
            initDefaultPatternEnablers();
        return defaultPatternEnablers;
    }

    private void initDefaultPatternEnablers(){
        int numberOfDirections = Directions.getTOTAL_DIRECTIONS_NUMBER();
        defaultPatternEnablers = new ArrayList<>(totalNumberOfPatterns);
        List<Integer> helperPatternEnabler;
        List<List<Integer>> helperDirections;
        for (int i = 0; i < totalNumberOfPatterns; i++) {
            helperDirections = new ArrayList<>(numberOfDirections);
            for (int j = 0; j < numberOfDirections; j++) {
                helperPatternEnabler = new ArrayList<Integer>(){
                    @Override
                    public boolean add(Integer integer) {
                        if (this.contains(integer))
                            return false;
                        return super.add(integer);
                    }
                };
                helperDirections.add(helperPatternEnabler);
            }
            defaultPatternEnablers.add(helperDirections);
        }

        for (int patternIndex = 0; patternIndex < patternList.size(); patternIndex++) {
            for (int direction = 0; direction < numberOfDirections; direction++) {
                for (int patternEnabler = 0; patternEnabler < patternList.size(); patternEnabler++) {
                    if (isEnabler(patternIndex, direction, patternEnabler))
                        defaultPatternEnablers.get(patternIndex).get(direction).add(patternEnabler);
                }
            }
        }
    }

    private boolean isEnabler2(int patternIndex, int direction, int patternEnabler) {
        Integer[][] pattern1 = patternList.get(patternIndex);
        Integer[][] pattern2 = patternList.get(patternEnabler);
        int[] directionOffset = Directions.getDirection(direction);
        int row1, row2, col1, col2, toRow, toCol;


        if (directionOffset[0] != 0 && directionOffset[1] != 0){
            // It's diagonal
            if (directionOffset[0] == 1) {
                // It's DOWN_...
                row1 = patternSize-1;
                row2 = 0;
            } else{
                // It's UP_...
                row1 = 0;
                row2 = patternSize-1;
            }

            if (directionOffset[1] == 1){
                // It's ..._RIGHT
                col1 = patternSize-1;
                col2 = 0;
            } else {
                // It's ..._LEFT
                col1 = 0;
                col2 = patternSize-1;
            }

            return pattern1[row1][col1].equals(pattern2[row2][col2]);
        }

        if (directionOffset[0] == 0){
            // It's vertical
            row1 = 0;
            row2 = 0;
            toRow = patternSize;

            if (directionOffset[1] == 1){
                // It's RIGHT
                col1 = patternSize-1;
                col2 = 0;
            } else {
                // It's LEFT
                col1 = 0;
                col2 = patternSize-1;
            }


            for (; row1 < toRow; row1++, row2++) {
                if (!pattern1[row1][col1].equals(pattern2[row2][col2]))
                    return false;
            }

            return true;
        }

        // It's horizontal
        col1 = 0;
        col2 = 0;
        toCol = patternSize;

        if (directionOffset[0] == 1){
            // It's DOWN
            row1 = patternSize-1;
            row2 = 0;
        } else {
            // It's UP
            row1 = 0;
            row2 = patternSize-1;
        }

        for (;col1 < toCol; col1++, col2++){
            if (!pattern1[row1][col1].equals(pattern2[row2][col2]))
                return false;
        }

        return true;
    }

    private boolean isEnabler(int patternIndex, int direction, int patternEnabler) {
        Integer[][] pattern1 = patternList.get(patternIndex);
        Integer[][] pattern2 = patternList.get(patternEnabler);
        int[] directionOffset = Directions.getDirection(direction);
        int row1, row2, col1, col2, toRow, toCol;


        // if both sides are different from 0 it means that there is offset in each direction (either -1 or +1)
        if (directionOffset[0] != 0 && directionOffset[1] != 0) {
            // It's diagonal
            if (directionOffset[0] == 1) {
                // It's DOWN_...
                row1 = patternSize - 1;
                row2 = 0;
            } else {
                // It's UP_...
                row1 = 0;
                row2 = patternSize - 1;
            }

            if (directionOffset[1] == 1) {
                // It's ..._RIGHT
                col1 = patternSize - 1;
                col2 = 0;
            } else {
                // It's ..._LEFT
                col1 = 0;
                col2 = patternSize - 1;
            }

            return pattern1[row1][col1].equals(pattern2[row2][col2]);
        }

        if (directionOffset[0] == 0) {
            // It's vertical
            row1 = 0;
            row2 = 0;
            toRow = patternSize;

            if (directionOffset[1] == 1) {
                // It's RIGHT
                col1 = patternSize - 1;
                col2 = 0;
            } else {
                // It's LEFT
                col1 = 0;
                col2 = patternSize - 1;
            }


            for (; row1 < toRow; row1++, row2++) {
                if (!pattern1[row1][col1].equals(pattern2[row2][col2]))
                    return false;
            }

            return true;
        }

        // It's horizontal
        col1 = 0;
        col2 = 0;
        toCol = patternSize;

        if (directionOffset[0] == 1) {
            // It;s DOWN
            row1 = patternSize - 1;
            row2 = 0;
        } else {
            // It's UP
            row1 = 0;
            row2 = patternSize - 1;
        }

        for (; col1 < toCol; col1++, col2++) {
            if (!pattern1[row1][col1].equals(pattern2[row2][col2]))
                return false;
        }

        return true;
    }
}
