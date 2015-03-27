package com.stephentse.asteroids.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;

import com.stephentse.asteroids.AsteroidsApplication;
import com.stephentse.asteroids.R;
import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.Bullet;
import com.stephentse.asteroids.model.sprites.Player;
import com.stephentse.asteroids.model.sprites.fx.PlayerExplosionSprite;

import java.util.Random;

public class SpriteFactory {

    public enum AsteroidSize {
        SIZE_20, SIZE_50, SIZE_80, SIZE_100,
    }

    private static int _idCounter = 0;

    public static int getUniqueId() {
        int value = _idCounter;
        _idCounter++;
        return value;
    }

    public static Player createPlayer(Bitmap playerBitmap, Bitmap bulletBitmap, Point position,
                                      int tickNumber, int minBulletDamage, int maxBulletDamage) {
        Player player = new Player();
        player.setBitmap(playerBitmap, true);
        player.setBulletBitmap(bulletBitmap);
        player.setPosition(position);
        player.setTickNumber(tickNumber);
        player.setMinBulletDamage(minBulletDamage);
        player.setMaxBulletDamage(maxBulletDamage);

        int id = getUniqueId();
        player.setId(id);

        return player;
    }

    public static Bullet createBullet() {
        Bitmap bitmap = BitmapFactory.decodeResource(
                AsteroidsApplication.getInstance().getResources(), R.drawable.bullet);
        return createBullet(new Point(0,0), bitmap);
    }

    public static Bullet createBullet(Point velocity, Bitmap bitmap) {
        Bullet bullet = new Bullet();
        bullet.setVelocity(velocity);
        bullet.setBitmap(bitmap, true);

        int id = getUniqueId();
        bullet.setId(id);

        return bullet;
    }

    public static Asteroid createRandomAsteroid(AsteroidSize size, Point position) {
        Random r = new Random();
        int rotationDelta = r.nextBoolean() ? 5 : -5;

        Asteroid asteroid = new Asteroid();
        asteroid.setPosition(position);
        asteroid.setVelocity(Asteroid.getRandomVelocity(1, 5));
        asteroid.setRotation(Asteroid.getRandomInitialRotation());
        asteroid.setRotationDelta(rotationDelta);

        int id = getUniqueId();
        asteroid.setId(id);

        Bitmap bitmap;
        switch(size) {
            case SIZE_20:
                asteroid.setPoints(200);
                bitmap = BitmapFactory.decodeResource(
                        AsteroidsApplication.getInstance().getResources(), R.drawable.box020);
                break;
            case SIZE_50:
                asteroid.setPoints(500);
                bitmap = BitmapFactory.decodeResource(
                        AsteroidsApplication.getInstance().getResources(), R.drawable.box050);
                break;
            case SIZE_80:
                asteroid.setPoints(800);
                bitmap = BitmapFactory.decodeResource(
                        AsteroidsApplication.getInstance().getResources(), R.drawable.box080);
                break;
            case SIZE_100:
            default:
                asteroid.setPoints(1000);
                bitmap = BitmapFactory.decodeResource(
                        AsteroidsApplication.getInstance().getResources(), R.drawable.box100);
                break;

        }
        asteroid.setBitmap(bitmap, true);

        return asteroid;
    }

    public static PlayerExplosionSprite createPlayerExplosionSprite(Point centerPosition) {
        PlayerExplosionSprite sprite = new PlayerExplosionSprite();
        sprite.setTickNumber((byte)3);

        Bitmap bitmap = BitmapFactory.decodeResource(
                AsteroidsApplication.getInstance().getResources(), R.drawable.explosion_sheet);
        sprite.setBitmap(bitmap);

        Rect bounds = new Rect(0, 0, bitmap.getHeight(), bitmap.getHeight());
        sprite.setBounds(bounds);

        int offsetX = centerPosition.x - (bounds.width() / 2);
        int offsetY = centerPosition.y - (bounds.width() / 2);
        sprite.setPosition(offsetX, offsetY);

        int id = getUniqueId();
        sprite.setId(id);


        return sprite;
    }
}
