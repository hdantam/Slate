package com.helloworld.slate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    Button createWhiteboard;
    Button openWhiteboard;
    static String codeInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        createWhiteboard = (Button) findViewById(R.id.newwhiteboard);
        createWhiteboard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openWhiteboard();
            }
        });

        openWhiteboard = (Button) findViewById(R.id.openwhiteboard);
        openWhiteboard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openWhiteboard();
                EditText textBoxInput = findViewById(R.id.enteredCode);
                codeInput = textBoxInput.getText().toString();
            }
        });
    }

    public static String getCode(){
        return codeInput;
    }

    public void openWhiteboard(){
        Intent intent = new Intent(this, PaintViewNew.class);
        startActivity(intent);
    }

}
