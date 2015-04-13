package com.stephentse.asteroids.system.gameDifficulty;

public class GameDifficulty {

    public static final String FRAME_RATE_KEY = "FRAME_RATE";
    public static final String MULTIPLIER_KEY = "DIFFICULTY_MULTIPLIER";

    public static final int FRAME_RATE_DEFAULT = 50;
    public static final float MULTIPLIER_DEFAULT = 1.0f;

    public enum DifficultyType {
        VerySlow,
        Slow,
        Normal,
        Fast,
    }

    private DifficultyType _type;
    private int _frameRate;
    private float _difficultyMultiplier;

    public GameDifficulty(DifficultyType type, int frameRate, float difficultyMultiplier) {
        _type = type;
        _frameRate = frameRate;
        _difficultyMultiplier = difficultyMultiplier;
    }

    public DifficultyType getDifficultyType() {
        return _type;
    }

    public int getFrameRate() {
        return _frameRate;
    }

    public float getDifficultyMultiplier() {
        return _difficultyMultiplier;
    }
}
