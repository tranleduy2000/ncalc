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

package com.duy.casiofx.token;

import java.io.Serializable;
/**
 * Object representation of an Operator to be used by Vectors.
 *
 * @author Alston Lin
 * @version 3.0
 */
public abstract class VectorOperator extends Token implements Serializable, OrderComparable {
    public static final int ADD = 1, SUBTRACT = 2, DOT = 3, CROSS = 4, ANGLE = 5;
    private int type;
    private int precedence;
    private boolean leftAssociative;

    public VectorOperator(String symbol, int type, int precedence, boolean leftAssociative) {
        super(symbol);
        this.type = type;
        this.precedence = precedence;
        this.leftAssociative = leftAssociative;
    }

    public abstract Token operate(Token left, Token right);

    public int getPrecedence() {
        return precedence;
    }

    public boolean isLeftAssociative() {
        return leftAssociative;
    }

    public int getType() {
        return type;
    }

}
