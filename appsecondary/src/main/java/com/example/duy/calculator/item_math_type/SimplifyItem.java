package com.example.duy.calculator.item_math_type;

import android.content.Context;

import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.FormatExpression;

/**
 * Created by DUy on 29-Dec-16.
 */

public class SimplifyItem extends IExprInput {
    private static final String TAG = "SimplifyItem";
    private String expr;

    public SimplifyItem(String expr) {
        this.expr = FormatExpression.appendParenthesis(expr);
//        Log.d(TAG, "SimplifyItem: " + this.mExpression);
    }

    @Override
    public String getError(BigEvaluator evaluator, Context applicationContext) {
        return null;
    }

    public String getInput() {
        return "Simplify(" + expr + ")";
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
