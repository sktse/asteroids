package com.stephentse.asteroids;

import android.graphics.Point;
import android.graphics.Rect;

import com.stephentse.asteroids.interactions.collision.PlayerAsteroidCollisionStrategy;
import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.Player;

import junit.framework.TestCase;

public class PlayerAsteroidCollisionStrategyTests extends TestCase {

    private Rect _hitBox;
    private Rect _touchBox;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _hitBox = new Rect(0, 0, 10, 10);
        _touchBox = new Rect(0, 15, 10, 25);
    }

    public void testNoIntersection() {
        Player player = getPlayer(20, 20, 3);
        Asteroid asteroid = getAsteroid(100, 100, 2, 4);
        assertTrue("Expected player to be enabled for collision tests", player.isEnabled());

        PlayerAsteroidCollisionStrategy strategy = new PlayerAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(player, asteroid);
        assertFalse("Expected player and asteroid to not collide", intersect);
    }

    public void testSquareCollisionButNoIntersection() {
        Player player = getPlayer(20, 20, 3);
        Asteroid asteroid = getAsteroid(100, 100, 2, 4);
        assertTrue("Expected player to be enabled for collision tests", player.isEnabled());

        PlayerAsteroidCollisionStrategy strategy = new PlayerAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid, player);
        assertFalse("Expected player and asteroid to not collide", intersect);
    }

    public void testIntersectionHorizontal() {
        Player player = getPlayer(26, 22, 3);
        Asteroid asteroid = getAsteroid(20, 20, 2, 4);
        assertTrue("Expected player to be enabled for collision tests", player.isEnabled());

        PlayerAsteroidCollisionStrategy strategy = new PlayerAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(player, asteroid);
        assertTrue("Expected player and asteroid to collide", intersect);

        strategy.collision();
        assertEquals("Expected first asteroid velocity to reverse x direction", asteroid.getVelocity(), new Point(-2, 4));
        assertEquals("Expected player to lose 1 hitpoint", 2, player.getHitPoints());
    }

    public void testIntersectionVertical() {
        Player player = getPlayer(20, 20, 3);
        Asteroid asteroid = getAsteroid(22, 26, 2, 4);
        assertTrue("Expected player to be enabled for collision tests", player.isEnabled());

        PlayerAsteroidCollisionStrategy strategy = new PlayerAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid, player);
        assertTrue("Expected player and asteroid to collide", intersect);

        strategy.collision();
        assertEquals("Expected first asteroid velocity to reverse y direction", asteroid.getVelocity(), new Point(2, -4));
        assertEquals("Expected player to lose 1 hitpoint", 2, player.getHitPoints());
    }

    public void testIntersectionBoth() {
        Player player = getPlayer(20, 20, 3);
        Asteroid asteroid = getAsteroid(22, 22, 2, 4);
        assertTrue("Expected player to be enabled for collision tests", player.isEnabled());

        PlayerAsteroidCollisionStrategy strategy = new PlayerAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid, player);
        assertTrue("Expected player and asteroid to collide", intersect);

        strategy.collision();
        assertEquals("Expected first asteroid velocity to reverse x and y direction", asteroid.getVelocity(), new Point(-2, -4));
        assertEquals("Expected player to lose 1 hitpoint", 2, player.getHitPoints());
    }

    public void testIntersectionClipHorizontal() {
        Player player = getPlayer(4, 22, 3);
        Asteroid asteroid = getAsteroid(995, 20, 2, 4);
        assertTrue("Expected player to be enabled for collision tests", player.isEnabled());

        PlayerAsteroidCollisionStrategy strategy = new PlayerAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(player, asteroid);
        assertTrue("Expected player and asteroid to collide", intersect);

        strategy.collision();
        assertEquals("Expected first asteroid velocity to reverse x direction", asteroid.getVelocity(), new Point(-2, 4));
        assertEquals("Expected player to lose 1 hitpoint", 2, player.getHitPoints());
    }

    public void testIntersectionClipVertical() {
        Player player = getPlayer(20, 7, 3);
        Asteroid asteroid = getAsteroid(22, 998, 2, 4);
        assertTrue("Expected player to be enabled for collision tests", player.isEnabled());

        PlayerAsteroidCollisionStrategy strategy = new PlayerAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(asteroid, player);
        assertTrue("Expected player and asteroid to collide", intersect);

        strategy.collision();
        assertEquals("Expected first asteroid velocity to reverse y direction", asteroid.getVelocity(), new Point(2, -4));
        assertEquals("Expected player to lose 1 hitpoint", 2, player.getHitPoints());
    }

    public void testPlayerDisabledCollision() {
        Player player = getPlayer(26, 22, 3);
        player.setEnabled(false);
        Asteroid asteroid = getAsteroid(20, 20, 2, 4);
        assertFalse("Expected player to be disabled for collision tests", player.isEnabled());

        PlayerAsteroidCollisionStrategy strategy = new PlayerAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(player, asteroid);
        assertFalse("Expected player and asteroid to not collide", intersect);
    }

    public void testOnlyTouchBoxCollision() {
        Player player = getPlayer(100, 100, 3);
        Asteroid asteroid = getAsteroid(100, 120, 5, 5);

        boolean touchBoxIntersection = player.getAbsoluteTouchBox().intersect(asteroid.getAbsoluteBounds());
        assertTrue("Expecting player touch box and asteroid to intersect", touchBoxIntersection);

        PlayerAsteroidCollisionStrategy strategy = new PlayerAsteroidCollisionStrategy(1000, 1000);
        boolean intersect = strategy.intersect(player, asteroid);
        assertFalse("Expected player and asteroid to not collide", intersect);
    }

    private Player getPlayer(int x, int y, int hitPoints) {
        Player player = new Player();
        player.setPosition(x, y);
        player.setHitPoints(hitPoints);
        player.setHitBox(_hitBox);
        player.setTouchBox(_touchBox);
        player.setEnabled(true);

        return player;
    }

    private Asteroid getAsteroid(int x, int y, int velocityX, int velocityY) {
        Asteroid asteroid = new Asteroid();
        asteroid.setPosition(x, y);
        asteroid.setVelocity(velocityX, velocityY);
        asteroid.setBounds(_hitBox);
        return asteroid;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
