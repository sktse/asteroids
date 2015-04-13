package com.stephentse.asteroids.api.crashlytics;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

public class CrashlyticsWrapper {

    private static final String LOG_GROUP = "Crashlytics";
    private static final String KEY_NAME = "io.fabric.ApiKey";
    private static boolean isInitialized = false;

    public static String getKey(PackageManager packageManager, String packageName) {
        ApplicationInfo ai = null;
        try {
            ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            //if we are missing or unable to get the Crashlytics key,
            //chances are we will not be able to log it to the cloud anyway.
            return null;
        }
        Bundle bundle = ai.metaData;
        String apiKey = bundle.getString(KEY_NAME);
        return apiKey;
    }

    public static void initialize(Context context) {
        isInitialized = true;
        Fabric.with(context, new Crashlytics());
    }

    public static void logException(String message, Throwable e) {
        if (isInitialized) {
            Crashlytics.log(message);
            Crashlytics.logException(e);
        }
        else {
            Log.e(LOG_GROUP, message);
            Log.e(LOG_GROUP, e.toString());
            e.printStackTrace();
        }
    }

    public static void logException(String message, JSONObject response, Throwable e) {
        String responseString = response == null ? "null" : response.toString();
        if (isInitialized) {
            Crashlytics.log(message);
            Crashlytics.log(responseString);
            Crashlytics.logException(e);
        }
        else {
            Log.e(LOG_GROUP, message);
            Log.e(LOG_GROUP, responseString);
            Log.e(LOG_GROUP, e.toString());
            e.printStackTrace();
        }
    }
}
