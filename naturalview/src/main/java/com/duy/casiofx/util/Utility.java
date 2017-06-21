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

package com.duy.casiofx.util;

import android.util.Log;

import com.duy.casiofx.exception.NumberTooLargeException;
import com.duy.casiofx.factory.BracketFactory;
import com.duy.casiofx.factory.DigitFactory;
import com.duy.casiofx.factory.FunctionToken;
import com.duy.casiofx.factory.OperatorFactory;
import com.duy.casiofx.token.BracketToken;
import com.duy.casiofx.token.DigitToken;
import com.duy.casiofx.token.NumberToken;
import com.duy.casiofx.token.OperatorToken;
import com.duy.casiofx.token.Token;
import com.duy.casiofx.token.VariableToken;
import com.duy.casiofx.token.VectorToken;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Contains miscellaneous static methods that provide utility.
 *
 * @author Alston Lin, Ejaaz Merali
 * @version 3.0
 */
public class Utility {

    private static final String TAG = "Utility";

    /**
     * Prints the given expression to be read by machine.
     *
     * @param expression The expression to print
     * @return The string representation of the given expression (machine readable)
     */
    public static String machinePrintExpression(ArrayList<Token> expression) {
        String s = "";
        for (int i = 0; i < expression.size(); i++) {
            Token token = expression.get(i);
            if (token instanceof NumberToken) {
                if (((NumberToken) token).getValue() % 1 != 0) {
                    s += ((NumberToken) token).getValue();
                } else {
                    s += (((NumberToken) token).getValue());
                }
            } else if (token instanceof OperatorToken) {
                switch (token.getType()) {
                    case OperatorToken.FRACTION:
                        s += "/";
                        break;
                    case OperatorToken.MULTIPLY:
                        s += "*";
                        break;
                    case OperatorToken.EXPONENT:
                        s += "^";
                        break;
                    case OperatorToken.SUBTRACT:
                        s += "-";
                        break;
                    case OperatorToken.VARROOT:
                        String base = "";
                        //Finds the root
                        int bracketCount = 1;
                        int j = s.length() - 2; //Starts at the character BEFORE the ")"
                        while (bracketCount > 0) {
                            char c = s.charAt(j);
                            if (c == ')') {
                                bracketCount++;
                            } else if (c == '(') {
                                bracketCount--;
                            }
                            s = s.substring(0, j); //Removes the last character
                            base = c + base; //Adds at the beginning of the root
                            j--;
                        }

                        //Removes the opening (
                        base = base.substring(1);

                        //Now adds the roots to the end of the Surd
                        ArrayList<Token> root = new ArrayList<>();
                        bracketCount = 1;
                        i = i + 2; //Starts after the ( bracket
                        while (bracketCount > 0) {
                            Token t = expression.get(i);
                            if (t instanceof BracketToken && t.getType() == BracketToken.OPEN) {
                                bracketCount++;
                            } else if (t instanceof BracketToken && t.getType() == BracketToken.CLOSE) {
                                bracketCount--;
                            }
                            root.add(t);
                            i++;
                        }
                        i--; //Reverses the last iteration
                        root.remove(root.size() - 1); //Takes out the closing )
                        s += "Surd(";
                        s += machinePrintExpression(root);
                        s += "," + base + ")";
                        break;
                    default:
                        s += token.getSymbol();
                }
            } else if (token instanceof VariableToken) {
                switch (token.getType()) {
                    case VariableToken.PI:
                        s += "Pi";
                        break;
                    case VariableToken.X:
                        s += "x";
                        break;
                    default:
                        s += token.getSymbol();
                        break;
                }
            } else if (token instanceof FunctionToken) {
                switch (token.getType()) {
                    case FunctionToken.SQRT:
                        s += "Sqrt";
                        break;
                    case FunctionToken.SIN:
                        s += "Sin";
                        break;
                    case FunctionToken.COS:
                        s += "Cos";
                        break;
                    case FunctionToken.TAN:
                        s += "Tan";
                        break;
                    case FunctionToken.CSC:
                        s += "Csc";
                        break;
                    case FunctionToken.SEC:
                        s += "Sec";
                        break;
                    case FunctionToken.COT:
                        s += "Cot";
                        break;
                    case FunctionToken.ERF:
                        s += "Erf";
                        break;
                    case FunctionToken.ARCSIN:
                        s += "ArcSin";
                        break;
                    case FunctionToken.ARCCOS:
                        s += "ArcCos";
                        break;
                    case FunctionToken.ARCTAN:
                        s += "ArcTan";
                        break;
                    case FunctionToken.SINH:
                        s += "Sinh";
                        break;
                    case FunctionToken.COSH:
                        s += "Cosh";
                        break;
                    case FunctionToken.TANH:
                        s += "Tanh";
                        break;
                    case FunctionToken.ARCSINH:
                        s += "ArcSinh";
                        break;
                    case FunctionToken.ARCCOSH:
                        s += "ArcCosh";
                        break;
                    case FunctionToken.ARCTANH:
                        s += "ArcTanh";
                        break;
                    case FunctionToken.LN:
                        s += "Ln";
                        break;
                    case FunctionToken.APPELLF1:
                        s += "AppellF1";
                        break;
                    default:
                        throw new IllegalArgumentException("NOT SUPPORTED YET: " + s);
                        //TODO: IMPLEMENT OTHERS
                }
            } else if (token instanceof BracketToken) {
                switch (token.getType()) {
                    case BracketToken.DENOMINATOR_OPEN:
                        s += "(";
                        break;
                    case BracketToken.NUMERATOR_OPEN:
                        s += "(";
                        break;
                    case BracketToken.SUPERSCRIPT_OPEN:
                        s += "(";
                        break;
                    case BracketToken.OPEN:
                        s += "(";
                        break;
                    case BracketToken.FRACTION_OPEN:
                        s += "(";
                        break;
                    case BracketToken.SUPERSCRIPT_CLOSE:
                        s += ")";
                        break;
                    case BracketToken.DENOMINATOR_CLOSE:
                        s += ")";
                        break;
                    case BracketToken.NUMERATOR_CLOSE:
                        s += ")";
                        break;
                    case BracketToken.FRACTION_CLOSE:
                        s += ")";
                        break;
                    case BracketToken.CLOSE:
                        s += ")";
                        break;
                    default:
                        throw new IllegalArgumentException("Error parsing expression"); //This should not happen
                }
            } else {
                s += token.getSymbol();
            }
        }
        return s;
    }

