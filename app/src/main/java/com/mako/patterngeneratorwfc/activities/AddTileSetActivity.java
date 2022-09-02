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
    private NumberPicker mNumberPickerRow;
    private NumberPicker mNumberPickerCol;

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


         mNumberPickerRow = findViewById(R.id.number_picker_row);
         mNumberPickerCol = findViewById(R.id.number_picker_col);

        mNumberPickerRow.setMinValue(1);
        mNumberPickerRow.setMaxValue(7);
        mNumberPickerRow.setOnValueChangedListener((picker, oldVal, newVal) -> {
            Log.d(TAG, "onCreate: number picker 1: new value: " + newVal);
            //synchronize numberPickers, so both have the same value
            //numberPickerCol.setValue(newVal);
        });
        mNumberPickerCol.setMinValue(1);
        mNumberPickerCol.setMaxValue(7);
        mNumberPickerCol.setOnValueChangedListener((picker, oldVal, newVal) -> {
            Log.d(TAG, "onCreate: number picker 2: new value: " + newVal);
            //synchronize numberPickers, so both have the same value
            //numberPickerRow.setValue(newVal);
        });

        initAddAndSubtractButtonsOnClick();
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

    private void initAddAndSubtractButtonsOnClick(){
        ImageButton addRow = findViewById(R.id.add_row);
        ImageButton subtractRow = findViewById(R.id.subtract_row);
        ImageButton addCol = findViewById(R.id.add_col);
        ImageButton subtractCol = findViewById(R.id.subtract_col);

        addRow.setOnClickListener((view) -> {
            Log.d(TAG, "initAddAndSubtractButtonsOnClick: addRow");
        });

        subtractRow.setOnClickListener((view) -> {
            Log.d(TAG, "initAddAndSubtractButtonsOnClick: subtractRow");
        });

        addCol.setOnClickListener((view) -> {
            Log.d(TAG, "initAddAndSubtractButtonsOnClick: addCol");
        });

        subtractCol.setOnClickListener((view) -> {
            Log.d(TAG, "initAddAndSubtractButtonsOnClick: subtractCol");
        });
    }
}