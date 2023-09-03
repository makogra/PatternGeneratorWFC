package com.mako.patterngeneratorwfc.utiles;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtilsWFC {

    private static final String TAG = ToastUtilsWFC.class.getName();
    private final Context context;

    public ToastUtilsWFC(Context context){
        this.context = context;
    }

    public void displayWFCStarted() {
        Toast.makeText(context, "WFC started...", Toast.LENGTH_SHORT).show();

    }

    public void displayWFCFinished(boolean result){
        toast(() -> {
            if (result){
                Toast.makeText(context, "WFC finished successful", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(context, "WFC failed", Toast.LENGTH_SHORT).show();
        });
    }

    private void toast(Runnable runnable){
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