    /**
     * Prints the expression to be read by humans.
     *
     * @param expression The expression to print
     * @return The string representation of the expression
     */
    public static String printExpression(ArrayList<Token> expression) {
        String s = "";
        for (Token t : expression) {
            if (t instanceof OperatorToken && t.getType() == OperatorToken.FRACTION) {
                s += "/";
            } else {
                s += t.getSymbol();
            }
        }
        return s;
    }

    /**
     * Returns the numerical value of a given set of digits.
     *
     * @param digits A list of digits to find the value of
     * @return The value of the given digits
     */
    public static double valueOf(List<DigitToken> digits) {
        //Counters number of Decimals first
        int decimalCount = 0;
        for (DigitToken d : digits) {
            if (d.getValue() == -1) {
                decimalCount++;
            }
        }
        if (decimalCount > 1) {
            throw new IllegalArgumentException("Invalid Decimal Placement");
        }
        double value = 0;
        int indexOfDecimal = -1;
        boolean negative = false;
        //Does negatives first
        try {
            while (digits.get(0).getValue() == DigitFactory.NEGATIVE) { //Only accepts negatives at the beginning
                digits.remove(0);
                negative = !negative; //Allows for multiple negatives
            }
        } catch (IndexOutOfBoundsException e) { //The digits only contains negatives (occurs during adding a neg to variables)
            return negative ? -1 : 1;
        }
        //Finds what index the decimal is in
        for (int i = 0; i < digits.size(); i++) {
            if (digits.get(i).getValue() == -1) {
                indexOfDecimal = i;
            }
        }

        //Sets the starting power based on where the decimal place was found, if any
        int power = indexOfDecimal == -1 ? digits.size() - 1 : indexOfDecimal - 1;
        for (DigitToken digit : digits) {
            if (digit.getValue() == -1) { //Decimal place
            } else if (digit.getValue() == DigitFactory.NEGATIVE) {
                throw new IllegalArgumentException("Invalid placement of negatives in Digits");
            } else {
                value += digit.getValue() * round(Math.pow(10, power), Math.abs(power) + 1);
                power--;
            }
        }

        return negative ? value * -1 : value;
    }

