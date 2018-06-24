/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.calculator.userinterface;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import com.duy.calculator.R;

/**
 * Font Manager
 * Created by Duy on 23-Jan-17.
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
