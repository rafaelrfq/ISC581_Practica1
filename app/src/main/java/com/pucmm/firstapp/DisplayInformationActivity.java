package com.pucmm.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_information);

        Intent intent = getIntent();
        String message = intent.getStringExtra("INFO");

        TextView textView = findViewById(R.id.textView);
        textView.setText(message);
    }
}