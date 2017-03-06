/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.example.duy.calculator.math_eval;


import android.util.Log;

import com.example.duy.calculator.math_eval.base.Base;
import com.example.duy.calculator.math_eval.base.Evaluator;

import org.javia.arity.SyntaxException;


public abstract class LogicEvaluator {
    public static final int RESULT_OK = 1;
    public static final int RESULT_ERROR = -1;
    public static final int INPUT_EMPTY = 2;
    public static final int RESULT_ERROR_WITH_INDEX = 3;
    public static final int RESULT_FAILED = 4;
    public static final String ERROR_INDEX_STRING = "?";
    protected final Tokenizer mTokenizer;
    private final Evaluator mEvaluator;
    private String TAG = LogicEvaluator.class.getName();

    public LogicEvaluator(Tokenizer tokenizer) {
        mEvaluator = new Evaluator();
        mTokenizer = tokenizer;
    }

    public Tokenizer getTokenizer() {
        return mTokenizer;
    }

    public void evaluateBase(CharSequence expr, EvaluateCallback callback) {
        evaluate(expr.toString(), callback);
    }

    public abstract BigEvaluator getEvaluator();

    private void evaluate(String expr, EvaluateCallback callback) {
        expr = FormatExpression.cleanExpression(expr, mTokenizer);
        Log.d(TAG, "evaluate: " + expr);
        try {
            String result = mEvaluator.evaluate(expr, getEvaluator());
            result = mTokenizer.getLocalizedExpression(result);
            Log.d(TAG, "evaluate: mResult = " + result);
            callback.onEvaluate(expr, result, LogicEvaluator.RESULT_OK);
        } catch (SyntaxException e) {
            String result = e.message + ";" + e.position;
            callback.onEvaluate(expr, result, LogicEvaluator.RESULT_ERROR_WITH_INDEX);
        } catch (Exception e) {
        }
    }

    public void setBase(String expr, Base base, EvaluateCallback callback) {
        try {
            String result;
            result = mEvaluator.getBaseModule().setBase(expr, base);
            if (result.isEmpty()) {
                callback.onEvaluate(expr, result, INPUT_EMPTY);
                return;
            }
            callback.onEvaluate(expr, result, RESULT_OK);
        } catch (SyntaxException e) {
//            e.printStackTrace();
        }

    }

    public Evaluator getSolver() {
        return mEvaluator;
    }




    public interface EvaluateCallback {
        void onEvaluate(String expr, String result, int errorResourceId);
    }
}