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

package com.duy.calculator.evaluator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.duy.calculator.DLog;
import com.duy.calculator.R;
import com.duy.calculator.data.CalculatorSetting;
import com.duy.calculator.item_math_type.StepItem;
import com.duy.calculator.tokenizer.Tokenizer;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.interfaces.AbstractEvalStepListener;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import static com.duy.calculator.evaluator.FormatExpression.clean;
import static com.duy.calculator.evaluator.Utility.isAcceptResultDecimal;
import static com.duy.calculator.evaluator.Utility.isAcceptResultFraction;
import static com.duy.calculator.evaluator.exceptions.ExceptionManager.getExceptionMessage;


/**
 * Evaluator
 * <p>
 * - Solve equation
 * - Factor Prime
 * - Integrate
 * - Derivative
 * ...
 * <p>
 * evaluate expression
 * <p>
 * convert text to text
 * Created by DUy on 21-Nov-16.
 */

public class MathEvaluator extends LogicEvaluator {
    private static final String ANS_VAR = "ans";
    private static final String TAG = "BigEvaluator";

    /**
     * evaluate engine
     */
    private static final ExprEvaluator EVAL_ENGINE;

    /**
     * convert expr to latex
     */
    private static final TeXUtilities TEX_ENGINE;

    static {
        //init evaluator
        EVAL_ENGINE = new ExprEvaluator();

        String combination = "C(n_, k_):=(factorial(Ceiling(n)) / " + "(factorial(Ceiling(k)) * " +
                "factorial(Ceiling(n - k))))";
        EVAL_ENGINE.evaluate(combination);

        String binomial = "P(n_, k_):=Binomial(n, k)";
        EVAL_ENGINE.evaluate(binomial);

        String cbrt = "cbrt(x_):= x^(1/3)";
        EVAL_ENGINE.evaluate(cbrt);

        TEX_ENGINE = new TeXUtilities(EVAL_ENGINE.getEvalEngine(), true);
    }

    /**
     * set value <code>true</code> if use mode result as fraction vaue
     * set value <code>false</code> if use mode result as decimal value
     */
    private boolean isFraction = true;
    //android context
    private Context mContext;
    /**
     * SharedPreferences for save value variable
     */
    private CalculatorSetting mCalculatorSetting;


    private MathEvaluator(Context context) {
        super(new Tokenizer(context));
        this.mContext = context;
        this.mCalculatorSetting = new CalculatorSetting(context);
        loadSetting();
    }

    public static ExprEvaluator getInstance() {
        return EVAL_ENGINE;
    }

    /**
     * @param exprInput The expression to doCalculate
     * @return The  value of the expression
     * @throws IllegalArgumentException If the user has input a invalid expression
     */
    public static String evaluateSimple(String exprInput, EvaluateConfig config) {
        IExpr result = MathEvaluator.getInstance().evaluate(exprInput);
        DLog.d(TAG, "Input expr = " + exprInput + "; result = " + result);

        if (result.isNumber() && !result.isFraction()) {
            return clean(result.toString());
        }

        if (!isAcceptResultFraction(result)) {
            String expr = "N(" + result.toString() + "," + config.getRoundTo() + ")";
            result = MathEvaluator.getInstance().evaluate(expr);
            if (isAcceptResultDecimal(result)) {
                return clean(result.toString());
            } else {
                throw new UnsupportedOperationException(clean(result.toString()));
            }
        } else {
            if (config.getEvaluateMode() == EvaluateConfig.FRACTION) {
                return (clean(result.toString()));
            } else {
                String expr = "N(" + result.toString() + "," + config.getRoundTo() + ")";
                result = MathEvaluator.getInstance().evaluate(expr);
                if (result.isNumeric()) {
                    if (isAcceptResultDecimal(result)) {

                        return (clean(result.toString()));
                    } else {
                        throw new UnsupportedOperationException(result.toString());
                    }
                } else {

                    throw new UnsupportedOperationException(result.toString());
                }
            }
        }
    }

    /**
     * return BigEvaluator
     *
     * @param context - android context
     * @return - mEvaluator
     */
    public static MathEvaluator newInstance(Context context) {
        return new MathEvaluator(context);
    }

