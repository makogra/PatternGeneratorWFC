package com.mako.patterngeneratorwfc.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.mako.patterngeneratorwfc.TileSet;

import java.util.List;

public class TileSetRepository {

    private TileSetDao mTileSetDao;
    private LiveData<List<String>> mAllIds;
    private LiveData<List<TileSet>> mTileSetList;

    //TODO make singleton
    public TileSetRepository(Application application){
        TileSetRoomDatabase db = TileSetRoomDatabase.getDatabase(application);
        mTileSetDao = db.tileSetDao();
        mAllIds = mTileSetDao.getAllIds();

        mTileSetList = mTileSetDao.getTileSetList();
    }

    public LiveData<List<String>> getAllIds() {
        return mAllIds;
    }

    public LiveData<List<TileSet>> getTileSetList() {
        return mTileSetList;
    }

    public LiveData<TileSet> getTileSet(String id){
        return mTileSetDao.getTileSet(id);
    }

    public LiveData<Integer> count(){
        return mTileSetDao.count();
    }

    public void insert(TileSet tileSet){
        TileSetRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTileSetDao.insert(tileSet);
        });
    }

    public String getFirstTileSet() {
        return mTileSetDao.getFirstTileSet();
    }

    //Maybe add the same as in insert
    public void deleteId(String id){
        TileSetRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTileSetDao.delete(id);
        });
    }

}
