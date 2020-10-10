package com.purwoto.githubusersubmission.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.purwoto.githubusersubmission.db.DatabaseContract.UserColumns.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbfavorit";
    private static int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_FAVORIT = String.format("CREATE TABLE %s" +
            " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL)",
            TABLE_NAME,
            DatabaseContract.UserColumns._ID,
            DatabaseContract.UserColumns.NAMA,
            DatabaseContract.UserColumns.AVATAR_URL,
            DatabaseContract.UserColumns.URL);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String avatar_url, String url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.UserColumns.NAMA, name);
        contentValues.put(DatabaseContract.UserColumns.AVATAR_URL,avatar_url);
        contentValues.put(DatabaseContract.UserColumns.URL, url);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public int deleteData(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "nama='" + name + "'", null);
    }

    public boolean checkFavorit(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        Boolean s = false;
        while(res.moveToNext()){
            if(res.getString(1).equals(name)){
                s = true;
                break;
            }
        }
        return s;
    }
}
