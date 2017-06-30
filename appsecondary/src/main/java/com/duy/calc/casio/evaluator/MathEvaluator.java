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

import android.util.Log;

import com.duy.calc.casio.JFok;
import com.duy.calc.casio.evaluator.thread.SolveThread;
import com.duy.calc.casio.exception.CalculateException;
import com.duy.calc.casio.exception.CanNotFindRootException;
import com.duy.calc.casio.exception.DMathException;
import com.duy.calc.casio.exception.SyntaxException;
import com.duy.calc.casio.exception.UnhandledException;
import com.duy.calc.casio.token.BracketToken;
import com.duy.calc.casio.token.FunctionToken;
import com.duy.calc.casio.token.NumberToken;
import com.duy.calc.casio.token.OperatorToken;
import com.duy.calc.casio.token.StringToken;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.token.VectorToken;
import com.duy.calc.casio.token.factory.BracketFactory;
import com.duy.calc.casio.token.factory.DigitFactory;
import com.duy.calc.casio.token.factory.FunctionFactory;
import com.duy.calc.casio.token.factory.OperatorFactory;
import com.duy.calc.casio.token.factory.VariableFactory;
import com.duy.calc.casio.tokenizer.ExceptionChecker;
import com.duy.calc.casio.tokenizer.MachineExpressionConverter;
import com.duy.calc.casio.util.DLog;
import com.google.common.collect.Lists;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.exception.WrongArgumentType;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import java.util.ArrayList;
import java.util.List;

import static com.duy.calc.casio.evaluator.Utility.addMissingBrackets;
import static com.duy.calc.casio.evaluator.Utility.isAcceptResultDecimal;
import static com.duy.calc.casio.evaluator.Utility.isAcceptResultFraction;
import static com.duy.calc.casio.evaluator.Utility.setupExpression;
import static com.duy.calc.casio.evaluator.Utility.subVariables;
import static com.duy.calc.casio.tokenizer.MachineExpressionConverter.convertStringToTokens;
import static com.duy.calc.casio.tokenizer.MachineExpressionConverter.convertTokensToString;

/**
 * Utilities specifically for calculus related functions.
 *
 * @author Alston Lin
 * @version 3.0
 */
public class MathEvaluator {
    private static final String TAG = "MathEvaluator";
    private static final ExprEvaluator EVAL_ENGINE;

    static {
        EVAL_ENGINE = new ExprEvaluator();
        String combination = "Combination(n_Integer, k_Integer):=(factorial(n) / "
                + "(factorial(k) * factorial(n - k)))";
        EVAL_ENGINE.evaluate(combination);
        String pol = "Pol(x_, y_):=Sqrt(x^2+y^2)";
        EVAL_ENGINE.evaluate(pol);
        String randomReal = "RandomInt(x_Integer, y_Integer):=x+RandomInteger(y-x)";
        EVAL_ENGINE.evaluate(randomReal);
    }

