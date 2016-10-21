package com.hanyu.zhihuluanbao.managers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hanyu.zhihuluanbao.commons.GsonRequest;
import com.hanyu.zhihuluanbao.commons.VolleyManager;

import java.util.Map;

/**
 * Created by hehuajia on 16/10/20.
 */

public class NetManager {

    public interface ResponseListener<T> {
        void onResponse(T response);

        void onErrorResponse(VolleyError error);

        void onAsyncResponse(T response);
    }

    public static <T> Request<T> doHttpGet(Context context, Map<String, String> headers, String url,
                                           Map<String, String> params,Class clazz,final ResponseListener<T> listener, final int timeout) {
        GsonRequest<T> request = new GsonRequest<T>(Request.Method.GET, url, clazz, headers, params, new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                if (listener != null) {
                    listener.onResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    listener.onErrorResponse(error);
                }
            }
        }, new GsonRequest.AsyncCacheListener<T>() {
            @Override
            public void onAsyncCache(T response) {
                if (listener != null) {
                    listener.onAsyncResponse(response);
                }
            }
        }, timeout);

        VolleyManager.getInstance(context).getRequestQueue().add(request);
        return request;
    }

    public static <T> Request<T> doHttpGet(Context context, Map<String, String> headers, String url,
                                           Map<String, String> params,Class clazz,final ResponseListener<T> listener) {
        return doHttpGet(context, headers, url, params, clazz, listener, 0);
    }

    public static <T> Request<T> doHttpPost(Context context, Map<String, String> headers, String url,
                                           Map<String, String> params,Class clazz,final ResponseListener<T> listener, final int timeout) {
        GsonRequest<T> request = new GsonRequest<T>(Request.Method.POST, url, clazz, headers, params, new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                if (listener != null) {
                    listener.onResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    listener.onErrorResponse(error);
                }
            }
        }, new GsonRequest.AsyncCacheListener<T>() {
            @Override
            public void onAsyncCache(T response) {
                if (listener != null) {
                    listener.onAsyncResponse(response);
                }
            }
        }, timeout);

        VolleyManager.getInstance(context).getRequestQueue().add(request);
        return request;
    }

    public static <T> Request<T> doHttpPost(Context context, Map<String, String> headers, String url,
                                           Map<String, String> params,Class clazz,final ResponseListener<T> listener) {
        return doHttpPost(context, headers, url, params, clazz, listener, 0);
    }
}
