package com.hanyu.zhihuluanbao.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.hanyu.zhihuluanbao.commons.API;
import com.hanyu.zhihuluanbao.managers.NetManager;
import com.hanyu.zhihuluanbao.models.MyDatabase;
import com.hanyu.zhihuluanbao.models.NewsListModel;
import com.hanyu.zhihuluanbao.models.NewsModel;
import com.hanyu.zhihuluanbao.models.StoryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2016/11/18.
 */
public class DownloadService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loadNewsList();
        loadNews();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**开启网络请求加载NewsList**/
    private void loadNewsList(){
        NetManager.doHttpGet(getApplicationContext(), null, API.GET_LATEST_NEWS_URL, null, NewsListModel.class, new NetManager.ResponseListener<NewsListModel>() {
            @Override
            public void onResponse(NewsListModel response) {

            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onAsyncResponse(NewsListModel response) {
                MyDatabase myDatabase = MyDatabase.getInstance(getApplicationContext());
                if (response.stories != null && response.stories.size() > 0) {
                    for (int i = 0; i < response.stories.size(); i++) {
                        myDatabase.saveStories(response.stories.get(i));
                    }
                }
                if (response.top_stories != null && response.top_stories.size() > 0) {
                    for (int i = 0; i < response.top_stories.size(); i++) {
                        myDatabase.saveTopStories(response.top_stories.get(i));
                    }
                }

            }
        });
    }

    /**开启网络请求加载News**/
    private void loadNews(){
        List<StoryModel> storyModelList ;
        final MyDatabase myDatabase =MyDatabase.getInstance(getApplicationContext());
        storyModelList = myDatabase.loadStory();
        for(int i = 0; i < storyModelList.size(); i++){
            StoryModel storyModel = storyModelList.get(i);
            Long url= storyModel.id;
            NetManager.doHttpGet(getApplicationContext(), null, API.BASE_GET_NEWS_URL + url, null, NewsModel.class, new NetManager.ResponseListener<NewsModel>() {
                @Override
                public void onResponse(NewsModel response) {

                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }

                @Override
                public void onAsyncResponse(NewsModel response) {
                    myDatabase.saveNews(response);
                }
            });
        }


    }

}
