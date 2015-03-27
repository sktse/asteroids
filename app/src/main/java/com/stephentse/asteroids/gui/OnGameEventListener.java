package com.stephentse.asteroids.gui;

import com.stephentse.asteroids.model.sprites.ISprite;

public interface OnGameEventListener {
    public void onGameEvent(int event, ISprite sprite);
}
