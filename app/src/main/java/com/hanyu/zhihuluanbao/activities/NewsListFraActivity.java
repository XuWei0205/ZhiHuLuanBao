package com.hanyu.zhihuluanbao.activities;

import android.app.Activity;


import android.os.Bundle;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.adapters.DrawerAdapter;
import com.hanyu.zhihuluanbao.fragments.NewsListFragment;
import com.hanyu.zhihuluanbao.managers.ActivityManager;
import com.hanyu.zhihuluanbao.utils.Util;

/**
 * Created by Dell on 2016/11/3.
 */
public class NewsListFraActivity extends ActionBarActivity {
    private NewsListFragment newsListFragment;
    private DrawerLayout drawerLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list_fra_act_layout);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.menu);

           
        }





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
