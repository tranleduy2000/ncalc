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

import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.Constants;
import com.duy.calculator.evaluator.FormatExpression;

/**
 * Created by Duy on 29-Dec-16.
 */

public class FunctionItem extends ExprInput {
    private String leftExpr = "y";
    private String rightExpr = "";
    public FunctionItem(String rightExpr) {
        this.rightExpr = FormatExpression.appendParenthesis(rightExpr);
    }

    public FunctionItem(String leftExpr, String rightExpr) {
        this.leftExpr = FormatExpression.appendParenthesis(leftExpr);
        this.rightExpr = FormatExpression.appendParenthesis(rightExpr);

    }

    @Override
    public String getError(MathEvaluator evaluator, Context applicationContext) {
        return null;
    }

    public String getLeftExpr() {
        return leftExpr;
    }

    public void setLeftExpr(String leftExpr) {
        this.leftExpr = leftExpr;
    }

    public String getRightExpr() {
        return rightExpr;
    }

    public void setRightExpr(String rightExpr) {
        this.rightExpr = rightExpr;
    }

    @Override
    public boolean isError(MathEvaluator evaluator) {
        boolean b = evaluator.isSyntaxError(leftExpr);
        if (b) return true;

        b = evaluator.isSyntaxError(rightExpr);
        return b;
    }

    public String getInput() {
        return this.toString();
    }

    @Override
    public String toString() {
        return this.leftExpr + Character.toString(Constants.EQUAL_UNICODE) + this.rightExpr;
    }


}
