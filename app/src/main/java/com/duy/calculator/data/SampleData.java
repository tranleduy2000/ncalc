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

package com.duy.calculator.data;

/**
 * Created by Duy on 29-Jan-17.
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
