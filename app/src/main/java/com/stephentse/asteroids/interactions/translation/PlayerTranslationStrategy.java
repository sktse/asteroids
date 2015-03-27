package com.stephentse.asteroids.interactions.translation;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.stephentse.asteroids.AsteroidsApplication;
import com.stephentse.asteroids.model.SpriteFactory;
import com.stephentse.asteroids.model.sprites.Bullet;
import com.stephentse.asteroids.model.sprites.ISprite;
import com.stephentse.asteroids.model.sprites.Player;

import java.util.HashMap;

public class PlayerTranslationStrategy implements ISpriteTranslationStrategy {

    private Player _player;

    public PlayerTranslationStrategy(ISprite sprite) {
        if (!(sprite instanceof Player)) {
            throw new IllegalArgumentException("Sprite is not an instance of Player");
        }
        _player = (Player) sprite;

    }

    @Override
    public synchronized void translate(HashMap<Integer, ISprite> sprites) {
        //do not need to move the player, since it will be moved manually
        //we will need to check if a bullet needs to be spawned
        if (_player.isEnabled() &&
            _player.isTickActive(AsteroidsApplication.getInstance().getGameStatus().getTickCount())) {
            Point playerPosition = _player.getPosition();

            Point velocity = new Point(0, -10);
            Bitmap bitmap = _player.getBulletBitmap();

            Bullet bullet = SpriteFactory.createBullet(velocity, bitmap);
            Point position = new Point(
                    playerPosition.x + (_player.getBounds().width() / 2) - (bullet.getBounds().width() / 2),
                    playerPosition.y - 10);
            int damage = _player.getBulletDamage();
            bullet.setPosition(position);
            bullet.setPower(damage);

            //There are no bullet/bullet interactions and
            //could potentially be a lot of bullets on the screen
            sprites.put(bullet.getId(), bullet);
        }

    }
}
