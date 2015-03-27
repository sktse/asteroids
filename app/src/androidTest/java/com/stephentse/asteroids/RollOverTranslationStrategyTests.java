package com.stephentse.asteroids;

import android.graphics.Point;
import android.graphics.Rect;

import com.stephentse.asteroids.interactions.translation.RollOverTranslationStrategy;
import com.stephentse.asteroids.model.sprites.Asteroid;

import junit.framework.TestCase;

public class RollOverTranslationStrategyTests extends TestCase {

    private Rect _bounds;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _bounds = new Rect(0, 0, 10, 10);
    }

    public void testNoRollOverTranslation() {
        Asteroid asteroid = getAsteroid(100, 200, 2, -4, 200, 5);

        assertEquals("Expected asteroid position to be (100, 200)", new Point(100, 200), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (2, -4)", new Point(2, -4), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 200", 200, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be 5", 5, asteroid.getRotationDelta());

        RollOverTranslationStrategy strategy = new RollOverTranslationStrategy(asteroid, 1000, 1000);
        strategy.translate(null);

        assertEquals("Expected asteroid position to be (102, 196)", new Point(102, 196), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (2, -4)", new Point(2, -4), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 205", 205, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be 5", 5, asteroid.getRotationDelta());
    }

    public void testNoRollOverTranslationRotateOver360() {
        Asteroid asteroid = getAsteroid(100, 200, 2, -4, 340, 60);

        assertEquals("Expected asteroid position to be (100, 200)", new Point(100, 200), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (2, -4)", new Point(2, -4), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 340", 340, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be 60", 60, asteroid.getRotationDelta());

        RollOverTranslationStrategy strategy = new RollOverTranslationStrategy(asteroid, 1000, 1000);
        strategy.translate(null);

        assertEquals("Expected asteroid position to be (102, 196)", new Point(102, 196), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (2, -4)", new Point(2, -4), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 40", 40, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be 60", 60, asteroid.getRotationDelta());
    }

    public void testNoRollOverTranslationRotateUnder0() {
        Asteroid asteroid = getAsteroid(100, 200, 2, -4, 10, -60);

        assertEquals("Expected asteroid position to be (100, 200)", new Point(100, 200), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (2, -4)", new Point(2, -4), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 10", 10, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be -60", -60, asteroid.getRotationDelta());

        RollOverTranslationStrategy strategy = new RollOverTranslationStrategy(asteroid, 1000, 1000);
        strategy.translate(null);

        assertEquals("Expected asteroid position to be (102, 196)", new Point(102, 196), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (2, -4)", new Point(2, -4), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 310", 310, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be -60", -60, asteroid.getRotationDelta());
    }

    public void testNoRollOverTranslationRotateTo0() {
        Asteroid asteroid = getAsteroid(100, 200, 2, -4, 355, 5);

        assertEquals("Expected asteroid position to be (100, 200)", new Point(100, 200), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (2, -4)", new Point(2, -4), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 355", 355, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be 5", 5, asteroid.getRotationDelta());

        RollOverTranslationStrategy strategy = new RollOverTranslationStrategy(asteroid, 1000, 1000);
        strategy.translate(null);

        assertEquals("Expected asteroid position to be (102, 196)", new Point(102, 196), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (2, -4)", new Point(2, -4), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 0", 0, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be 5", 5, asteroid.getRotationDelta());
    }

    public void testRollOverRightTranslation() {
        Asteroid asteroid = getAsteroid(100, 997, -2, 4, 90, 16);

        assertEquals("Expected asteroid position to be (100, 997)", new Point(100, 997), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (-2, 4)", new Point(-2, 4), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 90", 90, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be 16", 16, asteroid.getRotationDelta());

        RollOverTranslationStrategy strategy = new RollOverTranslationStrategy(asteroid, 1000, 1000);
        strategy.translate(null);

        assertEquals("Expected asteroid position to be (98, 1)", new Point(98, 1), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (-2, 4)", new Point(-2, 4), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 106", 106, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be 16", 16, asteroid.getRotationDelta());
    }

    public void testRollOverLeftTranslation() {
        Asteroid asteroid = getAsteroid(100, 30, 2, -40, 255, -14);

        assertEquals("Expected asteroid position to be (100, 30)", new Point(100, 30), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (2, -40)", new Point(2, -40), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 255", 255, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be -14", -14, asteroid.getRotationDelta());

        RollOverTranslationStrategy strategy = new RollOverTranslationStrategy(asteroid, 1000, 1000);
        strategy.translate(null);

        assertEquals("Expected asteroid position to be (102, 990)", new Point(102, 990), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (2, -40)", new Point(2, -40), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 241", 241, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be -14", -14, asteroid.getRotationDelta());
    }

    public void testRollOverTopTranslation() {
        Asteroid asteroid = getAsteroid(25, 654, -40, 3, 359, -14);

        assertEquals("Expected asteroid position to be (25, 654)", new Point(25, 654), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (-40, 3)", new Point(-40, 3), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 359", 359, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be -14", -14, asteroid.getRotationDelta());

        RollOverTranslationStrategy strategy = new RollOverTranslationStrategy(asteroid, 1000, 1000);
        strategy.translate(null);

        assertEquals("Expected asteroid position to be (985, 657)", new Point(985, 657), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (-40, 3)", new Point(-40, 3), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 345", 345, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be -14", -14, asteroid.getRotationDelta());
    }

    public void testRollOverBottomTranslation() {
        Asteroid asteroid = getAsteroid(992, 412, 20, -12, 44, 3);

        assertEquals("Expected asteroid position to be (992, 412)", new Point(992, 412), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (20, -12)", new Point(20, -12), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 44", 44, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be 3", 3, asteroid.getRotationDelta());

        RollOverTranslationStrategy strategy = new RollOverTranslationStrategy(asteroid, 1000, 1000);
        strategy.translate(null);

        assertEquals("Expected asteroid position to be (12, 400)", new Point(12, 400), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (20, -12)", new Point(20, -12), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 47", 47, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be 3", 3, asteroid.getRotationDelta());
    }

    public void testRollOverLeftAndBottomTranslation() {
        Asteroid asteroid = getAsteroid(985, 4, 25, -16, 180, 22);

        assertEquals("Expected asteroid position to be (985, 4)", new Point(985, 4), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (25, -16)", new Point(25, -16), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 180", 180, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be 22", 22, asteroid.getRotationDelta());

        RollOverTranslationStrategy strategy = new RollOverTranslationStrategy(asteroid, 1000, 1000);
        strategy.translate(null);

        assertEquals("Expected asteroid position to be (10, 988)", new Point(10, 988), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (25, -16)", new Point(25, -16), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 202", 202, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be 22", 22, asteroid.getRotationDelta());
    }

    public void testRollOverRightAndTopTranslation() {
        Asteroid asteroid = getAsteroid(15, 970, -30, 38, 255, -25);

        assertEquals("Expected asteroid position to be (15, 970)", new Point(15, 970), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (-30, 38)", new Point(-30, 38), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 255", 255, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be -25", -25, asteroid.getRotationDelta());

        RollOverTranslationStrategy strategy = new RollOverTranslationStrategy(asteroid, 1000, 1000);
        strategy.translate(null);

        assertEquals("Expected asteroid position to be (985, 8)", new Point(985, 8), asteroid.getPosition());
        assertEquals("Expected asteroid velocity to be (-30, 38)", new Point(-30, 38), asteroid.getVelocity());
        assertEquals("Expected asteroid rotation to be 230", 230, asteroid.getRotation());
        assertEquals("Expected asteroid rotation delta to be -25", -25, asteroid.getRotationDelta());
    }

    private Asteroid getAsteroid(int x, int y, int velocityX, int velocityY, int angle, int rotationDelta) {
        Asteroid asteroid = new Asteroid();
        asteroid.setPosition(x, y);
        asteroid.setVelocity(velocityX, velocityY);
        asteroid.setRotation(angle);
        asteroid.setRotationDelta(rotationDelta);
        asteroid.setBounds(_bounds);
        return asteroid;
    }
}
