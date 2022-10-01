package com.mako.patterngeneratorwfc.wfc;

import static android.util.ArraysDeep.deepEqual;
import static org.junit.Assert.*;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdjacencyRulesTest {

    private static final String TAG = AdjacencyRulesTest.class.getName();
    private AdjacencyRules adjacencyRules;
    private List<Integer[][]> patterList;

    @Before
    public void setUp() throws Exception {
        Log.d(TAG, "setUp: ");
        patterList = new ArrayList<Integer[][]>(){{
            add(new Integer[][]{
                    {1,1,1},
                    {2,2,2},
                    {3,3,3}
            });
            add(new Integer[][]{
                    {1,1,1},
                    {1,1,1},
                    {1,1,1}
            });
            add(new Integer[][]{
                    {1,3,1},
                    {1,3,2},
                    {1,3,3}
            });
        }};
        int overLap = 1;
        adjacencyRules = new AdjacencyRules(patterList, overLap);
    }

    private List<List<List<Integer>>> initShouldBe(){
        int totalNumberOfPatterns = patterList.size();
        int numberOfDirections = Directions.getTOTAL_DIRECTIONS_NUMBER();
        List<List<List<Integer>>> shouldBe = new ArrayList<>(totalNumberOfPatterns);
        List<Integer> helperPatternEnabler;
        List<List<Integer>> helperDirections;
        for (int i = 0; i < totalNumberOfPatterns; i++) {
            helperDirections = new ArrayList<>(numberOfDirections);
            for (int j = 0; j < numberOfDirections; j++) {
                helperPatternEnabler = new ArrayList<Integer>(){
                    @Override
                    public boolean add(Integer integer) {
                        if (this.contains(integer))
                            return false;
                        return super.add(integer);
                    }
                };
                helperDirections.add(helperPatternEnabler);
            }
            shouldBe.add(helperDirections);
        }
        return shouldBe;
    }

    @Test
    public void getDefaultPatternEnablers() {

        List<List<List<Integer>>> was = adjacencyRules.getDefaultPatternEnablers();
        List<List<List<Integer>>> shouldBe = initShouldBe();
        // PATTERN NR 0
        //              up
        shouldBe.get(0).get(0).add(1);
        //              up_right
        shouldBe.get(0).get(1).add(1);
        shouldBe.get(0).get(1).add(2);
        //              right
        shouldBe.get(0).get(2).add(0);
        //              left
        shouldBe.get(0).get(6).add(0);
        shouldBe.get(0).get(6).add(2);
        //              up_left
        shouldBe.get(0).get(7).add(1);

        // PATTERN NR 1
        //              up
        shouldBe.get(1).get(0).add(1);
        //              up_right
        shouldBe.get(1).get(1).add(1);
        shouldBe.get(1).get(1).add(2);
        //              right
        shouldBe.get(1).get(2).add(1);
        shouldBe.get(1).get(2).add(2);
        //              down_right
        shouldBe.get(1).get(3).add(0);
        shouldBe.get(1).get(3).add(1);
        shouldBe.get(1).get(3).add(2);
        //              down
        shouldBe.get(1).get(4).add(0);
        shouldBe.get(1).get(4).add(1);
        //              down_left
        shouldBe.get(1).get(5).add(0);
        shouldBe.get(1).get(5).add(1);
        shouldBe.get(1).get(5).add(2);
        //              left
        shouldBe.get(1).get(6).add(1);
        //              up_left
        shouldBe.get(1).get(7).add(1);


        // PATTERN NR 3
        //              up_right
        shouldBe.get(2).get(1).add(1);
        shouldBe.get(2).get(1).add(2);
        //              right
        shouldBe.get(2).get(2).add(0);
        //              down_left
        shouldBe.get(2).get(5).add(0);
        shouldBe.get(2).get(5).add(1);
        shouldBe.get(2).get(5).add(2);
        //              left
        shouldBe.get(2).get(6).add(1);
        //              up_left
        shouldBe.get(2).get(7).add(1);


        Log.d(TAG, "getDefaultPatternEnablers: " + shouldBe);
        Log.d(TAG, "getDefaultPatternEnablers: " + was);
        assertTrue(deepEqual(shouldBe, was));
    }

    @Test
    public void patternsEnablersTheSame(){
        patterList = new ArrayList<Integer[][]>() {{
            add(new Integer[][]{
                    {1, 1, 1},
                    {1, 1, 1},
                    {1, 1, 1}
            });
            add(new Integer[][]{
                    {1, 1, 1},
                    {1, 2, 1},
                    {1, 1, 1}
            });
            add(new Integer[][]{
                    {1, 1, 1},
                    {1, 1, 1},
                    {1, 1, 1}
            });
        }};
        adjacencyRules = new AdjacencyRules(patterList, 1);

        List<List<List<Integer>>> was = adjacencyRules.getDefaultPatternEnablers();
        List<List<List<Integer>>> shouldBe = initShouldBe();

        for (int i = 0; i < patterList.size(); i++) {
            for (int j = 0; j < Directions.getTOTAL_DIRECTIONS_NUMBER(); j++) {
                for (int k = 0; k < patterList.size(); k++) {
                    shouldBe.get(i).get(j).add(k);
                }
            }
        }
        Log.d(TAG, "patternsEnablersTheSame: was " + was);
        Log.d(TAG, "patternsEnablersTheSame: shouldBe " + shouldBe);
        assertTrue(deepEqual(shouldBe, was));
    }
}