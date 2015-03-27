package com.stephentse.asteroids;

import com.stephentse.asteroids.interactions.translation.BasicTranslationStrategy;
import com.stephentse.asteroids.interactions.translation.RollOverTranslationStrategy;
import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.Bullet;
import com.stephentse.asteroids.model.sprites.Player;
import com.stephentse.asteroids.interactions.translation.ISpriteTranslationStrategy;
import com.stephentse.asteroids.interactions.translation.PlayerTranslationStrategy;
import com.stephentse.asteroids.interactions.translation.TranslationStrategyFactory;

import junit.framework.TestCase;

public class TranslationStrategyFactoryTests extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testPlayerStrategyFactory() {
        Player player = new Player();
        TranslationStrategyFactory factory = new TranslationStrategyFactory(100, 100);
        ISpriteTranslationStrategy strategy = factory.getTranslationStrategy(player);
        assertTrue("Expected TranslationStrategyFactory to create PlayerTranslationStrategy", strategy instanceof PlayerTranslationStrategy);
    }

    public void testAsteroidStrategyFactory() {
        Asteroid asteroid = new Asteroid();
        TranslationStrategyFactory factory = new TranslationStrategyFactory(100, 100);
        ISpriteTranslationStrategy strategy = factory.getTranslationStrategy(asteroid);
        assertTrue("Expected TranslationStrategyFactory to create RollOverTranslationStrategy", strategy instanceof RollOverTranslationStrategy);
    }

    public void testBulletStrategyFactory() {
        Bullet bullet = new Bullet();
        TranslationStrategyFactory factory = new TranslationStrategyFactory(100, 100);
        ISpriteTranslationStrategy strategy = factory.getTranslationStrategy(bullet);
        assertTrue("Expected TranslationStrategyFactory to create BasicTranslationStrategy", strategy instanceof BasicTranslationStrategy);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
