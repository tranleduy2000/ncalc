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

import com.duy.calc.casio.token.BracketToken;
import com.duy.calc.casio.token.DigitToken;
import com.duy.calc.casio.token.FunctionToken;
import com.duy.calc.casio.token.MatrixToken;
import com.duy.calc.casio.token.OperatorToken;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.token.VariableToken;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Duy on 22-Jun-17.
 */

public class TokenUtil {

    /**
     * Gets the numerator of a specified fraction in the given expression.
     *
     * @param expression The expression where the numerator is located
     * @param i          The index of the fraction Token
     * @return The numerator of the fraction
     */
    public static ArrayList<Token> getNumerator(List<Token> expression, int i) {

        if (!(expression.get(i) instanceof OperatorToken && expression.get(i).getType() == OperatorToken.FRACTION)) {
            throw new IllegalArgumentException("Given index of the expression is not a Fraction Token.");
        }
        ArrayList<Token> num = new ArrayList<>();
        //Finds the numerator
        int bracketCount = 1;
        int j = i - 2;
        while (bracketCount > 0 && j >= 0) {
            Token token = expression.get(j);
            if (token instanceof BracketToken && token.getType() == BracketToken.NUMERATOR_OPEN) {
                bracketCount--;
            } else if (token instanceof BracketToken && token.getType() == BracketToken.NUMERATOR_CLOSE) {
                bracketCount++;
            }
            num.add(0, token);
            j--;
        }
        num.remove(0); //Takes out the open bracket
        return num;
    }

    /**
     * Gets the denominator of a specified fraction in the given expression.
     *
     * @param expression    The expression where the denom is located
     * @param fractionIndex The index of the fraction Token
     * @return The denom of the fraction
     */
    public static ArrayList<Token> getDenominator(List<Token> expression, int fractionIndex) {
        if (!(expression.get(fractionIndex) instanceof OperatorToken && expression.get(fractionIndex).getType() == OperatorToken.FRACTION)) {
            throw new IllegalArgumentException("Given index of the expression is not a Fraction Token.");
        }
        ArrayList<Token> denom = new ArrayList<>();
        //Now denom
        int bracketCount = 1;
        int j = fractionIndex + 2;
        while (bracketCount > 0 && j < expression.size()) {
            Token token = expression.get(j);
            if (token instanceof BracketToken && token.getType() == BracketToken.DENOMINATOR_OPEN) {
                bracketCount++;
            } else if (token instanceof BracketToken && token.getType() == BracketToken.DENOMINATOR_CLOSE) {
                bracketCount--;
            }
            denom.add(token);
            j++;
        }
        if (denom.size() > 0) {
            denom.remove(denom.size() - 1); //Takes out the close bracket
        }
        return denom;
    }

    /**
     * Finds the index where the denominator of the fraction Token at index i ends.
     *
     * @param fractionIndex The index of the fraction Token
     * @param expression    The expression to find the denominator end
     * @return The index of the end of the denominator
     */
    public static int getDenominatorEnd(int fractionIndex, List<Token> expression) {
        int j = fractionIndex + 2;
        int bracketCount = 1;
        while (bracketCount > 0) {
            Token t = expression.get(j);
            if (t instanceof BracketToken && t.getType() == BracketToken.DENOMINATOR_CLOSE) {
                bracketCount--;
            } else if (t instanceof BracketToken && t.getType() == BracketToken.DENOMINATOR_OPEN) {
                bracketCount++;
            }
            j++;
        }
        return j;
    }


    /**
     * Finds the max number of lines of text (vertically) there are in the expression
     *
     * @param expression The expression to find the height
     * @return The maximum height of a fraction in the given expression
     */
    public static int getMaxLine(List<Token> expression) {
        int maxFracHeight = 1;
        int numBracketCount = 0;
        int denomBracketCount = 0;
        boolean inExponent = false;
        int expBracketCount = 0;

        int maxNumerator = 1; //maximum line numerator of the fraction
        int maxDenominator = 0; //maximum line denominator of the fraction

        for (int i = 0; i < expression.size(); i++) {
            Token t = expression.get(i);
            if (t instanceof BracketToken) {
                switch (t.getType()) {
                    case BracketToken.SUPERSCRIPT_OPEN:
                        expBracketCount++;
                        inExponent = true;
                        break;
                    case BracketToken.SUPERSCRIPT_CLOSE:
                        expBracketCount--;
                        if (expBracketCount == 0) {
                            inExponent = false;
                        }
                        break;
                    case BracketToken.NUMERATOR_OPEN:
                        numBracketCount++;
                        break;
                    case BracketToken.NUMERATOR_CLOSE:
                        numBracketCount--;
                        break;
                    case BracketToken.DENOMINATOR_OPEN:
                        denomBracketCount++;
                        break;
                    case BracketToken.DENOMINATOR_CLOSE:
                        denomBracketCount--;
                        break;
                }
            } else if (t instanceof MatrixToken) {
                int height = ((MatrixToken) t).getNumOfRows();
                if (height > maxFracHeight) {
                    maxFracHeight = height;
                }
            }

            //Cannot be in a numerator or denominator or an exponent
            if (numBracketCount == 0 && denomBracketCount == 0 && !inExponent) {
                if (t instanceof OperatorToken && t.getType() == OperatorToken.FRACTION) {

                    ArrayList<Token> numerator = getNumerator(expression, i);
                    ArrayList<Token> denominator = getDenominator(expression, i);

                    int maxLineNumerator = getMaxLine(numerator);
                    int maxLineDenominator = getMaxLine(denominator);

                    maxNumerator = Math.max(maxLineNumerator, maxNumerator);
                    maxDenominator = Math.max(maxLineDenominator, maxDenominator);
                }
            }
        }
        return maxNumerator + maxDenominator;
    }


