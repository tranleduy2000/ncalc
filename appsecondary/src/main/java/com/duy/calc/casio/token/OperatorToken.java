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

import com.duy.calc.casio.OrderComparable;
import com.duy.calc.casio.exception.NumberTooLargeException;

import java.io.Serializable;

/**
 * Receives input from the digits surrounding the operation piece and results in a new number.
 *
 * @author Alston Lin, Ejaaz Merali
 * @version 3.0
 */
public abstract class OperatorToken extends Token implements Serializable, OrderComparable {

    public static final int ADD = 1;
    public static final int SUB = 2;
    public static final int MUL = 3;
    public static final int DIV = 4;

    public static final int POWER = 5;
    public static final int PERMUTATION = 6;
    public static final int COMBINATION = 7;
    public static final int FACTORIAL = 8;
    public static final int SURD = 9;
    public static final int FRACTION = 10;

    public static final int PERCENT = 11;
    public static final int NEG = 12;
    public static final int EQUAL = 13;
    public static final int QUOTIENT = 14;
    public static final int AND = 15;
    public static final int OR = 16;
    public static final int XOR = 17;

    private int precedence;
    private boolean leftAssociative;
    private boolean commutative;
    private boolean anticommutative;
    private int associative;

    /**
     * Should not be used outside of a factory; to create a type of operation,
     * see class OperatorFactory.
     *
     * @param symbol          The symbol of the Token to be shown on the calculator screen
     * @param type            The type of Operator (defined by the class constants)
     * @param precedence      Defines the order the operator is used (bigger = higher priority)
     * @param leftAssociative If the operator is left or right associative
     * @param c               If the operator is commutative, anticommutative, or noncommutative
     * @param a               If the operator is associative
     */
    public OperatorToken(String symbol, int type, int precedence, boolean leftAssociative, int c, boolean a) {
        super(symbol);
        this.leftAssociative = leftAssociative;
        this.type = type;
        this.precedence = precedence;
        this.setCommutativity(c);
    }

    public OperatorToken(String symbol, int type, int precedence, int associative) {
        super(symbol);
        this.associative = associative;
        this.type = type;
        this.precedence = precedence;
        this.associative = associative;
    }

    public int getAssociative() {
        return associative;
    }

    /**
     * Performs the operation with the given surrounding values.
     *
     * @param left  The value left of the operation
     * @param right The value right of the operation
     * @return The result of the operation
     */
    public abstract double operate(double left, double right) throws NumberTooLargeException;

    /**
     * @return The type of operation this is (see class constants for possible values)
     */
    public int getType() {
        return type;
    }

    /**
     * @return true if the operator is left associative, false if it's right
     */
    public boolean isLeftAssociative() {
        return leftAssociative;
    }

    /**
     * @return true if the operator is commutative, false if it is not
     */
    public boolean isCommutative() {
        return commutative;
    }

    /**
     * @return true if the operator is anticommutative, false if it is not
     */
    public boolean isAntiCommutative() {
        return anticommutative;
    }

    /**
     * @param c if it is 1 the operator is commutative, 0:  noncommutative, -1: anticommutative
     */
    private void setCommutativity(int c) {
        if (c > 0) {
            commutative = true;
            anticommutative = false;
        } else if (c == 0) {
            commutative = false;
            anticommutative = false;
        } else {
            commutative = false;
            anticommutative = true;
        }
    }

    /**
     * @return The order of which the Operator is operated (higher = more priority)
     */
    public int getPrecedence() {
        return precedence;
    }

    @Override
    public String toString() {
        return getSymbol();
    }
}