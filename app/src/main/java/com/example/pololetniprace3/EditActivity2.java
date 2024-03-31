package com.example.pololetniprace3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;

public class EditActivity2 extends AppCompatActivity {

    private EditText questionEditText;
    private EditText hintEditText;
    private EditText valueEditText;
    private DatabaseHelper dbHelper;
    private int currentCardId; // To keep track of the current card ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit2);

        // Initialize EditText views
        questionEditText = findViewById(R.id.text);
        hintEditText = findViewById(R.id.hint);
        valueEditText = findViewById(R.id.texthint);

        // Initialize dbHelper
        dbHelper = new DatabaseHelper(this);

        // Retrieve card data by ID (replace 1 with the actual card ID)
        currentCardId = dbHelper.getSmallestCardId(); // Initialize with the smallest card ID
        String[] cardData = dbHelper.getCardDataById(currentCardId);

        // Set card data to EditText fields
        questionEditText.setText(cardData[0]);
        hintEditText.setText(cardData[1]);
        valueEditText.setText(cardData[2]);

        // Set OnClickListener for the "Next" button
        Button nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update card values in the database
                updateCardValuesInDatabase(currentCardId);

                // Load the next card
                currentCardId++;
                loadNextCard();
            }
        });

        // Set OnClickListener for the "Done" button
        Button doneButton = findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update card values in the database
                updateCardValuesInDatabase(currentCardId);

                // Return to MainActivity
                Intent intent = new Intent(EditActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the current card
                dbHelper.deleteCard(currentCardId);

                // Update current card ID
                currentCardId--;

                // Load the next card
                loadNextCard();
            }
        });

    }

    private void updateCardValuesInDatabase(int cardId) {
        String question = questionEditText.getText().toString();
        String hint = hintEditText.getText().toString();
        String answer = valueEditText.getText().toString();
        dbHelper.updateCardValues(cardId, question, hint, answer);
    }

    private void loadNextCard() {
        int smallestCardId = dbHelper.getSmallestCardId();
        if (smallestCardId != -1) {
            String[] nextCardData = dbHelper.getCardDataById(smallestCardId);
            if (nextCardData != null) {
                questionEditText.setText(nextCardData[0]);
                hintEditText.setText(nextCardData[1]);
                valueEditText.setText(nextCardData[2]);
                currentCardId = smallestCardId; // Update current card ID
            }
        }
    }
}
