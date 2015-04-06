package com.stephentse.asteroids.model.sprites;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.Random;

public class Player implements IDamageableSprite {

    private int _rotation;
    private Point _position;
    private Rect _bounds;
    private Rect _hitBox;
    private Rect _touchBox;
    private Bitmap _playerBitmap;
    private Bitmap _bulletBitmap;
    private int _id;

    private boolean _isEnabled;
    private int _mod;
    private int _minBulletDamage;
    private int _maxBulletDamage;

    private int _hitPoints;

    public Player() {
        _id = 0;
        _rotation = 0;
        _position = new Point(0,0);
        _bounds = new Rect(0,0,0,0);
        _hitBox = new Rect(0,0,0,0);
        _touchBox = new Rect(0,0,0,0);

        _playerBitmap = null;
        _bulletBitmap = null;

        _isEnabled = false;
        _mod = 1;
        _minBulletDamage = 0;
        _maxBulletDamage = 1;

        _hitPoints = 1;
    }

    @Override
    public int getRotation() {
        return _rotation;
    }

    @Override
    public void setRotation(int rotation) {
        _rotation = rotation;
    }

    @Override
    public void rotate (int increment) {
        _rotation = increment;
    }

    @Override
    public void setBitmap(Bitmap bitmap, boolean setBounds) {
        _playerBitmap = bitmap;
        if (setBounds) {
            setBounds(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
        }
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        setBitmap(bitmap, false);
    }

    @Override
    public Bitmap getBitmap() {
        return _playerBitmap;
    }

    @Override
    public void setBounds(Rect bounds) {
        _bounds = new Rect(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    @Override
    public Rect getBounds() {
        return _bounds;
    }

    @Override
    public Rect getAbsoluteBounds() {
        Rect absoluteBounds = new Rect(_bounds);
        absoluteBounds.offset(_position.x, _position.y);
        return absoluteBounds;
    }

    public Rect getHitBox() {
        return _hitBox;
    }

    public Rect getAbsoluteHitBox() {
        Rect absoluteBounds = new Rect(_hitBox);
        absoluteBounds.offset(_position.x, _position.y);
        return absoluteBounds;
    }

    public void setHitBox(Rect hitBox) {
        _hitBox = hitBox;
    }

    public Rect getTouchBox() {
        return _touchBox;
    }

    public Rect getAbsoluteTouchBox() {
        Rect absoluteBounds = new Rect(_touchBox);
        absoluteBounds.offset(_position.x, _position.y);
        return absoluteBounds;
    }

    public Rect getPaddedAbsoluteTouchBox(int padding) {
        //use this for player click calculations
        //so it will create a more generous click boundary
        Rect bounds = getAbsoluteTouchBox();
        Rect paddedBounds = new Rect(bounds.left - padding, bounds.top - padding, bounds.right + padding, bounds.bottom + padding);
        return paddedBounds;
    }

    public void setTouchBox(Rect touchBox) {
        _touchBox = touchBox;
    }

    @Override
    public void setVelocity(Point velocity) {
    }

    @Override
    public void setVelocity(int x, int y) {
    }

    @Override
    public void setPosition(Point position) {
        _position = position;
    }

    @Override
    public void setPosition(int x, int y) {
        setPosition(new Point(x, y));
    }

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        _id = id;
    }

    @Override
    public Point getPosition() {
        return _position;
    }

    @Override
    public Point getVelocity() {
        return new Point(0, 0);
    }

    @Override
    public boolean isEnabled() {
        return _isEnabled;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        _isEnabled = isEnabled;
    }

    @Override
    public void setHitPoints(int hitpoints) {
        _hitPoints = hitpoints;
    }

    @Override
    public int getHitPoints() {
        return _hitPoints;
    }

    @Override
    public void damage(int hitpoints) {
        _hitPoints -= hitpoints;
    }


    public void setTickNumber(int number) {
        if (_mod <= 0) {
            throw new IllegalArgumentException("Modulus cannot be 0 or less than 0");
        }
        //set the nth tick to be active
        _mod = number;
    }

    public int getTickNumber() {
        return _mod;
    }

    public boolean isTickActive(int tickCount) {
        return tickCount % _mod == 0;
    }

    public void setBulletBitmap(Bitmap bitmap) {
        _bulletBitmap = bitmap;
    }

    public Bitmap getBulletBitmap() {
        return _bulletBitmap;
    }

    public void setMaxBulletDamage(int max) {
        _maxBulletDamage = max;
    }

    public int getMaxBulletDamage() {
        return _maxBulletDamage;
    }

    public void setMinBulletDamage(int min) {
        _minBulletDamage = min;
    }

    public int getMinBulletDamage() {
        return _minBulletDamage;
    }

    public int getBulletDamage() {
        int diff = _maxBulletDamage - _minBulletDamage;
        if (diff == 0) {
            return _maxBulletDamage;
        }

        Random r = new Random();
        int damage = _minBulletDamage + r.nextInt(diff);
        return damage;
    }
}
