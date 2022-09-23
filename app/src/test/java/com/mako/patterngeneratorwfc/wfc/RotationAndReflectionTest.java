package com.mako.patterngeneratorwfc.wfc;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;

public class RotationAndReflectionTest {

    Integer[][] input1 = new Integer[][]{
            {1,2},
            {3,4}
    };

    Integer[][] horizontalMirror1 = new Integer[][]{
            {3,4},
            {1,2}
    };

    Integer[][] verticalMirror1 = new Integer[][]{
            {2,1},
            {4,3}
    };

    Integer[][] diagonalReflection1 = new Integer[][]{
            {1,3},
            {2,4}
    };


    Integer[][] input2 = new Integer[][]{
            {1,2,3},
            {4,5,6},
            {7,8,9}
    };

    Integer[][] horizontalMirror2 = new Integer[][]{
            {7,8,9},
            {4,5,6},
            {1,2,3}
    };

    Integer[][] verticalMirror2 = new Integer[][]{
            {3,2,1},
            {6,5,4},
            {9,8,7}
    };

    Integer[][] diagonalReflection2 = new Integer[][]{
            {1,4,7},
            {2,5,8},
            {3,6,9}
    };

    boolean enableRotation = true;
    boolean enableReflection = true;

    RotationAndReflection test1 = new RotationAndReflection(input1, enableRotation, enableReflection);
    RotationAndReflection test2 = new RotationAndReflection(input2, enableRotation, enableReflection);

    @Test
    public void getAllRotationsAndReflections() {

        assertEquals(8, test1.getAllRotationsAndReflections().size());
    }

    @Test
    public void verticalMirror() {
        assertTrue(Arrays.deepEquals(verticalMirror1, test1.verticalMirror(input1)));
        assertTrue(Arrays.deepEquals(verticalMirror2, test2.verticalMirror(input2)));
    }

    @Test
    public void horizontalMirror() {
        assertTrue(Arrays.deepEquals(horizontalMirror1, test1.horizontalMirror(input1)));
        assertTrue(Arrays.deepEquals(horizontalMirror2, test2.horizontalMirror(input2)));
    }

    @Test
    public void diagonalRotation() {
        assertTrue(Arrays.deepEquals(diagonalReflection1, test1.diagonalRotation(input1)));
        assertTrue(Arrays.deepEquals(diagonalReflection2, test2.diagonalRotation(input2)));
    }

}