package com.stephentse.asteroids.api.crashlytics;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

public class CrashlyticsWrapper {

    private static final String KEY_NAME = "io.fabric.ApiKey";

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

    public static void logException(String message, Throwable e) {
        Crashlytics.log(message);
        Crashlytics.logException(e);
    }

    public static void logException(String message, JSONObject response, Throwable e) {
        Crashlytics.log(message);
        String responseString = response == null ? "null" : response.toString();
        Crashlytics.log(responseString);
        Crashlytics.logException(e);
    }
}
