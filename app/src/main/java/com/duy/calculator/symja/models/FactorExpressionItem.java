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
import com.duy.calculator.evaluator.FormatExpression;

/**
 * Created by Duy on 29-Dec-16.
 */

public class FactorExpressionItem extends ExprInput {
    private String expr;

    public FactorExpressionItem(String expr) {
        this.expr = FormatExpression.cleanExpression(expr);
    }

    @Override
    public String getError(MathEvaluator evaluator, Context applicationContext) {
        return null;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    @Override
    public boolean isError(MathEvaluator evaluator) {
        return false;
    }

    public String getInput() {
        return "Factor(" + expr + ", GaussianIntegers->True)";
    }

    @Override
    public String toString() {
        return this.expr;
    }
}
