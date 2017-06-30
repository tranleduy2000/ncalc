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

package com.duy.calc.casio.token;

/**
 * A piece that receives a collection of pieces as an input, and then outputs a resulting number.
 *
 * @author Ejaaz Merali, Alston Lin
 * @version 3.0
 */

public abstract class FunctionToken extends Token {

    public static final int DEGREE = 1;
    public static final int RADIAN = 2;
    public static final int GRADIAN = 3; //angleMode options

    public static final int SIN = 1;
    public static final int COS = 2;
    public static final int TAN = 3;
    public static final int SINH = 4;
    public static final int COSH = 5;
    public static final int TANH = 6;
    public static final int ARCSIN = 7;
    public static final int ARCCOS = 8;
    public static final int ARCTAN = 9;
    public static final int ARCSINH = 10;
    public static final int ARCCOSH = 11;
    public static final int ARCTANH = 12;
    public static final int LOG10 = 17;
    public static final int LN = 19;
    public static final int SQRT = 21;
    public static final int CSC = 24;
    public static final int SEC = 25;
    public static final int COT = 26;
    public static final int ERF = 27;
    public static final int APPELLF1 = 28;
    public static final int ERFI = 29;
    public static final int GAMMA = 30;
    public static final int LCM = 31;
    public static final int GCD = 32;
    public static final int ABS = 33;
    public static final int DERIVATIVE = 34;
    public static final int LOG_N = 35;
    public static final int RANDOM_INT = 36;
    public static final int RANDOM_REAL = 37;
    public static final int INT = 38;
    public static final int INTG = 39;
    public static final int POL = 40;
    public static final int REC = 41;
    public static final int NOT = 42;
    public static final int NEG = 43;

    public static int angleMode = DEGREE;

    /**
     * Should not be used outside of a factory; to create a type of function,
     * see class FunctionFactory.
     *
     * @param symbol The symbol of the Token to be shown on the calculator screen
     * @param type   The type of Function this is, as defined by the class constants
     */
    public FunctionToken(String symbol, int type) {
        super(symbol);
        this.type = type;
        this.precedence = FUNCTION_PRECEDENCE;
        this.associative = PREFIX;
    }

    /**
     * Performs the function with the given input.
     *
     * @param input The input that is given for the function
     * @return The output of the performed function
     */
    public abstract Number perform(double input);

    /**
     * @return The type of Function this is, as defined by the class constants
     */
    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return getSymbol();
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 17 + type * super.hashCode() * 13;
    }
}
