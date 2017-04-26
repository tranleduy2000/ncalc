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