package com.hanyu.zhihuluanbao.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.volley.VolleyError;
import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.commons.API;
import com.hanyu.zhihuluanbao.commons.HtmlFile;
import com.hanyu.zhihuluanbao.managers.NetManager;
import com.hanyu.zhihuluanbao.models.MyDatabase;
import com.hanyu.zhihuluanbao.models.NewsModel;
import com.hanyu.zhihuluanbao.utils.CLog;
import com.hanyu.zhihuluanbao.utils.Debug;
import com.hanyu.zhihuluanbao.utils.NetWorkingUtil;
import com.hanyu.zhihuluanbao.utils.Util;

/**
 * Created by Dell on 2016/10/25.
 */
public class ReadNewsActivity extends BasicActivity {
    private WebView wv_showNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            Intent intent = getIntent();
            long id = intent.getLongExtra("id", 0);
            NetWorkingUtil util = new NetWorkingUtil();
            int i =  util.getConnection(getApplicationContext());
            if(i == 1 || i == 2){
            showNews(API.BASE_GET_NEWS_URL + id);
            }else if(i == 0){
                showLocalNews(id);
            }
        } else {
            Util.toastTips(getApplicationContext(),"加载失败");

        }

    }
    public static void openActivity(Context context ,long id){
        Intent intent = new Intent (context,ReadNewsActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);

    }

    private void showLocalNews(long id){
        MyDatabase myDatabase = MyDatabase.getInstance(getApplicationContext());

        NewsModel newsModel = myDatabase.loadNews(id);
        if (newsModel.body != null){
            HtmlFile htmlFile = new HtmlFile(newsModel);
            wv_showNews.loadDataWithBaseURL("x-data://base",htmlFile.html, "text/html", "utf-8", null);
        }else{
            Util.toastTips(getApplicationContext(),"读取失败");
        }
    }

    private void showNews(String url){
        NetManager.doHttpGet(getApplicationContext(), null, url, null, NewsModel.class, new NetManager.ResponseListener<NewsModel>() {
            @Override
            public void onResponse(NewsModel response) {
                if(response.body != null) {
                    HtmlFile htmlFile = new HtmlFile(response);
                    wv_showNews.loadDataWithBaseURL("x-data://base",htmlFile.html, "text/html", "utf-8", null);
                }else{
                    Util.toastTips(getApplicationContext(),"读取数据错误");
                }

            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onAsyncResponse(NewsModel response) {

            }
        });

    }

    @Override
    void initView() {
        wv_showNews = (WebView) findViewById(R.id.news_webView);

    }

    @Override
    int getLayoutRes() {

        return R.layout.news_layout;
    }

    @Override

    View getContentView() {
        return null;
    }


}

