package com.stephentse.asteroids;

import android.graphics.Rect;

import com.stephentse.asteroids.interactions.collision.BulletAsteroidCollisionStrategy;
import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.Bullet;

import junit.framework.TestCase;

public class BulletAsteroidCollisionStrategyTests extends TestCase {

    private Rect _bounds;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _bounds = new Rect(0, 0, 10, 10);
    }

    public void testNoCollision() {
        Asteroid asteroid = getAsteroid(20, 20, 1, 2);
        Bullet bullet = getBullet(100, 100, 25);

        BulletAsteroidCollisionStrategy strategy = new BulletAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid, bullet);
        assertFalse("Expected bullet and asteroid to not collide", intersect);
    }

    public void testSquareCollisionButNoIntersection() {
        Asteroid asteroid = getAsteroid(20, 20, 1, 2);
        Bullet bullet = getBullet(28, 28, 25);

        BulletAsteroidCollisionStrategy strategy = new BulletAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid, bullet);
        assertFalse("Expected bullet and asteroid to not collide", intersect);
    }

    public void testIntersectionHorizontal() {
        Asteroid asteroid = getAsteroid(20, 20, 1, 2);
        Bullet bullet = getBullet(26, 22, 25);

        BulletAsteroidCollisionStrategy strategy = new BulletAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(bullet, asteroid);
        assertTrue("Expected bullet and asteroid to collide", intersect);

        strategy.collision();
        assertFalse("Expected bullet to be disabled after collision", bullet.isEnabled());
        assertEquals("Expected asteroid to be damaged by the bullet", 75, asteroid.getHitPoints());
    }

    public void testIntersectionVertical() {
        Asteroid asteroid = getAsteroid(20, 20, 1, 2);
        Bullet bullet = getBullet(22, 26, 28);

        BulletAsteroidCollisionStrategy strategy = new BulletAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid, bullet);
        assertTrue("Expected bullet and asteroid to collide", intersect);

        strategy.collision();
        assertFalse("Expected bullet to be disabled after collision", bullet.isEnabled());
        assertEquals("Expected asteroid to be damaged by the bullet", 72, asteroid.getHitPoints());
    }

    public void testIntersectionClipHorizontal() {
        Asteroid asteroid = getAsteroid(995, 20, 1, 2);
        Bullet bullet = getBullet(2, 22, 15);

        BulletAsteroidCollisionStrategy strategy = new BulletAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(bullet, asteroid);
        assertTrue("Expected bullet and asteroid to collide", intersect);

        strategy.collision();
        assertFalse("Expected bullet to be disabled after collision", bullet.isEnabled());
        assertEquals("Expected asteroid to be damaged by the bullet", 85, asteroid.getHitPoints());
    }

    public void testIntersectionClipVertical() {
        Asteroid asteroid = getAsteroid(20, 995, 1, 2);
        Bullet bullet = getBullet(22, 2, 8);

        BulletAsteroidCollisionStrategy strategy = new BulletAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid, bullet);
        assertTrue("Expected bullet and asteroid to collide", intersect);

        strategy.collision();
        assertFalse("Expected bullet to be disabled after collision", bullet.isEnabled());
        assertEquals("Expected asteroid to be damaged by the bullet", 92, asteroid.getHitPoints());
    }

    public void testAsteroidDestroyed() {
        Asteroid asteroid = getAsteroid(20, 995, 1, 2);
        Bullet bullet = getBullet(22, 2, 111);

        BulletAsteroidCollisionStrategy strategy = new BulletAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(bullet, asteroid);
        assertTrue("Expected bullet and asteroid to collide", intersect);

        strategy.collision();
        assertFalse("Expected bullet to be disabled after collision", bullet.isEnabled());
        assertEquals("Expected asteroid to be damaged by the bullet", -11, asteroid.getHitPoints());
        assertFalse("Expected asteroid to be disabled after collision", asteroid.isEnabled());
    }

    private Bullet getBullet(int x, int y, int power) {
        Bullet bullet = new Bullet();
        bullet.setPosition(x, y);
        bullet.setPower(power);
        bullet.setBounds(_bounds);
        return bullet;
    }

    private Asteroid getAsteroid(int x, int y, int velocityX, int velocityY) {
        Asteroid asteroid = new Asteroid();
        asteroid.setPosition(x, y);
        asteroid.setVelocity(velocityX, velocityY);
        asteroid.setHitPoints(100);
        asteroid.setBounds(_bounds);
        return asteroid;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
