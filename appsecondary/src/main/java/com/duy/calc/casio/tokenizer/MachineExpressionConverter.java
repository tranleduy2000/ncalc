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

import android.support.annotation.NonNull;

import com.duy.calc.casio.evaluator.EvaluateConfig;
import com.duy.calc.casio.token.BracketToken;
import com.duy.calc.casio.token.CommaToken;
import com.duy.calc.casio.token.ConstantToken;
import com.duy.calc.casio.token.DegreeToken;
import com.duy.calc.casio.token.DigitToken;
import com.duy.calc.casio.token.FunctionToken;
import com.duy.calc.casio.token.NumberToken;
import com.duy.calc.casio.token.OperatorToken;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.token.VariableToken;
import com.duy.calc.casio.token.factory.BracketFactory;
import com.duy.calc.casio.token.factory.FunctionFactory;
import com.duy.calc.casio.token.factory.OperatorFactory;
import com.duy.calc.casio.token.factory.PlaceholderFactory;
import com.duy.calc.casio.token.factory.VariableFactory;
import com.duy.calc.casio.util.DLog;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duy on 23-Jun-17.
 */

public class MachineExpressionConverter {
    private static final String TAG = "MachineExpressionGenera";

    /**
     * Converts the String into an expression tree to use the
     *
     * @param str The expression as a String
     * @return The expression tree
     */
    public static ArrayList<Token> convertStringToTokens(String str) {
        DLog.d(TAG, "convertStringToTokens() called with: str = [" + str + "]");

        ArrayList<Token> tokens = new ArrayList<>();
        int charIndex = 0;
        String temp;
        while (charIndex < str.length()) {
            char c = str.charAt(charIndex);
            temp = "";
            if (Character.isDigit(c)) {
                while (charIndex < str.length() &&
                        (Character.isDigit(str.charAt(charIndex)) || str.charAt(charIndex) == '.')) {
                    temp += str.charAt(charIndex);
                    charIndex++;
                }
                tokens.add(new NumberToken(temp));
                continue;
            }
            while (charIndex < str.length() && Character.isLetter(str.charAt(charIndex))) {
                temp += str.charAt(charIndex);
                charIndex++;
            }
            switch (temp) {
                case "pi":
                case "Pi":  //IDK, it changes from caps to non-caps
                    tokens.add(VariableFactory.makePI());
                    continue;
                case "x":
                    tokens.add(VariableFactory.makeX());
                    continue;
                case "e":
                    tokens.add(VariableFactory.makeExponent());
                    continue;
                case "Sinh":
                    tokens.add(FunctionFactory.makeSinh());
                    continue;
                case "Cosh":
                    tokens.add(FunctionFactory.makeCosh());
                    continue;
                case "Tanh":
                    tokens.add(FunctionFactory.makeCosh());
                    continue;
                case "Sqrt":
                    tokens.add(FunctionFactory.makeSqrt());
                    //the character at charindex is '('
                    int endIndex = StringUtil.getGroupBracketFromStart(str, charIndex);
                    tokens.add(BracketFactory.makeSqrtOpen());
                    tokens.addAll(convertStringToTokens(str.substring(charIndex + 1, endIndex)));
                    tokens.add(BracketFactory.makeSqrtClose());
                    charIndex = endIndex + 1;
                    continue;
                case "Sin":
                    tokens.add(FunctionFactory.makeSin());
                    continue;
                case "Cos":
                    tokens.add(FunctionFactory.makeCos());
                    continue;
                case "Tan":
                    tokens.add(FunctionFactory.makeTan());
                    continue;
                case "Csc":
                    tokens.add(FunctionFactory.makeCsc());
                    continue;
                case "Sec":
                    tokens.add(FunctionFactory.makeSec());
                    continue;
                case "Cot":
                    tokens.add(FunctionFactory.makeCot());
                    continue;
                case "Erf":
                    tokens.add(FunctionFactory.makeErf());
                    continue;
                case "Erfi":
                    tokens.add(FunctionFactory.makeErfi());
                    continue;
                case "Gamma":
                    tokens.add(FunctionFactory.makeGamma());
                    continue;
                case "ArcSin":
                    tokens.add(FunctionFactory.makeASin());
                    continue;
                case "ArcCos":
                    tokens.add(FunctionFactory.makeACos());
                    continue;
                case "ArcTan":
                    tokens.add(FunctionFactory.makeATan());
                    continue;
                case "ArcSinh":
                    tokens.add(FunctionFactory.makeASin());
                    continue;
                case "ArcCosh":
                    tokens.add(FunctionFactory.makeACos());
                    continue;
                case "ArcTanh":
                    tokens.add(FunctionFactory.makeATan());
                    continue;
                case "Log":
                    tokens.add(FunctionFactory.makeLog_10());
                    continue;
                case "Ln":
                    tokens.add(FunctionFactory.makeLn());
                    continue;
                case "LCM":
                    tokens.add(FunctionFactory.makeLCM());
                    continue;
                case "GCD":
                    tokens.add(FunctionFactory.makeGCD());
                    continue;
                case "Pol":
                    tokens.add(FunctionFactory.makePol());
                    continue;
            }

            //FILTERS OPERATORS
            switch (c) {
                case '+':
                    tokens.add(OperatorFactory.makeAdd());
                    break;
                case '-':
                    tokens.add(OperatorFactory.makeSubtract());
                    break;
                case '*':
                    tokens.add(OperatorFactory.makeMultiply());
                    break;
                case '/':
                    tokens.add(OperatorFactory.makeDivide());
                    break;
                case '^':
                    tokens.add(OperatorFactory.makePower());
                    if (charIndex + 1 < str.length()) {
                        if (str.charAt(charIndex + 1) == '(') {
                            int end = StringUtil.getGroupBracketFromStart(str, charIndex + 1);
                            tokens.add(BracketFactory.makeSuperscriptOpen());
                            tokens.addAll(convertStringToTokens(str.substring(charIndex + 2, end)));
                            tokens.add(BracketFactory.makeSuperscriptClose());
                            charIndex = end + 1;
                            continue;
                        }
                    }
                    break;
                case '!':
                    tokens.add(OperatorFactory.makeFactorial());
                    break;
                case '%':
                    tokens.add(OperatorFactory.makePercent());
                    break;
                case ',':
                    tokens.add(PlaceholderFactory.makeComma());
                    break;
                case '(':
                case '[':
                    tokens.add(BracketFactory.makeOpenParentheses());
                    break;
                case ')':
                case ']':
                    tokens.add(BracketFactory.makeCloseParentheses());
                    break;
                case '\\':

                    break;
                default:
                    throw new UnsupportedOperationException(temp);
            }
            charIndex++;
        }
        //Handles digits if it is the last token
        return tokens;
    }