    public static int getSubScriptGroupStartIndex(List<Token> expression, int i) {
        if (!(expression.get(i) instanceof BracketToken
                && expression.get(i).getType() == BracketToken.SUBSCRIPT_CLOSE)) {
            throw new InvalidParameterException("given i not a subscript close token");
        }
        //Extract the exponent expression
        int j = i - 1;
        int count = 1;
        while (count != 0 && j > -1) {
            Token t = expression.get(j);
            if (t instanceof BracketToken && t.getType() == BracketToken.SUBSCRIPT_CLOSE) {
                count--;
            } else if (t instanceof BracketToken && t.getType() == BracketToken.SUBSCRIPT_OPEN) {
                count++;
            }
            j--;
        }
        return j;
    }

    public static int getSubScriptGroupEndIndex(List<Token> expression, int i) {
        if (!(expression.get(i) instanceof BracketToken
                && expression.get(i).getType() == BracketToken.SUBSCRIPT_OPEN)) {
            throw new InvalidParameterException("given i not a subscript open token");
        }
        //Extract the exponent expression
        int j = i + 1;
        int count = 1;
        while (count != 0 && j < expression.size()) {
            Token t = expression.get(j);
            if (t instanceof BracketToken && t.getType() == BracketToken.SUBSCRIPT_CLOSE) {
                count++;
            } else if (t instanceof BracketToken && t.getType() == BracketToken.SUBSCRIPT_OPEN) {
                count--;
            }
            j++;
        }
        return Math.max(j, expression.size() - 1);
    }

    /**
     * Gets the index where the fraction starts.
     *
     * @param expression The expression to look at
     * @param numClose   The index where the closing bracket would be placed
     * @return The index where the NUMERATOR_OPEN bracket should go
     */
    public static int getFractionStart(List<Token> expression, int numClose) {
        Token tokenBefore = expression.get(numClose - 1);
        if (tokenBefore instanceof DigitToken || tokenBefore instanceof VariableToken) {
            int i = numClose - 1;
            if (tokenBefore instanceof VariableToken) {
                while (i >= 0 && expression.get(i) instanceof VariableToken) {
                    i--;
                }
            } else { //Digit
                while (i >= 0 && expression.get(i) instanceof DigitToken) {
                    i--;
                }
            }
            return i + 1;
        } else if (tokenBefore instanceof BracketToken && tokenBefore.getType() == BracketToken.PARENTHESES_CLOSE) {
            int i = numClose - 2;
            int bracketCount = 1;
            while (i >= 0 && bracketCount != 0) {
                Token t = expression.get(i);
                if (t instanceof BracketToken) {
                    BracketToken b = (BracketToken) t;
                    if (b.getType() == BracketToken.PARENTHESES_OPEN) {
                        bracketCount--;
                    } else if (b.getType() == BracketToken.PARENTHESES_CLOSE) {
                        bracketCount++;
                    }
                }
                i--;
            }
            //Includes the function if there is one
            if (i >= 0 && expression.get(i) instanceof FunctionToken) {
                i--;
            }
            return i + 1;
        } else if (tokenBefore instanceof BracketToken && tokenBefore.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
            //Goes to the token before the start of the superscript
            int i = numClose - 2;
            int bracketCount = 1;
            while (i >= 0 && bracketCount != 0) {
                Token t = expression.get(i);
                if (t instanceof BracketToken) {
                    BracketToken b = (BracketToken) t;
                    if (b.getType() == BracketToken.SUPERSCRIPT_OPEN) {
                        bracketCount--;
                    } else if (b.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
                        bracketCount++;
                    }
                }
                i--;
            }
            //Frac will start at whatever it wouldve been at what the exponent is over
            return getFractionStart(expression, i);
        } else if (tokenBefore instanceof BracketToken && tokenBefore.getType() == BracketToken.SQRT_CLOSE) {
            int i = numClose - 1;
            List<Token> group = getGroupFromEnd(expression, i, BracketToken.SQRT_OPEN, BracketToken.SQRT_CLOSE);
            return i - group.size();
        } else if (tokenBefore instanceof BracketToken && tokenBefore.getType() == BracketToken.ABS_CLOSE) {
            int i = numClose - 1;
            List<Token> group = getGroupFromEnd(expression, i, BracketToken.ABS_OPEN, BracketToken.ABS_CLOSE);
            return i - group.size();
        } else if (tokenBefore instanceof BracketToken && tokenBefore.getType() == BracketToken.FRACTION_CLOSE) {
            int i = numClose - 1;
            List<Token> group = getGroupFromEnd(expression, i, BracketToken.FRACTION_OPEN, BracketToken.FRACTION_CLOSE);
            return i - group.size() + 1;
        } else {
            return numClose;
        }
    }

