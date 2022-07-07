package com.mako.patterngeneratorwfc.database;

import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.mako.patterngeneratorwfc.TileSet;

import java.util.List;

public class TileSetRepository {

    private TileSetDao mTileSetDao;
    private LiveData<List<String>> mAllIds;
    private LiveData<List<TileSet>> mTileSetList;

    public TileSetRepository(Application application){
        TileSetRoomDatabase db = TileSetRoomDatabase.getDatabase(application);
        mTileSetDao = db.tileSetDao();

        /*TileSet tileSet = new TileSet("4", new int[][]{{12,2},{3,4}}, new ArrayList<String>(){{
            add("Q");
            add("W");
            add("E");
            add("R");
        }});
        mTileSetDao.insert(tileSet);

         */
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
}