    /**
     * restore data for evaluator
     * such as number of precision, mode fraction/real, ...
     */
    public void loadSetting() {
        isFraction = mCalculatorSetting.useFraction();

        String ans_val = mCalculatorSetting.getString(ANS_VAR, "0");
        define("ans", ans_val);
    }

    /**
     * save user setting
     * save last answer value
     */
    public void saveSetting() {
        SharedPreferences.Editor editor = mCalculatorSetting.getEditor();
        if (EVAL_ENGINE.getVariable(ANS_VAR).toString() == null) {
            editor.putString(ANS_VAR, "0");
        } else {
            editor.putString(ANS_VAR, EVAL_ENGINE.getVariable(ANS_VAR).toString());
        }
        editor.apply();
    }

    @Override
    public MathEvaluator getEvaluator() {
        return this;
    }


    public ExprEvaluator getEvalUtils() {
        return EVAL_ENGINE;
    }


    /**
     * evaluate expression, the result will be return callback via interface
     * #EvaluateCallback
     *
     * @param expression - input expression String Obe
     * @param callback   - interface for call back event
     * @ {@link com.duy.calculator.evaluator.LogicEvaluator.EvaluateCallback}
     */
    public void evaluateWithResultNormal(String expression, LogicEvaluator.EvaluateCallback callback) {
        //if input is empty, do not working
        if (expression.isEmpty()) {
            callback.onEvaluated(expression, "", LogicEvaluator.INPUT_EMPTY);
            return;
        }

        expression = FormatExpression.cleanExpression(expression, mTokenizer);
        expression = addUserDefinedVariable(expression); //$ans = ...

        try {
            IExpr res;
            //if mode is real
            if (!isFraction) {
                res = EVAL_ENGINE.evaluate("N(" + expression + ")");
                if (res.isNumeric()) {
                    try {
                        //format comma, dot
                        String sFormat;
                        int numDecimal = 10;
                        sFormat = DecimalFactory.round(res.toString(), numDecimal);
                        callback.onEvaluated(expression, sFormat, LogicEvaluator.RESULT_OK);
                    } catch (Exception e) {
                        // if not is numeric,
                        // it will be throw exception, although result as true
                        callback.onEvaluated(expression, res.toString(), LogicEvaluator.RESULT_OK);
                    }
                } else {
                    //if not is numeric, callback
                    //such as 2x + 1 + 2 = 2x + 3
                    callback.onEvaluated(expression, res.toString(), LogicEvaluator.RESULT_OK);
                }
            } else { //mode is fraction
                res = EVAL_ENGINE.evaluate(expression);
                callback.onEvaluated(expression, res.toString(), LogicEvaluator.RESULT_OK);
            }
        } catch (SyntaxError e) {
            callback.onEvaluated(expression, getExceptionMessage(mContext, e, isFraction),
                    LogicEvaluator.RESULT_ERROR_WITH_INDEX);
        } catch (Exception e) {
            callback.onEvaluated(expression, getExceptionMessage(mContext, e, isFraction),
                    LogicEvaluator.RESULT_ERROR);
        }
    }

