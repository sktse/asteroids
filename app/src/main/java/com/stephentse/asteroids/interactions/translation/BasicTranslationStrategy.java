package com.stephentse.asteroids.interactions.translation;

import android.graphics.Point;

import com.stephentse.asteroids.model.sprites.ISprite;

import java.util.HashMap;

/*
 * Basic translation strategy is the algorithm for sprites
 * that just move in the direction of their velocity
 */
public class BasicTranslationStrategy implements ISpriteTranslationStrategy {

    private ISprite _sprite;

    public BasicTranslationStrategy(ISprite sprite) {
        _sprite = sprite;
    }

    @Override
    public void translate(HashMap<Integer, ISprite> sprites) {
        Point position = _sprite.getPosition();
        Point velocity = _sprite.getVelocity();
        Point newPosition = new Point(position.x + velocity.x,
                position.y + velocity.y);

        _sprite.setPosition(newPosition);
    }
}
