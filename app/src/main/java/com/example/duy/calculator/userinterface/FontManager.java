package com.example.duy.calculator.userinterface;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import com.example.duy.calculator.R;

/**
 * Font Manager
 * Created by DUy on 23-Jan-17.
 */

public final class FontManager {

    /**
     * it can be null object, make private
     */
    private static Typeface typeface = null;

    public static Typeface loadTypefaceFromAsset(Context context) {
        if (typeface != null) return typeface;
        String path = "fonts/";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String font = sharedPreferences.getString(context.getString(R.string.key_pref_font),
                context.getString(R.string.font_light));
        if (font.equals(context.getString(R.string.font_black))) {
            path = path + "Roboto-Black.ttf";
        } else if (font.equals(context.getString(R.string.font_light))) {
            path = path + "Roboto-Light.ttf";
        } else if (font.equals(context.getString(R.string.font_bold))) {
            path = path + "Roboto-Bold.ttf";
        } else if (font.equals(context.getString(R.string.font_italic))) {
            path = path + "Roboto-Italic.ttf";
        } else if (font.equals(context.getString(R.string.font_thin))) {
            path = path + "Roboto-Thin.ttf";
        }
        AssetManager assetManager = context.getAssets();
        typeface = Typeface.createFromAsset(assetManager, path);
        return typeface;
    }

    public String getFont(Context context) {
        String path = "fonts/";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String font = sharedPreferences.getString(context.getString(R.string.key_pref_font),
                context.getString(R.string.font_light));
        if (font.equals(context.getString(R.string.font_black))) {
            return path + "Roboto-Black.ttf";
        } else if (font.equals(context.getString(R.string.font_light))) {
            return path + "Roboto-Light.ttf";
        } else if (font.equals(context.getString(R.string.font_bold))) {
            return path + "Roboto-Bold.ttf";
        } else if (font.equals(context.getString(R.string.font_italic))) {
            return path + "Roboto-Italic.ttf";
        } else if (font.equals(context.getString(R.string.font_thin))) {
            return path + "Roboto-Thin.ttf";
        }
        return path + "Roboto-Light.ttf";
    }

    public Typeface getTypeFace(Context context) {
        String path = "fonts/";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String font = sharedPreferences.getString(context.getString(R.string.key_pref_font),
                context.getString(R.string.font_light));
        if (font.equals(context.getString(R.string.font_black))) {
            path = path + "Roboto-Black.ttf";
        } else if (font.equals(context.getString(R.string.font_light))) {
            path = path + "Roboto-Light.ttf";
        } else if (font.equals(context.getString(R.string.font_bold))) {
            path = path + "Roboto-Bold.ttf";
        } else if (font.equals(context.getString(R.string.font_italic))) {
            path = path + "Roboto-Italic.ttf";
        } else if (font.equals(context.getString(R.string.font_thin))) {
            path = path + "Roboto-Thin.ttf";
        }
        AssetManager assetManager = context.getAssets();
        return Typeface.createFromAsset(assetManager, path);
    }


}
