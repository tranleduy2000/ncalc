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

package com.duy.ncalc.utils;

/**
 * Created by Duy on 19/7/2016
 */

public class StringMathUtils {

    public static String optimizeMath(String s) {
        String math = "";
        s = s.replace('{', '(');
        s = s.replace('}', ')');
        s = s.replace('[', '(');
        s = s.replace(']', ')');
        s = s.replace(':', '/');
        for (int i = 0; i < s.length(); i++) {
            if (isAccept(s.charAt(i))) {
                math += s.charAt(i);
            }
        }
        return math;
    }

    private static boolean isAccept(char c) {
        // 0123456789
        if (c >= 48 && c <= 57) {
            return true;
        }

//        if (c == 97 || c == 98 || c == 99) {
//            return true;
//        }
//
//        if (c >= 89 && c <= 91) {
//            return true;
//        }
//
//        if (c >= 120 && c <= 122) {
//            return true;
//        }
        switch (c) {
            case '!':
            case '%':
            case '(':
            case ')':
            case '*':
            case '+':
            case 45:
            case 46:
            case 47:
            case 60:
            case 61:
            case 62:
            case 35:
            case 94:
                return true;
        }

        return false;
    }
}
