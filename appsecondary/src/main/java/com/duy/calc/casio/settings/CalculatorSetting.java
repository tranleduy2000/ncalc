/*
 * Copyright (c) 2017 by Tran Le Duy
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

package com.duy.calc.casio.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntRange;
import android.support.annotation.StringRes;

import com.duy.calc.casio.evaluator.EvaluateConfig;
import com.example.duy.calculator.R;


/**
 * Created by Duy on 26-Jun-17.
 */

public class CalculatorSetting {
    public static final int DEFAULT_FONT_SIZE = 64;
    private SharedPreferences mPreferences;
    private Context context;

    public CalculatorSetting(Context context) {
        this.context = context;
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static CalculatorSetting newInstance(Context context) {
        return new CalculatorSetting(context);
    }

    public static EvaluateConfig createEvaluateConfig(Context context) {
        CalculatorSetting setting = new CalculatorSetting(context);
        EvaluateConfig config = new EvaluateConfig();
        config.setEvalMode(setting.useFraction() ? EvaluateConfig.FRACTION : EvaluateConfig.DECIMAL);
        config.setRoundTo(setting.getRoundTo());
        config.setTrigMode(setting.useRadian() ? EvaluateConfig.RADIAN : EvaluateConfig.DEGREE);
        return config;
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }

    public boolean useFraction() {
        return mPreferences.getBoolean(getString(R.string.key_fraction), false);
    }

    public boolean useRadian() {
        return mPreferences.getBoolean(getString(R.string.key_radian), true);
    }

    public void setAngleMode(boolean isRadian) {
        mPreferences.edit().putBoolean(getString(R.string.key_radian), isRadian).apply();
    }

    public void setFraction(boolean isFraction) {
        mPreferences.edit().putBoolean(getString(R.string.key_fraction), isFraction).apply();
    }

    public int getRoundTo() {
        return mPreferences.getInt(getString(R.string.key_round), 10);
    }

    public void setRoundTo(@IntRange(from = 5, to = 1000) int numberDecimal) {
        mPreferences.edit().putInt(getString(R.string.key_round), numberDecimal).apply();
    }

    public int getFontSize() {
        return mPreferences.getInt(getString(R.string.key_font_size), DEFAULT_FONT_SIZE);
    }

    public void setFontSize(int size) {
        mPreferences.edit().putInt(getString(R.string.key_font_size), size).apply();
    }

    public boolean hasVibrate() {
        return mPreferences.getBoolean(getString(R.string.key_vibrarte), true);
    }

    public void setVibrate(boolean vibrate) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(getString(R.string.key_vibrarte), vibrate).apply();
    }

    public boolean isAutoCalculate() {
        return mPreferences.getBoolean(getString(R.string.key_instant_result), false);
    }

    private String getString(@StringRes int id) {
        return context.getString(id);
    }

    public boolean isShowStatusBar() {
        return mPreferences.getBoolean(getString(R.string.key_show_status_bar), false);
    }

    public void setShowStatusBar(boolean isShow) {
        mPreferences.edit().putBoolean(getString(R.string.key_show_status_bar), isShow)
                .apply();
    }

    public void setAutoCalc(boolean auto) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(getString(R.string.key_instant_result), auto);
        editor.apply();
    }
}
