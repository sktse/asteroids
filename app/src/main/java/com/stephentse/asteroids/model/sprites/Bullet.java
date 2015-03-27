package com.stephentse.asteroids.model.sprites;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

public class Bullet implements ISprite {

    private int _id;
    private Point _position;
    private Bitmap _bitmap;
    private Rect _bounds;
    private Point _velocity;

    private boolean _isEnabled;
    private int _power;

    public Bullet() {
        _id = 0;
        _position = new Point(0, 0);
        _bitmap = null;
        _bounds = new Rect(0, 0, 0, 0);
        _velocity = new Point(0, 0);

        _isEnabled = true;
        _power = 0;
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
    public void setPosition(Point position) {
        _position = position;
    }

    @Override
    public void setPosition(int x, int y) {
        _position = new Point(x, y);
    }

    @Override
    public int getRotation() {
        return 0;
    }

    @Override
    public void setRotation(int rotation) {

    }

    @Override
    public void rotate(int increment) {

    }

    @Override
    public void setBitmap(Bitmap bitmap, boolean setBounds) {
        _bitmap = bitmap;
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
        return _bitmap;
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

    @Override
    public void setVelocity(Point velocity) {
        _velocity = velocity;
    }

    @Override
    public void setVelocity(int x, int y) {
        setVelocity(new Point(x, y));
    }

    @Override
    public Point getVelocity() {
        return _velocity;
    }

    @Override
    public boolean isEnabled() {
        return _isEnabled;
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        _isEnabled = isEnabled;
    }

    public void setPower(int power) {
        _power = power;
    }

    public int getPower() {
        return _power;
    }
}