    /**
     * Transforms all the digits into numbers as well as replacing Variables with numbers.
     *
     * @param tokens The expression to condense digits
     * @return The expression with the digits condensed
     */
    public static ArrayList<Token> condenseDigits(ArrayList<Token> tokens) {
        ArrayList<Token> newTokens = new ArrayList<>();
        ArrayList<DigitToken> digits = new ArrayList<>();
        boolean atDigits = false; //Tracks if it's currently tracking digits
        for (Token token : tokens) {
            if (atDigits) { //Going through digits
                if (token instanceof DigitToken) { //Number keeps going
                    digits.add((DigitToken) token);
                } else { //Number ended
                    atDigits = false;
                    NumberToken num = new NumberToken(Utility.valueOf(digits));
                    digits.clear();
                    //Special case of //-1 * Variable
                    if (num.getValue() == -1 && token instanceof VariableToken) {
                        ((VariableToken) token).setNegative(true);
                    } else if (num.getValue() == 1 && token instanceof VariableToken) { // 1 * Variable
                        ((VariableToken) token).setNegative(false);
                    } else if (num.getValue() < 0) { //Negative number; changes to -1 * Number
                        newTokens.add(new NumberToken(-1));
                        newTokens.add(OperatorFactory.makeMultiply());
                        newTokens.add(new NumberToken(num.getValue() * -1));
                    } else {
                        newTokens.add(num); //Adds the sum of all the digits
                    }
                    newTokens.add(token);
                }
            } else { //Not going through digits
                if (token instanceof DigitToken) { //Start of a number
                    atDigits = true;
                    digits.add((DigitToken) token);
                } else { //Not a digit; adds to the new list
                    newTokens.add(token);
                }
            }
        }
        if (!digits.isEmpty() && atDigits) { //Digits left
            newTokens.add(new NumberToken(Utility.valueOf(digits)));
        }
        return newTokens;
    }

    /**
     * Sets up the given expression to be processed and allows established
     * conventions (such as (a + b)(c + d) implying (a + b) * (c + d)).
     *
     * @param toSetup The expression to set up
     * @return The expression with the added Tokens to make the
     */
    public static ArrayList<Token> setupExpression(ArrayList<Token> toSetup) {
        ArrayList<Token> newExpression = new ArrayList<>();
        for (Token t : toSetup) {
            boolean negative = false;
            Token last = newExpression.isEmpty() ? null : newExpression.get(newExpression.size() - 1); //Last token in the new expression
            Token beforeLast = newExpression.size() > 1 ? newExpression.get(newExpression.size() - 2) : null;
            boolean lastIsSubtract = last instanceof OperatorToken && last.getType() == OperatorToken.SUBTRACT;
            boolean beforeLastIsOperator = beforeLast != null && beforeLast instanceof OperatorToken;
            boolean beforeLastIsOpenBracket = beforeLast != null && beforeLast instanceof BracketToken && (beforeLast.getType() == BracketToken.OPEN
                    || beforeLast.getType() == BracketToken.NUMERATOR_OPEN || beforeLast.getType() == BracketToken.DENOMINATOR_OPEN || beforeLast.getType() == BracketToken.SUPERSCRIPT_OPEN || beforeLast.getType() == BracketToken.FRACTION_OPEN);

            if (t instanceof BracketToken) {
                BracketToken b = (BracketToken) t;
                if (b.getType() == BracketToken.OPEN && last instanceof BracketToken && (last.getType() == BracketToken.CLOSE
                        || last.getType() == BracketToken.SUPERSCRIPT_CLOSE || last.getType() == BracketToken.DENOMINATOR_CLOSE)) { //Ex. (2 + 1)(3 + 4), (2)/(5)(x + 1) or x^(2)(x+1)
                    newExpression.add(OperatorFactory.makeMultiply()); //Implies multiplication between the two expressions in the brackets
                } else if ((last instanceof NumberToken || last instanceof VariableToken) && (b.getType() == BracketToken.OPEN || b.getType() == BracketToken.FRACTION_OPEN)) { //Ex. 3(2 + 1) or X(1+X) or 2(1/2)
                    newExpression.add(OperatorFactory.makeMultiply());
                } else if (last instanceof OperatorToken && last.getType() == OperatorToken.SUBTRACT && beforeLastIsOperator) { //Ex. E + -(X + 1) -> E + -1 * (X + 1)
                    newExpression.remove(last);
                    newExpression.add(new NumberToken(-1));
                    newExpression.add(OperatorFactory.makeMultiply());
                }
            } else if (t instanceof NumberToken || t instanceof VariableToken || t instanceof FunctionToken) { //So it works with Function mode too
                if (last instanceof NumberToken) { //Ex. 5A , 5f(x)
                    newExpression.add(OperatorFactory.makeMultiply());
                } else if (last instanceof BracketToken && (last.getType() == BracketToken.CLOSE
                        || last.getType() == BracketToken.SUPERSCRIPT_CLOSE || last.getType() == BracketToken.DENOMINATOR_CLOSE)) { //Ex. x^2(x + 1) or 2/5x
                    newExpression.add(OperatorFactory.makeMultiply());
                } else if (lastIsSubtract && (beforeLastIsOperator || beforeLastIsOpenBracket || newExpression.size() <= 1)) { //Ex. E * -X -> E * -1 * X
                    newExpression.remove(last);
                    if (t instanceof NumberToken) {
                        negative = true;
                    } else {
                        newExpression.add(new NumberToken(-1));
                        newExpression.add(OperatorFactory.makeMultiply());
                    }
                } else if (t instanceof FunctionToken && (last instanceof FunctionToken || last instanceof VariableToken)) { //Ex. f(x)g(x) or (1 + 2)f(x) or xf(x)
                    newExpression.add(OperatorFactory.makeMultiply());
                }

                if (t instanceof VariableToken && last instanceof VariableToken) { //Ex. pi x
                    newExpression.add(OperatorFactory.makeMultiply());
                }
            }
            if (negative) {
                newExpression.add(new NumberToken(((NumberToken) t).getValue() * -1));
            } else {
                newExpression.add(t);
            }
        }
        return newExpression;
    }

