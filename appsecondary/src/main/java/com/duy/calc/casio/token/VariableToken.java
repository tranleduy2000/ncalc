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

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents either a user-defined value or a variable for a function, represented by
 * english letters (ex. x, y, z). It also includes constants as well (such as Pi and e)
 *
 * @author Alston Lin
 * @version 3.0
 */
public abstract class VariableToken extends Token implements Serializable {

    public static final int A = 10000;
    public static final int B = 20000;
    public static final int C = 30000;
    public static final int D = 170000;
    public static final int E = 180000;
    public static final int F = 190000;
    public static final int M = 200000;

    public static final int X = 40000;
    public static final int Y = 160000;

    public static final int PI = 50000;
    public static final int e = 60000;
    public static final int ANS = 70000;
    public static final int CONSTANT = 80000;
    public static final int MATRIX_A = 90000;
    public static final int MATRIX_B = 100000;
    public static final int MATRIX_C = 110000;
    public static final int U = 120000;
    public static final int V = 130000;
    public static final int S = 140000;
    public static final int T = 150000;

    public static final int PRE_ANS = 16;

    public static final double PI_VALUE = Math.PI;
    public static final double E_VALUE = Math.E;

    public boolean negative = false;

    /**
     * Constructor for a Token that represents a user-defined value (algebraic variable)
     *
     * @param type   The type of variable as defined by the class constants
     * @param symbol The symbol as it will be shown as on the mDisplay
     */
    public VariableToken(int type, String symbol) {
        super(symbol);
        this.type = type;
        this.precedence = NUMBER_PRECEDENCE;
    }

    public int getType() {
        return type;
    }

    public boolean isNegative() {
        return negative;
    }

    public void setNegative(boolean negative) {
        this.negative = negative;
    }

    public String getSymbol() {
        String symbol = "";
        if (negative) {
            symbol = "-";
        }
        symbol += super.getSymbol();
        return symbol;
    }

    /**
     * @return The value of the variable/constant
     */
    public abstract ArrayList<Token> getValue();

}
