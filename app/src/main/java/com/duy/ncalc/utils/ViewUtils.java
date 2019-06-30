/*
 * Copyright (c) 2018 by Tran Le Duy
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

package com.duy.ncalc.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;

import com.duy.calculator.R;


/**
 * Created by Duy on 2/5/2018.
 */

public class ViewUtils {

    /**
     * Show the view or text notification for a short period of time.  This time
     * could be user-definable.  This is the default.
     *
     * @see Toast#setDuration
     */
    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    /**
     * Show the view or text notification for a long period of time.  This time
     * could be user-definable.
     *
     * @see Toast#setDuration
     */
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;
    public static float oneDpInPx = 1;

    public static void init(Context context) {
        oneDpInPx = ViewUtils.dpToPx(context, 1);
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int spToPx(Context context, int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static float pxToDp(Context context, int px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static int getAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public static int getColor(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{attr});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    public static boolean getBoolean(Context context, int attr, boolean def) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{attr});
        boolean value = a.getBoolean(0, def);
        a.recycle();
        return value;
    }

    @Nullable
    public static ColorStateList getColorStateList(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{attr});
        ColorStateList color = a.getColorStateList(0);
        a.recycle();
        return color;
    }

    public static boolean showDialog(Activity activity, Dialog dialog) {
        if (activity == null) {
            return false;
        }
        if (!activity.isFinishing()) {
            dialog.show();
            return true;
        }
        return false;
    }


    @SuppressWarnings("UnusedReturnValue")
    public static boolean showToast(@Nullable Context activity, @StringRes int idRes, int time) {
        return showToast(activity, activity != null ? activity.getString(idRes) : null, time);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean showToast(@Nullable Context activity, String idRes, int time) {
        if (activity == null) {
            return false;
        }
        if (activity instanceof Activity && ((Activity) activity).isFinishing()) {
            return false;
        }
        Toast.makeText(activity, idRes, time).show();
        return true;
    }
}
