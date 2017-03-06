package com.example.duy.calculator.item_math_type;

import android.content.Context;

import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.Constants;

/**
 * Created by DUy on 06-Jan-17.
 */

public class NumberIntegerItem extends IExprInput {
    private String number = "";
    private String cmd = "";

    public NumberIntegerItem(String number) {
        this.number = number;
    }

    public NumberIntegerItem() {
        this.number = Constants.ZERO;
    }

    public int length() {
        return number.length();
    }

    @Override
    public boolean isError(BigEvaluator evaluator) {
        return false;
    }

    @Override
    public String getInput() {
        return this.cmd + "(" + number + ")";
    }

    @Override
    public String toString() {
        return number;
    }

    @Override
    public String getError(BigEvaluator evaluator, Context applicationContext) {
        return null;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
