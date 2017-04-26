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

package com.example.duy.calculator.math_eval;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by DUy on 21-Jan-17.
 */

public class DefineFunction {

    private BigEvaluator mEvaluator;

    public DefineFunction(BigEvaluator e) {
        this.mEvaluator = e;
    }

    public void define(String var, double value) {
        try {
            mEvaluator.getEvalUtilities().defineVariable(var, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void define(String var, String value) {
        try {
            Double res = Double.valueOf(
                    String.valueOf(mEvaluator.getEvalUtils().evaluate("N(" + value + ")")));
            define(var, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * get list variable in expression
     * <p>
     * input: 2x + 3a + b + 1 + i1 + i2= 2
     * output is [x, a, b, i1, i2]
     *
     * @param expr input expression
     * @return -  List
     */
    public ArrayList<String> getListVariables(String expr) {
        ArrayList<String> variables = new ArrayList<>();
        try {
            String res = mEvaluator.evaluateWithResultNormal("Variables(" + expr + ")");
            res = res.replace("{", "");
            res = res.replace("}", "");
            String tmp[] = res.split(",");
            Collections.addAll(variables, tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return variables;
    }
}
