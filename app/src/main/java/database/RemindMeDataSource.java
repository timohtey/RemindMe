package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import model.ToDoEntry;

/**
 * Created by timohtey on 13/10/2016.
 */
public class RemindMeDataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context con;

    public RemindMeDataSource(Context context){
        con = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public SQLiteDatabase reopen(){
        DatabaseHelper dbOpenHelper = new DatabaseHelper(this.con);
        database = dbOpenHelper.getWritableDatabase();

        return database;
    }

    /**
     * This methods adds a to do entry into the database.
     * @param toDoEntry To Do Entry
     */
    public void addToDoEntry(ToDoEntry toDoEntry){
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.TO_DO_ENTRY_ID, toDoEntry.getToDoEntryId());
        contentValues.put(DatabaseHelper.TO_DO_ENTRY_NAME, toDoEntry.getToDoEntryName());
        contentValues.put(DatabaseHelper.TO_DO_ENTRY_STATE, toDoEntry.getToDoEntryState());

        database.insert(DatabaseHelper.TO_DO_ENTRY_TABLE_NAME, null, contentValues);
    }

    /**
     * This methods retrieves all of the Done To Do Entries.
     * @return List of Done To Do Entries.
     */
    public ArrayList<ToDoEntry> retrieveAllToDoEntries(){
        String retrieveAllToDoEntriesQuery = "SELECT * FROM " + DatabaseHelper.TO_DO_ENTRY_TABLE_NAME;
        ArrayList<ToDoEntry> toDoEntries = new ArrayList<>();
        Cursor cursor = database.rawQuery(retrieveAllToDoEntriesQuery, null);

        try {
            while (cursor.moveToNext()) {
                ToDoEntry toDoEntry = new ToDoEntry(cursor.getString(1), cursor.getInt(2));
                toDoEntry.setToDoEntryId(cursor.getInt(0));
                toDoEntries.add(toDoEntry);
            }
        } finally {
            cursor.close();
        }

        return toDoEntries;
    }

    /**
     * This method retrieves all of the Pending To Do Entries.
     * @return List of Pending To Do Entries
     */
    public ArrayList<ToDoEntry> retrievePendingToDoEntries(){
        ArrayList<ToDoEntry> toDoEntries = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TO_DO_ENTRY_TABLE_NAME, null, DatabaseHelper.TO_DO_ENTRY_STATE + " = " + 0 + "",
                null, null, null, null);

        try {
            while (cursor.moveToNext()) {
                ToDoEntry toDoEntry = new ToDoEntry(cursor.getString(1), cursor.getInt(2));
                toDoEntry.setToDoEntryId(cursor.getInt(0));
                toDoEntries.add(toDoEntry);
            }
        } finally {
            cursor.close();
        }

        return toDoEntries;
    }

    /**
     * This methods retrieves all of the Done To Do Entries.
     * @return List of Done To Do Entries.
     */
    public ArrayList<ToDoEntry> retrieveDoneToDoEntries(){
        ArrayList<ToDoEntry> toDoEntries = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TO_DO_ENTRY_TABLE_NAME, null, DatabaseHelper.TO_DO_ENTRY_STATE + " = " + 1 + "",
                null, null, null, null);

        try {
            while (cursor.moveToNext()) {
                ToDoEntry toDoEntry = new ToDoEntry(cursor.getString(1), cursor.getInt(2));
                toDoEntry.setToDoEntryId(cursor.getInt(0));
                toDoEntries.add(toDoEntry);
            }
        } finally {
            cursor.close();
        }

        return toDoEntries;
    }

    /**
     * This method switches the state of the To Do Entry.
     */
    public void switchToDoEntryState(ToDoEntry toDoEntry){
        if(toDoEntry.getToDoEntryState() == 0){
            toDoEntry.setToDoEntryState(1);
        } else {
            toDoEntry.setToDoEntryState(0);
        }

        database.execSQL("UPDATE " + DatabaseHelper.TO_DO_ENTRY_TABLE_NAME
                + " SET " + DatabaseHelper.TO_DO_ENTRY_STATE + " = " + toDoEntry.getToDoEntryState()
                + " WHERE " + DatabaseHelper.TO_DO_ENTRY_ID + " = " + toDoEntry.getToDoEntryId() + ";");
    }

    /**
     * This methods retrieves the maximum id foudn in the database.
     * @return Maximum Id
     */
    public int getMaxId(){
        String retrieveAllToDoEntriesQuery = "SELECT MAX(" + DatabaseHelper.TO_DO_ENTRY_ID + ") FROM "
                + DatabaseHelper.TO_DO_ENTRY_TABLE_NAME;

        Cursor cursor = database.rawQuery(retrieveAllToDoEntriesQuery, null);
        cursor.moveToNext();

        return cursor.getInt(0);
    }
}
