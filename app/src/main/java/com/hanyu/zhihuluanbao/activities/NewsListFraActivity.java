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
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.adapters.DrawerAdapter;
import com.hanyu.zhihuluanbao.commons.API;
import com.hanyu.zhihuluanbao.fragments.NewsListFragment;
import com.hanyu.zhihuluanbao.fragments.NewsPaperFra;
import com.hanyu.zhihuluanbao.managers.ActivityManager;
import com.hanyu.zhihuluanbao.managers.NetManager;
import com.hanyu.zhihuluanbao.models.OtherModel;
import com.hanyu.zhihuluanbao.models.ThemeModel;
import com.hanyu.zhihuluanbao.utils.Util;

import java.util.ArrayList;

/**
 * Created by Dell on 2016/11/3.
 */
public class NewsListFraActivity extends ActionBarActivity {
    private NewsListFragment newsListFragment;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private DrawerAdapter drawerAdapter;
    private ArrayList<OtherModel> allData = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list_fra_act_layout);

        getData();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.menu);

           
        }

        drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerAdapter = new DrawerAdapter(getApplicationContext());
        drawerList.setAdapter(drawerAdapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsPaperFra fra = new NewsPaperFra();
                Bundle arg = new Bundle();
                arg.putLong("id", allData.get(position).id);
                fra.setArguments(arg);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.content_frameLayout,fra,null);
                //ft.addToBackStack(null);
                ft.commitAllowingStateLoss();
                drawerLayout.closeDrawer(drawerList);

            }
        });




        newsListFragment = new NewsListFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_frameLayout,newsListFragment,null);
        //ft.addToBackStack(null);
        ft.commitAllowingStateLoss();


    }


    private void getData(){
        NetManager.doHttpGet(getApplicationContext(), null, API.GET_THEME_URL, null, ThemeModel.class, new NetManager.ResponseListener<ThemeModel>() {
            @Override
            public void onResponse(ThemeModel response) {
                if(response.others != null) {
                    allData.addAll(response.others);
                    drawerAdapter.setData(allData);
                    drawerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onAsyncResponse(ThemeModel response) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityManager.finishAll();
    }
}
