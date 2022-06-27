package com.mako.patterngeneratorwfc;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TileSet implements Parcelable {

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

    protected TileSet(Parcel in) {
        id = in.readInt();
        idAsTempPreview = in.readInt();
    }

    public static final Creator<TileSet> CREATOR = new Creator<TileSet>() {
        @Override
        public TileSet createFromParcel(Parcel in) {
            return new TileSet(in);
        }

        @Override
        public TileSet[] newArray(int size) {
            return new TileSet[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idAsTempPreview);
    }
}
