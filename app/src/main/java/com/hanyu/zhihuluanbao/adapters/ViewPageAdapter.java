package com.hanyu.zhihuluanbao.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hanyu.zhihuluanbao.views.BannerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2016/10/27.
 */
public class ViewPageAdapter extends PagerAdapter {
    private List<BannerView> viewList;


   public ViewPageAdapter(List<BannerView> viewList){
       this.viewList = viewList;

    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {

        return arg0 == arg1;
    }

    @Override
    public int getCount() {

        return viewList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {

        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(viewList.get(position));


        return viewList.get(position);
    }
}






