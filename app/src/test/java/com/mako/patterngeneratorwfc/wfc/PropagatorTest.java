package com.mako.patterngeneratorwfc.wfc;

import static org.junit.Assert.*;

import org.junit.Before;

public class PropagatorTest {

    private WFC wfc;

    @Before
    public void setUp() throws Exception {
        String[][] inputGrid = new String[][]{
                {"G","G","G","G","C","S","S","S"},
                {"G","G","G","C","C","S","S","S"},
                {"G","G","G","C","S","S","S","S"},
                {"G","G","G","C","S","S","S","S"},
                {"G","G","G","C","S","S","S","S"},
        };
        int[][] valueGrid = new int[][]{
                {1,1,1,1,2,3,3,3},
                {1,1,1,2,2,3,3,3},
                {1,1,1,2,3,3,3,3},
                {1,1,1,2,3,3,3,3},
                {1,1,1,2,3,3,3,3},
        };
        int outputHeight = 64;
        int outputWidth = 64;
        int patternSize = 3;
        int tilesOverLap = 1;
        wfc = new WFC(inputGrid, patternSize, tilesOverLap, outputHeight, outputWidth);
    }
}