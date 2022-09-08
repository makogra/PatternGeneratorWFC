package com.mako.patterngeneratorwfc.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.datamodels.AddTileSetViewModel;
import com.mako.patterngeneratorwfc.wfc.Colors;

import java.util.List;

public class AddTileSetActivity extends AppCompatActivity {

    private class CurrentColor {

        private int tag;

        CurrentColor(int tag){
            this.tag = tag;
        }

        private void setColor(int color){
            String key = Colors.getKey(color);

            if (!mValueToStringMap.contains(key)){
                mValueToStringMap.add(key);
            }
            tag = mValueToStringMap.indexOf(key);
        }

        private void setTag(int tag){
            this.tag = tag;
        }
    }

    private AddTileSetViewModel mAddTileSetViewModel;
    private static final String TAG = "AddTileSetActivity";
    private static final int TAG_COLOR = 0;
    private static final int TAG_ROW = 1;
    private static final int TAG_COLUMN = 2;

    private GridLayout mMainContent;
    private int rows,
                cols;
    private int[][] mValueGrid;
    private List<String> mValueToStringMap;
    private CurrentColor currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tile_set);

        mAddTileSetViewModel = new ViewModelProvider(this).get(AddTileSetViewModel.class);

        ImageButton cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(v -> cancel());

        ImageButton saveBtn = findViewById(R.id.save_button);
        saveBtn.setOnClickListener(v -> save());

        initAddAndSubtractButtonsOnClick();
        initTileSet();
        restoreValueGrid();
        initCurrentColor();

    }

    private void initCurrentColor() {
        if (mValueGrid.length > 0 && mValueGrid[0].length > 0){
            currentColor = new CurrentColor(mValueGrid[0][0]);
            return;
        }
        if (mValueToStringMap.isEmpty()){
            mValueToStringMap.add(Colors.getKey(Color.GREEN));
        }
        currentColor = new CurrentColor(1);
    }

    private void initTileSet() {
        if (mAddTileSetViewModel.getTileSet() == null){
            Log.d(TAG, "initAddTileSetViewModel: ");
            mAddTileSetViewModel.setTileSet(mAddTileSetViewModel.getSampleTileSet());
        }
        mValueGrid = mAddTileSetViewModel.getValueGrid();
        mValueToStringMap = mAddTileSetViewModel.getValueToStringPath();
    }

    private void restoreValueGrid() {
        rows = mValueGrid.length;
        cols = mValueGrid[0].length;
        
        if (cols != mMainContent.getColumnCount()){
            mMainContent.setColumnCount(cols);
        }

        TextView textView;
        int row,col;
        for (int i = 0; i < rows * cols; i++) {
            row = i / cols;
            col = i % cols;
            Log.d(TAG, "restoreValueGrid: row = " + row + " col = " + col + " value = " + mValueGrid[row][col]);
            if (i < mMainContent.getChildCount()){
                textView = (TextView) mMainContent.getChildAt(i);
                textView.setTag(R.integer.tag_color, mValueGrid[row][col]);
                textView.setTag(R.integer.tag_row, row);
                textView.setTag(R.integer.tag_col, col);
                textView.setBackgroundColor(getColorWithTag(textView.getTag(R.integer.tag_color)));
            } else {
                textView = templateView();
                textView.setTag(R.integer.tag_color, mValueGrid[row][col]);
                textView.setTag(R.integer.tag_row, row);
                textView.setTag(R.integer.tag_col, col);
                textView.setBackgroundColor(getColorWithTag(textView.getTag(R.integer.tag_color)));
                mMainContent.addView(textView);
            }
        }
        updateText();
    }

    private int getColor(int row, int col) {
        int value = mValueGrid[row][col];

        String colorStr = mValueToStringMap.get(value);

        return Colors.getValue(colorStr);
    }

    private int getColorWithTag(Object tag){
        String colorStr = mValueToStringMap.get((Integer) tag);
        return Colors.getValue(colorStr);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveValueGrid();
    }

    private void saveValueGrid() {
        mValueGrid = new int[rows][cols];
        int index;
        TextView textView;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                index = i * cols + j;
                textView = (TextView) mMainContent.getChildAt(index);
                mValueGrid[i][j] = (int) textView.getTag(R.integer.tag_color);
            }
        }

        mAddTileSetViewModel.setValueGrid(mValueGrid);
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

        //CustomGridLayout mainContent = new CustomGridLayout(this, findViewById(R.id.main_content));
        mMainContent = findViewById(R.id.main_content);
        rows = mMainContent.getRowCount();
        cols = mMainContent.getColumnCount();

        addRow.setOnClickListener((view) -> {
            Log.d(TAG, "initAddAndSubtractButtonsOnClick: addRow");
            addRow();
        });

        subtractRow.setOnClickListener((view) -> {
            Log.d(TAG, "initAddAndSubtractButtonsOnClick: subtractRow");
            subtractRow();
        });

        addCol.setOnClickListener((view) -> {
            Log.d(TAG, "initAddAndSubtractButtonsOnClick: addCol");
            addCol();
        });

        subtractCol.setOnClickListener((view) -> {
            Log.d(TAG, "initAddAndSubtractButtonsOnClick: subtractCol");
            subtractCol();
        });
    }

    private void subtractCol() {
        if (cols <= 0)
            return;
        for (int i = rows; i > 0; i--) {
            mMainContent.removeViewAt(i * cols - 1);
        }
        mMainContent.setColumnCount(mMainContent.getColumnCount() - 1);
        cols--;

        updateText();
    }

    private void subtractRow() {
        if (rows <= 0)
            return;
        for (int i = 0; i < cols; i++) {
            mMainContent.removeViewAt(mMainContent.getChildCount() - 1);
        }
        rows--;
    }

    private void addRow() {
        TextView textView;
        rows++;
        updateValueGrid();
        for (int i = 0; i < cols; i++) {
            textView = templateView();
            textView.setTag(R.integer.tag_color, mValueGrid[rows - 1][i]);
            textView.setTag(R.integer.tag_row, rows - 1);
            textView.setTag(R.integer.tag_col, i);
            textView.setBackgroundColor(getColorWithTag(textView.getTag(R.integer.tag_color)));
            mMainContent.addView(textView);
        }


        updateText();
    }

    private void updateValueGrid() {
        int[][] newValueGrid = new int[rows][cols];
        int[][] oldValueGrid = mValueGrid;

        boolean addInRow;
        boolean addInCol;
        for (int i = 0; i < rows; i++) {
            addInRow = oldValueGrid.length <= i;
            for (int j = 0; j < cols; j++) {
                addInCol = oldValueGrid[0].length <= j;

                if (addInRow) {
                    newValueGrid[i][j] = oldValueGrid[i - 1][j];
                } else if (addInCol) {
                    newValueGrid[i][j] = oldValueGrid[i][j - 1];
                } else {
                    newValueGrid[i][j] = oldValueGrid[i][j];
                }
            }
        }

        mValueGrid = newValueGrid;
        mAddTileSetViewModel.setValueGrid(newValueGrid);
    }

    private void addCol() {
        mMainContent.setColumnCount(mMainContent.getColumnCount() + 1);
        cols++;
        TextView textView;
        updateValueGrid();
        int index;
        for (int i = 0; i < rows; i++) {
            textView = templateView();
            index = (i + 1) * cols - 1;
            textView.setTag(R.integer.tag_color, mValueGrid[i][cols - 1]);
            textView.setTag(R.integer.tag_row, i);
            textView.setTag(R.integer.tag_col, cols - 1);
            textView.setBackgroundColor(getColorWithTag(textView.getTag(R.integer.tag_color)));
            mMainContent.addView(textView, index);
        }

        updateText();
    }

    private void updateText(){
        TextView textView;
        for (int i = 0; i < cols * rows; i++) {
            textView = (TextView) mMainContent.getChildAt(i);
            textView.setText("" + i);
        }
    }

    private TextView templateView(){
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = 100;
        params.width = 100;
        TextView textView = new TextView(this);
        textView.setTag(R.integer.tag_color, mValueGrid[rows - 1][cols - 1]);
        textView.setBackgroundColor(getColorWithTag(textView.getTag(R.integer.tag_color)));
        textView.setLayoutParams(params);
        textView.setOnClickListener((view) -> {
            int row = (int) view.getTag(R.integer.tag_row);
            int col = (int) view.getTag(R.integer.tag_col);
            Log.d(TAG, "templateView: row = " + row + " col = " + col);
            mValueGrid[row][col] = currentColor.tag;
            view.setTag(R.integer.tag_color, currentColor.tag);

            view.setBackgroundColor(getColorWithTag(view.getTag(R.integer.tag_color)));
        });
        // TODO add drag over

        return textView;
    }
}