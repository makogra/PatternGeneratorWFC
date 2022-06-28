package com.mako.patterngeneratorwfc.datamodels;

import androidx.lifecycle.ViewModel;

import com.mako.patterngeneratorwfc.TileSet;

import java.util.ArrayList;

public class AddTileSetViewModel extends ViewModel {

    public String test(){
        return "dziala";
    }

    public TileSet getTileSet() {
        return new TileSet(1, new int[][]{{2,2},{3,3}}, new ArrayList<Character>(){{add('B');add('C');}}, 2);
    }
}
