package com.hanyu.zhihuluanbao.managers;

import com.google.gson.Gson;
import com.hanyu.zhihuluanbao.models.NewsListModel;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Dell on 2016/10/11.
 */
public class OkHttpClientManager {
    private OkHttpClient okHttpClient;
    private static OkHttpClientManager mInstance;
    private Gson gson;

    private OkHttpClientManager(){
        okHttpClient = new OkHttpClient();
        gson = new Gson();
    }

    public static OkHttpClientManager getInstance(){
        if (mInstance == null){
            synchronized (OkHttpClientManager.class){
                if (mInstance == null){
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;

    }

    private Response _getAsyn(String url)throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        Response execute = call.execute();
        return  execute;
    }

    private String _getAsString(String url)throws IOException{
        Response response = _getAsyn(url);
        return response.body().string();
    }

    public static Response getAsyn(String url)throws IOException{
        return mInstance._getAsyn(url);
    }


    public static String getAsString(String url)throws IOException{
        return mInstance._getAsString(url);
    }

    private void handleRequest(String url)throws IOException{
        String request = mInstance._getAsString(url);
        NewsListModel newsListModel = gson.fromJson(request,NewsListModel.class);
    }






}
