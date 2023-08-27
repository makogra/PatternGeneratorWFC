package com.mako.patterngeneratorwfc.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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

            if (!valueToStringMap.contains(key)){
                valueToStringMap.add(key);
            }
            tag = valueToStringMap.indexOf(key);
        }

        private void setTag(int tag){
            this.tag = tag;
        }
    }

    private AddTileSetViewModel addTileSetViewModel;
    private static final String TAG = "AddTileSetActivity";

    private GridLayout mainContent;
    private int rows,
                cols;
    private int[][] valueGrid;
    private List<String> valueToStringMap;
    private CurrentColor currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tile_set);

        addTileSetViewModel = new ViewModelProvider(this).get(AddTileSetViewModel.class);

        cancelOnClick();
        saveOnClick();
        initAddAndSubtractButtonsOnClick();
        initTileSet();
        restoreValueGrid();
        initCurrentColor();
        initChangeTileSet();
    }

    private void initChangeTileSet() {
        ImageView currentTileSetView = findViewById(R.id.current_tile_set);
        currentTileSetView.setImageDrawable(new ColorDrawable(Colors.getValue(valueToStringMap.get(currentColor.tag))));
        //add tag?
        currentTileSetView.setOnClickListener((view) -> {
            //TODO add onClick listener, as popup menu to chose color.
            //temp change to another color
            //cyrcle throuw all curently colors in map
            currentColor.tag++;
            if (currentColor.tag == valueToStringMap.size())
                currentColor.tag = 1;

            currentTileSetView.setImageDrawable(new ColorDrawable(Colors.getValue(valueToStringMap.get(currentColor.tag))));
        });

    }

    private void saveOnClick() {
        ImageButton saveBtn = findViewById(R.id.save_button);
        saveBtn.setOnClickListener(v -> save());
    }

    private void cancelOnClick() {
        ImageButton cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(v -> cancel());
    }

    private void initCurrentColor() {
        if (valueGrid.length > 0 && valueGrid[0].length > 0){
            currentColor = new CurrentColor(valueGrid[0][0]);
            return;
        }
        if (valueToStringMap.isEmpty()){
            valueToStringMap.add(Colors.getKey(Color.GREEN));
        }
        currentColor = new CurrentColor(1);
    }

    private void initTileSet() {
        if (addTileSetViewModel.getTileSet() == null){
            Log.d(TAG, "initAddTileSetViewModel: ");
            addTileSetViewModel.setTileSet(addTileSetViewModel.getSampleTileSet());
        }
        valueGrid = addTileSetViewModel.getValueGrid();
        valueToStringMap = addTileSetViewModel.getValueToStringPath();
    }

    private void restoreValueGrid() {
        rows = valueGrid.length;
        cols = valueGrid[0].length;
        
        if (cols != mainContent.getColumnCount()){
            mainContent.setColumnCount(cols);
        }

        TextView textView;
        int row,col;
        for (int i = 0; i < rows * cols; i++) {
            row = i / cols;
            col = i % cols;
            Log.d(TAG, "restoreValueGrid: row = " + row + " col = " + col + " value = " + valueGrid[row][col]);
            if (i < mainContent.getChildCount()){
                textView = (TextView) mainContent.getChildAt(i);
                textView.setTag(R.integer.tag_color, valueGrid[row][col]);
                textView.setTag(R.integer.tag_row, row);
                textView.setTag(R.integer.tag_col, col);
                textView.setBackgroundColor(getColorWithTag(textView.getTag(R.integer.tag_color)));
            } else {
                textView = templateView();
                textView.setTag(R.integer.tag_color, valueGrid[row][col]);
                textView.setTag(R.integer.tag_row, row);
                textView.setTag(R.integer.tag_col, col);
                textView.setBackgroundColor(getColorWithTag(textView.getTag(R.integer.tag_color)));
                mainContent.addView(textView);
            }
        }
        updateText();
    }

    private int getColor(int row, int col) {
        int value = valueGrid[row][col];

        String colorStr = valueToStringMap.get(value);

        return Colors.getValue(colorStr);
    }

    private int getColorWithTag(Object tag){
        String colorStr = valueToStringMap.get((Integer) tag);
        return Colors.getValue(colorStr);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveValueGrid();
    }

    private void saveValueGrid() {
        valueGrid = new int[rows][cols];
        int index;
        TextView textView;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                index = i * cols + j;
                textView = (TextView) mainContent.getChildAt(index);
                valueGrid[i][j] = (int) textView.getTag(R.integer.tag_color);
            }
        }

        addTileSetViewModel.setValueGrid(valueGrid);
    }

    private void save() {
        saveValueGrid();
        Intent intent = new Intent();
        EditText editText = findViewById(R.id.activity_add_tile_set_id_text_edit);
        String id = editText.getText().toString();
        if (id.equals(""))
            id = addTileSetViewModel.getTileId();
        else
            addTileSetViewModel.setTileId(id);
        Log.i(TAG, "add new tile set of id : " + id);
        intent.putExtra("TileSet", addTileSetViewModel.getTileSet());

        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        addTileSetViewModel.setTileSet(addTileSetViewModel.getSampleTileSet());
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

        mainContent = findViewById(R.id.main_content);
        rows = mainContent.getRowCount();
        cols = mainContent.getColumnCount();

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
            mainContent.removeViewAt(i * cols - 1);
        }
        mainContent.setColumnCount(mainContent.getColumnCount() - 1);
        cols--;

        updateText();
    }

    private void subtractRow() {
        if (rows <= 0)
            return;
        for (int i = 0; i < cols; i++) {
            mainContent.removeViewAt(mainContent.getChildCount() - 1);
        }
        rows--;
    }

    private void addRow() {
        TextView textView;
        rows++;
        updateValueGrid();
        for (int i = 0; i < cols; i++) {
            textView = templateView();
            textView.setTag(R.integer.tag_color, valueGrid[rows - 1][i]);
            textView.setTag(R.integer.tag_row, rows - 1);
            textView.setTag(R.integer.tag_col, i);
            textView.setBackgroundColor(getColorWithTag(textView.getTag(R.integer.tag_color)));
            mainContent.addView(textView);
        }


        updateText();
    }

    private void updateValueGrid() {
        int[][] newValueGrid = new int[rows][cols];
        int[][] oldValueGrid = valueGrid;

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

        valueGrid = newValueGrid;
        addTileSetViewModel.setValueGrid(newValueGrid);
    }

    private void addCol() {
        mainContent.setColumnCount(mainContent.getColumnCount() + 1);
        cols++;
        TextView textView;
        updateValueGrid();
        int index;
        for (int i = 0; i < rows; i++) {
            textView = templateView();
            index = (i + 1) * cols - 1;
            textView.setTag(R.integer.tag_color, valueGrid[i][cols - 1]);
            textView.setTag(R.integer.tag_row, i);
            textView.setTag(R.integer.tag_col, cols - 1);
            textView.setBackgroundColor(getColorWithTag(textView.getTag(R.integer.tag_color)));
            mainContent.addView(textView, index);
        }

        updateText();
    }

    private void updateText(){
        TextView textView;
        for (int i = 0; i < cols * rows; i++) {
            textView = (TextView) mainContent.getChildAt(i);
            textView.setText("" + i);
        }
    }

    private TextView templateView(){
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = 100;
        params.width = 100;
        TextView textView = new TextView(this);
        textView.setTag(R.integer.tag_color, valueGrid[rows - 1][cols - 1]);
        textView.setBackgroundColor(getColorWithTag(textView.getTag(R.integer.tag_color)));
        textView.setLayoutParams(params);
        textView.setOnClickListener((view) -> {
            int row = (int) view.getTag(R.integer.tag_row);
            int col = (int) view.getTag(R.integer.tag_col);
            Log.d(TAG, "templateView: row = " + row + " col = " + col);
            valueGrid[row][col] = currentColor.tag;
            view.setTag(R.integer.tag_color, currentColor.tag);

            view.setBackgroundColor(getColorWithTag(view.getTag(R.integer.tag_color)));
        });
        // TODO add drag over

        return textView;
    }
}