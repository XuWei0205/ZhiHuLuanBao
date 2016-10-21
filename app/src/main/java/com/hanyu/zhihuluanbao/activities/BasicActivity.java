package com.hanyu.zhihuluanbao.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by hehuajia on 16/10/20.
 */

public abstract class BasicActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if (getLayoutRes() != 0) {
            setContentView(getLayoutRes());
        } else {
            if (getContentView() == null) {
                throw new RuntimeException("Content View is null!");
            }
            setContentView(getContentView());
        }
        initView();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutRes() != 0) {
            setContentView(getLayoutRes());
        } else {
            if (getContentView() == null) {
                throw new RuntimeException("Content View is null!");
            }
            setContentView(getContentView());
        }
        initView();
    }

    /** 初始化控件*/
    abstract void initView();

    /** 设置Activity的布局文件，用View的方式返回0*/
    abstract int getLayoutRes();

    /** 设置Activity的布局View，getLayoutRes返回0才有效*/
    abstract View getContentView();
}
