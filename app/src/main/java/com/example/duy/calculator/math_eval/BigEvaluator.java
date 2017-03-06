package com.example.duy.calculator.math_eval;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.duy.calculator.DLog;
import com.example.duy.calculator.R;
import com.example.duy.calculator.utils.ConfigApp;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;


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

public final class BigEvaluator extends LogicEvaluator {
    private static final String ANS_VAR = "ans";
    private static final String TAG = "BigEvaluator";
    public static BigEvaluator mEvaluator;
    private int numDecimal = 10;
    /**
     * symja parser
     */
    private ExprEvaluator mEvalUtils;
    private EvalEngine mEngine;
    /**
     * set value <code>true</code> if use mode degress
     * set value <code>false</code> if use mode radian
     */
    private boolean isUseDegree = false;
    /**
     * set value <code>true</code> if use mode mResult as fraction vaue
     * set value <code>false</code> if use mode mResult as decimal value
     */
    private boolean isFraction = true;
    //debug
    private boolean debug = ConfigApp.DEBUG;
    //android context
    private Context mContext;
    private TeXUtilities mTexEngine;
    /**
     * SharedPreferences for save value variable
     */
    private SharedPreferences mPref;

    /**
     * constructor
     *
     * @param context
     */
    public BigEvaluator(Context context) {
        super(new Tokenizer(context));

        this.mContext = context;
        this.mEvalUtils = new ExprEvaluator();
        this.mEngine = mEvalUtils.getEvalEngine();
        this.mTexEngine = new TeXUtilities(mEngine, true);
        this.mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        loadSetting();
    }

    /**
     * return BigEvaluator
     *
     * @param context - android context
     * @return - mEvaluator
     */
    public static BigEvaluator getInstance(Context context) {
        if (mEvaluator == null) {
            mEvaluator = new BigEvaluator(context);
        }
        return mEvaluator;
    }

    /**
     * restore data for evaluator
     * such as number of precision, mode fraction/real, ...
     */
    public void loadSetting() {
        addFunction();
        String ans_val = mPref.getString(ANS_VAR, "0");
        define("ans", ans_val);
    }

    /**
     * save user setting
     * save last answer value
     */
    public void saveSetting() {
        SharedPreferences.Editor editor = mPref.edit();
        if (mEvalUtils.getVariable(ANS_VAR).toString() == null) {
            editor.putString(ANS_VAR, "0");
        } else {
            editor.putString(ANS_VAR, mEvalUtils.getVariable(ANS_VAR).toString());
        }
        editor.apply();
    }

    @Override
    public BigEvaluator getEvaluator() {
        return this;
    }

