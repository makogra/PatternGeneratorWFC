package com.mako.patterngeneratorwfc.ui;

import static org.junit.Assert.*;

import org.junit.Test;

public class TileSetFragmentTest {

    @Test
    public void deleteTileSet_numberOfChildrenDecrease(){
        int numberOfChildrenBefore = 0;
        int numberOfChildrenAfter = 0;

        assertEquals(numberOfChildrenBefore - 1, numberOfChildrenAfter);
    }

}