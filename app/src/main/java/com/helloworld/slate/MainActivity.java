package com.helloworld.slate;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private PaintView paintView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        paintView = new PaintView(this);
        paintView.addEventListener();

        super.onCreate(savedInstanceState);
        setContentView(paintView);
    }
}
