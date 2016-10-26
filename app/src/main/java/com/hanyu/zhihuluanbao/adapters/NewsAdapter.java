package com.hanyu.zhihuluanbao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.models.StoryModel;

import java.util.ArrayList;

/**
 * Created by Dell on 2016/10/24.
 */
public class NewsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    public ArrayList<StoryModel> mDatas = new ArrayList<>();

    public NewsAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }
    public void setDatas(ArrayList<StoryModel> datas) {
        this.mDatas.clear();
        this.mDatas.addAll(datas);
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public StoryModel getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.news_item, null);
            holder = new Holder();
            holder.tv_content = (TextView) convertView.findViewById(R.id.news_title);
            holder.imgv_pic = (ImageView) convertView.findViewById(R.id.news_image);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final StoryModel storyModel = mDatas.get(position);
        holder.tv_content.setText(storyModel.title);
        if (storyModel.images == null || storyModel.images.size() <= 0) {
            holder.imgv_pic.setVisibility(View.GONE);
        } else {
            holder.imgv_pic.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(storyModel.images.get(0)).into(holder.imgv_pic);
        }
        return convertView;
    }

    static class Holder {
        public TextView tv_content;
        public ImageView imgv_pic;
    }
  /*  public View getView(int position, View convertView, ViewGroup parent) {
        StoryModel storyModel = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView storyImage = (ImageView) view.findViewById(R.id.news_image);
        TextView storyTitle = (TextView) view.findViewById(R.id.news_title);

        storyTitle.setText(storyModel.title);
        return view;
        */
}




