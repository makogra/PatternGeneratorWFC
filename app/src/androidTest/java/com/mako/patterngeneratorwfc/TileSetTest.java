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
        int id = 0;
        int[][] valueGrid = new int[][]{{0,1},{2,3}};
        List<Character> valueToChar = new ArrayList<Character>(){{
            add('A');
            add('B');
            add('C');
            add('D');
        }};
        int idAsTempPreview = 0;
        tileSet = new TileSet(id, valueGrid, valueToChar, idAsTempPreview);
        Parcel parcel = Parcel.obtain();
        tileSet.writeToParcel(parcel, tileSet.describeContents());
        parcel.setDataPosition(0);
        TileSet tileSetCopy = TileSet.CREATOR.createFromParcel(parcel);
        assertEquals(id, tileSetCopy.getTileId());
        assertEquals(idAsTempPreview, tileSetCopy.getIdAsTempPreview());
        assertEquals(valueGrid.length, tileSetCopy.getTileSetHeight());
        int[][] valueGridCopy = tileSetCopy.getValueGrid();
        for (int i = 0; i < valueGrid.length; i++) {
            for (int j = 0; j < valueGrid[0].length; j++) {
                assertEquals(valueGrid[i][j], valueGridCopy[i][j]);
            }
        }
        List<Character> valueToCharCopy = tileSetCopy.getValueToChar();
        for (int i = 0; i < valueToChar.size(); i++) {
            assertEquals(valueToChar.get(i), valueToCharCopy.get(i));
        }
        assertEquals(valueToChar, valueToCharCopy);
    }

}