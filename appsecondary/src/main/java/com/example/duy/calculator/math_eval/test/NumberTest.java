package com.example.duy.calculator.math_eval.test;

import android.util.Log;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.interfaces.IExpr;

public class NumberTest {
    static final String TAG = "NumberTest";

    public void exec() {
        EvalEngine evalEngine = new EvalEngine();
        evalEngine.setNumericMode(true, 5);
        IExpr iExpr = evalEngine.parse("sqrt(2)*sqrt(2)");
        iExpr = evalEngine.evaluate(iExpr);
        Log.i(TAG, "exec: " + iExpr.toString());
        iExpr = evalEngine.evalWithoutNumericReset(iExpr);
        Log.i(TAG, "exec: " + iExpr.toString());
    }
}