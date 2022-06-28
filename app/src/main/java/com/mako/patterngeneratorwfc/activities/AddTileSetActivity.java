package com.mako.patterngeneratorwfc.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.datamodels.AddTileSetViewModel;

public class AddTileSetActivity extends AppCompatActivity {

    private AddTileSetViewModel mAddTileSetViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tile_set);

        mAddTileSetViewModel = new ViewModelProvider(this).get(AddTileSetViewModel.class);

        TextView testTextView = findViewById(R.id.test);
        testTextView.setText(mAddTileSetViewModel.test());

        ImageButton cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(v -> {
            //TODO cancel end exit
        });

        ImageButton saveBtn = findViewById(R.id.save_button);
        saveBtn.setOnClickListener(v -> {
            //TODO save and return
        });

    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }
}