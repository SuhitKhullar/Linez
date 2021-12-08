package com.example.linez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView placeName = findViewById(R.id.PlaceName);
        TextView waitTime = findViewById(R.id.WaitTime);

        Intent i = getIntent();

        String name = i.getStringExtra("name");
        Double wait = i.getDoubleExtra("wait", 0.00);

        placeName.setText(name);
        waitTime.setText(String.valueOf(wait) + "minutes");


    }
}