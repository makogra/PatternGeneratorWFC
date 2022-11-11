package com.mako.patterngeneratorwfc.datamodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.database.TileSetRepository;

import java.util.List;

public class TempViewModel extends AndroidViewModel {

    private TileSetRepository tileSetRepository;
    private LiveData<List<TileSet>> listLiveData;
    private LiveData<Integer> count;

    public TempViewModel(@NonNull Application application) {
        super(application);
        tileSetRepository = TileSetRepository.getInstance(application);
        this.listLiveData = tileSetRepository.getTileSetList();
        this.count = tileSetRepository.count();
    }

    public LiveData<List<TileSet>> getListLiveData() {
        return listLiveData;
    }

    public LiveData<Integer> getCount() {
        return count;
    }

    public void insert(TileSet tileSet){
        tileSetRepository.insert(tileSet);
    }

    public LiveData<TileSet> getTileSet(String id){
        return tileSetRepository.getTileSet(id);
    }

    public LiveData<List<String>> getAllIds() {
        return tileSetRepository.getAllIds();
    }
}
