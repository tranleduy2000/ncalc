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
