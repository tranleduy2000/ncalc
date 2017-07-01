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

package com.duy.calculator;

import android.util.Log;

/**
 * My Logger
 * Created by Duy on 18-Jan-17.
 */
public class DLog {
    public static final boolean DEBUG = false;

    private static final String TAG = "DLog";

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void i(Object... array) {

    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void printStack(Exception e) {
        Log.e(TAG, "printStack: isSyntaxError " + e.getMessage());
    }
}