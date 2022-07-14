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

    void propagate(){
        EntryPattern entryPattern;
        while (!propagateQueue.isEmpty()) {
            entryPattern = propagateQueue.poll();
            propagateAt(entryPattern);
        }
    }

    void propagateAt(EntryPattern entry){
        int row = entry.row;
        int col = entry.col;
        int pattern = entry.pattern;
        int directionIndex = entry.directionIndex;
        boolean propagateEnabler = entry.propagateEnabler;
        Cell cell = wave.getCell(row, col);

        if (cell.isObserved())
            return;

        if (propagateEnabler){
            cell.removePatternFromPatternEnablers(directionIndex, pattern);
        } else {
            cell.removePatternEnablesExceptPattern(directionIndex, pattern);
        }

        wave.addToLowestEntropyQueue(row, col);

    }

    void addToPropagate(int row, int col, int pattern, boolean propagateEnabler){
        int patternRow;
        int patternCol;
        int[] direction;
        int[][] directions = Directions.getDIRECTIONS();
        int maxHeight = wave.getHeight();
        int maxWidth = wave.getWidth();
        for (int directionIndex = 0; directionIndex < directions.length; directionIndex++) {
            direction = directions[directionIndex];
            patternRow = row + direction[0];
            patternCol = col + direction[1];

            if (patternRow < 0 || patternRow >= maxHeight || patternCol < 0 || patternCol >= maxWidth || wave.getCell(patternRow, patternCol).isObserved())
                continue;

            propagateQueue.add(new EntryPattern(patternRow, patternCol, pattern, Directions.oppositeDirectionIndex(directionIndex), propagateEnabler));
        }
    }
}
