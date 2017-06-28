package com.example.duy.calculator.item_math_type;

import android.content.Context;

import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.Constants;

/**
 * Created by DUy on 06-Jan-17.
 */

public class ModuleItem extends IExprInput {
    protected String num1 = "0";
    protected String num2 = "0";

    public ModuleItem(String n, String k) {
        this.num1 = n;
        this.num2 = k;
    }

    public String getNum1() {
        return num1;
    }

    public void setNum1(String num1) {
        this.num1 = num1;
    }

    public String getNum2() {
        return num2;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    @Override
    public boolean isError(BigEvaluator evaluator) {
        return false;
    }

    @Override
    public String toString() {
        return Constants.MODULE + Constants.LEFT_PAREN + this.num1 + "," + this.num2
                + Constants.RIGHT_PAREN;
    }

    @Override
    public String getInput() {
        return Constants.MODULE + Constants.LEFT_PAREN + this.num1 + "," + this.num2
                + Constants.RIGHT_PAREN;
    }

    @Override
    public String getError(BigEvaluator evaluator, Context applicationContext) {
        return null;
    }
}
