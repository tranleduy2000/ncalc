package com.example.duy.calculator.version_old.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.duy.calculator.R;

import static com.example.duy.calculator.version_old.settings.PreferencesUtil.bindPreferenceSummaryToValue;

public class FragmentSetting extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences
        // to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design
        // guidelines.
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pref_precision)));
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pref_latex_mode)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pref_font)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pref_lang)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pref_theme)));
    }
}