    /**
     * evaluate expression, the result will be return callback via interface
     * #EvaluateCallback
     *
     * @param expression - input expression String Obe
     * @param callback   - interface for call back event
     * @ {@link com.duy.calculator.evaluator.LogicEvaluator.EvaluateCallback}
     */
    public void evaluateWithResultNormal(String expression, LogicEvaluator.EvaluateCallback callback,
                                         EvaluateConfig config) {
        //if input is empty, do not working
        if (expression.isEmpty()) {
            callback.onEvaluated(expression, "", LogicEvaluator.INPUT_EMPTY);
            return;
        }

        expression = FormatExpression.cleanExpression(expression, mTokenizer);
        expression = addUserDefinedVariable(expression); //$ans = ...

        Log.d(TAG, "evaluateWithResultNormal: " + expression);
        try {
            IExpr res;
            //if mode is real
            if (!isFraction) {
                res = EVAL_ENGINE.evaluate("N(" + expression + ")");
                Log.d(TAG, "evaluateWithResultNormal: numeric " + expression);
                if (res.isNumeric()) {
                    try {
                        //format comma, dot
                        String sFormat;
                        int numDecimal = 10;
                        sFormat = DecimalFactory.round(res.toString(), numDecimal);
//                        sFormat = res.toString();
                        Log.i(TAG, "evaluateWithResultNormal: isNumeric " + res.toString()
                                + " = " + sFormat);
                        callback.onEvaluated(expression, sFormat, LogicEvaluator.RESULT_OK);
                    } catch (Exception e) {
                        //  e.printStackTrace();
                        // if not is numeric,
                        // it will be throw exception, although result as true
                        callback.onEvaluated(expression, res.toString(), LogicEvaluator.RESULT_OK);
                    }
                } else {
                    //if not is numeric, callback
                    //such as 2x + 1 + 2 = 2x + 3
                    callback.onEvaluated(expression, res.toString(), LogicEvaluator.RESULT_OK);
                }
            } else { //mode is fraction
                Log.d(TAG, "evaluateWithResultNormal: fraction " + expression);

                res = EVAL_ENGINE.evaluate(expression);
                callback.onEvaluated(expression, res.toString(), LogicEvaluator.RESULT_OK);
            }
        } catch (SyntaxError e) {
            callback.onEvaluated(expression, getExceptionMessage(mContext, e, isFraction), LogicEvaluator.RESULT_ERROR_WITH_INDEX);
        } catch (Exception e) {
            callback.onEvaluated(expression, getExceptionMessage(mContext, e, isFraction), LogicEvaluator.RESULT_ERROR);
        }
    }

    private String addUserDefinedVariable(String expression) {
        if (!expression.contains("ans")) return expression;
        try {
            if (EVAL_ENGINE.getVariable("ans").toString().equals("null")) {
                expression = expression.replace("ans", "(0)");
            } else {
                expression = expression.replace("ans", "(" + EVAL_ENGINE.getVariable("ans") + ")");
            }
        } catch (Exception e) {
            expression = expression.replace("ans", "(0)");
        }
        return expression;
    }

    /**
     * get string from resource
     *
     * @param id - resource id
     * @return - String object
     */
    public String getString(int id) {
        return mContext.getString(id);
    }

    /**
     * evaluate expression, the result will be return String Object
     *
     * @param expression - input expression
     * @return - String
     */
    public String evaluateWithResultNormal(String expression) {
        final String[] res = {""};
        evaluateWithResultNormal(expression, new EvaluateCallback() {
            @Override
            public void onEvaluated(String expr, String result, int errorResourceId) {
                res[0] = result;
            }

            @Override
            public void onCalculateError(Exception e) {

            }
        });
        return res[0];
    }


    /**
     * use fraction in result
     *
     * @return - boolean
     */
    public boolean isFraction() {
        return isFraction;
    }

    public void setFraction(boolean fraction) {
        isFraction = fraction;
    }

    /**
     * return derivative of function
     *
     * @param expr     - expression
     * @param callback - interface for call back
     */
    public void derivativeFunction(String expr, final EvaluateCallback callback) {
        final boolean[] next = new boolean[1];
        final String[] resFraction = new String[1];
        final String[] resReal = new String[1];
        resFraction[0] = "";
        resReal[0] = "";

        boolean last = isFraction;
        setFraction(true);
        //result as fraction
        evaluateWithResultAsTex(expr, new EvaluateCallback() { //catch error
            @Override
            public void onEvaluated(final String expr0, String result0, final int errorResourceId0) {
                if (errorResourceId0 == LogicEvaluator.RESULT_OK) {
                    resFraction[0] = result0;
                    next[0] = true;
                } else {
                    //return error
                    next[0] = false;
                    callback.onEvaluated(expr0, result0, LogicEvaluator.RESULT_ERROR);
                }
            }

            @Override
            public void onCalculateError(Exception e) {

            }
        });

        //result as real number

        if (next[0]) {
            String inp = Constants.SIMPLIFY
                    + Constants.LEFT_PAREN
                    + evaluateWithResultNormal(expr)
                    + Constants.RIGHT_PAREN;
            setFraction(false);
            evaluateWithResultAsTex(inp, new EvaluateCallback() {
                @Override
                public void onEvaluated(String expr, String result, int errorResourceId) {
                    if (errorResourceId == LogicEvaluator.RESULT_OK) {
                        resReal[0] = result;
                    }
                }

                @Override
                public void onCalculateError(Exception e) {

                }
            });
        }

        setFraction(last);

        //result
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(resFraction[0])
                .append(Constants.WEB_SEPARATOR)
                .append(resReal[0]);
        if (stringBuilder.toString().contains("log")) {
            stringBuilder.append(Constants.WEB_SEPARATOR) //<hr>
                    .append(getString(R.string.ln_hint)); //log(x) is the natural logarithm
        }

        //call back feedback
        callback.onEvaluated(expr,
                stringBuilder.toString(),
                RESULT_OK);
    }