    /**
     * Looks for and multiplies constants next to each other in the given expression.
     * i.e. AB -> A * B
     *
     * @param toSetup The expression to multiply constants
     * @return The expression with the multiplication tokens added
     */
    public static ArrayList<Token> multiplyConstants(ArrayList<Token> toSetup) {
        ArrayList<Token> newExpression = new ArrayList<>();
        for (Token t : toSetup) {
            Token last = newExpression.isEmpty() ? null : newExpression.get(newExpression.size() - 1); //Last token in the new expression
            if (t instanceof VariableToken && last instanceof VariableToken) {
                newExpression.add(OperatorFactory.makeMultiply());
            }
            newExpression.add(t);
        }
        return newExpression;
    }

    /**
     * Uses the shunting yard algorithm to change the expression from infix to reverse polish.
     *
     * @param infix The infix expression
     * @return The expression in reverse polish
     * @throws IllegalArgumentException The infix notation is invalid
     */
    public static ArrayList<Token> convertToReversePolish(ArrayList<Token> infix) {
        Log.d(TAG, "convertToReversePolish() called with: infix = [" + infix + "]");

        ArrayList<Token> reversePolish = new ArrayList<>();
        Stack<Token> stack = new Stack<>();
        for (Token token : infix) {
            if (token instanceof NumberToken || token instanceof VariableToken) { //Adds directly to the queue if it's a token
                reversePolish.add(token);
            } else if (token instanceof FunctionToken) { //Adds to the stack if it's a function
                stack.push(token);
            } else if (token instanceof OperatorToken) {
                if (!stack.empty()) { //Make sure it's not empty to prevent bugs
                    Token top = stack.lastElement();
                    while (top != null && ((top instanceof OperatorToken && ((OperatorToken) token).isLeftAssociative()
                            && ((OperatorToken) top).getPrecedence() >= ((OperatorToken) token).getPrecedence()) || top instanceof FunctionToken)) { //Operator is left associative and higher precendence / is a function
                        reversePolish.add(stack.pop()); //Pops top element to the queue
                        top = stack.isEmpty() ? null : stack.lastElement(); //Assigns the top element of the stack if it exists
                    }
                }
                stack.push(token);
            } else if (token instanceof BracketToken) {
                BracketToken bracket = (BracketToken) token;
                if (bracket.getType() == BracketToken.OPEN
                        || bracket.getType() == BracketToken.SUPERSCRIPT_OPEN
                        || bracket.getType() == BracketToken.NUMERATOR_OPEN
                        || bracket.getType() == BracketToken.DENOMINATOR_OPEN
                        || bracket.getType() == BracketToken.FRACTION_OPEN
                        || bracket.getType() == BracketToken.SQRT_OPEN) { //Pushes the bracket to the stack if it's open
                    stack.push(bracket);
                } else if (bracket.getType() == BracketToken.CLOSE
                        || bracket.getType() == BracketToken.SUPERSCRIPT_CLOSE
                        || bracket.getType() == BracketToken.NUMERATOR_CLOSE
                        || bracket.getType() == BracketToken.DENOMINATOR_CLOSE
                        || bracket.getType() == BracketToken.FRACTION_CLOSE
                        || bracket.getType() == BracketToken.SQRT_CLOSE) { //For close brackets, pop operators onto the list until a open bracket is found
                    Token top = stack.lastElement();
                    while (!(top instanceof BracketToken)) { //While it has not found an open bracket
                        reversePolish.add(stack.pop()); //Pops the top element
                        if (stack.isEmpty()) { //Mismatched brackets
                            throw new IllegalArgumentException();
                        }
                        top = stack.lastElement();
                    }
                    stack.pop(); //Removes the bracket
                }
            }
        }
        //All tokens read at this point
        while (!stack.isEmpty()) { //Puts the remaining tokens in the stack to the queue
            reversePolish.add(stack.pop());
        }
        return reversePolish;
    }

