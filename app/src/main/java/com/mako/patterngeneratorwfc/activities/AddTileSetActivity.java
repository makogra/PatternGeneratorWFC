package com.mako.patterngeneratorwfc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.datamodels.AddTileSetViewModel;

public class AddTileSetActivity extends AppCompatActivity {

    private AddTileSetViewModel mAddTileSetViewModel;
    private static final String TAG = "AddTileSetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tile_set);

        mAddTileSetViewModel = new ViewModelProvider(this).get(AddTileSetViewModel.class);
        mAddTileSetViewModel.setTileSet(mAddTileSetViewModel.getSampleTileSet());

        ImageButton cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(v -> cancel());

        ImageButton saveBtn = findViewById(R.id.save_button);
        saveBtn.setOnClickListener(v -> {
            save();
        });


        NumberPicker numberPickerRow = findViewById(R.id.number_picker_row);
        NumberPicker numberPickerCol = findViewById(R.id.number_picker_col);

        numberPickerRow.setMinValue(1);
        numberPickerRow.setMaxValue(7);
        numberPickerRow.setOnValueChangedListener((picker, oldVal, newVal) -> {
            Log.d(TAG, "onCreate: number picker 1: new value: " + newVal);
            //synchronize numberPickers, so both have the same value
            numberPickerCol.setValue(newVal);
        });
        numberPickerCol.setMinValue(1);
        numberPickerCol.setMaxValue(7);
        numberPickerCol.setOnValueChangedListener((picker, oldVal, newVal) -> {
            Log.d(TAG, "onCreate: number picker 2: new value: " + newVal);
            //synchronize numberPickers, so both have the same value
            numberPickerRow.setValue(newVal);
        });
    }

    private void save() {
        Intent intent = new Intent();
        EditText editText = findViewById(R.id.activity_add_tile_set_id_text_view);
        String id = editText.getText().toString();
        if (id.equals(""))
            id = mAddTileSetViewModel.getTileId();
        else
            mAddTileSetViewModel.setTileId(id);
        Log.i(TAG, "add new tile set of id : " + id);
        intent.putExtra("TileSet", mAddTileSetViewModel.getTileSet());

        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        mAddTileSetViewModel.setTileSet(mAddTileSetViewModel.getSampleTileSet());
        super.onResume();
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }
}