    /**
     * Solve equation and return string result, and real, and fraction
     *
     * @param expr
     * @param callback
     */
    public void solveEquation(String expr, final EvaluateCallback callback) {
        Log.d(TAG, "solveEquation: " + expr);
        boolean last = isFraction;
        setFraction(true);
        final String[] resFraction = new String[1];
        resFraction[0] = "";

        //result as fraction
        evaluateWithResultNormal(expr, new EvaluateCallback() { //catch error
            @Override
            public void onEvaluated(final String expr0, String result0, final int errorResourceId0) {
                if (errorResourceId0 == LogicEvaluator.RESULT_OK) {
                    resFraction[0] = result0;
                } else {
                    //return error
                    callback.onEvaluated(expr0, result0, LogicEvaluator.RESULT_ERROR);
                }
            }

            @Override
            public void onCalculateError(Exception e) {

            }
        });

        if (resFraction[0].toLowerCase().contains("solve")) {
            String c = getString(R.string.not_find_root);
            callback.onEvaluated(expr, c, RESULT_OK);
            return;
            //return if EvalEngine can not find solution of the equation
            //solve(x^1/3 = - 1)
        } else if (resFraction[0].contains("{}")) {
            String c = getString(R.string.no_root);
            callback.onEvaluated(expr, c, RESULT_OK);
            return; //return if the equation no root
        }

        //process data fraction
        String sCopy = resFraction[0];
        sCopy = sCopy.replace(" ", "");
        sCopy = sCopy.replace("->", "==");
        int j;
        j = 1;
        ArrayList<String> result = new ArrayList<>();
        for (int i = 1; //{{x->10},{x->2}}
             i < sCopy.length() - 1;
             i++) {
            if (sCopy.charAt(i) == '}') {
                String tmp = sCopy.substring(j + 1, i);
                i += 2;
                j = i;
                result.add(tmp);
                DLog.d("solve " + tmp);
            }
        }
        DLog.i("solve " + result);

        String b = "";
        setFraction(true);

        //puts terms in a sum over a common denominator and cancels factors in the result.
        for (int i = 0; i < result.size(); i++) {
            b += evaluateWithResultAsTex(result.get(i));
        }
        b += Constants.WEB_SEPARATOR;
        setFraction(false);
        for (int i = 0; i < result.size(); i++) {
            b += evaluateWithResultAsTex(result.get(i));
        }
        setFraction(last);

        //call back feedback, result ok
        callback.onEvaluated(expr, b, RESULT_OK);
    }

    /**
     * convert math text to latex
     *
     * @param expr - input
     * @return - out
     */
    public String evaluateWithResultAsTex(String expr) {
        final String[] res = new String[1];
        evaluateWithResultAsTex(expr, new EvaluateCallback() {
            @Override
            public void onEvaluated(String expr, String result, int errorResourceId) {
                res[0] = result;
            }

            @Override
            public void onCalculateError(Exception e) {

            }
        });
        return res[0];
    }

