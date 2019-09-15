package com.helloworld.slate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PaintViewNew extends AppCompatActivity {
    Button clrButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_view_new);
        clrButton = findViewById(R.id.clrButton);
        final PaintV p = findViewById(R.id.paintV);
        //Adds method to respond to input
        p.addEventListener();
        getSupportActionBar().setSubtitle("Room #" + p.getCode());
        clrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.resetCanvas();
            }
        });
    }
}
