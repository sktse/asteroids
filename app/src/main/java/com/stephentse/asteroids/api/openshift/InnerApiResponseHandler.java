package com.stephentse.asteroids.api.openshift;

import org.json.JSONArray;
import org.json.JSONObject;

public class InnerApiResponseHandler {
    public void onSuccess(JSONObject result) {}
    public void onSuccess(JSONArray result) {}
    public void onFailure(Throwable e, JSONObject response) {
        onFailure(e);
    }
    public void onFailure(Throwable e) {}
}