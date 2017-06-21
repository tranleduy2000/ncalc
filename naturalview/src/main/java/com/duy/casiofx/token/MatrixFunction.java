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

/**
 * The Object representation of a mathematical Function that has a Matrix as the input.
 *
 * @author Ejaaz Merali
 * @version 3.0
 */
public abstract class MatrixFunction extends Token {

    public static final int REF = 1, RREF = 2, DET = 3, TRANSPOSE = 4, INVERSE = 5, DIAG = 6,
            EIGENVECT = 7, EIGENVAL = 8, TRACE = 9, RANK = 10, LU = 11;

    /**
     * Should not be used outside of a factory; to create a type of function,
     * see class FunctionFactory.
     *
     * @param symbol The symbol of the Token to be shown on the calculator screen
     * @param type   The type of Function this is, as defined by the class constants
     */
    protected MatrixFunction(String symbol, int type) {
        super(symbol);
        this.type = type;
    }

    /**
     * Performs the function with the given input.
     *
     * @param input The input that is given for the function
     * @return The output of the performed function
     */
    public abstract double[][] perform(double[][] input);

    /**
     * @return The type of Function this is, as defined by the class constants
     */
    public int getType() {
        return type;
    }

}
