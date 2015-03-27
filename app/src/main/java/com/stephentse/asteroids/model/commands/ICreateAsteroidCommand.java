package com.stephentse.asteroids.model.commands;

import android.graphics.Rect;

import com.stephentse.asteroids.model.sprites.Asteroid;

import java.util.List;

public interface ICreateAsteroidCommand {
    public List<Asteroid> execute(Rect creationBounds);
}
