package com.hanyu.zhihuluanbao.commons;

import android.os.Environment;

/** 全局数据类，存放一些常量或全局通用的静态变量*/
public class GlobalData {
	public static String APP_ROOT_DIR = Environment.getExternalStorageDirectory().getPath() + "/cApp";
	public static String IMG_DIR = APP_ROOT_DIR + "/image_cache";
	/** glide缓存目录 在BaseApplication中赋值*/
	public static String GLIDE_IMG_DIR ="";
	public static String GLIDE_IMG_DIR_NAME ="/image_cache";

}