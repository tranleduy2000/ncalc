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
