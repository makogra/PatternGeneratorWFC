package com.mako.patterngeneratorwfc.wfc;

public class Directions {

    public static final int[] UP = {-1, 0};
    public static final int[] UP_RIGHT = {-1, 1};
    public static final int[] RIGHT = {0, 1};
    public static final int[] DOWN_RIGHT = {1, 1};
    public static final int[] DOWN = {1, 0};
    public static final int[] DOWN_LEFT = {1, -1};
    public static final int[] LEFT = {0, -1};
    public static final int[] UP_LEFT = {-1, -1};

    private static final int[][] DIRECTIONS = {UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT};

    private static final int TOTAL_DIRECTIONS_NUMBER = DIRECTIONS.length;

    public static int[] getUP() {
        return UP;
    }

    public static int[] getUP_RIGHT() {
        return UP_RIGHT;
    }

    public static int[] getRIGHT() {
        return RIGHT;
    }

    public static int[] getDOWN_RIGHT() {
        return DOWN_RIGHT;
    }

    public static int[] getDOWN() {
        return DOWN;
    }

    public static int[] getDOWN_LEFT() {
        return DOWN_LEFT;
    }

    public static int[] getLEFT() {
        return LEFT;
    }

    public static int[] getUP_LEFT() {
        return UP_LEFT;
    }

    public static int[][] getDIRECTIONS() {
        return DIRECTIONS;
    }

    public static int getTOTAL_DIRECTIONS_NUMBER() {
        return TOTAL_DIRECTIONS_NUMBER;
    }

    public static int[] oppositeDirection(int[] direction){
        return new int[]{-direction[0], -direction[1]};
    }

    public  static int[] oppositeDirection(int directionIndex){
        int index = (directionIndex + TOTAL_DIRECTIONS_NUMBER /2) % DIRECTIONS.length;
        return DIRECTIONS[index];
    }

    public static int oppositeDirectionIndex(int directionIndex){
        return (directionIndex + TOTAL_DIRECTIONS_NUMBER / 2) % DIRECTIONS.length;
    }

    public static int[] getDirection(int directionIndex) {
        return DIRECTIONS[directionIndex];
    }
}
