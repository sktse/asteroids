package com.stephentse.asteroids.model.commands;

import android.graphics.Point;
import android.graphics.Rect;

import com.stephentse.asteroids.model.SpriteFactory;
import com.stephentse.asteroids.model.sprites.Asteroid;

import java.util.ArrayList;
import java.util.List;

public class CreateCyclingAsteroidCommand implements ICreateAsteroidCommand {

    private int _hitPoints;
    private SpriteFactory.AsteroidSize _size;
    private Point _position;
    private Point _velocity;

    public CreateCyclingAsteroidCommand(int hitPoints, SpriteFactory.AsteroidSize size, Point position, Point velocity) {
        _hitPoints = hitPoints;
        _size = size;
        _position = position;
        _velocity = velocity;
    }

    @Override
    public List<Asteroid> execute(Rect creationBounds) {
        SpriteFactory.AsteroidSize size = getSmallerSize(_size);
        Point velocity = new Point(_velocity.x * -1, _velocity.y * -1);
        ICreateAsteroidCommand command = new CreateCyclingAsteroidCommand(_hitPoints, size, _position, velocity);

        Asteroid asteroid = SpriteFactory.createRandomAsteroid(_size, _position);
        asteroid.setPoints(_hitPoints);
        asteroid.setVelocity(_velocity);
        asteroid.setChildCreateAsteroidCommand(command);

        List<Asteroid> asteroids = new ArrayList<Asteroid>();
        asteroids.add(asteroid);

        return asteroids;
    }

    private SpriteFactory.AsteroidSize getSmallerSize(SpriteFactory.AsteroidSize size) {
        switch (size) {
            case SIZE_100:
                return SpriteFactory.AsteroidSize.SIZE_80;
            case SIZE_80:
                return SpriteFactory.AsteroidSize.SIZE_50;
            case SIZE_50:
                return SpriteFactory.AsteroidSize.SIZE_20;
            case SIZE_20:
            default:
                return SpriteFactory.AsteroidSize.SIZE_100;
        }
    }
}