    /**
     * Evaluates a given expression in reverse polish notation and returns the resulting value.
     *
     * @param tokens The expression in reverse polish
     * @return The value of the expression
     * @throws IllegalArgumentException The user has inputted an invalid expression
     */
    public static double evaluateExpression(ArrayList<Token> tokens) {
        Log.d(TAG, "evaluateExpression() called with: tokens = [" + tokens + "]");

        Stack<NumberToken> stack = new Stack<>();
        for (Token token : tokens) {
            if (token instanceof NumberToken) { //Adds all numbers directly to the stack
                stack.push((NumberToken) token);
            } else if (token instanceof OperatorToken) {
                //Operates the first and second top operators
                NumberToken right = stack.pop();
                NumberToken left = stack.pop();
                stack.push(new NumberToken(((OperatorToken) token).operate(left.getValue(), right.getValue()))); //Adds the result back to the stack
            } else if (token instanceof FunctionToken) { //Function uses the top number on the stack
                NumberToken top = stack.pop(); //Function performs on the first number
                stack.push(new NumberToken(((FunctionToken) token).perform(top.getValue()))); //Adds the result back to the stack
            } else { //This should never be reached
                throw new IllegalArgumentException();
            }
        }
        if (stack.size() == 0) {
            throw new IllegalArgumentException("Input is empty");
        } else if (stack.size() != 1) {
            throw new IllegalArgumentException("Illegal Expression"); //There should only be 1 token left on the stack
        } else {
            return stack.pop().getValue();
        }
    }

    public static ArrayList<Token> convertDoublesToVector(double[] vector) {
        ArrayList<Token> newVector = new ArrayList<>();
        newVector.add(BracketFactory.makeOpenSquareBracket());
        newVector.add(new NumberToken(vector[0]));
        newVector.add(new Token(",") {
        });
        newVector.add(new NumberToken(vector[1]));
        ;
        if (vector.length > 2) {
            newVector.add(new Token(",") {
            });
            newVector.add(new NumberToken(vector[2]));
        }
        newVector.add(BracketFactory.makeCloseSquareBracket());
        return newVector;
    }

    /**
     * Converts an expression of Tokens into an expression represented by a String
     *
     * @param expression Expression being converted to a string
     * @return String String representation of the expression
     */
    public static String convertTokensToString(ArrayList<Token> expression) {
        String stringExpression = "";
        for (int i = 0; i < expression.size(); i++) {
            if (expression.get(i) instanceof NumberToken) {
                String s = String.valueOf(((NumberToken) expression.get(i)).getValue());
                s = !s.contains(".") ? s : (s.indexOf("E") > 0 ? s.substring(0, s.indexOf("E")).replaceAll("0*$", "")
                        .replaceAll("\\.$", "").concat(s.substring(s.indexOf("E"))) : s.replaceAll("0*$", "")
                        .replaceAll("\\.$", "")); //Removes trailing zeroes
                stringExpression = stringExpression + s;
            } else {
                stringExpression = stringExpression + (expression.get(i)).getSymbol();
            }
        }
        return stringExpression;
    }

    /**
     * Gets the tokens of all stored vectors from variables and creates a new expression for calculations
     *
     * @param expression Expression of Tokens
     * @return ArrayList<Token> New expression of Tokens
     */
    public static ArrayList<Token> convertVariablesToTokens(ArrayList<Token> expression) {
        ArrayList<Token> tempExpression = new ArrayList<>();
        for (Token t : expression) {
            if (t instanceof VectorToken) {
                //ArrayList<Token> tempVector = ((Vector) t).getVector();
                //tempExpression.addAll(tempVector);
            } else {
                tempExpression.add(t);
            }
        }
        return tempExpression;

    }