    public static ArrayList<Token> getInsideGroupFromStart(List<Token> expression, int start, int openType,
                                                           int closeType) {
        if (!(expression.get(start) instanceof BracketToken
                && expression.get(start).getType() == openType)) {
            throw new InvalidParameterException("the start index not given type = " + openType
                    + " current is " + expression.get(start));
        }

        Stack<BracketToken> stack = new Stack<>();
        stack.push((BracketToken) expression.get(start));

        ArrayList<Token> group = new ArrayList<>();
        int j = start + 1;
        int count = 1;
        while (j < expression.size()) {
            Token t = expression.get(j);
            if (t instanceof BracketToken && ((BracketToken) t).isOpen()) {
                stack.push((BracketToken) t);
                count++;
            } else if (t instanceof BracketToken) {
                if (stack.isEmpty()) {
                    return group;
                }
                BracketToken open = stack.pop();
                if (!(((BracketToken) t).hasRelation(open)) || stack.isEmpty()) {
                    return group;
                }
                count--;
            }
            if (count == 0) {
                break;
            } else {
                group.add(t);
            }
            j++;
        }
        return group;
    }

    public static ArrayList<Token> getInsideGroupFromEnd(List<Token> expression, int end, int openType,
                                                         int closeType) {
        if (!(expression.get(end) instanceof BracketToken
                && expression.get(end).getType() == closeType)) {
            throw new InvalidParameterException("the end index not given type = " + closeType);
        }

        Stack<BracketToken> stack = new Stack<>();
        stack.push((BracketToken) expression.get(end));

        ArrayList<Token> group = new ArrayList<>();

        int j = end - 1;
        int count = 1;
        while (j > -1) {
            Token t = expression.get(j);
            if (t instanceof BracketToken && ((BracketToken) t).isOpen()) {
                if (stack.isEmpty()) {
                    return group;
                }
                BracketToken close = stack.pop();
                if (!close.hasRelation((BracketToken) t) || stack.isEmpty()) {
                    return group;
                }
                count--;
            } else if (t instanceof BracketToken) {
                stack.push((BracketToken) t);
                count++;
            }
            if (count == 0) {
                break;
            } else {
                group.add(0, t);
            }
            j--;
        }
        return group;
    }

    public static ArrayList<Token> getGroupFromStart(List<Token> expression, int start, int openType,
                                                     int closeType) {
        if (!(expression.get(start) instanceof BracketToken
                && expression.get(start).getType() == openType)) {
            throw new InvalidParameterException("the start index not given type = " + openType);
        }
        //Extract the exponent expression
        ArrayList<Token> group = new ArrayList<>();
        Stack<BracketToken> stack = new Stack<>();

        group.add(expression.get(start));
        stack.push((BracketToken) expression.get(start));

        int j = start + 1;
        int scriptCount = 1;
        while (scriptCount != 0 && j < expression.size()) {
            Token t = expression.get(j);
            if (t instanceof BracketToken && t.getType() == openType) {
                scriptCount++;
                stack.push((BracketToken) t);
            } else if (t instanceof BracketToken && t.getType() == closeType) {
                scriptCount--;
            }
            group.add(t);
            j++;
        }
        return group;
    }

    public static List<Token> getGroupFromEnd(List<Token> expression, int end, int openType,
                                              int closeType) {
        if (!(expression.get(end) instanceof BracketToken
                && expression.get(end).getType() == closeType)) {
            throw new InvalidParameterException("the end index not given type = " + closeType);
        }
        LinkedList<Token> group = new LinkedList<>();
        group.add(expression.get(end));
        int j = end - 1;
        int count = 1;
        while (count != 0 && j > -1) {
            Token t = expression.get(j);
            if (t instanceof BracketToken && t.getType() == openType) {
                count--;
            } else if (t instanceof BracketToken && t.getType() == closeType) {
                count++;
            }
            group.addFirst(t);
            j--;
        }
        return group;
    }


    public static boolean hasEqualToken(List<Token> expr) {
        for (Token token : expr) {
            if (token instanceof OperatorToken && token.getType() == OperatorToken.EQUAL) {
                return true;
            }
        }
        return false;
    }

}
