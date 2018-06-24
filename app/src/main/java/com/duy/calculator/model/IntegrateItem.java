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

package com.duy.calculator.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.duy.calculator.evaluator.Constants;
import com.duy.calculator.evaluator.FormatExpression;
import com.duy.calculator.evaluator.MathEvaluator;

/**
 * Created by Duy on 29-Dec-16.
 */

public class IntegrateItem extends ExprInput {
    protected final String var = "x";
    protected String from, to;
    protected String input;

    public IntegrateItem(@NonNull String input, @NonNull String f, @NonNull String t) {
        this.from = FormatExpression.cleanExpression(f);
        this.to = FormatExpression.cleanExpression(t);
        this.input = FormatExpression.cleanExpression(input);
    }

    @Override
    public String getError(MathEvaluator evaluator, Context applicationContext) {
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
    public boolean isError(MathEvaluator evaluator) {
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
