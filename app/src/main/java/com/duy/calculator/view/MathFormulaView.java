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

package com.duy.calculator.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import com.duy.calculator.DLog;
import com.duy.calculator.R;

import io.github.kexanie.library.MathView;

import static io.github.kexanie.library.MathView.Engine.KATEX;
import static io.github.kexanie.library.MathView.Engine.MATHJAX;

/**
 * Created by Duy on 26-Jan-17.
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
        String engine = preferenceManager.getString(context.getString(R.string.key_pref_latex_mode), "KaTeX");
        DLog.i("Mode latex is " + engine);
        if (engine.equals(context.getString(R.string.katex))) {
            setEngine(KATEX);
        } else {
            setEngine(MATHJAX);
        }
    }
}
