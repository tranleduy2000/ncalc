package com.example.duy.calculator;


import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * <<<<<<< HEAD
 * Setting for calculator
 * =======
 * Setting for calcualtor
 * >>>>>>> refs/remotes/origin/master
 * <p>
 * Include precision of calculate. Font, theme, style. Dev mode, trace mode.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
    }
}
