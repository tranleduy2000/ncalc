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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.duy.calculator.R;
import com.duy.calculator.evaluator.base.LogicEvaluator;
import com.duy.calculator.evaluator.exceptions.ExpressionChecker;
import com.duy.calculator.symja.models.StepItem;
import com.duy.ncalc.settings.CalculatorSetting;
import com.duy.ncalc.utils.DLog;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.basic.ToggleFeature;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.interfaces.AbstractEvalStepListener;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


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
    private static final MathEvaluator INSTANCE = new MathEvaluator();

    private static final String TAG = "BigEvaluator";
    /**
     * evaluate engine
     */
    private final ExprEvaluator mExprEvaluator;

    private final OutputFormFactory mOutputFactory;

    private MathEvaluator() {
        ToggleFeature.QUANTITY = true;
        mExprEvaluator = new ExprEvaluator();
        //mTexEngine = new TeXUtilities(mExprEvaluator.getEvalEngine(), true);
        for (String function : CustomFunctions.getAllCustomFunctions()) {
            mExprEvaluator.eval(function);
        }
        DecimalFormatSymbols usSymbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("0.0####", usSymbols);
        mOutputFactory = OutputFormFactory.get(true, false, 5, 7);
    }

    @NonNull
    public static MathEvaluator getInstance() {
        return INSTANCE;
    }

    public static void initFromSetting(CalculatorSetting setting) {
        Config.PARSER_USE_LOWERCASE_SYMBOLS = setting.isParserUseLowercaseSymbol();
        Config.DOMINANT_IMPLICIT_TIMES = setting.isDominantImplicitTimes();
        Config.EXPLICIT_TIMES_OPERATOR = setting.isExplicitTimesOperator();
    }

    @Nullable
    public static Exception getError(String expr) {
        try {
            EvalEngine.get().parse(expr);
        } catch (Exception e) {
            return e;
        }
        return null;
    }

    /**
     * @param exprInput The expression to calculate
     * @return The  value of the expression
     * @throws IllegalArgumentException If the user has input a invalid expression
     */
    public IExpr evalStrSimple(String exprInput, EvaluateConfig config) {
        IExpr result;
        if (config.getEvaluateMode() == EvaluateConfig.DECIMAL) {
            result = evalStr("N(" + exprInput + ")");
        } else {
            result = evalStr(exprInput);
        }
        return result;
    }

    public IExpr evaluateSimple(IAST function, EvaluateConfig config) {
        IExpr result;
        if (config.getEvaluateMode() == EvaluateConfig.DECIMAL) {
            result = evaluate(F.N(function));
        } else {
            result = evaluate(function);
        }
        return result;
    }

    public IExpr evalStr(String exprInput) {
        return mExprEvaluator.eval(exprInput);
    }

    public IExpr evaluate(IExpr expr) {
        return mExprEvaluator.eval(expr);
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
     * @ {@link LogicEvaluator.EvaluateCallback}
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
            IExpr iExpr = evalStrSimple(expression, config);
            callback.onEvaluated(expression, toFormattedString(iExpr), RESULT_OK);
        } catch (Exception e) {
            callback.onCalculateError(e);
        }
    }

    private String toFormattedString(IExpr expr) throws IOException {
        StringBuilder strBuffer = new StringBuilder();
        mOutputFactory.reset();
        mOutputFactory.convert(strBuffer, expr);
        return strBuffer.toString();
    }

    private String addUserDefinedVariable(String expression) {
        if (!expression.contains("ans")) return expression;
        try {
            if (mExprEvaluator.getVariable("ans").toString().equals("null")) {
                expression = expression.replace("ans", "(0)");
            } else {
                expression = expression.replace("ans", "(" + mExprEvaluator.getVariable("ans") + ")");
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
        IExpr iExpr = evalStrSimple(expression,
                EvaluateConfig.newInstance().setEvalMode(EvaluateConfig.FRACTION));
        try {
            return toFormattedString(iExpr);
        } catch (IOException ioe) {
        }
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

        IExpr result = evalStrSimple(exprStr, config);
        return LaTexFactory.toLaTeX(result);
    }

    public String evaluateWithResultAsTex(EvaluateConfig config, IExpr head, String... args) {
        //if input is empty, do not working
        if (args == null || args.length == 0 || args[0].isEmpty()) {
            return "";
        }
        try {
            IExpr[] exprArgs = new IExpr[args.length];

            for (int i = 0; i < args.length; i++) {
                exprArgs[i] = evalStr(args[i]);
            }
            IAST function = F.ast(exprArgs, head);

            IExpr result = evaluateSimple(function, config);
            return LaTexFactory.toLaTeX(result);
        } catch (RuntimeException rex) {
            return "";
        }
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
        IExpr roots = mExprEvaluator.evaluate(solveStr);

        if (!roots.isFree(F.Solve)) {
            return context.getString(R.string.not_find_root);
        } else if (!roots.isListOfLists()) {
            return context.getString(R.string.no_root);
        }
        return LaTexFactory.toLaTeX(roots);
    }


    public void define(String var, double value) {
        try {
            mExprEvaluator.defineVariable(var, value);
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
            IExpr iExpr = mExprEvaluator.evaluate(value);
            mExprEvaluator.defineVariable(var, iExpr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * true if is a number
     */
    public boolean isNumber(String value) {
        try {
            return Boolean.valueOf(String.valueOf(mExprEvaluator.evaluate("NumberQ(" + value + ")")));
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
            String res = mExprEvaluator.evaluate("Variables(" + expr + ")").toString();
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
            mExprEvaluator.getEvalEngine().parse(expr);
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
        String s = mExprEvaluator.evaluate("FactorInteger(" + input + ")").toString();
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
        try {
            IExpr temp = mExprEvaluator.parse(expr);
            if (config.getEvaluateMode() == EvaluateConfig.DECIMAL) {
                temp = F.N(temp);
            }
            IExpr result = evaluate(temp);
            if (result.toString().toLowerCase().contains("solve")) {
                return context.getString(R.string.not_find_root);
            } else if (result.toString().equalsIgnoreCase("{}")) {
                return context.getString(R.string.no_root);
            }
            return LaTexFactory.toLaTeX(result);
        } catch (RuntimeException rex) {

        }
        return LaTexFactory.toLaTeX(F.stringx("Error in input: " + expr.toString()));
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
        IExpr result = mExprEvaluator.evaluate(factorStr);
        if (result.toString().toLowerCase().contains("factor")) {
            factorStr = "Factor(" + input + ", GaussianIntegers->True)";
            result = mExprEvaluator.evaluate(factorStr);
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
                LaTexFactory latex = new LaTexFactory(EvalEngine.get(), true);
                latex.toTeX(input, stringWriterInput);
                //mTexEngine.toTeX(input, stringWriterInput);
                StringWriter stringWriterOutput = new StringWriter();

                latex.toTeX(result, stringWriterOutput);
                //mTexEngine.toTeX(result, stringWriterOutput);
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
