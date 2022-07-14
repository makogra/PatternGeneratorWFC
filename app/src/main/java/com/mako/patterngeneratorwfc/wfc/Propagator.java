package com.mako.patterngeneratorwfc.wfc;

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

    public void addToPropagate(int row, int col, int patternId, boolean b) {
        return;
    }
}
