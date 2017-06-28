package com.example.duy.calculator;

import android.util.Log;

/**
 * My Logger
 * Created by DUy on 18-Jan-17.
 */
public class DLog {
    public static final boolean DEBUG = true;
    private static final String TAG = DLog.class.getSimpleName();

    public static void d(String msg) {
        if (DEBUG)
            Log.d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    public static void e(String msg) {
        if (DEBUG)
            Log.e(TAG, msg);
    }

    public static void i(String msg) {
        if (DEBUG)
            Log.i(TAG, msg);
    }

    public static void w(String msg) {
        if (DEBUG)
            Log.w(TAG, msg);
    }

    public static void printStack(Exception e) {
        if (DEBUG)
            Log.e(TAG, "printStack:  " + e.getMessage());

    }
}