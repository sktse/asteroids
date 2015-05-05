package com.stephentse.asteroids.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.stephentse.asteroids.AsteroidsApplication;
import com.stephentse.asteroids.model.SpriteFactory;
import com.stephentse.asteroids.model.commands.ICreateAsteroidCommand;
import com.stephentse.asteroids.model.sprites.Asteroid;
import com.stephentse.asteroids.model.sprites.Bullet;
import com.stephentse.asteroids.model.sprites.IDamageableSprite;
import com.stephentse.asteroids.model.sprites.ISprite;
import com.stephentse.asteroids.model.sprites.Player;
import com.stephentse.asteroids.interactions.collision.CollisionStrategyFactory;
import com.stephentse.asteroids.interactions.collision.ICollisionStrategy;
import com.stephentse.asteroids.model.sprites.fx.ICosmeticSprite;
import com.stephentse.asteroids.interactions.translation.ISpriteTranslationStrategy;
import com.stephentse.asteroids.interactions.translation.TranslationStrategyFactory;
import com.stephentse.asteroids.interactions.translation.TranslationUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameBoard extends View {

    private Paint p;
    private List<Point> _starField;
    private int _starAlpha;
    private int _starFade;
    private int _numberOfStars;

    private static final int PLAYER_CLICK_PADDING = 20;

    private HashMap<Integer, HashSet<Integer>> _collisions = new HashMap<Integer, HashSet<Integer>>();
    private HashMap<Integer, IDamageableSprite> _sprites;
    private HashMap<Integer, ISprite> _bullets;
    private HashMap<Integer, ICosmeticSprite> _cosmeticSprites;
    private int _playerId;
    private Point _spriteClickDelta;

    private CollisionStrategyFactory _collisionStrategyFactory;
    private TranslationStrategyFactory _translationStrategyFactory;

    private OnGameEventListener _gameEventListener;

    private Matrix m = null;
    private boolean _isLayoutComplete;

    public GameBoard(Context context, AttributeSet aSet) {
        super(context, aSet);
        m = new Matrix();
        p = new Paint();

        _starField = null;

        _starAlpha = 80;
        _starFade = 2;
        _numberOfStars = 25;

        _playerId = -1;
        _spriteClickDelta = new Point(0, 0);

        _sprites = new HashMap<Integer, IDamageableSprite>();
        _bullets = new HashMap<Integer, ISprite>();
        _cosmeticSprites = new HashMap<Integer, ICosmeticSprite>();

        //enable the long press so we can click and draw sprites
        setLongClickable(true);

        _gameEventListener = null;
        _collisionStrategyFactory = null;
        _translationStrategyFactory = null;

        _isLayoutComplete = false;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getWidth() > 0 && getHeight() > 0) {
                    _isLayoutComplete = true;
                }
            }
        });
    }

    public synchronized void resetStarField() {
        _starField = null;
    }

    public synchronized void setNumberOfStars(int numberOfStars) {
        _numberOfStars = numberOfStars;
        resetStarField();
    }

    public synchronized int getNumberOfStars() {
        return _numberOfStars;
    }

    public synchronized void initializeGame(
            Point playerPosition, int tickNumber, int minBulletDamage, int maxBulletDamage,
            ICreateAsteroidCommand command, Rect creationBounds, Bitmap shipBitmap, Bitmap bulletBitmap) {
        _sprites.clear();
        _bullets.clear();

        Player player = SpriteFactory.createPlayer(
                shipBitmap, bulletBitmap, playerPosition,
                tickNumber, minBulletDamage, maxBulletDamage);
        _sprites.put(player.getId(), player);
        _playerId = player.getId();

        initializeAsteroids(command, creationBounds);
    }

    public synchronized void initializeAsteroids(ICreateAsteroidCommand command, Rect creationBounds) {
        List<Asteroid> asteroids = command.execute(creationBounds);
        for (Asteroid asteroid : asteroids) {
            _sprites.put(asteroid.getId(), asteroid);
        }
    }

    public synchronized void continueGame(Point playerPosition, int tickNumber, int minBulletDamage, int maxBulletDamage, Bitmap shipBitmap, Bitmap bulletBitmap) {
        Player player = SpriteFactory.createPlayer(
                shipBitmap, bulletBitmap, playerPosition,
                tickNumber, minBulletDamage, maxBulletDamage);
        _sprites.put(player.getId(), player);
        _playerId = player.getId();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!_sprites.containsKey(_playerId)) {
            //player no longer exists
            return super.onTouchEvent(event);
        }

        Player player = (Player) _sprites.get(_playerId);

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            Point clickPoint = new Point((int) event.getX(), (int) event.getY());
            if (player.getPaddedAbsoluteTouchBox(PLAYER_CLICK_PADDING).contains(clickPoint.x, clickPoint.y)) {
                Point playerPoint = player.getPosition();
                _spriteClickDelta = new Point(clickPoint.x - playerPoint.x, clickPoint.y - playerPoint.y);
                player.setEnabled(true);
            }
        }
        else if (action == MotionEvent.ACTION_MOVE) {
            if (player.isEnabled()) {
                int maxX = getWidth() - player.getBounds().width() - 1;
                int maxY = getHeight() - player.getBounds().height() - 1;

                int moveX = ((int) event.getX()) - _spriteClickDelta.x;
                moveX = moveX < 0 ? 0 : moveX;
                moveX = moveX > maxX ? maxX : moveX;
                int moveY = ((int) event.getY()) - _spriteClickDelta.y;
                moveY = moveY < 0 ? 0 : moveY;
                moveY = moveY > maxY ? maxY : moveY;
                player.setPosition(moveX, moveY);
            }
        }
        else if (action == MotionEvent.ACTION_UP) {
            player.setEnabled(false);
        }

        return super.onTouchEvent(event);
    }

    private void initializeStars(int maxX, int maxY) {
        _starField = new ArrayList<Point>();
        for (int i = 0; i < _numberOfStars; i++) {
            Random r = new Random();
            int x = r.nextInt(maxX-5+1)+5;
            int y = r.nextInt(maxY-5+1)+5;
            _starField.add(new Point(x, y));
        }
    }

    private CollisionStrategyFactory getCollisionStrategyFactory() {
        if (_collisionStrategyFactory == null) {
            _collisionStrategyFactory = new CollisionStrategyFactory(getWidth(), getHeight());
        }
        return _collisionStrategyFactory;
    }

    private TranslationStrategyFactory getTranslationStrategyFactory() {
        if (_translationStrategyFactory == null) {
            _translationStrategyFactory = new TranslationStrategyFactory(getWidth(), getHeight());
        }
        return _translationStrategyFactory;
    }

    public synchronized void tick() {
        if (!_isLayoutComplete) {
            return;
        }
        tickStarAlpha();

        AsteroidsApplication.getInstance().getGameStatus().incrementTickCount();
        TranslationStrategyFactory translationStrategyFactory = getTranslationStrategyFactory();
        CollisionStrategyFactory collisionStrategyFactory = getCollisionStrategyFactory();

        //move non-bullet sprites
        Iterator<Integer> spriteIterator = _sprites.keySet().iterator();
        while (spriteIterator.hasNext()) {
            int key = spriteIterator.next();
            ISprite sprite = _sprites.get(key);
            ISpriteTranslationStrategy strategy = translationStrategyFactory.getTranslationStrategy(sprite);
            strategy.translate(_bullets);
        }

        //move sprites
        Iterator<Integer> iterator = _bullets.keySet().iterator();
        while(iterator.hasNext()) {
            int key = iterator.next();
            Bullet bullet = (Bullet) _bullets.get(key);
            ISpriteTranslationStrategy strategy = translationStrategyFactory.getTranslationStrategy(bullet);
            strategy.translate(_bullets);
        }

        //check collisions between non-bullet sprites
        List<Integer> spriteKeys = new ArrayList<Integer>(_sprites.keySet());
        for (int x = 0; x < (spriteKeys.size() - 1); x++) {
            ISprite sprite1 = _sprites.get(spriteKeys.get(x));
            for (int y = x + 1; y < spriteKeys.size(); y++) {
                ISprite sprite2 = _sprites.get(spriteKeys.get(y));
                boolean hasPreviouslyCollided = false;

                //deal with multiple simultaneous collisions
                //change the value to a HashSet with all the things is has collided with
                if (_collisions.containsKey(sprite1.getId()) && _collisions.get(sprite1.getId()).contains(sprite2.getId())) {
                    hasPreviouslyCollided = true;
                } else if (!_collisions.containsKey(sprite1.getId())) {
                    //collisions was initialized
                    _collisions.put(sprite1.getId(), new HashSet<Integer>());
                }

                ICollisionStrategy strategy = collisionStrategyFactory.getCollisionStrategy(sprite1, sprite2);
                boolean spritesIntersect = strategy.intersect(sprite1, sprite2);
                if (spritesIntersect && !hasPreviouslyCollided) {
                    _collisions.get(sprite1.getId()).add(sprite2.getId());
                    strategy.collision();
                }
                else if (!spritesIntersect && hasPreviouslyCollided) {
                    //clear the previous collision
                    _collisions.get(sprite1.getId()).remove(sprite2.getId());
                }
            }
        }

        //check collisions between bullets and non-bullet sprites
        for (int x = 0; x < spriteKeys.size(); x++) {
            ISprite sprite = _sprites.get(spriteKeys.get(x));
            Iterator<Integer> bulletIterator = _bullets.keySet().iterator();
            while (bulletIterator.hasNext()) {
                int key = bulletIterator.next();
                Bullet bullet = (Bullet) _bullets.get(key);

                ICollisionStrategy strategy = collisionStrategyFactory.getCollisionStrategy(sprite, bullet);
                boolean spritesIntersect = strategy.intersect(sprite, bullet);
                if (spritesIntersect) {
                    strategy.collision();
                    //do something?
                }
            }
        }

        TranslationUtilities.garbageCollectBullets(_bullets);
        TranslationUtilities.tickCosmeticSprites(_cosmeticSprites);
        if (!isWinningScenario()) {
            TranslationUtilities.tickDamageableSprites(
                    _sprites, _cosmeticSprites, _gameEventListener);
        }
    }

    public void setStarAlpha(int alpha) {
        _starAlpha = alpha;
    }

    private void tickStarAlpha() {
        _starAlpha += _starFade;

        if (_starAlpha > 252 || _starAlpha < 80) {
            _starFade = _starFade *- 1;
        }
    }

    private boolean isWinningScenario() {
        if (_sprites.size() == 1) {
            List<ISprite> remainingSprites = new ArrayList<ISprite>(_sprites.values());
            ISprite firstSprite = remainingSprites.get(0);
            if (firstSprite instanceof Player) {
                //we only have one sprite and it is the player
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized void onDraw(Canvas canvas) {
        p.setColor(Color.BLACK);
        p.setAlpha(255);
        p.setStrokeWidth(1);
        canvas.drawRect(0, 0, getWidth(), getHeight(), p);

        if (_starField == null) {
            initializeStars(canvas.getWidth(), canvas.getHeight());
        }
        p.setColor(Color.CYAN);
        p.setAlpha(_starAlpha);
        p.setStrokeWidth(5);

        for (int i = 0; i < _numberOfStars; i++) {
            canvas.drawPoint(_starField.get(i).x, _starField.get(i).y, p);
        }

        //Now we draw our sprites.  Items drawn in this function are stacked.
        //The items drawn at the top of the loop are on the bottom of the z-order.
        //Therefore we draw our set, then our actors, and finally any fx.
        Iterator<Integer> iterator =  _sprites.keySet().iterator();
        while (iterator.hasNext()) {
            int key = iterator.next();
            ISprite sprite = _sprites.get(key);
            if (sprite.getPosition().x >= 0) {
                m.reset();
                m.postTranslate((float) (sprite.getPosition().x), (float) (sprite.getPosition().y));
                if (sprite.getRotation() != 0) {
                    m.postRotate(sprite.getRotation(),
                            (float) (sprite.getPosition().x + sprite.getBounds().width() / 2.0),
                            (float) (sprite.getPosition().y + sprite.getBounds().width() / 2.0));
                }
                canvas.drawBitmap(sprite.getBitmap(), m, null);

                //detect clipping and rolling over the drawing
                int maxX = canvas.getWidth();
                int maxY = canvas.getHeight();
                int clippedX = sprite.getPosition().x + sprite.getBounds().width() > maxX ? sprite.getPosition().x - maxX : 0;
                int clippedY = sprite.getPosition().y + sprite.getBounds().height() > maxY ? sprite.getPosition().y - maxY : 0;

                if (clippedX < 0 || clippedY < 0) {
                    clippedX = clippedX == 0 ? sprite.getPosition().x : clippedX;
                    clippedY = clippedY == 0 ? sprite.getPosition().y : clippedY;

                    m.reset();
                    m.postTranslate((float) (clippedX), (float) (clippedY));
                    if (sprite.getRotation() != 0) {
                        m.postRotate(sprite.getRotation(),
                                (float) (clippedX + sprite.getBounds().width() / 2.0),
                                (float) (clippedY + sprite.getBounds().width() / 2.0));
                        canvas.drawBitmap(sprite.getBitmap(), m, null);
                    }
                }

                //TODO fix scenario where the sprite clips all 4 corners
            }
        }

        //bullet translation and drawing
        Iterator<Integer> bulletIterator =  _bullets.keySet().iterator();
        while (bulletIterator.hasNext()) {
            int key = bulletIterator.next();
            Bullet bullet = (Bullet) _bullets.get(key);

            m.reset();
            m.postTranslate((float)(bullet.getPosition().x), (float)(bullet.getPosition().y));
            canvas.drawBitmap(bullet.getBitmap(), m, null);
        }

        //gfx animation translation and drawing
        Iterator<Integer> cosmeticIterator = _cosmeticSprites.keySet().iterator();
        while(cosmeticIterator.hasNext()) {
            int key = cosmeticIterator.next();
            ICosmeticSprite sprite = _cosmeticSprites.get(key);

            m.reset();
            m.postTranslate((float)(sprite.getPosition().x), (float)(sprite.getPosition().y));
            canvas.drawBitmap(sprite.getAnimationTile(), m, null);
        }
    }

    public synchronized void setOnGameEventListener(OnGameEventListener listener) {
        _gameEventListener = listener;
    }
}
