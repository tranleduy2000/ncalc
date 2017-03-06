package com.example.duy.calculator.item_math_type;

import android.content.Context;
import android.util.Log;

import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.Constants;
import com.example.duy.calculator.math_eval.FormatExpression;

/**
 * Created by DUy on 01-Jan-17.
 */

public class DerivativeItem extends IExprInput {
    private static final String TAG = "DerivativeItem";
    private String input;
    private String var = "x";
    private String level = "1";

    public DerivativeItem(String input, String var) {
        this.input = FormatExpression.cleanExpression(input);
        //if var = "", do not set var
        if (!var.isEmpty()) this.var = var;
    }

    public DerivativeItem(String input, String var, String level) {
        this.input = FormatExpression.cleanExpression(input);
        if (!var.isEmpty()) this.var = var;     //if var = "", do not set var
        this.level = level;
    }

    public DerivativeItem(String input) {
        this.input = FormatExpression.cleanExpression(input);
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    @Override
    public boolean isError(BigEvaluator evaluator) {
        return evaluator.isSyntaxError(input);
    }

    public String getInput() {
        //build mResult
        String res = Constants.DERIVATIVE +
                Constants.LEFT_PAREN +
                input + "," + "{" + var + "," + level + "}" +
                Constants.RIGHT_PAREN;
        //pri(2x + sin(x), x)
        Log.d(TAG, "getInput: " + res);
        return res;
    }

    @Override
    public String toString() {
        return this.getInput();
    }

    @Override
    public String getError(BigEvaluator evaluator, Context applicationContext) {
        return null;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
