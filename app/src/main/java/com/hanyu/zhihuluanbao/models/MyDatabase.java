package com.hanyu.zhihuluanbao.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hanyu.zhihuluanbao.database.MyDatabaseHelper;
import com.hanyu.zhihuluanbao.utils.CLog;
import com.hanyu.zhihuluanbao.utils.Time;

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

    public void saveStories(StoryModel storyModel,String date){
        db.beginTransaction();
        try {
            if (storyModel != null) {
                ContentValues values = new ContentValues();
                Gson gson = new Gson();
                String data = gson.toJson(storyModel);
                values.put("data", data);
                values.put("story_id", storyModel.id);
                values.put("date",date);
                db.insertWithOnConflict("Stories", "story_id", values, SQLiteDatabase.CONFLICT_REPLACE);
                db.setTransactionSuccessful();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    public void saveNews(NewsModel newsModel,String date){
        db.beginTransaction();
        try {
            if (newsModel != null) {
                ContentValues values = new ContentValues();
                Gson gson = new Gson();
                String data = gson.toJson(newsModel);
                values.put("data", data);
                values.put("date",date);
                values.put("news_id", newsModel.id);
                db.insertWithOnConflict("News", "news_id", values, SQLiteDatabase.CONFLICT_REPLACE);
                db.setTransactionSuccessful();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    public void saveTopStories(TopStoryModel topStoryModel,String date){
        db.beginTransaction();
        try {
            if (topStoryModel != null) {
                ContentValues values = new ContentValues();
                Gson gson = new Gson();
                String data = gson.toJson(topStoryModel);
                values.put("data", data);
                values.put("date",date);
                values.put("topStories_id", topStoryModel.id);
                db.insertWithOnConflict("TopStories", "topStory_id", values, SQLiteDatabase.CONFLICT_REPLACE);
                db.setTransactionSuccessful();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.endTransaction();
        }
    }

    public List<StoryModel> loadStory(String date) {
        List<StoryModel> stories = new ArrayList<>();
        db.beginTransaction();
        try {
            CLog.i("date ______________" + date);
            Cursor cursor =
                    db.query("Stories", null, "date = ?", new String[]{String.valueOf(date)}, null, null, null);
            if (cursor.moveToFirst()) {
                StoryModel storyModel;
                do {
                    Gson gson = new Gson();
                    storyModel = gson.fromJson(cursor.getString(cursor.getColumnIndex("data")), StoryModel.class);
                    stories.add(storyModel);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
        return stories;

    }

    public NewsModel loadNews (long id){
        NewsModel newsModel = new NewsModel();
        Cursor cursor = db.query("News",null,"news_id = ?",new String[]{String.valueOf(id)},null,null,null);

        if (cursor.moveToFirst()){

            do {
                Gson gson = new Gson();
                newsModel = gson.fromJson(cursor.getString(cursor.getColumnIndex("data")),NewsModel.class);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return newsModel;
    }

    public List<TopStoryModel> loadTopStory(String date){
        List<TopStoryModel> topStories = new ArrayList<>();
        db.beginTransaction();
        try {

            Cursor cursor = db.query("TopStories", null, "date = ?", new String[]{String.valueOf(date)}, null, null, null, null);
            if (cursor.moveToFirst()) {
                TopStoryModel topStoryModel;
                do {
                    Gson gson = new Gson();
                    topStoryModel = gson.fromJson(cursor.getString(cursor.getColumnIndex("data")), TopStoryModel.class);
                    topStories.add(topStoryModel);
                } while (cursor.moveToNext());
            }
            cursor.close();

            db.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();

        }
        return topStories;
    }

    public void deleteData(){
        Time time = new Time();
        String date =  time.getDate(-2);
        String date2 = time.getDate(-1);
        db.beginTransaction();
        try {
            db.delete("Stories", "date < ?", new String[]{date});
            db.delete("TopStories","date < ?",new String[]{date2});
            db.delete("News","date < ?",new String[]{date});
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

}
