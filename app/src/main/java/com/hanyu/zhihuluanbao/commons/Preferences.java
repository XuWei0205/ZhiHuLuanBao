package com.hanyu.zhihuluanbao.commons;

import android.content.Context;

import com.hanyu.zhihuluanbao.commons.BasicPreference;

public class Preferences extends BasicPreference {
	private static Preferences instance;

	public static Preferences getInstance(Context context) {
		if (instance == null) {
			synchronized (Preferences.class) {
				if (instance == null) {
					instance = new Preferences(context.getApplicationContext());
				}
			}
		}
		return instance;
	}


	private Preferences(Context context) {
		super(context);
	}

	/** 这对方法在2.3以后，调用editor.apply();异步写入preference，效率更高*/
	public void setIsAutoUpdate(boolean autoUpdate) {
		editor.putBoolean("auto_update", autoUpdate);
		save();
	}
	public boolean isAutoUpdate() {
		return settings.getBoolean("auto_update", false);
	}

	/** 这对方法仅调用editor.commit(); 同步写入preference，业务上必须同步操作才使用*/
	public void setIsFistIn(boolean isFistIn) {
		editor.putBoolean("is_first_in", isFistIn);
		editor.commit();
	}
	public boolean isFistIn() {
		return settings.getBoolean("is_first_in", false);
	}

	public void setWelcomePageInfo(String pageInfo) {
		editor.putString("wel_page_info", pageInfo);
		save();
	}
	public String getWelcomePageInfo() {
		return settings.getString("auto_update", "");
	}


	private void save(){
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
			editor.apply();
		} else {
			editor.commit();
		}
	}

}
