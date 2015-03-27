package com.stephentse.asteroids.interactions.translation;

import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.Bullet;
import com.stephentse.asteroids.model.sprites.ISprite;
import com.stephentse.asteroids.model.sprites.Player;

public class TranslationStrategyFactory {

    private int _maxWidth = 0;
    private int _maxHeight = 0;

    public TranslationStrategyFactory(int maxWidth, int maxHeight) {
        _maxHeight = maxHeight;
        _maxWidth = maxWidth;
    }

    public synchronized ISpriteTranslationStrategy getTranslationStrategy(ISprite sprite) {
        if (sprite instanceof Asteroid) {
            return new RollOverTranslationStrategy(sprite, _maxWidth, _maxHeight);
        }
        else if (sprite instanceof Player) {
            return new PlayerTranslationStrategy(sprite);
        }
        else if (sprite instanceof Bullet) {
            return new BasicTranslationStrategy(sprite);
        }
        else {
            throw new IllegalArgumentException("No strategy for " + sprite.getClass().getName());

        }
    }
}
