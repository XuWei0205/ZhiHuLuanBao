package com.hanyu.zhihuluanbao.commons;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyManager {

	private static VolleyManager volley = null;

	private static RequestQueue queue = null;

	public static VolleyManager getInstance(Context context) {
        if (volley == null || queue == null){
            synchronized (VolleyManager.class){
                if (volley == null || queue == null) {
                    volley = new VolleyManager(context.getApplicationContext());
                }
            }
        }
		return volley;
	}

	private VolleyManager(Context context) {
		queue = Volley.newRequestQueue(context, new OKHttpClientStack());
	}
	
	public RequestQueue getRequestQueue(){
		return queue;
	}

	public void stopVolley() {
		if (queue != null) {
			queue.stop();
		}
		if (volley != null) {
			volley = null;
		}
	}

}
