package com.example.duy.calculator.item_math_type;

import android.content.Context;

import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.FormatExpression;

/**
 * Created by DUy on 29-Dec-16.
 */

public class ExpressionItem extends IExprInput {
    public static final String TAG = ExpressionItem.class.getSimpleName();
    private String expr;

    public ExpressionItem(String expr) {
        this.expr = FormatExpression.cleanExpression(expr);
    }

    @Override
    public String getError(BigEvaluator evaluator, Context applicationContext) {
        return null;
    }

    public String getInput() {
        return expr;
    }

    @Override
    public boolean isError(BigEvaluator evaluator) {
        return evaluator.isSyntaxError(this.expr);
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return this.expr;
    }
}
