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

package com.duy.calc.casio.token.factory;

import com.duy.calc.casio.Node;
import com.duy.calc.casio.token.NumberToken;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.token.VariableToken;

/**
 * Contains methods that builds commonly used expressions as subtrees.
 *
 * @author Alston Lin
 * @version 3.0
 */
public class ExpressionFactory {

    /**
     * Builds an expression of npi / m as a Node.
     *
     * @param n The multiplier for pi
     * @param m The denom
     * @return The built expression
     */
    public static Node<Token> makeNPiOverM(double n, double m) {
        Node<Token> piOverTwo = new Node<Token>(OperatorFactory.makeDivide());
        Node<Token> multiply = new Node<Token>(OperatorFactory.makeMultiply());

        multiply.addChild(new Node<Token>(new NumberToken(n)));
        multiply.addChild(new Node<Token>(VariableFactory.makePI()));

        piOverTwo.addChild(multiply);
        piOverTwo.addChild(new Node<Token>(new NumberToken(m)));

        return piOverTwo;
    }

    /**
     * Makes an expression of pi/n, where n is given.
     *
     * @param negative If pi should be negative
     * @param n        The denom
     * @return The built expression
     */
    public static Node<Token> makePiOverN(boolean negative, double n) {
        Node<Token> piOverTwo = new Node<Token>(OperatorFactory.makeDivide());
        VariableToken pi = VariableFactory.makePI();
        pi.setNegative(negative);
        piOverTwo.addChild(new Node<Token>(pi));
        piOverTwo.addChild(new Node<Token>(new NumberToken(n)));
        return piOverTwo;
    }

    public static Node<Token> makeOneHalf(boolean negative) {
        Node<Token> oneHalf = new Node<Token>(OperatorFactory.makeDivide());
        if (negative) {
            oneHalf.addChild(new Node<Token>(new NumberToken(-1)));
            oneHalf.addChild(new Node<Token>(new NumberToken(2)));
        } else {
            oneHalf.addChild(new Node<Token>(new NumberToken(1)));
            oneHalf.addChild(new Node<Token>(new NumberToken(2)));
        }
        return oneHalf;
    }

    public static Node<Token> makeSqrt2Over2(boolean negative) {
        Node<Token> sqrt2Over2 = new Node<Token>(OperatorFactory.makeDivide());
        Node<Token> sqrt = new Node<Token>(FunctionFactory.makeSqrt());
        sqrt.addChild(new Node<Token>(new NumberToken(2)));
        sqrt2Over2.addChild(sqrt);
        if (negative) {
            sqrt2Over2.addChild(new Node<Token>(new NumberToken(-2)));
        } else {
            sqrt2Over2.addChild(new Node<Token>(new NumberToken(2)));
        }
        return sqrt2Over2;
    }

    public static Node<Token> makeSqrt3Over2(boolean negative) {
        Node<Token> sqrt3Over2 = new Node<Token>(OperatorFactory.makeDivide());
        Node<Token> sqrt = new Node<Token>(FunctionFactory.makeSqrt());
        sqrt.addChild(new Node<Token>(new NumberToken(3)));
        sqrt3Over2.addChild(sqrt);
        if (negative) {
            sqrt3Over2.addChild(new Node<Token>(new NumberToken(-2)));
        } else {
            sqrt3Over2.addChild(new Node<Token>(new NumberToken(2)));
        }
        return sqrt3Over2;
    }

    public static Node<Token> makeSqrt3Over3(boolean negative) {
        Node<Token> sqrt3Over2 = new Node<Token>(OperatorFactory.makeDivide());
        Node<Token> sqrt = new Node<Token>(FunctionFactory.makeSqrt());
        sqrt.addChild(new Node<Token>(new NumberToken(3)));
        sqrt3Over2.addChild(sqrt);
        if (negative) {
            sqrt3Over2.addChild(new Node<Token>(new NumberToken(-3)));
        } else {
            sqrt3Over2.addChild(new Node<Token>(new NumberToken(3)));
        }
        return sqrt3Over2;
    }

    public static Node<Token> makeSqrt3(boolean negative) {
        Node<Token> sqrt3 = new Node<Token>(FunctionFactory.makeSqrt());
        sqrt3.addChild(new Node<Token>(new NumberToken(3)));
        if (negative) {
            Node<Token> multiply = new Node<Token>(OperatorFactory.makeMultiply());
            multiply.addChild(new Node<Token>(new NumberToken(-1)));
            multiply.addChild(sqrt3);
            return multiply;
        }
        return sqrt3;
    }
}
