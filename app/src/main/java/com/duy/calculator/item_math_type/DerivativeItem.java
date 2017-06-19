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
 * Created by DUy on 01-Jan-17.
 */

public class DerivativeItem extends AExprInput {
    private static final String TAG = "DerivativeItem";
    private String input;
    private String var = "x";
    private String level = "1";

    public DerivativeItem(String input, String var) {
        this.input = FormatExpression.cleanExpression(input);
        //if var = "", do not set var
        if (!var.isEmpty()) this.var = var;
    }

    public DerivativeItem(String input, String var, String level) {
        this.input = FormatExpression.cleanExpression(input);
        if (!var.isEmpty()) this.var = var;     //if var = "", do not set var
        this.level = level;
    }

    public DerivativeItem(String input) {
        this.input = FormatExpression.cleanExpression(input);
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
        String res = Constants.DERIVATIVE +
                Constants.LEFT_PAREN +
                input + "," + "{" + var + "," + level + "}" +
                Constants.RIGHT_PAREN;
        //pri(2x + sin(x), x)
        Log.d(TAG, "getInput: " + res);
        return res;
    }

    @Override
    public String toString() {
        return this.getInput();
    }

    @Override
    public String getError(BigEvaluator evaluator, Context applicationContext) {
        return null;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
