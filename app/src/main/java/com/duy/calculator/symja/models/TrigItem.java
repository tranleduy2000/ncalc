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

package com.duy.calculator.symja.models;

import android.util.Log;

import static com.duy.calculator.symja.models.TrigItem.TRIG_TYPE.EXPAND;
import static com.duy.calculator.symja.models.TrigItem.TRIG_TYPE.EXPONENT;
import static com.duy.calculator.symja.models.TrigItem.TRIG_TYPE.REDUCE;
import static com.duy.calculator.symja.activities.TrigActivity.TAG;

/**
 * Trigonometric Item
 * Created by Duy on 31-Jan-17.
 */

public class TrigItem extends ExpressionItem {
    private int mType = EXPAND;

    public TrigItem(String expr) {
        super(expr);
    }

    public String getInput(int type) {
        Log.d(TAG, "getInput: " + type);
        switch (type) {
            case EXPAND:
                return "TrigExpand(" + getExpr() + ")";
            case REDUCE:
                return "TrigReduce(" + getExpr() + ")";
            case EXPONENT:
                return "TrigToExp(" + getExpr() + ")";
            default:
                return getExpr();
        }
    }

    @Override
    public String getInput() {
        return getInput(mType);
    }

    public void setType(int type) {
        this.mType = type;
    }

    public static class TRIG_TYPE {
        public static final int EXPAND = 1;
        public static final int REDUCE = 2;
        public static final int EXPONENT = 3;
    }
}
