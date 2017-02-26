package com.ifalot.sfie.util;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

@SuppressWarnings("SpellCheckingInspection")
public class Database {

    private static final String DBName = "SFIEDB";
    private static SQLiteDatabase db = null;

    public static void initDatabase(Context context){
        db = context.openOrCreateDatabase(DBName, Context.MODE_PRIVATE, null);
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS calendar (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "date BIGINT NOT NULL );");
            db.execSQL("CREATE TABLE IF NOT EXISTS meal (" +
                    "calendarid INTEGER NOT NULL, " +
                    "foodid INTEGER NOT NULL, " +
                    "PRIMARY KEY ('calendarid', 'foodid') );");
            db.execSQL("CREATE TABLE IF NOT EXISTS food (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "ingredients VARCHAR(1024) NOT NULL );");
            db.execSQL("CREATE TABLE IF NOT EXISTS fridge (" +
                    "name VARCHAR(255) PRIMARY KEY, " +
                    "quantity REAL NOT NULL, " +
                    "theend BIGINT DEFAULT -1 );");
            Log.d("Database", "Tables successfully created");
        } catch (SQLException e){
            Log.d("Database", "Failed to init database: " + e);
        }
    }

    public static SQLiteDatabase getDB(){
        return db;
    }

}
