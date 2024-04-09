package com.example.pololetniprace3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private Button hintButton; // Add hint button
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
                Intent intent = new Intent(PracticeActivity2.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the hint text
                TextView hintTextView = findViewById(R.id.texthint);
                hintTextView.setText("");

                // Get the next random question
                String randomQuestion = getRandomQuestion();
                questionTextView.setText(randomQuestion);
            }
        });


        // Initialize hint button
        hintButton = findViewById(R.id.hint);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current question displayed
                String currentQuestion = questionTextView.getText().toString();
                // Retrieve hint from the database based on the current question
                String hint = getHintForQuestion(currentQuestion);
                // Display the hint or a message if no hint is available
                if (!hint.isEmpty()) {
                    // Set the hint text in the appropriate TextView
                    TextView hintTextView = findViewById(R.id.texthint);
                    hintTextView.setText(hint);
                } else {
                    // Set a message if no hint is available
                    TextView hintTextView = findViewById(R.id.texthint);
                    hintTextView.setText("No hint was set");
                }
            }
        });


        View rectangleView = findViewById(R.id.rectangle);
        rectangleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = questionTextView.getText().toString();
                if (questionsList.contains(currentText)) {
                    String answer = getAnswerForQuestion(currentText);
                    questionTextView.setText(answer);
                } else {
                    // If current text is an answer, find the corresponding question
                    String originalQuestion = getQuestionForAnswer(currentText);
                    questionTextView.setText(originalQuestion);
                }
            }
        });

    }

    private String getQuestionForAnswer(String answer) {
        String question = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_QUESTION +
                " FROM " + DatabaseHelper.TABLE_CARDS +
                " WHERE " + DatabaseHelper.COLUMN_ANSWER + " = ?", new String[]{answer});

        if (cursor != null && cursor.moveToFirst()) {
            int questionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_QUESTION);
            if (questionIndex != -1) {
                question = cursor.getString(questionIndex);
            }
            cursor.close();
        }

        db.close();
        return (question != null) ? question : "Question not found";
    }

    private String getHintForQuestion(String question) {
        String hint = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_HINT +
                " FROM " + DatabaseHelper.TABLE_CARDS +
                " WHERE " + DatabaseHelper.COLUMN_QUESTION + " = ?", new String[]{question});

        if (cursor != null && cursor.moveToFirst()) {
            int hintIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_HINT);
            if (hintIndex != -1) {
                hint = cursor.getString(hintIndex);
            }
            cursor.close();
        }

        db.close();
        return (hint != null) ? hint : "";
    }

    private String getAnswerForQuestion(String question) {
        String answer = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_ANSWER +
                " FROM " + DatabaseHelper.TABLE_CARDS +
                " WHERE " + DatabaseHelper.COLUMN_QUESTION + " = ?", new String[]{question});

        if (cursor != null && cursor.moveToFirst()) {
            int answerIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ANSWER);
            if (answerIndex != -1) {
                answer = cursor.getString(answerIndex);
            }
            cursor.close();
        }

        db.close();

        // If answer is not found, return the original question
        return (answer != null) ? answer : question;
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
        if (questionsList.isEmpty()) {
            return "No questions available for this card set.";
        }

        if (shownQuestionsSet.size() == questionsList.size()) {
            shownQuestionsSet.clear();
        }

        Random random = new Random();
        String randomQuestion;
        do {
            randomQuestion = questionsList.get(random.nextInt(questionsList.size()));
        } while (shownQuestionsSet.contains(randomQuestion));

        shownQuestionsSet.add(randomQuestion);
        return randomQuestion;
    }
}