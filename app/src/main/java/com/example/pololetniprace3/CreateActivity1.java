package com.example.pololetniprace3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class CreateActivity1 extends AppCompatActivity {

    private static final String TAG = "CreateActivity1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create1);

        Button nextButton = findViewById(R.id.next);
        EditText editText = findViewById(R.id.cardsetname);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardSetName = editText.getText().toString().trim();
                if (!cardSetName.isEmpty()) {
                    DatabaseHelper dbHelper = new DatabaseHelper(CreateActivity1.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("card_set_name", cardSetName);
                    // Insert the new card set and get its ID
                    long cardSetId = db.insert("card_sets", null, values);
                    db.close();

                    Log.d(TAG, "Inserted card set ID: " + cardSetId); // Log the inserted card set ID

                    if (cardSetId != -1) {
                        // Pass the card set ID to CreateActivity2
                        Intent createIntent2 = new Intent(CreateActivity1.this, CreateActivity2.class);
                        createIntent2.putExtra("card_set_id", cardSetId); // Pass the correct card set ID
                        startActivity(createIntent2);
                    } else {
                        Toast.makeText(CreateActivity1.this, "Failed to create card set", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateActivity1.this, "Please enter a card set name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}