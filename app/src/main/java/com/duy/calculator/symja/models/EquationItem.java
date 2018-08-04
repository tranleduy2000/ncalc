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

import com.duy.calculator.evaluator.FormatExpression;
import com.duy.calculator.evaluator.MathEvaluator;

import java.util.regex.Pattern;

/**
 * Created by Duy on 29-Dec-16.
 * <p>
 * Item for Input Equation
 * <p>
 * such as:
 * <p>
 * 2x + 2 = 2;
 * <p>
 * 3x + 2y = 2t;
 * <p>
 * 1/sin(x) + 2cos(pi) = 0;
 */

public class EquationItem extends ExprInput {
    private String left;
    private String right = "0";

    /**
     * constructor
     * <p>
     * 2x = 0
     *
     * @param expr - equation
     */
    public EquationItem(String expr) {
        expr = expr.replace("==", "=");
        String[] s = expr.split(Pattern.quote("="));
        if (s.length == 2) {
            this.left = FormatExpression.cleanExpression(s[0]);
            this.right = FormatExpression.cleanExpression(s[1]);
        } else {
            this.left = FormatExpression.cleanExpression(s[0]);
            this.right = "0";
        }
    }

    /**
     * constructor
     * <p>
     * 2x = y
     *
     * @param left  - left equation
     * @param right - right equation
     */
    public EquationItem(String left, String right) {
        this.left = left;
        this.right = right;
    }


    @Override
    public boolean isError(MathEvaluator evaluator) {
        if (evaluator.isSyntaxError(left)) {
            return true;
        } else if (evaluator.isSyntaxError(right)) {
            return true;
        }
        return false;
    }

    @Override
    public String getInput() {
        return this.left + "==" + this.right;
    }

    @Override
    public String getError(MathEvaluator evaluator, Context applicationContext) {
//        return evaluator.getError(getInput());
        return "";
    }
}
