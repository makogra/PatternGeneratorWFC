package com.mako.patterngeneratorwfc;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TileSetViewModel extends ViewModel {

    private List<TileSet> tileSetList;

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

    // TODO: Implement the ViewModel
}