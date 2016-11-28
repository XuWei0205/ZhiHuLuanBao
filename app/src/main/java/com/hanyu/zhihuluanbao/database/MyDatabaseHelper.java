package com.hanyu.zhihuluanbao.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hanyu.zhihuluanbao.utils.CLog;

/**
 * Created by Dell on 2016/11/18.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String STORIES ="create table Stories("
    + " id integer primary key autoincrement,"
            + "story_id integer UNIQUE,"
            + "date integer,"
            + "data text)";

    public static final String NEWS ="create table News("
            +"id integer primary key autoincrement,"
            +"news_id integer UNIQUE,"
            +"date integer,"
            +"data text)";

    public static final String TOPSTORIES = "create table TopStories("
            + "id integer primary key autoincrement,"
            + "topStories_id integer UNIQUE,"
            + "date integer,"
            + "data text)";

    public MyDatabaseHelper(Context context ,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);

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
        switch (oldVersion){
            case 1:

            default:
        }
    }
}
