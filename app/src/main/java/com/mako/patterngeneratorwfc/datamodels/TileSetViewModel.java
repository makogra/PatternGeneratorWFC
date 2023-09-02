package com.mako.patterngeneratorwfc.datamodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.database.TileSetRepository;

import java.util.List;

public class TileSetViewModel extends AndroidViewModel {

    //TODO remove currentPosition and work only with current id(String), add method to search for the TileSet with specific id and return it.
    //TODO Work on a callback in database class
    private static final String TAG  = "TileSetViewModel";

    private final TileSetRepository tileSetRepository;
    private LiveData<List<TileSet>> tileSetList;
    private List<TileSet> list;
    private String currentId;
    private int size = 0;


    public TileSetViewModel(Application application){
        super(application);
        if (application.getApplicationContext() == null)
            Log.wtf(TAG, "Null jak nic");
        tileSetRepository = TileSetRepository.getInstance(application);


    }

    public void initCurrentId() {
        if (currentId == null){
            setCurrentId(tileSetRepository.getFirstTileSet());
        }
    }


    public LiveData<List<TileSet>> getTileSetList() {
        if (tileSetList == null){
            tileSetList = tileSetRepository.getTileSetList();
        }
        return tileSetList;
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        Log.d(TAG, "Current TileSet Id has changed to = " + currentId);
        this.currentId = currentId;
    }


    public TileSet getTileSet(int position) {
        return list.get(position);
    }

    public TileSet getCurrentTileSet(){
        return tileSetRepository.getTileSetValue(currentId);
    }

    public void insert(@NonNull TileSet tileSet){
        tileSetRepository.insert(tileSet);
    }

    //TODO delete
    public TileSet getTilesetWithId(String id){
        for (TileSet ts : list){
            if (ts.getTileId().equals(id))
                return ts;
        }
        return null;
    }

    public void delete(String id){
        tileSetRepository.deleteId(id);
    }


    //private List<>
}