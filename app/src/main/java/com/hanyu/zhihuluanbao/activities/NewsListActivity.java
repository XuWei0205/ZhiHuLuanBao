package com.hanyu.zhihuluanbao.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.adapters.NewsAdapter;
import com.hanyu.zhihuluanbao.commons.API;
import com.hanyu.zhihuluanbao.managers.ActivityManager;
import com.hanyu.zhihuluanbao.managers.NetManager;
import com.hanyu.zhihuluanbao.models.BeforeListNewsModel;
import com.hanyu.zhihuluanbao.models.NewsListModel;
import com.hanyu.zhihuluanbao.models.StoryModel;
import com.hanyu.zhihuluanbao.utils.CLog;
import com.hanyu.zhihuluanbao.utils.Debug;
import com.hanyu.zhihuluanbao.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Dell on 2016/10/21.
 */
public class NewsListActivity extends BasicActivity implements View.OnClickListener{

    private Button menu;
    private TextView newsDate;
    private Button msg;
    private Button changeMode;
    private ListView listView;
    private NewsAdapter newsAdapter;
    private Boolean isLatest = true;
    private int lastVisibleIndex;
    private ProgressBar progressBar;
    private String date;
    private Calendar now;
    private View footerView,headerView;
    private ArrayList<StoryModel> allDatas = new ArrayList<>();

    private SimpleDateFormat dateFormat;
   // private String[] data ={"t1","t2","t3","t4","t5","t6","t7","t8","t9","t10","t11"};


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        menu.setOnClickListener(this);
        msg.setOnClickListener(this);
        changeMode.setOnClickListener(this);
        newsAdapter = new NewsAdapter(getApplicationContext());
        now = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyyMMdd");
        date = dateFormat.format(now.getTime());
        CLog.i("date--------> ", "" + date);
        getData(date);
        headerView=LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.news_list_header_layout, null);
        listView.addHeaderView(headerView);
        footerView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.more_data, null);
        progressBar=(ProgressBar)footerView.findViewById(R.id.load_bar);
        listView.addFooterView(footerView);

        listView.setAdapter(newsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NewsListActivity.this, ReadNewsActivity.class);
                StoryModel storyModel = newsAdapter.getItem(position - 1);
                intent.putExtra("id", storyModel.id);
                CLog.i("id--->", "" + storyModel.id);
                startActivity(intent);

            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastVisibleIndex = firstVisibleItem + visibleItemCount -1;
                if (totalItemCount == newsAdapter.getCount()){
                    listView.removeFooterView(footerView);
                    Util.toastTips(getApplicationContext(),"没有更多数据");
                }

            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                        lastVisibleIndex == newsAdapter.getCount() + 1) {
                    CLog.i(newsAdapter.getCount() + "----------------" + lastVisibleIndex);
                    progressBar.setVisibility(View.VISIBLE);
                    getData(date);
                    CLog.i("BeforeData------->", date);
                }


            }


        });






    }

    private void getData(final String dates){
        String url;
        if (isLatest) {
            isLatest = false;
            url=API.GET_LATEST_NEWS_URL;
        }else{
            url = API.BASE_GET_BEFORE_URL + dates;
        }
        NetManager.doHttpGet(getApplicationContext(), null, url, null,
                BeforeListNewsModel.class, new NetManager.ResponseListener<BeforeListNewsModel>() {
                    @Override
                    public void onResponse(BeforeListNewsModel response) {
                        newsDate.setText(response.date);
                        if (response.stories != null) {
                            allDatas.addAll(response.stories);
                            newsAdapter.setDatas(allDatas);
                            newsAdapter.notifyDataSetChanged();
                            now.add(Calendar.DATE, -1);
                            date = dateFormat.format(now.getTime());
                        } else {
                            Util.toastTips(getApplicationContext(), "读取数据失败");
                        }

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                    @Override
                    public void onAsyncResponse(BeforeListNewsModel response) {

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
        newsDate= (TextView) findViewById(R.id.date);
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
