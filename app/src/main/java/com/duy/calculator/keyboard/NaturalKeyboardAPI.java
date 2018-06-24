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

package com.duy.calculator.keyboard;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.duy.calculator.activities.base.AbstractEvaluatorActivity;

/**
 * Created by Duy on 05-Jul-17.
 */

public class NaturalKeyboardAPI {
    public static final int REQUEST_INPUT = 9991;
    private static final String KEY_GET_INPUT = "HAS_GET_INPUT";
    private static final String LAUNCHER_ACTIVITY = "com.duy.calc.casio.calculator.ScienceCalculatorActivity";
    private static final String APP_ID = "com.duy.calc.casio";
    private static final String TAG = "NaturalKeyboardAPI";

    public static void getExpression(AbstractEvaluatorActivity activity) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName(APP_ID, LAUNCHER_ACTIVITY));
            intent.putExtra(KEY_GET_INPUT, true);
            activity.startActivityForResult(intent, REQUEST_INPUT);
        } catch (ActivityNotFoundException e) {
            activity.showDialogInstallNaturalKeyboard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public static String processResult(Intent intent) {
        Log.d(TAG, "processResult() called with: intent = [" + intent + "]");

        if (intent.hasExtra(Intent.EXTRA_RETURN_RESULT)) {
            return intent.getStringExtra(Intent.EXTRA_RETURN_RESULT);
        }
        return "";
    }
}
