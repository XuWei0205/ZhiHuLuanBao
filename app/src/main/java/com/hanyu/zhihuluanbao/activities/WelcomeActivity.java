package com.hanyu.zhihuluanbao.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.managers.NetManager;
import com.hanyu.zhihuluanbao.managers.OkHttpClientManager;
import com.hanyu.zhihuluanbao.models.WelcomeImage;
import com.hanyu.zhihuluanbao.utils.API;
import com.hanyu.zhihuluanbao.utils.Debug;


public class WelcomeActivity extends BasicActivity {
    private ImageView imageView;
    private TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetManager.doHttpGet(this, null, API.GET_IMAGE_URL, null, WelcomeImage.class,
                new NetManager.ResponseListener<WelcomeImage>() {
                    @Override
                    public void onResponse(WelcomeImage response) {
                        textView.setText(response.getText());
                        showImage(response.getImg());


                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                    @Override
                    public void onAsyncResponse(WelcomeImage response) {

                    }
                }, 200);


    }

    private void showImage(String url){
        Glide.with(this)
                .load(url)
                .into(imageView);


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



}