package com.stephentse.asteroids.gui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TypefaceStringArrayAdapter extends ArrayAdapter<String> {

    private Typeface _typeface;

    public TypefaceStringArrayAdapter(Context context, int resource) {
        super(context, resource);
        _typeface = null;
    }

    public TypefaceStringArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        _typeface = null;
    }

    public TypefaceStringArrayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        _typeface = null;
    }

    public TypefaceStringArrayAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
        _typeface = null;
    }

    public TypefaceStringArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        _typeface = null;
    }

    public TypefaceStringArrayAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
        _typeface = null;
    }

    public void setTypeface(Typeface typeface) {
        _typeface = typeface;
    }

    public Typeface getTypeface() {
        return _typeface;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setTypeface(_typeface);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setTypeface(_typeface);
        return textView;
    }
}
