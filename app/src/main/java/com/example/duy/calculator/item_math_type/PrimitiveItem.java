package com.example.duy.calculator.item_math_type;

import android.content.Context;
import android.util.Log;

import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.Constants;
import com.example.duy.calculator.math_eval.FormatExpression;

/**
 * Created by DUy on 29-Dec-16.
 */

public class PrimitiveItem extends IExprInput {
    @Override
    public String getError(BigEvaluator evaluator, Context applicationContext) {
        return null;
    }
    private static final String TAG = "PrimitiveItem";
    private String input;
    private String var = "x";

    public PrimitiveItem(String input, String var) {
        this.input = FormatExpression.appendParenthesis(input);
        this.var = var;
    }

    public PrimitiveItem(String input) {
        this.input = FormatExpression.appendParenthesis(input);
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
        String res = Constants.PRIMITIVE +
                Constants.LEFT_PAREN +
                input + "," + var +
                Constants.RIGHT_PAREN;
        //pri(2x + sin(x), x)
        Log.d(TAG, "getInput: " + res);
        return res;
    }
}
