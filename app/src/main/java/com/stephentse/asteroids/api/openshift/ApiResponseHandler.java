package com.stephentse.asteroids.api.openshift;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class ApiResponseHandler {

    private WeakReference<ICaller> _caller;
    private InnerApiResponseHandler _handler;

    public ApiResponseHandler(ICaller caller, InnerApiResponseHandler handler) {
        _caller = new WeakReference<ICaller>(caller);
        _handler = handler;
    }

    private boolean active() {
        ICaller caller = _caller.get();
        if (caller == null) {
            return false;
        }

        if (!caller.active()) {
            return false;
        }

        return true;
    }

    public JsonHttpResponseHandler getHandler() {
        return new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (!active()) {
                    return;
                }

                _handler.onSuccess(response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (!active()) {
                    return;
                }

                _handler.onSuccess(response);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (!active()) {
                    return;
                }

                _handler.onFailure(throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        };
    }

}
