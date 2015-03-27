package com.stephentse.asteroids.interactions.translation;

import com.stephentse.asteroids.model.sprites.ISprite;

import java.util.HashMap;

public interface ISpriteTranslationStrategy {
    public void translate(HashMap<Integer, ISprite> sprites);
}
