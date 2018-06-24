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
