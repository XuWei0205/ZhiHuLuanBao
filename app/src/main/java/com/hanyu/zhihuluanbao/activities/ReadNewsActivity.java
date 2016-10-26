package com.hanyu.zhihuluanbao.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.android.volley.VolleyError;
import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.commons.API;
import com.hanyu.zhihuluanbao.commons.HtmlFile;
import com.hanyu.zhihuluanbao.managers.NetManager;
import com.hanyu.zhihuluanbao.models.NewsModel;
import com.hanyu.zhihuluanbao.utils.CLog;
import com.hanyu.zhihuluanbao.utils.Debug;
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
            if (Debug.DEVELOP_MODE) {
                CLog.i("id----->" + id);
            }
            showNews(API.BASE_GET_NEWS_URL + id);
        } else {
            Util.toastTips(getApplicationContext(),"加载失败");

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
