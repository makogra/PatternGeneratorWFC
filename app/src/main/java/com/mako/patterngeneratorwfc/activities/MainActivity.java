package com.mako.patterngeneratorwfc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.datamodels.TempViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TempViewModel tempViewModel;
    private final String TAG = "mainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ScreenSlidePager.class);
        //startActivity(intent);

        Button goToSliderBtn = findViewById(R.id.go_to_slider_btn);
        goToSliderBtn.setOnClickListener(v -> startActivity(intent));

        tempViewModel = new ViewModelProvider(this).get(TempViewModel.class);

        TextView textView = findViewById(R.id.textView);
        textView.setText("" + tempViewModel.getCount());
        Log.d(TAG, tempViewModel.getCount().toString());

        popiulateAndObserve(textView);

        //TileSet tileFromRepo = tempViewModel.getTileSet(id);
        //Log.d(TAG, "tileFromRepo = " + tileFromRepo);

        //Log.d(TAG, tileSet.equals(tileFromRepo));



    }

    private void popiulateAndObserve(TextView textView) {
        String id = "Main Theread TileSet";
        TileSet tileSet = new TileSet(id, new int[][]{{1,2},{8,9}}, new ArrayList<String>(){{
            add("W");
            add("S");
            add("X");
            add("R");
        }});

        tempViewModel.insert(tileSet);
        Log.d(TAG, "tempViewModel.getCount() = " + tempViewModel.getCount());

        tempViewModel.getCount().observe(this, integer -> {
            //Log.d(TAG, String.valueOf(integer.intValue()));
            Log.d(TAG, integer.toString());
            textView.setText("" + integer);
        });

        tempViewModel.getListLiveData().observe(this, tileSets -> {
            for (TileSet set : tileSets){
                Log.d(TAG, set.toString());
            }
        });

        String id2 = "Main Theread TileSet second";
        TileSet tileSet2 = new TileSet(id2, new int[][]{{1,2},{8,9}}, new ArrayList<String>(){{
            add("W");
            add("S");
            add("X");
            add("R");
        }});
        tempViewModel.insert(tileSet2);

        tempViewModel.getAllIds().observe(this, ids -> {
            Log.d(TAG, "MainActivity.onCreate : allIds = " + ids);
            for (String s : ids){
                Log.d(TAG, "id = " + s);
            }
        });
    }
}