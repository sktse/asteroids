package com.stephentse.asteroids.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class TypefaceFactory {

    private static final String SQUARE_PUSH_TYPEFACE_PATH = "fonts/spv3.5.ttf";
    private static final String BASIC_TYPEFACE_PATH = "fonts/basic_square_7_solid.ttf";
    private static final String SQUARE_TYPEFACE_PATH = "fonts/5x5_square.ttf";
    private static final String FORCED_SQUARE_TYPEFACE_PATH = "fonts/forced_square.ttf";

    public enum TypefaceName { SQUARE_PUSH, SQUARE, BASIC, FORCED_SQUARE };

    public static Typeface getTypeFace(TypefaceName name, AssetManager assetManager) {
        switch(name) {
            case SQUARE_PUSH:
                return Typeface.createFromAsset(assetManager, SQUARE_PUSH_TYPEFACE_PATH);
            case BASIC:
                return Typeface.createFromAsset(assetManager, BASIC_TYPEFACE_PATH);
            case SQUARE:
                return Typeface.createFromAsset(assetManager, SQUARE_TYPEFACE_PATH);
            case FORCED_SQUARE:
                return Typeface.createFromAsset(assetManager, FORCED_SQUARE_TYPEFACE_PATH);
            default:
                throw new IllegalArgumentException("Unknown typeface name: " + name);
        }
    }
}
