package com.mako.patterngeneratorwfc.wfc;

import static android.util.ArraysDeep.deepEqualIntArr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RotationAndReflectionTest {

    private static final String TAG = RotationAndReflectionTest.class.getName();
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

    Integer[][] rotation1_90 = new Integer[][]{
            {3,1},
            {4,2}
    };

    Integer[][] rotation1_180 = new Integer[][]{
            {4,3},
            {2,1}
    };

    Integer[][] rotation1_270 = new Integer[][]{
            {2,4},
            {1,3}
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

    Integer[][] rotation2_90 = new Integer[][]{
            {7,4,1},
            {8,5,2},
            {9,6,3}
    };

    Integer[][] rotation2_180 = new Integer[][]{
            {9,8,7},
            {6,5,4},
            {3,2,1}
    };

    Integer[][] rotation2_270 = new Integer[][]{
            {3,6,9},
            {2,5,8},
            {1,4,7}
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

    @Test
    public void allRotations(){
        boolean rotation = true;
        boolean reflection = false;
        RotationAndReflection rotationTest_1 = new RotationAndReflection(input1, rotation, reflection);
        RotationAndReflection rotationTest_2 = new RotationAndReflection(input2, rotation, reflection);
        List<Integer[][]> shouldBe_1 = new ArrayList<Integer[][]>(){{
            add(input1);
            add(rotation1_90);
            add(rotation1_180);
            add(rotation1_270);
        }};
        List<Integer[][]> shouldBe_2 = new ArrayList<Integer[][]>(){{
            add(input2);
            add(rotation2_90);
            add(rotation2_180);
            add(rotation2_270);
        }};
        List<Integer[][]> was_1 = rotationTest_1.getAll();
        List<Integer[][]> was_2 = rotationTest_2.getAll();
        Log.d(TAG, "allRotations: shouldBe");
        shouldBe_1.forEach((integers -> System.out.println(Arrays.deepToString(integers))));
        Log.d(TAG, "allRotations: was");
        was_1.forEach((integers -> System.out.println(Arrays.deepToString(integers))));
        Log.d(TAG, "allRotations: 1 " + shouldBe_1);
        Log.d(TAG, "allRotations: 1 " + was_1);
        assertTrue(deepEqualIntArr(shouldBe_1, was_1));
        Log.d(TAG, "allRotations: 2 " + shouldBe_2);
        Log.d(TAG, "allRotations: 2 " + was_2);
        assertTrue(deepEqualIntArr(shouldBe_2, was_2));


    }

}