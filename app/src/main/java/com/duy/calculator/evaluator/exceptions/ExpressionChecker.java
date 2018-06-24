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

package com.duy.calculator.evaluator.exceptions;

import java.util.Stack;

/**
 * Created by Duy on 01-Jul-17.
 */

public class ExpressionChecker {

    public static void checkBalanceBracket(String expr) throws NonBalanceBracketException {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == '(') {
                stack.push(expr.charAt(i));
            } else if (expr.charAt(i) == ')') {
                if (stack.isEmpty()) throw new NonBalanceBracketException(expr, i);
                Character open = stack.pop();
                if (open != '(') {
                    throw new NonBalanceBracketException(expr, i);
                }
            }
        }
    }


    public static void checkExpression(String expr) {
        checkBalanceBracket(expr);
    }
}
