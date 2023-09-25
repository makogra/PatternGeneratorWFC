package com.mako.patterngeneratorwfc.datamodels;

import androidx.lifecycle.ViewModel;

public class WFCViewModel extends ViewModel {

    //save output

    public WFCViewModel(){
    }

    public String getTestConnection() {
        return "Connection test succeed";
    }
}