    public static String convertTokensToString(@NonNull List<Token> tokens) {
        return convertTokensToString(tokens, new EvaluateConfig());
    }

    public static String convertTokensToString(@NonNull List<Token> tokens,
                                               @NonNull EvaluateConfig config) {


        StringBuilder expr = new StringBuilder();
        int i = 0;
        while (i < tokens.size()) {
            Token token = tokens.get(i);
            if (token instanceof NumberToken) {
                expr.append((((NumberToken) token).getValue()));
            } else if (token instanceof OperatorToken) {
                switch (token.getType()) {
                    case OperatorToken.DIV:
                        expr.append("/");
                        break;
                    case OperatorToken.ADD:
                        expr.append("+");
                        break;
                    case OperatorToken.FRACTION:
                        expr.append("/");
                        break;
                    case OperatorToken.MUL:
                        expr.append("*");
                        break;
                    case OperatorToken.POWER:
                        expr.append("^");
                        break;
                    case OperatorToken.SUB:
                        expr.append("-");
                        break;
                    case OperatorToken.FACTORIAL:
                        expr.append("!");
                        break;
                    case OperatorToken.PERCENT:
                        expr.append("/").append("100");
                        break;
                    case OperatorToken.SURD:
                        i = processSurd(tokens, expr, i);
                        break;
                    case OperatorToken.COMBINATION:
                        i = processCombination(tokens, expr, i);
                        break;
                    case OperatorToken.PERMUTATION:
                        i = processPermutation(tokens, expr, i);
                        break;
                    case OperatorToken.QUOTIENT:
                        // TODO: 26-Jun-17
                        break;
                    case OperatorToken.EQUAL:
                        expr.append("==");
                    default:
                        expr.append(token.getSymbol());
                }
            } else if (token instanceof VariableToken) {
                if (token instanceof ConstantToken) {
                    switch (token.getType()) {
                        case ConstantToken.PI:
                            expr.append("Pi");
                            break;
                        case ConstantToken.E:
                            expr.append("E");
                            break;
                        default:
                            expr.append(token.getSymbol());
                            break;
                    }
                } else {
                    switch (token.getType()) {
                        case VariableToken.PI:
                            expr.append("Pi");
                            break;
                        case VariableToken.X:
                            expr.append("x");
                            break;
                        case VariableToken.E:
                            expr.append("e");
                            break;
                        default:
                            expr.append(token.getSymbol());
                            break;
                    }
                }
            } else if (token instanceof DegreeToken) {
                expr.append("(degree)");
            } else if (token instanceof CommaToken) {
                expr.append(",");
            } else if (token instanceof FunctionToken) {
                switch (token.getType()) {
                    case FunctionToken.SIN:
                        i = processTrig(tokens, i, expr, config, "Sin", FunctionToken.SIN);
                        break;
                    case FunctionToken.COS:
                        i = processTrig(tokens, i, expr, config, "Cos", FunctionToken.COS);
                        break;
                    case FunctionToken.TAN:
                        i = processTrig(tokens, i, expr, config, "Tan", FunctionToken.TAN);
                        break;
                    case FunctionToken.ARCSIN:
                        i = processTrigReverser(tokens, i, expr, config, "ArcSin", FunctionToken.ARCSIN);
                        break;
                    case FunctionToken.ARCCOS:
                        i = processTrigReverser(tokens, i, expr, config, "ArcCos", FunctionToken.ARCCOS);
                        break;
                    case FunctionToken.ARCTAN:
                        i = processTrigReverser(tokens, i, expr, config, "ArcTan", FunctionToken.ARCTAN);
                        break;
                    case FunctionToken.SINH:
                        i = processTrig(tokens, i, expr, config, "Sinh", FunctionToken.SINH);
                        break;
                    case FunctionToken.COSH:
                        i = processTrig(tokens, i, expr, config, "Cosh", FunctionToken.COSH);
                        break;
                    case FunctionToken.TANH:
                        i = processTrig(tokens, i, expr, config, "Tanh", FunctionToken.TANH);
                        break;
                    case FunctionToken.ARCSINH:
                        i = processTrigReverser(tokens, i, expr, config, "ArcSinh", FunctionToken.ARCSINH);
                        break;
                    case FunctionToken.ARCCOSH:
                        i = processTrigReverser(tokens, i, expr, config, "ArcCosh", FunctionToken.ARCCOSH);
                        break;
                    case FunctionToken.ARCTANH:
                        i = processTrigReverser(tokens, i, expr, config, "ArcTanh", FunctionToken.ARCTANH);
                        break;
                    case FunctionToken.SQRT:
                        expr.append("Sqrt");
                        break;
                    case FunctionToken.LN:
                        expr.append("Ln");
                        break;
                    case FunctionToken.APPELLF1:
                        expr.append("AppellF1");
                        break;
                    case FunctionToken.ABS:
                        expr.append("Abs");
                        break;
                    case FunctionToken.GCD:
                        expr.append("GCD");
                        break;
                    case FunctionToken.LCM:
                        expr.append("LCM");
                        break;
                    case FunctionToken.ERF:
                        expr.append("Erf");
                        break;
                    case FunctionToken.LOG10:
                        expr.append("Log");
                        break;
                    case FunctionToken.LOG_N:
                        i = processLogN(tokens, i, expr);
                        break;
                    case FunctionToken.RANDOM_INT:
                        expr.append("RandomInt");
                        break;
                    case FunctionToken.RANDOM_REAL:
                        expr.append("RandomReal()");
                        break;
                    case FunctionToken.INT:
                        expr.append("IntegerPart");
                        break;
                    case FunctionToken.INTG:
                        expr.append("Floor");
                        break;
                    case FunctionToken.DERIVATIVE:
                        i = processDerivative(tokens, i, expr);
                        // TODO: 26-Jun-17 improve
                        break;
                    case FunctionToken.POL:
                        expr.append("Pol");
                        break;
                    case FunctionToken.REC:
                        expr.append("Rec");
                        break;
                    default:
                        throw new IllegalArgumentException("NOT SUPPORTED YET: " + expr);
                        //TODO: IMPLEMENT OTHERS
                }
            } else if (token instanceof BracketToken) {
                if (((BracketToken) token).isOpen()) {
                    expr.append("(");
                } else {
                    expr.append(")");
                }
            } else {
                expr.append(token.getSymbol());
            }
            i++;
        }
        DLog.d(TAG, "convertTokensToString() " + tokens + " returned: " + expr);
        return expr.toString();
    }

