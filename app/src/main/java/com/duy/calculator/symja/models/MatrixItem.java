/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.calculator.symja.models;

import android.content.Context;

import com.duy.calculator.evaluator.MathEvaluator;

/**
 * Created by Duy on 09-Jan-17.
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
