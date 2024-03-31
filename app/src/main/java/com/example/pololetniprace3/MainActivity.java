package com.example.pololetniprace3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button practice = findViewById(R.id.practice);
        Button create = findViewById(R.id.create);
        Button edit = findViewById(R.id.edit);
        Button add = findViewById(R.id.add);

        practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent practiceIntent = new Intent(MainActivity.this, PracticeActivity1.class);
                startActivity(practiceIntent);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(MainActivity.this, CreateActivity1.class);
                startActivity(createIntent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(MainActivity.this, EditActivity1.class);
                startActivity(editIntent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(MainActivity.this, AddActivity1.class);
                startActivity(addIntent);
            }
        });
    }
}