    private static int processTrig(List<Token> tokens, int i, StringBuilder expr, EvaluateConfig config,
                                   String functionName, int functionType) {
        checkParams(tokens.get(i), FunctionFactory.class, FunctionToken.class, functionType);
        checkParams(tokens.get(i + 1), BracketFactory.class, BracketToken.class, BracketToken.PARENTHESES_OPEN);
        ArrayList<Token> group = TokenUtil.getGroupFromStart(tokens, i + 1,
                BracketToken.PARENTHESES_OPEN, BracketToken.PARENTHESES_CLOSE);
        if (config.getTrigMode() == EvaluateConfig.RADIAN) {
            expr.append(functionName).append("(").append(convertTokensToString(group, config)).append(")");
        } else if (config.getTrigMode() == EvaluateConfig.DEGREE) {
            expr.append(functionName)
                    .append("(").append("(")
                    .append(convertTokensToString(group, config))
                    .append(")").append("degree").append(")");
        } else if (config.getTrigMode() == EvaluateConfig.GRADIAN) {
            expr.append("(").append(functionName).append("(").append(convertTokensToString(group, config))
                    .append(")").append("*63.66").append(")");

        }
        return i + group.size();
    }

    private static int processTrigReverser(List<Token> tokens, int i, StringBuilder expr, EvaluateConfig config,
                                           String name, int type) {
        checkParams(tokens.get(i), FunctionFactory.class, FunctionToken.class, type);
        checkParams(tokens.get(i + 1), BracketFactory.class, BracketToken.class, BracketToken.PARENTHESES_OPEN);
        ArrayList<Token> group = TokenUtil.getGroupFromStart(tokens, i + 1,
                BracketToken.PARENTHESES_OPEN, BracketToken.PARENTHESES_CLOSE);
        if (config.getTrigMode() == EvaluateConfig.RADIAN) {
            expr.append(name).append("(").append(convertTokensToString(group, config)).append(")");

        } else if (config.getTrigMode() == EvaluateConfig.DEGREE) {
            expr.append("((").append(name)
                    .append("(")
                    .append(convertTokensToString(group, config))
                    .append(")").append(")*180/Pi)");
        }
        return i + group.size();
    }

