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
