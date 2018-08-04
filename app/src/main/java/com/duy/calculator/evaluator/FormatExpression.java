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

package com.duy.calculator.evaluator;

import com.duy.calculator.symja.tokenizer.ExpressionTokenizer;

public class FormatExpression {

    public static String cleanExpression(String expr, ExpressionTokenizer tokenizer) {
        expr = tokenizer.getNormalExpression(expr);
        return cleanExpression(expr);
    }

    public static String cleanExpression(String expr) {
        expr = expr.replace("÷", "/");
        expr = expr.replace("×", "*");
        expr = expr.replace("√", "Sqrt");
        expr = expr.replace("∑", "Sum");
        expr = expr.replace("∫", "Int");
        expr = expr.replace("∞", "Infinity");
        expr = expr.replace("π", "Pi");
        expr = expr.replace("∏", "Product");
        expr = appendParenthesis(expr);
        return expr;
    }


    /**
     * Append parenthesis at the end of unclosed functions
     * <p/>
     * ie. sin(90 becomes sin(90)
     */
    public static String appendParenthesis(String input) {
        int open = 0, close = 0, open2 = 0, close2 = 0, open3 = 0, close3 = 0;
        for (char c : input.toCharArray()) {
            switch (c) {
                case '(':
                    open++;
                    break;
                case ')':
                    close++;
                    break;
                case '[':
                    open2++;
                    break;
                case ']':
                    close2++;
                    break;
                case '{':
                    open3++;
                    break;
                case '}':
                    close3++;
                    break;
            }
        }
        StringBuilder result = new StringBuilder(input);
        if (open > close) {
            for (int j = 0; j < open - close; j++) {
                result.append(")");
            }
        } else if (close > open) {
            for (int j = 0; j < close - open; j++) {
                result.insert(0, "(");
            }
        }
        if (open2 > close2)
            for (int j = 0; j < open2 - close2; j++) {
                result.append("]");
            }
        else {
            for (int j = 0; j < close2 - open2; j++) {
                result.insert(0, "[");
            }
        }
        if (open3 > close3) {
            for (int j = 0; j < open3 - close3; j++) {
                result.append("}");
            }
        } else
            for (int j = 0; j < close3 - open3; j++) {
                result.insert(0, "{");
            }
        return result.toString();
    }

}
