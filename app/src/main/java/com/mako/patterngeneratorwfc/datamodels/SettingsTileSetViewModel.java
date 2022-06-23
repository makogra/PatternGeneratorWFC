package com.mako.patterngeneratorwfc.datamodels;

import androidx.lifecycle.ViewModel;

public class SettingsTileSetViewModel extends ViewModel {

    private int sample = 0;

    public int getSample() {
        return sample;
    }

    public void setSample(int sample) {
        this.sample = sample;
    }

    public void increaseSample(){
        this.sample++;
    }

    public void decreaseSample(){
        this.sample--;
    }
}
