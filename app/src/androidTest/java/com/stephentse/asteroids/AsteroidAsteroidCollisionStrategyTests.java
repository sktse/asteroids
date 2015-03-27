package com.stephentse.asteroids;

import android.graphics.Point;
import android.graphics.Rect;

import com.stephentse.asteroids.interactions.collision.AsteroidAsteroidCollisionStrategy;
import com.stephentse.asteroids.model.sprites.Asteroid;

import junit.framework.TestCase;

public class AsteroidAsteroidCollisionStrategyTests extends TestCase {

    private Rect _bounds;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        _bounds = new Rect(0, 0, 10, 10);
    }

    public void testNoCollision() {
        Asteroid asteroid1 = getAsteroid(20, 20, 1, 2);
        Asteroid asteroid2 = getAsteroid(200, 200, -2, -3);

        AsteroidAsteroidCollisionStrategy strategy = new AsteroidAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid1, asteroid2);
        assertFalse("Expected asteroids to not collide", intersect);

        strategy.collision();
        assertEquals("Expected first asteroid velocity to be unchanged", asteroid1.getVelocity(), new Point(1, 2));
        assertEquals("Expected second asteroid velocity to be unchanged", asteroid2.getVelocity(), new Point(-2, -3));
    }

    public void testSquareCollisionButNoIntersection() {
        Asteroid asteroid1 = getAsteroid(20, 20, 1, 2);
        Asteroid asteroid2 = getAsteroid(29, 29, -2, -3);

        AsteroidAsteroidCollisionStrategy strategy = new AsteroidAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid1, asteroid2);
        assertFalse("Expected asteroids to not collide", intersect);

        strategy.collision();
        assertEquals("Expected first asteroid velocity to be unchanged", asteroid1.getVelocity(), new Point(1, 2));
        assertEquals("Expected second asteroid velocity to be unchanged", asteroid2.getVelocity(), new Point(-2, -3));
    }

    public void testIntersectionHorizontal() {
        Asteroid asteroid1 = getAsteroid(20, 20, 1, 2);
        Asteroid asteroid2 = getAsteroid(25, 22, -2, -3);

        AsteroidAsteroidCollisionStrategy strategy = new AsteroidAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid1, asteroid2);
        assertTrue("Expected asteroids to collide", intersect);

        strategy.collision();
        assertEquals("Expected first asteroid velocity to reverse x direction", asteroid1.getVelocity(), new Point(-1, 2));
        assertEquals("Expected second asteroid velocity to reverse x direction", asteroid2.getVelocity(), new Point(2, -3));
    }

    public void testIntersectionVertical() {
        Asteroid asteroid1 = getAsteroid(20, 20, 1, 2);
        Asteroid asteroid2 = getAsteroid(22, 25, -2, -3);

        AsteroidAsteroidCollisionStrategy strategy = new AsteroidAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid1, asteroid2);
        assertTrue("Expected asteroids to collide", intersect);

        strategy.collision();
        assertEquals("Expected first asteroid velocity to reverse y direction", asteroid1.getVelocity(), new Point(1, -2));
        assertEquals("Expected second asteroid velocity to reverse y direction", asteroid2.getVelocity(), new Point(-2, 3));
    }

    public void testIntersectionBoth() {
        Asteroid asteroid1 = getAsteroid(20, 20, 1, 2);
        Asteroid asteroid2 = getAsteroid(22, 22, -2, -3);

        AsteroidAsteroidCollisionStrategy strategy = new AsteroidAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid1, asteroid2);
        assertTrue("Expected asteroids to collide", intersect);

        strategy.collision();
        assertEquals("Expected first asteroid velocity to reverse x and y direction", asteroid1.getVelocity(), new Point(-1, -2));
        assertEquals("Expected second asteroid velocity to reverse x and y direction", asteroid2.getVelocity(), new Point(2, 3));
    }

    public void testIntersectionClipHorizontal() {
        Asteroid asteroid1 = getAsteroid(995, 20, 1, 2);
        Asteroid asteroid2 = getAsteroid(1, 22, -2, -3);

        AsteroidAsteroidCollisionStrategy strategy = new AsteroidAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid1, asteroid2);
        assertTrue("Expected asteroids to collide", intersect);

        strategy.collision();
        assertEquals("Expected first asteroid velocity to reverse x direction", asteroid1.getVelocity(), new Point(-1, 2));
        assertEquals("Expected second asteroid velocity to reverse x direction", asteroid2.getVelocity(), new Point(2, -3));
    }

    public void testIntersectionClipVertical() {
        Asteroid asteroid1 = getAsteroid(20, 995, 1, 2);
        Asteroid asteroid2 = getAsteroid(22, 2, -2, -3);

        AsteroidAsteroidCollisionStrategy strategy = new AsteroidAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid1, asteroid2);
        assertTrue("Expected asteroids to collide", intersect);

        strategy.collision();
        assertEquals("Expected first asteroid velocity to reverse y direction", asteroid1.getVelocity(), new Point(1, -2));
        assertEquals("Expected second asteroid velocity to reverse y direction", asteroid2.getVelocity(), new Point(-2, 3));
    }

    private Asteroid getAsteroid(int x, int y, int velocityX, int velocityY) {
        Asteroid asteroid = new Asteroid();
        asteroid.setPosition(x, y);
        asteroid.setVelocity(velocityX, velocityY);
        asteroid.setBounds(_bounds);
        return asteroid;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
