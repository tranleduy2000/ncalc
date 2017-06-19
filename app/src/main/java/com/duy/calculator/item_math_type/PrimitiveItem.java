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

import android.content.Context;
import android.util.Log;

import com.duy.calculator.math_eval.BigEvaluator;
import com.duy.calculator.math_eval.Constants;
import com.duy.calculator.math_eval.FormatExpression;

/**
 * Created by DUy on 29-Dec-16.
 */

public class PrimitiveItem extends AExprInput {
    @Override
    public String getError(BigEvaluator evaluator, Context applicationContext) {
        return null;
    }
    private static final String TAG = "PrimitiveItem";
    private String input;
    private String var = "x";

    public PrimitiveItem(String input, String var) {
        this.input = FormatExpression.appendParenthesis(input);
        this.var = var;
    }

    public PrimitiveItem(String input) {
        this.input = FormatExpression.appendParenthesis(input);
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    @Override
    public boolean isError(BigEvaluator evaluator) {
        return evaluator.isSyntaxError(input);
    }

    public String getInput() {
        //build mResult
        String res = Constants.PRIMITIVE +
                Constants.LEFT_PAREN +
                input + "," + var +
                Constants.RIGHT_PAREN;
        //pri(2x + sin(x), x)
        Log.d(TAG, "getInput: " + res);
        return res;
    }
}
