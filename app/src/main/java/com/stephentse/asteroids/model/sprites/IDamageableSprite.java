package com.stephentse.asteroids.model.sprites;

public interface IDamageableSprite extends ISprite{
    public void setHitPoints(int hitpoints);
    public int getHitPoints();
    public void damage(int hitpoints);
}
