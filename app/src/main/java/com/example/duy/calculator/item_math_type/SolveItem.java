package com.example.duy.calculator.item_math_type;

import android.content.Context;

import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.Constants;
import com.example.duy.calculator.math_eval.FormatExpression;

/**
 * Created by DUy on 01-Jan-17.
 */

public class SolveItem extends IExprInput {
    private String leftExpr = "x";
    private String rightExpr = "0";

    private String var = "x";

    public SolveItem(String inp) {
        processInput(inp);
    }

    public SolveItem(String leftExpr, String rightExpr) {
        this.leftExpr = FormatExpression.appendParenthesis(leftExpr);
        this.rightExpr = FormatExpression.appendParenthesis(rightExpr);
    }

    @Override
    public String getError(BigEvaluator evaluator, Context applicationContext) {
        return null;
    }

    /**
     * process input
     *
     * @param inp - expression
     */
    private void processInput(String inp) {
        //2x + 1 = 2 ....
        if (inp.contains("=")) {
            String[] s = inp.split("=");
            if (s.length >= 2) { //"2x + 1 = 2 = 3" -> only use "2x + 1 = 2"
                if (leftExpr.isEmpty()) leftExpr = "0";
                leftExpr = FormatExpression.appendParenthesis(s[0]);
                if (rightExpr.isEmpty()) rightExpr = "0";
                rightExpr = FormatExpression.appendParenthesis(s[1]);
            } else { //"2x + 1 =" -> index exception because length s = 1
                leftExpr = FormatExpression.appendParenthesis(s[0]);
                rightExpr = "0";
            }
        } else { // 2x + 1
            leftExpr = FormatExpression.appendParenthesis(inp);
            rightExpr = "0";
        }
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
        return Constants.SOLVE + Constants.LEFT_PAREN
                + leftExpr + " == " + rightExpr + " ," + var +
                Constants.RIGHT_PAREN;
    }

    @Override
    public String toString() {
        return this.leftExpr + Character.toString(Constants.EQUAL_UNICODE) + this.rightExpr;
    }

    /**
     * return input expression
     * @return - String
     */
    public String getExpr() {
        return this.leftExpr + "==" + rightExpr;
    }
}
