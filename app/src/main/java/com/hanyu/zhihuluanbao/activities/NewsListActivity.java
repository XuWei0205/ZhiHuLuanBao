package com.hanyu.zhihuluanbao.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.adapters.NewsAdapter;
import com.hanyu.zhihuluanbao.adapters.ViewPageAdapter;
import com.hanyu.zhihuluanbao.commons.API;
import com.hanyu.zhihuluanbao.managers.ActivityManager;
import com.hanyu.zhihuluanbao.managers.NetManager;
import com.hanyu.zhihuluanbao.models.NewsListModel;
import com.hanyu.zhihuluanbao.models.StoryModel;
import com.hanyu.zhihuluanbao.utils.CLog;
import com.hanyu.zhihuluanbao.utils.LocalDisplay;
import com.hanyu.zhihuluanbao.utils.Util;
import com.hanyu.zhihuluanbao.views.BannerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by Dell on 2016/10/21.
 */
public class NewsListActivity extends BasicActivity{

    private ListView lv_content;
    private NewsAdapter newsAdapter;
    private Boolean isLatest = true;
    private int lastVisibleIndex;
    private String date;
    private Calendar now;
    private View footerView, headerView;
    private ArrayList<StoryModel> allDatas = new ArrayList<>();
    private SimpleDateFormat dateFormat;
    private List<BannerView> viewList;
    private AutoScrollViewPager viewPager;
    private ViewPageAdapter pageAdapter;
    private PtrFrameLayout mPtrFrame;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newsAdapter = new NewsAdapter(getApplicationContext());
        now = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyyMMdd");
        date = dateFormat.format(now.getTime());
        CLog.i("date--------> ", "" + date);
        getData(date, false);
        headerView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.news_list_header_layout, null);
        lv_content.addHeaderView(headerView);
        footerView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.more_data, null);
        lv_content.addFooterView(footerView);
        lv_content.setAdapter(newsAdapter);


        viewPager = (AutoScrollViewPager) headerView.findViewById(R.id.header_image);
        viewPager.startAutoScroll();
        viewPager.setInterval(3000);

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoryModel storyModel = newsAdapter.getItem(position - 1);
                long storyId = storyModel.id;
                ReadNewsActivity.openActivity(getApplicationContext(),storyId);
            }
        });
        lv_content.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
                if (totalItemCount == newsAdapter.getCount()) {
                    lv_content.removeFooterView(footerView);
                    Util.toastTips(getApplicationContext(), "没有更多数据");
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                        lastVisibleIndex == newsAdapter.getCount() + 1) {
                    CLog.i(newsAdapter.getCount() + "----------------" + lastVisibleIndex);
                    getData(date, false);
                    CLog.i("BeforeData------->", date);
                }


            }


        });


        /** 设置mPtrFrame **/
        mPtrFrame = (PtrFrameLayout) findViewById(R.id.list_frame);
        final MaterialHeader header = new MaterialHeader(getApplicationContext());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
        header.setPtrFrameLayout(mPtrFrame);


        mPtrFrame.setLoadingMinTime(1000);
        mPtrFrame.setDurationToCloseHeader(1500);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setResistance(2.0f);//设置提抗力
        mPtrFrame.setRatioOfHeaderHeightToRefresh(0.2f);//触发刷新时移动的位置比例
        mPtrFrame.setDurationToClose(200);//回弹延时
        mPtrFrame.setDurationToCloseHeader(1000);//头部回弹时间
        mPtrFrame.setPullToRefresh(false);//
        mPtrFrame.setKeepHeaderWhenRefresh(true);//

        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                CLog.i("do refresh");
                isLatest = true;
                getData(date, true);

            }
        });


    }


    private void getData(final String dates, final boolean isFromRefresh) {
        String url;
        if (isLatest) {
            isLatest = false;
            url = API.GET_LATEST_NEWS_URL;
        } else {

            url = API.BASE_GET_BEFORE_URL + dates;
            now.add(Calendar.DATE, -1);
            date = dateFormat.format(now.getTime());

        }
        NetManager.doHttpGet(getApplicationContext(), null, url, null,
                NewsListModel.class, new NetManager.ResponseListener<NewsListModel>() {
                    @Override
                    public void onResponse(NewsListModel response) {
                        if (response.top_stories != null && response.top_stories.size() > 0) {
                            if (viewList == null) {
                                viewList = new ArrayList<BannerView>(response.top_stories.size());
                            }
                            if (isFromRefresh) {
                                viewList.clear();
                            }
                            if (pageAdapter == null) {
                                pageAdapter = new ViewPageAdapter(viewList);
                                viewPager.setAdapter(pageAdapter);
                            } else {
                                pageAdapter.notifyDataSetChanged();
                            }
                        }

                        if (response.stories != null) {
                            if (isFromRefresh) {
                                allDatas.clear();
                            }
                            allDatas.addAll(response.stories);
                            newsAdapter.setDatas(allDatas);
                            newsAdapter.notifyDataSetChanged();


                            if (isFromRefresh) {
                                now = Calendar.getInstance();
                                date = dateFormat.format(now.getTime());

                            }
                        } else {
                            Util.toastTips(getApplicationContext(), "读取数据失败");
                        }
                        if (isFromRefresh) {
                            mPtrFrame.refreshComplete();
                        }

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (isFromRefresh) {
                            mPtrFrame.refreshComplete();
                        }
                        Util.toastTips(getApplicationContext(), "加载失败");


                    }

                    @Override
                    public void onAsyncResponse(NewsListModel response) {
                        if (response.top_stories != null && response.top_stories.size() > 0){
                            BannerView bannerView;
                            int size =response.top_stories.size();
                            for (int i = 0; i < size; i++) {
                                bannerView = new BannerView(NewsListActivity.this);
                                bannerView.setTitle(response.top_stories.get(i).title);
                                bannerView.setImagePic(response.top_stories.get(i).image);
                                bannerView.setOnClickListener(bannerClick());
                                bannerView.setTag(response.top_stories.get(i).id);
                                viewList.add(bannerView);
                            }
                        }else{
                            Util.toastTips(getApplicationContext(),"读取数据失败");
                        }

                    }
                });


    }


    private View.OnClickListener bannerClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = (long) v.getTag();

            }
        };
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityManager.finishAll();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        viewPager.startAutoScroll();
        viewPager.setInterval(3000);
        super.onResume();
    }

    protected void onPause() {
        viewPager.stopAutoScroll();
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    void initView() {
        lv_content = (ListView) findViewById(R.id.newsList);

    }

    @Override
    int getLayoutRes() {
        return R.layout.news_list_layout;
    }

    @Override
    View getContentView() {
        return null;
    }

}
