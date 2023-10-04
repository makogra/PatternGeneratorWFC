package com.mako.patterngeneratorwfc.wfc;

import static org.junit.Assert.*;

import org.junit.Test;

public class DirectionsTest {

    @Test
    public void testOppositeDirection() {
        assertArrayEquals(Directions.getUP(), Directions.oppositeDirection(Directions.getDOWN()));
        assertArrayEquals(Directions.getUP_LEFT(), Directions.oppositeDirection(Directions.getDOWN_RIGHT()));
        assertArrayEquals(Directions.getRIGHT(), Directions.oppositeDirection(Directions.getLEFT()));


        assertArrayEquals(Directions.getUP(), Directions.oppositeDirection(4));
        assertArrayEquals(Directions.getUP_RIGHT(), Directions.oppositeDirection(5));
        assertArrayEquals(Directions.getRIGHT(), Directions.oppositeDirection(6));
        assertArrayEquals(Directions.getDOWN_RIGHT(), Directions.oppositeDirection(7));
        assertArrayEquals(Directions.getDOWN(), Directions.oppositeDirection(0));
        assertArrayEquals(Directions.getDOWN_LEFT(), Directions.oppositeDirection(1));
        assertArrayEquals(Directions.getLEFT(), Directions.oppositeDirection(2));
        assertArrayEquals(Directions.getUP_LEFT(), Directions.oppositeDirection(3));
    }

    @Test
    public void testOppositeDirectionIndex(){
        assertEquals(0, Directions.oppositeDirectionIndex(4));
        assertEquals(1, Directions.oppositeDirectionIndex(5));
        assertEquals(2, Directions.oppositeDirectionIndex(6));
        assertEquals(3, Directions.oppositeDirectionIndex(7));
        assertEquals(4, Directions.oppositeDirectionIndex(0));
        assertEquals(5, Directions.oppositeDirectionIndex(1));
        assertEquals(6, Directions.oppositeDirectionIndex(2));
        assertEquals(7, Directions.oppositeDirectionIndex(3));
    }

}