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

import org.matheclipse.core.interfaces.IExpr;

import java.util.regex.Pattern;

/**
 * Created by Duy on 30-Jun-17.
 */

public class Utility {
    private static final Pattern ACCEPT_FRACTION = Pattern.compile("(sin|cos|tan|sinh|cosh|tanh|" +
                    "arcsin|arccos|arctan|" +
                    "arcsinh|arccosh|arctanh|log|ln|gcd|lcm|pol|rec|int|intg|randomint|randomreal)",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern ACCEPT_DECIMAL = Pattern.compile("(sin|cos|tan|sinh|cosh|tanh|" +
            "arcsin|arccos|arctan|" +
            "arcsinh|arccosh|arctanh|log|ln|gcd|lcm|pol|rec|" +
            "int|intg|randomint|randomreal|sqrt|cbrt|[/*])", Pattern.CASE_INSENSITIVE);

    public static boolean isAcceptResultFraction(IExpr expr) {

        return !ACCEPT_FRACTION.matcher(expr.toString()).find();
    }

    public static boolean isAcceptResultDecimal(IExpr expr) {
        return !ACCEPT_DECIMAL.matcher(expr.toString()).find();
    }
}
