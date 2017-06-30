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

import com.duy.calc.casio.token.MatrixToken;
import com.duy.calc.casio.token.StringToken;
import com.duy.calc.casio.token.Token;

import java.util.ArrayList;

/**
 * Created by Duy on 26-Jun-17.
 */
public class MatrixFactory {
    @SuppressWarnings("unchecked")
    public static MatrixToken createMatrix(String[][] entries) {
        ArrayList<Token>[][] temp = new ArrayList[entries.length][entries[0].length];
        for (int i = 0; i < entries.length; i++) {
            for (int j = 0; j < entries[0].length; j++) {
                ArrayList<Token> entry = new ArrayList<>();
                entry.add(new StringToken(entries[i][j]));
                temp[i][j] = entry;
            }
        }
        return new MatrixToken(temp);
    }

    public static Token createMatrixHyp() {
        String[][] hyp = new String[5][2];
        hyp[0][0] = "1:sinh  ";
        hyp[0][1] = "2:cosh  ";
        hyp[1][0] = hyp[1][1] = "";
        hyp[2][0] = "3:tanh  ";
        hyp[2][1] = "4:arsinh";
        hyp[3][0] = hyp[3][1] = "";
        hyp[4][0] = "5:arcosh";
        hyp[4][1] = "6:artanh";
        return MatrixFactory.createMatrix(hyp);
    }

    public static Token createMatrixMode() {
        String[][] modes = new String[4][1];
        modes[0][0] = "1:COMP    2:CMPLX";
        modes[1][0] = "3:STAT    4:BASE-N";
        modes[2][0] = "5:EQN     6:MATRIX";
        modes[3][0] = "7:TABLE   8:VECTOR";
        return new MatrixToken(modes);
    }

}
