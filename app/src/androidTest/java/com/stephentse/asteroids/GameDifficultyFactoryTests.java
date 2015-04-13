package com.stephentse.asteroids;

import com.stephentse.asteroids.system.gameDifficulty.GameDifficulty;
import com.stephentse.asteroids.system.gameDifficulty.GameDifficultyFactory;

import junit.framework.TestCase;

public class GameDifficultyFactoryTests extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testDifficultyTypeVerySlow() {
        GameDifficulty difficulty = GameDifficultyFactory.getDifficulty(GameDifficulty.DifficultyType.VerySlow);
        assertEquals("Expected type to be VerySlow", GameDifficulty.DifficultyType.VerySlow, difficulty.getDifficultyType());
        assertEquals("Expected frame rate to be 40", 40, difficulty.getFrameRate());
        assertEquals("Expected score multiplier to be 0.3", 0.3f, difficulty.getDifficultyMultiplier());
    }

    public void testDifficultyTypeSlow() {
        GameDifficulty difficulty = GameDifficultyFactory.getDifficulty(GameDifficulty.DifficultyType.Slow);
        assertEquals("Expected type to be Slow", GameDifficulty.DifficultyType.Slow, difficulty.getDifficultyType());
        assertEquals("Expected frame rate to be 27", 27, difficulty.getFrameRate());
        assertEquals("Expected score multiplier to be 0.5", 0.5f, difficulty.getDifficultyMultiplier());
    }

    public void testDifficultyTypeNormal() {
        GameDifficulty difficulty = GameDifficultyFactory.getDifficulty(GameDifficulty.DifficultyType.Normal);
        assertEquals("Expected type to be Normal", GameDifficulty.DifficultyType.Normal, difficulty.getDifficultyType());
        assertEquals("Expected frame rate to be 20", 20, difficulty.getFrameRate());
        assertEquals("Expected score multiplier to be 1.0", 1.0f, difficulty.getDifficultyMultiplier());
    }

    public void testDifficultyTypeFast() {
        GameDifficulty difficulty = GameDifficultyFactory.getDifficulty(GameDifficulty.DifficultyType.Fast);
        assertEquals("Expected type to be Fast", GameDifficulty.DifficultyType.Fast, difficulty.getDifficultyType());
        assertEquals("Expected frame rate to be 17", 17, difficulty.getFrameRate());
        assertEquals("Expected score multiplier to be 1.1", 1.1f, difficulty.getDifficultyMultiplier());
    }

    public void testDefaults() {
        assertEquals("Expected default frame rate to be 50", 50, GameDifficulty.FRAME_RATE_DEFAULT);
        assertEquals("Expected default multiplier to be 1.0", 1.0f, GameDifficulty.MULTIPLIER_DEFAULT);
    }

}
