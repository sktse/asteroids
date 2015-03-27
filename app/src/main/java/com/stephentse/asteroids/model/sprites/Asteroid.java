package com.stephentse.asteroids.model.sprites;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

import com.stephentse.asteroids.model.commands.ICreateAsteroidCommand;

import java.util.Random;

public class Asteroid implements IDamageableSprite {

    private int _id;
    private Point _position;
    private int _rotation;
    private int _rotationDelta;
    private Point _velocity;
    private Rect _bounds;
    private Bitmap _bitmap;
    private boolean _isEnabled;

    private int _hitPoints;
    private int _points;
    private boolean _isChildrenEnabled;
    private ICreateAsteroidCommand _childCommand;

    public Asteroid() {
        _id = 0;
        _position = new Point(-1, -1);
        _rotation = 0;
        _rotationDelta = 5;
        _bounds = new Rect(0,0,0,0);
        _velocity = new Point(0,0);
        _bitmap = null;
        _isEnabled = true;

        _hitPoints = 100;
        _points = 1000;
        _isChildrenEnabled = false;
        _childCommand = null;
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
        return new Point(_position.x, _position.y);
    }

    @Override
    public void setPosition(Point position) {
        _position = new Point(position.x, position.y);
    }

    @Override
    public void setPosition(int x, int y) {
        setPosition(new Point(x, y));
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
    public void rotate(int increment) {
        int rotation = _rotation + increment;
        if (rotation >= 360) {
            rotation -= 360;
        } else if (rotation < 0) {
            rotation += 360;
        }

        _rotation = rotation;
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
        _velocity = new Point(velocity.x, velocity.y);
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

    @Override
    public void damage(int damage) {
        _hitPoints -= damage;
    }

    @Override
    public void setHitPoints(int hitPoints) {
        _hitPoints = hitPoints;
    }

    @Override
    public int getHitPoints() {
        return _hitPoints;
    }

    public void setPoints(int points) {
        _points = points;
    }

    public int getPoints() {
        return _points;
    }

    public int getRotationDelta() {
        return _rotationDelta;
    }

    public void setRotationDelta(int rotationDelta) {
        _rotationDelta = rotationDelta;
    }

    public void setChildrenEnabled(boolean isEnabled) {
        _isChildrenEnabled = isEnabled;
    }

    public boolean isChildrenEnabled() {
        if (_childCommand == null) {
            return false;
        }

        return _isChildrenEnabled;
    }

    public void setChildCreateAsteroidCommand(ICreateAsteroidCommand command) {
        if (command != null) {
            _isChildrenEnabled = true;
        }
        _childCommand = command;
    }

    public ICreateAsteroidCommand getChildCreateAsteroidCommand() {
        return _childCommand;
    }

    public static Point getRandomVelocity(int minimum, int maximum) {
        Random r = new Random();
        int min = minimum;
        int max = maximum;
        int x = r.nextInt(max - min + 1) + min;
        int y = r.nextInt(max - min + 1) + min;

        if (r.nextBoolean()) {
            x *= -1;
        }

        if (r.nextBoolean()) {
            y *= -1;
        }

        return new Point (x, y);
    }

    public static int getRandomInitialRotation() {
        Random r = new Random();
        return r.nextInt(359);
    }

    public static int getRandomSpinRotation(int max) {
        int randomMax = max - 1;
        Random r = new Random();
        int spin = r.nextInt(randomMax) + 1;
        if (r.nextBoolean()) {
            spin *= -1;
        }

        return spin;
    }
}
