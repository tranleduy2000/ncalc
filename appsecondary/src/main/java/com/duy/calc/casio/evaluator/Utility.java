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

package com.duy.calc.casio.evaluator;

import com.duy.calc.casio.exception.NumberTooLargeException;
import com.duy.calc.casio.token.BracketToken;
import com.duy.calc.casio.token.ConstantToken;
import com.duy.calc.casio.token.DigitToken;
import com.duy.calc.casio.token.FunctionToken;
import com.duy.calc.casio.token.NumberToken;
import com.duy.calc.casio.token.OperatorToken;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.token.VariableToken;
import com.duy.calc.casio.token.VectorToken;
import com.duy.calc.casio.token.factory.BracketFactory;
import com.duy.calc.casio.token.factory.OperatorFactory;
import com.duy.calc.casio.tokenizer.MachineExpressionConverter;
import com.duy.calc.casio.util.DLog;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import static com.duy.calc.casio.evaluator.EvaluateConfig.DECIMAL;
import static com.duy.calc.casio.tokenizer.MachineExpressionConverter.convertStringToTokens;

/**
 * Contains miscellaneous static methods that provide utility.
 *
 * @author Alston Lin, Ejaaz Merali
 * @version 3.0
 */
public class Utility {

    public static final Pattern ACCEPT_FRACTION = Pattern.compile("(sin|cos|tan|sinh|cosh|tanh|" +
                    "arcsin|arccos|arctan|" +
                    "arcsinh|arccosh|arctanh|log|ln|gcd|lcm|pol|rec|int|intg|randomint|randomreal)",
            Pattern.CASE_INSENSITIVE);
    public static final Pattern ACCEPT_DECIMAL = Pattern.compile("(sin|cos|tan|sinh|cosh|tanh|" +
            "arcsin|arccos|arctan|" +
            "arcsinh|arccosh|arctanh|log|ln|gcd|lcm|pol|rec|" +
            "int|intg|randomint|randomreal|sqrt|cbrt)", Pattern.CASE_INSENSITIVE);

    private static final String TAG = "Utility";


