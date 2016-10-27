package com.hanyu.zhihuluanbao.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hanyu.zhihuluanbao.R;

/**
 * Created by Dell on 2016/10/27.
 */
public class BannerView extends FrameLayout {
    private TextView tv_title;
    private ImageView imgv_pic;
    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.page1_layout, null);
        tv_title = (TextView) view.findViewById(R.id.textView_title);
        imgv_pic = (ImageView) view.findViewById(R.id.page_image);
        addView(view);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setImagePic(String url) {
        Glide.with(getContext()).load(url).into(imgv_pic);
    }
}
