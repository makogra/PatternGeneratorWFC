package com.mako.patterngeneratorwfc.datamodels;

import androidx.lifecycle.ViewModel;

public class SettingsTileSetViewModel extends ViewModel {

    private int sample = 0;
    private int sampleMin = 0;
    private int sampleMax = 10;

    public int getSample() {
        return sample;
    }

    public void setSample(int sample) {
        if (sample < sampleMin || sample > sampleMax)
            return;
        this.sample = sample;
    }

    public void increaseSample(){
        setSample(sample + 1);
    }

    public void decreaseSample(){
        setSample(sample - 1);
    }
}
