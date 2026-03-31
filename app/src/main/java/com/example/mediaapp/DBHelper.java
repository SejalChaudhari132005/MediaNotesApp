package com.example.mediaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NotesDB_12";
    private static final int DATABASE_VERSION = 2; // Incremented version
    private static final String TABLE_NAME = "notes_12";

    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_IMAGE_PATH = "image_path";
    private static final String COL_DATE = "date";
    private static final String COL_NOTE_TYPE = "note_type";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_IMAGE_PATH + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_NOTE_TYPE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertNote(String title, String description, String imagePath, String date, String noteType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_IMAGE_PATH, imagePath);
        values.put(COL_DATE, date);
        values.put(COL_NOTE_TYPE, noteType);

        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public List<NoteModel> getAllNotes() {
        List<NoteModel> notesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);
            if (cursor.moveToFirst()) {
                do {
                    NoteModel note = new NoteModel();
                    note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                    note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)));
                    note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)));
                    note.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_PATH)));
                    note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
                    note.setNoteType(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE_TYPE)));
                    notesList.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return notesList;
    }
}
