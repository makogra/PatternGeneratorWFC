package com.mako.patterngeneratorwfc.wfc;

import java.util.List;

public class AdjacencyRules {

    private final int totalNumberOfPatterns;
    private List<List<List<Integer>>> defaultPatternEnablers;
    private final List<Integer[][]> patternList;
    private final int patternSize;

    AdjacencyRules(List<Integer[][]> patternList, int totalNumberOfPatterns){
        this.patternList = patternList;
        this.totalNumberOfPatterns = totalNumberOfPatterns;
        this.patternSize = patternList.get(0).length;
    }
}
