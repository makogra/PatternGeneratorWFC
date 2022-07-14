package com.mako.patterngeneratorwfc.wfc;

import java.util.ArrayList;
import java.util.List;

public class RotationAndReflection {

    private final Integer[][] inputPattern;
    private final int patternSize;

    public RotationAndReflection(Integer[][] inputPattern){
        this.inputPattern = inputPattern;
        this.patternSize = inputPattern.length;
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
}
