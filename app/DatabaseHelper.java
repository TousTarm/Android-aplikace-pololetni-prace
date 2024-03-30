import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "CardSets";

    // Table name
    private static final String TABLE_CARD_SETS = "card_sets";

    // Table columns
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CARD_SET_NAME = "card_set_name";

    // Create table SQL query
    private static final String CREATE_TABLE_CARD_SETS = "CREATE TABLE " + TABLE_CARD_SETS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CARD_SET_NAME + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_CARD_SETS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD_SETS);

        // Create tables again
        onCreate(db);
    }
}
