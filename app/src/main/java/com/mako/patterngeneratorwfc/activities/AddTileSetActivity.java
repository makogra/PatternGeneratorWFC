package com.mako.patterngeneratorwfc.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

public class AddTileSetActivity extends AppCompatActivity {

    private AddTileSetViewModel mAddTileSetViewModel;
    private static final String TAG = "AddTileSetActivity";
    private GridLayout mMainContent;
    private int rows,
                cols;

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
        initAddTileSetViewModel();
        restoreValueGrid();

    }

    private void initAddTileSetViewModel() {
        if (mAddTileSetViewModel.getTileSet() == null){
            Log.d(TAG, "initAddTileSetViewModel: ");
            mAddTileSetViewModel.setTileSet(mAddTileSetViewModel.getSampleTileSet());
        }
    }

    private void restoreValueGrid() {
        int[][] valueGrid = mAddTileSetViewModel.getValueGrid();
        rows = valueGrid.length;
        cols = valueGrid[0].length;
        
        if (cols != mMainContent.getColumnCount()){
            mMainContent.setColumnCount(cols);
        }

        TextView textView;
        int row,col;
        Log.d(TAG, "restoreValueGrid: RED = " + (int)Color.RED + " Green = " + Color.GREEN + " Gray = " + Color.GRAY);
        for (int i = 0; i < rows * cols; i++) {
            row = i / cols;
            col = i % cols;
            Log.d(TAG, "restoreValueGrid: row = " + row + " col = " + col + " value = " + valueGrid[row][col]);
            if (i < mMainContent.getChildCount()){
                textView = (TextView) mMainContent.getChildAt(i);
                textView.setTag(valueGrid[row][col]);
                textView.setBackgroundColor((Integer) textView.getTag());
            } else {
                textView = templateView();
                textView.setTag(valueGrid[row][col]);
                textView.setBackgroundColor((Integer) textView.getTag());
                mMainContent.addView(textView);
            }
        }
        updateText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveValueGrid();
    }

    private void saveValueGrid() {
        int[][] valueGrid = new int[rows][cols];
        int index;
        TextView textView;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                index = i * cols + j;
                textView = (TextView) mMainContent.getChildAt(index);
                valueGrid[i][j] = (int) textView.getTag();
            }
        }

        mAddTileSetViewModel.setValueGrid(valueGrid);
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
        for (int i = 0; i < cols; i++) {
            textView = templateView();
            mMainContent.addView(textView);
        }
        rows++;

        updateText();
    }

    private void addCol() {
        mMainContent.setColumnCount(mMainContent.getColumnCount() + 1);
        cols++;
        TextView textView;
        int index;
        for (int i = 0; i < rows; i++) {
            textView = templateView();
            index = (i + 1) * cols - 1;
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

    private void initGridLayout(){
        //GridLayout mainContent = findViewById(R.id.main_content);
    }

    private void updateMainContent(GridLayout mainContent){
        if (mainContent.getRowCount() * mainContent.getColumnCount() > mainContent.getChildCount()){
            fillMainContent(mainContent);
        } else {
            deleteOverFlow(mainContent);
        }
    }

    private void deleteOverFlow(GridLayout mainContent) {
        while (isOverFlow(mainContent)){
            mainContent.removeViewAt(mainContent.getChildCount() - 1);
        }
    }

    private void deleteOverFlowTo(GridLayout mainContent, int newChildCount){
        while (mainContent.getChildCount() > newChildCount){
            mainContent.removeViewAt(newChildCount);
        }
    }

    private void fillMainContent(GridLayout mainContent) {
        TextView textView;
        while (!isFull(mainContent)){
            textView = new TextView(this);

            mainContent.addView(findViewById(R.id.add_col));
        }
    }

    private boolean isOverFlow(GridLayout mainContent){
        return mainContent.getChildCount() > mainContent.getRowCount() * mainContent.getColumnCount();
    }

    private boolean isFull(GridLayout mainContent){
        return mainContent.getChildCount() == mainContent.getRowCount() * mainContent.getColumnCount();
    }

    private TextView templateView(){
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = 100;
        params.width = 100;
        TextView textView = new TextView(this);
        textView.setTag(Color.GREEN);
        textView.setBackgroundColor((Integer) textView.getTag());
        textView.setLayoutParams(params);
        textView.setOnClickListener((view) -> {
            //TODO Add onclicklistener
            view.setTag(Color.RED);
            view.setBackgroundColor((Integer) view.getTag());
        });

        return textView;
    }
}