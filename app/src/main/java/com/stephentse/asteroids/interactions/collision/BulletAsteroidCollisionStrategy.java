package com.stephentse.asteroids.interactions.collision;

import android.graphics.Rect;

import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.Bullet;
import com.stephentse.asteroids.model.sprites.ISprite;

import java.util.List;

public class BulletAsteroidCollisionStrategy implements ICollisionStrategy {

    private int _maxWidth = 0;
    private int _maxHeight = 0;

    private Bullet _bullet;
    private Asteroid _asteroid;

    public BulletAsteroidCollisionStrategy(int maxWidth, int maxHeight) {
        _maxWidth = maxWidth;
        _maxHeight = maxHeight;
    }

    @Override
    public boolean intersect(ISprite sprite1, ISprite sprite2) {
        _bullet = sprite1 instanceof Bullet ? (Bullet) sprite1 : (Bullet) sprite2;
        _asteroid = sprite1 instanceof Asteroid ? (Asteroid) sprite1 : (Asteroid) sprite2;

        List<Rect> asteroidBounds = CollisionUtilities.expandClippedBounds(_asteroid, _maxWidth, _maxHeight);
        Rect bulletBounds = _bullet.getAbsoluteBounds();
        for (int i = 0; i < asteroidBounds.size(); i++) {
            Rect bounds = asteroidBounds.get(i);
            if (CollisionUtilities.circularIntersection(bounds, bulletBounds)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void collision() {
        _asteroid.damage(_bullet.getPower());
        _bullet.setEnabled(false);

        if (_asteroid.getHitPoints() <= 0) {
            //if asteroid has run out of hit points, mark is as disabled
            _asteroid.setEnabled(false);
        }
    }
}
