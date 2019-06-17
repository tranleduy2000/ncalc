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
 * Created by Duy on 29-Dec-16.
 */

public abstract class ExprInput {
    /**
     * check error syntax for input
     *
     * @param evaluator - Class for evaluate
     * @return - true if input error
     */
    public abstract boolean isError(MathEvaluator evaluator);

    /**
     * build and return input
     * <p>
     * such as:
     * <p>
     * Solve(2x + 1, x)
     * Int(x, x)
     * D(2x + 3)
     * Int(sqrt(x), {x, 2, 4}
     *
     * @return - input
     */
    public abstract String getInput();

    /**
     * return error of input
     *
     * @param evaluator          - BigEvaluator for evaluate input if needed
     * @param applicationContext - mContext of application for get string language
     * @return - error found
     */
    public abstract String getError(MathEvaluator evaluator, Context applicationContext);


}