    /**
     * convert math text to latex
     *
     * @param expr - input
     * @return - out
     */
    public void evaluateWithResultAsTex(String expr, EvaluateCallback callback) {
        expr = FormatExpression.cleanExpression(expr, mTokenizer);
        //string for result callback
        StringWriter writer = new StringWriter();
        try {
            if (!isFraction) {
                expr = "N(" + expr + "," + mCalculatorSetting.getPrecision() + ")";
            }

            //$ans = ...
            expr = addUserDefinedVariable(expr);

            IExpr r = EVAL_ENGINE.evaluate(expr);
            // TODO: 29-Jan-17 round result if mode is numeric.
            //if result is numeric, round it, if not round value, result will be wrong
            //such as sqrt(2)*sqrt(2) = 2.0000000000004 -> wrong answer;
            if (r.isNumeric()) {
                TEX_ENGINE.toTeX(r, writer);
            } else {
                TEX_ENGINE.toTeX(r, writer);
            }

            callback.onEvaluated(expr, "$$" + writer.toString() + "$$", LogicEvaluator.RESULT_OK);
        } catch (SyntaxError e) {
            callback.onEvaluated(expr, getExceptionMessage(mContext, e, isFraction),
                    LogicEvaluator.RESULT_ERROR_WITH_INDEX);
        } catch (Exception e) {
            callback.onEvaluated(expr, getExceptionMessage(mContext, e, isFraction),
                    LogicEvaluator.RESULT_ERROR);
        }
    }


    public ExprEvaluator getEvalUtilities() {
        return EVAL_ENGINE;
    }

    public TeXUtilities getTexEngine() {
        return TEX_ENGINE;
    }

