package com.hanyu.zhihuluanbao.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hanyu.zhihuluanbao.database.MyDatabaseHelper;

/**
 * Created by Dell on 2016/11/18.
 */
public class MyDatabase {
    public static final String NAME = "stories";
    public static final int VERSION = 1;
    private SQLiteDatabase db;
    private static MyDatabase myDatabase;


    private MyDatabase(Context context){
        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(context,NAME,null,VERSION);
        db = myDatabaseHelper.getWritableDatabase();
    }

    public static MyDatabase getInstance(Context context){
        if(myDatabase == null){
            synchronized (MyDatabase.class){
                if (myDatabase == null){
                    myDatabase = new MyDatabase(context);
                }
            }

        }
        return myDatabase;
    }

    public void saveStories(StoryModel storyModel){
        if(storyModel != null) {
            ContentValues values = new ContentValues();
            values.put("id", storyModel.id);
            values.put("images", String.valueOf(storyModel.images));
            values.put("title", storyModel.title);
            db.insert("Stories", null, values);
        }

    }
}
