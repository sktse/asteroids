package com.stephentse.asteroids.interactions.collision;

import android.graphics.Point;
import android.graphics.Rect;

import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.ISprite;

import java.util.List;

public class AsteroidAsteroidCollisionStrategy implements ICollisionStrategy {

    private int _maxWidth = 0;
    private int _maxHeight = 0;

    private Asteroid _asteroid1;
    private Asteroid _asteroid2;

    private Rect _asteroid1Bounds = null;
    private Rect _asteroid2Bounds = null;

    public AsteroidAsteroidCollisionStrategy(int maxWidth, int maxHeight) {
        _maxWidth = maxWidth;
        _maxHeight = maxHeight;
    }

    @Override
    public boolean intersect(ISprite sprite1, ISprite sprite2) {
        _asteroid1 = (Asteroid) sprite1;
        _asteroid2 = (Asteroid) sprite2;

        List<Rect> sprite1Bounds = CollisionUtilities.expandClippedBounds(_asteroid1, _maxWidth, _maxHeight);
        List<Rect> sprite2Bounds = CollisionUtilities.expandClippedBounds(_asteroid2, _maxWidth, _maxHeight);

        for (int a = 0; a < sprite1Bounds.size(); a++) {
            Rect bounds1 = sprite1Bounds.get(a);
            for (int b = 0; b < sprite2Bounds.size(); b++) {
                Rect bounds2 = sprite2Bounds.get(b);
                if (CollisionUtilities.circularIntersection(bounds1, bounds2)) {
                    _asteroid1Bounds = bounds1;
                    _asteroid2Bounds = bounds2;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void collision() {
        if (_asteroid1Bounds == null || _asteroid2Bounds == null) {
            return;
        }

        Rect intersection = new Rect();
        intersection.setIntersect(_asteroid1Bounds, _asteroid2Bounds);

        int intersectionWidth = intersection.width();
        int intersectionHeight = intersection.height();
        Point asteroidVelocity1 = new Point(_asteroid1.getVelocity());
        Point asteroidVelocity2 = new Point(_asteroid2.getVelocity());

        if (intersectionWidth - intersectionHeight == 0) {
            asteroidVelocity1.negate();
            asteroidVelocity2.negate();
        } else if (intersectionWidth > intersectionHeight) {
            asteroidVelocity1 = new Point(asteroidVelocity1.x, asteroidVelocity1.y * -1);
            asteroidVelocity2 = new Point(asteroidVelocity2.x, asteroidVelocity2.y * -1);
        } else {
            asteroidVelocity1 = new Point(asteroidVelocity1.x * -1, asteroidVelocity1.y);
            asteroidVelocity2 = new Point(asteroidVelocity2.x * -1, asteroidVelocity2.y);
        }

        _asteroid1.setVelocity(asteroidVelocity1);
        _asteroid2.setVelocity(asteroidVelocity2);
    }
}
