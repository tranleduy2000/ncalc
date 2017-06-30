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

import com.duy.calc.casio.token.ConstantToken;
import com.duy.calc.casio.token.NumberToken;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.token.VariableToken;

import java.util.ArrayList;

/**
 * Contains static methods that will create Variable tokens.
 *
 * @author Alston Lin, Ejaaz Merali
 * @version 3.0
 */
public class VariableFactory {
    //Advanced
    public static ArrayList<Token> A_Value = new ArrayList<>();
    public static ArrayList<Token> B_Value = new ArrayList<>();
    public static ArrayList<Token> C_Value = new ArrayList<>();
    public static ArrayList<Token> D_Value = new ArrayList<>();
    public static ArrayList<Token> E_Value = new ArrayList<>();
    public static ArrayList<Token> F_Value = new ArrayList<>();
    public static ArrayList<Token> M_Value = new ArrayList<>();
    public static ArrayList<Token> X_Value = new ArrayList<>();
    public static ArrayList<Token> Y_Value = new ArrayList<>();

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
    private static ArrayList<Token> preAnsValueCompute = new ArrayList<>();
    private static ArrayList<Token> ansValueCompute = new ArrayList<>();

    public static ArrayList<Token> ansValueFunc = new ArrayList<>();
    public static ArrayList<Token> ansValueVec = new ArrayList<>();
    public static ArrayList<Token> ansValueMat = new ArrayList<>();

    public static VariableToken makeA() {
        return new VariableToken(VariableToken.A, "A") {
            public ArrayList<Token> getValue() {
                return A_Value;
            }
        };
    }

    public static VariableToken makeB() {
        return new VariableToken(VariableToken.B, "B") {
            public ArrayList<Token> getValue() {
                return B_Value;
            }
        };
    }

    public static VariableToken makeC() {
        return new VariableToken(VariableToken.C, "C") {
            public ArrayList<Token> getValue() {
                return C_Value;
            }
        };
    }

    public static VariableToken makeD() {
        return new VariableToken(VariableToken.D, "D") {
            public ArrayList<Token> getValue() {
                return D_Value;
            }
        };
    }

    public static VariableToken makeE() {
        return new VariableToken(VariableToken.E, "E") {
            public ArrayList<Token> getValue() {
                return E_Value;
            }
        };
    }

    public static VariableToken makeF() {
        return new VariableToken(VariableToken.F, "F") {
            public ArrayList<Token> getValue() {
                return F_Value;
            }
        };
    }

    public static VariableToken makeM() {
        return new VariableToken(VariableToken.M, "M") {
            public ArrayList<Token> getValue() {
                return M_Value;
            }
        };
    }


    public static VariableToken makeConstant() {
        return new VariableToken(VariableToken.CONSTANT, "Constant") {
            public ArrayList<Token> getValue() {
                return C_Value;
            }
        };
    }

    public static VariableToken makeX() {
        return new VariableToken(VariableToken.X, "X") {
            public ArrayList<Token> getValue() {
                return X_Value;
            }
        };
    }

    public static VariableToken makeY() {
        return new VariableToken(VariableToken.Y, "Y") {
            public ArrayList<Token> getValue() {
                return Y_Value;
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

    public static ConstantToken makeExponent() {
        return new ConstantToken("e", "", "e", VariableToken.E_VALUE, "SI");
    }

    public static VariableToken makeAnsCompute() {
        return new VariableToken(VariableToken.ANS, "Ans") {
            public ArrayList<Token> getValue() {
                return ansValueCompute;
            }
        };
    }

    public static VariableToken makePreAnsCompute() {
        return new VariableToken(VariableToken.PRE_ANS, "PreAns") {
            public ArrayList<Token> getValue() {
                return preAnsValueCompute;
            }
        };
    }

    public static VariableToken makeAnsFunc() {
        return new VariableToken(VariableToken.ANS, "Ans") {
            public ArrayList<Token> getValue() {
                return ansValueFunc;
            }
        };
    }

    public static VariableToken makeAnsVec() {
        return new VariableToken(VariableToken.ANS, "Ans") {
            public ArrayList<Token> getValue() {
                return ansValueVec;
            }
        };
    }

    public static VariableToken makeAnsMat() {
        return new VariableToken(VariableToken.ANS, "Ans") {
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


    public static void setAnsValue(ArrayList<Token> result) {
        preAnsValueCompute = ansValueCompute;
        ansValueCompute = result;
    }
}
