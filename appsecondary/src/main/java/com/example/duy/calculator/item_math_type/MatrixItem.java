package com.example.duy.calculator.item_math_type;

import android.content.Context;

import com.example.duy.calculator.math_eval.BigEvaluator;

/**
 * Created by DUy on 09-Jan-17.
 */

public class MatrixItem extends IExprInput {
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
    public boolean isError(BigEvaluator evaluator) {
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
    public String getError(BigEvaluator evaluator, Context applicationContext) {
        return null;
    }
}
