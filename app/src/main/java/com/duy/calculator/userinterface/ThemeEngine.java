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
import android.content.res.Resources;

import com.duy.calculator.R;

/**
 * Created by Duy on 3/7/2016
 */
public class ThemeEngine {
    public static final String NULL = "";
    public static final int THEME_NOT_FOUND = -1;
    private final int mCyanTheme;
    private final int mBlueTheme;
    private final int mBlueGrayTheme;
    private final int mIndigoTheme;
    private final int mRedTheme;
    private final int mPinkTheme;
    private final int mPurpleTheme;
    private final int mDeepPurpleTheme;
    private final int mTealTheme;
    private final int mYellowTheme;
    private final int mLightTheme;
    private final int mDarkTheme;
    private final int mBrownTheme;
    private final int mGradientTheme;
    private Resources mResources;

    public ThemeEngine(Context applicationContext) {
        this.mResources = applicationContext.getResources();
        mCyanTheme = R.style.AppTheme_Cyan;
        mBlueGrayTheme = R.style.AppTheme_BlueGray;
        mBlueTheme = R.style.AppTheme_Blue;
        mIndigoTheme = R.style.AppTheme_Indigo;
        mRedTheme = R.style.AppTheme_Red;
        mPinkTheme = R.style.AppTheme_Pink;
        mPurpleTheme = R.style.AppTheme_Purple;
        mDeepPurpleTheme = R.style.AppTheme_DeepPurple;
        mTealTheme = R.style.AppTheme_Teal;
        mYellowTheme = R.style.AppTheme_Yellow;
        mLightTheme = R.style.AppThemeLight;
        mDarkTheme = R.style.AppThemeDark;
        mBrownTheme = R.style.AppTheme_Brown;
        mGradientTheme = R.style.AppTheme_Gradient;
    }

    /**
     * get theme from mResult
     *
     * @param name
     * @return
     */
    public int getTheme(String name) {
        name = name.trim();
        if (name.equals(NULL)) {
            return THEME_NOT_FOUND;
        }
        if (name.equals(mResources.getString(R.string.theme_light))) {
            return mLightTheme;
        } else if (name.equals(mResources.getString(R.string.theme_cyan))) {
            return mCyanTheme;
        } else if (name.equals(mResources.getString(R.string.theme_indigo))) {
            return mIndigoTheme;
        } else if (name.equals(mResources.getString(R.string.theme_brown))) {
            return mBrownTheme;
        } else if (name.equals(mResources.getString(R.string.theme_blue))) {
            return mBlueTheme;
        } else if (name.equals(mResources.getString(R.string.theme_red))) {
            return mRedTheme;
        } else if (name.equals(mResources.getString(R.string.theme_purple))) {
            return mPurpleTheme;
        } else if (name.equals(mResources.getString(R.string.theme_deep_purple))) {
            return mDeepPurpleTheme;
        } else if (name.equals(mResources.getString(R.string.theme_teal))) {
            return mTealTheme;
        } else if (name.equals(mResources.getString(R.string.theme_yellow))) {
            return mYellowTheme;
        } else if (name.equals(mResources.getString(R.string.theme_dark))) {
            return mDarkTheme;
        } else if (name.equals(mResources.getString(R.string.theme_gradient))) {
            return mGradientTheme;
        }
        return mLightTheme;
    }
}

