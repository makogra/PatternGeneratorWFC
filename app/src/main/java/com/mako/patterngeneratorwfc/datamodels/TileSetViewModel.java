package com.mako.patterngeneratorwfc.datamodels;

import androidx.lifecycle.ViewModel;

import com.mako.patterngeneratorwfc.TileSet;

import java.util.ArrayList;
import java.util.List;

public class TileSetViewModel extends ViewModel {

    private List<TileSet> tileSetList;
    private int currentId;

    //TODO remove hard coded


    public List<TileSet> getTileSetList() {
        if (tileSetList == null)
            hardCodedTileSetList();
        return tileSetList;
    }

    private void hardCodedTileSetList() {
        this.tileSetList = new ArrayList<TileSet>() {{
            add(new TileSet(1));
            add(new TileSet(2));
            add(new TileSet(3));
            add(new TileSet(5));
            add(new TileSet(8));
        }};

    }

    public int getCurrentId() {
        return currentId;
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }

    // TODO: Implement the ViewModel
}