    /**
     * Cleans up the given expression to render it more human-readable.
     *
     * @param expression The expression the clean up
     * @return The human readable version (note: not machine readable)
     */
    public static ArrayList<Token> cleanupExpressionForReading(ArrayList<Token> expression) {
        ArrayList<Token> newExpression = new ArrayList<>();
        for (int i = 0; i < expression.size(); i++) {
            Token t = expression.get(i);
            Token last = newExpression.size() > 0 ? newExpression.get(newExpression.size() - 1) : null;
            //Token beforeLast = newExpression.size() > 1 ? newExpression.get(newExpression.size() - 2) : null;
            if (t instanceof VariableToken && last != null && last instanceof OperatorToken && last.getType() == OperatorToken.MULTIPLY) { // E * V -> EV
                newExpression.remove(last);
            } else if (t instanceof BracketToken && t.getType() == BracketToken.OPEN && last instanceof OperatorToken && last.getType() == OperatorToken.MULTIPLY) { //E * (E) -> E(E)
                newExpression.remove(last);
            } else if (t instanceof FunctionToken && last instanceof OperatorToken && last.getType() == OperatorToken.MULTIPLY) { //E * F -> EF
                newExpression.remove(last);
            }
            newExpression.add(t);
        }
        return newExpression;
    }

    /**
     * Returns the argument of a vector. (angle to the X axis)
     *
     * @param vector The vector.
     * @return argument The angle of the vector to the X axis.
     */

    public static double calculateArgument(double[] vector) {
        if (vector.length != 2) {
            throw new IllegalArgumentException("Error: This feature is only usable with 2D vectors.");
        }
        double x = vector[0];
        double y = vector[1];

        if (x == 0) {
            return 90;
        }
        double argument = Utility.round(Math.abs(Math.toDegrees(Math.atan(y / x))), 3);
        return argument;
    }

