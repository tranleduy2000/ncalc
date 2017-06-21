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

import com.duy.casiofx.token.VectorOperator;
import com.duy.casiofx.util.VectorUtilities;
import com.duy.casiofx.token.NumberToken;
import com.duy.casiofx.token.Token;
import com.duy.casiofx.token.VectorToken;

/**
 * Contains static factory methods of Functions used by Vectors.
 *
 * @author Alston Lin
 * @version 3.0
 */
public class VectorOperatorFactory {

    public static VectorOperator makeAdd() {
        return new VectorOperator("+", VectorOperator.ADD, 1, true) {
            @Override
            public Token operate(Token left, Token right) {
                if (left instanceof VectorToken && right instanceof VectorToken) {
                    VectorToken leftVector = (VectorToken) left;
                    VectorToken rightVector = (VectorToken) right;
                    if (leftVector.getDimensions() == rightVector.getDimensions()) {
                        double[] values = new double[leftVector.getDimensions()];
                        for (int i = 0; i < leftVector.getDimensions(); i++) {
                            values[i] += leftVector.getValues()[i] + rightVector.getValues()[i];
                        }
                        return new VectorToken(values);
                    } else {
                        throw new IllegalArgumentException("Attempted to operate two vector of different dimensions");
                    }
                } else if (left instanceof NumberToken && right instanceof NumberToken) {
                    return new NumberToken(((NumberToken) left).getValue() + ((NumberToken) right).getValue());
                } else {
                    throw new IllegalArgumentException("Illegal Adding.");
                }
            }
        };
    }

    public static VectorOperator makeSubtract() {
        return new VectorOperator("-", VectorOperator.SUBTRACT, 1, true) {
            @Override
            public Token operate(Token left, Token right) {
                if (left instanceof VectorToken && right instanceof VectorToken) {
                    VectorToken leftVector = (VectorToken) left;
                    VectorToken rightVector = (VectorToken) right;
                    if (leftVector.getDimensions() == rightVector.getDimensions()) {
                        double[] values = new double[leftVector.getDimensions()];
                        for (int i = 0; i < leftVector.getDimensions(); i++) {
                            values[i] += leftVector.getValues()[i] - rightVector.getValues()[i];
                        }
                        return new VectorToken(values);
                    } else {
                        throw new IllegalArgumentException("Attempted to operate two vector of different dimensions");
                    }
                } else if (left instanceof NumberToken && right instanceof NumberToken) {
                    return new NumberToken(((NumberToken) left).getValue() - ((NumberToken) right).getValue());
                } else {
                    throw new IllegalArgumentException("Illegal Subtracting.");
                }
            }
        };
    }

    public static VectorOperator makeDot() {
        return new VectorOperator("•", VectorOperator.DOT, 2, true) {
            @Override
            public Token operate(Token left, Token right) {
                if (left instanceof VectorToken && right instanceof VectorToken) {
                    return new NumberToken(VectorUtilities.findDotProduct((VectorToken) left, (VectorToken) right));
                } else if (left instanceof NumberToken && right instanceof VectorToken) {
                    return VectorUtilities.findScalarProduct(((NumberToken) left).getValue(), (VectorToken) right);
                } else if (right instanceof NumberToken && left instanceof VectorToken) {
                    return VectorUtilities.findScalarProduct(((NumberToken) right).getValue(), (VectorToken) left);
                } else if (left instanceof NumberToken && right instanceof NumberToken) {
                    return new NumberToken(((NumberToken) left).getValue() * ((NumberToken) right).getValue());
                } else {
                    throw new IllegalArgumentException("Illegal Dot Product,");
                }
            }
        };
    }

    public static VectorOperator makeCross() {
        return new VectorOperator("×", VectorOperator.CROSS, 3, true) {
            @Override
            public Token operate(Token left, Token right) {
                if (left instanceof VectorToken && right instanceof VectorToken) {
                    return VectorUtilities.findCrossProduct((VectorToken) left, (VectorToken) right);
                } else if (left instanceof NumberToken && right instanceof VectorToken) {
                    return VectorUtilities.findScalarProduct(((NumberToken) left).getValue(), (VectorToken) right);
                } else if (right instanceof NumberToken && left instanceof VectorToken) {
                    return VectorUtilities.findScalarProduct(((NumberToken) right).getValue(), (VectorToken) left);
                } else if (left instanceof NumberToken && right instanceof NumberToken) {
                    return new NumberToken(((NumberToken) left).getValue() * ((NumberToken) right).getValue());
                } else {
                    throw new IllegalArgumentException("Illegal Dot Product,");
                }
            }
        };
    }

    public static VectorOperator makeAngle() {
        return new VectorOperator("∠", VectorOperator.ANGLE, 3, true) {
            @Override
            public Token operate(Token left, Token right) {
                if (left instanceof VectorToken && right instanceof VectorToken) {
                    VectorToken leftVector = (VectorToken) left;
                    VectorToken rightVector = (VectorToken) right;
                    if (leftVector.getDimensions() == rightVector.getDimensions()) {
                        double result;
                        double magnitudes = (VectorUtilities.calculateMagnitude(leftVector) * VectorUtilities.calculateMagnitude(rightVector));
                        double arccos = (VectorUtilities.findDotProduct(leftVector, rightVector))
                                / magnitudes;
                        //Fixes the rounding issue when the result is close to 1
                        if (Math.abs(1 - arccos) < 1e-5) {
                            arccos = 1;
                        }
                        result = Math.acos(arccos);
                        //Switched depending on angle mode
                        if (Double.isNaN(result)) {
                            result = 0d;
                        }
                        switch (FunctionToken.angleMode) {
                            case FunctionToken.DEGREE:
                                result *= (180 / Math.PI);
                                break;
                            case FunctionToken.RADIAN:
                                //Deos nothing
                                break;
                            case FunctionToken.GRADIAN:
                                result *= (200 / Math.PI);
                                break;
                        }
                        return new NumberToken(result);
                    } else {
                        throw new IllegalArgumentException("Attempted to operate two vector of different dimensions");
                    }
                } else {
                    throw new IllegalArgumentException("Can only find an angle between two Vectors");
                }
            }
        };
    }
}