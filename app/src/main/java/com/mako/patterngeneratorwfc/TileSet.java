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
        hardCodedValueGrid();
    }

    @Deprecated
    public TileSet(int id){
        this.id = id;
        idAsTempPreview = this.id;
        hardCodedValueGrid();
    }

    @Deprecated
    private void hardCodedValueGrid() {
        valueGrid = new int[9][9];
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

    public int getTileSetHeight() {
        return valueGrid.length;
    }

    public int getTileSetLength() {
        return valueGrid[0].length;
    }
}
