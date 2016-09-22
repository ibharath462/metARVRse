package com.example.bharath.metarvrse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Bharath on 9/22/2016.
 */
public class data extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Books";
    private static final int DATABASE_VERSION = 1;

    public data(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String books = "books";

    private static final String name = "name";
    private static final String author = "author";
    private static final String isbn = "isbn";
    private static final String image = "image";
    private static final String id = "id";

    private static final String CREATE_books="CREATE TABLE "+books+"("+id+" INTEGER PRIMARY KEY AUTOINCREMENT, "+name+" TEXT, "+author+" TEXT, "+isbn+" TEXT, "+image+")";


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_books);
        Log.d("DB", "DB created..");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + books);
        onCreate(db);

    }

    public void add(String n,String a,String i,String u){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(name,n);
        cv.put(author,a);
        cv.put(isbn,i);
        cv.put(image,u);

        db.insert(books, null, cv);
        Log.d("Inserted:", "DB values inserted..");
        db.close();
    }

    public void delete(String t){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM books" + " WHERE isbn" + "='" + t + "'");
        Log.d("db:","Deleted value "+t);
        db.close();
    }

    public ArrayList<ArrayList<String>> get(){


        String selectQuery = "SELECT  * FROM " + books;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        final ArrayList<ArrayList<String>> list=new ArrayList<ArrayList<String>>();
        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> t=new ArrayList<String>();
                t.add(cursor.getString(1));
                t.add(cursor.getString(2));
                t.add(cursor.getString(3));
                t.add(cursor.getString(4));
                list.add(t);

            } while (cursor.moveToNext());
        }
        return  list;
    }

    boolean searchDuplicate(String t){
        String selectQuery = "SELECT  * FROM " + books;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getString(3).equals(t)){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        return false;
    }
}
