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

package com.example.duy.calculator.utils;

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
