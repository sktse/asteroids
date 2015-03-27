package com.stephentse.asteroids.model.commands;

import android.graphics.Point;
import android.graphics.Rect;

import com.stephentse.asteroids.model.SpriteFactory;
import com.stephentse.asteroids.model.sprites.Asteroid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateAsteroidCommand implements ICreateAsteroidCommand{

    private int _hitPoints;
    private SpriteFactory.AsteroidSize _size;

    private int _count;
    private CreateAsteroidCommand _childCommand;

    public CreateAsteroidCommand(int hitPoints, SpriteFactory.AsteroidSize size, int count, CreateAsteroidCommand childCommand) {
        _hitPoints = hitPoints;
        _size = size;

        _count = count;
        _childCommand = childCommand;
    }

    public List<Asteroid> execute(Rect creationBounds) {
        List<Asteroid> asteroids = new ArrayList<Asteroid>();
        for (int i = 0; i < _count; i++) {
            Point randomPoint = getRandomPoint(creationBounds);
            Asteroid asteroid = SpriteFactory.createRandomAsteroid(_size, randomPoint);
            asteroid.setHitPoints(_hitPoints);
            asteroid.setChildCreateAsteroidCommand(_childCommand);

            asteroids.add(asteroid);
        }
        return asteroids;
    }

    private Point getRandomPoint(Rect creationBounds) {
        //get a point within the defined creation bounds
        //remember that the defined point is the upper left hand corner

        int minX = creationBounds.left;
        int maxX = creationBounds.right;
        int minY = creationBounds.top;
        int maxY = creationBounds.bottom;
        int diffX = maxX - minX;
        int diffY = maxY - minY;

        Random r = new Random();
        int x = r.nextInt(diffX) + minX;
        int y = r.nextInt(diffY) + minY;

        return new Point(x, y);
    }


}
