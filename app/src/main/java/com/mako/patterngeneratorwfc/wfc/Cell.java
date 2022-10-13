package com.mako.patterngeneratorwfc.wfc;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import kotlin.random.Random;

public class Cell implements Comparable<Cell> {

    private static final String TAG = Cell.class.getName();
    private static List<List<List<Integer>>> sDefaultPatternEnablers;
    private static int sTotalNumberOfPossiblePatterns;
    private static double[] sRelativeFrequency;
    private static Propagator sPropagator;

    private boolean isObserved;
    // index correspondent's to pattern id
    private boolean[] possiblePatterns;
    // patternEnablers.get(patternIndex).get(directionIndex) = List of all patterns "patternsEnablers" in direction (directionIndex) that make pattern (patternIndex) possible.
    // patternEnablers.get(patternIndex).get(directionIndex) = null  is equivalent to possiblePatterns[patternIndex] = false and implies that patternEnablers.get(patternIndex) = null
    // It means that if there are non patternsEnablers in any direction(directionIndex), pattern (patternIndex) isn't possible to occur in this cell
    private List<List<List<Integer>>> patternEnablers;
    // Number of patterns that are still possible
    private int numberOfPossiblePatterns;
    // Number that represent uncertainty. The fewer possibilities of patterns, lower the value
    private double entropy;
    // small number added to entropy to more easily distinguish the lower entropy between two cells
    private final double entropyNoise;
    private final int row;
    private final int col;



    Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.isObserved = false;
        this.numberOfPossiblePatterns = sTotalNumberOfPossiblePatterns;
        this.patternEnablers = clonePatternEnables(sDefaultPatternEnablers);

        this.possiblePatterns = new boolean[numberOfPossiblePatterns];
        Arrays.fill(possiblePatterns, true);
        updatePossiblePatterns();
        updateNumberOfPossiblePatterns();

