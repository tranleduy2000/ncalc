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
import com.duy.calculator.evaluator.Constants;

/**
 * Created by Duy on 06-Jan-17.
 */

public class ModuleItem extends ExprInput {
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
    public boolean isError(MathEvaluator evaluator) {
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
    public String getError(MathEvaluator evaluator, Context applicationContext) {
        return null;
    }
}