    /**
     * Finds the value of the function at the given x value.
     *
     * @param function The function to evaluate
     * @param x        The x value to find the function at
     * @return The y value, or -1 if non-existant
     */
    public static double valueAt(ArrayList<Token> function, double x) {
        ArrayList<Token> expression = new ArrayList<>();
        //Substitutes all variables with the given x value
        for (Token token : function) {
            if (token instanceof VariableToken && token.getType() == VariableToken.X) {
                expression.add(((VariableToken) token).isNegative() ? new NumberToken(-x) : new NumberToken(x));
            } else if (token instanceof VariableToken) {
                ArrayList<Token> value = ((VariableToken) token).getValue();
                double result = Utility.process(value);
                expression.add(new NumberToken(result));
            } else {
                expression.add(token);
            }
        }
        try {
            double y = Utility.evaluateExpression(Utility.convertToReversePolish(expression));
            return y;
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Calculates the quadrant that the vector is in. Only works in 2D.
     *
     * @param vector The vector.
     * @return int The quadrant.
     */
    public static int calculateQuadrant(double[] vector) {
        if (vector.length != 2) {
            throw new IllegalArgumentException("Error: This feature is only usable with 2D vectors.");
        }
        double x = vector[0];
        double y = vector[1];

        //Quadrant 1
        if (x > 0d && y > 0d) {
            return 1;
        }
        //Quadrant 2
        if (x < 0 && y > 0) {
            return 2;
        }
        //Quadrant 3
        if (x < 0 && y < 0) {
            return 3;
        }
        //Quadrant 4
        if (x > 0 && y < 0) {
            return 4;
        }
        //vector lies on positive y axis
        if (x == 0 && y > 0) {
            return -1;
        }
        //vector lies on positive x axis
        if (x > 0 && y == 0) {
            return -2;
        }
        //vector lies on negative y axis
        if (x == 0 && y < 0) {
            return -3;
        }
        //vector lies on negative x axis
        if (x < 0 && y == 0) {
            return -4;
        }
        return -100;
    }

    /**
     * Calculates the direction of the vector using true bearings. Only works in 2D.
     *
     * @param vector The vector.
     * @return double The direction in true bearings
     */
    public static double calculateTrueBearing(double[] vector) {
        if (vector.length != 2) {
            throw new IllegalArgumentException("Error: This feature is only usable with 2D vectors.");
        }

        int quadrant = calculateQuadrant(vector);
        double trueBearing = -1;
        //Returns angles when lying on an axis
        if (quadrant == -1) { //positive y axis
            trueBearing = 0;
        } else if (quadrant == -2) { //positive x axis
            trueBearing = 90;
        } else if (quadrant == -3) { //negative y axis
            trueBearing = 180;
        } else if (quadrant == -4) { //negative x axis
            trueBearing = 270;
        }

        //Returns angles that do not lie on an axis
        if (quadrant == 1) {
            trueBearing = 90 - calculateArgument(vector);
        }
        if (quadrant == 2) {
            trueBearing = 270 + calculateArgument(vector);
        }
        if (quadrant == 3) {
            trueBearing = 180 + (90 - calculateArgument(vector));
        }
        if (quadrant == 4) {
            trueBearing = 90 + calculateArgument(vector);
        }
        return trueBearing;
    }

    /**
     * Returns the direction of a vector in bearing form. Only works with 2D vectors.
     *
     * @param vector The vector.
     * @return ArrayList<Token> The direction.
     */
    public static ArrayList<Token> calculateBearing(double[] vector) {
        double angle = calculateArgument(vector);
        int quadrant = calculateQuadrant(vector);
        ArrayList<Token> output = new ArrayList<>();

        //returns vectors that lie on an axis
        if (quadrant == -1) { //positive y axis
            output.add(new Token("N") {
            });
            output.add(new NumberToken(0));
            output.add(new Token("°") {
            });
            output.add(new Token("E") {
            });
        }
        if (quadrant == -2) { //positive x axis
            output.add(new Token("E") {
            });
            output.add(new NumberToken(0));
            output.add(new Token("°") {
            });
            output.add(new Token("N") {
            });
        }
        if (quadrant == -3) { //negative y axis
            output.add(new Token("S") {
            });
            output.add(new NumberToken(0));
            output.add(new Token("°") {
            });
            output.add(new Token("W") {
            });
        }
        if (quadrant == -4) { //negative x axis
            output.add(new Token("W") {
            });
            output.add(new NumberToken(0));
            output.add(new Token("°") {
            });
            output.add(new Token("S") {
            });
        }

        //returns vectors that do not lie on an axis
        if (quadrant == 1) {
            output.add(new Token("E") {
            });
            output.add(new NumberToken(Utility.round(angle, 3)));
            output.add(new Token("°") {
            });
            output.add(new Token("N") {
            });
        }
        if (quadrant == 2) {
            output.add(new Token("W") {
            });
            output.add(new NumberToken(Utility.round(angle, 3)));
            output.add(new Token("°") {
            });
            output.add(new Token("N") {
            });
        }
        if (quadrant == 3) {
            output.add(new Token("W") {
            });
            output.add(new NumberToken(Utility.round(angle, 3)));
            output.add(new Token("°") {
            });
            output.add(new Token("S") {
            });
        }
        if (quadrant == 4) {
            output.add(new Token("E") {
            });
            output.add(new NumberToken(Utility.round(angle, 3)));
            output.add(new Token("°") {
            });
            output.add(new Token("S") {
            });
        }
        return output;
    }

    /**
     * Returns a new vector represented as an array of doubles by multipliying the old vector by a
     * multiplier
     *
     * @param multiplier Multiplier to be multiplied into a vector
     * @param vector     Vector represented as an array of doubles
     * @return double[] New vector
     */
    public static double[] multiplyVector(double multiplier, double[] vector) {
        int dimensions = 0;
        if (vector.length == 2) {
            dimensions = 2;
        } else if (vector.length == 3) {
            dimensions = 3;
        }
        double[] newVector = new double[dimensions];
        newVector[0] = Utility.round(multiplier * vector[0], 3);
        newVector[1] = Utility.round(multiplier * vector[1], 3);
        if (dimensions > 2) {
            newVector[2] = Utility.round(multiplier * vector[2], 3);
        }
        return newVector;
    }

    /**
     * Finds the roots of any given function, if any
     *
     * @param function The function to finds the roots of
     * @return A list of roots for the function
     */
    public static ArrayList<Double> findRoots(ArrayList<Token> function) {
        return null;
    }

    /**
     * Finds the factorial of the given integer
     *
     * @param n The base of the factorial
     * @return The value of the factorial
     */
    public static double factorial(int n) throws NumberTooLargeException {
        if (n == 1 || n == 0) {
            return 1;
        } else {
            double result = n * factorial(n - 1);
            if (Double.isInfinite(result)) {
                throw new NumberTooLargeException();
            }
            return result;
        }
    }

    /**
     * Rounds the given double to the given amount of significant digits.
     *
     * @param unrounded The unrounded value
     * @param sigDigs   The amount of significant digits to round to
     * @return The rounded value
     */
    public static double round(double unrounded, int sigDigs) {
        BigDecimal rounded = new BigDecimal(unrounded);
        return rounded.round(new MathContext(sigDigs)).doubleValue();
    }

    /**
     * Adds any missing end brackets to the expression.
     *
     * @param expression The expression that may have missing brackets
     * @return The expression with all the missing brackets added to the end
     */
    public static ArrayList<Token> addMissingBrackets(ArrayList<Token> expression) {
        int bracketCount = 0;
        ArrayList<Token> newExpression = new ArrayList<>();
        //Counts brackets
        for (Token t : expression) {
            newExpression.add(t);
            if (t instanceof BracketToken) {
                BracketToken b = (BracketToken) t;
                if (b.getType() == BracketToken.OPEN) {
                    bracketCount++;
                } else if (b.getType() == BracketToken.CLOSE) {
                    bracketCount--;
                }
            }
        }
        //Adds missing brackets
        for (int i = bracketCount; i > 0; i--) {
            newExpression.add(BracketFactory.makeCloseParentheses());
        }
        return newExpression;
    }

    /**
     * Processes the expression and returns the result using the Shunting Yard Algorithm to convert
     * the expression into reverse polish and then evaluating it.
     *
     * @param tokens The expression to process
     * @return The numerical value of the expression
     * @throws IllegalArgumentException If the user has input a invalid expression
     */
    public static double process(ArrayList<Token> tokens) {
        tokens = setupExpression(subVariables(condenseDigits(addMissingBrackets(tokens))));
        Log.d(TAG, "process: " + tokens);
        return evaluateExpression(convertToReversePolish(tokens));
    }

    /**
     * Substitutes all the variables on the tokens list with the defined values
     *
     * @param tokens The tokens to sub variables
     * @return The list of tokens with the variables substituted
     */
    public static ArrayList<Token> subVariables(ArrayList<Token> tokens) {
        ArrayList<Token> newTokens = new ArrayList<>();
        for (Token token : tokens) {
            if (token instanceof VariableToken && token.getType() != VariableToken.S && token.getType() != VariableToken.T) {
                int index = tokens.indexOf(token);
                VariableToken v = (VariableToken) token;
                ArrayList<Token> val = v.getValue();
                val = condenseDigits(val);
                if (val.isEmpty()) {
                    val.add(new NumberToken(0));
                }
                newTokens.addAll(val);
            } else {
                newTokens.add(token);
            }
        }
        return newTokens;
    }

    /**
     * Reduces the value such that there is no common denom between the values (if they
     * are all integer doubles).
     *
     * @param dvalues The values to reduce
     * @return The reduced values
     */
    public static double[] reduce(double[] dvalues) {
        int[] values = new int[dvalues.length];
        int i = 0;
        while (i < values.length && dvalues[i] % 1 == 0d) {
            values[i] = (int) dvalues[i];
            i++;
        }
        if (i == values.length) {
            int gcd = Utility.gcd(values);
            for (int j = 0; j < dvalues.length; j++) {
                dvalues[j] /= gcd;
            }
        }
        return dvalues;
    }

    /**
     * Returns the gcd of all the values
     *
     * @param values The values to find the gcd
     * @return The gcd
     */
    public static int gcd(int[] values) {
        //Finds min value
        int min = Integer.MAX_VALUE;
        for (int value : values) {
            value = Math.abs(value);
            if (value < min && value != 0) {
                min = value;
            }
        }
        boolean finished = false;
        int i = min;
        while (!finished && i > 0) {
            finished = true;
            for (int value : values) {
                if (value % i != 0) {
                    finished = false;
                }
            }
            i--;
        }
        return i + 1;
    }

    /**
     * Substitutes all the variables on the tokens list with the defined values
     *
     * @param tokens    The tokens to sub variables
     * @param constants If constants shall be subbed in too
     * @return The list of tokens with the variables substituted
     */
    public static ArrayList<Token> subVariables(ArrayList<Token> tokens, boolean constants) {
        ArrayList<Token> newTokens = new ArrayList<>();
        for (Token token : tokens) {
            if (token instanceof VariableToken && ((token.getType() != VariableToken.PI && token.getType() != VariableToken.E && token.getType() != VariableToken.CONSTANT) || constants)) {
                int index = tokens.indexOf(token);
                VariableToken v = (VariableToken) token;
                ArrayList<Token> val = v.getValue();
                val = condenseDigits(val);
                if (val.isEmpty()) {
                    val.add(new NumberToken(0));
                }
                newTokens.addAll(v.getValue());
            } else {
                newTokens.add(token);
            }
        }
        return newTokens;
    }
}
