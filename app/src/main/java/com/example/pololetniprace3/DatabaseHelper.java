package com.example.pololetniprace3;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database version
    private static final int DATABASE_VERSION = 4;

    // Database name
    private static final String DATABASE_NAME = "CardSets";

    // Table names
    public static final String TABLE_CARD_SETS = "card_sets";
    public static final String TABLE_CARDS = "cards";

    // Common column names
    public static final String COLUMN_ID = "_id";

    // Card Sets table column names
    public static final String COLUMN_CARD_SET_NAME = "card_set_name";

    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_HINT = "hint";
    public static final String COLUMN_ANSWER = "answer";
    public static final String COLUMN_SET_ID = "set_id";

    // Create table SQL queries
    public static final String CREATE_TABLE_CARD_SETS = "CREATE TABLE " + TABLE_CARD_SETS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CARD_SET_NAME + " TEXT" + ")";

    public static final String CREATE_TABLE_CARDS = "CREATE TABLE " + TABLE_CARDS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_QUESTION + " TEXT, " +
            COLUMN_ANSWER + " TEXT, " +
            COLUMN_HINT + " TEXT, " + // Add the new column here
            COLUMN_SET_ID + " INTEGER, " +
            "FOREIGN KEY(" + COLUMN_SET_ID + ") REFERENCES " + TABLE_CARD_SETS + "(" + COLUMN_ID + ")" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_CARD_SETS);
        db.execSQL(CREATE_TABLE_CARDS);
    }

    public static String getColumnQuestion() {
        return COLUMN_QUESTION;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // Drop older tables if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD_SETS);

            // Create tables again
            onCreate(db);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error upgrading database: " + e.getMessage());
        }
    }


    public List<String> getAllCardSets() {
        List<String> cardSets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_CARD_SETS, new String[]{COLUMN_ID, COLUMN_CARD_SET_NAME}, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int cardSetNameIndex = cursor.getColumnIndex(COLUMN_CARD_SET_NAME);
                do {
                    String cardSetName = cursor.getString(cardSetNameIndex);
                    cardSets.add(cardSetName);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching card sets: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return cardSets;
    }

    // Method to retrieve card data by ID
    @SuppressLint("Range")
    public String[] getCardDataById(int cardId) {
        String[] cardData = null; // Initialize to null
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_CARDS, new String[]{COLUMN_QUESTION, COLUMN_HINT, COLUMN_ANSWER}, COLUMN_ID + "=?",
                    new String[]{String.valueOf(cardId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                // Create the array if a valid card is found
                cardData = new String[3];
                cardData[0] = cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION));
                cardData[1] = cursor.getString(cursor.getColumnIndex(COLUMN_HINT));
                cardData[2] = cursor.getString(cursor.getColumnIndex(COLUMN_ANSWER));
                Log.d("DatabaseHelper", "Retrieved data for card ID " + cardId);
                // Log the retrieved data
                Log.d("DatabaseHelper", "Question: " + cardData[0]);
                Log.d("DatabaseHelper", "Hint: " + cardData[1]);
                Log.d("DatabaseHelper", "Answer: " + cardData[2]);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching card data by ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return cardData;
    }


    public void updateCardValues(int cardId, String question, String hint, String answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, question);
        values.put(COLUMN_HINT, hint);
        values.put(COLUMN_ANSWER, answer);
        db.update(TABLE_CARDS, values, COLUMN_ID + "=?", new String[]{String.valueOf(cardId)});
        db.close();
    }

    public void deleteCard(int cardId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_CARDS, COLUMN_ID + " = ?", new String[]{String.valueOf(cardId)});
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error deleting card: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public int getSmallestCardId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int smallestId = -1;

        try {
            cursor = db.rawQuery("SELECT MIN(" + COLUMN_ID + ") FROM " + TABLE_CARDS, null);
            if (cursor != null && cursor.moveToFirst()) {
                smallestId = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching smallest card ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return smallestId;
    }

    public void insertCard(String cardSetName, String question, String hint, String answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            // Get the ID of the card set
            int setId = getCardSetIdByName(cardSetName);

            // Insert the new card
            values.put(COLUMN_QUESTION, question);
            values.put(COLUMN_HINT, hint);
            values.put(COLUMN_ANSWER, answer);
            values.put(COLUMN_SET_ID, setId);
            db.insert(TABLE_CARDS, null, values);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting card: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    @SuppressLint("Range")
    public int getCardSetIdByName(String cardSetName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int setId = -1;
        try {
            cursor = db.query(TABLE_CARD_SETS, new String[]{COLUMN_ID}, COLUMN_CARD_SET_NAME + "=?",
                    new String[]{cardSetName}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                setId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching card set ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return setId;
    }
}