        this.entropyNoise = Random.Default.nextDouble(0.00001);
        updateEntropy();
    }

    public static void setsDefaultPatternEnablers(List<List<List<Integer>>> sDefaultPatternEnablers) {
        Cell.sDefaultPatternEnablers = sDefaultPatternEnablers;
    }

    public static void setsTotalNumberOfPossiblePatterns(int sTotalNumberOfPossiblePatterns) {
        Cell.sTotalNumberOfPossiblePatterns = sTotalNumberOfPossiblePatterns;
    }

    public static void setsRelativeFrequency(double[] sRelativeFrequency) {
        Cell.sRelativeFrequency = sRelativeFrequency;
    }

    public static void setsPropagator(Propagator sPropagator) {
        Cell.sPropagator = sPropagator;
    }

    //Getters
    public double getEntropy() {
        return entropy;
    }

    public int getNumberOfPossiblePatterns() {
        return numberOfPossiblePatterns;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isObserved() {
        return isObserved;
    }

    private List<List<List<Integer>>> clonePatternEnables(List<List<List<Integer>>> defaultPatternEnablers) {
        List<List<List<Integer>>> copy = new ArrayList<>(defaultPatternEnablers.size());
        for (int patternId = 0; patternId < defaultPatternEnablers.size(); patternId++) {
            copy.add(new ArrayList<>());
            for (int direction = 0; direction < defaultPatternEnablers.get(patternId).size(); direction++) {
                copy.get(patternId).add(new ArrayList<>());
                for (int patternEnabler = 0; patternEnabler < defaultPatternEnablers.get(patternId).get(direction).size(); patternEnabler++) {
                    copy.get(patternId).get(direction).add(defaultPatternEnablers.get(patternId).get(direction).get(patternEnabler));
                }
            }
        }
        return copy;
    }

    private void updateEntropy() {
        double totalWeight = 0.0f;
        double sumOfWeightLogWeight = 0.0f;
        double relativeFrequencyHelper;
        for (int patternIndex = 0; patternIndex < possiblePatterns.length; patternIndex++) {
            if (!possiblePatterns[patternIndex])
                continue;

            relativeFrequencyHelper = sRelativeFrequency[patternIndex];
            totalWeight += relativeFrequencyHelper;
            sumOfWeightLogWeight += relativeFrequencyHelper * Math.log(relativeFrequencyHelper);
        }

        entropy = Math.log(totalWeight) - (sumOfWeightLogWeight / totalWeight) + entropyNoise;
    }

    private void updatePossiblePatterns() {
        for (int i = 0; i < possiblePatterns.length; i++) {
            if (patternEnablers.get(i) == null) {
                possiblePatterns[i] = false;
                continue;
            }
            for (int j = 0; j < Directions.getTOTAL_DIRECTIONS_NUMBER(); j++) {
                if (patternEnablers.get(i).get(j) == null || patternEnablers.get(i).get(j).size() == 0) {
                    possiblePatterns[i] = false;
                    patternEnablers.set(i, null);
                    break;
                }
            }

        }
    }

    private void updateNumberOfPossiblePatterns(){
        int sum = 0;
        for (boolean pattern : possiblePatterns){
            if (pattern)
                sum++;
        }
        this.numberOfPossiblePatterns = sum;
    }

    void update(){
        updatePossiblePatterns();
        updateNumberOfPossiblePatterns();
        updateEntropy();
        if (numberOfPossiblePatterns == 0)
            Log.w(TAG, "update: numberOfPossiblePatterns = 0");
            //isObserved = true;
    }

    public void removePatternFromPatternEnablers(int directionIndex, int patternIndex) {
        boolean update = false;
        List<Integer> helperList;
        for (int patternId = 0; patternId < possiblePatterns.length; patternId++) {
            if (!possiblePatterns[patternId] || patternEnablers.get(patternId) == null) {
                continue;
            }
            helperList = patternEnablers.get(patternId).get(directionIndex);
            if (helperList.remove((Integer) patternIndex)) {
                if (helperList.isEmpty()) {
                    update = true;
                    sPropagator.addToPropagate(row, col, patternId, true);
                }
            }
        }
        if (update) {
            update();
        }
    }

    void removePatternEnablesExceptPattern(int directionIndex, int patternIndex) {
        boolean update = false;
        List<Integer> listOfPatternEnables;
        List<Integer> listOfPatternsToRemove;
        for (int patternId = 0; patternId < possiblePatterns.length; patternId++) {
            if (!possiblePatterns[patternId] || patternEnablers.get(patternId) == null) {
                continue;
            }

            listOfPatternsToRemove = new ArrayList<>();
            listOfPatternEnables = patternEnablers.get(patternId).get(directionIndex);

            for (Integer pattern : listOfPatternEnables) {
                if (pattern == patternIndex) {
                    continue;
                }
                listOfPatternsToRemove.add(pattern);


            }

            patternEnablers.get(patternId).get(directionIndex).removeAll(listOfPatternsToRemove);

            if (listOfPatternEnables.isEmpty()) {
                update = true;
                sPropagator.addToPropagate(row, col, patternId, true);
            }

        }
        if (update) {
            update();
        }
    }

    public int observe() {
        if (isObserved) {
            // Cell is already observed
            return -2;
        }
        if (numberOfPossiblePatterns == 0) {
            // Contradiction. Any pattern can't be placed
            return -1;
        }


        int patternIndex = getPatternIndex();



        if (patternIndex < 0 || patternIndex >= sTotalNumberOfPossiblePatterns) {
            //Error
            return -3;
        }
        // success
        setAllPossiblePatternsToFalse();
        isObserved = true;
        return patternIndex;
    }

    private void setAllPossiblePatternsToFalse() {
        Arrays.fill(possiblePatterns, false);
        Collections.fill(patternEnablers, null);
    }

    private int getPatternIndex() {
        Random random = Random.Default;
        updatePossiblePatterns();
        updateNumberOfPossiblePatterns();
        int randomIndex = random.nextInt(numberOfPossiblePatterns);

        for (int patternIndex = 0; patternIndex < possiblePatterns.length; patternIndex++) {
            if (!possiblePatterns[patternIndex])
                continue;
            if (randomIndex-- <= 0)
                return patternIndex;
        }
        return possiblePatterns.length-1;
    }

    @Override
    public int compareTo(Cell o) {
        return Double.compare(this.entropy, o.entropy);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "isObserved=" + isObserved +
                ", possiblePatterns=" + Arrays.toString(possiblePatterns) +
                ", numberOfPossiblePatterns=" + numberOfPossiblePatterns +
                ", entropy=" + entropy +
                ", row=" + row +
                ", col=" + col +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Cell)){
            return false;
        }
        Cell c = (Cell) obj;
        return c.row == this.row && c.col == this.col;
    }
}
