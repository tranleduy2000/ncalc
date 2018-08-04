/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.duy.calculator.evaluator.base;


import com.duy.calculator.evaluator.FormatExpression;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.symja.tokenizer.ExpressionTokenizer;

import org.javia.arity.SyntaxException;


public abstract class LogicEvaluator {
    public static final int RESULT_OK = 1;
    public static final int RESULT_ERROR = -1;
    public static final int INPUT_EMPTY = 2;
    public static final int RESULT_ERROR_WITH_INDEX = 3;
    public static final String ERROR_INDEX_STRING = "?";
    protected final ExpressionTokenizer mTokenizer;
    private final Evaluator mEvaluator;

    public LogicEvaluator() {
        mEvaluator = new Evaluator();
        mTokenizer = new ExpressionTokenizer();
    }

    public ExpressionTokenizer getTokenizer() {
        return mTokenizer;
    }

    public void evaluateBase(CharSequence expr, EvaluateCallback callback) {
        evaluate(expr.toString(), callback);
    }

    public abstract MathEvaluator getEvaluator();

    private void evaluate(String expr, EvaluateCallback callback) {
        expr = FormatExpression.cleanExpression(expr, mTokenizer);
        try {
            String result = mEvaluator.evaluate(expr, getEvaluator());
            result = mTokenizer.getLocalizedExpression(result);
            callback.onEvaluated(expr, result, LogicEvaluator.RESULT_OK);
        } catch (SyntaxException e) {
            String result = e.message + ";" + e.position;
            callback.onEvaluated(expr, result, LogicEvaluator.RESULT_ERROR_WITH_INDEX);
        } catch (Exception e) {
        }
    }

    public void setBase(String expr, Base base, EvaluateCallback callback) {
        try {
            String result;
            result = mEvaluator.getBaseModule().setBase(expr, base);
            if (result.isEmpty()) {
                callback.onEvaluated(expr, result, INPUT_EMPTY);
                return;
            }
            callback.onEvaluated(expr, result, RESULT_OK);
        } catch (SyntaxException e) {
        }
    }

    public Evaluator getBaseEvaluator() {
        return mEvaluator;
    }


    public interface EvaluateCallback {
        void onEvaluated(String expr, String result, int errorResourceId);

        void onCalculateError(Exception e);
    }
}