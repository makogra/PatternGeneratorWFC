package com.mako.patterngeneratorwfc.datamodels;

import androidx.lifecycle.ViewModel;

import com.mako.patterngeneratorwfc.TileSet;

import java.util.ArrayList;
import java.util.List;

public class AddTileSetViewModel extends ViewModel {

    private TileSet tileSet;

    //Getters
    public String getTileId() {
        return tileSet.getTileId();
    }

    public int getTileSetHeight() {
        return tileSet.getTileSetHeight();
    }

    public int getTileSetLength() {
        return tileSet.getTileSetLength();
    }

    public int[][] getValueGrid() {
        return tileSet.getValueGrid();
    }

    public List<String> getValueToStringPath() {
        return tileSet.getValueToStringPath();
    }


    public TileSet getSampleTileSet() {
        String id = "Sample Tile";
        String[][] inputGrid = new String[][]{
                {"G","G","G","G","C","S","S","S"},
                {"G","G","G","C","C","S","S","S"},
                {"G","G","G","C","S","S","S","S"},
                {"G","G","G","C","S","S","S","S"},
                {"G","G","G","C","S","S","S","S"}
        };
        int[][] valueGrid = new int[][]{
                {1,1,1,1,2,3,3,3},
                {1,1,1,2,2,3,3,3},
                {1,1,1,2,3,3,3,3},
                {1,1,1,2,3,3,3,3},
                {1,1,1,2,3,3,3,3}
        };
        List<String> valueToStringList = new ArrayList<String>(){{
            add("G");
            add("L");
            add("S");
        }};
        return new TileSet(id, valueGrid, valueToStringList);
    }

    public TileSet getTileSet() {
        return tileSet;
    }

    // Setters
    public void setTileId(String id) {
        tileSet.setTileId(id);
    }

    public void setValueGrid(int[][] valueGrid) {
        tileSet.setValueGrid(valueGrid);
    }

    public void setValueToString(List<String> valueToStringPath) {
        tileSet.setValueToStringPath(valueToStringPath);
    }

    public void setTileSet(TileSet tileSet) {
        this.tileSet = tileSet;
    }
}
