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
import android.util.Log;

import com.duy.calculator.DLog;
import com.duy.calculator.R;
import com.duy.calculator.evaluator.exceptions.ExpressionChecker;
import com.duy.calculator.item_math_type.StepItem;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.expression.IntegerSym;
import org.matheclipse.core.interfaces.AbstractEvalStepListener;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;


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
 * Created by Duy on 21-Nov-16.
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
    private static MathEvaluator INSTANCE = null;

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

        String ceiling = "Ceil(x_):=Ceiling(x)";
        EVAL_ENGINE.evaluate(ceiling);

        TEX_ENGINE = new TeXUtilities(EVAL_ENGINE.getEvalEngine(), true);
    }

    private MathEvaluator() {

    }


    /**
     * @param exprInput The expression to doCalculate
     * @return The  value of the expression
     * @throws IllegalArgumentException If the user has input a invalid expression
     */
    public static IExpr evaluateSimple(String exprInput, EvaluateConfig config) {
        IExpr result = evaluate(exprInput);
        if (result.isNumber() && !result.isFraction()) {
            if (result instanceof IntegerSym) {
                return result;
            } else { //numeric or complex
                return F.num(DecimalFormatter.round(result.toString().replace("\"", ""), config.getRoundTo()));
            }
        }
        if (config.getEvaluateMode() == EvaluateConfig.DECIMAL) {
            result = evaluate("N(" + exprInput + ")");
            if (result.isNumber() && !result.isFraction()) {
                if (result instanceof IntegerSym) {
                    return result;
                } else {
                    return F.num(DecimalFormatter.round(result.toString().replace("\"", ""), config.getRoundTo()));
                }
            }
            return result;
        } else {
            return result;
        }
    }

    /**
     * return BigEvaluator
     *
     * @return - mEvaluator
     */
    public static MathEvaluator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MathEvaluator();
        }
        return INSTANCE;
    }

    public static TeXUtilities getTexEngine() {
        return TEX_ENGINE;
    }

    public static Exception getError(String expr) {
        try {
            EvalEngine.get().parse(expr);
        } catch (Exception e) {
            return e;
        }
        return null;
    }

    public static IExpr evaluate(String exprInput) {
        return EVAL_ENGINE.evaluate(exprInput);
    }

    @Override
    public MathEvaluator getEvaluator() {
        return this;
    }


    /**
     * evaluate expression, the result will be return callback via interface
     * #EvaluateCallback
     *
     * @param expression - input expression S
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

        try {
            IExpr iExpr = evaluateSimple(expression, config);
            callback.onEvaluated(expression, iExpr.toString(), RESULT_OK);
        } catch (Exception e) {
            callback.onCalculateError(e);
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
        return "";
    }

    public String evaluateWithResultNormal(String expression) {
        IExpr iExpr = evaluateSimple(expression,
                EvaluateConfig.newInstance().setEvalMode(EvaluateConfig.FRACTION));
        return iExpr.toString();
    }

    /**
     * return derivative of function
     */
    public String derivativeFunction(String diffStr, EvaluateConfig config) {
        if (config.getEvaluateMode() == EvaluateConfig.DECIMAL) {
            diffStr = "N(" + diffStr + ")";
        }
        String result = evaluateWithResultAsTex(diffStr, config);

        //result
        StringBuilder builder = new StringBuilder();
        builder.append(result);
        if (builder.toString().contains("log")) {
            builder.append(Constants.WEB_SEPARATOR) //<hr>
                    .append(getString(R.string.ln_hint)); //log(x) is the natural logarithm
        }
        return builder.toString();
    }

    /**
     * return derivative of function
     */
    public String evaluateWithResultAsTex(String exprStr, EvaluateConfig config) {
        //if input is empty, do not working
        if (exprStr.isEmpty()) {
            return exprStr;
        }

        exprStr = FormatExpression.cleanExpression(exprStr, mTokenizer);
        exprStr = addUserDefinedVariable(exprStr); //$ans = ...

        ExpressionChecker.checkExpression(exprStr);

        IExpr result = evaluateSimple(exprStr, config);
        return LaTexFactory.toLaTeX(result);
    }

    /**
     * @see MathEvaluator#evaluateWithResultAsTex(String, EvaluateConfig)
     * <p>
     * return derivative of function
     */
    public void evaluateWithResultAsTex(String exprStr, EvaluateCallback callback, EvaluateConfig config) {
        try {
            String result = evaluateWithResultAsTex(exprStr, config);
            callback.onEvaluated(exprStr, result, RESULT_OK);
        } catch (Exception e) {
            callback.onCalculateError(e);
        }
    }

    /**
     * Solve equation and return string result
     */
    public String solveEquation(String solveStr, final EvaluateConfig config, Context context) {
        if (config.getEvaluateMode() == EvaluateConfig.DECIMAL) {
            solveStr = "N(" + solveStr + ")";
        }
        String roots = EVAL_ENGINE.evaluate(solveStr).toString();

        if (roots.toLowerCase().contains("solve")) {
            return context.getString(R.string.not_find_root);
        } else if (roots.contains("{}")) {
            return context.getString(R.string.no_root);
        }

        roots = roots.replaceAll("\\s+", "").replaceAll("->", "==");
        int j = 1;
        ArrayList<String> listRoot = new ArrayList<>();
        for (int i = 1; i < roots.length() - 1; i++) {
            if (roots.charAt(i) == '}') {
                String tmp = roots.substring(j + 1, i);
                i += 2;
                j = i;
                listRoot.add(tmp);
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < listRoot.size(); i++) {
            result.append(evaluateWithResultAsTex(listRoot.get(i), config));
        }
        return result.toString();
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
     */
    public boolean isNumber(String value) {
        try {
            return Boolean.valueOf(String.valueOf(EVAL_ENGINE.evaluate("NumberQ(" + value + ")")));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * return list variable on expression
     */
    public ArrayList<String> getListVariables(String expr) {
        ArrayList<String> variables = new ArrayList<>();
        try {
            String res = EVAL_ENGINE.evaluate("Variables(" + expr + ")").toString();
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

    /**
     * factor prime number
     */
    public String factorPrime(String input) {
        String s = EVAL_ENGINE.evaluate("FactorInteger(" + input + ")").toString();
        if (s.toLowerCase().contains("factorinteger")) {
            return input;
        }

        s = s.replaceAll("\\s+", "");
        int j = 1;
        StringBuilder result = new StringBuilder();
        for (int i = 1; i < s.length() - 1; i++) {
            if (s.charAt(i) == '}') {
                String tmp = s.substring(j + 1, i);
                j = i + 2;
                i += 2;
                String arr[] = tmp.split(",");
                result.append(arr[0]);
                result.append("^{").append(arr[1]).append("}");
                result.append("\\times "); //character mutiply
            }
        }
        result.delete(result.length() - "\\times ".length(), result.length()); //remove dot
        result.insert(0, "$$=").append("$$");
        return result.toString();
    }

    /**
     * Solve system equations and return string result
     */
    public String solveSystemEquation(String expr, EvaluateConfig config, Context context) {
        if (config.getEvaluateMode() == EvaluateConfig.DECIMAL) {
            expr = "N(" + expr + ")";
        }
        IExpr result = evaluate(expr);
        if (result.toString().toLowerCase().contains("solve")) {
            return context.getString(R.string.not_find_root);
        } else if (result.toString().equalsIgnoreCase("{}")) {
            return context.getString(R.string.no_root);
        }
        return LaTexFactory.toLaTeX(result);
       /* ArrayList<String> listRoots = new ArrayList<>();
        String[] s1 = result.split(Pattern.quote("},{"));
        for (int i = 0; i < s1.length; i++) {
            s1[i] = s1[i].replace("{", "").replace("}", "").replace("->", "==");
            String[] s2 = s1[i].split(Pattern.quote(","));
            for (int j = 0; j < s2.length; j++) {
                listRoots.add(s2[j]);
            }
        }*/
/*
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < listRoots.size(); i++) {
            builder.append(evaluateWithResultAsTex(listRoots.get(i), config)).append(" ");
        }*/
    /*    Log.d(TAG, "solveSystemEquation() returned: " + builder);
        return builder.toString();*/
    }

    /**
     * Factors the polynomial expression in <b>input</b>
     *
     * @param input - input expression
     */
    public String factorPolynomial(String input, final EvaluateConfig config) {
        String factorStr = "Factor(" + input + ")";
        IExpr result = EVAL_ENGINE.evaluate(factorStr);
        if (result.toString().toLowerCase().contains("factor")) {
            factorStr = "Factor(" + input + ", GaussianIntegers->True)";
            result = EVAL_ENGINE.evaluate(factorStr);
            if (result.toString().toLowerCase().contains("factor")) {
                return input;
            } else {
                return LaTexFactory.toLaTeX(result);
            }
        } else {
            return LaTexFactory.toLaTeX(result);
        }
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