    public void define(String var, double value) {
        try {
            EVAL_ENGINE.defineVariable(var, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * define function or variable
     *
     * @param var   - name of function or variable
     * @param value - expression or value
     */
    public void define(String var, String value) {
        try {
            IExpr iExpr = EVAL_ENGINE.evaluate(value);
            EVAL_ENGINE.defineVariable(var, iExpr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * true if is a number
     *
     * @param value
     * @return
     */
    public boolean isNumber(String value) {
        try {
            boolean res = Boolean.valueOf(String.valueOf(EVAL_ENGINE.evaluate("NumberQ(" + value + ")")));
            //     Log.d(TAG, "isNumber: " + value + "  - " + res);
            return res;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * return list variable on expression
     *
     * @param expr input expression
     * @return
     */
    public ArrayList<String> getListVariables(String expr) {
        ArrayList<String> variables = new ArrayList<>();
        try {
            String res = evaluateWithResultNormal("Variables(" + expr + ")");
            res = res.replace("{", "");
            res = res.replace("}", "");
            String tmp[] = res.split(",");
            Collections.addAll(variables, tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return variables;
    }

    /**
     * syntax error checking
     *
     * @param expr - input expression
     * @return - true if error
     */
    public boolean isSyntaxError(String expr) {
        try {
            EVAL_ENGINE.getEvalEngine().parse(expr);
        } catch (SyntaxError e) {
            DLog.printStack(e);
            return true;
        } catch (MathException e) {
            DLog.printStack(e);
            return true;
        } catch (Exception e) {
            DLog.printStack(e);
            return true;
        }
        return false;
    }

    public String getError(String expr) {
        final String[] res = {""};
        evaluateWithResultNormal(expr, new EvaluateCallback() {
            @Override
            public void onEvaluated(String expr, String result, int errorResourceId) {
                if (errorResourceId == LogicEvaluator.RESULT_OK) res[0] = "";
                else res[0] = result;
            }

            @Override
            public void onCalculateError(Exception e) {

            }
        });
        return res[0];
    }


    /**
     * factor prime number
     * such as
     * <p>
     * 8 = 2^3;
     * <p>
     * 10 = 2 * 5
     * <p>
     * output is latex: 20 = 2^{2} \\times 4^{1}
     *
     * @param input    - number integer
     * @param callback - listener for callback when finish evaluate
     */
    public void factorPrime(String input, EvaluateCallback callback) {
        boolean last = isFraction();
        setFraction(true);
        String s = evaluateWithResultNormal("FactorInteger(" + input + ")");
        if (s.toLowerCase().contains("FactorInteger".toLowerCase())) {
            callback.onEvaluated(input, getString(R.string.cannot_factor), RESULT_FAILED);
            return;
        }
        DLog.i("Factor prime: " + s);

        s = s.replace(" ", "");
        int j;
        j = 1;
        String result = "";
        for (int i = 1; //{{7,1},{1427,1}}
             i < s.length() - 1;
             i++) {
            if (s.charAt(i) == '}') {
                String tmp = s.substring(j + 1, i);
                j = i + 2;
                i += 2;
                DLog.i(tmp);
                String arr[] = tmp.split(",");
                result += arr[0];
                result += "^{" + arr[1] + "}";
                result += "\\times "; //character mutiply
            }
        }
        result = result.substring(0, result.length() - "\\times ".length()); //remove dot
        result = "$$=" + result + "$$"; //add input 12 = 2^2 * 3
        callback.onEvaluated(input, result, RESULT_OK);
        DLog.i("factor prime: " + result);
        setFraction(last);
    }

    /**
     * Solve system equations and return string result, and real, and fraction
     *
     * @param expr     - input -> Solve({2x^2 + y == 0, x + 2y == -1},{x, y});
     * @param callback - callback for finish evaluate
     */
    public void solveSystemEquation(String expr, final EvaluateCallback callback) {
        Log.d(TAG, "solveEquation: " + expr);
        boolean last = isFraction;
        setFraction(true);

        final String[] temp = new String[]{""};
        final boolean[] hasNext = {true};
        //result as fraction
        evaluateWithResultNormal(expr, new EvaluateCallback() {
            @Override
            public void onEvaluated(final String input, String result, final int resultState) {
                if (resultState == LogicEvaluator.RESULT_OK) {
                    temp[0] = result;
                    Log.d(TAG, "onEvaluated: " + result);
                } else {
                    //return error
                    callback.onEvaluated(input, result, LogicEvaluator.RESULT_ERROR);
                    hasNext[0] = false;
                }
            }

            @Override
            public void onCalculateError(Exception e) {

            }
        });

        if (!hasNext[0]) return;
        if (temp[0].toLowerCase().contains("solve")) {
            String c = getString(R.string.not_find_root);
            callback.onEvaluated(expr, c, RESULT_OK);
            return;
            //return if EvalEngine can not find solution of the equation
            //solve(x^1/3 = - 1)
        } else if (temp[0].contains("{}")) {
            String c = getString(R.string.no_root);
            callback.onEvaluated(expr, c, RESULT_OK);
            return; //return if the equation no root
        }

        //if input can eval and result
        //process data fraction
        String sCopy = temp[0];
        DLog.i("solve result = " + sCopy);
        /*
         * solve({2x^2 + y ==0, x + 2y == -1}, {x, y})
         * {{x->1/8-Sqrt(17)/8,y->-9/16+Sqrt(17)/16},{x->1/8+Sqrt(17)/8,y->-9/16-Sqrt(17)/16}}
         */
        ArrayList<String> result = new ArrayList<>();
        String[] s1 = sCopy.split(Pattern.quote("},{"));
        for (int i = 0; i < s1.length; i++) {
            s1[i] = s1[i].replace("{", "").replace("}", "").replace("->", "==");
            String[] s2 = s1[i].split(Pattern.quote(","));
            for (int j = 0; j < s2.length; j++) {
                Log.d(TAG, "solveSystemEquation: " + s2[j]);
                result.add(s2[j]);
            }
        }

        String b = "";
        //call back feedback, result ok
        for (int i = 0; i < result.size(); i++) {
            b += evaluateWithResultAsTex(result.get(i));
        }
        b += Constants.WEB_SEPARATOR;
        setFraction(false);
        for (int i = 0; i < result.size(); i++) {
            b += evaluateWithResultAsTex(result.get(i));
        }
        setFraction(last);
        callback.onEvaluated(expr, b, RESULT_OK);
    }

    /**
     * Factors the polynomial expression in <b>input</b>
     *
     * @param input    - input expression
     * @param callback - result callback or exception
     */
    public void factorPolynomial(String input, final EvaluateCallback callback) {
        boolean last = isFraction;
        setFraction(true);
        evaluateWithResultAsTex(input, new EvaluateCallback() {
            @Override
            public void onEvaluated(String expr, String result, int errorResourceId) {
                if (result.toLowerCase().contains("factor")) {
                    callback.onEvaluated(expr,
                            getString(R.string.cannot_factor_polynomial),
                            errorResourceId);
                } else {
                    callback.onEvaluated(expr, result, errorResourceId);
                }

            }

            @Override
            public void onCalculateError(Exception e) {

            }
        });
        setFraction(last);
    }

    /**
     * expands out all positive integer powers and products of sums in input.
     *
     * @param input
     * @param callback
     */
    public void expandAll(String input, final EvaluateCallback callback) {
        boolean last = isFraction;
        setFraction(true);
        final String[] res = new String[1];
        evaluateWithResultAsTex("ExpandAll(" + input + ")", new EvaluateCallback() {
            @Override
            public void onEvaluated(String expr, String result, int errorResourceId) {
                res[0] = result;
            }

            @Override
            public void onCalculateError(Exception e) {

            }
        });
        res[0] += Constants.WEB_SEPARATOR;
        setFraction(false);
        evaluateWithResultAsTex("ExpandAll(" + input + ")", new EvaluateCallback() {
            @Override
            public void onEvaluated(String expr, String result, int errorResourceId) {
                res[0] += result;
                callback.onEvaluated(expr, res[0], errorResourceId);
            }

            @Override
            public void onCalculateError(Exception e) {

            }
        });
        setFraction(last);
    }

    public void simplifyExpression(String input, final EvaluateCallback callback) {
        boolean last = isFraction;
        setFraction(true);
        final String[] res = new String[1];
        evaluateWithResultAsTex(input, new EvaluateCallback() {
            @Override
            public void onEvaluated(String expr, String result, int errorResourceId) {
                res[0] = result;
            }

            @Override
            public void onCalculateError(Exception e) {

            }
        });
        res[0] += Constants.WEB_SEPARATOR;
        setFraction(false);
        evaluateWithResultAsTex(input, new EvaluateCallback() {
            @Override
            public void onEvaluated(String expr, String result, int errorResourceId) {
                res[0] += result;
                callback.onEvaluated(expr, res[0], errorResourceId);
            }

            @Override
            public void onCalculateError(Exception e) {

            }
        });
        setFraction(last);
    }

    public void minimizeBoolean(String expression, final EvaluateCallback callback) {
        boolean last = isFraction;
        setFraction(true);
        expression = "BooleanMinimize(" + expression + ")";

        evaluateWithResultAsTex(expression, new EvaluateCallback() {
            @Override
            public void onEvaluated(String expr, String result, int errorResourceId) {
                callback.onEvaluated(expr, result, errorResourceId);
            }

            @Override
            public void onCalculateError(Exception e) {

            }
        });
    }

    public void integrateFunction(String input, EvaluateCallback callback) {
        simplifyExpression(input, callback);
    }

    /**
     * @param expression: input expression
     * @return step  by step eval of expression
     */
    public ArrayList<StepItem> getStep(String expression) {
        final ArrayList<StepItem> listStep = new ArrayList<>();

        ExprEvaluator util = new ExprEvaluator();
        EvalEngine engine = util.getEvalEngine();
        engine.setStepListener(new AbstractEvalStepListener() {
            @Override
            public void add(IExpr input, IExpr result, int depth, long l, String s) {
                StringWriter stringWriterInput = new StringWriter();
                TEX_ENGINE.toTeX(input, stringWriterInput);
                StringWriter stringWriterOutput = new StringWriter();
                TEX_ENGINE.toTeX(result, stringWriterOutput);
                listStep.add(new StepItem(stringWriterInput.toString(),
                        stringWriterOutput.toString(), depth));

                Log.d(TAG, "add() called with: input = [" + input + "], result = ["
                        + result + "], i = [" + depth + "], l = [" + l + "], s = [" + s + "]");

            }
        });
        util.evaluate(expression);
        engine.setTraceMode(false);
        return listStep;
    }

}
