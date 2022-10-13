package com.mako.patterngeneratorwfc.wfc;

import android.util.Log;

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
    private final String TAG = "Propagator";
    private final int mMaxHeight;
    private final int mMaxWidth;

    Propagator(Wave wave){
        this.wave = wave;
        this.mMaxHeight = wave.getHeight();
        this.mMaxWidth = wave.getWidth();
        propagateQueue = new LinkedList<>();
    }

    void propagate(){
        EntryPattern entryPattern;
        int propagationCount = 0;
        while (!propagateQueue.isEmpty()) {
            entryPattern = propagateQueue.poll();
            propagateAt(entryPattern);
            propagationCount++;
        }
        Log.d(TAG, "propagate: count = " + propagationCount);
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

        wave.addToLowestEntropyQueue(cell);

    }

    void addToPropagate(int row, int col, int pattern, boolean propagateEnabler){
        int patternRow;
        int patternCol;
        int[] direction;
        int[][] directions = Directions.getDIRECTIONS();
        for (int directionIndex = 0; directionIndex < directions.length; directionIndex++) {
            direction = directions[directionIndex];
            patternRow = row + direction[0];
            patternCol = col + direction[1];

            if (patternRow < 0 || patternRow >= mMaxHeight || patternCol < 0 || patternCol >= mMaxWidth || wave.getCell(patternRow, patternCol).isObserved())
                continue;

            propagateQueue.offer(new EntryPattern(patternRow, patternCol, pattern, Directions.oppositeDirectionIndex(directionIndex), propagateEnabler));
        }
    }

    public void finish(){
        /*
        if (null != mExecutor)
            mExecutor.shutdown();

         */
    }
}
