package com.mako.patterngeneratorwfc.wfc;

import static org.junit.Assert.*;

import com.mako.patterngeneratorwfc.TileSet;

import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class PropagatorTest {

    private WFC wfc;

    @Before
    public void setUp() {
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
        boolean rotation = true;
        boolean reflection = true;
        List<String> map = new ArrayList<String>(){{
            add("_");
            add("G");
            add("C");
            add("S");
        }};
        TileSet tileSet = new TileSet("Test", valueGrid, map);

        wfc = new WFC(tileSet, patternSize, tilesOverLap, outputHeight, outputWidth, rotation, reflection);
    }
}