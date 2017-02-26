package com.hanyu.zhihuluanbao.activities;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.commons.API;
import com.hanyu.zhihuluanbao.commons.BaseTask;
import com.hanyu.zhihuluanbao.commons.Preferences;
import com.hanyu.zhihuluanbao.managers.NetManager;
import com.hanyu.zhihuluanbao.managers.TaskManager;
import com.hanyu.zhihuluanbao.models.MyDatabase;
import com.hanyu.zhihuluanbao.models.WelcomeImage;
import com.hanyu.zhihuluanbao.utils.Util;




public class WelcomeActivity extends BasicActivity {
    private ImageView imgv_splash;
    private TextView tv_copyright;
    private Handler handler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deleteDate();
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
                NewsListFraActivity.openActivity(WelcomeActivity.this);
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
                .into(imgv_splash);
        tv_copyright.setText(welcomeImage.text);


    }
    private void getPageInfo(){
       NetManager.doHttpGet(getApplicationContext(), null, API.GET_IMAGE_URL, null, WelcomeImage.class, new NetManager.ResponseListener<WelcomeImage>() {
           @Override
           public void onResponse(WelcomeImage response) {
               DisplayMetrics dm = getResources().getDisplayMetrics();
               int w_screen = dm.widthPixels;
               int h = dm.heightPixels;
               int h_screen = h - Util.getStatusBarHeight(getApplicationContext());


               Glide.with(getApplicationContext())
                       .load(response.img)
                       .downloadOnly(w_screen, h_screen);
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

/**删除之前的缓存数据**/
    private void deleteDate(){
        TaskManager.getIns().executeAsyncTask(new BaseTask() {
            @Override
            public void executeTask() {

            }

            @Override
            public void executeAsyncTask() {
                MyDatabase myDatabase = MyDatabase.getInstance(getApplicationContext());
                myDatabase.deleteData();

            }
        });
    }




    int getLayoutRes() {
        return R.layout.welcome_layout;

    }


    void initView() {
        imgv_splash = (ImageView) findViewById(R.id.imgv_welcome_image);
        tv_copyright = (TextView) findViewById(R.id.tv_welcome_copyright);
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