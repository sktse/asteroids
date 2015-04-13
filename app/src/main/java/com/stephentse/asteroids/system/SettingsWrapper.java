package com.stephentse.asteroids.system;

import com.stephentse.asteroids.AsteroidsApplication;
import com.stephentse.asteroids.system.gameDifficulty.GameDifficulty;

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

    public static boolean isHighScore(long highScore) {
        long currentHighScore = getHighScore();

        return highScore > currentHighScore;
    }

    public static GameDifficulty.DifficultyType getGameDifficulty() {
        Settings settings = AsteroidsApplication.getSettings();
        return GameDifficulty.DifficultyType.valueOf(settings.getDifficulty());
    }

    public static void setGameDifficulty(GameDifficulty.DifficultyType type) {
        Settings settings = AsteroidsApplication.getSettings();
        settings.setDifficulty(type.name());
    }
}
