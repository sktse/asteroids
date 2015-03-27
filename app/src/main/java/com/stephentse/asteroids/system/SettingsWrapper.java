package com.stephentse.asteroids.system;

import com.stephentse.asteroids.AsteroidsApplication;

public class SettingsWrapper {

    public static long getHighScore() {
        Settings settings = AsteroidsApplication.getSettings();
        return settings.getHighScore();
    }

    public static void setIfHighScore(long highScore) {
        long currentHighScore = getHighScore();

        //the input high score is larger than the existing high score
        if (highScore > currentHighScore) {
            Settings settings = AsteroidsApplication.getSettings();
            settings.setHighScore(highScore);
        }
    }
}
