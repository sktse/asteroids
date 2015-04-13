package com.stephentse.asteroids.system;

import android.content.SharedPreferences;

public class Settings {

    private static final String HIGH_SCORE = "Settings::_highScore";
    private static final String NAME = "Settings::_name";
    private static final String USER_ID = "Settings::_userId";
    private static final String DIFFICULTY = "Settings::_difficulty";

    private long _highScore;
    private String _name;
    private String _userId;
    private String _difficulty;

    // Storage and Listener
    private SharedPreferences _storage;

    public Settings(SharedPreferences storage) {
        _storage = storage;

        _highScore = _storage.getLong(HIGH_SCORE, 0);
        _name = _storage.getString(NAME, "you");
        _userId = _storage.getString(USER_ID, null);
        _difficulty = _storage.getString(DIFFICULTY, "Normal");
    }

    public synchronized void setHighScore(long value) {
        _highScore = value;

        _storage.edit().putLong(HIGH_SCORE, _highScore).commit();
    }

    public synchronized long getHighScore() {
        return _highScore;
    }

    public synchronized void setName(String value) {
        _name = value;

        _storage.edit().putString(NAME, _name).commit();
    }

    public synchronized String getName() {
        return _name;
    }

    public synchronized void setUserId(String userId) {
        _userId = userId;

        _storage.edit().putString(USER_ID, _userId).commit();
    }

    public synchronized String getUserId() {
        return _userId;
    }

    public synchronized void setDifficulty(String value) {
        _difficulty = value;

        _storage.edit().putString(DIFFICULTY, _difficulty).commit();
    }

    public synchronized  String getDifficulty() {
        return _difficulty;
    }
}
