package com.mako.patterngeneratorwfc;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "tileset_table")
public class TileSet implements Parcelable {

    //TODO make this class to database

    @PrimaryKey
    private int id;
    private int[][] valueGrid;
    private List<String> valueToStringPath;
    //private image preview;
    private int idAsTempPreview;

    public TileSet(int id, int[][] valueGrid, List<String> valueToStringPath, int idAsTempPreview) {
        this.id = id;
        this.valueGrid = valueGrid;
        this.valueToStringPath = valueToStringPath;
        this.idAsTempPreview = idAsTempPreview;
    }

    @Deprecated
    public TileSet(){
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
        int arrLength = in.readInt();
        int arrDepth = in.readInt();
        valueGrid = new int[arrLength][arrDepth];
        for (int[] ints : valueGrid) {
            in.readIntArray(ints);
        }
        valueToStringPath = new ArrayList<>();
        in.readList(valueToStringPath, Character.class.getClassLoader());
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

    public int getTileId() {
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

    public int[][] getValueGrid() {
        return valueGrid;
    }

    public List<String> getValueToStringPath() {
        return valueToStringPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idAsTempPreview);
        dest.writeInt(valueGrid.length);
        dest.writeInt(valueGrid[0].length);
        for (int[] ints : valueGrid) {
            dest.writeIntArray(ints);
        }
        dest.writeList(valueToStringPath);
    }

    @NonNull
    @Override
    public String toString() {
        return "TileSet{" +
                "id=" + id +
                ", valueGrid=" + Arrays.toString(valueGrid) +
                ", valueToChar=" + valueToStringPath +
                ", idAsTempPreview=" + idAsTempPreview +
                '}';
    }

    public void setTileId(int id) {
        this.id = id;
    }

    public void setValueGrid(int[][] valueGrid) {
        this.valueGrid = valueGrid;
    }

    public void setValueToStringPath(List<String> valueToStringPath) {
        this.valueToStringPath = valueToStringPath;
    }

    public void setIdAsTempPreview(int idAsTempPreview) {
        this.idAsTempPreview = idAsTempPreview;
    }
}
