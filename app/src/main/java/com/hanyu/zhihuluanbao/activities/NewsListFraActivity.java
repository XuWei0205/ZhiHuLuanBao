package com.hanyu.zhihuluanbao.activities;

import android.app.Activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.hanyu.zhihuluanbao.service.DownloadService;
import com.hanyu.zhihuluanbao.utils.CLog;
import com.hanyu.zhihuluanbao.utils.NetWorkingUtil;
import com.hanyu.zhihuluanbao.utils.Util;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Dell on 2016/11/3.
 */
public class NewsListFraActivity extends ActionBarActivity {
    private NewsListFragment newsListFragment;
    private DrawerLayout drawerLayout;
    private ListView lv_drawer;
    private DrawerAdapter drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private View headerView;
    private Button home,downLoad;
    private int currentFragment ;


    private ArrayList<OtherModel> allData = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//news_list_fra_act_layout

        getData();

        drawerLayout = (DrawerLayout) findViewById(R.id.mDrawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);

        }
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        lv_drawer = (ListView) findViewById(R.id.mDrawer_list);
        headerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.drawer_list_header,null);
        lv_drawer.addHeaderView(headerView);

        /**设置回到主页按键实现回到主页功能**/
        home = (Button)headerView.findViewById(R.id.to_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment == 2) {
                    newsListFragment = new NewsListFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.mContent_fra, newsListFragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    currentFragment = 1;
                }
                drawerLayout.closeDrawer(lv_drawer);
            }
        });
        /**设置抽屉菜单的点击功能实现切换主题日报功能**/
        drawerAdapter = new DrawerAdapter(getApplicationContext());
        lv_drawer.setAdapter(drawerAdapter);
        lv_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsPaperFra fra = new NewsPaperFra();
                Bundle arg = new Bundle();
                arg.putLong("id", allData.get(position-1).id);
                fra.setArguments(arg);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.mContent_fra,fra,null);
                ft.commitAllowingStateLoss();
                currentFragment = 2;
                drawerLayout.closeDrawer(lv_drawer);

            }
        });

        /**设置离线下载按键**/
        downLoad = (Button) headerView.findViewById(R.id.download);
        downLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetWorkingUtil netWorkingUtil = new NetWorkingUtil();
                int net = netWorkingUtil.getConnection(getApplicationContext());
                if(net == 0){
                    Util.toastTips(getApplicationContext(),"无网络，请检查网络");
                }else if(net == 1){
                    CLog.i(getApplicationContext(),"startService");
                    try {
                        Intent startService = new Intent(getApplicationContext(), DownloadService.class);
                        startService(startService);
                    }catch (Exception e){
                        CLog.i("exception","----->" +e.toString());
                    }
                }else if(net == 2){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewsListFraActivity.this);
                    alertDialog.setTitle("提示");
                    alertDialog.setMessage("正在使用手机流量");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("继续下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intentService = new Intent(getApplicationContext(), DownloadService.class);
                                startService(intentService);
                            }catch(Exception e){
                                CLog.i("exception","------>" + e.toString());
                            }
                        }
                    });
                    alertDialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }

            }
        });

        /**初始化首页fragment**/
        newsListFragment = new NewsListFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.mContent_fra,newsListFragment,null);
        ft.commitAllowingStateLoss();
        currentFragment = 1;


    }

    public static void openActivity(Context context){
        Intent intent = new Intent(context,NewsListFraActivity.class);
        context.startActivity(intent);
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
