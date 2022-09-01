package com.mako.patterngeneratorwfc.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.mako.patterngeneratorwfc.TileSet;

import java.util.List;

public class TileSetRepository {

    private static final String TAG = "TileSetRepository";
    private static volatile TileSetRepository INSTANCE;
    private final TileSetDao mTileSetDao;

    private TileSetRepository(Application application){
        TileSetRoomDatabase db = TileSetRoomDatabase.getDatabase(application);
        mTileSetDao = db.tileSetDao();
    }

    public static TileSetRepository getInstance(Application application){
        if (INSTANCE == null){
            synchronized (TileSetRepository.class){
                if (INSTANCE == null){
                    INSTANCE = new TileSetRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    public static TileSetRepository getInstance(){
        return INSTANCE;
    }

    public static boolean canGetInstance(){
        return INSTANCE != null;
    }

    public LiveData<List<String>> getAllIds() {
        return mTileSetDao.getAllIds();
    }

    public LiveData<List<TileSet>> getTileSetList() {
        return mTileSetDao.getTileSetList();
    }

    public LiveData<TileSet> getTileSet(String id){
        return mTileSetDao.getTileSet(id);
    }

    public TileSet getTileSetValue(String id){
        return mTileSetDao.getTileSetValue(id);
    }

    public LiveData<Integer> count(){
        return mTileSetDao.count();
    }

    public void insert(TileSet tileSet){
        TileSetRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTileSetDao.insert(tileSet);
            Log.i(TAG, "inserted: " + tileSet);
        });
    }

    public String getFirstTileSet() {
        return mTileSetDao.getFirstTileSet();
    }

    public void deleteId(String id){
        TileSetRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTileSetDao.delete(id);
            Log.i(TAG, "TileSet with id: [" + id + "] has been DELETED");
        });
    }

}
