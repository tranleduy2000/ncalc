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

package com.duy.calculator.item_math_type;

import android.content.Context;

import com.duy.calculator.evaluator.MathEvaluator;

/**
 * Created by DUy on 09-Jan-17.
 */

public class MatrixItem extends ExprInput {
    private int row, col;
    private String[][] matrix;
    private String[] vector; //dont uses
    private String[] variable;

    public MatrixItem(int row, int col, String[][] matrix, String[] vector, String[] variable) {
        this.row = row;
        this.col = col;
        this.matrix = matrix;
        this.vector = vector;
        this.variable = variable;
    }

    public MatrixItem(int row, int col, String[][] matrix, String[] variable) {
        this.row = row;
        this.col = col;
        this.matrix = matrix;
        this.variable = variable;
    }

    @Override
    public boolean isError(MathEvaluator evaluator) {
        return false;
    }

    @Override
    public String getInput() {
        StringBuilder result = new StringBuilder();
        result.append("Solve(");
        result.append("{");
        //{{1, 1}, {1, 2}}
        for (int i = 0; i < row; i++) {
            result.append("{");
            for (int j = 0; j < col; j++) {
                result.append("(")
                        .append(matrix[i][j])
                        .append(")")
                        .append("*")
                        .append(variable[i]);
                if (j != col - 1) result.append(",");
            }
            result.append("}");
            if (i != row - 1) result.append(",");
        }
        result.append("},{");
        for (int i = 0; i < variable.length; i++) {
            result.append(variable[i]);
            if (i != variable.length - 1) result.append(",");
        }
        result.append("})");
        return result.toString();
    }

    @Override
    public String toString() {
        return getInput();
    }

    @Override
    public String getError(MathEvaluator evaluator, Context applicationContext) {
        return null;
    }
}
