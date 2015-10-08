package com.android_dev.tatenuufrn.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adelinosegundo on 10/8/15.
 */
public class EventDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tatenufrn";
    public static final String EVENTS_TABLE_NAME = "events";

    private static final String EVENTS_TABLE_CREATE =
            "CREATE TABLE " + EVENTS_TABLE_NAME + " (" +
                    "id INTEGER NOT NULL PRIMARY KEY autoincrement, " +
                    "title TEXT," +
                    "description TEXT," +
                    "image TEXT," +
                    "startTime TEXT," +
                    "endTime TEXT," +
                    "address TEXT," +
                    "radiusTrigger INTEGER," +
                    "fbEventId INTEGER," +
                    "rating REAL);";

    public EventDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EVENTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE_NAME);
        onCreate(db);
    }
}
