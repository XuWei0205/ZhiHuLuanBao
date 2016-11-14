package com.hanyu.zhihuluanbao.activities;

import android.app.Activity;


import android.os.Bundle;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.zip.Inflater;

/**
 * Created by Dell on 2016/11/3.
 */
public class NewsListFraActivity extends ActionBarActivity {
    private NewsListFragment newsListFragment;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private DrawerAdapter drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private View headerView;
    private Button home;
    private int currentFragment ;

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
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerList = (ListView) findViewById(R.id.drawer_list);
        headerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.drawer_list_header,null);
        drawerList.addHeaderView(headerView);
        home = (Button)headerView.findViewById(R.id.to_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment == 2) {
                    newsListFragment = new NewsListFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frameLayout, newsListFragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    currentFragment = 1;
                }
                drawerLayout.closeDrawer(drawerList);
            }
        });

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
                currentFragment = 2;
                drawerLayout.closeDrawer(drawerList);

            }
        });




        newsListFragment = new NewsListFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_frameLayout,newsListFragment,null);
        //ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
        currentFragment = 1;


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
