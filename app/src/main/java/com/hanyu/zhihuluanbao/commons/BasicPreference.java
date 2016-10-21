package com.hanyu.zhihuluanbao.commons;

import android.content.Context;
import android.content.SharedPreferences;

public class BasicPreference {
    protected Context context;
    protected SharedPreferences settings;
    protected SharedPreferences.Editor editor;

    public BasicPreference(Context context) {
        this.context = context;
        settings = context.getSharedPreferences(getClass().getSimpleName(), 0);
        editor = settings.edit();
    }

    public void registerOnSharePreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        settings.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharePreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        settings.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
