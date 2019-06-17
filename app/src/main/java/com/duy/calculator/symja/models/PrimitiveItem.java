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

import android.content.Context;
import android.util.Log;

import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.Constants;
import com.duy.calculator.evaluator.FormatExpression;

/**
 * Created by Duy on 29-Dec-16.
 */

public class PrimitiveItem extends ExprInput {
    @Override
    public String getError(MathEvaluator evaluator, Context applicationContext) {
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
    public boolean isError(MathEvaluator evaluator) {
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
