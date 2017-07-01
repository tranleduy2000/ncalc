/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
