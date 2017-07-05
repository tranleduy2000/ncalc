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
