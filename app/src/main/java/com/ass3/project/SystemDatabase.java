package com.ass3.project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SystemDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "system";


    private static final int DB_VERSION = 1;
    public SystemDatabase(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + "Profile" + " ("
                + "id" + " TEXT, "
                + "username" + " TEXT, "
                + "about" + " TEXT, "
                + "dishes" + " NUMBER, "
                + "picture" + " TEXT)";

        sqLiteDatabase.execSQL(query);
//        query = "INSERT INTO Profile (userid) VALUES (0)";
//        sqLiteDatabase.execSQL(query);
    }

    public void AddData(String id,String user,String about,int Count,String image) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO Profile (id,username,about,dishes,picture) VALUES  ('"+ id +"','" + user + "','" + about + "','" + Count + "','" + image + "');";
//       String query = "UPDATE Profile SET username = '"+user+"', about = '"+about+"', dishes = '"+Count+"', picture = '"+image+"' WHERE id = '0'";
        db.execSQL(query);
        db.close();
    }


    public void UpdateData(String id,String user,String about) {

        SQLiteDatabase db = this.getWritableDatabase();
//      String query = "INSERT INTO Profile (id,username,about,dishes,picture) VALUES  ('"+ id +"','" + user + "','" + about + "','" + Count + "','" + image + "');";
        String query = "UPDATE Profile SET username = '"+user+"', about = '"+about+"' WHERE id = '"+id+"';";
        db.execSQL(query);
        db.close();
    }


    public void UpdateCount(String id,int count) {

        SQLiteDatabase db = this.getWritableDatabase();
//      String query = "INSERT INTO Profile (id,username,about,dishes,picture) VALUES  ('"+ id +"','" + user + "','" + about + "','" + Count + "','" + image + "');";
        String query = "UPDATE Profile SET dishes = '"+count+"'  WHERE id = '"+id+"';";
        db.execSQL(query);
        db.close();
    }





    public ArrayList<String> getData(String Id) {

        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM Profile WHERE id = '"+Id+"'", null);
        String username = "";
        String about = "";
        String dish = "";
        String picture = "";

        ArrayList<String> data = new ArrayList<String>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                data.add(cursor.getString(1));
                data.add(cursor.getString(2));
                data.add(cursor.getString(3));
                data.add(cursor.getString(4));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }

        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return data;
    }

//    public String getData() {
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // on below line we are creating a cursor with query to read data from database.
//        Cursor cursor = db.rawQuery("SELECT * FROM Profile WHERE id = 1", null);
//        String username = "";
//
//
//        // moving our cursor to first position.
//        if (cursor.moveToFirst()) {
//            do {
//                // on below line we are adding the data from cursor to our array list.
//                username = cursor.getString(1);
//            } while (cursor.moveToNext());
//            // moving our cursor to next.
//        }
//
//        // at last closing our cursor
//        // and returning our array list.
//        cursor.close();
//        return username;
//    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
