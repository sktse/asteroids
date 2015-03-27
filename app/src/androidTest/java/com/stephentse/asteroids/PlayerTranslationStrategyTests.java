package com.stephentse.asteroids;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;

import com.stephentse.asteroids.interactions.translation.PlayerTranslationStrategy;
import com.stephentse.asteroids.model.sprites.Bullet;
import com.stephentse.asteroids.model.sprites.ISprite;
import com.stephentse.asteroids.model.sprites.Player;

import junit.framework.TestCase;

import java.util.HashMap;

public class PlayerTranslationStrategyTests extends TestCase {

    private Rect _bounds;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        _bounds = new Rect(0, 0, 10, 10);
    }

    public void testTickActivePlayerTranslation() {
        Player player = getActivePlayer(200, 400, 10);
        HashMap<Integer, ISprite> sprites = new HashMap<>();
        PlayerTranslationStrategy strategy = new PlayerTranslationStrategy(player);
        strategy.translate(sprites);

        assertEquals("Expected player position to be unchanged", new Point(200, 400), player.getPosition());
        assertEquals("Expected a new sprite to be created", 1, sprites.size());

        Object[] spriteArray = sprites.values().toArray();
        ISprite resultSprite = (ISprite)spriteArray[0];

        assertTrue("Expected created sprite to be a bullet", resultSprite instanceof Bullet);
    }

    public void testTickInactivePlayerTranslation() {
        Player player = getInactivePlayer(200, 400, 10);
        HashMap<Integer, ISprite> sprites = new HashMap<>();
        PlayerTranslationStrategy strategy = new PlayerTranslationStrategy(player);
        strategy.translate(sprites);

        assertEquals("Expected player position to be unchanged", new Point(200, 400), player.getPosition());
        assertEquals("Expected no sprites to be created", 0, sprites.size());
    }

    public void testDisabledTickActivePlayerTranslation() {
        Player player = getActivePlayer(200, 400, 10);
        player.setEnabled(false);

        assertFalse("Expected player to be disabled", player.isEnabled());

        HashMap<Integer, ISprite> sprites = new HashMap<>();
        PlayerTranslationStrategy strategy = new PlayerTranslationStrategy(player);
        strategy.translate(sprites);

        assertEquals("Expected player position to be unchanged", new Point(200, 400), player.getPosition());
        assertEquals("Expected no sprites to be created", 0, sprites.size());
    }

    private Player getActivePlayer(int x, int y, int power) {
        MockActivePlayer player = new MockActivePlayer();
        player.setPosition(x, y);
        player.setBounds(_bounds);
        player.setEnabled(true);
        player.setMinBulletDamage(power);
        player.setMaxBulletDamage(power);

        Bitmap bulletBitmap = BitmapFactory.decodeResource(
                AsteroidsApplication.getInstance().getResources(), R.drawable.bullet);
        player.setBulletBitmap(bulletBitmap);

        return player;
    }

    private Player getInactivePlayer(int x, int y, int power) {
        MockInactivePlayer player = new MockInactivePlayer();
        player.setPosition(x, y);
        player.setBounds(_bounds);
        player.setEnabled(true);
        player.setMinBulletDamage(power);
        player.setMaxBulletDamage(power);

        return player;
    }

    public class MockActivePlayer extends Player {
        @Override
        public boolean isTickActive(int tickCount) {
            //for this test, just make the tick always active and spawn a bullet
            return true;
        }
    }

    public class MockInactivePlayer extends Player {
        @Override
        public boolean isTickActive(int tickCount) {
            //for this test, just make the tick always inactive and never spawn a bullet
            return false;
        }
    }
}
