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

package com.duy.calculator.evaluator;

import java.util.ArrayList;

/**
 * Created by Duy on 22-Mar-18.
 */

public class CustomFunctions {
    private static final String COMBINATION = "Comb(n_, k_):=(factorial(Ceiling(n))/(factorial(Ceiling(k))*factorial(Ceiling(n-k))))";
    private static final String BINOMIAL = "Perm(n_, k_):=(factorial(Ceiling(n)) / " +
            "(factorial(Ceiling(n - k))))";
    private static final String CUBEROOT = "cbrt(x_):= x^(1/3)";
    private static final String CEILING = "Ceil(x_):=Ceiling(x)";

    public static ArrayList<String> getAllCustomFunctions() {
        ArrayList<String> functions = new ArrayList<>();
        functions.add(COMBINATION);
        functions.add(BINOMIAL);
        functions.add(CUBEROOT);
        functions.add(CEILING);
        return functions;
    }
}
