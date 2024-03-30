package com.example.pololetniprace3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pololetniprace3.DatabaseHelper;
import com.example.pololetniprace3.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PracticeActivity2 extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView questionTextView;
    private Button done;
    private Button nextButton;
    private String cardSetName;
    private List<String> questionsList;
    private Set<String> shownQuestionsSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice2);

        cardSetName = getIntent().getStringExtra("card_set_name");

        dbHelper = new DatabaseHelper(this);
        questionsList = getQuestionsForCardSet(cardSetName);
        shownQuestionsSet = new HashSet<>();

        String randomQuestion = getRandomQuestion();
        questionTextView = findViewById(R.id.text);
        questionTextView.setText(randomQuestion);

        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to MainActivity
                Intent intent = new Intent(PracticeActivity2.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close this activity
            }
        });

        nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String randomQuestion = getRandomQuestion();
                questionTextView.setText(randomQuestion);
            }
        });

        View rectangleView = findViewById(R.id.rectangle);
        rectangleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the answer TextView
                String answer = "This is the answer."; // Replace this with your actual answer

                // Replace the text in the question TextView with the answer
                questionTextView.setText(answer);
            }
        });
    }

    private List<String> getQuestionsForCardSet(String cardSetName) {
        List<String> questionsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_QUESTION +
                " FROM " + DatabaseHelper.TABLE_CARDS +
                " INNER JOIN " + DatabaseHelper.TABLE_CARD_SETS +
                " ON " + DatabaseHelper.TABLE_CARDS + "." + DatabaseHelper.COLUMN_SET_ID +
                " = " + DatabaseHelper.TABLE_CARD_SETS + "." + DatabaseHelper.COLUMN_ID +
                " WHERE " + DatabaseHelper.COLUMN_CARD_SET_NAME + " = ?", new String[]{cardSetName});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int questionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_QUESTION);
                if (questionIndex != -1) {
                    String question = cursor.getString(questionIndex);
                    questionsList.add(question);
                }
            }
            cursor.close();
        }

        db.close();
        return questionsList;
    }

    private String getRandomQuestion() {
        // Ensure there are questions left to show
        if (questionsList.isEmpty()) {
            return "No questions available for this card set.";
        }

        // If all questions have been shown at least once, reset the shown questions set
        if (shownQuestionsSet.size() == questionsList.size()) {
            shownQuestionsSet.clear();
        }

        // Get a random question from the list
        Random random = new Random();
        String randomQuestion;
        do {
            randomQuestion = questionsList.get(random.nextInt(questionsList.size()));
        } while (shownQuestionsSet.contains(randomQuestion));

        // Add the question to the shown questions set
        shownQuestionsSet.add(randomQuestion);
        return randomQuestion;
    }
}