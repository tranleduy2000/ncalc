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

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void printStack(Exception e) {
        Log.e(TAG, "printStack: isSyntaxError " + e.getMessage());
    }
}