package com.josevi.gastos.db;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "josevi.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_SHIPPINGS_TABLE = " CREATE TABLE " + DBContract.ShippingsEntry.TABLE_NAME + " (" +
                DBContract.ShippingsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.ShippingsEntry.COLUMN_ID + " TEXT NOT NULL, " +
                DBContract.ShippingsEntry.COLUMN_STORE + " INTEGER NOT NULL, " +
                DBContract.ShippingsEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                DBContract.ShippingsEntry.COLUMN_SHIPPING + " TEXT NOT NULL, " +
                DBContract.ShippingsEntry.COLUMN_OTHERS + " FLOAT );";

        sqLiteDatabase.execSQL(SQL_CREATE_SHIPPINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBContract.ShippingsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
