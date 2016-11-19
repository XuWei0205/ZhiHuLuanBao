package com.hanyu.zhihuluanbao.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hanyu.zhihuluanbao.utils.CLog;

/**
 * Created by Dell on 2016/11/18.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    public static final String STORIES ="create table Stories("
    + " id integer primary key autoincrement"
            + "images text"
            +"title text"
            + "type integer"
            + "ga_prefix text)";

    public static final String NEWS ="create table News("
            +"id integer primary key autoincrement"
            +"body text"
            +"image_resource text"
            + "title text"
            + "image text"
            + "share_url text"
            + "js text"
            + "ga_prefix text"
            + "images text"
            + "type integer"
            + "css text)";
    public static final String TOPSTORIES = "create table TopStories(" +
            "id integer primary key autoincrement" +
            "image text" +
            "type integer" +
            "ga_prefix text" +
            "title text)";

    public MyDatabaseHelper(Context context ,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STORIES);
        db.execSQL(NEWS);
        db.execSQL(TOPSTORIES);
        CLog.i("db create succeed");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
