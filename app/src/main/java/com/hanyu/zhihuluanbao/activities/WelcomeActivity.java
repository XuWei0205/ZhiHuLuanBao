package com.hanyu.zhihuluanbao.activities;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.commons.API;
import com.hanyu.zhihuluanbao.commons.Preferences;
import com.hanyu.zhihuluanbao.managers.NetManager;
import com.hanyu.zhihuluanbao.models.WelcomeImage;
import com.hanyu.zhihuluanbao.utils.Util;

import java.util.Map;


public class WelcomeActivity extends BasicActivity {
    private ImageView imageView;
    private TextView textView;
    private Handler handler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断最新图片

        if(TextUtils.isEmpty(Preferences.getInstance(getApplicationContext())
                .getWelcomePageInfo())){
           getPageInfo();
        }else {
            showSplashImage();
            getPageInfo();
        }

        // 延迟启动下一个页面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent (WelcomeActivity.this,NewsListFraActivity.class);
                //Intent intent = new Intent(WelcomeActivity.this,NewsListActivity.class);
                startActivity(intent);


            }
        },3000);
    }


    private void showSplashImage(){
        String s =
        Preferences.getInstance(getApplicationContext()).getWelcomePageInfo();
        Gson gson = new Gson();
        WelcomeImage welcomeImage =
        gson.fromJson(s, WelcomeImage.class);

        Glide.with(getApplicationContext())
                .load(welcomeImage.img)
                .crossFade(2000)
                .into(imageView);
        textView.setText(welcomeImage.text);


    }
    private void getPageInfo(){
       NetManager.doHttpGet(getApplicationContext(), null, API.GET_IMAGE_URL, null, WelcomeImage.class, new NetManager.ResponseListener<WelcomeImage>() {
           @Override
           public void onResponse(WelcomeImage response) {
               DisplayMetrics dm =getResources().getDisplayMetrics();
               int w_screen = dm.widthPixels;
               int h = dm.heightPixels;
               int h_screen =h - Util.getStatusBarHeight(getApplicationContext());


               Glide.with(getApplicationContext())
                       .load(response.img)
                       .downloadOnly(w_screen,h_screen);
               // Glide 异步下载图片

           }

           @Override
           public void onErrorResponse(VolleyError error) {

           }

           @Override
           public void onAsyncResponse(WelcomeImage response) {
               Gson gson = new Gson();
               Preferences.getInstance(getApplicationContext()).setWelcomePageInfo(gson.toJson(response));

           }
       }, 200);

    }






    int getLayoutRes() {
        return R.layout.welcome_layout;

    }


    void initView() {
        imageView = (ImageView) findViewById(R.id.welcomeImage);
        textView = (TextView) findViewById(R.id.copyright);
    }



    View getContentView(){
        return null;
    }


    protected void onStart(){
        super.onStart();
    }

    protected void onResume(){
        super.onResume();
    }

    protected void onPause(){
        super.onPause();
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}