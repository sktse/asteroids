package com.stephentse.asteroids;

import android.graphics.Rect;

import com.stephentse.asteroids.interactions.collision.BulletPlayerCollisionStrategy;
import com.stephentse.asteroids.model.sprites.Bullet;
import com.stephentse.asteroids.model.sprites.Player;

import junit.framework.TestCase;

public class BulletPlayerCollisionStrategyTests extends TestCase {

    private Rect _bounds;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _bounds = new Rect(0, 0, 10, 10);
    }

    public void testNoCollision() {
        Player player = getPlayer(20, 20, 100);
        Bullet bullet = getBullet(100, 100, 25);

        BulletPlayerCollisionStrategy strategy = new BulletPlayerCollisionStrategy();
        boolean intersect = strategy.intersect(player, bullet);
        assertFalse("Expected player and bullet to not collide", intersect);
    }

    public void testCollision() {
        Player player = getPlayer(20, 20, 100);
        Bullet bullet = getBullet(22, 22, 25);

        BulletPlayerCollisionStrategy strategy = new BulletPlayerCollisionStrategy();
        boolean intersect = strategy.intersect(player, bullet);
        assertFalse("Expected player and bullet to not collide", intersect);
    }


    private Player getPlayer(int x, int y, int hitPoints) {
        Player player = new Player();
        player.setPosition(x, y);
        player.setHitPoints(hitPoints);
        player.setBounds(_bounds);
        player.setEnabled(true);

        return player;
    }

    private Bullet getBullet(int x, int y, int power) {
        Bullet bullet = new Bullet();
        bullet.setPosition(x, y);
        bullet.setPower(power);
        bullet.setBounds(_bounds);
        return bullet;
    }
}
