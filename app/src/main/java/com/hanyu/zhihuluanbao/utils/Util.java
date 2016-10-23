package com.hanyu.zhihuluanbao.utils;

import android.content.Context;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * Created by hehuajia on 16/10/20.
 */

public class Util {

    public static void toastTips(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    public static void toastTips(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }
    public static int getStatusBarHeight(Context context){
        int statusBarHeight = 0;
        try {

            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = context.getResources().getDimensionPixelSize(x);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

}
