package com.example.komputer.ism_app_grafika;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private int COLOR_RED;
    private int COLOR_YELLOW;
    private int COLOR_BLUE;
    private int COLOR_GREEN;

    private PowierzchniaRysunku powierzchniaRysunku;

    private Button btnRed;
    private Button btnYellow;
    private Button btnBlue;
    private Button btnGreen;
    private Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        COLOR_RED = getResources().getColor(R.color.colorRed);
        COLOR_YELLOW = getResources().getColor(R.color.colorYellow);
        COLOR_BLUE = getResources().getColor(R.color.colorBlue);
        COLOR_GREEN = getResources().getColor(R.color.colorGreen);

        powierzchniaRysunku = (PowierzchniaRysunku) findViewById(R.id.surfaceView);

        btnRed = (Button)findViewById(R.id.btnRed);
        btnYellow = (Button)findViewById(R.id.btnYellow);
        btnBlue = (Button)findViewById(R.id.btnBlue);
        btnGreen = (Button)findViewById(R.id.btnGreen);
        btnClear = (Button)findViewById(R.id.btnClear);

        btnRed.setOnClickListener(createColorSetOnClickListener(COLOR_RED));
        btnYellow.setOnClickListener(createColorSetOnClickListener(COLOR_YELLOW));
        btnBlue.setOnClickListener(createColorSetOnClickListener(COLOR_BLUE));
        btnGreen.setOnClickListener(createColorSetOnClickListener(COLOR_GREEN));
        btnClear.setOnClickListener(createClearOnClickListener());
    }

    Button.OnClickListener createColorSetOnClickListener(final int color) {
        return new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                powierzchniaRysunku.setColor(color);
            }
        };
    }

    Button.OnClickListener createClearOnClickListener() {
        return new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                powierzchniaRysunku.clear();
            }
        };
    }
}
