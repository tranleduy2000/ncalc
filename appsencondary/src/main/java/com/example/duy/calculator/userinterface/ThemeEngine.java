package com.example.duy.calculator.userinterface;

import android.content.Context;
import android.content.res.Resources;

import com.example.duy.calculator.R;

/**
 * <<<<<<< HEAD
 * Theme manager
 * <p>
 * =======
 * >>>>>>> refs/remotes/origin/master
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
    private final int mDayNightTheme;
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
        mDayNightTheme = R.style.AppTheme_DayNight;
        mGradientTheme = R.style.AppTheme_Gradient;
    }

    /**
     * get theme from result
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
        } else if (name.equals(mResources.getString(R.string.theme_day_night))) {
            return mDayNightTheme;
        } else if (name.equals(mResources.getString(R.string.theme_gradient))) {
            return mGradientTheme;
        }
        return mLightTheme;
    }
}

