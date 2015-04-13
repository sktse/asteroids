package com.stephentse.asteroids.system.gameDifficulty;

public class GameDifficultyFactory {

    /*
        Difficulty Specifications

            Type    |  Frame Rate  |  Multiplier
        ------------+--------------+------------
          Very Slow | 40 (25 fps)  |     0.3
            Slow    | 27 (37.5 fps)|     0.5
           Normal   | 20 (50 fps)  |     1.0
            Fast    | 17 (60 fps)  |     1.1
     */

    /**
     * Gets the frame rate and the score multiplier for a given difficulty rating
     * @param type The difficulty rating
     * @return The GameDifficulty object containing the frame rate and score multiplier
     */
    public static GameDifficulty getDifficulty(GameDifficulty.DifficultyType type) {
        switch(type) {
            case VerySlow:
                return new GameDifficulty(type, 40, 0.3f);
            case Slow:
                return new GameDifficulty(type, 27, 0.5f);
            case Normal:
                return new GameDifficulty(type, 20, 1.0f);
            case Fast:
                return new GameDifficulty(type, 17, 1.1f);
            default:
                throw new IllegalArgumentException("Unknown difficulty type: " + type.toString());
        }
    }
}
