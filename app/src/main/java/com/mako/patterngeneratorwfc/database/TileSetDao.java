package com.mako.patterngeneratorwfc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mako.patterngeneratorwfc.TileSet;

import java.util.List;

@Dao
public interface TileSetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TileSet tileSet);

    @Query("SELECT id FROM tileset_table")
    LiveData<List<String>> getAllIds();

    @Query("SELECT * FROM tileset_table WHERE id = :id")
    TileSet getTileSet(String id);
}
