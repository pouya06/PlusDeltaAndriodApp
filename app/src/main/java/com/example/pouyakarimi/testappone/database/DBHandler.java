package com.example.pouyakarimi.testappone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pouyakarimi.testappone.objects.Note;
import com.example.pouyakarimi.testappone.objects.User;
import com.example.pouyakarimi.testappone.utils.DateUtil;

import java.util.ArrayList;

/**
 * Created by pouyakarimi on 10/11/15.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "plusdeltas.db";

    private static final String TABLE_NOTES = "notes";
    private static final String NOTE_COLUMN_ID = "id";
    private static final String NOTE_COLUMN_TEXT = "text";
    private static final String NOTE_COLUMN_ISPLUS = "isPlus";
    private static final String NOTE_COLUMN_CREATEDAT = "createdAt";
    private static final String NOTE_COLUMN_UPDATEDAT = "updatedAt";

    private static final String TABLE_USER = "user";
    private static final String USER_COLUMN_ID = "id";
    private static final String USER_COLUMN_NAME = "name";
    private static final String USER_COLUMN_EMAIL = "email";
    private static final String USER_COLUMN_ISPRIMARY = "isPrimary";


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryNote = "CREATE TABLE " + TABLE_NOTES + " ( " +
                NOTE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOTE_COLUMN_TEXT + " TEXT, " +
                NOTE_COLUMN_ISPLUS + " INTEGER, " +
                NOTE_COLUMN_CREATEDAT + " TEXT, " +
                NOTE_COLUMN_UPDATEDAT + " TEXT " +
                ");";
        db.execSQL(queryNote);

        String queryUser = "CREATE TABLE " + TABLE_USER + " ( " +
                USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_COLUMN_NAME + " TEXT, " +
                USER_COLUMN_EMAIL + " TEXT, " +
                USER_COLUMN_ISPRIMARY + " INTEGER " +
                ");";
        db.execSQL(queryUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addNewNoteRow(Note note) {
        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN_TEXT, note.getText());
        values.put(NOTE_COLUMN_ISPLUS, note.isItPlus() ? 1 : 0);
        values.put(NOTE_COLUMN_CREATEDAT, DateUtil.currentDate());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NOTES, null, values);
        db.close();

    }

    public void addNewUserRow(User user) {
        if (user.getName() != null && user.getEmail() != null) {
            ContentValues values = new ContentValues();
            values.put(USER_COLUMN_NAME, user.getName());
            values.put(USER_COLUMN_EMAIL, user.getEmail());
            values.put(USER_COLUMN_ISPRIMARY, user.isItPrimary() ? 1 : 0);
            SQLiteDatabase db = getWritableDatabase();
            db.insert(TABLE_USER, null, values);
            db.close();
        }
    }

    public void updateNoteRow(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NOTES +
                " SET " + NOTE_COLUMN_TEXT + " =\"" + note.getText() + "\" , " +
                NOTE_COLUMN_ISPLUS + " = " + (note.isItPlus() ? 1 : 0) +
                NOTE_COLUMN_UPDATEDAT + " =\"" + DateUtil.currentDate() + "\" " +
                " WHERE " + NOTE_COLUMN_ID + " =\"" + note.getId() + "\";");
        db.close();
    }

    public void updateUserRow(User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_USER +
                " SET " + USER_COLUMN_NAME + " =\"" + user.getName() + "\" , " +
                USER_COLUMN_EMAIL + " =\"" + user.getEmail() + "\" , " +
                USER_COLUMN_ISPRIMARY + " = " + (user.isItPrimary() ? 1 : 0) +
                " WHERE " + USER_COLUMN_ID + " =\"" + user.getId() + "\";");
        db.close();
    }

    public void deleteNoteRow(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTES +
                " Where " + NOTE_COLUMN_ID + " =\" " + note.getId() + "\";");
        db.close();
    }

    public void deleteUserRow(User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USER +
                " Where " + USER_COLUMN_ID + " =\" " + user.getId() + "\";");
        db.close();
    }

    public ArrayList<Note> notesArray(int isPlus) {
        ArrayList<Note> arrayNote = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "Select * from " + TABLE_NOTES + " where " + NOTE_COLUMN_ISPLUS + "=\"" + isPlus + "\";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex("text")) != null) {
                Note note = new Note();
                note.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex("id"))));
                note.setText(cursor.getString(cursor.getColumnIndex("text")));
                note.setIsItPlus(isPlus == 1 ? true : false);
                arrayNote.add(note);
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return arrayNote;
    }

    public User primaryUser() {
        User user = new User();

        SQLiteDatabase db = getWritableDatabase();
        String query = "Select * from " + TABLE_USER + " where " + USER_COLUMN_ISPRIMARY + "=1;";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        user.setName(cursor.getString(cursor.getColumnIndex("name")));
        user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
        user.setisItPrimary(Integer.valueOf(cursor.getColumnIndex("isPrimary")) == 1 ? true : false);

        cursor.close();
        db.close();
        return user;
    }

    public Boolean IsThereAPrimaryUser() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "Select * from " + TABLE_USER + " where " + USER_COLUMN_ISPRIMARY + "='1';";
        Cursor cursor = db.rawQuery(query, null);

        return cursor.getCount() != 0;

    }

    public ArrayList<User> listOfUsers() {
        ArrayList<User> userList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "Select * from " + TABLE_USER + " Order By " + USER_COLUMN_ISPRIMARY + " DESC ";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex("name")) != null
                    && cursor.getString(cursor.getColumnIndex("email")) != null) {
                User user = new User();
                user.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex("id"))));
                user.setName(cursor.getString(cursor.getColumnIndex("name")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                user.setisItPrimary(Integer.valueOf(cursor.getString(cursor.getColumnIndex("isPrimary"))) == 1 ? true : false);
                userList.add(user);
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return userList;
    }

}
