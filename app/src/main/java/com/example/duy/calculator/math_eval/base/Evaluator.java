package com.example.duy.calculator.math_eval.base;

import android.util.Log;

import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.Constants;

import org.javia.arity.Symbols;
import org.javia.arity.SyntaxException;

/**
 * thực hiện việc tính toán các biểu thức
 * <p/>
 * Create by duy on 30/7/2016
 */
public class Evaluator {
    public static final long serialVersionUID = 4L;
    /**
     * Solves math problems
     * <p/>
     * Basic math + functions (trig, pi)
     * Matrices
     * Hex and Bin conversion
     */
    // Used for solving basic math
    private static final Symbols symbols = new Symbols();
    private BaseModule mBaseModule;

    private String TAG = Evaluator.class.getName();

    public Evaluator() {
        mBaseModule = new BaseModule(this);
    }

    public static boolean equal(String a, String b) {
        return clean(a).equals(clean(b));
    }

    public static String clean(String equation) {
        return equation
                .replace('-', Constants.MINUS_UNICODE)
                .replace('/', Constants.DIV_UNICODE)
                .replace('*', Constants.MUL_UNICODE)
                .replace(Constants.INFINITY, Character.toString(Constants.INFINITY_UNICODE));
    }

    public static boolean isOperator(char c) {
        return ("" + Constants.PLUS_UNICODE +
                Constants.MINUS_UNICODE +
                Constants.DIV_UNICODE +
                Constants.MUL_UNICODE +
                Constants.POWER_UNICODE
        ).indexOf(c) != -1;
    }

    public static boolean isOperator(String c) {
        return isOperator(c.charAt(0));
    }

    public static boolean isNegative(String number) {
        return number.startsWith(String.valueOf(Constants.MINUS_UNICODE)) || number.startsWith("-");
    }

    /**
     * check digit
     *
     * @param number - char
     * @return true if is digit
     */
    public static boolean isDigit(char number) {
        return String.valueOf(number).matches(Constants.REGEX_NUMBER);
    }

    public static boolean isComplex(String value) {
        return value.contains("i") || value.contains("I");
    }


    /**
     * Input an equation as a string
     * ex: sin(150)
     * and get the mResult returned.
     */
    public String evaluate(String input, BigEvaluator evaluator) throws SyntaxException {
        if (input.trim().isEmpty()) {
            return "";
        }
        String decimalInput = convertToDecimal(input);
        String result = "";
        try {
            result = evaluator.evaluateWithResultNormal(decimalInput);
            if (result.toLowerCase().equals(Constants.TRUE) || result.toLowerCase().equals(Constants.FALSE))
                return result;
            result = clean(mBaseModule.changeBase(result, Base.DECIMAL, mBaseModule.getBase()));
            Log.d(TAG, "solver: " + result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SyntaxException();
        }
        return result;
    }


    public double eval(String input) throws SyntaxException {
        return symbols.eval(input);
    }

    public void define(String var, double val) {
        try {
            symbols.define(var, val);
        } catch (Exception e) {
            //do something
        }
    }

    public String convertToDecimal(String input) throws SyntaxException {
        return mBaseModule.changeBase(input, mBaseModule.getBase(), Base.DECIMAL);
    }

    public Base getBase() {
        return mBaseModule.getBase();
    }

    public void setBase(Base base) {
        mBaseModule.setBase(base);
    }

    public BaseModule getBaseModule() {
        return mBaseModule;
    }

}

