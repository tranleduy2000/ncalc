package com.example.duy.calculator;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.example.duy.calculator.userinterface.ThemeEngine;

/**
 * Setting for calcualtor
 * <p>
 * Include precision of calculate. Font, theme, style. Dev mode, trace mode.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString(getResources().getString(R.string.key_pref_theme), "");
        ThemeEngine themeEngine = new ThemeEngine(this);
        int themeId = themeEngine.getTheme(name);
        if (themeId != ThemeEngine.THEME_NOT_FOUND) {
//            super.setTheme(themeId);
        }
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting);
        }
    }
}
