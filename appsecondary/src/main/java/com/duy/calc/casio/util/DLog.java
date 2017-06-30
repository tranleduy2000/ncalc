/*
 * Copyright (c) 2017 by Tran Le Duy
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

package com.duy.calc.casio.util;

import android.util.Log;

import org.matheclipse.core.interfaces.IExpr;

/**
 * Created by Duy on 25-Jun-17.
 */

public class DLog {
    private static final String TAG = "DLog";
    public static boolean ANDROID = true;

    public static void d(String tag, String msg) {
        if (!ANDROID) {
            System.out.println(tag + ": " + msg);
        } else {
            Log.d(tag, msg);
        }
    }

    public static void showResultInfo(IExpr result) {
        d(TAG, result.toString());
        d(TAG, "is number " + result.isNumber());
        d(TAG, "is numeric " + result.isNumber());
    }
}
