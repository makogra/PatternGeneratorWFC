package com.mako.patterngeneratorwfc.wfc;

import java.util.ArrayList;
import java.util.List;

public class RotationAndReflection {

    private final Integer[][] inputPattern;
    private final int patternSize;
    private final boolean enableRotation;
    private final boolean enableReflection;

    public RotationAndReflection(Integer[][] inputPattern, boolean enableRotation, boolean enableReflection){
        if (inputPattern == null)
            throw new IllegalArgumentException("InputPattern is null");
        if (inputPattern.length == 0)
            throw new IllegalArgumentException("InputPattern is empty");
        if (inputPattern.length != inputPattern[0].length)
            throw new IllegalArgumentException("InputPattern is NxM instead of NxN");
        this.inputPattern = inputPattern;
        this.patternSize = inputPattern.length;
        this.enableRotation = enableRotation;
        this.enableReflection = enableReflection;
    }

    public List<Integer[][]> getAll(){
        List<Integer[][]> allPatterns = new ArrayList<>();
        int size;

        allPatterns.add(inputPattern);

        if (enableRotation){
            size = allPatterns.size();
            int numberOfRotations = 3;
            Integer[][] previousPattern;
            for (int i = 0; i < size; i++) {
                previousPattern = allPatterns.get(i);
                for (int j = 0; j < numberOfRotations; j++) {
                    previousPattern = rightRotate(previousPattern);
                    allPatterns.add(previousPattern);
                }
            }
        }

        if (enableReflection){
            size = allPatterns.size();
            for (int i = 0; i < size; i++) {
                allPatterns.add(horizontalMirror(allPatterns.get(i)));
                allPatterns.add(verticalMirror(allPatterns.get(i)));
            }
        }
        return allPatterns;
    }

    public List<Integer[][]> getAllRotationsAndReflections(){
        List<Integer[][]> allPatterns = new ArrayList<>();

        allPatterns.add(inputPattern);
        allPatterns.add(horizontalMirror(inputPattern));

        for (int i = 0; i < 2; i++) {
            allPatterns.add(verticalMirror(allPatterns.get(i)));
        }

        for (int i = 0; i < 4; i++) {
            allPatterns.add(diagonalRotation(allPatterns.get(i)));
        }

        return allPatterns;
    }

    public Integer[][] verticalMirror(Integer[][] pattern){
        Integer[][] mirrored = new Integer[patternSize][patternSize];

        for (int i = 0; i < patternSize; i++) {
            for (int j = 0; j < patternSize; j++) {
                mirrored[i][patternSize-j-1] = pattern[i][j];
            }
        }
        return mirrored;
    }

    public Integer[][] horizontalMirror(Integer[][] pattern){
        Integer[][] mirrored = new Integer[patternSize][patternSize];

        for (int i = 0; i < patternSize; i++) {
            for (int j = 0; j < patternSize; j++) {
                mirrored[patternSize-i-1][j] = pattern[i][j];
            }
        }
        return mirrored;
    }

    public Integer[][] diagonalRotation(Integer[][] pattern){
        Integer[][] rotated = new Integer[patternSize][patternSize];
        for (int i = 0; i < patternSize; i++) {
            for (int j = 0; j < patternSize; j++) {
                rotated[j][i] = pattern[i][j];
            }
        }
        return rotated;
    }

    //function to rotate the matrix by 90 degrees clockwise
    private Integer[][] rightRotate(Integer[][] matrix) {
        Integer[][] outputMatrix = new Integer[patternSize][patternSize];
        //determines the transpose of the matrix
        for(int i=0;i<patternSize;i++)
        {
            for(int j=i;j<patternSize;j++)
            {
                outputMatrix[i][j] = matrix[j][i];
                outputMatrix[j][i] = matrix[i][j];
            }
        }

        //then we reverse the elements of each row
        int temp;
        for(int i=0;i<patternSize;i++)
        {
            //logic to reverse each row i.e 1D Array.
            int low = 0, high = patternSize-1;
            while(low < high)
            {
                temp = outputMatrix[i][high];
                outputMatrix[i][high] =  outputMatrix[i][low];
                outputMatrix[i][low] = temp;
                low++;
                high--;
            }
        }
        return outputMatrix;
    }
}
