// AddActivity2.java

package com.example.pololetniprace3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;

public class AddActivity2 extends AppCompatActivity {

    private EditText questionEditText;
    private EditText hintEditText;
    private EditText valueEditText;
    private DatabaseHelper dbHelper;
    private String cardSetName; // To store the selected card set name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add2);

        // Retrieve the selected card set name from the intent
        Intent intent = getIntent();
        if (intent != null) {
            cardSetName = intent.getStringExtra("card_set_name");
        }

        // Initialize EditText views
        questionEditText = findViewById(R.id.question);
        hintEditText = findViewById(R.id.hint);
        valueEditText = findViewById(R.id.texthint);

        // Initialize dbHelper
        dbHelper = new DatabaseHelper(this);

        // Set OnClickListener for the "Save" button
        Button saveButton = findViewById(R.id.done);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Insert new card into the selected card set
                insertNewCard();

                // Return to MainActivity
                Intent intent = new Intent(AddActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Insert current card into the database
                insertNewCard();

                questionEditText.setText("");
                hintEditText.setText("");
                valueEditText.setText("");
            }
        });

    }

    private void insertNewCard() {
        // Get values from EditText fields
        String question = questionEditText.getText().toString();
        String hint = hintEditText.getText().toString();
        String answer = valueEditText.getText().toString();

        // Insert new card into the selected card set
        dbHelper.insertCard(cardSetName, question, hint, answer);
    }
}
