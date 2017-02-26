package com.hanyu.zhihuluanbao.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.VolleyError;
import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.activities.NewsListFraActivity;
import com.hanyu.zhihuluanbao.commons.API;
import com.hanyu.zhihuluanbao.managers.NetManager;
import com.hanyu.zhihuluanbao.models.MyDatabase;
import com.hanyu.zhihuluanbao.models.NewsListModel;
import com.hanyu.zhihuluanbao.models.NewsModel;
import com.hanyu.zhihuluanbao.models.StoryModel;
import com.hanyu.zhihuluanbao.utils.CLog;
import com.hanyu.zhihuluanbao.utils.Time;

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
        Intent intent = new Intent(getBaseContext(),NewsListFraActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, intent,0);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("知乎乱报")
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentText("正在下载")
                .setAutoCancel(true)
                .setTicker("正在离线下载")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(1,notification);
        manager.cancel(1);
        CLog.i(getApplicationContext(),"create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CLog.i(getApplicationContext(),"startCommand");
        loadNewsList();

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
                    long size = response.stories.size();
                    for (int i = 0; i < size; i++) {
                        myDatabase.saveStories(response.stories.get(i),response.date);
                    }
                }
                loadNews();
                if (response.top_stories != null && response.top_stories.size() > 0) {
                    long size = response.top_stories.size();
                    for (int i = 0; i < size; i++) {
                        myDatabase.saveTopStories(response.top_stories.get(i),response.date);
                    }
                }

            }
        });
    }

    /**开启网络请求加载News**/
    private void loadNews(){
        final Time time = new Time();
        List<StoryModel> storyModelList ;
        final MyDatabase myDatabase =MyDatabase.getInstance(getApplicationContext());
        storyModelList = myDatabase.loadStory(time.getDate());
        long size = storyModelList.size();
        for(int i = 0; i < size; i++){
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
                    myDatabase.saveNews(response,time.getDate());
                }
            });
        }


    }

}
