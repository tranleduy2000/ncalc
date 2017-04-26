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
