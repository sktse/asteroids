package com.stephentse.asteroids;

import android.graphics.Point;

import com.stephentse.asteroids.interactions.translation.BasicTranslationStrategy;
import com.stephentse.asteroids.model.sprites.Bullet;

import junit.framework.TestCase;

public class BasicTranslationStrategyTests extends TestCase {

    public void testTranslation() {
        Bullet bullet = getBullet(100, 100, -3, 5);
        BasicTranslationStrategy strategy = new BasicTranslationStrategy(bullet);
        strategy.translate(null);

        assertEquals("Expected the bullet to be at position (97, 105)", new Point(97, 105), bullet.getPosition());
    }

    private Bullet getBullet(int x, int y, int velocityX, int velocityY) {
        Bullet bullet = new Bullet();
        bullet.setPosition(x, y);
        bullet.setVelocity(velocityX, velocityY);
        return bullet;
    }
}
