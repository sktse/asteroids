package com.stephentse.asteroids;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.stephentse.asteroids.interactions.translation.TranslationUtilities;
import com.stephentse.asteroids.model.SpriteFactory;
import com.stephentse.asteroids.model.commands.CreateAsteroidCommand;
import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.Bullet;
import com.stephentse.asteroids.model.sprites.IDamageableSprite;
import com.stephentse.asteroids.model.sprites.ISprite;
import com.stephentse.asteroids.model.sprites.Player;
import com.stephentse.asteroids.model.sprites.fx.ICosmeticSprite;
import com.stephentse.asteroids.model.sprites.fx.PlayerExplosionSprite;

import junit.framework.TestCase;

import java.util.HashMap;

public class TickTests extends TestCase {

    private static final int SPRITE_ID = 1;

    private Bitmap _bulletBitmap;
    private int _bulletMinX;
    private int _bulletMinY;
    private Rect _bounds;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        _bulletBitmap =  BitmapFactory.decodeResource(AsteroidsApplication.getInstance().getResources(), R.drawable.bullet);
        _bulletMinX = 0 - _bulletBitmap.getWidth() * 2;
        _bulletMinY = 0 - _bulletBitmap.getHeight() * 2;
        _bounds = new Rect(0, 0, 10, 10);
    }

    public void testNoGarbageCollectBullets() {
        HashMap<Integer, ISprite> bullets = getBullet(100, 200, true, _bulletBitmap);
        TranslationUtilities.garbageCollectBullets(bullets);

        assertEquals("Expected 1 bullet to be in the set", 1, bullets.size());
    }

    public void testLeftGarbageCollectBullets() {
        HashMap<Integer, ISprite> bullets = getBullet(_bulletMinX - 1, 400, true, _bulletBitmap);
        TranslationUtilities.garbageCollectBullets(bullets);

        assertEquals("Expected 0 bullet to be in the set", 0, bullets.size());
    }

    public void testTopGarbageCollectBullets() {
        HashMap<Integer, ISprite> bullets = getBullet(300, _bulletMinY - 3, true, _bulletBitmap);
        TranslationUtilities.garbageCollectBullets(bullets);

        assertEquals("Expected 0 bullet to be in the set", 0, bullets.size());
    }

    public void testDisabledGarbageCollectBullets() {
        HashMap<Integer, ISprite> bullets = getBullet(300, 525, false, _bulletBitmap);
        TranslationUtilities.garbageCollectBullets(bullets);

        assertEquals("Expected 0 bullet to be in the set", 0, bullets.size());
    }

    public void testTickCosmeticSprites() {
        HashMap<Integer, ICosmeticSprite> sprites = getCosmeticSprite(100, 200);
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());

        ICosmeticSprite sprite = sprites.get(SPRITE_ID);
        Bitmap tile0 = sprite.getAnimationTile();
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());
        assertEquals("Expected animation tile number to be 0", 0, sprite.getAnimationTileNumber());
        assertFalse("Expected animation to not be completed", sprite.isAnimationDone());

        //tick 0 => 1
        TranslationUtilities.tickCosmeticSprites(sprites);
        sprite = sprites.get(SPRITE_ID);
        Bitmap tile1 = sprite.getAnimationTile();
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());
        assertEquals("Expected animation tile number to be 1", 1, sprite.getAnimationTileNumber());
        assertFalse("Expected animation to not be completed", sprite.isAnimationDone());
        assertNotSame("Expected animation bitmap to be different", tile0, tile1);

        //tick 1 => 2
        TranslationUtilities.tickCosmeticSprites(sprites);
        sprite = sprites.get(SPRITE_ID);
        Bitmap tile2 = sprite.getAnimationTile();
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());
        assertEquals("Expected animation tile number to be 2", 2, sprite.getAnimationTileNumber());
        assertFalse("Expected animation to not be completed", sprite.isAnimationDone());
        assertNotSame("Expected animation bitmap to be different", tile0, tile2);
        assertNotSame("Expected animation bitmap to be different", tile1, tile2);

        //tick 2 => 3
        TranslationUtilities.tickCosmeticSprites(sprites);
        sprite = sprites.get(SPRITE_ID);
        Bitmap tile3 = sprite.getAnimationTile();
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());
        assertEquals("Expected animation tile number to be 3", 3, sprite.getAnimationTileNumber());
        assertFalse("Expected animation to not be completed", sprite.isAnimationDone());
        assertNotSame("Expected animation bitmap to be different", tile0, tile3);
        assertNotSame("Expected animation bitmap to be different", tile1, tile3);
        assertNotSame("Expected animation bitmap to be different", tile2, tile3);

        //tick 3 => 4
        TranslationUtilities.tickCosmeticSprites(sprites);
        sprite = sprites.get(SPRITE_ID);
        Bitmap tile4 = sprite.getAnimationTile();
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());
        assertEquals("Expected animation tile number to be 4", 4, sprite.getAnimationTileNumber());
        assertFalse("Expected animation to not be completed", sprite.isAnimationDone());
        assertNotSame("Expected animation bitmap to be different", tile0, tile4);
        assertNotSame("Expected animation bitmap to be different", tile1, tile4);
        assertNotSame("Expected animation bitmap to be different", tile2, tile4);
        assertNotSame("Expected animation bitmap to be different", tile3, tile4);

        //tick 4 => 5 [DONE]
        TranslationUtilities.tickCosmeticSprites(sprites);
        sprite = sprites.get(SPRITE_ID);
        Bitmap tile5 = sprite.getAnimationTile();
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());
        assertEquals("Expected animation tile number to be 5", 5, sprite.getAnimationTileNumber());
        assertTrue("Expected animation to be completed", sprite.isAnimationDone());
        assertNotSame("Expected animation bitmap to be different", tile0, tile5);
        assertNotSame("Expected animation bitmap to be different", tile1, tile5);
        assertNotSame("Expected animation bitmap to be different", tile2, tile5);
        assertNotSame("Expected animation bitmap to be different", tile3, tile5);
        assertNotSame("Expected animation bitmap to be different", tile4, tile5);

        //tick 5 => [SPRITE REMOVED]
        TranslationUtilities.tickCosmeticSprites(sprites);
        assertEquals("Expected 0 sprite to be in the set", 0, sprites.size());
    }

    public void testTickInactiveCosmeticSprites() {
        MockInactivePlayerExplosionSprite sprite = new MockInactivePlayerExplosionSprite();

        Bitmap bitmap = BitmapFactory.decodeResource(
                AsteroidsApplication.getInstance().getResources(), R.drawable.explosion_sheet);
        sprite.setBitmap(bitmap);

        Rect bounds = new Rect(0, 0, bitmap.getHeight(), bitmap.getHeight());
        sprite.setBounds(bounds);
        sprite.setPosition(200, 300);
        sprite.setId(SPRITE_ID);

        HashMap<Integer, ICosmeticSprite> sprites = new HashMap<>();
        sprites.put(sprite.getId(), sprite);

        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());
        assertEquals("Expected animation tile number to be 0", 0, sprite.getAnimationTileNumber());
        assertFalse("Expected animation to not be completed", sprite.isAnimationDone());

        TranslationUtilities.tickCosmeticSprites(sprites);
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());
        assertEquals("Expected animation tile number to be 0", 0, sprite.getAnimationTileNumber());
        assertFalse("Expected animation to not be completed", sprite.isAnimationDone());
    }

    public void testEnabledAsteroidTickDamageableSprites() {
        HashMap<Integer, ICosmeticSprite> cosmeticSprites = new HashMap<>();
        Asteroid asteroid = getAsteroid(200, 200, 1, 2);
        HashMap<Integer, IDamageableSprite> sprites = new HashMap<>();
        sprites.put(SPRITE_ID, asteroid);
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());

        TranslationUtilities.tickDamageableSprites(sprites, cosmeticSprites, null);
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());

        Asteroid afterTickAsteroid = (Asteroid)sprites.get(SPRITE_ID);
        assertEquals("Expected unchanged asteroid", asteroid, afterTickAsteroid);
    }

    public void testDisabledAsteroidTickDamageableSprites() {
        HashMap<Integer, ICosmeticSprite> cosmeticSprites = new HashMap<>();
        Asteroid asteroid = getAsteroid(200, 200, 1, 2);
        asteroid.setEnabled(false);
        HashMap<Integer, IDamageableSprite> sprites = new HashMap<>();
        sprites.put(SPRITE_ID, asteroid);
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());

        TranslationUtilities.tickDamageableSprites(sprites, cosmeticSprites, null);
        assertEquals("Expected 0 sprite to be in the set", 0, sprites.size());
    }

    public void testDisabledAsteroidWithChildrenTickDamageableSprites() {
        HashMap<Integer, ICosmeticSprite> cosmeticSprites = new HashMap<>();
        CreateAsteroidCommand command = new CreateAsteroidCommand(4, SpriteFactory.AsteroidSize.SIZE_100, 4, null);

        Asteroid asteroid = getAsteroid(200, 200, 1, 2);
        asteroid.setEnabled(false);
        asteroid.setChildCreateAsteroidCommand(command);
        HashMap<Integer, IDamageableSprite> sprites = new HashMap<>();
        sprites.put(SPRITE_ID, asteroid);
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());

        TranslationUtilities.tickDamageableSprites(sprites, cosmeticSprites, null);
        assertEquals("Expected 4 sprite to be in the set", 4, sprites.size());
    }

    public void testAlivePlayerTickDamageableSprites() {
        HashMap<Integer, ICosmeticSprite> cosmeticSprites = new HashMap<>();

        Player player = getPlayer(100, 100, 2);
        HashMap<Integer, IDamageableSprite> sprites = new HashMap<>();
        sprites.put(SPRITE_ID, player);
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());

        TranslationUtilities.tickDamageableSprites(sprites, cosmeticSprites, null);
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());
        assertEquals("Expected 0 cosmetic sprite to be in the set", 0, cosmeticSprites.size());
    }

    public void testZeroHitpointsPlayerTickDamageableSprites() {
        HashMap<Integer, ICosmeticSprite> cosmeticSprites = new HashMap<>();

        Player player = getPlayer(100, 100, 0);
        HashMap<Integer, IDamageableSprite> sprites = new HashMap<>();
        sprites.put(SPRITE_ID, player);
        assertEquals("Expected 0 hitpoints from player sprite", 0, player.getHitPoints());
        assertEquals("Expected 1 sprite to be in the set", 1, sprites.size());

        TranslationUtilities.tickDamageableSprites(sprites, cosmeticSprites, null);
        assertEquals("Expected 0 sprite to be in the set", 0, sprites.size());
        assertEquals("Expected 1 cosmetic sprite to be in the set", 1, cosmeticSprites.size());
    }

    private Player getPlayer(int x, int y, int hitPoints) {
        Player player = new Player();
        player.setPosition(x, y);
        player.setHitPoints(hitPoints);
        player.setBounds(_bounds);
        player.setEnabled(true);
        player.setId(SPRITE_ID);

        return player;
    }

    private Asteroid getAsteroid(int x, int y, int velocityX, int velocityY) {
        Asteroid asteroid = new Asteroid();
        asteroid.setPosition(x, y);
        asteroid.setVelocity(velocityX, velocityY);
        asteroid.setBounds(_bounds);
        asteroid.setId(SPRITE_ID);
        return asteroid;
    }

    private HashMap<Integer, ISprite> getBullet(int x, int y, boolean isEnabled, Bitmap bitmap) {
        Bullet bullet = new Bullet();
        bullet.setPosition(x, y);
        bullet.setBitmap(bitmap, true);
        bullet.setId(SPRITE_ID);
        bullet.setEnabled(isEnabled);

        HashMap<Integer, ISprite> bullets = new HashMap<>();
        bullets.put(bullet.getId(), bullet);
        return bullets;
    }

    public HashMap<Integer, ICosmeticSprite> getCosmeticSprite(int x, int y) {
        MockPlayerExplosionSprite sprite = new MockPlayerExplosionSprite();

        Bitmap bitmap = BitmapFactory.decodeResource(
                AsteroidsApplication.getInstance().getResources(), R.drawable.explosion_sheet);
        sprite.setBitmap(bitmap);

        Rect bounds = new Rect(0, 0, bitmap.getHeight(), bitmap.getHeight());
        sprite.setBounds(bounds);
        sprite.setPosition(x, y);
        sprite.setId(SPRITE_ID);

        HashMap<Integer, ICosmeticSprite> sprites = new HashMap<>();
        sprites.put(sprite.getId(), sprite);
        return sprites;
    }

    public class MockPlayerExplosionSprite extends PlayerExplosionSprite {
        @Override
        public boolean isTickActive(int tickCount) {
            return true;
        }
    }

    public class MockInactivePlayerExplosionSprite extends PlayerExplosionSprite {
        @Override
        public boolean isTickActive(int tickCount) {
            return false;
        }
    }
}
