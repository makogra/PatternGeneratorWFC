package com.mako.patterngeneratorwfc.datamodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.service.quicksettings.Tile;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;

import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.database.TileSetRepository;

import java.util.ArrayList;
import java.util.List;

public class TileSetViewModel extends AndroidViewModel {

    //TODO remove curentPsoition and work only with current id(String), add method to shearch for the TileSet with secific id and return it.
    //TODO Wrok on a callback in database class
    private static final String TAG  = "TileSetViewModel";

    private final TileSetRepository mTileSetRepository;
    private LiveData<List<TileSet>> tileSetList;
    private List<TileSet> list;
    private String currentId;
    private int currentIndex;
    private int size = 0;


    public TileSetViewModel(Application application){
        super(application);
        if (application.getApplicationContext() == null)
            Log.d(TAG, "Null jak nic");
        mTileSetRepository = TileSetRepository.getInstance(application);
        Log.d(TAG, "TileSetViewModel() called with: application = [" + application + "]");
        tileSetList = mTileSetRepository.getTileSetList();


    }

    public void initCurrentId() {
        if (currentId == null){
            Log.d(TAG, "initCurrentId() called");
            setCurrentId(mTileSetRepository.getFirstTileSet());
        }
    }


    public LiveData<List<TileSet>> getTileSetList() {
        return tileSetList;
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        Log.i(TAG, "Current TileSet Id has changed to = " + currentId);
        this.currentId = currentId;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public TileSet getTileSet(int position) {
        return list.get(position);
    }

    public void insert(@NonNull TileSet tileSet){
        mTileSetRepository.insert(tileSet);
    }

    //TODO delete
    public TileSet getTilesetWithId(String id){
        for (TileSet ts : list){
            if (ts.getTileId().equals(id))
                return ts;
        }
        return null;
    }


    //private List<>
}