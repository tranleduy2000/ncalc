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

import java.util.ArrayList;

/**
 * Created by Duy on 22-Mar-18.
 */

public class CustomFunctions {
    private static final String COMBINATION = "Comb(n_, k_):=(Binomial(n.k))";
    private static final String BINOMIAL = "Perm(n_, k_):=(Gamma(n+1) / Gamma(k+1))";
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
