package com.stephentse.asteroids.interactions.collision;

import com.stephentse.asteroids.model.sprites.ISprite;

public interface ICollisionStrategy {
    public boolean intersect(ISprite sprite1, ISprite sprite2);
    public void collision();
}
