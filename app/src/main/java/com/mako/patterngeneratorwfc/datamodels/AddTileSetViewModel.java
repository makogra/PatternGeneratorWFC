package com.mako.patterngeneratorwfc.datamodels;

import androidx.lifecycle.ViewModel;

import com.mako.patterngeneratorwfc.TileSet;

import java.util.ArrayList;
import java.util.List;

public class AddTileSetViewModel extends ViewModel {

    private TileSet tileSet;

    //Getters
    public int getTileId() {
        return tileSet.getTileId();
    }

    public int getIdAsTempPreview() {
        return tileSet.getIdAsTempPreview();
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


    public TileSet getTileSet() {
        return new TileSet(1, new int[][]{{2,2},{3,3}}, new ArrayList<String>(){{add("B");add("C");}}, 2);
    }


    // Setters
    public void setTileId(int id) {
        tileSet.setTileId(id);
    }

    public void setValueGrid(int[][] valueGrid) {
        tileSet.setValueGrid(valueGrid);
    }

    public void setValueToChar(List<String> valueToStringPath) {
        tileSet.setValueToStringPath(valueToStringPath);
    }

    public void setIdAsTempPreview(int idAsTempPreview) {
        tileSet.setIdAsTempPreview(idAsTempPreview);
    }
}
