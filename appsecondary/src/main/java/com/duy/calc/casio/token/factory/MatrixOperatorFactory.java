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

import com.duy.calc.casio.token.MatrixOperator;
import com.duy.calc.casio.evaluator.MatrixEvaluator;
import com.duy.calc.casio.token.NumberToken;

/**
 * Contains static factory methods for Operators in Matrix Mode.
 *
 * @author Ejaaz Merali, Keith Wong
 * @version 3.0
 */

public class MatrixOperatorFactory {

    public static MatrixOperator makeMatrixAdd() {
        return new MatrixOperator("+", MatrixOperator.ADD, 2, true, 1, true) {
            @Override
            public Object operate(Object left, Object right) {
                if (left instanceof double[][] && right instanceof double[][]) {
                    double[][] leftD = (double[][]) left;
                    double[][] rightD = (double[][]) right;
                    if (leftD.length == rightD.length && leftD[0].length == rightD[0].length) {
                        return MatrixEvaluator.add(leftD, rightD);
                    } else {
                        throw new IllegalArgumentException("Matrices are not the same size");
                    }
                } else if (left instanceof NumberToken && right instanceof NumberToken) {
                    return new NumberToken(OperatorFactory.makeAdd().operate(((NumberToken) left).getDoubleValue(), ((NumberToken) right).getDoubleValue()));
                } else {
                    throw new IllegalArgumentException("Both arguments must be Matrices or Numbers");
                }
            }
        };
    }

    public static MatrixOperator makeMatrixSubtract() {
        return new MatrixOperator("-", MatrixOperator.SUBTRACT, 2, true, 1, true) {
            @Override
            public Object operate(Object left, Object right) {
                if (left instanceof double[][] && right instanceof double[][]) {
                    double[][] leftD = (double[][]) left;
                    double[][] rightD = (double[][]) right;
                    if (leftD.length == rightD.length && leftD[0].length == rightD[0].length) {
                        return MatrixEvaluator.subtract(leftD, rightD);
                    } else {
                        throw new IllegalArgumentException("Matrices are not the same size");
                    }
                } else if (left instanceof NumberToken && right instanceof NumberToken) {
                    return new NumberToken(OperatorFactory.makeSubtract().operate(((NumberToken) left).getDoubleValue(), ((NumberToken) right).getDoubleValue()));
                } else {
                    throw new IllegalArgumentException("Both arguments must be Matrices or Numbers");
                }
            }
        };
    }

    public static MatrixOperator makeMatrixMultiply() {
        return new MatrixOperator("·", MatrixOperator.MULTIPLY, 2, true, 1, true) {
            @Override
            public Object operate(Object left, Object right) {
                if (left instanceof double[][] && right instanceof double[][]) {//matrix-matrix multiplication
                    try {
                        return MatrixEvaluator.multiply((double[][]) left, (double[][]) right);
                    } catch (Exception e) {
                        throw e;
                    }
                } else if (left instanceof NumberToken && right instanceof double[][]) {//scalar multiplication
                    return MatrixEvaluator.scalarMultiply((double[][]) right, ((NumberToken) left).getDoubleValue());
                } else if (right instanceof NumberToken && left instanceof double[][]) {//scalar multiplication
                    return MatrixEvaluator.scalarMultiply((double[][]) left, ((NumberToken) right).getDoubleValue());
                } else if (left instanceof NumberToken && right instanceof NumberToken) {
                    return new NumberToken(OperatorFactory.makeMultiply().operate(((NumberToken) left).getDoubleValue(), ((NumberToken) right).getDoubleValue()));
                } else {
                    throw new IllegalArgumentException("Invalid Input");
                }
            }
        };
    }

    public static MatrixOperator makeMatrixDivide() {
        return new MatrixOperator("/", MatrixOperator.DIVIDE, 2, true, 1, true) {
            @Override
            public Object operate(Object left, Object right) {
                if (left instanceof double[][] && right instanceof double[][]) {//matrix-matrix multiplication
                    try {
                        return MatrixEvaluator.multiply((double[][]) left, MatrixEvaluator.findInverse((double[][]) right));
                    } catch (Exception e) {
                        throw e;
                    }
                } else if (left instanceof NumberToken && right instanceof double[][]) {//scalar multiplication + inversion
                    return MatrixEvaluator.scalarMultiply(MatrixEvaluator.findInverse((double[][]) right), ((NumberToken) left).getDoubleValue());
                } else if (right instanceof NumberToken && left instanceof double[][]) {//scalar multiplication
                    return MatrixEvaluator.scalarMultiply((double[][]) left, 1 / (((NumberToken) right).getDoubleValue()));
                } else if (left instanceof NumberToken && right instanceof NumberToken) {
                    return new NumberToken(OperatorFactory.makeDivide().operate(((NumberToken) left).getDoubleValue(), ((NumberToken) right).getDoubleValue()));
                } else {
                    throw new IllegalArgumentException("Invalid Input");
                }
            }
        };
    }

    public static MatrixOperator makeMatrixExponent() {
        return new MatrixOperator("^", MatrixOperator.EXPONENT, 2, true, 1, true) {
            @Override
            public Object operate(Object left, Object right) {
                if (left instanceof double[][] && right instanceof NumberToken) {
                    try {
                        return MatrixEvaluator.exponentiate((double[][]) left, ((NumberToken) right).getDoubleValue());
                    } catch (Exception e) {
                        throw e;
                    }
                } else {
                    throw new IllegalArgumentException("Invalid Input");
                }
            }
        };
    }

}
