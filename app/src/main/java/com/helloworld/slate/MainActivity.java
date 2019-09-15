package com.helloworld.slate;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.api.Backend;

public class MainActivity extends AppCompatActivity {
    private static BackendSocket backendSocket;
    private static PaintView paintView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        backendSocket = new BackendSocket();
        paintView = new PaintView(this);
        super.onCreate(savedInstanceState);

        backendSocket.addEventListener();
        setContentView(paintView);
    }

    public static void updateWhiteboard(Bitmap b){
        backendSocket.updateWhiteboard(b);
    }

    public static void setBitmap(Bitmap b){
        paintView.setBitmap(b);
    }

    public static Bitmap getBitmap() {
        return paintView.getBitmap();
    }

}
