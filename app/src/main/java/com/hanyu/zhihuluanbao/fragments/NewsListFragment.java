package com.hanyu.zhihuluanbao.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.annotations.Until;
import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.activities.NewsListActivity;
import com.hanyu.zhihuluanbao.activities.NewsListFraActivity;
import com.hanyu.zhihuluanbao.activities.ReadNewsActivity;
import com.hanyu.zhihuluanbao.adapters.NewsAdapter;
import com.hanyu.zhihuluanbao.adapters.ViewPageAdapter;
import com.hanyu.zhihuluanbao.commons.API;
import com.hanyu.zhihuluanbao.commons.BaseTask;
import com.hanyu.zhihuluanbao.managers.NetManager;
import com.hanyu.zhihuluanbao.managers.TaskManager;
import com.hanyu.zhihuluanbao.models.MyDatabase;
import com.hanyu.zhihuluanbao.models.NewsListModel;
import com.hanyu.zhihuluanbao.models.StoryModel;
import com.hanyu.zhihuluanbao.models.TopStoryModel;
import com.hanyu.zhihuluanbao.utils.CLog;
import com.hanyu.zhihuluanbao.utils.LocalDisplay;
import com.hanyu.zhihuluanbao.utils.NetWorkingUtil;
import com.hanyu.zhihuluanbao.utils.Util;
import com.hanyu.zhihuluanbao.views.BannerView;
import com.viewpagerindicator.TitlePageIndicator;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.FormatFlagsConversionMismatchException;
import java.util.List;

import java.util.logging.LogRecord;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by Dell on 2016/11/3.
 */
public class NewsListFragment extends BasicFragment  {



    private ListView lv_content;
    private NewsAdapter newsAdapter;
    private Boolean isLatest = true;
    private int lastVisibleIndex;
    //private ProgressBar progressBar;
    private String date;
    private Calendar now;
    private View footerView,headerView;
    private ArrayList<StoryModel> allDatas = new ArrayList<>();

    private SimpleDateFormat dateFormat;

    private List<BannerView> viewList;

