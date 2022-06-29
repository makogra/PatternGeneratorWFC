package com.mako.patterngeneratorwfc.datamodels;

import androidx.lifecycle.ViewModel;

import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.adapters.TileSetAdapter;

import java.util.ArrayList;
import java.util.List;

public class TileSetViewModel extends ViewModel {

    private List<TileSet> tileSetList;
    private String currentId;
    private int currentIndex;

    //TODO remove hard coded

    public TileSetViewModel(){
        hardCodedTileSetList();
    }


    public List<TileSet> getTileSetList() {
        return tileSetList;
    }

    @Deprecated
    private void hardCodedTileSetList() {
        this.tileSetList = new ArrayList<TileSet>() {{
            add(new TileSet("1"));
            add(new TileSet("2"));
            add(new TileSet("3"));
            add(new TileSet("5"));
            add(new TileSet("8"));
        }};

    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }

    public void setCurrentIndex(int index){
        currentIndex = index;
        currentId = tileSetList.get(index).getTileId();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getTileSetListSize() {
        return this.tileSetList.size();
    }

    public TileSet getTileSet(int position) {
        return tileSetList.get(position);
    }
}