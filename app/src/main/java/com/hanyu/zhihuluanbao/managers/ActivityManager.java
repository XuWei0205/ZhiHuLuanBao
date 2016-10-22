package com.hanyu.zhihuluanbao.managers;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2016/10/22.
 */
public class ActivityManager {
    public static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity){
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public static void finishAll(){
        for(Activity activity: activityList){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
