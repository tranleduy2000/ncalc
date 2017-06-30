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

import com.duy.calc.casio.evaluator.Utility;
import com.duy.calc.casio.exception.NumberTooLargeException;
import com.duy.calc.casio.token.OperatorToken;
import com.duy.calc.casio.token.Token;

/**
 * Contains static methods that will create Operator pieces.
 *
 * @author Alston Lin, Ejaaz Merali
 * @version 3.0
 */
public class OperatorFactory {


    public static OperatorToken makeAdd() {
        return new OperatorToken("+", OperatorToken.ADD, Token.ADD_SUB_PRECEDENCE, OperatorToken.LEFT) {
            @Override
            public double operate(double left, double right) {
                return left + right;
            }
        };
    }

    public static OperatorToken makeSubtract() {
        return new OperatorToken("−", OperatorToken.SUB, Token.ADD_SUB_PRECEDENCE, OperatorToken.LEFT) {
            @Override
            public double operate(double left, double right) {
                return left - right;
            }
        };
    }

    public static OperatorToken makeMultiply() {
        return new OperatorToken("×", OperatorToken.MUL, Token.MUL_DIV_PRECEDENCE, Token.LEFT) {
            @Override
            public double operate(double left, double right) {
                return left * right;
            }
        };
    }

    public static OperatorToken makeDivide() {
        return new OperatorToken("÷", OperatorToken.DIV, Token.MUL_DIV_PRECEDENCE, Token.LEFT) {
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
        return new OperatorToken("", OperatorToken.FRACTION, Token.LEFT, true, 0, false) {
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

    public static OperatorToken makePower() {
        return new OperatorToken("", OperatorToken.POWER, Token.POWER_PRECEDENCE, Token.RIGHT) {
            @Override
            public double operate(double left, double right) {
                double result = Math.pow(left, right);
                if (result == Double.NaN)
                    throw new IllegalArgumentException("The answer involves Imaginary numbers (currently not supported)");
                return result;
            }

            @Override
            public String toString() {
                return "^";
            }
        };
    }

    public static OperatorToken makeFactorial() {
        return new OperatorToken("!", OperatorToken.FACTORIAL, OperatorToken.FACTORIAL_PRECEDENCE, false, 0, false) {
            @Override
            public double operate(double left, double right) {
                if (left % 1 != 0) { // Makes sure this is an integer
                    throw new IllegalArgumentException();
                }
                return Utility.factorial((int) left);
            }
        };
    }

    public static OperatorToken makePercent() {
        return new OperatorToken("%", OperatorToken.PERCENT, OperatorToken.PERCENT_PRECEDENCE, false, 0, false) {
            @Override
            public double operate(double left, double right) {
                return left / 100.0d;
            }
        };
    }

    public static OperatorToken makePermutation() {
        return new OperatorToken("P", OperatorToken.PERMUTATION, Token.PERMUTATION_PRECEDENCE, Token.LEFT) {
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
                            return Math.round(unrounded); //Can only give integer answer; fixes floating point errors
                        }
                    } catch (StackOverflowError e) {
                        throw new IllegalArgumentException("The calculation is to large to compute.");
                    }
                }
            }
        };
    }

    public static OperatorToken makeCombination() {
        return new OperatorToken("C", OperatorToken.COMBINATION, OperatorToken.COMBINATION_PRECEDENCE, Token.LEFT) {
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

    public static Token makeNeg() {
        return new OperatorToken("_", OperatorToken.NEG, Token.NEG_PRECEDENCE, Token.PREFIX) {
            @Override
            public double operate(double left, double right) throws NumberTooLargeException {
                return 0;
            }
        };
    }

    public static OperatorToken makeSurd() {
        return new OperatorToken("√", OperatorToken.SURD, OperatorToken.SQRT_PRECEDENCE, Token.PREFIX) {
            @Override
            public double operate(double left, double right) {
                if (left == 0) {
                    throw new IllegalArgumentException("Cannot have 0 as the base for a variable root!");
                }
                return Math.pow(right, 1 / left);
            }
        };
    }

    public static OperatorToken makeEqual() {
        return new OperatorToken("=", OperatorToken.EQUAL, OperatorToken.SQRT_PRECEDENCE, Token.PREFIX) {
            @Override
            public double operate(double left, double right) {
                if (left == 0) {
                    throw new IllegalArgumentException("Cannot have 0 as the base for a variable root!");
                }
                return Math.pow(right, 1 / left);
            }
        };
    }

    public static OperatorToken makeQuotient() {
        return new OperatorToken("÷R", OperatorToken.QUOTIENT, OperatorToken.MUL_DIV_PRECEDENCE, Token.LEFT) {
            @Override
            public double operate(double left, double right) {
                if (left == 0) {
                    throw new IllegalArgumentException("Cannot have 0 as the base for a variable root!");
                }
                return Math.pow(right, 1 / left);
            }
        };
    }

    public static OperatorToken makeAnd() {
        return new OperatorToken("and", OperatorToken.AND, OperatorToken.AND_PRECEDENCE, Token.LEFT) {
            @Override
            public double operate(double left, double right) {
                long l = (long) left;
                long r = (long) right;;
                return l & r;
            }
        };
    }

    public static OperatorToken makeOr() {
        return new OperatorToken("or", OperatorToken.OR, OperatorToken.OR_PRECEDENCE, Token.LEFT) {
            @Override
            public double operate(double left, double right) {
                long l = (long) left;
                long r = (long) right;;
                return l | r;
            }
        };
    }

    public static OperatorToken makeXor() {
        return new OperatorToken("xor", OperatorToken.XOR, OperatorToken.XOR_PRECEDENCE, Token.LEFT) {
            @Override
            public double operate(double left, double right) {
                long l = (long) left;
                long r = (long) right;;
                return l ^ r;
            }
        };
    }


}
