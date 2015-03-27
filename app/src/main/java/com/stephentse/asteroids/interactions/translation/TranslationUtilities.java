package com.stephentse.asteroids.interactions.translation;

import android.graphics.Point;
import android.graphics.Rect;

import com.stephentse.asteroids.AsteroidsApplication;
import com.stephentse.asteroids.gui.GameEvent;
import com.stephentse.asteroids.gui.OnGameEventListener;
import com.stephentse.asteroids.model.SpriteFactory;
import com.stephentse.asteroids.model.commands.ICreateAsteroidCommand;
import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.Bullet;
import com.stephentse.asteroids.model.sprites.IDamageableSprite;
import com.stephentse.asteroids.model.sprites.ISprite;
import com.stephentse.asteroids.model.sprites.Player;
import com.stephentse.asteroids.model.sprites.fx.ICosmeticSprite;
import com.stephentse.asteroids.model.sprites.fx.PlayerExplosionSprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TranslationUtilities {

    public synchronized static void garbageCollectBullets(HashMap<Integer, ISprite> bullets) {
        List<Integer> removeSet = new ArrayList<Integer>();
        Iterator<Integer> iterator =  bullets.keySet().iterator();
        while (iterator.hasNext()) {
            int key = iterator.next();
            Bullet bullet = (Bullet) bullets.get(key);
            Point position = bullet.getPosition();

            int minX = 0 - bullet.getBounds().width() * 2;
            int minY = 0 - bullet.getBounds().height() * 2;
            //TODO add conditions for the other sides as well
            if (position.x < minX || position.y < minY) {
                removeSet.add(key);
            }
            else if (!bullet.isEnabled()) {
                removeSet.add(key);
            }
        }

        for (int i = 0; i < removeSet.size(); i++) {
            int key = removeSet.get(i);
            bullets.remove(key);
        }
    }

    public synchronized static void tickDamageableSprites(
            HashMap<Integer, IDamageableSprite> sprites,
            HashMap<Integer, ICosmeticSprite> cosmeticSprites,
            OnGameEventListener listener) {
        List<Integer> removeSet = new ArrayList<Integer>();
        List<IDamageableSprite> addSet = new ArrayList<IDamageableSprite>();

        Iterator<Integer> iterator =  sprites.keySet().iterator();
        while (iterator.hasNext()) {
            int key = iterator.next();
            ISprite sprite = sprites.get(key);

            //if we have a disabled asteroid
            if (sprite instanceof Asteroid && !sprite.isEnabled()) {
                Asteroid asteroid = (Asteroid) sprite;

                //add it to the removal set
                removeSet.add(sprite.getId());

                //notify that an asteroid has been destroyed
                if (listener != null) {
                    listener.onGameEvent(GameEvent.ASTEROID_DESTROYED, asteroid);
                }

                //check if we need to spawn any child asteroids
                if (asteroid.isChildrenEnabled()) {
                    //we need to spawn the children asteroids
                    Rect asteroidBounds = asteroid.getAbsoluteBounds();

                    ICreateAsteroidCommand command = asteroid.getChildCreateAsteroidCommand();
                    List<Asteroid> children = command.execute(asteroidBounds);
                    addSet.addAll(children);
                }
            } else if (sprite instanceof Player) {
                Player player = (Player) sprite;
                if (player.getHitPoints() <= 0) {
                    removeSet.add(player.getId());

                    //notify that the player has been destroyed has been destroyed
                    if (listener != null) {
                        listener.onGameEvent(GameEvent.PLAYER_REKTD, player);
                    }

                    Point playerCenterPoint = new Point(player.getAbsoluteBounds().centerX(),
                                                        player.getAbsoluteBounds().centerY());
                    PlayerExplosionSprite explosionSprite = SpriteFactory.createPlayerExplosionSprite(playerCenterPoint);
                    cosmeticSprites.put(explosionSprite.getId(), explosionSprite);
                }
            }
        }

        //remove the disabled asteroids
        for (int i = 0; i < removeSet.size(); i++) {
            int key = removeSet.get(i);
            sprites.remove(key);
        }

        //add the new spawned asteroids
        for (int i = 0; i < addSet.size(); i++) {
            IDamageableSprite sprite = addSet.get(i);
            sprites.put(sprite.getId(), sprite);
        }

        if (sprites.size() == 1) {
            List<ISprite> remainingSprites = new ArrayList<ISprite>(sprites.values());
            ISprite firstSprite = remainingSprites.get(0);
            if (firstSprite instanceof Player) {
                //we only have one sprite and it is the player
                if (listener != null) {
                    listener.onGameEvent(GameEvent.GAME_COMPLETE, null);
                }
            }
        }
    }

    public synchronized static void tickCosmeticSprites(HashMap<Integer, ICosmeticSprite> sprites) {
        List<Integer> removeSet = new ArrayList<Integer>();

        Iterator<Integer> iterator =  sprites.keySet().iterator();
        while (iterator.hasNext()) {
            int key = iterator.next();
            ICosmeticSprite sprite = sprites.get(key);

            if (!sprite.isAnimationDone()) {
                //animation is not finished, but need to check if we have waited enough ticks
                //to advance the animation
                if (sprite.isTickActive(AsteroidsApplication.getInstance().getGameStatus().getTickCount())) {
                    sprite.incrementAnimationTileNumber();
                }
            } else {
                removeSet.add(sprite.getId());
            }
        }

        //remove the finished animations
        for (int i = 0; i < removeSet.size(); i++) {
            int key = removeSet.get(i);
            sprites.remove(key);
        }
    }
}
