package com.mako.patterngeneratorwfc.datamodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsTileSetViewModel extends ViewModel {

    private static final String TAG = "SettingsTileSetViewModel";
    private static ArrayList<String> settingsList;
    private static String[] settingsArr;
    private static int settingsLength;
    private static boolean listInitialized;

    private boolean newInstance = true;
    private int[] settingsMin;
    private int[] settingsMax;
    private int[] settingsValue;


    // Getters
    public static int getSettingsLength(){
        return settingsLength;
    }

    public int getMin(int position){
        if (outOfArr(position))
            throw new ArrayIndexOutOfBoundsException("getMin, position: " + position);
        return settingsMin[position];
    }

    public int getMax(int position){
        if (outOfArr(position))
            throw new ArrayIndexOutOfBoundsException("getMax, position: " + position);
        return settingsMax[position];
    }

    public int getValue(int position){
        if (outOfArr(position))
            throw new ArrayIndexOutOfBoundsException("getValue, position: " + position);
        return settingsValue[position];
    }

    public String getXMLName(int position){
        return settingsList.get(position);
    }

    public String getHumanName(int position){
        return settingsList.get(position).replaceAll("_", " ");
    }

    public static boolean isNotListAlreadyInitialized(){
        return !listInitialized;
    }

    // Setters
    public static void setSettingsArr(String[] arr){
        Log.d(TAG, Arrays.toString(arr));
        settingsArr = arr;
        settingsLength = arr.length;
        setSettingsList();
    }

    public static void setSettingsList(){
        if (settingsArr == null || settingsList != null)
            return;
        listInitialized = true;
        settingsList = new ArrayList<>();
        settingsList.addAll(Arrays.asList(settingsArr));
    }

    public void initMinMax(){
        this.settingsMin = new int[settingsLength];
        this.settingsMax = new int[settingsLength];
    }

    public void initValue() {
        this.settingsValue = new int[settingsLength];
    }

    public void setMin(int value, int position)  {
        if (outOfArr(position)) {
            Log.d(TAG, "setMin, position: " + position);
            return;
        }
        if (value < 0) {
            Log.d(TAG, "setMin, value: " + value);
            return;
        }
        this.settingsMin[position] = value;
    }

    public void setMax(int value, int position) {
        if (outOfArr(position)) {
            Log.d(TAG, "setMax, position: " + position);
            return;
        }
        this.settingsMax[position] = value;
    }

    public boolean setValue(int value, int position) {
        String info = "min: " + getMin(position) + " max: " + getMax(position) + " value: " + value + " at position: " + position;
        if (outOfArr(position)){
            Log.w(TAG, "setValue, FAILED (illegal position): " + info);
            return false;
        }
        if (value < getMin(position) || value > getMax(position)) {
            Log.d(TAG, "setValue: FAILED (illegal value): " + info);
            return false;
        }
        this.settingsValue[position] = value;
        Log.d(TAG, "setValue: SUCCESS " + info);
        return true;
    }

    public void increment(int position) throws IllegalAccessException {
        setValue(getValue(position) + 1, position);
    }

    public void decrement(int position) throws IllegalAccessException {
        setValue(getValue(position) - 1, position);
    }



    private boolean outOfArr(int position) {
        return position < 0 || position > settingsLength;
    }

    public boolean isNewInstance() {
        return newInstance;
    }

    public void setNewInstance(boolean newInstance) {
        this.newInstance = newInstance;
    }

    @NonNull
    @Override
    public String toString() {
        return "SettingsTileSetViewModel{" +
                "settingsMin=" + Arrays.toString(settingsMin) +
                ", settingsMax=" + Arrays.toString(settingsMax) +
                ", settingsValue=" + Arrays.toString(settingsValue) +
                '}';
    }

    public boolean isNotValueInited() {
        return settingsValue == null;
    }


    public boolean isNotMinMaxInnited() {
        return settingsMin == null || settingsMax == null;
    }
}
