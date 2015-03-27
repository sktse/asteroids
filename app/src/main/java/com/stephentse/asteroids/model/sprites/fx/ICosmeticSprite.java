package com.stephentse.asteroids.model.sprites.fx;

import android.graphics.Bitmap;

import com.stephentse.asteroids.model.sprites.ISprite;

public interface ICosmeticSprite extends ISprite {

    public void setTickNumber(int number);
    public int getTickNumber();
    public boolean isTickActive(int tickCount);
    public void setAnimationTileNumber(int number);
    public int getAnimationTileNumber();
    public void incrementAnimationTileNumber();
    public void resetAnimationTileNumber();
    public boolean isAnimationDone();
    public Bitmap getAnimationTile();
}
