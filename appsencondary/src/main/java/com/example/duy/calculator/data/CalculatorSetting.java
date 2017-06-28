package com.example.duy.calculator.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.duy.calculator.R;
import com.example.duy.calculator.utils.ConfigApp;

/**
 * Setting for application
 * <p>
 * Created by Duy on 3/7/2016
 */
public class CalculatorSetting {

    public static final long serialVersionUID = 310L;

    public static final String preName = "app_data";
    public static final String INPUT_MATH = "inp_math";
    public static final String RESULT_MATH = "res_math";

    public static final String INPUT_BASE = "INPUT_BASE";
    public static final String RESULT_BASE = "RESULT_BASE";
    public static final String BASE = "BASE";

    public static final String USE_RADIANS = "USE_RADIANS";
    public static final String NEW_UPDATE = "new_update" + serialVersionUID;
    public static final String GO_TO_MARKET = "GO_TO_MARKET";

    public static final String CMPLX_ON = "COMPLEX";
    public static final String INPUT_STATISTIC = "INPUT_STATISTIC";
    /**
     * duplication with key_pref_fraction on mSetting.xml file
     */
    public static final String IS_FRACTION = "fraction_mode";

    public static final String IS_DEGREE = "IS_DEGREE";
    private static final String IS_UPDATE = "IS_UPDATE_" + ConfigApp.VERSION_CODE;

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private Context context;

    public CalculatorSetting(Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharedPreferences.edit();
    }

    public CalculatorSetting(SharedPreferences mPreferences, Context context) {
        this.context = context;

        this.sharedPreferences = mPreferences;
        this.editor = sharedPreferences.edit();
    }

    public boolean useRadians() {
        return sharedPreferences.getBoolean(USE_RADIANS, false);
    }

    public boolean useComplex() {
        return sharedPreferences.getBoolean(CMPLX_ON, false);
    }

    public boolean useLightTheme() {
        return sharedPreferences.getBoolean("THEME_STYLE", false);
    }

    public boolean instantResult() {
        return sharedPreferences.getBoolean(context.getString(R.string.key_pref_instant_res), true);
    }

    public boolean useFraction() {
        return sharedPreferences.getBoolean(CalculatorSetting.IS_FRACTION, false);
    }

    public void setFraction(boolean b) {
        editor.putBoolean(IS_FRACTION, b).commit();
    }

    public boolean isUpdate(Context context) {
        return !(new CalculatorSetting(context).getBoolean(IS_UPDATE));
    }

    public void setUpdate(boolean b) {
        put(IS_UPDATE, b);
    }

    public void put(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void put(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void put(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public void put(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    /**
     * get long value from key,
     *
     * @param key - key
     * @return -1 if not found
     */
    public long getLong(String key) {
        return sharedPreferences.getLong(key, -1);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean isNewUpdate() {
        return sharedPreferences.getBoolean(NEW_UPDATE, false);
    }

    public void setVar(String name, String value) {
        editor.putString("var_" + name, value);
    }
}
