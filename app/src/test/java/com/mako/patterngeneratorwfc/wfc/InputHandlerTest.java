package com.mako.patterngeneratorwfc.wfc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class InputHandlerTest {

    String[][] inputGrid;
    int[][] inputGridValue;
    int patternSize;
    InputHandler inputHandler;

    @Before
    public void SetUp(){
        inputGrid = new String[][]{
                {"G", "G", "G", "G", "C", "S"},
                {"G", "G", "G", "C", "C", "S"},
                {"G", "G", "G", "C", "S", "S"},
                {"G", "G", "G", "C", "S", "S"},
                {"G", "G", "G", "C", "S", "S"}
        };
        inputGridValue = new int[][]{
                {0,0,0,0,1,2},
                {0,0,0,1,1,2},
                {0,0,0,1,2,2},
                {0,0,0,1,2,2},
                {0,0,0,1,2,2},
        };
        patternSize = 4;
        boolean rotation = true;
        boolean reflection = false;

        inputHandler = new InputHandler(inputGridValue, patternSize, rotation, reflection);
    }

    @Test
    public void getValueGrid() {
        int[][] shouldBe = new int[][]{
                {0,0,0,0,1,2},
                {0,0,0,1,1,2},
                {0,0,0,1,2,2},
                {0,0,0,1,2,2},
                {0,0,0,1,2,2},
        };

        assertArrayEquals(shouldBe, inputHandler.getValueGrid());
    }

    /*@Test
    public void getInputToValueMap() {
        List<String> shouldBe = new ArrayList<>();
        shouldBe.add("G");
        shouldBe.add("C");
        shouldBe.add("S");

        assertEquals(shouldBe, inputHandler.getInputToValueMap());
        //assertIterableEquals(shouldBe, inputHandler.getInputToValueMap());
    }

     */

    @Test
    public void getPatternList() {

        Integer[][][] shouldBeArray = new Integer[][][]{{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {1, 1, 1, 0}},
                {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {1, 1, 1, 1}},
                {{0, 0, 0, 0}, {0, 0, 0, 0}, {1, 1, 1, 0}, {2, 2, 1, 1}},
                {{0, 0, 0, 0}, {0, 0, 0, 0}, {1, 1, 1, 1}, {2, 2, 2, 1}},
                {{0, 0, 0, 0}, {0, 0, 0, 1}, {0, 0, 0, 1}, {0, 0, 0, 1}},
                {{0, 0, 0, 0}, {1, 1, 1, 0}, {2, 2, 1, 1}, {2, 2, 2, 2}},
                {{0, 0, 0, 0}, {1, 1, 1, 1}, {2, 2, 2, 1}, {2, 2, 2, 2}},
                {{0, 0, 0, 1}, {0, 0, 0, 1}, {0, 0, 0, 1}, {0, 0, 0, 1}},
                {{0, 0, 0, 1}, {0, 0, 1, 1}, {0, 0, 1, 2}, {0, 0, 1, 2}},
                {{0, 0, 1, 1}, {0, 0, 1, 2}, {0, 0, 1, 2}, {0, 0, 1, 2}},
                {{0, 0, 1, 2}, {0, 1, 1, 2}, {0, 1, 2, 2}, {0, 1, 2, 2}},
                {{0, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
                {{0, 1, 1, 2}, {0, 1, 2, 2}, {0, 1, 2, 2}, {0, 1, 2, 2}},
                {{1, 0, 0, 0}, {1, 0, 0, 0}, {1, 0, 0, 0}, {0, 0, 0, 0}},
                {{1, 0, 0, 0}, {1, 0, 0, 0}, {1, 0, 0, 0}, {1, 0, 0, 0}},
                {{1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
                {{1, 1, 2, 2}, {0, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}},
                {{1, 2, 2, 2}, {1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}},
                {{2, 1, 0, 0}, {2, 1, 0, 0}, {1, 1, 0, 0}, {1, 0, 0, 0}},
                {{2, 1, 0, 0}, {2, 1, 0, 0}, {2, 1, 0, 0}, {1, 1, 0, 0}},
                {{2, 2, 1, 0}, {2, 2, 1, 0}, {2, 1, 1, 0}, {2, 1, 0, 0}},
                {{2, 2, 1, 0}, {2, 2, 1, 0}, {2, 2, 1, 0}, {2, 1, 1, 0}},
                {{2, 2, 2, 2}, {1, 1, 2, 2}, {0, 1, 1, 1}, {0, 0, 0, 0}},
                {{2, 2, 2, 2}, {1, 2, 2, 2}, {1, 1, 1, 1}, {0, 0, 0, 0}}};

        List<Integer[][]> actual = inputHandler.getPatternList();

        assertEquals(shouldBeArray.length, actual.size());

        for (Integer[][] shouldBePattern : shouldBeArray){
            assertTrue(actual.contains(shouldBePattern));
        }
    }

    @Test
    public void getTotalNumberOfPatterns() {
        assertEquals(24, inputHandler.getTotalNumberOfPatterns());
    }

}