package com.stephentse.asteroids.interactions.collision;

import android.graphics.Point;
import android.graphics.Rect;

import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.ISprite;
import com.stephentse.asteroids.model.sprites.Player;

import java.util.List;

public class PlayerAsteroidCollisionStrategy implements ICollisionStrategy {

    private int _maxWidth = 0;
    private int _maxHeight = 0;

    private Player _player;
    private Asteroid _asteroid;

    private Rect _playerBounds = null;
    private Rect _asteroidBounds = null;

    public PlayerAsteroidCollisionStrategy(int maxWidth, int maxHeight) {
        _maxWidth = maxWidth;
        _maxHeight = maxHeight;
    }

    @Override
    public boolean intersect(ISprite sprite1, ISprite sprite2) {
        _player = sprite1 instanceof Player ? (Player) sprite1 : (Player) sprite2;
        _asteroid = sprite1 instanceof Asteroid ? (Asteroid) sprite1 : (Asteroid) sprite2;

        if (!_player.isEnabled()) {
            return false;
        }

        Rect playerAbsoluteBounds = _player.getAbsoluteBounds();
        List<Rect> asteroidAbsoluteBounds = CollisionUtilities.expandClippedBounds(_asteroid, _maxWidth, _maxHeight);

        for (int i = 0; i < asteroidAbsoluteBounds.size(); i++) {
            Rect asteroidBounds = asteroidAbsoluteBounds.get(i);
            if (CollisionUtilities.circularIntersection(playerAbsoluteBounds, asteroidBounds)) {
                _playerBounds = playerAbsoluteBounds;
                _asteroidBounds = asteroidBounds;
                return true;
            }
        }
        return false;
    }

    @Override
    public void collision() {
        if (_playerBounds == null || _asteroidBounds == null) {
            return;
        }

        Rect intersection = new Rect();
        intersection.setIntersect(_playerBounds, _asteroidBounds);

        int intersectionWidth = intersection.width();
        int intersectionHeight = intersection.height();
        Point velocity = new Point(_asteroid.getVelocity());

        if (intersectionWidth - intersectionHeight == 0) {
            velocity.negate();
        } else if (intersectionWidth > intersectionHeight) {
            velocity = new Point(velocity.x, velocity.y * -1);
        } else {
            velocity = new Point(velocity.x * -1, velocity.y);
        }

        _asteroid.setVelocity(velocity);
        _player.damage(1);
    }
}
