package com.stephentse.asteroids;

import android.app.Application;
import android.preference.PreferenceManager;

import com.crashlytics.android.Crashlytics;
import com.stephentse.asteroids.api.crashlytics.CrashlyticsWrapper;
import com.stephentse.asteroids.system.GameStatus;
import com.stephentse.asteroids.system.Settings;
import io.fabric.sdk.android.Fabric;

public class AsteroidsApplication extends Application {

    private static AsteroidsApplication _singleton;
    private static Settings _settings;
    private static GameStatus _status;

    public AsteroidsApplication() {
        super();
        _singleton = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String crashlyticsKey = CrashlyticsWrapper.getKey(getPackageManager(), getPackageName());
        if (crashlyticsKey != null && !crashlyticsKey.isEmpty()) {
            //if we have a crashlytics key, then initialize it
            //when Crashlytics starts, it will log an informational under the 'Fabric' key
            Fabric.with(this, new Crashlytics());
        }

        if (_settings == null) {
            _settings = new Settings(PreferenceManager.getDefaultSharedPreferences(this));
        }

        if (_status == null) {
            _status = new GameStatus();
        }
    }

    public static AsteroidsApplication getInstance() {
        return _singleton;
    }

    public static Settings getSettings() {
        return _settings;
    }

    public static GameStatus getGameStatus() {
        return _status;
    }
}
