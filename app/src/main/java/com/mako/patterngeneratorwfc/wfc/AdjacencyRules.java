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

    AdjacencyRules(List<Integer[][]> patternList){
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

    private boolean isEnabler(int patternIndex, int direction, int patternEnabler) {
        Integer[][] pattern1 = patternList.get(patternIndex);
        Integer[][] pattern2 = patternList.get(patternEnabler);
        int[] directionVector = Directions.getDirection(direction);
        int row1, row2, col1, col2, toRow, toCol;


        if (directionVector[0] != 0 && directionVector[1] != 0){
            // It's diagonal
            if (directionVector[0] == 1) {
                // It's DOWN_...
                row1 = patternSize-OVERLAP;
                row2 = 0;
            } else{
                // It's UP_...
                row1 = 0;
                row2 = patternSize-OVERLAP;
            }

            if (directionVector[1] == 1){
                // It's ..._RIGHT
                col1 = patternSize-OVERLAP;
                col2 = 0;
            } else {
                // It's ..._LEFT
                col1 = 0;
                col2 = patternSize-OVERLAP;
            }

            for (int i = 0; i < OVERLAP; i++) {
                for (int j = 0; j < OVERLAP; j++) {
                    if (!pattern1[row1 + i][col1 + j].equals(pattern2[row2 + i][col2 + j]))
                        return false;
                }
            }
            return true;
        }

        if (directionVector[0] == 0){
            // It's horizontal
            toRow = patternSize;

            if (directionVector[1] == 1){
                // It's RIGHT
                col1 = patternSize-OVERLAP;
                col2 = 0;
            } else {
                // It's LEFT
                col1 = 0;
                col2 = patternSize-OVERLAP;
            }

            for (int i = 0; i < OVERLAP; i++) {
                for (int row = 0; row < toRow; row++) {
                    if (!pattern1[row][col1 + i].equals(pattern2[row][col2 + i]))
                        return false;
                }
            }

            return true;
        }

        // It's vertical
        toCol = patternSize;

        if (directionVector[0] == -1){
            // It's UP
            row1 = 0;
            row2 = patternSize-OVERLAP;
        } else {
            // It's DOWN
            row1 = patternSize-OVERLAP;
            row2 = 0;
        }

        for (int i = 0; i < OVERLAP; i++) {
            for (int col = 0; col < toCol; col++){
                if (!pattern1[row1 + i][col].equals(pattern2[row2 + i][col]))
                    return false;
            }
        }

        return true;
    }
}
