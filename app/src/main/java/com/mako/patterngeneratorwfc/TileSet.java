package com.mako.patterngeneratorwfc;

import java.util.List;

public class TileSet {

    //TODO make this class to database

    private int id;
    private int[][] valueGrid;
    private List<Character> valueToChar;
    //private image preview;
    private int idAsTempPreview;

    @Deprecated
    TileSet(){
        this.id = this.hashCode();
        idAsTempPreview = this.id;
    }

    @Deprecated
    public TileSet(int id){
        this.id = id;
        idAsTempPreview = this.id;
    }

    TileSet(char[][] inputGrid){
        //TODO make valueGrid and valueToCharMap
    }

    public int getId() {
        return id;
    }

    public int getIdAsTempPreview() {
        return idAsTempPreview;
    }
}
