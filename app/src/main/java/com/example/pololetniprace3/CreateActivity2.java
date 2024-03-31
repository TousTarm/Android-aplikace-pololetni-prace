package com.example.pololetniprace3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateActivity2 extends AppCompatActivity {

    // Database helper instance
    private DatabaseHelper dbHelper;

    // Current card set id (owner)
    private long currentCardSetId; // Change data type to long

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create2);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Get the current card set id passed from the previous activity
        currentCardSetId = getIntent().getLongExtra("card_set_id", -1); // Change data type to long

        // Log the received card set ID
        Log.d("CreateActivity2", "Received card set ID: " + currentCardSetId);

        // Find input fields and next button
        EditText questionEditText = findViewById(R.id.question);
        EditText answerEditText = findViewById(R.id.answer);
        EditText hintEditText = findViewById(R.id.hint); // Add this line to find the hint EditText
        Button nextButton = findViewById(R.id.next);

        // Set click listener for the next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from input fields
                String question = questionEditText.getText().toString().trim();
                String answer = answerEditText.getText().toString().trim();
                String hint = hintEditText.getText().toString().trim(); // Get hint text

                // Insert the card into the database
                if (!question.isEmpty() && !answer.isEmpty()) {
                    insertCard(question, answer, hint); // Pass hint as an argument
                } else {
                    // Show a toast message if either question or answer is empty
                    Toast.makeText(CreateActivity2.this, "Please enter both question and answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    Button doneButton = findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to MainActivity
                Intent intent = new Intent(CreateActivity2.this, MainActivity.class);
                // Start the MainActivity
                startActivity(intent);
                // Finish this activity to remove it from the back stack
                finish();
            }
        });

    }

    // Method to insert a new card into the database
    // Method to insert a new card into the database
    private void insertCard(String question, String answer, String hint) {
        EditText questionEditText = findViewById(R.id.question);
        EditText answerEditText = findViewById(R.id.answer);
        EditText hintEditText = findViewById(R.id.hint); // Add reference to the hint EditText

        // Ensure currentCardSetId is valid
        if (currentCardSetId == -1) {
            Toast.makeText(this, "Invalid card set ID", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_QUESTION, question);
        values.put(DatabaseHelper.COLUMN_ANSWER, answer);
        values.put(DatabaseHelper.COLUMN_HINT, hint); // Insert hint into ContentValues
        values.put(DatabaseHelper.COLUMN_SET_ID, currentCardSetId); // Set the current card set id as owner
        long result = db.insert(DatabaseHelper.TABLE_CARDS, null, values);
        db.close();

        questionEditText.getText().clear();
        answerEditText.getText().clear();
        hintEditText.getText().clear(); // Clear the hint field after insertion

        if (result != -1) {
            Toast.makeText(this, "Card inserted successfully", Toast.LENGTH_SHORT).show();
            // Optionally, you can finish the activity after inserting the card
            // finish();
        } else {
            Toast.makeText(this, "Failed to insert card", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database helper when the activity is destroyed
        dbHelper.close();
    }
}

