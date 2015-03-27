package com.stephentse.asteroids.model.sprites.fx;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PlayerExplosionSprite implements ICosmeticSprite {

    private int _id;

    private Point _position;
    private Bitmap _bitmap;
    private Rect _bounds;
    private boolean _isEnabled;

    private int _mod;
    private int _tileNumber;
    private List<Bitmap> _tiles;

    public PlayerExplosionSprite() {
        _id = 0;
        _position = new Point(0,0);
        _bounds = new Rect(0,0,0,0);
        _bitmap = null;
        _isEnabled = true;

        _mod = 1;
        _tileNumber = 0;
        _tiles = new ArrayList<Bitmap>();
    }

    @Override
    public void setTickNumber(int number) {
        if (_mod <= 0) {
            throw new IllegalArgumentException("Modulus cannot be 0 or less than 0");
        }
        //set the nth tick to be active
        _mod = number;
    }

    @Override
    public int getTickNumber() {
        return _mod;
    }

    @Override
    public boolean isTickActive(int tickCount) {
        return tickCount % _mod == 0;
    }

    @Override
    public void setAnimationTileNumber(int number) {
        _tileNumber = number;
    }

    @Override
    public int getAnimationTileNumber() {
        return _tileNumber;
    }

    @Override
    public void incrementAnimationTileNumber() {
        _tileNumber++;
    }

    @Override
    public void resetAnimationTileNumber() {
        _tileNumber = 0;
    }

    @Override
    public boolean isAnimationDone() {
        //if our tile index number is larger than the number of tiles we actually have,
        //we are done with the animation
        return (_tileNumber + 1) >= _tiles.size();
    }

    @Override
    public Bitmap getAnimationTile() {
        return _tiles.get(_tileNumber);
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
        //no op
        return 0;
    }

    @Override
    public void setRotation(int rotation) {
        //no op
    }

    @Override
    public void rotate(int increment) {
        //no op
    }

    @Override
    public void setBitmap(Bitmap bitmap, boolean setBounds) {
        _bitmap = bitmap;
        Log.v("Stephen", "Bitmap dimensions (" + _bitmap.getWidth() + "," + _bitmap.getHeight() + ")");
        if (setBounds) {
            //set bounds will generate the tiles
            setBounds(new Rect(0,0, bitmap.getWidth(), bitmap.getHeight()));
        } else {
            //otherwise we need to generate them ourselves
            _tiles = generateTiles();
        }
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        setBitmap(bitmap, true);
    }

    @Override
    public Bitmap getBitmap() {
        return _bitmap;
    }

    @Override
    public void setBounds(Rect bounds) {
        _bounds = bounds;
        _tiles = generateTiles();
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
        //no op
    }

    @Override
    public void setVelocity(int x, int y) {
        //no op
    }

    @Override
    public Point getVelocity() {
        return new Point(0, 0);
    }

    @Override
    public boolean isEnabled() {
        return _isEnabled && !isAnimationDone();
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        _isEnabled = isEnabled;
    }

    private List<Bitmap> generateTiles() {
        List<Bitmap> tiles = new ArrayList<Bitmap>();
        int maxTileNumber = _bitmap.getWidth() / _bounds.width() - 1;

        for (int i = 0; i <= maxTileNumber; i++) {
            int offset = _bounds.left + i * _bounds.width();
            Bitmap tileBitmap = Bitmap.createBitmap(_bitmap, offset, _bounds.top, _bounds.width(), _bounds.height());
            tiles.add(tileBitmap);
        }

        return tiles;
    }
}
