package com.stephentse.asteroids.interactions.collision;

import com.stephentse.asteroids.model.sprites.ISprite;

public class BulletPlayerCollisionStrategy implements ICollisionStrategy {
    @Override
    public boolean intersect(ISprite sprite1, ISprite sprite2) {
        return false;
    }

    @Override
    public void collision() {

    }
}
