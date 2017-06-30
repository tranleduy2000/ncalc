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

import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.FormatExpression;

/**
 * Created by DUy on 29-Dec-16.
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