    private AutoScrollViewPager viewPager;
    private ViewPageAdapter pageAdapter;
    private PtrFrameLayout mPtrFrame;
    private NetWorkingUtil netWorkingUtil;
    private MyHandler myHandler = new MyHandler(this);





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.news_lisi_fra_layout,container,false);
        lv_content = (ListView)view.findViewById(R.id.newsList);

        newsAdapter = new NewsAdapter(getActivity().getApplicationContext());
        now = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyyMMdd");
        date = dateFormat.format(now.getTime());
        CLog.i("date--------> ", "" + date);

        netWorkingUtil = new NetWorkingUtil();
        int connection = netWorkingUtil.getConnection(getActivity());
        if (connection == 1 || connection == 2 ) {
            getData(date, false);
        }else if (connection == 0){
            getLocalData();

        }


        headerView=LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.news_list_header_layout, null);
        lv_content.addHeaderView(headerView);
        footerView = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.more_data, null);
        //progressBar=(ProgressBar)footerView.findViewById(R.id.load_bar);

        lv_content.addFooterView(footerView);
        lv_content.setAdapter(newsAdapter);


        viewPager = (AutoScrollViewPager)headerView.findViewById(R.id.header_image);
        viewPager.startAutoScroll();
        viewPager.setInterval(3000);


        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ReadNewsActivity.class);
                StoryModel storyModel = newsAdapter.getItem(position - 1);
                intent.putExtra("id", storyModel.id);
                CLog.i("id--->", "" + storyModel.id);
                startActivity(intent);

            }
        });
        lv_content.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastVisibleIndex = firstVisibleItem + visibleItemCount -1;
                if (totalItemCount == newsAdapter.getCount()){
                    lv_content.removeFooterView(footerView);
                    Util.toastTips(getActivity().getApplicationContext(),"没有更多数据");
                }

            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                        lastVisibleIndex == newsAdapter.getCount() + 1) {
                    CLog.i(newsAdapter.getCount() + "----------------" + lastVisibleIndex);
                    //progressBar.setVisibility(View.VISIBLE);
                    getData(date,false);
                    CLog.i("BeforeData------->", date);
                }


            }


        });



        /** 设置mPtrFrame **/
        mPtrFrame = (PtrFrameLayout) view.findViewById(R.id.list_frame);
        final MaterialHeader header = new MaterialHeader(getContext().getApplicationContext());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
        header.setPtrFrameLayout(mPtrFrame);




        mPtrFrame.setLoadingMinTime(1000);
        mPtrFrame.setDurationToCloseHeader(1500);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setResistance(4.0f);//设置提抗力
        mPtrFrame.setRatioOfHeaderHeightToRefresh(0.2f);//触发刷新时移动的位置比例
        mPtrFrame.setDurationToClose(3000);//回弹延时
        mPtrFrame.setDurationToCloseHeader(1000);//头部回弹时间
        mPtrFrame.setPullToRefresh(false);//
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.disableWhenHorizontalMove(true);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                CLog.i("do refresh");
                isLatest = true;

                getData(date,true);

            }
        });
        return view;

    }



    private void getData(final String dates, final boolean isFromRefresh){
        String url;
        if (isLatest ) {
            isLatest = false;
            url= API.GET_LATEST_NEWS_URL;
        }else{

            url = API.BASE_GET_BEFORE_URL + dates;
            now.add(Calendar.DATE, -1);
            date = dateFormat.format(now.getTime());

        }
        NetManager.doHttpGet(getActivity().getApplicationContext(), null, url, null,
                NewsListModel.class, new NetManager.ResponseListener<NewsListModel>() {
                    @Override
                    public void onResponse(NewsListModel response) {
                        if (response.top_stories != null && response.top_stories.size() > 0) {
                            // 设置banner
                            if (viewList == null) {
                                viewList = new ArrayList<BannerView>(response.top_stories.size());
                            }
                            if (isFromRefresh) {
                                viewList.clear();
                            }
                            BannerView bannerView;
                            long size = response.top_stories.size();
                            for (int i = 0; i < size; i++) {
                                bannerView = new BannerView(getActivity());
                                bannerView.setTitle(response.top_stories.get(i).title);
                                bannerView.setImagePic(response.top_stories.get(i).image);
                                bannerView.setOnClickListener(bannerClick());
                                bannerView.setTag(response.top_stories.get(i).id);
                                viewList.add(bannerView);
                            }
                            if (pageAdapter == null) {
                                pageAdapter = new ViewPageAdapter(viewList);
                                viewPager.setAdapter(pageAdapter);
                               // TitlePageIndicator titleIndicator = (TitlePageIndicator)headerView.findViewById(R.id.indicator);
                                //titleIndicator.setViewPager(viewPager);


                            } else {

                                pageAdapter.notifyDataSetChanged();
                            }
                        }
                        //newsDate.setText(response.date);
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
                            Util.toastTips(getActivity().getApplicationContext(), "读取数据失败");
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
                        Util.toastTips(getActivity().getApplicationContext(), "加载失败");


                    }

                    @Override
                    public void onAsyncResponse(NewsListModel response) {

                    }
                });




    }

    private void getLocalData(){
       TaskManager.getIns().executeAsyncTask(new BaseTask() {
           @Override
           public void executeTask() {
           }

           @Override
           public void executeAsyncTask() {
               List<TopStoryModel> topStoryModelList;
               topStoryModelList = MyDatabase
                       .getInstance(getActivity().getApplicationContext()).loadTopStory(date);
               List<StoryModel> storyModelList;
               storyModelList = MyDatabase
                       .getInstance(getActivity().getApplication()).loadStory(date);
               Message message = new Message();
               ArrayList<List> arrayList = new ArrayList<List>();
               arrayList.add(topStoryModelList);
               arrayList.add(storyModelList);
               message.obj = arrayList;
               myHandler.sendMessage(message);

           }
       });

    }


    static class MyHandler extends Handler{
            WeakReference<NewsListFragment> mFragment;
            MyHandler(NewsListFragment fragment){
                mFragment = new WeakReference<NewsListFragment>(fragment);
            }
            public void handleMessage(Message msg){
                NewsListFragment theFragment = mFragment.get();
                if (theFragment == null ){
                    return;
                }
                List<TopStoryModel> topStoryModelList =((ArrayList <List>) msg.obj).get(0);
                if (topStoryModelList != null && topStoryModelList.size() > 0) {
                    // 设置banner
                    if (theFragment.viewList == null) {
                        theFragment.viewList = new ArrayList<BannerView>(topStoryModelList.size());
                    }

                    BannerView bannerView;
                    long size = topStoryModelList.size();
                    for (int i = 0; i < size; i++) {
                        bannerView = new BannerView(theFragment.getActivity());
                        bannerView.setTitle(topStoryModelList.get(i).title);
                        bannerView.setImagePic(topStoryModelList.get(i).image);
                        bannerView.setOnClickListener(theFragment.bannerClick());
                        bannerView.setTag(topStoryModelList.get(i).id);
                        theFragment.viewList.add(bannerView);
                    }
                    if (theFragment.pageAdapter == null) {
                        theFragment.pageAdapter = new ViewPageAdapter(theFragment.viewList);
                        theFragment.viewPager.setAdapter(theFragment.pageAdapter);
                        //TitlePageIndicator titleIndicator = (TitlePageIndicator)theFragment.headerView.findViewById(R.id.indicator);
                        //titleIndicator.setViewPager(theFragment.viewPager);

                    } else {

                        theFragment.pageAdapter.notifyDataSetChanged();
                    }
                }

                List<StoryModel> storyModelList =((ArrayList <List>) msg.obj).get(1);
                if (storyModelList != null){
                    theFragment.allDatas.addAll(storyModelList);
                    theFragment.newsAdapter.setDatas(theFragment.allDatas);
                    theFragment.newsAdapter.notifyDataSetChanged();

                }



            }
    }










    private View.OnClickListener bannerClick() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                long id = (long) v.getTag();
                Intent intent = new Intent (getActivity(),ReadNewsActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

            }
        };
    }





}
