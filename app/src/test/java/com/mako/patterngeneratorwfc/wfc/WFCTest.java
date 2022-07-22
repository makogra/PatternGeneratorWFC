package com.mako.patterngeneratorwfc.wfc;

import static org.junit.Assert.*;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

public class WFCTest {

    String[][] tempInputGrid;
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
        wfc = new WFC(tempInputGrid, 3, 1, 64, 32);
    }

    @Test
    public void runTest(){
        wfc.run(30);
    }
}