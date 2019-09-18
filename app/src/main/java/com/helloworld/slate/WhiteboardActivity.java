package com.helloworld.slate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WhiteboardActivity extends AppCompatActivity {
    Button clrButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whiteboard);
        clrButton = findViewById(R.id.clrButton);
        final Whiteboard p = findViewById(R.id.paintV);

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
