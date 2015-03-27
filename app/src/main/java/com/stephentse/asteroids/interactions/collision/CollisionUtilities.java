package com.stephentse.asteroids.interactions.collision;

import android.graphics.Point;
import android.graphics.Rect;

import com.stephentse.asteroids.model.sprites.ISprite;

import java.util.ArrayList;
import java.util.List;

public class CollisionUtilities {

    public static boolean circularIntersection(Rect bounds1, Rect bounds2) {
        Point thisCenter = new Point(bounds1.centerX(), bounds1.centerY());
        int thisRadius = bounds1.height() > bounds1.width() ? bounds1.width()/2 : bounds1.height()/2;

        Point otherCenter = new Point(bounds2.centerX(), bounds2.centerY());
        int otherRadius = bounds2.height() > bounds2.width() ? bounds2.width()/2 : bounds2.height()/2;

        //base on the equation from:
        //http://stackoverflow.com/questions/8566336/java-circle-circle-collision-detection
        int xDiff = thisCenter.x - otherCenter.x;
        int yDiff = thisCenter.y - otherCenter.y;
        int distanceSquared = xDiff * xDiff + yDiff * yDiff;
        boolean collision = distanceSquared < (thisRadius + otherRadius) * (thisRadius + otherRadius);

        return collision;
    }

    public static List<Rect> expandClippedBounds(ISprite sprite, int maxWidth, int maxHeight) {
        List<Rect> spriteBounds = new ArrayList<Rect>();
        Rect absoluteBounds = sprite.getAbsoluteBounds();
        spriteBounds.add(absoluteBounds);

        //if needed, expand all the bounds of each sprite to deal with clipping
        //clip off the right side
        if (absoluteBounds.right > maxWidth) {
            Rect clippedXBounds = new Rect(absoluteBounds);
            clippedXBounds.offsetTo(absoluteBounds.left - maxWidth, absoluteBounds.top);
            spriteBounds.add(clippedXBounds);
        }

        //the bottom is clipping the over
        if (absoluteBounds.bottom > maxHeight) {
            Rect clippedYBounds = new Rect(absoluteBounds);
            clippedYBounds.offsetTo(absoluteBounds.left, absoluteBounds.top - maxHeight);
            spriteBounds.add(clippedYBounds);
        }

        return spriteBounds;
    }

}
