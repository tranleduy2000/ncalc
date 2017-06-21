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

import org.apache.commons.math3.special.Erf;

/**
 * Contains static methods that will create function pieces.
 *
 * @author Ejaaz Merali
 * @version 3.0
 */

public class FunctionFactory {
    private static double sin(double a) {
        if (a % Math.PI == 0) {
            return 0;
        } else {
            return Math.sin(a);
        }
    }

    private static double cos(double a) {
        if ((a % Math.PI / 2 == 0) && (a % Math.PI != 0)) {
            return 0;
        } else {
            return Math.cos(a);
        }
    }

    private static double tan(double a) {
        if (sin(a) == 0) {
            return 0;
        } else if (cos(a) == 0) {
            throw new IllegalArgumentException("tan is not defined at this value!");
        } else {
            return Math.tan(a);
        }
    }

    //Secondary trig functions (perform() should never be called for these)
    public static FunctionToken makeCsc() {
        return new FunctionToken("csc", FunctionToken.CSC) {
            @Override
            public double perform(double input) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static FunctionToken makeSec() {
        return new FunctionToken("sec", FunctionToken.SEC) {
            @Override
            public double perform(double input) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static FunctionToken makeCot() {
        return new FunctionToken("cot", FunctionToken.COT) {
            @Override
            public double perform(double input) {
                throw new UnsupportedOperationException();
            }
        };
    }

    //Special functions
    public static FunctionToken makeErf() {
        return new FunctionToken("erf", FunctionToken.ERF) {
            @Override
            public double perform(double input) {
                return Erf.erf(input);
            }
        };
    }

    public static FunctionToken makeErfi() { //Do not call perform()
        return new FunctionToken("erfi", FunctionToken.ERFI) {
            @Override
            public double perform(double input) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static FunctionToken makeGamma() {
        return new FunctionToken("Γ", FunctionToken.GAMMA) {
            @Override
            public double perform(double input) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static FunctionToken makeAppellF1() { //Do not call perform()
        return new FunctionToken("AppellF1", FunctionToken.APPELLF1) {
            @Override
            public double perform(double input) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static FunctionToken makeSin() { //In degrees
        return new FunctionToken("sin", FunctionToken.SIN) {
            @Override
            public double perform(double input) {
                switch (angleMode) {
                    case DEGREE:
                        return sin(Math.toRadians(input));
                    case RADIAN:
                        return sin(input);
                    case GRADIAN:
                        return sin(input * (Math.PI / 200));
                    default:
                        throw new IllegalArgumentException("Illegal Angle Mode!");
                }
            }
        };
    }

    public static FunctionToken makeASin() { //Result will be in degrees
        //TODO: MAKE IT SIN-1
        return new FunctionToken("arcsin", FunctionToken.ARCSIN) {
            @Override
            public double perform(double input) {
                if (Math.abs(input) > 1) {//Makes sure the input is within the range of sin
                    throw new IllegalArgumentException("The answer involves Imaginary numbers (currently not supported)");
                } else {
                    switch (angleMode) {
                        case DEGREE:
                            return Math.toDegrees(Math.asin(input));
                        case RADIAN:
                            return Math.asin(input);
                        case GRADIAN:
                            return ((Math.asin(input)) * (200 / Math.PI));
                        default:
                            throw new IllegalArgumentException("Illegal Angle Mode!");
                    }
                }
            }
        };
    }

    public static FunctionToken makeCos() { //In degrees
        return new FunctionToken("cos", FunctionToken.COS) {
            @Override
            public double perform(double input) {
                switch (angleMode) {
                    case DEGREE:
                        if ((input % 90 == 0) && (input % 180 != 0)) {
                            return 0;
                        }
                        return cos(Math.toRadians(input));
                    case RADIAN:
                        return cos(input);
                    case GRADIAN:
                        if ((input % 100 == 0) && (input % 200 != 0)) {
                            return 0;
                        }
                        return cos(input * (Math.PI / 200));
                    default:
                        throw new IllegalArgumentException("Illegal Angle Mode!");
                }
            }
        };
    }

    public static FunctionToken makeACos() { //Result will be in degrees
        //TODO: MAKE IT SIN-1
        return new FunctionToken("arccos", FunctionToken.ARCCOS) {
            @Override
            public double perform(double input) {
                if (Math.abs(input) > 1) {//Makes sure the input is within the range of sin
                    throw new IllegalArgumentException("The answer involves Imaginary numbers (currently not supported)");
                } else {
                    switch (angleMode) {
                        case DEGREE:
                            return Math.toDegrees(Math.acos(input));
                        case RADIAN:
                            return Math.acos(input);
                        case GRADIAN:
                            return ((Math.acos(input)) * (200 / Math.PI));
                        default:
                            throw new IllegalArgumentException("Illegal Angle Mode!");
                    }
                }
            }
        };
    }

    public static FunctionToken makeTan() { //In degrees
        return new FunctionToken("tan", FunctionToken.TAN) {
            @Override
            public double perform(double input) {
                switch (angleMode) {
                    case DEGREE:
                        if ((input % 90 == 0) && (input % 180 != 0)) {
                            throw new IllegalArgumentException("tan is not defined at this value!");
                        }
                        return tan(Math.toRadians(input));
                    case RADIAN:
                        if ((input % (Math.PI / 2) == 0) && (input % Math.PI != 0)) {
                            throw new IllegalArgumentException("tan is not defined at this value!");
                        }
                        return tan(input);
                    case GRADIAN:
                        if ((input % 100 == 0) && (input % 200 != 0)) {
                            throw new IllegalArgumentException("tan is not defined at this value!");
                        }
                        return tan(input * (Math.PI / 200));
                    default:
                        throw new IllegalArgumentException("Illegal Angle Mode!");
                }
            }
        };
    }

    public static FunctionToken makeATan() { //Result will be in degrees
        return new FunctionToken("arctan", FunctionToken.ARCTAN) {
            @Override
            public double perform(double input) {
                switch (angleMode) {
                    case DEGREE:
                        return Math.toDegrees(Math.atan(input));
                    case RADIAN:
                        return Math.atan(input);
                    case GRADIAN:
                        return ((Math.atan(input)) * (200 / Math.PI));
                    default:
                        throw new IllegalArgumentException("Illegal Angle Mode!");
                }
            }
        };
    }

    //NOTE: Hyperbolic functions do not use angles
    public static FunctionToken makeSinh() {
        return new FunctionToken("sinh", FunctionToken.SINH) {
            @Override
            public double perform(double input) {
                return Math.sinh(input);
            }
        };
    }

    public static FunctionToken makeASinh() {
        return new FunctionToken("arsinh", FunctionToken.ARCSINH) {
            @Override
            public double perform(double input) {
                return Math.log(input + Math.sqrt(Math.pow(input, 2) + 1));
            }
        };
    }

    public static FunctionToken makeCosh() {
        return new FunctionToken("cosh", FunctionToken.COSH) {
            @Override
            public double perform(double input) {
                return Math.cosh(input);
            }
        };
    }

    public static FunctionToken makeACosh() {
        return new FunctionToken("arcosh", FunctionToken.ARCCOSH) {
            @Override
            public double perform(double input) {
                if (input >= 1) {
                    return Math.log(input + Math.sqrt(Math.pow(input, 2) - 1));
                } else {
                    throw new IllegalArgumentException("The answer involves Imaginary numbers (currently not supported)");
                }
            }
        };
    }

    public static FunctionToken makeTanh() {
        return new FunctionToken("tanh", FunctionToken.TANH) {
            @Override
            public double perform(double input) {
                return Math.tanh(input);
            }
        };
    }

    public static FunctionToken makeATanh() {
        return new FunctionToken("arctanh", FunctionToken.ARCTANH) {
            @Override
            public double perform(double input) {
                if (Math.abs(input) < 1) {
                    return 0.5 * Math.log((input + 1) / (1 - input));
                } else {
                    throw new IllegalArgumentException("The answer involves Imaginary numbers (currently not supported)");
                }
            }
        };
    }

    public static FunctionToken makeLog_10() {
        return new FunctionToken("log", FunctionToken.LOG10) {
            @Override
            public double perform(double input) {
                return Math.log10(input);
            }
        };
    }

    public static FunctionToken makeLn() {
        return new FunctionToken("ln", FunctionToken.LN) {
            @Override
            public double perform(double input) {
                return Math.log(input);
            }
        };
    }

    public static FunctionToken makeSqrt() {
        return new FunctionToken("√", FunctionToken.SQRT) {
            @Override
            public double perform(double input) {
                if (input < 0) {
                    throw new IllegalArgumentException("The answer involves Imaginary numbers (currently not supported)");
                } else {
                    return Math.sqrt(input);
                }
            }
        };
    }

}