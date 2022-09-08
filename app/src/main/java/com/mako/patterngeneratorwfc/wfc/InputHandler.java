package com.mako.patterngeneratorwfc.wfc;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {


    private final int[][] valueGrid;
    private final int patternSize;
    private List<Integer[][]> patternList;
    private double[] relativeFrequency;

    public InputHandler(int[][] valueGrid, int patternSize){
        this.valueGrid = valueGrid;
        this.patternSize = patternSize;
        makePatternGridAndPatternList();
    }

    public int[][] getValueGrid() {
        return valueGrid;
    }

    public List<Integer[][]> getPatternList() {
        return patternList;
    }

    public double[] getRelativeFrequency() {
        return relativeFrequency;
    }

    public int getTotalNumberOfPatterns() {
        return patternList.size();
    }

    private void makePatternGridAndPatternList(){
        int patternGridHeight = valueGrid.length + 1 - patternSize;
        int patternGridWidth = valueGrid[0].length + 1 - patternSize;
        this.patternList = new ArrayList<Integer[][]>(){
            @Override
            public boolean add(Integer[][] integers) {
                if (this.contains(integers))
                    return false;
                return super.add(integers);
            }

            @Override
            public boolean contains(Object o) {
                if (!(o instanceof Integer[][]))
                    return false;

                Integer[][] ints = (Integer[][]) o;

                LABEL: for (Integer[][] integers : this){
                    for (int i = 0; i < integers.length; i++) {
                        for (int j = 0; j < integers[0].length; j++) {
                            if (integers[i][j].intValue() != ints[i][j].intValue())
                                continue LABEL;
                        }
                    }
                    return true;
                }
                return false;
            }

            @Override
            public int indexOf(Object o) {
                if (!(o instanceof Integer[][]))
                    return -1;

                Integer[][] integers = (Integer[][]) o;

                LABEL: for (int index = 0; index < this.size(); index++) {
                    for (int i = 0; i < integers.length; i++) {
                        for (int j = 0; j < integers[0].length; j++) {
                            if (!integers[i][j].equals(this.get(index)[i][j]))
                                continue LABEL;
                        }
                    }
                    return index;
                }
                return -1;
            }
        };

        Integer[][] pattern;
        int patternIndex;
        List<Integer> listOfPatternsWithDuplicates = new ArrayList<>();
        List<Integer[][]> allRotations;
        RotationAndReflection rotationAndReflection;

        for (int row = 0; row < patternGridHeight; row++) {
            for (int col = 0; col < patternGridWidth; col++) {
                pattern = new Integer[patternSize][patternSize];
                for (int i = 0; i < patternSize; i++) {
                    for (int j = 0; j < patternSize; j++) {
                        pattern[i][j] = valueGrid[row+i][col+j];
                    }
                }
                rotationAndReflection = new RotationAndReflection(pattern);
                allRotations = rotationAndReflection.getAllRotationsAndReflections();
                for (Integer[][] rotation : allRotations){
                    if (patternList.contains(rotation)){
                        patternIndex = patternList.indexOf(rotation);
                    } else {
                        patternIndex = patternList.size();
                        patternList.add(rotation);
                    }
                    listOfPatternsWithDuplicates.add(patternIndex);
                }
            }
        }
        defineRelativeFrequency(listOfPatternsWithDuplicates);

    }

    private void defineRelativeFrequency(List<Integer> list){
        int totalNumberOfPatternsWithDuplicates = list.size();
        int[] frequency = new int[patternList.size()];
        relativeFrequency = new double[patternList.size()];

        for (Integer pattern : list){
            frequency[pattern]++;
        }

        for (int i = 0; i < frequency.length; i++) {
            relativeFrequency[i] = frequency[i] / (double)totalNumberOfPatternsWithDuplicates;
        }
    }

}