    /**
     * add define function
     */
    private void addFunction() {
        try {
            mEvalUtils.evaluate("C(n_, k_):=(factorial(Ceiling(n)) / " + "(factorial(Ceiling(k)) * factorial(Ceiling(n - k))))");
            mEvalUtils.evaluate("P(n_, k_):=(factorial(Ceiling(n)) / (factorial(Ceiling(n - k))))");
            mEvalUtils.evaluate("cbrt(x_):= x^(1/3)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ExprEvaluator getEvalUtils() {
        return mEvalUtils;
    }

    public EvalEngine getEngine() {
        return mEngine;
    }


    /**
     * evaluate expression, the mResult will be return callback via interface
     * #EvaluateCallback
     *
     * @param expression - input expression String Obe
     * @param callback   - interface for call back event
     * @ {@link com.example.duy.calculator.math_eval.LogicEvaluator.EvaluateCallback}
     */
    public void evaluateWithResultNormal(String expression, LogicEvaluator.EvaluateCallback callback) {
        //if input is empty, do not working
        if (expression.isEmpty())
            callback.onEvaluate(expression, "", LogicEvaluator.INPUT_EMPTY);

        expression = FormatExpression.cleanExpression(expression, mTokenizer);
        expression = addUserDefinedVariable(expression); //$ans = ...

        Log.d(TAG, "evaluateWithResultNormal: " + expression);
        try {
            IExpr res;
            //if mode is real
            if (!isFraction) {
                EvalEngine evalEngine = new EvalEngine();
                mEngine.setNumericMode(true, 5);
                res = mEngine.evaluate("N(" + expression + ")");
                Log.d(TAG, "evaluateWithResultNormal: numeric " + expression);
                if (res.isNumeric()) {
                    try {
                        //format comma, dot
                        String sFormat;
                        sFormat = DecimalFactory.round(res.toString(), numDecimal);
//                        sFormat = res.toString();
                        Log.i(TAG, "evaluateWithResultNormal: isNumeric " + res.toString()
                                + " = " + sFormat);
                        callback.onEvaluate(expression, sFormat, LogicEvaluator.RESULT_OK);
                    } catch (Exception e) {
                        //  e.printStackTrace();
                        // if not is numeric,
                        // it will be throw exception, although mResult as true
                        callback.onEvaluate(expression, res.toString(), LogicEvaluator.RESULT_OK);
                    }
                } else {
                    //if not is numeric, callback
                    //such as 2x + 1 + 2 = 2x + 3
                    callback.onEvaluate(expression, res.toString(), LogicEvaluator.RESULT_OK);
                }
            } else { //mode is fraction
                Log.d(TAG, "evaluateWithResultNormal: fraction " + expression);

                res = mEvalUtils.evaluate(expression);
                callback.onEvaluate(expression, res.toString(), LogicEvaluator.RESULT_OK);
            }
        } catch (SyntaxError e) {
            callback.onEvaluate(expression, getExceptionMessage(e, isFraction), LogicEvaluator.RESULT_ERROR_WITH_INDEX);
        } catch (Exception e) {
            callback.onEvaluate(expression, getExceptionMessage(e, isFraction), LogicEvaluator.RESULT_ERROR);
        }
    }

    private String addUserDefinedVariable(String expression) {
        if (!expression.contains("ans")) return expression;
        try {
            if (mEvalUtils.getVariable("ans").toString().equals("null")) {
                expression = expression.replace("ans", "(0)");
            } else {
                expression = expression.replace("ans", "(" + mEvalUtils.getVariable("ans") + ")");
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
     * evaluate expression, the mResult will be return String Object
     *
     * @param expression - input expression
     * @return - String
     */
    public String evaluateWithResultNormal(String expression) {
        final String[] res = {""};
        evaluateWithResultNormal(expression, new EvaluateCallback() {
            @Override
            public void onEvaluate(String expr, String result, int errorResourceId) {
                res[0] = result;
            }
        });
        return res[0];
    }


    /**
     * use fraction in mResult
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
        //mResult as fraction
        evaluateWithResultAsTex(expr, new EvaluateCallback() { //catch error
            @Override
            public void onEvaluate(final String expr0, String result0, final int errorResourceId0) {
                if (errorResourceId0 == LogicEvaluator.RESULT_OK) {
                    resFraction[0] = result0;
                    next[0] = true;
                } else {
                    //return error
                    next[0] = false;
                    callback.onEvaluate(expr0, result0, LogicEvaluator.RESULT_ERROR);
                }
            }
        });

        //mResult as real number

        if (next[0]) {
            String inp = Constants.SIMPLIFY
                    + Constants.LEFT_PAREN
                    + evaluateWithResultNormal(expr)
                    + Constants.RIGHT_PAREN;
            setFraction(false);
            evaluateWithResultAsTex(inp, new EvaluateCallback() {
                @Override
                public void onEvaluate(String expr, String result, int errorResourceId) {
                    if (errorResourceId == LogicEvaluator.RESULT_OK) {
                        resReal[0] = result;
                    }
                }
            });
        }

        setFraction(last);

        //mResult
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
        callback.onEvaluate(expr,
                stringBuilder.toString(),
                RESULT_OK);
    }

    /**
     * Solve equation and return string mResult, and real, and fraction
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

        //mResult as fraction
        evaluateWithResultNormal(expr, new EvaluateCallback() { //catch error
            @Override
            public void onEvaluate(final String expr0, String result0, final int errorResourceId0) {
                if (errorResourceId0 == LogicEvaluator.RESULT_OK) {
                    resFraction[0] = result0;
                } else {
                    //return error
                    callback.onEvaluate(expr0, result0, LogicEvaluator.RESULT_ERROR);
                }
            }
        });

        if (resFraction[0].toLowerCase().contains("solve")) {
            String c = getString(R.string.not_find_root);
            callback.onEvaluate(expr, c, RESULT_OK);
            return;
            //return if EvalEngine can not find solution of the equation
            //solve(x^1/3 = - 1)
        } else if (resFraction[0].contains("{}")) {
            String c = getString(R.string.no_root);
            callback.onEvaluate(expr, c, RESULT_OK);
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

        //puts terms in a sum over a common denominator and cancels factors in the mResult.
        for (int i = 0; i < result.size(); i++) {
            b += evaluateWithResultAsTex(result.get(i));
        }
        b += Constants.WEB_SEPARATOR;
        setFraction(false);
        for (int i = 0; i < result.size(); i++) {
            b += evaluateWithResultAsTex(result.get(i));
        }
        setFraction(last);

        //call back feedback, mResult ok
        callback.onEvaluate(expr, b, RESULT_OK);
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
            public void onEvaluate(String expr, String result, int errorResourceId) {
                res[0] = result;
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
        //string for mResult callback
        StringWriter writer = new StringWriter();
        try {
            if (!isFraction) expr = "N(" + expr + ")";

            //$ans = ...
            expr = addUserDefinedVariable(expr);

            IExpr r = mEvalUtils.evaluate(expr);
            // TODO: 29-Jan-17 round mResult if mode is numeric.
            //if mResult is numeric, round it, if not round value, mResult will be wrong
            //such as sqrt(2)*sqrt(2) = 2.0000000000004 -> wrong answer;
            if (r.isNumeric()) {
                mTexEngine.toTeX(DecimalFactory.round(r.toScript(), numDecimal), writer);
            } else {
                mTexEngine.toTeX(r, writer);
            }
        } catch (SyntaxError e) {
            callback.onEvaluate(expr, getExceptionMessage(e, isFraction), LogicEvaluator.RESULT_ERROR_WITH_INDEX);
        } catch (Exception e) {
            callback.onEvaluate(expr, getExceptionMessage(e, isFraction), LogicEvaluator.RESULT_ERROR);
        }
        DLog.d(writer.toString());
        callback.onEvaluate(expr, "$$" + writer.toString() + "$$", LogicEvaluator.RESULT_OK);
    }

    /**
     * get exception message
     *
     * @param e          - Exception
     * @param isFraction - mode is fraction or real,
     *                   if is mode fraction input look like 2/3 + 1/2 + error ...
     *                   else input look like N(...)
     * @return String Object
     */
    public String getExceptionMessage(Exception e, boolean isFraction) {
        if (debug)
            e.printStackTrace();
        if (e instanceof SyntaxError) {
            SyntaxError copyException = (SyntaxError) e;
            String res = copyException.getCurrentLine();
            int index = copyException.getStartOffset();
            try {
                //set selected handler error
                if (res.length() > 0) {
                    res = res.substring(0, index) + // text before error
                            LogicEvaluator.ERROR_INDEX_STRING + "" +
                            res.substring(index, res.length()); //text after error
                } else res = "";
                //if not use fraction, remove N()
                if (!isFraction) {
                    res = res.substring(2, res.length() - 1);
                }
                return res;
            } catch (Exception ex) {
                return "";
            }
        } else if (e instanceof MathException) {
            String s = "<h3>" + getString(R.string.math_error) + "</h3>" +
                    getString(R.string.reason) + "</br>"
                    + e.getMessage();
            DLog.e(s);
            return s;
        }
        return e.getMessage();
    }

    public ExprEvaluator getEvalUtilities() {
        return mEvalUtils;
    }

    public TeXUtilities getTexEngine() {
        return mTexEngine;
    }

    public boolean isUseDegree() {
        return isUseDegree;
    }

    public void setUseDegree(boolean useDegree) {
        isUseDegree = useDegree;
    }

    public int getNumDecimal() {
        return numDecimal;
    }

    public void setNumDecimal(int numDecimal) {
        this.numDecimal = numDecimal;
    }

    public void define(String var, double value) {
        try {
            mEvalUtils.defineVariable(var, value);
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
            IExpr iExpr = mEvalUtils.evaluate(value);
            mEvalUtils.defineVariable(var, iExpr);
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
            boolean res = Boolean.valueOf(String.valueOf(mEvalUtils.evaluate("NumberQ(" + value + ")")));
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
            mEngine.parse(expr);
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
            public void onEvaluate(String expr, String result, int errorResourceId) {
                if (errorResourceId == LogicEvaluator.RESULT_OK) res[0] = "";
                else res[0] = result;
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
            callback.onEvaluate(input, getString(R.string.cannot_factor), RESULT_FAILED);
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
        callback.onEvaluate(input, result, RESULT_OK);
        DLog.i("factor prime: " + result);
        setFraction(last);
    }

    /**
     * Solve system equations and return string mResult, and real, and fraction
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
        //mResult as fraction
        evaluateWithResultNormal(expr, new EvaluateCallback() {
            @Override
            public void onEvaluate(final String input, String result, final int resultState) {
                if (resultState == LogicEvaluator.RESULT_OK) {
                    temp[0] = result;
                    Log.d(TAG, "onEvaluate: " + result);
                } else {
                    //return error
                    callback.onEvaluate(input, result, LogicEvaluator.RESULT_ERROR);
                    hasNext[0] = false;
                }
            }
        });

        if (!hasNext[0]) return;
        if (temp[0].toLowerCase().contains("solve")) {
            String c = getString(R.string.not_find_root);
            callback.onEvaluate(expr, c, RESULT_OK);
            return;
            //return if EvalEngine can not find solution of the equation
            //solve(x^1/3 = - 1)
        } else if (temp[0].contains("{}")) {
            String c = getString(R.string.no_root);
            callback.onEvaluate(expr, c, RESULT_OK);
            return; //return if the equation no root
        }

        //if input can eval and mResult
        //process data fraction
        String sCopy = temp[0];
        DLog.i("solve mResult = " + sCopy);
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
        //call back feedback, mResult ok
        for (int i = 0; i < result.size(); i++) {
            b += evaluateWithResultAsTex(result.get(i));
        }
        b += Constants.WEB_SEPARATOR;
        setFraction(false);
        for (int i = 0; i < result.size(); i++) {
            b += evaluateWithResultAsTex(result.get(i));
        }
        setFraction(last);
        callback.onEvaluate(expr, b, RESULT_OK);
    }

    /**
     * factors the polynomial expression in <b>input</b>
     *
     * @param input
     * @param callback
     */
    public void factorPolynomial(String input, final EvaluateCallback callback) {
        boolean last = isFraction;
        setFraction(true);
        evaluateWithResultAsTex(input, new EvaluateCallback() {
            @Override
            public void onEvaluate(String expr, String result, int errorResourceId) {
                if (result.toLowerCase().contains("factor")) {
                    callback.onEvaluate(expr,
                            getString(R.string.cannot_factor_polynomial),
                            errorResourceId);
                } else {
                    callback.onEvaluate(expr, result, errorResourceId);
                }

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
            public void onEvaluate(String expr, String result, int errorResourceId) {
                res[0] = result;
            }
        });
        res[0] += Constants.WEB_SEPARATOR;
        setFraction(false);
        evaluateWithResultAsTex("ExpandAll(" + input + ")", new EvaluateCallback() {
            @Override
            public void onEvaluate(String expr, String result, int errorResourceId) {
                res[0] += result;
                callback.onEvaluate(expr, res[0], errorResourceId);
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
            public void onEvaluate(String expr, String result, int errorResourceId) {
                res[0] = result;
            }
        });
        res[0] += Constants.WEB_SEPARATOR;
        setFraction(false);
        evaluateWithResultAsTex(input, new EvaluateCallback() {
            @Override
            public void onEvaluate(String expr, String result, int errorResourceId) {
                res[0] += result;
                callback.onEvaluate(expr, res[0], errorResourceId);
            }
        });
        setFraction(last);
    }

    public void integrateFunction(String input, EvaluateCallback callback) {
        simplifyExpression(input, callback);
    }
}
