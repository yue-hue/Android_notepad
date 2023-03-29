package com.example.notepad_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {
    Context context;
    DBHelper databaseHelper;
    SQLiteDatabase db;

    public DBManager(Context context) {
        this.context = context;
        databaseHelper = new DBHelper(context);
    }

    public void openDB() {
        db = databaseHelper.getWritableDatabase();
    }

    public void insertDB(String note_name, String note_date, String note_text) {
        ContentValues cv = new ContentValues();

        cv.put(DBHelper.COLUMN_NAME, note_name);
        cv.put(DBHelper.COLUMN_DATE, note_date);
        cv.put(DBHelper.COLUMN_NOTE, note_text);

        db.insert(DBHelper.TABLE_NAME, null, cv);
    }

    public void updateDB(String note_name, String note_date, String note_text, int note_id) {
        ContentValues cv = new ContentValues();

        cv.put(DBHelper.COLUMN_NAME, note_name);
        cv.put(DBHelper.COLUMN_DATE, note_date);
        cv.put(DBHelper.COLUMN_NOTE, note_text);

        db.update(DBHelper.TABLE_NAME, cv, DBHelper.COLUMN_ID + " = '" + note_id + "'", null);
    }

    public void deleteDB(int note_id) {
        db.delete(DBHelper.TABLE_NAME,DBHelper.COLUMN_ID + " = '" + note_id + "'", null);
    }

    public ArrayList<Note> getAllDB() {
        ArrayList<Note> note_list = new ArrayList<Note>();

        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);//"strftime('%s', date_to_sort) ASC");

        while (cursor.moveToNext()) {
            int note_id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DATE));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NOTE));

            note_list.add(new Note(note_id, name, date, note));
        }

        cursor.close();

        return note_list;
    }

    public Note openNoteDB(int _id) {
        //Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME + " WHERE _id = '" + _id + "'", null);
        String selection = DBHelper.COLUMN_ID + " = '" + _id + "'";
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, selection, null ,null, null, null);

        cursor.moveToFirst();

        String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DATE));
        String note = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NOTE));

        Note note_struct = new Note(_id, name, date, note);

        cursor.close();

        return note_struct;
    }

    public ArrayList<Note> searchDB(String search_text) {
        ArrayList<Note> note_list = new ArrayList<Note>();

        String selection = DBHelper.COLUMN_NOTE + " LIKE '%" + search_text + "%' OR " + DBHelper.COLUMN_NAME + " LIKE '%" + search_text + "%'";

        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, selection, null ,null, null, null);

        while (cursor.moveToNext()) {
            int note_id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DATE));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NOTE));

            note_list.add(new Note(note_id, name, date, note));
        }

        cursor.close();

        return note_list;
    }

    public void closeDB() {
        databaseHelper.close();
    }
}
