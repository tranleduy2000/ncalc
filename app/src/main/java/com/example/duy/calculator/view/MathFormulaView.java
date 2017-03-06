package com.example.duy.calculator.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import com.example.duy.calculator.DLog;
import com.example.duy.calculator.R;

import io.github.kexanie.library.MathView;

import static io.github.kexanie.library.MathView.Engine.KATEX;
import static io.github.kexanie.library.MathView.Engine.MATHJAX;

/**
 * Created by DUy on 26-Jan-17.
 */

public class MathFormulaView extends MathView {
    public MathFormulaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) setup(context);
    }

    /**
     * set up support zoom for math view
     */
    private void setup(Context context) {
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);

        //hide control view
        getSettings().setDisplayZoomControls(false);

        SharedPreferences preferenceManager
                = PreferenceManager.getDefaultSharedPreferences(context);
        String engine = preferenceManager.getString(
                context.getString(R.string.key_pref_latex_mode), "KaTeX");
        DLog.i("Mode latex is " + engine);
        if (engine.equals(context.getString(R.string.katex))) {
            setEngine(KATEX);
        } else {
            setEngine(MATHJAX);
        }
    }
}
