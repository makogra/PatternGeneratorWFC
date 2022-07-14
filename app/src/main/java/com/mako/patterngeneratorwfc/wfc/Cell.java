package com.mako.patterngeneratorwfc.wfc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.random.Random;

public class Cell {

    private boolean isObserved;
    // index correspondent's to pattern id
    private boolean[] possiblePatterns;
    // patternEnablers.get(patternIndex).get(directionIndex) = List of all patterns "patternsEnablers" in direction (directionIndex) that make pattern (patternIndex) possible.
    // patternEnablers.get(patternIndex).get(directionIndex) = null  is equivalent to possiblePatterns[patternIndex] = false and implies that patternEnablers.get(patternIndex) = null
    // It means that if there are non patternsEnablers in any direction(directionIndex), pattern (patternIndex) isn't possible to occur in this cell
    private List<List<List<Integer>>> patternEnablers;
    // Number of patterns that are still possible
    private int numberOfPossiblePatterns;
    private final int totalNumberOfPatterns;
    // Number that represent uncertainty. The fewer possibilities of patterns, lower the value
    private double entropy;
    // small number added to entropy to more easily distinguish the lower entropy between two cells
    private final double entropyNoise;
    private final double[] relativeFrequency;
    private final int row;
    private final int col;
    private final Propagator propagator;

    Cell(int row, int col, List<List<List<Integer>>> defaultPatternEnablers, int totalNumberOfPossiblePatterns, double[] relativeFrequency, Propagator propagator) {
        this.row = row;
        this.col = col;
        this.isObserved = false;
        this.relativeFrequency = relativeFrequency;
        this.totalNumberOfPatterns = totalNumberOfPossiblePatterns;
        this.numberOfPossiblePatterns = totalNumberOfPatterns;
        this.propagator = propagator;
        this.patternEnablers = clonePatternEnables(defaultPatternEnablers);

        this.possiblePatterns = new boolean[numberOfPossiblePatterns];
        Arrays.fill(possiblePatterns, true);
        updatePossiblePatterns();
        updateNumberOfPossiblePatterns();

        this.entropyNoise = Random.Default.nextDouble(0.00001);
        updateEntropy();
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

            relativeFrequencyHelper = relativeFrequency[patternIndex];
            totalWeight += relativeFrequencyHelper;
            sumOfWeightLogWeight += relativeFrequencyHelper * Math.log(relativeFrequencyHelper);
        }

        entropy = Math.log(totalWeight) - (sumOfWeightLogWeight / totalWeight) + entropyNoise;
    }

    public boolean isObserved() {
        return false;
    }

    private void updatePossiblePatterns() {
        for (int i = 0; i < possiblePatterns.length; i++) {
            if (patternEnablers.get(i) == null) {
                possiblePatterns[i] = false;
                continue;
            }
            for (int j = 0; j < Directions.getTOTAL_DIRECTIONS_NUMBER(); j++) {
                if (patternEnablers.get(i).get(j).size() == 0) {
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
            isObserved = true;
    }
}
