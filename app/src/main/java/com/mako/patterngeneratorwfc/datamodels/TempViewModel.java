package com.mako.patterngeneratorwfc.datamodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.database.TileSetRepository;

import java.util.List;

public class TempViewModel extends AndroidViewModel {

    private TileSetRepository mRepository;
    private LiveData<List<TileSet>> listLiveData;
    private LiveData<Integer> count;

    public TempViewModel(@NonNull Application application) {
        super(application);
        mRepository = TileSetRepository.getInstance(application);
        this.listLiveData = mRepository.getTileSetList();
        this.count = mRepository.count();
    }

    public LiveData<List<TileSet>> getListLiveData() {
        return listLiveData;
    }

    public LiveData<Integer> getCount() {
        return count;
    }

    public void insert(TileSet tileSet){
        mRepository.insert(tileSet);
    }

    public LiveData<TileSet> getTileSet(String id){
        return mRepository.getTileSet(id);
    }

    public LiveData<List<String>> getAllIds() {
        return mRepository.getAllIds();
    }
}
