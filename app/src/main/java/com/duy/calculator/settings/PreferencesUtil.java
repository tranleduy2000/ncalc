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

package com.duy.calculator.settings;

import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatDelegate;

public class PreferencesUtil {

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object value) {
                    String stringValue = value.toString();

                    if (preference instanceof ListPreference) {
                        // For list preferences, look up the correct display value in
                        // the preference's 'entries' list.
                        ListPreference listPreference = (ListPreference) preference;
                        int index = listPreference.findIndexOfValue(stringValue);

                        // Set the summary to reflect the new value.
                        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                    } else if (preference instanceof RingtonePreference) {
                    } else if (preference instanceof EditTextPreference) {
                        EditTextPreference editTextPreference = (EditTextPreference) preference;
                        editTextPreference.setSummary(editTextPreference.getText());
                    } else {
                        // For all other preferences, set the summary to the value's
                        // simple string representation.
                        preference.setSummary(stringValue);
                    }
                    return true;
                }
            };

    private AppCompatDelegate mDelegate;

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    public static void bindPreferenceSummaryToValue(Preference preference) {
        if (preference == null) return;
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


}