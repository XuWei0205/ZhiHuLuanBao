package com.hanyu.zhihuluanbao.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hanyu.zhihuluanbao.database.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

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
            Gson gson = new Gson ();
            String data= gson.toJson(storyModel);
            values.put("data",data);
            db.insert("Stories", null, values);
        }

    }

    public void saveNews(NewsModel newsModel){
        if (newsModel != null){
           ContentValues values = new ContentValues();
            Gson gson = new Gson();
            String data = gson.toJson(newsModel);
            values.put("data",data);
            db.insert("News",null,values);
        }
    }

    public void saveTopStories(TopStoryModel topStoryModel){
        if (topStoryModel != null) {
            ContentValues values = new ContentValues();
            Gson gson = new Gson();
            String data = gson.toJson(topStoryModel);
            values.put("data",data);
            db.insert("TopStories",null,values);

        }
    }

    public List<StoryModel> loadStory() {
        List<StoryModel> stories = new ArrayList<>();
        Cursor cursor =
                db.query("Stories", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            StoryModel storyModel ;
            do {
                Gson gson = new Gson();
                storyModel = gson.fromJson(cursor.getString(cursor.getColumnIndex("data")),StoryModel.class);
                stories.add(storyModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return stories;
    }

    public List<NewsModel> loadNews(){
        List<NewsModel> newsModelList = new ArrayList<>();
        Cursor cursor = db.query("News",null,null,null,null,null,null);

        if (cursor.moveToFirst()){
            NewsModel newsModel;
            do {
                Gson gson = new Gson();
                newsModel = gson.fromJson(cursor.getString(cursor.getColumnIndex("data")),NewsModel.class);
                newsModelList.add(newsModel);

            }while(cursor.moveToNext());
        }
        cursor.close();
        return newsModelList;
    }

    public List<TopStoryModel> loadTopStory(){
        List<TopStoryModel> topStories = new ArrayList<>();
        Cursor cursor = db.query("TopStories",null,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            TopStoryModel topStoryModel;
            do {
                Gson gson = new Gson();
                topStoryModel = gson.fromJson(cursor.getString(cursor.getColumnIndex("data")),TopStoryModel.class);
                topStories.add(topStoryModel);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return topStories;
    }

}
