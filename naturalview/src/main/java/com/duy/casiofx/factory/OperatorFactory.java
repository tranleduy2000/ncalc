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

package com.duy.casiofx.factory;

import com.duy.casiofx.exception.NumberTooLargeException;
import com.duy.casiofx.token.OperatorToken;
import com.duy.casiofx.util.Utility;

/**
 * Contains static methods that will create Operator pieces.
 *
 * @author Alston Lin, Ejaaz Merali
 * @version 3.0
 */
public class OperatorFactory {

    public static OperatorToken makeAdd() {
        return new OperatorToken("+", OperatorToken.ADD, OperatorToken.ADD_SUBTRACT, true, 1, true) {
            @Override
            public double operate(double left, double right) {
                return left + right;
            }
        };
    }

    public static OperatorToken makeSubtract() {
        return new OperatorToken("−", OperatorToken.SUBTRACT, OperatorToken.ADD_SUBTRACT, true, -1, false) {
            @Override
            public double operate(double left, double right) {
                return left - right;
            }
        };
    }

    public static OperatorToken makeMultiply() {
        return new OperatorToken("×", OperatorToken.MULTIPLY, OperatorToken.MULTIPLY_DIVIDE, true, 1, true) {
            @Override
            public double operate(double left, double right) {
                return left * right;
            }
        };
    }

    public static OperatorToken makeDivide() {
        return new OperatorToken("÷", OperatorToken.DIVIDE, OperatorToken.MULTIPLY_DIVIDE, true, 0, false) {
            @Override
            public double operate(double left, double right) {
                if (right == 0) {
                    throw new ArithmeticException("Division by zero");
                } else {
                    return left / right;
                }
            }
        };
    }

    public static OperatorToken makeFraction() {
        return new OperatorToken("", OperatorToken.FRACTION, OperatorToken.MULTIPLY_DIVIDE, true, 0, false) {
            @Override
            public double operate(double left, double right) {
                if (right == 0) {
                    throw new ArithmeticException("Division by zero");
                } else {
                    return left / right;
                }
            }

            @Override
            public String toString() {
                return "/";
            }
        };
    }

    public static OperatorToken makeExponent() {
        return new OperatorToken("", OperatorToken.EXPONENT, OperatorToken.EXPONENT_PRECEDENCE, false, 0, false) {
            @Override
            public double operate(double left, double right) {
                double result = Math.pow(left, right);
                if (result == Double.NaN)
                    throw new IllegalArgumentException("The answer involves Imaginary numbers (currently not supported)");
                return result;
            }
        };
    }

    public static OperatorToken makeFactorial() {
        return new OperatorToken("!", OperatorToken.FACTORIAL, OperatorToken.EXPONENT_PRECEDENCE, false, 0, false) {
            @Override
            public double operate(double left, double right) {
                if (left % 1 != 0) { // Makes sure this is an integer
                    throw new IllegalArgumentException();
                }
                return Utility.factorial((int) left);
            }
        };
    }

    public static OperatorToken makeVariableRoot() {
        return new OperatorToken("√", OperatorToken.VARROOT, OperatorToken.EXPONENT_PRECEDENCE, false, 0, false) {
            @Override
            public double operate(double left, double right) {
                if (left == 0) {
                    throw new IllegalArgumentException("Cannot have 0 as the base for a variable root!");
                }
                return Math.pow(right, 1 / left);
            }
        };
    }

    public static OperatorToken makePermutation() {
        return new OperatorToken("P", OperatorToken.PERMUTATION, OperatorToken.EXPONENT_PRECEDENCE, false, 0, false) {
            @Override
            public double operate(double left, double right) {
                if (left % 1 != 0 || right % 1 != 0) {
                    throw new IllegalArgumentException("Arguments must be integers");
                } else if (right > left) {
                    throw new IllegalArgumentException("n must be greater than or equal to r");
                } else {
                    try {
                        if (right == left) {
                            return Utility.factorial((int) left);
                        } else {
                            double unrounded = Utility.factorial((int) left) / Utility.factorial((int) (left - right));
                            return Math.round(unrounded); //Can only give integer ansswers; fixes floating point errors
                        }
                    } catch (StackOverflowError e) {
                        throw new IllegalArgumentException("The calculation is to large to compute.");
                    }
                }
            }
        };
    }

    public static OperatorToken makeCombination() {
        return new OperatorToken("C", OperatorToken.COMBINATION, OperatorToken.EXPONENT_PRECEDENCE, false, 0, false) {
            @Override
            public double operate(double left, double right) throws NumberTooLargeException {
                if (left % 1 != 0 || right % 1 != 0) {
                    throw new IllegalArgumentException("Arguments must be integers");
                } else {
                    if (right > left) {
                        throw new IllegalArgumentException("n must be greater than or equal to r");
                    } else if (right == left) {
                        return 1;
                    } else {
                        try {
                            double unrounded = Utility.factorial((int) left) / (Utility.factorial((int) right) * Utility.factorial((int) (left - right)));
                            return Math.round(unrounded);
                        } catch (StackOverflowError e) {
                            throw new IllegalArgumentException("The calculation is to large to compute.");
                        }
                    }
                }
            }
        };
    }
}
