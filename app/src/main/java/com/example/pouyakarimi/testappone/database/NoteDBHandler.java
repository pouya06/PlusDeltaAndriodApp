package com.example.pouyakarimi.testappone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pouyakarimi.testappone.objects.Note;

/**
 * Created by pouyakarimi on 10/11/15.
 */
public class NoteDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "plusdeltas.db";
    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TEXT = "_text";
    private static final String COLUMN_ISPLUS = "_isPlus";

    public NoteDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NOTES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
                COLUMN_TEXT + " TEXT " +
                COLUMN_ISPLUS + " INTEGER " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public void addNewRow(Note note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, note.getText());
        values.put(COLUMN_ISPLUS, note.isItPlus() ? 1 : 0);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NOTES, null, values);
        db.close();

    }

    public void deleteRow(String noteText) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTES +
                " Where " + COLUMN_TEXT + " =\" " + noteText + "\";");
    }

}
