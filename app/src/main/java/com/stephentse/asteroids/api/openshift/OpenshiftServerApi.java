package com.stephentse.asteroids.api.openshift;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class OpenshiftServerApi {

    private static AsyncHttpClient asyncClient = new AsyncHttpClient();
    private HandlerThread _handlerThread;
    private Handler _handler;

    private static OpenshiftServerApi openshiftApi;

    public static synchronized OpenshiftServerApi getApi() {
        if (openshiftApi == null) {
            openshiftApi = new OpenshiftServerApi();
        }
        return openshiftApi;
    }

    public void postScore(String userId, String name, long score, ApiResponseHandler handler) {
        String path = "/score/top";
        String url = getAbsoluteUrl(path);

        RequestParams params = new RequestParams();
        if (userId != null) {
            //user id is optional and an user_id will be created if one is not provided
            params.put("user_id", userId);
        }
        params.put("name", name);
        params.put("score", score);
        post(url, params, handler);
    }

    public void getScores(int limit, ApiResponseHandler handler) {
        String path = "/score/top";
        String url = getAbsoluteUrl(path);

        RequestParams params = new RequestParams();
        params.put("limit", limit);
        get(url, params, handler);
    }


    private OpenshiftServerApi() {
        asyncClient.setUserAgent("Android Asynchronous Http Client/1.4.5 (http://loopj.com/android-async-http/)");
        _handlerThread = new HandlerThread("OpenshiftServerApi", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        _handlerThread.start();
        _handler = new Handler(_handlerThread.getLooper());
    }

    private void get(final String url, final RequestParams params, final ApiResponseHandler handler) {
        if (Looper.myLooper() != null) {
            asyncClient.get(url, params, handler.getHandler());
        }
        else {
            _handler.post(new Runnable() {

                @Override
                public void run() {
                    asyncClient.get(url, params, handler.getHandler());
                }
            });
        }
    }

    private void post(final String url, final RequestParams params, final ApiResponseHandler handler) {
        if (Looper.myLooper() != null) {
            asyncClient.post(url, params, handler.getHandler());
        }
        else {
            _handler.post(new Runnable() {

                @Override
                public void run() {
                    asyncClient.post(url, params, handler.getHandler());
                }
            });
        }
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return "https://asteroidsapi-stephentse.rhcloud.com" + relativeUrl;
    }
}
