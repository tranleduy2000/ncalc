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
