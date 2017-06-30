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

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a digit, operation, brackets, or a function on the calculator screen; cannot be used by itself
 * (must use a subclass of this).
 *
 * @author Alston Lin
 * @version 3.0
 */
public abstract class Token implements Serializable {

    public static final int PREFIX = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int SUFFIX = 4;

    public static final int NOPE = 0;

    public static final int FUNCTION_PRECEDENCE = 0;
    public static final int LEFT_PARENT_PRECEDENCE = 1;
    public static final int COMMA_PRECEDENCE = 2;
    public static final int RIGHT_PARENT_PRECEDENCE = 3;

    public static final int XOR_PRECEDENCE = 4;
    public static final int AND_PRECEDENCE = 5;
    public static final int OR_PRECEDENCE = 6;
    public static final int NOT_PRECEDENCE = 7;

    public static final int ADD_SUB_PRECEDENCE = 8;
    public static final int MUL_DIV_PRECEDENCE = 9;
    public static final int PERMUTATION_PRECEDENCE = 10;
    public static final int COMBINATION_PRECEDENCE = 11;
    public static final int NEG_PRECEDENCE = 12;
    public static final int POWER_PRECEDENCE = 13;
    public static final int FACTORIAL_PRECEDENCE = 14;
    public static final int PERCENT_PRECEDENCE = 15;
    public static final int SQRT_PRECEDENCE = 16;


    public static final int NUMBER_PRECEDENCE = 20;
    public static final int CONST_PRECEDENCE = 20;

    protected int type;
    protected int precedence = NOPE;
    protected int associative = NOPE;
    @Nullable
    protected String symbol;
    private ArrayList<Token> dependencies = new ArrayList<>(); //Tokens that are dependent with this token

    /**
     * Creates a new Token to be shown on the calculator screen.
     *
     * @param symbol The symbol of the Token to be shown on the calculator screen
     */
    public Token(@Nullable String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return The symbol for this Token to be shown on the mDisplay
     */
    @Nullable
    public String getSymbol() {
        return symbol;
    }

    /**
     * Adds a dependent token to the list; if this token is removed so would the dependencies.
     *
     * @param t The token that is dependent on this one
     */
    public void addDependency(Token t) {
        dependencies.add(t);
    }

    /**
     * @return The Tokens that are dependent on this one
     */
    public ArrayList<Token> getDependencies() {
        return dependencies;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        if (symbol != null) {
            if (symbol.isEmpty()) {
                return getClass().getSimpleName() + "-" + type;
            } else {
                return symbol;
            }
        }
        return "null";
    }

    /**
     * @return The order of which the Operator is operated (higher = more priority)
     */
    public int getPrecedence() {
        return precedence;
    }

    public int getAssociative() {
        return associative;
    }
}

