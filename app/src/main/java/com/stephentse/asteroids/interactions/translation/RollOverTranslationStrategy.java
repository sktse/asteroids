package com.stephentse.asteroids.interactions.translation;

import android.graphics.Point;

import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.ISprite;

import java.util.HashMap;

/*
 * Roll over translation strategy is the algorithm for sprites
 * that will roll over to the opposite side when clipping the sides
 */
public class RollOverTranslationStrategy implements ISpriteTranslationStrategy {

    private ISprite _sprite;
    private int _maxWidth;
    private int _maxHeight;

    public RollOverTranslationStrategy(ISprite sprite, int maxWidth, int maxHeight) {
        _sprite = sprite;
        _maxWidth = maxWidth;
        _maxHeight = maxHeight;
    }

    @Override
    public void translate(HashMap<Integer, ISprite> sprites) {
        rollOverTranslation(_sprite, _maxWidth, _maxHeight);

        if (_sprite instanceof Asteroid) {
            Asteroid asteroid = (Asteroid) _sprite;
            asteroid.rotate(asteroid.getRotationDelta());
        }
    }

    private void rollOverTranslation(ISprite sprite, int maxWidth, int maxHeight) {
        Point spritePosition = sprite.getPosition();

        spritePosition.x = spritePosition.x + sprite.getVelocity().x;
        spritePosition.y = spritePosition.y + sprite.getVelocity().y;

        if (spritePosition.x >= maxWidth || spritePosition.x < 0) {
            if (spritePosition.x < 0) {
                spritePosition.x += maxWidth;
            } else {
                spritePosition.x -= maxWidth;
            }
        }

        if (spritePosition.y >= maxHeight || spritePosition.y < 0) {
            if (spritePosition.y < 0) {
                spritePosition.y += maxHeight;
            } else {
                spritePosition.y -= maxHeight;
            }
        }

        sprite.setPosition(spritePosition);
    }
}
