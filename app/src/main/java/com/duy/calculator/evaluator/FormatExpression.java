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

import android.content.Context;

import com.duy.calculator.evaluator.base.BaseModule;
import com.duy.calculator.evaluator.base.Evaluator;
import com.duy.calculator.tokenizer.Tokenizer;

import java.util.regex.Pattern;

import static com.duy.calculator.evaluator.LogicEvaluator.ERROR_INDEX_STRING;

public class FormatExpression {

    private String TAG = FormatExpression.class.getName();

    /**
     * clean expression
     */
    public static String cleanExpression(String expr, Tokenizer tokenizer) {
        expr = tokenizer.getNormalExpression(expr);
        return cleanExpression(expr);
    }

    public static String cleanExpression(String expr) {
        expr = expr.replace(ERROR_INDEX_STRING, "");
        while (expr.contains("--")) expr = expr.replace("--", "+");
        while (expr.contains("++")) expr = expr.replace("++", "+");
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
     * clean expression
     * <p>
     * append parentheses
     * <p>
     * replace char (div mul add, sub...)
     *
     * @param expr - input expression
     * @return - math language
     */
    public static String cleanExpression(String expr, Context context) {
        return cleanExpression(expr, new Tokenizer());
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

    public static String clean(String string) {
        return string.replaceAll(Pattern.quote("\\"), "").replaceAll("\\s+", "");
    }

    /**
     * Insert html superscripts so that exponents appear properly.
     * <p/>
     * ie. 2^3 becomes 2<sup>3</sup>
     */
    public String insertSupScripts(String input) {
        final StringBuilder formattedInput = new StringBuilder();

        int sub_open = 0;
        int sub_closed = 0;
        int paren_open = 0;
        int paren_closed = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == Constants.POWER_UNICODE) {
                formattedInput.append("<sup>");
                if (sub_open == 0) formattedInput.append("<small>");
                sub_open++;
                if (i + 1 == input.length()) {
                    formattedInput.append(c);
                    if (sub_closed == 0) formattedInput.append("</small>");
                    formattedInput.append("</sup>");
                    sub_closed++;
                } else {
                    formattedInput.append(Constants.POWER_PLACEHOLDER);
                }
                continue;
            }

            if (sub_open > sub_closed) {
                if (paren_open == paren_closed) {
                    // Decide when to break the <sup> started by ^
                    if (c == Constants.PLUS_UNICODE // 2^3+1
                            || (c == Constants.MINUS_UNICODE && input.charAt(i - 1) != Constants.POWER_UNICODE) // 2^3-1
                            || c == Constants.MUL_UNICODE // 2^3*1
                            || c == Constants.DIV_UNICODE // 2^3/1
                            || c == Constants.EQUAL_UNICODE // X^3=1
                            || (c == Constants.LEFT_PAREN && (Evaluator.isDigit(input.charAt(i - 1)) || input.charAt(i - 1) == Constants.RIGHT_PAREN)) // 2^3(1)
                            // or
                            // 2^(3-1)(0)
                            || (Evaluator.isDigit(c) && input.charAt(i - 1) == Constants.RIGHT_PAREN) // 2^(3)1
                            || (!Evaluator.isDigit(c) && Evaluator.isDigit(input.charAt(i - 1))) && c != Constants.DECIMAL_POINT) { // 2^3log(1)
                        while (sub_open > sub_closed) {
                            if (sub_closed == 0) formattedInput.append("</small>");
                            formattedInput.append("</sup>");
                            sub_closed++;
                        }
                        sub_open = 0;
                        sub_closed = 0;
                        paren_open = 0;
                        paren_closed = 0;
                        if (c == Constants.LEFT_PAREN) {
                            paren_open--;
                        } else if (c == Constants.RIGHT_PAREN) {
                            paren_closed--;
                        }
                    }
                }
                if (c == Constants.LEFT_PAREN) {
                    paren_open++;
                } else if (c == Constants.RIGHT_PAREN) {
                    paren_closed++;
                }
            }
            formattedInput.append(c);
        }
        while (sub_open > sub_closed) {
            if (sub_closed == 0) formattedInput.append("</small>");
            formattedInput.append("</sup>");
            sub_closed++;
        }
        return formattedInput.toString();
    }

    /**
     * Add comas to an equation or mResult
     * <p/>
     * 12345 becomes 12,345
     * <p/>
     * 10101010 becomes 1010 1010
     * <p/>
     * ABCDEF becomes AB CD EF
     */
    public String addComas(Evaluator evaluator, String text) {
        return addComas(evaluator, text, -1).replace(BaseModule.SELECTION_HANDLE + "", "");
    }

    /**
     * Add comas to an equation or mResult.
     * A temp character (BaseModule.SELECTION_HANDLE) will be added
     * where the selection handle should be.
     * <p/>
     * 12345 becomes 12,345
     * <p/>
     * 10101010 becomes 1010 1010
     * <p/>
     * ABCDEF becomes AB CD EF
     */
    public String addComas(Evaluator evaluator, String text, int selectionHandle) {
        return evaluator.getBaseModule().groupSentence(text, selectionHandle);
    }

    public String format(Evaluator evaluator, String text) {
        return appendParenthesis(insertSupScripts(addComas(evaluator, text)));
    }

}
