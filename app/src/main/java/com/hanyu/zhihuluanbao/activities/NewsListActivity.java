package com.hanyu.zhihuluanbao.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.managers.ActivityManager;

/**
 * Created by Dell on 2016/10/21.
 */
public class NewsListActivity extends BasicActivity implements View.OnClickListener{

    private Button menu;
    private TextView date;
    private Button msg;
    private Button changeMode;
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        menu.setOnClickListener(this);
        msg.setOnClickListener(this);
        changeMode.setOnClickListener(this);




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
