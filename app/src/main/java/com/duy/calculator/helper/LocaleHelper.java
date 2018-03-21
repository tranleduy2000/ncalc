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

package com.duy.calculator.helper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import com.duy.calculator.R;
import com.duy.calculator.data.CalculatorSetting;

import java.util.Locale;

/**
 * Created by Duy on 22-Mar-18.
 */

public class LocaleHelper {
    private static final String TAG = "LocaleHelper";

    @SuppressLint("ObsoleteSdkInt")
    public static Context onAttach(Context context) {
        CalculatorSetting setting = new CalculatorSetting(context);
        String langCode = setting.getString(context.getString(R.string.key_pref_lang), "default_lang");
        try {
            Log.d(TAG, "loadSetting: current language " + langCode);
            Locale locale;
            if (langCode.equals("default_lang")) {
                locale = Locale.getDefault();
            } else if (langCode.contains("_")) {
                String language = langCode.substring(0, langCode.indexOf("_"));
                String country = langCode.substring(langCode.indexOf("_"));
                locale = new Locale(language, country);
            } else {
                locale = new Locale(langCode);
            }
            return setLocale(context, locale);
        } catch (Exception e) {
            Log.e(TAG, "loadSetting: failed to set language " + langCode);
        }
        return context;
    }

    private static Context setLocale(Context context, Locale language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }
        return updateResourcesLegacy(context, language);
    }


    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, Locale locale) {
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, Locale locale) {
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }
}
