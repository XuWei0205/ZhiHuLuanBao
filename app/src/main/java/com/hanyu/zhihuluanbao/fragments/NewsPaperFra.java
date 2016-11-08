package com.hanyu.zhihuluanbao.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.adapters.NewsAdapter;
import com.hanyu.zhihuluanbao.commons.API;
import com.hanyu.zhihuluanbao.managers.NetManager;
import com.hanyu.zhihuluanbao.models.PaperListModel;
import com.hanyu.zhihuluanbao.models.StoryModel;
import com.hanyu.zhihuluanbao.utils.Util;

import java.util.ArrayList;

/**
 * Created by Dell on 2016/11/4.
 */
public class NewsPaperFra extends BasicFragment {
    private NewsAdapter adapter;
    private ListView newsList;
    private String id;
    private View headerView;
    private ImageView paperImage;
    private ArrayList<StoryModel> datas = new ArrayList<>();



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_paper_layout,container,false);
        newsList = (ListView)view.findViewById(R.id.paperList);
        headerView =LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.paper_header,null);
        newsList.addHeaderView(headerView);
        adapter = new NewsAdapter(getActivity().getApplicationContext());
        newsList.setAdapter(adapter);

        paperImage = (ImageView) headerView.findViewById(R.id.page_image);


        getData(id);
        return view;

    }



    private void getData(String id){
        NetManager.doHttpGet(getActivity().getApplication(), null, API.BASE_GET_THEME_URL + id, null, PaperListModel.class, new NetManager.ResponseListener<PaperListModel>() {

            @Override
            public void onResponse(PaperListModel response) {
                Glide.with(getActivity())
                        .load(response.background)
                        .into(paperImage);
                if(response.stories != null){
                    datas.addAll(response.stories);
                    adapter.setDatas(datas);
                    adapter.notifyDataSetChanged();
                }else{
                    Util.toastTips(getActivity().getApplicationContext(),"读取数据失败");
                }



            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onAsyncResponse(PaperListModel response) {

            }
        });
    }


}
