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

package com.duy.ncalc.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.duy.calculator.R;

import static com.duy.ncalc.settings.PreferencesUtil.bindPreferenceSummaryToValue;

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