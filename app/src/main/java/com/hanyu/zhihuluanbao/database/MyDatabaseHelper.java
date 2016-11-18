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
            + "image text"
            +"title text)";

    public MyDatabaseHelper(Context context ,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STORIES);
        CLog.i("db create succeed");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
