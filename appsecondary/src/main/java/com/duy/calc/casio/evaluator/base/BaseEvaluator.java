/*
 * Copyright (c) 2017 by Tran Le Duy
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

package com.duy.calc.casio.evaluator.base;

import org.javia.arity.Symbols;
import org.javia.arity.SyntaxException;

/**
 * thực hiện việc tính toán các biểu thức
 * <p/>
 * Create by duy on 30/7/2016
 */
public class BaseEvaluator {
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

    private String TAG = BaseEvaluator.class.getName();

    public BaseEvaluator() {
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
     * and get the result returned.
     */
    public String evaluate(String input) throws SyntaxException {
        if (input.trim().isEmpty()) {
            return "";
        }
        String decimalInput = convertToDecimal(input);
        String result = "";
        try {
            result = clean(mBaseModule.changeBase(result, Base.DECIMAL, mBaseModule.getBase()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new SyntaxException();
        }
        return result;
    }


    public double eval(String input) throws SyntaxException {
        return symbols.eval(input);
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

