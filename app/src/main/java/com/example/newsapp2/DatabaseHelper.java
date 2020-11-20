package com.example.newsapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.net.URI;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Savepage.db";
    public static final String TABLE_NAME="pages_table";
    public static final String COL_0="ID";
    public static final String COL_1="Title";
    public static final String COL_2="url";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("+COL_1+" TEXT PRIMARY KEY,"+COL_2+" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME+";");//DROP TABLE IF EXISTS TABLE_NAME;
        onCreate(db);
    }
    public boolean insertData(String title, String url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();

        contentValues.put(COL_1,title);//put editText values in the database columns
        contentValues.put(COL_2,url);
        long result = db.insert(TABLE_NAME, null,contentValues);
        if (result==-1)
            return false;
        else {
            Log.d("Success", "TRUE");
            return true;
        }
    }
}
