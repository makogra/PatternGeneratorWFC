package com.mako.patterngeneratorwfc.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.mako.patterngeneratorwfc.TileSet;

import java.util.List;

public class TileSetRepository {

    private static final String TAG = "TileSetRepository";
    private static volatile TileSetRepository INSTANCE;
    private final TileSetDao tileSetDao;

    private TileSetRepository(Application application){
        TileSetRoomDatabase db = TileSetRoomDatabase.getDatabase(application);
        tileSetDao = db.tileSetDao();
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
        return tileSetDao.getAllIds();
    }

    public LiveData<List<TileSet>> getTileSetList() {
        return tileSetDao.getTileSetList();
    }

    public LiveData<TileSet> getTileSet(String id){
        return tileSetDao.getTileSet(id);
    }

    public TileSet getTileSetValue(String id){
        return tileSetDao.getTileSetValue(id);
    }

    public LiveData<Integer> count(){
        return tileSetDao.count();
    }

    public void insert(TileSet tileSet){
        TileSetRoomDatabase.databaseWriteExecutor.execute(() -> {
            tileSetDao.insert(tileSet);
            Log.i(TAG, "inserted: " + tileSet);
        });
    }

    public String getFirstTileSet() {
        return tileSetDao.getFirstTileSet();
    }

    public void deleteId(String id){
        TileSetRoomDatabase.databaseWriteExecutor.execute(() -> {
            tileSetDao.delete(id);
            Log.i(TAG, "TileSet with id: [" + id + "] has been DELETED");
        });
    }

}
