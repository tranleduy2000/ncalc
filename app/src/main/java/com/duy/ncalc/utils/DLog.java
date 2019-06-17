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

package com.duy.ncalc.utils;

import android.util.Log;

/**
 * My Logger
 * Created by Duy on 18-Jan-17.
 */
public class DLog {
    public static final boolean DEBUG = false;
    private static final String TAG = "DLog";

    public static boolean UI_TESTING_MODE = false;

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