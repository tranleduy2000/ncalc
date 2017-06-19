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

package com.duy.calculator.data;

/**
 * Created by DUy on 29-Jan-17.
 */

public class SampleData {
    public static final String[] TRIG_EXPAND_DATA = new String[]{
            "Sin(2x)"
    };
    public static final String[] TRIG_REDUCE_DATA = new String[]{
            "2*sin(x)*cos(x)"
    };
    public static final String[] TRIG_EXPONENT_DATA = new String[]{
            "sin(x)"
    };
    public static final String[] SIMPLIFY_DATA = new String[]{
            "sqrt(20)-sqrt(45)-3*sqrt(18)+sqrt(72)",
            "(sqrt(6)+sqrt(6))^2-sqrt(120)"
    };
    public static final String[] DERIVATIVE_DATA = new String[]{
            "sqrt(20)-sqrt(45)-3*sqrt(18)+sqrt(72)",
            "(sqrt(6)+sqrt(6))^2-sqrt(120)"
    };
    public static final String[] ANTI_DERIVATIVE_DATA = new String[]{
            "x^2 - 3x + 1/x",
            "(2x^4 + 3)/(x^2)",
            "(x-1)/x^2",
            "(x^2-1)^2/x^2",
            "tan(x)^2", "cos(x)^2", "(tan(x)-cot(x))^2", "sin(3x)"
    };
    final String[] SOLVE_DATA = new String[]{
            "2x + 1 = 0",
            "ax + b = 0",
            "x^2 + 3x - 1 = 0",
            "2x^2 - 3m*x + 2= 0",
    };

}
