package com.stephentse.asteroids.interactions.collision;

import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.Bullet;
import com.stephentse.asteroids.model.sprites.ISprite;
import com.stephentse.asteroids.model.sprites.Player;

public class CollisionStrategyFactory {

    private int _maxWidth = 0;
    private int _maxHeight = 0;

    public CollisionStrategyFactory(int maxWidth, int maxHeight) {
        _maxWidth = maxWidth;
        _maxHeight = maxHeight;
    }

    public ICollisionStrategy getCollisionStrategy(ISprite sprite1, ISprite sprite2) {
        if (sprite1 instanceof Asteroid && sprite2 instanceof Asteroid) {
            return new AsteroidAsteroidCollisionStrategy(_maxWidth, _maxHeight);
        } else if ((sprite1 instanceof Asteroid && sprite2 instanceof Player) ||
                   (sprite2 instanceof Asteroid && sprite1 instanceof Player)) {
            return new PlayerAsteroidCollisionStrategy(_maxWidth, _maxHeight);
        } else if ((sprite1 instanceof Bullet && sprite2 instanceof Asteroid) ||
                   (sprite2 instanceof Bullet && sprite1 instanceof Asteroid)) {
            return new BulletAsteroidCollisionStrategy(_maxWidth, _maxHeight);
        } else if ((sprite1 instanceof Bullet && sprite2 instanceof Player) ||
                   (sprite2 instanceof Bullet && sprite1 instanceof Player)) {
            return new BulletPlayerCollisionStrategy();
        } else {
            throw new IllegalArgumentException(
                    "No strategy for: " + sprite1.getClass().getName() + " & " +
                                          sprite2.getClass().getName());
        }
    }
}
