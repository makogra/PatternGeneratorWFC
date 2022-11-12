package com.mako.patterngeneratorwfc.datamodels;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

public class ResultViewModel extends ViewModel {


    // small bitmap that represent all needed information about the output
    private Bitmap bitmap;
    //TODO find usage or delete
    // temporary redundant and unused
    private int height;
    private int width;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
