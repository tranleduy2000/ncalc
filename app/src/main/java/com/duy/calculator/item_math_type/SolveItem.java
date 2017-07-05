/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.calculator.item_math_type;

import android.content.Context;

import com.duy.calculator.evaluator.Constants;
import com.duy.calculator.evaluator.FormatExpression;
import com.duy.calculator.evaluator.MathEvaluator;

/**
 * Created by Duy on 01-Jan-17.
 */

public class SolveItem extends ExprInput {
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
    public String getError(MathEvaluator evaluator, Context applicationContext) {
        return null;
    }

    /**
     * process input
     *
     * @param inp - expression
     */
    private void processInput(String inp) {
        while (inp.contains("==")) inp = inp.replace("==", "="); //clear ==
        if (inp.contains("X")) {
            var = "X";
        } else {
            var = "x";
        }

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
    public boolean isError(MathEvaluator evaluator) {
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
     *
     * @return - String
     */
    public String getExpr() {
        return this.leftExpr + "==" + rightExpr;
    }
}