    /**
     * Transforms all the digits into numbers as well as replacing Variables with numbers.
     *
     * @param tokens The expression to condense digits
     * @return The expression with the digits condensed
     */
    public static ArrayList<Token> condenseDigits(List<Token> tokens) {
        ArrayList<Token> newTokens = new ArrayList<>();
        ArrayList<DigitToken> digits = new ArrayList<>();
        boolean atDigits = false; //Tracks if it's currently tracking digits
        for (Token token : tokens) {
            if (atDigits) { //Going through digits
                if (token instanceof DigitToken) { //Number keeps going
                    digits.add((DigitToken) token);
                } else { //Number ended
                    atDigits = false;

                    NumberToken num = new NumberToken(MachineExpressionConverter.digitsToNumber(digits));
                    digits.clear();
                    newTokens.add(num); //Adds the sum of all the digits

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
            newTokens.add(new NumberToken(MachineExpressionConverter.digitsToNumber(digits)));
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
    public static ArrayList<Token> setupExpression(List<Token> toSetup) {
        ArrayList<Token> newExpression = new ArrayList<>();
        for (Token token : toSetup) {
            boolean negative = false;
            Token last = newExpression.isEmpty() ? null : newExpression.get(newExpression.size() - 1); //Last token in the new expression
            Token beforeLast;
            if (newExpression.size() > 1) {
                beforeLast = newExpression.get(newExpression.size() - 2);
            } else {
                beforeLast = null;
            }
            boolean lastIsSubtract = last instanceof OperatorToken && last.getType() == OperatorToken.SUB;
            boolean beforeLastIsOperator = beforeLast != null && beforeLast instanceof OperatorToken;
            boolean beforeLastIsOpenBracket = (beforeLast instanceof BracketToken) && ((BracketToken) beforeLast).isOpen();

            if (token instanceof BracketToken) {
                BracketToken bracket = (BracketToken) token;
                if ((bracket.getType() == BracketToken.PARENTHESES_OPEN)
                        && (last instanceof BracketToken) &&
                        ((last.getType() == BracketToken.PARENTHESES_CLOSE)
                                || (last.getType() == BracketToken.SUPERSCRIPT_CLOSE)
                                || (last.getType() == BracketToken.DENOMINATOR_CLOSE))) { //Ex. (2 + 1)(3 + 4), (2)/(5)(x + 1) or x^(2)(x+1)
                    //Implies multiplication between the two expressions in the brackets
                    newExpression.add(OperatorFactory.makeMultiply());
                } else if (((last instanceof NumberToken) || (last instanceof VariableToken))
                        && ((bracket.getType() == BracketToken.PARENTHESES_OPEN)
                        || (bracket.getType() == BracketToken.FRACTION_OPEN))) { //Ex. 3(2 + 1) or X(1+X) or 2(1/2)

                    newExpression.add(OperatorFactory.makeMultiply());
                } else if ((last instanceof OperatorToken)
                        && (last.getType() == OperatorToken.SUB) && beforeLastIsOperator) { //Ex. e + -(X + 1) -> e + -1 * (X + 1)
                    newExpression.remove(last);
                    newExpression.add(new NumberToken(-1));
                    newExpression.add(OperatorFactory.makeMultiply());
                }
            } else if (token instanceof NumberToken
                    || token instanceof VariableToken
                    || token instanceof FunctionToken) { //So it works with Function buttonMode too

                if (last instanceof NumberToken) { //Ex. 5A , 5f(x)

                    newExpression.add(OperatorFactory.makeMultiply());

                } else if (last instanceof BracketToken
                        && (last.getType() == BracketToken.PARENTHESES_CLOSE
                        || last.getType() == BracketToken.SUPERSCRIPT_CLOSE
                        || last.getType() == BracketToken.DENOMINATOR_CLOSE)) { //Ex. x^2(x + 1) or 2/5x

                    newExpression.add(OperatorFactory.makeMultiply());

                } else if (lastIsSubtract &&
                        (beforeLastIsOperator || beforeLastIsOpenBracket || newExpression.size() <= 1)) { //Ex. e * -X -> e * -1 * X

                    newExpression.remove(last);

                    if (token instanceof NumberToken) {
                        negative = true;
                    } else {
                        newExpression.add(new NumberToken(-1));
                        newExpression.add(OperatorFactory.makeMultiply());
                    }

                } else if (token instanceof FunctionToken
                        && (last instanceof FunctionToken || last instanceof VariableToken)) { //Ex. f(x)g(x) or (1 + 2)f(x) or xf(x)

                    newExpression.add(OperatorFactory.makeMultiply());
                }

                if (token instanceof VariableToken && last instanceof VariableToken) { //Ex. pi x
                    newExpression.add(OperatorFactory.makeMultiply());
                }
            }
            if (negative) {
                newExpression.add(new NumberToken(((NumberToken) token).getDoubleValue() * -1));
            } else {
                newExpression.add(token);
            }
        }
        return newExpression;
    }

    /**
     * Looks for and multiplies constants next to each other in the given expression.
     * i.e. AB -> A * B
     *
     * @param toSetup The expression to multiply constants
     * @return The expression with the multiplication expression added
     */
    public static ArrayList<Token> multiplyConstants(List<Token> toSetup) {
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
     * @throws java.lang.IllegalArgumentException The infix notation is invalid
     */
    public static ArrayList<Token> convertToRPN(List<Token> infix) {

        ArrayList<Token> consumer = new ArrayList<>();
        Stack<Token> stack = new Stack<>();

        for (Token token : infix) {
            if (token instanceof NumberToken || token instanceof ConstantToken || token instanceof VariableToken) {
                //Adds directly to the queue if it's a token
                consumer.add(token);

            } else if (token instanceof FunctionToken) { //Adds to the stack if it's a function
                stack.push(token);
            } else if (token instanceof BracketToken) {
                BracketToken bracket = (BracketToken) token;
                if (bracket.isOpen()) { //Pushes the bracket to the stack if it's open
                    stack.push(bracket);
                } else {
                    //For close brackets, pop operators onto the list until a open bracket is found
                    Token top = stack.lastElement();
                    while (!(top instanceof BracketToken)) { //While it has not found an open bracket

                        consumer.add(stack.pop()); //Pops the top element
                        if (stack.isEmpty()) { //Mismatched brackets
                            throw new IllegalArgumentException();
                        }

                        top = stack.lastElement();
                    }

                    stack.pop(); //Removes the bracket
                }
            } else if (token instanceof OperatorToken) {
                if (!stack.empty()) {
                    //Make sure it's not empty to prevent bugs
                    Token top = stack.peek(); //top of stack

                    while ((top != null)
                            && (((top instanceof OperatorToken) && (
                            token.getAssociative() == Token.LEFT) && (top.getPrecedence() >= token.getPrecedence()))
                            || (top instanceof FunctionToken))) { //Operator is left associative and higher precedence / is a function
                        consumer.add(stack.pop()); //Pops top element to the queue

                        if (stack.isEmpty()) break;
                        top = stack.peek();
                    }
                }
                stack.push(token);
            }
        }
        //All expression read at this point
        while (!stack.isEmpty()) { //Puts the remaining expression in the stack to the queue
            consumer.add(stack.pop());
        }
        System.out.println(consumer);
        return consumer;
    }

    /**
     * Evaluates a given expression in reverse polish notation and returns the resulting value.
     *
     * @param tokens The expression in reverse polish
     * @return The value of the expression
     * @throws java.lang.IllegalArgumentException The user has inputted an invalid expression
     */
    public static double evaluateRPN(ArrayList<Token> tokens) {

        Stack<NumberToken> stack = new Stack<>();
        for (Token token : tokens) {
            if (token instanceof NumberToken) {
                //Adds all numbers directly to the stack
                stack.push((NumberToken) token);

            } else if (token instanceof OperatorToken) {
                //Operates the first and second top operators
                NumberToken right = stack.pop();
                NumberToken left = stack.pop();

                NumberToken value = new NumberToken(((OperatorToken) token)
                        .operate(left.getDoubleValue(), right.getDoubleValue()));

                //Adds the result back to the stack
                stack.push(value);
            } else if (token instanceof FunctionToken) {
                //Function performs on the first number
                NumberToken top = stack.pop();

                //Adds the result back to the stack
                NumberToken result = new NumberToken(((FunctionToken) token).perform(top.getDoubleValue()));
                stack.push(result);
            } else { //This should never be reached
                throw new IllegalArgumentException();
            }
        }
        if (stack.size() == 0) {
            throw new IllegalArgumentException("Input is empty");
        } else if (stack.size() != 1) {
            throw new IllegalArgumentException("Illegal Expression"); //There should only be 1 token left on the stack
        } else {
            return stack.pop().getDoubleValue();
        }
    }

    public static ArrayList<Token> convertDoublesToVector(double[] vector) {
        ArrayList<Token> newVector = new ArrayList<>();
        newVector.add(BracketFactory.makeOpenSquareBracket());
        newVector.add(new NumberToken(vector[0]));
        newVector.add(new Token(",") {

        });
        newVector.add(new NumberToken(vector[1]));
        if (vector.length > 2) {
            newVector.add(new Token(",") {
            });
            newVector.add(new NumberToken(vector[2]));
        }
        newVector.add(BracketFactory.makeCloseSquareBracket());
        return newVector;
    }


    /**
     * Gets the expression of all stored vectors from variables and creates a new expression for calculations
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
    public static ArrayList<Token> cleanupExpressionForReading(List<Token> expression) {
        ArrayList<Token> newExpression = new ArrayList<>();
        for (int i = 0; i < expression.size(); i++) {
            Token t = expression.get(i);
            Token last;
            if (newExpression.size() > 0) {
                last = newExpression.get(newExpression.size() - 1);
            } else {
                last = null;
            }
            //Token beforeLast = newExpression.size() > 1 ? newExpression.get(newExpression.size() - 2) : null;
            if (t instanceof VariableToken
                    && last instanceof OperatorToken && last.getType() == OperatorToken.MUL) { // e * V -> EV
                newExpression.remove(last);
            } else if (t instanceof BracketToken && t.getType() == BracketToken.PARENTHESES_OPEN
                    && last instanceof OperatorToken && last.getType() == OperatorToken.MUL) { //e * (e) -> e(e)
                newExpression.remove(last);
            } else if (t instanceof FunctionToken && last instanceof OperatorToken
                    && last.getType() == OperatorToken.MUL) { //e * F -> EF
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
     * @param function The function to doCalculate
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
                String result = Utility.evaluate(value);
                expression.add(new NumberToken(result));
            } else {
                expression.add(token);
            }
        }
        try {
            return Utility.evaluateRPN(Utility.convertToRPN(expression));
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
            output.add(new Token("e") {
            });
        }
        if (quadrant == -2) { //positive x axis
            output.add(new Token("e") {
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
            output.add(new Token("e") {
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
            output.add(new Token("e") {
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
                if (b.getType() == BracketToken.PARENTHESES_OPEN) {
                    bracketCount++;
                } else if (b.getType() == BracketToken.PARENTHESES_CLOSE) {
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
     * @param tokens The expression to doCalculate
     * @return The numerical value of the expression+++---9+8
     * @throws IllegalArgumentException If the user has input a invalid expression
     */
    public static String evaluate(ArrayList<Token> tokens) {
        try {
            tokens = addMissingBrackets(tokens); //ok
            tokens = subVariables(tokens);
            tokens = setupExpression(tokens);
            DLog.d(TAG, "setupExpression: " + tokens);

            String inputExpression = MachineExpressionConverter.convertTokensToString(tokens);
            DLog.d(TAG, "input = " + inputExpression);
            ExprEvaluator exprEvaluator = new ExprEvaluator();
            IExpr result = exprEvaluator.evaluate(inputExpression);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Processes the expression and returns the result using the Shunting Yard Algorithm to convert
     * the expression into reverse polish and then evaluating it.
     *
     * @param tokens The expression to doCalculate
     * @return The numerical value of the expression+++---9+8
     * @throws IllegalArgumentException If the user has input a invalid expression
     */
    public static ArrayList<Token> evaluateSimple(ArrayList<Token> tokens, int fraction) {
        try {
            tokens = addMissingBrackets(tokens); //ok
            tokens = subVariables(tokens);
            tokens = setupExpression(tokens);
            DLog.d(TAG, "setupExpression: " + tokens);

            String inputExpression = MachineExpressionConverter.convertTokensToString(tokens);
            if (fraction == DECIMAL) {
                inputExpression = "N(" + inputExpression + ")";
            }
            DLog.d(TAG, "input = " + inputExpression);
            IExpr result = MathEvaluator.getInstance().evaluate(inputExpression);
            return convertStringToTokens(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public static boolean isAcceptResultFraction(IExpr expr) {

        return !ACCEPT_FRACTION.matcher(expr.toString()).find();
    }

    public static boolean isAcceptResultDecimal(IExpr expr) {
        return !ACCEPT_DECIMAL.matcher(expr.toString()).find();
    }

    /**
     * Substitutes all the variables on the expression list with the defined values
     *
     * @param tokens The expression to sub variables
     * @return The list of expression with the variables substituted
     */
    public static ArrayList<Token> subVariables(ArrayList<Token> tokens) {
        ArrayList<Token> newTokens = new ArrayList<>();
        for (Token token : tokens) {
            if (token instanceof VariableToken
                    && token.getType() != VariableToken.S && token.getType() != VariableToken.T
                    && token.getType() != VariableToken.PI && token.getType() != VariableToken.e) {
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
     * Substitutes all the variables on the expression list with the defined values
     *
     * @param tokens    The expression to sub variables
     * @param constants If constants shall be subbed in too
     * @return The list of expression with the variables substituted
     */
    public static ArrayList<Token> subVariables(ArrayList<Token> tokens, boolean constants) {
        ArrayList<Token> newTokens = new ArrayList<>();
        for (Token token : tokens) {
            if (token instanceof VariableToken &&
                    ((token.getType() != VariableToken.PI
                            && token.getType() != VariableToken.e
                            && token.getType() != VariableToken.CONSTANT) || constants)) {
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

    /**
     * Finds the sum of the given array of widths
     *
     * @param widths The array to sum
     * @return The sum
     */
    private float sum(float[] widths) {
        float widthSum = 0;
        for (float width : widths) {
            widthSum += width;
        }
        return widthSum;
    }
}
