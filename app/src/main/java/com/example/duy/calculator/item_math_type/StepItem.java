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

/**
 * Created by Duy on 19-May-17.
 */

public class StepItem extends AExprInput {
    private String input;
    private String result;
    private int depth;

    public StepItem(String input, String result, int depth) {
        this.input = input;
        this.result = result;
        this.depth = depth;
    }

    @Override
    public boolean isError(BigEvaluator evaluator) {
        return false;
    }

    /**
     * @return output with Latex format
     */
    public String toTex() {
        return "$$" + getInput() + "$$";
    }

    public String getInput() {
        return input + " = " + result;
    }

    @Override
    public String getError(BigEvaluator evaluator, Context applicationContext) {
        return null;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