    /**
     * Processes the expression and returns the result using the Shunting Yard Algorithm to convert
     * the expression into reverse polish and then evaluating it.
     *
     * @param tokens The expression to doCalculate
     * @return The numerical value of the expression+++---9+8
     * @throws IllegalArgumentException If the user has input a invalid expression
     */
    public static ArrayList<Token> evaluateSimple(ArrayList<Token> tokens,
                                                  EvaluateConfig config)
            throws CalculateException {
        try {
            ExceptionChecker.checkAll(tokens);

            tokens = addMissingBrackets(tokens); //ok
            tokens = subVariables(tokens);
            tokens = setupExpression(tokens);

            String input = MachineExpressionConverter.convertTokensToString(tokens, config);
            IExpr result = MathEvaluator.getInstance().evaluate(input);

            if (result.isNumber() && !result.isFraction()) {
                return convertStringToTokens(result.toString());
            }

            if (!isAcceptResultFraction(result)) {
                String expr = "N(" + result.toString() + "," + config.getRoundTo() + ")";
                result = MathEvaluator.getInstance().evaluate(expr);
                if (isAcceptResultDecimal(result) && result.isNumeric()) {
                    return convertStringToTokens(result.toString());
                } else {
                    throw new UnsupportedOperationException(result.toString());
                }
            } else {
                if (config.getEvaluateMode() == EvaluateConfig.FRACTION) {
                    DLog.d(TAG, "evaluateSimple: isNumber " + result.isNumber());
                    return convertStringToTokens(result.toString());
                } else {
                    String expr = "N(" + result.toString() + "," + config.getRoundTo() + ")";
                    result = MathEvaluator.getInstance().evaluate(expr);
                    if (result.isNumeric()) {
                        if (isAcceptResultDecimal(result)) {
                            return Lists.<Token>newArrayList(new NumberToken(result.toString()));
                        } else {
                            throw new UnsupportedOperationException(result.toString());
                        }
                    } else {
                        throw new UnsupportedOperationException(result.toString());
                    }
                }
            }
        } catch (SyntaxError e) {
            throw new SyntaxException(e);
        } catch (MathException e) {
            throw new DMathException(e);
        } catch (CalculateException e) {
            throw e;
        } catch (Exception e) {
            throw new UnhandledException(e);
        }

    }

