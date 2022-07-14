package com.mako.patterngeneratorwfc.wfc;

import java.util.LinkedList;
import java.util.Queue;

public class Propagator {

    private static class EntryPattern{
        int row;
        int col;
        int pattern;
        int directionIndex;
        boolean propagateEnabler;

        public EntryPattern(int row, int col, int pattern, int directionIndex, boolean propagateEnabler) {
            this.row = row;
            this.col = col;
            this.pattern = pattern;
            this.directionIndex = directionIndex;
            this.propagateEnabler = propagateEnabler;
        }


    }

    private Queue<EntryPattern> propagateQueue;
    private final Wave wave;

    Propagator(Wave wave){
        this.wave = wave;
        propagateQueue = new LinkedList<>();
    }

    public void addToPropagate(int row, int col, int patternId, boolean b) {
        return;
    }
}
