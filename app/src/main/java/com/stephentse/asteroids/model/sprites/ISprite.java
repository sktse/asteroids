package com.stephentse.asteroids.model.sprites;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

public interface ISprite {

    public int getId();
    public void setId(int id);
    public Point getPosition();
    public void setPosition(Point position);
    public void setPosition(int x, int y);
    public int getRotation();
    public void setRotation(int rotation);
    public void rotate(int increment);
    public void setBitmap(Bitmap bitmap, boolean setBounds);
    public void setBitmap(Bitmap bitmap);
    public Bitmap getBitmap();
    public void setBounds(Rect bounds);
    public Rect getBounds();
    public Rect getAbsoluteBounds();
    public void setVelocity(Point velocity);
    public void setVelocity(int x, int y);
    public Point getVelocity();
    public boolean isEnabled();
    public void setEnabled(boolean isEnabled);
}