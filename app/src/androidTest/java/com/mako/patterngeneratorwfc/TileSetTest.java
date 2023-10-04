package com.mako.patterngeneratorwfc;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TileSetTest {

    private TileSet tileSet;

    @Test
    public void checkIfParcelCorrectly_isCorrect() {
        String id = "0";
        int[][] valueGrid = new int[][]{{0,1},{2,3}};
        List<String> valueToStringPath = new ArrayList<String>(){{
            add("A");
            add("B");
            add("C");
            add("D");
        }};
        tileSet = new TileSet(id, valueGrid, valueToStringPath);
        Parcel parcel = Parcel.obtain();
        tileSet.writeToParcel(parcel, tileSet.describeContents());
        parcel.setDataPosition(0);
        TileSet tileSetCopy = TileSet.CREATOR.createFromParcel(parcel);
        assertEquals(id, tileSetCopy.getTileId());
        assertEquals(valueGrid.length, tileSetCopy.getTileSetHeight());
        int[][] valueGridCopy = tileSetCopy.getValueGrid();
        for (int i = 0; i < valueGrid.length; i++) {
            for (int j = 0; j < valueGrid[0].length; j++) {
                assertEquals(valueGrid[i][j], valueGridCopy[i][j]);
            }
        }
        List<String> valueToStringPathCopy = tileSetCopy.getValueToStringPath();
        for (int i = 0; i < valueToStringPath.size(); i++) {
            assertEquals(valueToStringPath.get(i), valueToStringPathCopy.get(i));
        }
        assertEquals(valueToStringPath, valueToStringPathCopy);
    }

}