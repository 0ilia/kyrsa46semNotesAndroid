package com.example.kursa4new;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "notes.db";

    public static final String TABLE_NAME = "notes";
    public static final String VIEW = "VIEW_notes";

    public  static final String _ID = "id";
    public static final String _THEME = "theme";
    public static final String _MESSAGE = "message";
    public static final String _UNIX_TIMECreate = "unix_timeCreate";
    public static final String _UNIX_TIMEUpdate = "unix_timeUpdate";


    public DBHelper(@Nullable Context context/*, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version*/) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table  "+TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY autoincrement, "
                + _THEME + " text,"
                + _MESSAGE + " text,"
                + _UNIX_TIMECreate + " text,"
                + _UNIX_TIMEUpdate + " text )");
        //CREATE VIEW add_note_view AS SELECT * FROM notes
        //db.execSQL("create VIEW  "+ VIEW + "as SELECT * FROM notes ");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table  if exists " + TABLE_NAME);
        onCreate(db);
    }
}