    private static void checkParams(@NonNull Token token, @NonNull Class<?> factory,
                                    @NonNull Class<? extends Token> clazz, int type) {
        String name = token.getClass().getName();
        String factoryName = factory.getName();
        if (token.getType() != type) {
            throw new InvalidParameterException("Unexpected type, " + type + " expected");
        }
        if (token.getClass().getName().contains("$")) { //anonymous class
            if (!name.startsWith(factoryName) || (token.getType() != type)) {
                throw new InvalidParameterException("Unexpected token, " + factory.getSimpleName()
                        + " expected but " + token + " found ");
            }
        } else {
            if (!token.getClass().getName().equals(clazz.getName())) {
                throw new InvalidParameterException("Unexpected token, " + factory.getSimpleName()
                        + " expected but " + token + " found ");
            }
        }
    }

    private static int processDerivative(List<Token> tokens, int i, StringBuilder toAppend) {
        Token t = tokens.get(i);
        if (!(t instanceof FunctionToken && t.getType() == FunctionToken.DERIVATIVE)) {
            throw new InvalidParameterException("the index not given derivative function");
        }
        //next token is open derivative
        i++;
        ArrayList<Token> expr = TokenUtil.getGroupFromStart(tokens, i,
                BracketToken.DERIVATIVE_OPEN, BracketToken.DERIVATIVE_CLOSE);
        i += expr.size(); //the token at i is open subscript
        ArrayList<Token> x = TokenUtil.getGroupFromStart(tokens, i,
                BracketToken.SUBSCRIPT_OPEN, BracketToken.SUBSCRIPT_CLOSE);
        i += x.size();
        return i;
    }

    private static int processLogN(List<Token> tokens, int i, StringBuilder toAppend) {
        if (!(tokens.get(i) instanceof FunctionToken && tokens.get(i).getType() == FunctionToken.LOG_N)) {
            throw new UnsupportedOperationException("process log n");
        }
        i++; //subscript open
        ArrayList<Token> base = TokenUtil.getGroupFromStart(tokens, i, BracketToken.SUBSCRIPT_OPEN,
                BracketToken.SUBSCRIPT_CLOSE);
        i += base.size();
        ArrayList<Token> expr = TokenUtil.getGroupFromStart(tokens, i, BracketToken.LOGN_OPEN,
                BracketToken.LOGN_CLOSE);
        i += expr.size();
        toAppend.append("Log(").append(convertTokensToString(base)).append(",")
                .append(convertTokensToString(expr)).append(")");
        return i;
    }

