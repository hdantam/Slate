package com.helloworld.slate;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.api.Backend;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    //private static BackendSocket backendSocket;
    private PaintView paintView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //backendSocket = new BackendSocket();
        paintView = new PaintView(this);
        paintView.addEventListener();
        super.onCreate(savedInstanceState);

        //backendSocket.addEventListener();
        setContentView(paintView);
    }
}
