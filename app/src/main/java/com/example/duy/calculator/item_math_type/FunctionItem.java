package com.example.duy.calculator.item_math_type;

import android.content.Context;

import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.Constants;
import com.example.duy.calculator.math_eval.FormatExpression;

/**
 * Created by DUy on 29-Dec-16.
 */

public class FunctionItem extends IExprInput {
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
    public String getError(BigEvaluator evaluator, Context applicationContext) {
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
    public boolean isError(BigEvaluator evaluator) {
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
