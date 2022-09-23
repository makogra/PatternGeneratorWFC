package com.mako.patterngeneratorwfc.wfc;

import static org.junit.Assert.*;

import androidx.annotation.Nullable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class InputHandlerTest {

    String[][] inputGrid;
    int[][] inputGridValue;
    int patternSize;
    InputHandler inputHandler;

    @Before
    public void SetUp(){
        inputGrid = new String[][]{
                {"G", "G", "G", "C", "C", "S"},
                {"G", "G", "C", "C", "S", "S"},
                {"G", "G", "C", "S", "S", "S"}
        };
        inputGridValue = new int[][]{
                {0,0,0,1,1,2},
                {0,0,1,1,2,2},
                {0,0,1,2,2,2},
        };
        patternSize = 2;
        boolean rotation = true;
        boolean reflection = true;

        inputHandler = new InputHandler(inputGridValue, patternSize, rotation, reflection);
    }

    @Test
    public void getValueGrid() {
        int[][] shouldBe = new int[][]{
                {0,0,0,1,1,2},
                {0,0,1,1,2,2},
                {0,0,1,2,2,2},
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
        List<Integer[][]> shouldBe = new ArrayList<Integer[][]>(){
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
        };
        shouldBe.add(new Integer[][]{{0,0}, {0,0}});
        shouldBe.add(new Integer[][]{{0,0}, {0,1}});
        shouldBe.add(new Integer[][]{{0,1}, {1,1}});
        shouldBe.add(new Integer[][]{{1,1}, {1,2}});
        shouldBe.add(new Integer[][]{{1,2}, {2,2}});
        shouldBe.add(new Integer[][]{{0,1}, {0,1}});
        shouldBe.add(new Integer[][]{{2,2}, {2,2}});

        //Rotations and Reflections

        shouldBe.add(new Integer[][]{{0,0}, {1,0}});
        shouldBe.add(new Integer[][]{{0,1}, {0,0}});
        shouldBe.add(new Integer[][]{{1,0}, {0,0}});

        shouldBe.add(new Integer[][]{{1,0}, {1,1}});
        shouldBe.add(new Integer[][]{{1,1}, {0,1}});
        shouldBe.add(new Integer[][]{{1,1}, {1,0}});

        shouldBe.add(new Integer[][]{{1,1}, {2,1}});
        shouldBe.add(new Integer[][]{{1,2}, {1,1}});
        shouldBe.add(new Integer[][]{{2,1}, {1,1}});

        shouldBe.add(new Integer[][]{{2,1}, {2,2}});
        shouldBe.add(new Integer[][]{{2,2}, {1,2}});
        shouldBe.add(new Integer[][]{{2,2}, {2,1}});

        shouldBe.add(new Integer[][]{{1,0}, {1,0}});
        shouldBe.add(new Integer[][]{{1,1}, {0,0}});
        shouldBe.add(new Integer[][]{{0,0}, {1,1}});


        List<Integer[][]> actual = inputHandler.getPatternList();

        assertEquals(shouldBe.size(), actual.size());

        Integer[][] actualPattern;
        for (int i = 0; i < actual.size(); i++) {
            actualPattern = actual.get(i);
            assertTrue(shouldBe.contains(actualPattern));
        }
    }

    @Test
    public void getTotalNumberOfPatterns() {

        assertEquals(22, inputHandler.getTotalNumberOfPatterns());
    }

}