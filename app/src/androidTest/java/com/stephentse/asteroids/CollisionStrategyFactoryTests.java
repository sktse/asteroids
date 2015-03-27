package com.stephentse.asteroids;

import com.stephentse.asteroids.interactions.collision.AsteroidAsteroidCollisionStrategy;
import com.stephentse.asteroids.interactions.collision.BulletAsteroidCollisionStrategy;
import com.stephentse.asteroids.interactions.collision.BulletPlayerCollisionStrategy;
import com.stephentse.asteroids.interactions.collision.CollisionStrategyFactory;
import com.stephentse.asteroids.interactions.collision.ICollisionStrategy;
import com.stephentse.asteroids.interactions.collision.PlayerAsteroidCollisionStrategy;
import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.Bullet;
import com.stephentse.asteroids.model.sprites.Player;

import junit.framework.TestCase;

public class CollisionStrategyFactoryTests extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testAsteroidAsteroidCollisionStrategyFactory() {
        Asteroid asteroid1 = new Asteroid();
        Asteroid asteroid2 = new Asteroid();
        CollisionStrategyFactory factory = new CollisionStrategyFactory(100, 100);
        ICollisionStrategy strategy = factory.getCollisionStrategy(asteroid1, asteroid2);

        assertTrue("Expected CollisionStrategyFactory to create AsteroidAsteroidCollisionStrategy", strategy instanceof AsteroidAsteroidCollisionStrategy);
    }

    public void testPlayerAsteroidCollisionStrategyFactory() {
        Player player = new Player();
        Asteroid asteroid = new Asteroid();
        CollisionStrategyFactory factory = new CollisionStrategyFactory(100, 100);
        ICollisionStrategy strategy = factory.getCollisionStrategy(player, asteroid);

        assertTrue("Expected CollisionStrategyFactory to create PlayerAsteroidCollisionStrategy", strategy instanceof PlayerAsteroidCollisionStrategy);
    }

    public void testReversePlayerAsteroidCollisionStrategyFactory() {
        Player player = new Player();
        Asteroid asteroid = new Asteroid();
        CollisionStrategyFactory factory = new CollisionStrategyFactory(100, 100);
        ICollisionStrategy strategy = factory.getCollisionStrategy(asteroid, player);

        assertTrue("Expected CollisionStrategyFactory to create PlayerAsteroidCollisionStrategy", strategy instanceof PlayerAsteroidCollisionStrategy);
    }

    public void testBulletAsteroidCollisionStrategyFactory() {
        Bullet bullet = new Bullet();
        Asteroid asteroid = new Asteroid();
        CollisionStrategyFactory factory = new CollisionStrategyFactory(100, 100);
        ICollisionStrategy strategy = factory.getCollisionStrategy(bullet, asteroid);

        assertTrue("Expected CollisionStrategyFactory to create BulletAsteroidCollisionStrategy", strategy instanceof BulletAsteroidCollisionStrategy);
    }

    public void testReverseBulletAsteroidCollisionStrategyFactory() {
        Bullet bullet = new Bullet();
        Asteroid asteroid = new Asteroid();
        CollisionStrategyFactory factory = new CollisionStrategyFactory(100, 100);
        ICollisionStrategy strategy = factory.getCollisionStrategy(asteroid, bullet);

        assertTrue("Expected CollisionStrategyFactory to create BulletAsteroidCollisionStrategy", strategy instanceof BulletAsteroidCollisionStrategy);
    }

    public void testBulletPlayerCollisionStrategyFactory() {
        Bullet bullet = new Bullet();
        Player player = new Player();
        CollisionStrategyFactory factory = new CollisionStrategyFactory(100, 100);
        ICollisionStrategy strategy = factory.getCollisionStrategy(bullet, player);

        assertTrue("Expected CollisionStrategyFactory to create BulletPlayerCollisionStrategy", strategy instanceof BulletPlayerCollisionStrategy);
    }

    public void testReverseBulletPlayerCollisionStrategyFactory() {
        Bullet bullet = new Bullet();
        Player player = new Player();
        CollisionStrategyFactory factory = new CollisionStrategyFactory(100, 100);
        ICollisionStrategy strategy = factory.getCollisionStrategy(player, bullet);

        assertTrue("Expected CollisionStrategyFactory to create BulletAsteroidCollisionStrategy", strategy instanceof BulletPlayerCollisionStrategy);
    }

    public void testBulletBulletCollisionStrategyFactory() {
        String exceptionMessage = "";
        String expectedExceptionMessage = "No strategy for: com.stephentse.asteroids.model.sprites.Bullet & com.stephentse.asteroids.model.sprites.Bullet";

        Bullet bullet1 = new Bullet();
        Bullet bullet2 = new Bullet();
        CollisionStrategyFactory factory = new CollisionStrategyFactory(100, 100);

        try {
            ICollisionStrategy strategy = factory.getCollisionStrategy(bullet1, bullet2);
        }
        catch (IllegalArgumentException iae) {
            exceptionMessage = iae.getMessage();
        }

        assertEquals("Expected CollisionStrategyFactory to throw expected IllegalArgumentException", expectedExceptionMessage, exceptionMessage);
    }

    public void testPlayerPlayerCollisionStrategyFactory() {
        String exceptionMessage = "";
        String expectedExceptionMessage = "No strategy for: com.stephentse.asteroids.model.sprites.Player & com.stephentse.asteroids.model.sprites.Player";

        Player player1 = new Player();
        Player player2 = new Player();
        CollisionStrategyFactory factory = new CollisionStrategyFactory(100, 100);

        try {
            ICollisionStrategy strategy = factory.getCollisionStrategy(player1, player2);
        }
        catch (IllegalArgumentException iae) {
            exceptionMessage = iae.getMessage();
        }

        assertEquals("Expected CollisionStrategyFactory to throw expected IllegalArgumentException", expectedExceptionMessage, exceptionMessage);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
