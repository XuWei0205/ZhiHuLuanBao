package com.hanyu.zhihuluanbao.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.adapters.NewsAdapter;
import com.hanyu.zhihuluanbao.commons.API;
import com.hanyu.zhihuluanbao.managers.ActivityManager;
import com.hanyu.zhihuluanbao.managers.NetManager;
import com.hanyu.zhihuluanbao.models.NewsModel;
import com.hanyu.zhihuluanbao.models.StoryModel;
import com.hanyu.zhihuluanbao.utils.Util;

import java.util.ArrayList;
import java.util.List;

import static com.hanyu.zhihuluanbao.R.drawable.ic_launcher;

/**
 * Created by Dell on 2016/10/21.
 */
public class NewsListActivity extends BasicActivity implements View.OnClickListener{

    private Button menu;
    private TextView date;
    private Button msg;
    private Button changeMode;
    private ListView listView;
    private NewsAdapter newsAdapter;

   // private String[] data ={"t1","t2","t3","t4","t5","t6","t7","t8","t9","t10","t11"};


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        menu.setOnClickListener(this);
        msg.setOnClickListener(this);
        changeMode.setOnClickListener(this);
        newsAdapter = new NewsAdapter(getApplicationContext());
        getData();
        listView.addHeaderView(LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.news_list_header_layout, null));
        listView.setAdapter(newsAdapter);






    }

    private void getData(){
        NetManager.doHttpGet(getApplicationContext(), null, API.GET_LATEST_NEWS_URL, null,
                NewsModel.class, new NetManager.ResponseListener<NewsModel>() {
                    @Override
                    public void onResponse(NewsModel response) {
                        if (response != null && response.stories != null) {
                            newsAdapter.setDatas(response.stories);
                            newsAdapter.notifyDataSetChanged();
                        } else {
                            Util.toastTips(getApplicationContext(),"读取数据错误");
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Util.toastTips(getApplicationContext(),"网络错误");
                    }

                    @Override
                    public void onAsyncResponse(NewsModel response) {


                    }
                });

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityManager.finishAll();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    void initView() {
        menu = (Button) findViewById(R.id.menu);
        date = (TextView) findViewById(R.id.date);
        msg = (Button) findViewById(R.id.msg);
        changeMode = (Button) findViewById(R.id.changeMode);
        listView = (ListView) findViewById(R.id.newsList);


    }

    @Override
    int getLayoutRes() {
        return R.layout.news_list_layout;
    }

    @Override
    View getContentView() {
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu:
                //打开抽屉式菜单

                break;
            case R.id.msg:
                //查看消息


                break;
            case R.id.changeMode:
                //改变模式

                break;
            default:
                break;
        }

    }
}
