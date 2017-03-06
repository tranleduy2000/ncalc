package com.example.duy.calculator.utils;

import android.content.Context;

/**
 * Created by Duy on 3/7/2016
 */
@Deprecated
public class GraphicUtil {
    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
