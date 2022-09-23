package com.mako.patterngeneratorwfc.wfc;

import static org.junit.Assert.*;

import android.util.Log;

import com.mako.patterngeneratorwfc.TileSet;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WFCTest {

    String[][] tempInputGrid;
    int[][] tempInputGridValue;
    WFC wfc;

    @Before
    public void setUp(){
        tempInputGrid =  new String[][]{
                {"G","G","G","G","C","S","S","S"},
                {"G","G","G","C","C","S","S","S"},
                {"G","G","G","C","S","S","S","S"},
                {"G","G","G","C","S","S","S","S"},
                {"G","G","G","C","S","S","S","S"}
        };
        tempInputGridValue =  new int[][]{
                {1,1,1,1,2,3,3,3},
                {1,1,1,2,2,3,3,3},
                {1,1,1,2,3,3,3,3},
                {1,1,1,2,3,3,3,3},
                {1,1,1,2,3,3,3,3}
        };
        List<String> map = new ArrayList<String>(){{
            add("_");
            add("G");
            add("C");
            add("S");
        }};
        TileSet tileSet = new TileSet("Test", tempInputGridValue, map);
        
        wfc = new WFC(tileSet, 3, 1, 64, 32, true, true);
    }

    @Test
    public void runTest(){
        wfc.run(30);
    }
}