package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by timohtey on 13/10/2016.
 *
 * This class creates the entity tables.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database
    private SQLiteDatabase db;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "RemindMe";

    // Table for To Do Entries
    public static final String TO_DO_ENTRY_TABLE_NAME = "TO_DO_ENTRY";
    public static final String TO_DO_ENTRY_ID = "TO_DO_ENTRY_ID";
    public static final String TO_DO_ENTRY_NAME = "TO_DO_ENTRY_NAME";
    public static final String TO_DO_ENTRY_STATE = "TO_DO_ENTRY_STATE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        db = sqLiteDatabase;

        // Create Tables
        createToDoEntryTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Steps to upgrade the database for the new version ...
    }

    /**
     * This method creates the To Do Entry Table.
     */
    private void createToDoEntryTable(){
        StringBuilder createToDoEntryTable = new StringBuilder();

        createToDoEntryTable.append("CREATE TABLE IF NOT EXISTS " + TO_DO_ENTRY_TABLE_NAME + "(");
        createToDoEntryTable.append(    TO_DO_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
        createToDoEntryTable.append(    TO_DO_ENTRY_NAME + " TEXT, ");
        createToDoEntryTable.append(    TO_DO_ENTRY_STATE + " INTEGER NOT NULL");
        createToDoEntryTable.append(")");

        db.execSQL(createToDoEntryTable.toString());
    }
}
