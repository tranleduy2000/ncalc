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

package com.example.duy.calculator.item_math_type;

import android.content.Context;

import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.Constants;
import com.example.duy.calculator.math_eval.FormatExpression;

/**
 * Created by DUy on 29-Dec-16.
 */

public class IntegrateItem extends IExprInput {
    protected final String var = "x";
    protected String from, to;
    protected String input;

    public IntegrateItem(String input, String f, String t) {
        this.from = FormatExpression.cleanExpression(f);
        this.to = FormatExpression.cleanExpression(t);
        this.input = FormatExpression.cleanExpression(input);
    }

    @Override
    public String getError(BigEvaluator evaluator, Context applicationContext) {
        return null;
    }

    public String getVar() {
        return var;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public boolean isError(BigEvaluator evaluator) {
        return false;
    }

    @Override
    public String toString() {
        return "Integrate " + input +
                Constants.FROM + " " + from + " " +
                Constants.TO + " " + to;
    }

    public String getInput() {
        return "Int(" + input + ",{" + var + "," + from + "," + to + "})";
    }

    public void setInput(String input) {
        this.input = input;
    }
}
