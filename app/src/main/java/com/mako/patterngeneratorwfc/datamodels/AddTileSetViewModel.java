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
        return new TileSet("1", new int[][]{{2,2},{3,3}}, new ArrayList<String>(){{add("B");add("C");}});
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

    public void setValueToChar(List<String> valueToStringPath) {
        tileSet.setValueToStringPath(valueToStringPath);
    }

    public void setTileSet(TileSet tileSet) {
        this.tileSet = tileSet;
    }
}
