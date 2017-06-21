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

import com.duy.casiofx.token.ConstantToken;
import com.duy.casiofx.token.NumberToken;
import com.duy.casiofx.token.Token;
import com.duy.casiofx.token.VariableToken;

import java.util.ArrayList;

/**
 * Contains static methods that will create Variable tokens.
 *
 * @author Alston Lin, Ejaaz Merali
 * @version 3.0
 */
public class VariableFactory {
    //Advanced
    public static ArrayList<Token> aValue = new ArrayList<>();
    public static ArrayList<Token> bValue = new ArrayList<>();
    public static ArrayList<Token> cValue = new ArrayList<>();

    //Function
    public static ArrayList<Token> xValue = new ArrayList<>();
    public static ArrayList<Token> yValue = new ArrayList<>();

    //Vector
    public static ArrayList<Token> uValue = new ArrayList<>();
    public static ArrayList<Token> vValue = new ArrayList<>();
    public static ArrayList<Token> sValue = new ArrayList<>();
    public static ArrayList<Token> tValue = new ArrayList<>();

    //Matrix
    public static ArrayList<Token> matrixAValue = new ArrayList<>();
    public static ArrayList<Token> matrixBValue = new ArrayList<>();
    public static ArrayList<Token> matrixCValue = new ArrayList<>();

    //Ans
    public static ArrayList<Token> ansValueAdv = new ArrayList<>(); //Values of the variables
    public static ArrayList<Token> ansValueFunc = new ArrayList<>(); //Values of the variables
    public static ArrayList<Token> ansValueVec = new ArrayList<>(); //Values of the variables
    public static ArrayList<Token> ansValueMat = new ArrayList<>(); //Values of the variables

    public static VariableToken makeA() {
        return new VariableToken(VariableToken.A, "A") {
            public ArrayList<Token> getValue() {
                return aValue;
            }
        };
    }

    public static VariableToken makeB() {
        return new VariableToken(VariableToken.B, "B") {
            public ArrayList<Token> getValue() {
                return bValue;
            }
        };
    }

    public static VariableToken makeC() {
        return new VariableToken(VariableToken.C, "C") {
            public ArrayList<Token> getValue() {
                return cValue;
            }
        };
    }

    public static VariableToken makeConstant() {
        return new VariableToken(VariableToken.CONSTANT, "Constant") {
            public ArrayList<Token> getValue() {
                return cValue;
            }
        };
    }

    public static VariableToken makeX() {
        return new VariableToken(VariableToken.X, "X") {
            public ArrayList<Token> getValue() {
                return xValue;
            }
        };
    }

    public static VariableToken makeY() {
        return new VariableToken(VariableToken.Y, "Y") {
            public ArrayList<Token> getValue() {
                return yValue;
            }
        };
    }

    public static VariableToken makePI() {
        return new VariableToken(VariableToken.PI, "π") {
            public ArrayList<Token> getValue() {
                ArrayList<Token> tokens = new ArrayList<>();
                tokens.add(new NumberToken(PI_VALUE));
                return tokens;
            }

            public String toLaTeX() {
                return "$\\pi$";
            }
        };
    }

    public static VariableToken makeE() {
        return new VariableToken(VariableToken.E, "e") {
            public ArrayList<Token> getValue() {
                ArrayList<Token> tokens = new ArrayList<>();
                tokens.add(new NumberToken(E_VALUE));
                return tokens;
            }

            public String toLaTeX() {
                return "$e$";
            }
        };
    }

    public static VariableToken makeAnsAdv() {
        return new VariableToken(VariableToken.ANS, "ANS") {
            public ArrayList<Token> getValue() {
                return ansValueAdv;
            }
        };
    }

    public static VariableToken makeAnsFunc() {
        return new VariableToken(VariableToken.ANS, "ANS") {
            public ArrayList<Token> getValue() {
                return ansValueFunc;
            }
        };
    }

    public static VariableToken makeAnsVec() {
        return new VariableToken(VariableToken.ANS, "ANS") {
            public ArrayList<Token> getValue() {
                return ansValueVec;
            }
        };
    }

    public static VariableToken makeAnsMat() {
        return new VariableToken(VariableToken.ANS, "ANS") {
            public ArrayList<Token> getValue() {
                return ansValueMat;
            }
        };
    }

    public static VariableToken makeU() {
        return new VariableToken(VariableToken.U, "U") {
            public ArrayList<Token> getValue() {
                return uValue;
            }
        };
    }

    public static VariableToken makeV() {
        return new VariableToken(VariableToken.V, "V") {
            public ArrayList<Token> getValue() {
                return vValue;
            }
        };
    }

    public static VariableToken makeS() {
        return new VariableToken(VariableToken.S, "s") {
            public ArrayList<Token> getValue() {
                return sValue;
            }
        };
    }

    public static VariableToken makeT() {
        return new VariableToken(VariableToken.T, "t") {
            public ArrayList<Token> getValue() {
                return tValue;
            }
        };
    }

    public static VariableToken makeMatrixA() {
        return new VariableToken(VariableToken.MATRIX_A, "A") {
            public ArrayList<Token> getValue() {
                return matrixAValue;
            }
        };
    }

    public static VariableToken makeMatrixB() {
        return new VariableToken(VariableToken.MATRIX_B, "B") {
            public ArrayList<Token> getValue() {
                return matrixBValue;
            }
        };
    }

    public static VariableToken makeMatrixC() {
        return new VariableToken(VariableToken.MATRIX_C, "C") {
            public ArrayList<Token> getValue() {
                return matrixCValue;
            }
        };
    }

    public static VariableToken makeConstantToken(ConstantToken inConstant) {
        final ConstantToken constant = inConstant;
        return new VariableToken(VariableToken.CONSTANT, constant.getSymbol()) {
            public ArrayList<Token> getValue() {
                ArrayList<Token> tokens = new ArrayList<>();
                tokens.addAll(constant.getValue());
                return tokens;
            }

            public String toLaTeX() {
                return constant.getSymbol();
            }
        };
    }

}
