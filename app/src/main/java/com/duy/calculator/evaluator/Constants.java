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

import java.util.regex.Pattern;

/**
 * Created by Will on 11/8/2014.
 */
public class Constants {
    public static final char INFINITY_UNICODE = '\u221e';
    public static final char DEGREE_UNICODE = '\u00b0';

    public static final char LEFT_PAREN = '(';
    public static final char RIGHT_PAREN = ')';

    public static final char MINUS_UNICODE = '-';
    public static final char MUL_UNICODE = '×';
    public static final char PLUS_UNICODE = '+';
    public static final char DIV_UNICODE = '÷';
    public static final char POWER_UNICODE = '^';
    public static final char EQUAL_UNICODE = '=';

    public static final String INFINITY = "Infinity";
    public static final String ZERO = "0";

    public static final String FALSE = "false";
    public static final String TRUE = "true";
    public static final String FROM = "From";
    public static final String TO = "To";

    public static final String INTEGRATE = "Integrate";
    public static final String PRIMITIVE = INTEGRATE;
    public static final String SOLVE = "Solve";
    public static final String MODULE = "Mod";

    public static final String X = "x";
    public static final String Y = "y";
    public static final String WEB_SEPARATOR = "<hr>";
    public static final String DERIVATIVE = "D";
    public static final String C = "C";
    public static final String P = "P";
    public static final String K = "K";

    public static char DECIMAL_POINT;
    public static char DECIMAL_SEPARATOR;
    public static char BINARY_SEPARATOR;
    public static char HEXADECIMAL_SEPARATOR;
    public static char MATRIX_SEPARATOR;
    public static char OCTAL_SEPARATOR;
    public static String REGEX_NUMBER;
    public static String REGEX_NOT_NUMBER;

    static {
        rebuildConstants();
    }

    /**
     * If the locale changes, but the app is still in memory, you may need to rebuild these constants
     */
    public static void rebuildConstants() {
        DECIMAL_POINT = '.';
        DECIMAL_SEPARATOR = ' ';

        // Use a space for Bin and Hex
        BINARY_SEPARATOR = ' ';
        HEXADECIMAL_SEPARATOR = ' ';
        OCTAL_SEPARATOR = ' ';

        // We have to be careful with the Matrix Separator.
        // It defaults to "," but that's a common decimal point.
        MATRIX_SEPARATOR = ',';
        String number = "A-F0-9" +
                Pattern.quote(String.valueOf(DECIMAL_POINT)) +
                Pattern.quote(String.valueOf(DECIMAL_SEPARATOR)) +
                Pattern.quote(String.valueOf(BINARY_SEPARATOR)) +
                Pattern.quote(String.valueOf(OCTAL_SEPARATOR)) +
                Pattern.quote(String.valueOf(HEXADECIMAL_SEPARATOR));

        REGEX_NUMBER = "[" + number + "]";
        REGEX_NOT_NUMBER = "[^" + number + "]";
    }
}
