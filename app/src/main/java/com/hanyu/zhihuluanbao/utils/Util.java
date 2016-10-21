package com.hanyu.zhihuluanbao.utils;

import android.content.Context;
import android.widget.Toast;

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

}
