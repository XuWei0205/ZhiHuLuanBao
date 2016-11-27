package com.hanyu.zhihuluanbao.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Dell on 2016/11/27.
 */
public class Time {
    private SimpleDateFormat dateFormat;
    private Calendar now;
    public String getDate(){
        String date;
        dateFormat = new SimpleDateFormat("yyyyMMdd");
        now = Calendar.getInstance();
        date = dateFormat.format(now.getTime());
        return date;
    }
}
