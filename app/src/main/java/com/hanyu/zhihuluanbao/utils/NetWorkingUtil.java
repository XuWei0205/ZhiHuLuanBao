package com.hanyu.zhihuluanbao.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Dell on 2016/11/18.
 */
public class NetWorkingUtil {
    public static final int NO_CONNECTION = 0;
    public static final int WIFI = 1;
    public static final int MOBILE_DATA = 2;

    public int getConnection(Context context){
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State state1 = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(state1 == NetworkInfo.State.CONNECTED || state1  == NetworkInfo.State.CONNECTING){
            return WIFI;
        }

        NetworkInfo.State state2 =manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if( state2 == NetworkInfo.State.CONNECTED || state2 == NetworkInfo.State.CONNECTING ){
            return MOBILE_DATA;

        }
        return NO_CONNECTION;
    }
}
