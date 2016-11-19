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
            values.put("id", storyModel.id);
            values.put("type",storyModel.type);
            values.put("ga_prefix",storyModel.ga_prefix);
            values.put("images",toString(storyModel.images));
            //values.put("images", String.valueOf(storyModel.images));
            values.put("title", storyModel.title);
            db.insert("Stories", null, values);
        }

    }

    public void saveNews(NewsModel newsModel){
        if (newsModel != null){
            ContentValues values = new ContentValues();
            values.put("id",newsModel.id);
            values.put("body",newsModel.body);
            values.put("ga_prefix",newsModel.ga_prefix);
            values.put("image",newsModel.image);
            values.put("image_source",newsModel.image_source);
            values.put("share_url",newsModel.share_url);
            values.put("title",newsModel.title);
            values.put("type",newsModel.type);
            values.put("images",toString(newsModel.images));
            values.put("css",toString(newsModel.css));
            values.put("js",toString(newsModel.js));
            //values.put("",newsModel.css);
            //values.put("",newsModel.images);
            //values.put("",newsModel.js);
            db.insert("News",null,values);
        }
    }

    public void saveTopStories(TopStoryModel topStoryModel){
        if (topStoryModel != null) {
            ContentValues values = new ContentValues();
            values.put("id",topStoryModel.id);
            values.put("ga_prefix",topStoryModel.ga_prefix);
            values.put("image",topStoryModel.image);
            values.put("title",topStoryModel.title);
            values.put("type", topStoryModel.type);

        }
    }

    public List<StoryModel> loadStory() {
        List<StoryModel> stories = new ArrayList<>();
        Cursor cursor =
                db.query("Stories", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                StoryModel storyModel = new StoryModel();
                storyModel.images = toArrayList(cursor.getString(cursor.getColumnIndex("images")));
                storyModel.ga_prefix = cursor.getString(cursor.getColumnIndex("ga_prefix"));
                storyModel.id = cursor.getLong(cursor.getColumnIndex("id"));
                storyModel.title = cursor.getString(cursor.getColumnIndex("title"));
                storyModel.type = cursor.getInt(cursor.getColumnIndex("type"));
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
            do {
                NewsModel newsModel = new NewsModel();
                newsModel.body = cursor.getString(cursor.getColumnIndex("body"));
                newsModel.ga_prefix = cursor.getString(cursor.getColumnIndex("ga_prefix"));
                newsModel.css = toArrayList(cursor.getString(cursor.getColumnIndex("css")));
                newsModel.id = cursor.getLong(cursor.getColumnIndex("id"));
                newsModel.image = cursor.getString(cursor.getColumnIndex("image"));
                newsModel.image_source = cursor.getString(cursor.getColumnIndex("image_source"));
                newsModel.share_url = cursor.getString(cursor.getColumnIndex("share_url"));
                newsModel.title = cursor.getString(cursor.getColumnIndex("title"));
                newsModel.type = cursor.getInt(cursor.getColumnIndex("type"));
                newsModel.images = toArrayList(cursor.getString(cursor.getColumnIndex("images")));
                newsModel.js = toArrayList(cursor.getString(cursor.getColumnIndex("js")));
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
            do {
                TopStoryModel topStoryModel = new TopStoryModel();
                topStoryModel.ga_prefix = cursor.getString(cursor.getColumnIndex("ga_prefix"));
                topStoryModel.id = cursor.getLong(cursor.getColumnIndex("id"));
                topStoryModel.image = cursor.getString(cursor.getColumnIndex("image"));
                topStoryModel.title = cursor.getString(cursor.getColumnIndex("title"));
                topStoryModel.type = cursor.getInt(cursor.getColumnIndex("type"));
                topStories.add(topStoryModel);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return topStories;
    }
    private String toString(ArrayList<String> string){
        Gson gson = new Gson();
         gson.toJson(string);
        return gson.toJson(string);

    }
    private ArrayList<String> toArrayList(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, new TypeToken<String>() {
        }.getType());

    }
}