    private static int processPermutation(List<Token> tokens, StringBuilder expr, int i) {
        // TODO: 24-Jun-17
        return i;
    }

    private static int processCombination(List<Token> tokens, StringBuilder expr, int i) {
        if (tokens.get(i - 1) instanceof BracketToken) {
            StringBuilder left = new StringBuilder();
            //Finds the root
            int bracketCount = 1;
            int j = expr.length() - 2; //Starts at the character BEFORE the ")"
            expr.delete(expr.length() - 1, expr.length());
            while (bracketCount > 0) {
                char c = expr.charAt(j);
                if (c == ')') {
                    bracketCount++;
                } else if (c == '(') {
                    bracketCount--;
                }
                expr.delete(j, j + 1); //Removes the last character
                left.insert(0, c); //Adds at the beginning of the root
                j--;
            }
            //Removes the opening (
            left.delete(0, 1);

            //Now adds the roots to the end of the Surd
            ArrayList<Token> right = new ArrayList<>();
            bracketCount = 1;
            i = i + 2; //Starts after the ( bracket
            while (bracketCount > 0) {
                Token t = tokens.get(i);
                if (t instanceof BracketToken && t.getType() == BracketToken.PARENTHESES_OPEN) {
                    bracketCount++;
                } else if (t instanceof BracketToken && t.getType() == BracketToken.PARENTHESES_CLOSE) {
                    bracketCount--;
                }
                right.add(t);
                i++;
            }
            i--; //Reverses the last iteration
            right.remove(right.size() - 1); //Takes out the closing )
            expr.append("Combination(");
            expr.append(convertTokensToString(right));
            expr.append(",").append(left).append(")");
            return i;
        } else {

        }
        // TODO: 24-Jun-17
        return i;
    }

    private static int processSurd(List<Token> tokens, StringBuilder expr, int i) {
        String base = "";
        //Finds the root
        int bracketCount = 1;
        int j = expr.length() - 2; //Starts at the character BEFORE the ")"
        expr.delete(expr.length() - 1, expr.length());
        while (bracketCount > 0) {
            char c = expr.charAt(j);
            if (c == ')') {
                bracketCount++;
            } else if (c == '(') {
                bracketCount--;
            }
            expr.delete(j, j + 1); //Removes the last character
            base = c + base; //Adds at the beginning of the root
            j--;
        }
        //Removes the opening (
        base = base.substring(1);

        //Now adds the roots to the end of the Surd
        ArrayList<Token> root = new ArrayList<>();
        bracketCount = 1;
        j = i + 2; //Starts after the ( bracket
        while (bracketCount > 0 && j < tokens.size()) {
            Token t = tokens.get(j);
            if (t instanceof BracketToken && t.getType() == BracketToken.PARENTHESES_OPEN) {
                bracketCount++;
            } else if (t instanceof BracketToken && t.getType() == BracketToken.PARENTHESES_CLOSE) {
                bracketCount--;
            }
            root.add(t);
            j++;
        }

        root.remove(root.size() - 1); //Takes out the closing )
        expr.append("Surd(");
        expr.append(convertTokensToString(root));
        expr.append(",").append(base).append(")");
        return j - 1;
    }


    /**
     * Prints the expression to be read by humans.
     *
     * @param expression The expression to print
     * @return The string representation of the expression
     */
    public static String printExpression(List<Token> expression) {
        StringBuilder s = new StringBuilder();
        for (Token t : expression) {
            if (t instanceof OperatorToken && t.getType() == OperatorToken.FRACTION) {
                s.append("/");
            } else {
                s.append(t.getSymbol());
            }
        }
        return s.toString();
    }

    public static String digitsToNumber(List<DigitToken> digits) {
        StringBuilder stringBuilder = new StringBuilder();
        for (DigitToken digit : digits) {
            stringBuilder.append(digit);
        }
        return stringBuilder.toString();
    }

    public static String toString(List<Token> tokens) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Token token : tokens) {
            stringBuilder.append(token);
        }
        return stringBuilder.toString();
    }

}
