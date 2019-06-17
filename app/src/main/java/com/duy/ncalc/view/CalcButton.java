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

package com.duy.ncalc.view;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;

import com.duy.ncalc.userinterface.FontManager;

/**
 * Created by Duy on 21-Jan-17.
 */

public class CalcButton extends android.support.v7.widget.AppCompatButton {

    public CalcButton(Context context) {
        super(context);
        setup(context);

    }

    public CalcButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);

    }

    public CalcButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);

    }

    private void setup(Context context) {
        setTypeface(FontManager.loadTypefaceFromAsset(context));

        String text = getText().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            setText(Html.fromHtml(text));
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            animate().alpha(1f).start();
        } else {
            animate().alpha(0.2f).start();
        }
        super.setEnabled(enabled);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            animate().alpha(1f).start();
        } else {
            animate().alpha(0.3f).start();
        }
    }
}
