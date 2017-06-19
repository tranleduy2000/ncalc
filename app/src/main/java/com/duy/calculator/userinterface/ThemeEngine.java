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
    private final int mLightDarkActionBarTheme;
    private final int mGradientTheme;
    private Resources mResources;

    public ThemeEngine(Context applicationContext) {
        this.mResources = applicationContext.getResources();
        mCyanTheme = R.style.AppTheme_Cyan_NoActionBar;
        mBlueGrayTheme = R.style.AppTheme_BlueGray_NoActionBar;
        mBlueTheme = R.style.AppTheme_Blue_NoActionBar;
        mIndigoTheme = R.style.AppTheme_Indigo_NoActionBar;
        mRedTheme = R.style.AppTheme_Red_NoActionBar;
        mPinkTheme = R.style.AppTheme_Pink_NoActionBar;
        mPurpleTheme = R.style.AppTheme_Purple_NoActionBar;
        mDeepPurpleTheme = R.style.AppTheme_DeepPurple_NoActionBar;
        mTealTheme = R.style.AppTheme_Teal_NoActionBar;
        mYellowTheme = R.style.AppTheme_Yellow_NoActionBar;
        mLightTheme = R.style.AppTheme_Light_NoActionBar;
        mDarkTheme = R.style.AppTheme_NoActionBar;
        mLightDarkActionBarTheme = R.style.AppTheme_Light_DarkActionBar_NoActionBar;
        mBrownTheme = R.style.AppTheme_Brown_NoActionBar;
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

