package com.mako.patterngeneratorwfc.database.convertors;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;

public class NestedIntArrayConvertorTest {

    @Test
    public void fromString() {
        String s = "[[12, 1, 4, 7], [54, 7, 8, 9]]";
        int[][] intArr = new int[][]{
                {12,1,4,7},
                {54,7,8,9}
        };
        assertTrue(Arrays.deepEquals(intArr, NestedIntArrayConvertor.fromString(s)));
    }

    @Test
    public void testToString() {
        String s = "[[12, 1, 4, 7], [54, 7, 8, 9]]";
        int[][] intArr = new int[][]{
                {12,1,4,7},
                {54,7,8,9}
        };
        assertEquals(s, NestedIntArrayConvertor.intArrToString(intArr));
    }

    @Test
    public void nullFromString_correct(){
        assertNull(NestedIntArrayConvertor.fromString(null));
    }

    @Test
    public void nullIntArrToString_correct(){
        assertNull(NestedIntArrayConvertor.intArrToString(null));
    }

    @Test
    public void emptyFromString_correct(){
        assertTrue(Arrays.deepEquals(new int[0][0], NestedIntArrayConvertor.fromString("")));
        assertTrue(Arrays.deepEquals(new int[][]{}, NestedIntArrayConvertor.fromString("")));
    }

    @Test
    public void emptyIntArrToString_correct(){
        assertEquals("[]", NestedIntArrayConvertor.intArrToString(new int[0][0]));
        assertEquals("[]", NestedIntArrayConvertor.intArrToString(new int[][]{}));
    }

    @Test
    public void bothMethodsWorkTogether_correct(){
        String s = "[[12, 1, 4, 7], [54, 7, 8, 9]]";
        int[][] intArr = new int[][]{
                {12,1,4,7},
                {54,7,8,9}
        };

        assertEquals(s, NestedIntArrayConvertor.intArrToString(NestedIntArrayConvertor.fromString(s)));

        assertTrue(Arrays.deepEquals(intArr, NestedIntArrayConvertor.fromString(NestedIntArrayConvertor.intArrToString(intArr))));
    }

}