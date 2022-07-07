package com.mako.patterngeneratorwfc;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.mako.patterngeneratorwfc.database.convertors.ListOfStringConverter;
import com.mako.patterngeneratorwfc.database.convertors.NestedIntArrayConvertor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "tileset_table")
@TypeConverters({NestedIntArrayConvertor.class, ListOfStringConverter.class})
public class TileSet implements Parcelable {

    //TODO make this class to database

    @PrimaryKey
    @NonNull
    private String id;
    private int[][] valueGrid;
    private List<String> valueToStringPath;
    //private image preview;

    public TileSet(String id, int[][] valueGrid, List<String> valueToStringPath) {
        this.id = id;
        this.valueGrid = valueGrid;
        this.valueToStringPath = valueToStringPath;
    }

    @Deprecated
    public TileSet(){
        this.id = "" + this.hashCode();
        hardCodedValueGrid();
    }

    @Deprecated
    public TileSet(@NonNull String id){
        this.id = id;
        hardCodedValueGrid();
    }

    protected TileSet(Parcel in) {
        id = in.readString();
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

    public String getTileId() {
        return id;
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
        dest.writeString(id);
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
                '}';
    }

    public void setTileId(String id) {
        this.id = id;
    }

    public void setValueGrid(int[][] valueGrid) {
        this.valueGrid = valueGrid;
    }

    public void setValueToStringPath(List<String> valueToStringPath) {
        this.valueToStringPath = valueToStringPath;
    }

    //TEMP
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