    /**
     * Differentiates the given function in ArrayList form.
     *
     * @param function The function to derivative
     * @return The differentiated function
     */
    public static ArrayList<Token> derivative(ArrayList<Token> function) {
        String expr = convertTokensToString(setupExpression(function));
        try {
            String derivativeStr = differentiateStr(expr);
            ArrayList<Token> derivative = convertStringToTokens(derivativeStr);
            derivative = JFok.jFokExpression(derivative);
            return derivative;
        } catch (SyntaxError e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Differentiates the given function in ArrayList form.
     *
     * @param function The function to derivative
     * @return The differentiated function
     */
    public static ArrayList<Token> integrate(ArrayList<Token> function) throws UnsupportedOperationException {
        String expr = convertTokensToString(setupExpression(function));
        try {
            String integralStr = integrateStr(expr);
            ArrayList<Token> integral = convertStringToTokens(integralStr);
            integral = replaceAppellF1(integral);
            integral = JFok.jFokExpression(integral);
            //Constant of Integration
            integral.add(OperatorFactory.makeAdd());
            integral.add(VariableFactory.makeConstant());
            return integral;
        } catch (SyntaxError e) { //Malformed expression
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Replaces the AppellF1 functions with their values
     *
     * @param expression The original expression
     * @return The expression with AppellF1 instances replaced
     */
    public static ArrayList<Token> replaceAppellF1(ArrayList<Token> expression) {
        boolean replaced = false; //If there was a AppellF1 function replaced
        ArrayList<Token> newExpr = new ArrayList<>();
        for (int i = 0; i < expression.size(); i++) {
            Token t = expression.get(i);
            if (t instanceof FunctionToken && t.getType() == FunctionToken.APPELLF1) {
                replaced = true;
                i += 2; //Open bracket
                ArrayList<Token> function = new ArrayList<>();
                int bracketCount = 1;
                while (bracketCount != 0) {
                    Token token = expression.get(i);
                    if (token instanceof BracketToken && token.getType() == BracketToken.PARENTHESES_OPEN) {
                        bracketCount++;
                    } else if (token instanceof BracketToken && ((BracketToken) token).getType() == BracketToken.PARENTHESES_CLOSE) {
                        bracketCount--;
                    }
                    function.add(token);
                    i++;
                }
                function.remove(function.size() - 1); //Last close bracket
                if (convertTokensToString(function).equals("2.0,1.0/2.0,1.0/2.0,3.0,x,-x")) {
                    //AppellF1(2,1/2,1/2,3/x,-x) = -2sqrt(1-x^2)/x^2
                    newExpr.add(DigitFactory.makeNegative());
                    newExpr.add(DigitFactory.makeTwo());
                    newExpr.add(OperatorFactory.makeMultiply());
                    newExpr.add(FunctionFactory.makeSqrt());
                    newExpr.add(BracketFactory.makeOpenParentheses());
                    newExpr.add(DigitFactory.makeOne());
                    newExpr.add(OperatorFactory.makeSubtract());
                    newExpr.add(VariableFactory.makeX());
                    newExpr.add(OperatorFactory.makePower());
                    newExpr.add(DigitFactory.makeTwo());
                    newExpr.add(BracketFactory.makeCloseParentheses());
                    newExpr.add(OperatorFactory.makeDivide());
                    newExpr.add(VariableFactory.makeX());
                    newExpr.add(OperatorFactory.makePower());
                    newExpr.add(DigitFactory.makeTwo());
                }
            } else {
                newExpr.add(t);
            }
        }
        return replaced ? expand(newExpr) : newExpr;
    }

    /**
     * Uses the Symja library to doCalculate the String version of the integral.
     *
     * @param function The function to integrate
     * @return The indefinite integral of the function
     */
    public static String integrateStr(String function) {
        String str = "Factor(integrate(" + function + ",x))";
        IExpr integral = EVAL_ENGINE.evaluate(str);
        String integralStr = integral.toString();
        if (integral.toString().contains("Integrate")) { //Could not integrate into an elementary function
            throw new UnsupportedOperationException();
        }
        if (integralStr.contains("Factor")) { //Cannot factor further
            integralStr = integralStr.substring(7, integralStr.length() - 1);
        }
        return integralStr;
    }


    /**
     * Checks if the Token before the given index is an Operator.
     *
     * @param tokens    The list of Tokens
     * @param tempIndex the index to checkAll
     * @return If there is an Operator before or not
     */
    private static boolean operatorBefore(ArrayList<Token> tokens, int tempIndex) {
        if (tempIndex == 0) {
            return false;
        }
        while (tempIndex > 1 && tokens.get(tempIndex - 1) instanceof BracketToken) { //Deos not count brackets
            tempIndex--;
        }
        return tokens.get(tempIndex - 1) instanceof OperatorToken;
    }

    /**
     * Uses the Symja library to doCalculate the String version of the derivative
     *
     * @param function The function to find the derivative
     * @return The derivative
     */
    public static String differentiateStr(String function) {
        IExpr derivative = EVAL_ENGINE.evaluate("diff(" + function + ",x)");
        return derivative.toString();
    }

    /**
     * Simplifies the given function in ArrayList form.
     *
     * @param function The function to derivative
     * @return The differentiated function
     */
    public static ArrayList<Token> simplify(ArrayList<Token> function) {
        String expr = convertTokensToString(setupExpression(function));
        try {
            expr = expr.replaceAll("☺", "\\$");
            String simplifiedStr = simplifyStr(expr).replaceAll("\\$", "☺");
            ArrayList<Token> simplified = convertStringToTokens(simplifiedStr);
            simplified = JFok.jFokExpression(simplified);
            return simplified;
        } catch (SyntaxError e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Uses the Symja library to simplify the String version of the expression
     *
     * @param function The function to find the derivative
     * @return The derivative
     */
    public static String simplifyStr(String function) {
        IExpr simplified = EVAL_ENGINE.evaluate("Simplify(" + function + ")");
        return simplified.toString();
    }

    /**
     * Expands the Expression.
     *
     * @param expression The expression to expand
     * @return The expanded expression
     */
    public static ArrayList<Token> expand(ArrayList<Token> expression) {
        String simplifyStr = "Expand(" + convertTokensToString(setupExpression(expression)) + ")";
        try {
            ArrayList<Token> simplified = convertStringToTokens(EVAL_ENGINE.evaluate(simplifyStr).toString());
            simplified = JFok.jFokExpression(simplified);
            return simplified;
        } catch (SyntaxError e) {
            return null; //Malformed Expression
        }
    }

    /**
     * Factors the Expression.
     *
     * @param expression The expression to factor
     * @return The factored expression
     */
    public static ArrayList<Token> factor(ArrayList<Token> expression) {
        String simplifyStr = "Factor(" + convertTokensToString(setupExpression(expression)) + ")";
        try {
            String simplifiedStr = EVAL_ENGINE.evaluate(simplifyStr).toString();
            if (simplifiedStr.contains("Factor")) { //Cannot factor further
                simplifiedStr = simplifiedStr.substring(7, simplifiedStr.length() - 1);
            }
            ArrayList<Token> simplified = convertStringToTokens(simplifiedStr);
            simplified = JFok.jFokExpression(simplified);
            return simplified;
        } catch (SyntaxError e) {
            return null; //Malformed Expression
        }
    }

    /**
     * Finds the roots of the expression.
     *
     * @param expression The expression to find the roots of
     * @return The roots of the expression in a List of expressions
     */
    public static ArrayList<ArrayList<Token>> findRoots(List<Token> expression) {
        ExceptionChecker.checkAll(expression);
        String cmd = SolveThread.createSolveCommand(expression, false);
        try {
            //Uses the library to get the result as a String
            IExpr roots = EVAL_ENGINE.evaluate(cmd);
            String rootsStr = roots.toString().replace("\n", "");
            ArrayList<ArrayList<Token>> rootsList = new ArrayList<>();

            if (rootsStr.contains("Solve")) {
                return rootsList;
            }
            //Converts the String into a List of Floats
            String temp = "";
            for (int i = 5; i < rootsStr.length() - 2; i++) { //Deos not count the beginning and end {{x-> and }}
                char c = rootsStr.charAt(i);
                if (c == '}') { //Solutions separate by {},
                    if (!temp.contains("I")) { //REAL NUMBERS ONLY
                        rootsList.add(convertStringToTokens(temp));
                    }
                    i += 5; //Skips the },{x->
                    temp = "";
                } else {
                    temp += c;
                }
            }

            //Real numbers only
            if (!temp.contains("I")) {
                rootsList.add(convertStringToTokens(temp)); //For the last root
            }
            return rootsList;
        } catch (SyntaxError e) { //Malformed Expression
            return null;
        } catch (WrongArgumentType e) {
            return new ArrayList<>(); //Expression was in the form of e/e; No real roots
        }
    }

    /**
     * Finds all real eigen values for the given matrix.
     *
     * @param matrix The matrix
     * @return The eigenvalues of the matrix
     */
    public static double[] getEigenValues(double[][] matrix) {
        String eigenString = parseMatrix(matrix);
        return getEigenValuesStr(eigenString);
    }

    /**
     * Converts the given matrix into a String.
     *
     * @param matrix The matrix as an array of doubles
     * @return The string representation
     */
    private static String parseMatrix(double[][] matrix) {
        String s = "{";
        for (int i = 0; i < matrix.length; i++) {
            if (i != 0) {
                s += ",";
            }
            s += "{";
            for (int j = 0; j < matrix[i].length; j++) {
                if (j != 0) {
                    s += ",";
                }
                s += matrix[i][j];
            }
            s += "}";
        }
        s += "}";
        return s;
    }

    /**
     * Uses the symja library to doCalculate an Eigenvalue computation from a String query.
     *
     * @param matrix The string representation of the matrix
     * @return The eigenvalues of the matrix
     */
    public static double[] getEigenValuesStr(String matrix) {
        String str = "Eigenvalues(" + matrix + ")";
        IExpr eigenvalues = EVAL_ENGINE.evaluate(str);
        String eigenStr = eigenvalues.toString();
        eigenStr = eigenStr.substring(1, eigenStr.length() - 2); //Removes start and end {}
        String[] eigenValuesStr = eigenStr.split(",");
        double[] eigenValues = new double[eigenValuesStr.length];
        for (int i = 0; i < eigenValuesStr.length; i++) {
            eigenValues[i] = Double.parseDouble(eigenValuesStr[i]);
        }
        return eigenValues;
    }

    /**
     * Uses symja to get the eigenbasis of a given matrix
     *
     * @param matrix The input matrix
     * @return The basis of the eigenspace of the matrix
     */
    public static ArrayList<VectorToken> getEigenVectors(double[][] matrix) { //TODO: finish this
        String eigenString = parseMatrix(matrix);
        return getEigenVectorsStr(eigenString, MatrixEvaluator.roundInfinitesimals(getEigenValues(matrix)));
    }

    public static ArrayList<VectorToken> getEigenVectorsStr(String matrix, double[] eigenvals) {
        String str = "Nullspace(" + matrix;
        ArrayList<Double> done = new ArrayList<>();
        int n = eigenvals.length;
        double[][] vects;
        boolean abort = false;
        //int[] dim = new int[2];
        String vectors = "{";
        for (Double val : eigenvals) {
            if (!done.contains(val) && abort == false) {
                String currentExpr = str.concat(" - N(" + val + ")*IdentityMatrix(" + n + "))");
                IExpr expr = null;
                try {
                    expr = EVAL_ENGINE.evaluate(currentExpr);
                } catch (Exception e) {
                    abort = true;
                    break;
                }
                if (!abort) {
                    String temp = expr.toString();
                    if (temp.equals("{}")) {
                        abort = true;
                        break;
                    }
                    temp = temp.substring(1, temp.length() - 1).trim();
                    vectors += temp;
                    vectors += ",";
                    done.add(val);
                }
            }
        }
        if (abort == false) {
            vectors += "}";

            vects = deparseMatrix(vectors, n, n);
            ArrayList<VectorToken> output = new ArrayList<>();
            for (double[] vector : vects) {
                output.add(new VectorToken(vector));
            }
            return output;
        } else {
            return new ArrayList<VectorToken>();
        }
    }

    private static double[][] deparseMatrix(String matrix, int ncol, int nrow) {
        matrix = matrix.substring(1, matrix.length() - 2); //Removes start and end {} and a ,
        String[] strArray = matrix.split("\\},");
        for (int i = 0; i < strArray.length; i++) {
            strArray[i] = strArray[i].trim();
        }
        double[][] output = new double[nrow][ncol];
        for (int i = 0; i < strArray.length; i++) {
            String[] temp = strArray[i].split(",");
            for (int j = 0; j < temp.length; j++) {
                temp[j] = temp[j].replaceAll("\\{", "").trim();
                temp[j] = temp[j].replaceAll("\\}", "").trim();
                output[i][j] = Double.parseDouble(temp[j]);
            }
        }
        return output;
    }

    public static ArrayList<Token> primeFactor(List<Token> tokens) {
        String primeFactor = MachineExpressionConverter.convertTokensToString(tokens);
        primeFactor = "FactorInteger(" + primeFactor + ")";
        Log.d(TAG, "primeFactor: " + primeFactor);
        IExpr result = EVAL_ENGINE.evaluate(primeFactor);
        primeFactor = result.toString();
        if (primeFactor.contains("FactorInteger")) {
            throw new UnsupportedOperationException("Can not prime factor");
        }

        ArrayList<Token> res = new ArrayList<>();
        primeFactor = primeFactor.substring(2, primeFactor.length() - 2); //delete { }
        String[] params = primeFactor.split("\\},\\{");
        for (int i = 0; i < params.length; i++) {
            String param = params[i];
            String[] split = param.split(",");
            res.add(new StringToken(split[0]));
            res.add(OperatorFactory.makePower());
            res.add(BracketFactory.makeSuperscriptOpen());
            res.add(new StringToken(split[1]));
            res.add(BracketFactory.makeSuperscriptClose());
            if (i != params.length - 1) {
                res.add(OperatorFactory.makeMultiply());
            }
        }
        Log.d(TAG, "primeFactor() returned: " + res);
        return res;
    }

    public static ExprEvaluator getInstance() {
        return EVAL_ENGINE;
    }
}

