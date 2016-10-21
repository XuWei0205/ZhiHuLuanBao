package com.hanyu.zhihuluanbao.commons;


import com.android.volley.AuthFailureError;
//import com.android.volley.DEBUG;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hanyu.zhihuluanbao.utils.CLog;
import com.hanyu.zhihuluanbao.utils.Debug;


import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * Gson Request
 */
public class GsonRequest<T> extends Request<T> {

    private final Gson mGson = new Gson();
    private final Class<T> mClazz;
    private final Response.Listener<T> mListener;
    private final Map<String, String> params;
    private final Map<String, String> mHeaders;
    private final AsyncCacheListener<T> asynCacheListenner;

    /**
     * @param method             Method.POST or Method.GET
     * @param url                http url
     * @param clazz              resp class
     * @param listener           resp listener
     * @param errorListener      error listener
     * @param asynCacheListenner async cache listener
     */
    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener, AsyncCacheListener<T> asynCacheListenner, int timeout) {
        this(method, url, clazz, headers, null, listener, errorListener, asynCacheListenner, timeout);
    }

    /**
     * @param method             Method.POST or Method.GET
     * @param url                http url
     * @param clazz              resp class
     * @param params             http params
     * @param listener           resp listener
     * @param errorListener      error listener
     * @param asynCacheListenner async cache listener
     */
    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Map<String, String> params,
                       Response.Listener<T> listener, Response.ErrorListener errorListener, AsyncCacheListener<T> asynCacheListenner, int timeout) {
        super(method, url, errorListener);
        this.mClazz = clazz;
        this.mHeaders = headers;
        this.params = params;
        this.mListener = listener;
        this.asynCacheListenner = asynCacheListenner;
        if (timeout > 0) {
            setRetryPolicy(new DefaultRetryPolicy(timeout, 1, 1f));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (getMethod() == Method.GET) {
            return null;
        }
        return params;
    }

    @Override
    public String getCacheKey() {
        // post请求缓存key改为url+params
        if (getMethod() == Method.POST) {
            try {
                StringBuilder param = new StringBuilder();
                Map<String, String> p = getParams();
                if (p != null && p.size() > 0) {
                    param.append(new String(getBody()));
                    if (param.length() > 0) {
                        param.deleteCharAt(param.length() - 1);
                    }
                    return super.getCacheKey() + param.toString();
                }
            } catch (AuthFailureError authFailureError) {
                authFailureError.printStackTrace();
            }
        }
        return super.getCacheKey();
    }


    private byte[] getPara(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(entry.getKey());
                encodedParams.append('=');
                encodedParams.append(entry.getValue());
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    @Override
    public String getBodyContentType() {
        return super.getBodyContentType();
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, "UTF-8");
            CLog.i("parseNetworkResponse ---> :" + json);
            T result = mGson.fromJson(json, mClazz);
            if (result == null) {
                return Response.error(new ParseError(new Exception("Net Error")));
            }

            try {
                if (asynCacheListenner != null) {
                    asynCacheListenner.onAsyncCache(result);
                }
            } catch (IllegalArgumentException e) {
                if (Debug.DEVELOP_MODE) {
                    e.printStackTrace();
                }
            }

            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            if (Debug.DEVELOP_MODE) {
                e.printStackTrace();
            }
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            if (Debug.DEVELOP_MODE) {
                e.printStackTrace();
            }
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            if (Debug.DEVELOP_MODE) {
                e.printStackTrace();
            }
            return Response.error(new ParseError(e));
        } finally {

        }
    }

    public interface AsyncCacheListener<T> {
        public void onAsyncCache(T response);
    }


}


