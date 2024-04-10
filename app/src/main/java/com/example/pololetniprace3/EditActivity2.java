package com.example.pololetniprace3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

public class EditActivity2 extends AppCompatActivity {

    private EditText questionEditText;
    private EditText hintEditText;
    private EditText answerEditText;
    private DatabaseHelper dbHelper;
    private int currentCardId; // To keep track of the current card ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit2);

        // Initialize EditText views
        questionEditText = findViewById(R.id.question);
        hintEditText = findViewById(R.id.hint);
        answerEditText = findViewById(R.id.answer);

        // Initialize dbHelper
        dbHelper = new DatabaseHelper(this);

        // Retrieve card data by ID (replace 1 with the actual card ID)
        currentCardId = dbHelper.getSmallestCardId(); // Initialize with the smallest card ID
        String[] cardData = dbHelper.getCardDataById(currentCardId);

        // Set card data to EditText fields
        questionEditText.setText(cardData[0]);
        hintEditText.setText(cardData[1]);
        answerEditText.setText(cardData[2]);

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

        /*
        Button deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the current card
                dbHelper.deleteCard(currentCardId);

                // Load the next card
                loadNextCard();
            }
        });
         */
    }


    private void updateCardValuesInDatabase(int cardId) {
        String question = questionEditText.getText().toString();
        String hint = hintEditText.getText().toString();
        String answer = answerEditText.getText().toString();
        dbHelper.updateCardValues(cardId, question, hint, answer);
    }

    private void loadNextCard() {
        // Increment the current card ID to get data for the next card

        // Retrieve data for the next card
        String[] nextCardData = dbHelper.getCardDataById(currentCardId);

        // Check if data for the next card exists
        if (nextCardData != null) {
            // Update the EditText fields with data for the next card
            questionEditText.setText(nextCardData[0]);
            hintEditText.setText(nextCardData[1]);
            answerEditText.setText(nextCardData[2]);

            // Update card values in the database
            updateCardValuesInDatabase(currentCardId);
        } else {
            // Decrement the current card ID as no next card exists
            currentCardId--;

            // Display a toast message indicating it's the last card
            Toast.makeText(this, "This is the last card of the set", Toast.LENGTH_SHORT).show();
        }
    }
}
