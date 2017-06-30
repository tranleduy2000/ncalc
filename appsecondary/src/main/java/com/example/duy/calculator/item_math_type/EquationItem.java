package com.example.duy.calculator.item_math_type;

import android.content.Context;

import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.FormatExpression;

import java.util.regex.Pattern;

/**
 * Created by DUy on 29-Dec-16.
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

public class EquationItem extends IExprInput {
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
    public boolean isError(BigEvaluator evaluator) {
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
    public String getError(BigEvaluator evaluator, Context applicationContext) {
        return evaluator.getError(getInput());
    }
}
