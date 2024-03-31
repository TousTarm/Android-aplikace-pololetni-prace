package com.example.pololetniprace3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 3;

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
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD_SETS);

        // Create tables again
        onCreate(db);
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
}
