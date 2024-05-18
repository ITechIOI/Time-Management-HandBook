package com.example.time_management_handbook.model;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class CustomContextWrapper extends ContextWrapper {
    public CustomContextWrapper(Context base) {
        super(base);
    }

    public Drawable getDrawableByName(String name) {
        try {
            int id = getResources().getIdentifier(name, "drawable", getPackageName());
            if (id == 0) {
                throw new IllegalArgumentException("Resource not found");
            }
            return getResources().getDrawable(id);
        } catch (Exception e) {
            Log.e("CustomContextWrapper", "Error getting drawable", e);
            return null;
        }
    }
}