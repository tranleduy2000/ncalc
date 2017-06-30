/*
 * Copyright (c) 2017 by Tran Le Duy
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

package com.duy.calc.casio.tokenizer;

import com.duy.calc.casio.exception.EmptyInputException;
import com.duy.calc.casio.exception.NonBalanceBracketException;
import com.duy.calc.casio.exception.PlaceholderException;
import com.duy.calc.casio.token.BracketToken;
import com.duy.calc.casio.token.PlaceholderToken;
import com.duy.calc.casio.token.Token;

import java.util.List;
import java.util.Stack;

/**
 * Created by Duy on 27-Jun-17.
 */

public class ExceptionChecker {
    private static void checkBalanceBracket(List<Token> expr) throws NonBalanceBracketException {
        Stack<Token> stack = new Stack<>();
        for (int i = 0; i < expr.size(); i++) {
            Token current = expr.get(i);
            if (current instanceof BracketToken) {
                if (((BracketToken) current).isOpen()) {
                    stack.push(current);
                } else {
                    if (stack.isEmpty()) {
                        throw new NonBalanceBracketException(expr, i);
                    }
                    Token open = stack.pop();
                    if (current.getType() == BracketToken.PARENTHESES_CLOSE) {
                        if (open.getType() != BracketToken.PARENTHESES_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    } else if (current.getType() == BracketToken.SQUARE_CLOSED) {
                        if (open.getType() != BracketToken.SQUARE_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    } else if (current.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
                        if (open.getType() != BracketToken.SUPERSCRIPT_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    } else if (current.getType() == BracketToken.NUMERATOR_CLOSE) {
                        if (open.getType() != BracketToken.NUMERATOR_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    } else if (current.getType() == BracketToken.DENOMINATOR_CLOSE) {
                        if (open.getType() != BracketToken.DENOMINATOR_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    } else if (current.getType() == BracketToken.MAGNITUDE_CLOSE) {
                        if (open.getType() != BracketToken.MAGNITUDE_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    } else if (current.getType() == BracketToken.FRACTION_CLOSE) {
                        if (open.getType() != BracketToken.FRACTION_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    } else if (current.getType() == BracketToken.SQRT_CLOSE) {
                        if (open.getType() != BracketToken.SQRT_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    } else if (current.getType() == BracketToken.ABS_CLOSE) {
                        if (open.getType() != BracketToken.ABS_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    } else if (current.getType() == BracketToken.COMP_CLOSE) {
                        if (open.getType() != BracketToken.COMP_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    } else if (current.getType() == BracketToken.PERM_CLOSE) {
                        if (open.getType() != BracketToken.PERM_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    } else if (current.getType() == BracketToken.SUBSCRIPT_CLOSE) {
                        if (open.getType() != BracketToken.SUBSCRIPT_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    } else if (current.getType() == BracketToken.DERIVATIVE_CLOSE) {
                        if (open.getType() != BracketToken.DERIVATIVE_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    } else if (current.getType() == BracketToken.LOGN_CLOSE) {
                        if (open.getType() != BracketToken.LOGN_OPEN) {
                            throw new NonBalanceBracketException(expr, i);
                        }
                    }
                }
            }
        }
    }

    private static int checkNonPlaceholder(List<Token> expr) throws PlaceholderException {
        for (int i = 0; i < expr.size(); i++) {
            Token token = expr.get(i);
            if (token instanceof PlaceholderToken && token.getType() != PlaceholderToken.COMMA) {
                throw new PlaceholderException(expr, i);
            }
        }
        return -1;
    }

    private static void checkInputEmpty(List<Token> tokens) {
        if (tokens.isEmpty()) throw new EmptyInputException();
    }

    public static void checkAll(List<Token> tokens) {
        checkInputEmpty(tokens);
        checkNonPlaceholder(tokens);
        checkBalanceBracket(tokens);
    }

    public static String getMessage(Exception e){
        return "";
    }
}
