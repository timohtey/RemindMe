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

    public void addToDoEntry(ToDoEntry toDoEntry){
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.TO_DO_ENTRY_ID, toDoEntry.getToDoEntryId());
        contentValues.put(DatabaseHelper.TO_DO_ENTRY_NAME, toDoEntry.getToDoEntryName());
        contentValues.put(DatabaseHelper.TO_DO_ENTRY_STATE, toDoEntry.getToDoEntryState());

        database.insert(DatabaseHelper.TO_DO_ENTRY_TABLE_NAME, null, contentValues);
    }

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
}
