package com.stephentse.asteroids.system;

import android.content.SharedPreferences;

public class Settings {

    private static final String HIGH_SCORE = "Settings::_highScore";

    private long _highScore;

    // Storage and Listener
    private SharedPreferences _storage;

    public Settings(SharedPreferences storage) {
        _storage = storage;

        _highScore = _storage.getLong(HIGH_SCORE, 0);
    }

    public synchronized void setHighScore(long value) {
        _highScore = value;

        _storage.edit().putLong(HIGH_SCORE, _highScore).commit();
    }

    public synchronized long getHighScore() {
        return _highScore;
    }

}
