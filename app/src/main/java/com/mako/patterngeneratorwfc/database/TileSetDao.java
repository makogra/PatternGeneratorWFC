package com.mako.patterngeneratorwfc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mako.patterngeneratorwfc.TileSet;

import java.util.List;

@Dao
public interface TileSetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = TileSet.class)
    void insert(TileSet tileSet);

    @Query("SELECT * FROM tileset_table ORDER BY id")
    LiveData<List<TileSet>> getTileSetList();

    @Query("SELECT id FROM tileset_table")
    LiveData<List<String>> getAllIds();

    @Query("SELECT * FROM tileset_table WHERE id = :id")
    LiveData<TileSet> getTileSet(String id);

    @Query("SELECT COUNT(*) FROM tileset_table")
    LiveData<Integer> count();

    @Query("DELETE FROM tileset_table")
    void deleteAll();

    @Query("SELECT id FROM tileset_table LIMIT 1")
    String getFirstTileSet();

    @Query("DELETE FROM tileset_table WHERE id = :id")
    void delete(String id);

    @Query("SELECT * FROM tileset_table WHERE id = :id")
    TileSet getTileSetValue(String id);
}
