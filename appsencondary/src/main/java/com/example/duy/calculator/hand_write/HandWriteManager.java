package com.example.duy.calculator.hand_write;

import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;

import com.example.duy.calculator.R;
import com.example.duy.calculator.userinterface.FontManager;
import com.example.duy.calculator.utils.ColorUtil;
import com.myscript.atk.math.widget.MathWidgetApi;

/**
 * Created by DUy on 04-Jan-17.
 */

public class HandWriteManager {
    public static boolean initHandWrite(MathWidgetApi widget,
                                        Activity context,
                                        CalcHandWriteCallback mCallback,
                                        boolean autoEval) {
        //    mContainerGesture = (LinearLayout) findViewById(R.id.container_gesture);
        boolean success = widget.registerCertificate(MyCertificate.getBytes());
        if (!success) return false;
        widget.addSearchDir("zip://" + context.getPackageCodePath() + "!/assets/conf");
        widget.configure("math", "standard");

        TypedValue typedValue = new TypedValue();
        final Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        //set background
        widget.setBackgroundColor(color);

        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        widget.setInkColor(typedValue.data);
        widget.setBaselineColor(ColorUtil.BLACK);
        widget.setTransientTextColor(typedValue.data);
        widget.setTextColor(typedValue.data);

        widget.setOnGestureListener(mCallback);
        widget.setOnSolvingListener(mCallback);
        widget.setOnPenListener(mCallback);
        widget.setOnRecognitionListener(mCallback);
        widget.setOnConfigureListener(mCallback);
        widget.setOnUndoRedoListener(mCallback);

        FontManager fontManager = new FontManager();
        widget.setTypeface(fontManager.getTypeFace(context));

        widget.setAngleUnit(MathWidgetApi.AngleUnit.DEGREE);
        widget.setDecimalsCount(3);
        widget.setInkThickness(1);
        if (!autoEval) {
            widget.setBeautificationOption(MathWidgetApi.RecognitionBeautification.BeautifyFontify);
        } else {
            widget.setBeautificationOption(MathWidgetApi.RecognitionBeautification.BeautifyFontifyAndSolve);
        }
        widget.setPaddingRatio(0.5f, .5f, .5f, 0.5f);
        return true;
    }

}
