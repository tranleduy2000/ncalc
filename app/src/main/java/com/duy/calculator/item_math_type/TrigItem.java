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

package com.duy.calculator.item_math_type;

import android.util.Log;

import static com.duy.calculator.item_math_type.TrigItem.TRIG_TYPE.EXPAND;
import static com.duy.calculator.item_math_type.TrigItem.TRIG_TYPE.EXPONENT;
import static com.duy.calculator.item_math_type.TrigItem.TRIG_TYPE.REDUCE;
import static com.duy.calculator.trigonometry.TrigActivity.TAG;

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
