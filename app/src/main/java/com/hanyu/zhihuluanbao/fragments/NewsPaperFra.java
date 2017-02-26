package com.hanyu.zhihuluanbao.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.activities.ReadNewsActivity;
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
    private ListView lv_news;

    private View headerView;
    private ImageView imgv_paper;
    private TextView tv_title;

    private ArrayList<StoryModel> datas = new ArrayList<>();





    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_paper_layout,container,false);
        lv_news = (ListView)view.findViewById(R.id.paperList);
        headerView =LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.paper_header,null);
        lv_news.addHeaderView(headerView);
        adapter = new NewsAdapter(getActivity().getApplicationContext());
        lv_news.setAdapter(adapter);
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ReadNewsActivity.class);
                StoryModel story = adapter.getItem(position-1);
                intent.putExtra("id",story.id);
                startActivity(intent);
            }
        });


        imgv_paper = (ImageView) headerView.findViewById(R.id.paperHeaderImage);
        tv_title = (TextView) headerView.findViewById(R.id.paperTitle);
        long id = getArguments().getLong("id");
        getData(id);

        return view;

    }
    private void getData(long id){
        NetManager.doHttpGet(getActivity().getApplication(), null, API.BASE_GET_THEME_URL + id, null, PaperListModel.class, new NetManager.ResponseListener<PaperListModel>() {

            @Override
            public void onResponse(PaperListModel response) {

                Glide.with(getActivity())
                        .load(response.background)
                        .into(imgv_paper);
                tv_title.setText(response.name);
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
