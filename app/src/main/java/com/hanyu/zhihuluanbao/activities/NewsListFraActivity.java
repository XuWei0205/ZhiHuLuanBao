package com.hanyu.zhihuluanbao.activities;

import android.app.Activity;


import android.os.Bundle;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.fragments.NewsListFragment;
import com.hanyu.zhihuluanbao.managers.ActivityManager;

/**
 * Created by Dell on 2016/11/3.
 */
public class NewsListFraActivity extends FragmentActivity {
    private NewsListFragment newsListFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list_fra_act_layout);
        newsListFragment = new NewsListFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_frameLayout,newsListFragment,null);
        ft.commitAllowingStateLoss();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityManager.finishAll();
    }
}
