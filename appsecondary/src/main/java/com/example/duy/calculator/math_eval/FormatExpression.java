package com.example.duy.calculator.math_eval;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import com.example.duy.calculator.math_eval.base.BaseModule;
import com.example.duy.calculator.math_eval.base.Evaluator;

import static com.example.duy.calculator.math_eval.LogicEvaluator.ERROR_INDEX_STRING;

public class FormatExpression {

    private String TAG = FormatExpression.class.getName();

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
    public static String cleanExpression(String expr, Tokenizer tokenizer) {
        expr = expr.replace(ERROR_INDEX_STRING, "");
        expr = tokenizer.getNormalExpression(expr);
        expr = appendParenthesis(expr);
        while (expr.contains("--")) expr = expr.replace("--", "+");
        while (expr.contains("++")) expr = expr.replace("++", "+");
        if (expr.length() > 0)
            while (expr.length() > 0 &&
                    Evaluator.isOperator(Character.toString(expr.charAt(expr.length() - 1)))) {
                expr = expr.substring(0, expr.length() - 1);
            }
        return expr;
    }

    public static String cleanExpression(String expr) {
        expr = expr.replace(ERROR_INDEX_STRING, "");
        expr = appendParenthesis(expr);
        while (expr.contains("--")) expr = expr.replace("--", "+");
        while (expr.contains("++")) expr = expr.replace("++", "+");
        if (expr.length() > 0)
            while (expr.length() > 0 &&
                    Evaluator.isOperator(Character.toString(expr.charAt(expr.length() - 1)))) {
                expr = expr.substring(0, expr.length() - 1);
            }
        return expr;
    }

    /**
     * convert primitive text to html text
     *
     * @param source - text source
     * @return - text in html
     */
    public static String toHtml(String source) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(source);
        }
        return result.toString();
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
        return cleanExpression(expr, new Tokenizer(context));
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
        if (open > close) {
            for (int j = 0; j < open - close; j++) {
                input += ")";
            }
        } else if (close > open) {
            for (int j = 0; j < close - open; j++) {
                input = "(" + input;
            }
        }
        if (open2 > close2)
            for (int j = 0; j < open2 - close2; j++) input += "]";
        else
            for (int j = 0; j < close2 - open2; j++) input = "[" + input;
        if (open3 > close3)
            for (int j = 0; j < open3 - close3; j++) input += "}";
        else
            for (int j = 0; j < close3 - open3; j++) input = "{" + input;
        return input;
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
