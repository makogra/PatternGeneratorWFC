package com.mako.patterngeneratorwfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mako.patterngeneratorwfc.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ScreenSlidePager.class);
        startActivity(intent);

        Button goToSliderBtn = findViewById(R.id.go_to_slider_btn);
        goToSliderBtn.setOnClickListener(v -> startActivity(intent));
    }

}