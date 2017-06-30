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

package com.duy.calc.casio;

import com.duy.calc.casio.token.factory.BracketFactory;
import com.duy.calc.casio.token.factory.DigitFactory;
import com.duy.calc.casio.token.factory.ExpressionFactory;
import com.duy.calc.casio.token.FunctionToken;
import com.duy.calc.casio.token.factory.FunctionFactory;
import com.duy.calc.casio.token.factory.OperatorFactory;
import com.duy.calc.casio.token.factory.VariableFactory;
import com.duy.calc.casio.token.BracketToken;
import com.duy.calc.casio.token.NumberToken;
import com.duy.calc.casio.token.OperatorToken;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.token.VariableToken;
import com.duy.calc.casio.evaluator.Utility;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Contains the JFok Algorithm and the core of the mathematical system.
 *
 * @author J(ason)Fok, Ejaaz Merali, Alston Lin
 * @version 3.0
 */
public class JFok {

    public static final double FRACTIONALIZE_ERROR = 1e-12;
    public static final double FRACTIONALIZE_DENOM_LIMIT = 10000;

    /**
     * Simplify and factor the given mathematical expression. There should be no
     * variables in the expression given.
     *
     * @param expression The expression to simplify
     * @return The fully simplified expression
     */
    public static ArrayList<Token> simplifyExpression(ArrayList<Token> expression) {
        //Sets up the tree
        Node<Token> root = setupAndConvertToTree(expression);
        //This is where the magic happens
        root = simplify(root);
        //Converts it back to human-readable form and cleans it up
        expression = traverseTree(root);
        expression = cleanupExpression(expression);
        return expression;
    }

    /**
     * Transforms the given expression into proper mathematical form by evaluating any sub-expressions that
     * would return an exact value (eg. ln(e)), as well as taking away any negative exponents.
     *
     * @param expression The expression to jFok
     * @return The expression that has been jFoked
     */
    public static ArrayList<Token> jFokExpression(ArrayList<Token> expression) {
        //Sets up the tree
        Node<Token> root = setupAndConvertToTree(expression);
        root = jFok(root);
        //Converts it back to human-readable form and cleans it up
        expression = traverseTree(root);
        expression = cleanupExpression(expression);
        return expression;
    }

    /**
     * Converts the expression into standard form.
     *
     * @param expression The expression to convert
     * @return The expression in standard form
     */
    public static ArrayList<Token> convertToStandardForm(ArrayList<Token> expression) {
        //Sets up the tree
        Node<Token> root = setupAndConvertToTree(expression);
        //This is where the magic happens
        root = expand(root);
        //Converts it back to human-readable form
        expression = traverseTree(root);
        expression = cleanupExpression(expression);
        return expression;
    }

    /**
     * Converts the given Number into a fraction. If the denominator if more
     * than 1000, it will assume the result is inaccurate and leave it.
     *
     * @param number The number Token
     * @return An equivalent fraction
     */
    public static ArrayList<Token> fractionalize(NumberToken number) {
        double value = number.getDoubleValue();
        int n = (int) Math.floor(value);
        ArrayList<Token> output = new ArrayList<>();
        value -= n;

        //Checks if it is an integer
        if (value < FRACTIONALIZE_ERROR) {
            output.add(new NumberToken(n));
            return output;
        } else if (1 - FRACTIONALIZE_ERROR < value) {
            output.add(new NumberToken(n + 1));
            return output;
        }

        //The lower fraction starts at 0 / 1
        int lowerN = 0;
        int lowerD = 1;
        //The upper fraction starts at 1 / 1
        int upperN = 1;
        int upperD = 1;

        while (true) { //Continues until returns

            //Finds the average of the upper and lower fractions
            int middleN = lowerN + upperN;
            int middleD = lowerD + upperD;

            if (middleD * (value + FRACTIONALIZE_ERROR) < middleN) {
                upperN = middleN;
                upperD = middleD;
            } else if (middleN < (value - FRACTIONALIZE_ERROR) * middleD) {
                lowerN = middleN;
                lowerD = middleD;
            } else {
                if (middleD < FRACTIONALIZE_DENOM_LIMIT) { //Denom is within the limit
                    NumberToken num = new NumberToken(n * middleD + middleN);
                    NumberToken denom = new NumberToken(middleD);

                    output.add(BracketFactory.makeFractionOpen());
                    output.add(BracketFactory.makeNumeratorOpen());
                    output.add(num);
                    output.add(BracketFactory.makeNumeratorClose());
                    output.add(OperatorFactory.makeFraction());
                    output.add(BracketFactory.makeDenominatorOpen());
                    output.add(denom);
                    output.add(BracketFactory.makeDenominatorClose());
                    output.add(BracketFactory.makeFractionClose());
                    return output;
                } else {
                    ArrayList<Token> original = new ArrayList<>();
                    original.add(number);
                    return original;
                }
            }
        }
    }

    /**
     * Calls all the preparation methods for the expression and then converts the list
     * of expression into a expression tree.
     *
     * @param expression The list of expression
     * @return The expression tree
     */
    public static Node<Token> setupAndConvertToTree(ArrayList<Token> expression) {
        expression = Utility.condenseDigits(expression);
        expression = Utility.setupExpression(expression);
        expression = Utility.convertToRPN(expression);
        return setupTree(expression);
    }

    /**
     * Expands the given binary expression tree by applying rules.
     *
     * @param root The root of the tree
     * @return The root of the new expanded tree
     */
    private static Node<Token> expand(Node<Token> root) {
        if (root.getNumOfChildren() > 0) { //Not a leaf
            Node<Token> n1 = root.getNumOfChildren() > 0 ? root.getChildren().get(0) : null;
            Node<Token> n2 = root.getNumOfChildren() > 1 ? root.getChildren().get(1) : null;
            //Applies rules to the subtrees first, if any are found
            if ((n1 != null && n1.getNumOfChildren() != 0) || (n2 != null && n2.getNumOfChildren() != 0)) { //The children are not leaves
                //Evaluates the subtrees first
                n1 = expand(n1);
                if (n2 != null) {
                    n2 = expand(n2);
                }
                //Adds the new subtrees to the root
                root.getChildren().clear();
                root.addChild(n1);
                if (n2 != null) {
                    root.addChild(n2);
                }
                //Now applies the rules
                root = applyExpandingRules(root);
                return root;
            } else { //Children are leaves; applies the rules
                root = applyExpandingRules(root);
                return root;
            }
        } else { //Base case: the root is a leaf
            return root;
        }
    }

    /**
     * Applies the math rules to expand and then simplify the expression into
     * standard form.
     *
     * @param root The root of the tree/subtree
     * @return The root of the new tree/subtree
     */
    private static Node<Token> applyExpandingRules(Node<Token> root) {
        root = applyPowers(root);
        root = processMultipleFractions(root, new Command<Node<Token>, Node<Token>>() {
            @Override
            public Node<Token> execute(Node<Token> o) {
                return expand(o);
            }
        });
        root = applyDistributiveProperty(root);
        root = multiplyPolynomials(root);
        root = multiplyTerms(root);
        //Rules to be applied before constants are evaluated
        root = applyCommutativeProperty(root);
        root = evaluateConstants(root, false, false);
        //Rules to be applied after constants are evaluated
        //root = addLikeTerms(root);
        root = multiplyVariables(root);
        return root;
    }

    /**
     * Applies the math rules to simplify and factor the expression/
     *
     * @param root The root of the tree/subtree
     * @return The new root of the tree/subtree
     */
    private static Node<Token> applySimplifyingRules(Node<Token> root) {
        Command<Node<Token>, Node<Token>> command = new Command<Node<Token>, Node<Token>>() {
            @Override
            public Node<Token> execute(Node<Token> o) {
                return simplify(o);
            }
        };
        root = multiplyDecimalFractions(root);
        root = applyTrigSpecialValues(root);
        root = applyInvTrigSpecialValues(root);
        root = processMultipleFractions(root, command);
        root = applySquareRootRules(root);
        root = applyCommutativeProperty(root);
        //Rules to be applied before constants are evaluated
        root = evaluateConstants(root, true, true);
        //Rules to be applied after constants are evaluated
        root = multiplyFractions(root, command);
        root = addLikeFractions(root);
        root = simplifyFraction(root);
        root = simplifyIdenticalNumDenom(root);
        root = removeMultiplicationsOfOne(root);
        root = applyTrigRules(root);
        return root;
    }

    /**
     * Applies special values of inverse trig functions in Radians.
     *
     * @param root The node to apply this rule to
     * @return The new node
     */
    private static Node<Token> applyInvTrigSpecialValues(Node<Token> root) {
        if (FunctionToken.angleMode == FunctionToken.RADIAN && root.getContent() instanceof FunctionToken &&
                (root.getContent().getType() == FunctionToken.ARCSIN || root.getContent().getType() == FunctionToken.ARCCOS || root.getContent().getType() == FunctionToken.ARCTAN)) {
            ArrayList<Token> expression = traverseTree(root.getChildren().get(0));
            double value = Double.parseDouble(Utility.evaluate(expression));
            value = Utility.round(value, 7);
            if (root.getContent().getType() == FunctionToken.ARCSIN) {
                if (value == -1) { //arcsin(-1) = -pi / 2
                    return ExpressionFactory.makePiOverN(true, 2);
                } else if (value == -0.8660254) {//arcsin(-sqrt3/2) = -pi / 3
                    return ExpressionFactory.makePiOverN(true, 3);
                } else if (value == -0.7071068) {//arcsin(-sqrt2/2) = -pi / 4
                    return ExpressionFactory.makePiOverN(true, 4);
                } else if (value == -0.5) {//arcsin(-1/2) = -pi / 6
                    return ExpressionFactory.makePiOverN(true, 6);
                } else if (value == 0) { //arcsin(0) = 0
                    return new Node<Token>(new NumberToken(0));
                } else if (value == 0.5) {//arcsin(1/2) = pi / 6
                    return ExpressionFactory.makePiOverN(false, 6);
                } else if (value == 0.7071068) {//arcsin(sqrt2/2) = pi / 4
                    return ExpressionFactory.makePiOverN(false, 4);
                } else if (value == 0.8660254) {//arcsin(sqrt3/2) = pi / 3
                    return ExpressionFactory.makePiOverN(false, 3);
                } else if (value == 1) { //arcsin(1) = pi / 2
                    return ExpressionFactory.makePiOverN(false, 2);
                }
            } else if (root.getContent().getType() == FunctionToken.ARCCOS) {
                if (value == -1) { //arccos(-1) = pi
                    return new Node<Token>(VariableFactory.makePI());
                } else if (value == -0.8660254) {//arccos(-sqrt3/2) = 5pi / 6
                    return ExpressionFactory.makeNPiOverM(5, 6);
                } else if (value == -0.7071068) {//arccos(-sqrt2/2) = 3pi / 4
                    return ExpressionFactory.makeNPiOverM(3, 4);
                } else if (value == -0.5) {//arccos(-1/2) = 2pi / 3
                    return ExpressionFactory.makeNPiOverM(2, 3);
                } else if (value == 0) { //arccos(0) = pi / 2
                    return ExpressionFactory.makePiOverN(false, 2);
                } else if (value == 0.5) {//arccos(1/2) = pi / 3
                    return ExpressionFactory.makePiOverN(false, 3);
                } else if (value == 0.7071068) {//arccos(sqrt2/2) = pi / 4
                    return ExpressionFactory.makePiOverN(false, 4);
                } else if (value == 0.8660254) {//arccos(sqrt3/2) = pi / 6
                    return ExpressionFactory.makePiOverN(false, 6);
                } else if (value == 1) { //arccos(1) = 0
                    return new Node<Token>(new NumberToken(0));
                }
            } else if (root.getContent().getType() == FunctionToken.ARCTAN) {
                if (value == -1.732051) { //arctan(-sqrt3) = -pi / 3
                    return ExpressionFactory.makePiOverN(true, 3);
                } else if (value == -1) { //arctan(-1) = -pi / 4
                    return ExpressionFactory.makePiOverN(true, 4);
                } else if (value == -0.5773503) { //arctan(-sqrt3/3) = -pi / 6
                    return ExpressionFactory.makePiOverN(true, 6);
                } else if (value == 0) { //arctan(0) = 0
                    return new Node<Token>(new NumberToken(0));
                } else if (value == 0.5773503) { //arctan(sqrt3/3) = pi / 6
                    return ExpressionFactory.makePiOverN(false, 6);
                } else if (value == 1) { //arctan(1) = pi / 4
                    return ExpressionFactory.makePiOverN(false, 4);
                } else if (value == 1.732051) { //arctan(sqrt3) = pi / 3
                    return ExpressionFactory.makePiOverN(false, 3);
                }
            } else {
                return root;
            }
        }
        return root;
    }

    /**
     * Multiplies decimals so that there would be no decimal fractions
     *
     * @param root The root of the fraction to doCalculate
     * @return The new root
     */
    private static Node<Token> multiplyDecimalFractions(Node<Token> root) {
        if (root.getContent() instanceof OperatorToken && (root.getContent().getType() == OperatorToken.FRACTION || root.getContent().getType() == OperatorToken.DIV)) {
            Token child1 = root.getChildren().get(0).getContent();
            Token child2 = root.getChildren().get(1).getContent();
            if (child1 instanceof NumberToken && child2 instanceof NumberToken) {
                double value1 = ((NumberToken) child1).getDoubleValue();
                double value2 = ((NumberToken) child2).getDoubleValue();
                if (value1 % 1 != 0 || value2 % 1 != 0) {
                    String s1 = Double.toString(Math.abs(value1));
                    String s2 = Double.toString(Math.abs(value2));
                    int decimals1 = s1.length() - s1.indexOf('.') - 1;
                    int decimals2 = s2.length() - s2.indexOf('.') - 1;
                    int multiplier = (int) (decimals1 > decimals2 ? Math.pow(10, decimals1) : Math.pow(10, decimals2));
                    value1 *= multiplier;
                    value2 *= multiplier;
                    Node<Token> div = new Node<Token>(OperatorFactory.makeFraction());
                    div.addChild(new Node<Token>(new NumberToken(Math.round(value1))));
                    div.addChild(new Node<Token>(new NumberToken(Math.round(value2))));
                    return div;
                }
            }
        }
        return root;
    }

    private static Node<Token> applyTrigSpecialValues(Node<Token> root) {
        if (root.getContent() instanceof FunctionToken && root.getChildren().get(0).getContent() instanceof NumberToken) {
            double num = ((NumberToken) root.getChildren().get(0).getContent()).getDoubleValue();

            //Converts to degrees
            if (FunctionToken.angleMode == FunctionToken.RADIAN) {
                num = Math.toDegrees(num);
            } else if (FunctionToken.angleMode == FunctionToken.GRADIAN) {
                num = 0.9 * num;
            }

            //Converts it into a positive number
            if (num < 0) {
                int multiple = (int) (-(num / 360) + 1);
                num += multiple * 360;
            }
            //Rounds to 1e-7 tolerance
            double tolerance = 1e-7;
            num = Math.round(num / tolerance) * tolerance;
            if (root.getContent().getType() == FunctionToken.SIN) {
                double degree = num % 360;
                boolean negative = false;
                //Converts to <90 degree
                if (degree > 90 && degree < 180) { // Q2 -> 180 - angle
                    degree = 180 - degree;
                } else if (degree > 180 && degree < 270) { // Q3 -> angle - 180 & negative
                    degree = degree - 180;
                    negative = true;
                } else if (degree > 270) { //Q4 -> 360 - angle & negative
                    degree = 360 - degree;
                    negative = true;
                }

                if (degree == 0) { //sin(0) = 0
                    return new Node<Token>(new NumberToken(0));
                } else if (degree == 30) { //sin(30) = 1/2
                    return ExpressionFactory.makeOneHalf(negative);
                } else if (degree == 45) { //sin(45) = sqrt2 / 2
                    return ExpressionFactory.makeSqrt2Over2(negative);
                } else if (degree == 60) { //sin(60) = sqrt3 / 2
                    return ExpressionFactory.makeSqrt3Over2(negative);
                } else if (degree == 90) { //sin(90) = 1
                    return new Node<Token>(new NumberToken(1));
                }
            } else if (root.getContent().getType() == FunctionToken.COS) {
                double degree = num % 360;
                boolean negative = false;
                //Converts to <90 degree
                if (degree > 90 && degree < 180) { // Q2 -> 180 - angle & negative
                    degree = 180 - degree;
                    negative = true;
                } else if (degree > 180 && degree < 270) { // Q3 -> angle - 180 & negative
                    degree = degree - 180;
                    negative = true;
                } else if (degree > 270) { //Q4 -> 360 - angle
                    degree = 360 - degree;
                }
                if (degree == 0) { //cos(0) = 1
                    return new Node<Token>(new NumberToken(1));
                } else if (degree == 30) { //cos(30) = sqrt3 / 2
                    return ExpressionFactory.makeSqrt3Over2(negative);
                } else if (degree == 45) { //cos(45) = sqrt2 / 2
                    return ExpressionFactory.makeSqrt2Over2(negative);
                } else if (degree == 60) { //cos(60) = 1 / 2
                    return ExpressionFactory.makeOneHalf(negative);
                } else if (degree == 90) { //cos(90) = 0
                    return new Node<Token>(new NumberToken(0));
                }
            } else if (root.getContent().getType() == FunctionToken.TAN) {
                double degree = num % 360;
                boolean negative = false;
                //Converts to <90 degree
                if (degree > 90 && degree < 180) { // Q2 -> 180 - angle & negative
                    degree = 180 - degree;
                    negative = true;
                } else if (degree > 180 && degree < 270) { // Q3 -> angle - 180
                    degree = degree - 180;
                } else if (degree > 270) { //Q4 -> 360 - angle & negative
                    degree = 360 - degree;
                    negative = true;
                }
                if (degree == 0) { //tan(0) = 0
                    return new Node<Token>(new NumberToken(1));
                }
                if (degree == 30) { //tan(30) = sqrt 3 / 3
                    return ExpressionFactory.makeSqrt3Over3(negative);
                } else if (degree == 60) { //tan(30) = sqrt 3
                    return ExpressionFactory.makeSqrt3(negative);
                } else if (degree == 90) { //tan(90) = UNDEFINED
                    throw new IllegalArgumentException("tan is not defined at this value!");
                }
            }
        }
        return root;
    }

    /**
     * Applies simplifying trig rules to the treee if it applies.
     *
     * @param root The root to apply trig rules
     * @return The new tree
     */
    private static Node<Token> applyTrigRules(Node<Token> root) {
        if (root.getContent() instanceof OperatorToken && root.getContent().getType() == OperatorToken.DIV) {
            Node child1 = root.getChildren().get(0);
            Node child2 = root.getChildren().get(1);
            if (child1.getContent() instanceof FunctionToken && child2.getContent() instanceof FunctionToken) {
                if ((((FunctionToken) child1.getContent()).getType() == FunctionToken.SIN && ((FunctionToken) child2.getContent()).getType() == FunctionToken.COS)) { //sin / cos -> tan
                    Node<Token> inside1 = (Node<Token>) child1.getChildren().get(0);
                    Node inside2 = (Node) child2.getChildren().get(0);
                    if (isBranchesEqual(inside1, inside2)) {
                        Node<Token> tan = new Node<Token>(FunctionFactory.makeTan());
                        tan.addChild(inside1);
                        return tan;
                    }
                } else if ((((FunctionToken) child1.getContent()).getType() == FunctionToken.COS && ((FunctionToken) child2.getContent()).getType() == FunctionToken.SIN)) { //cos / sin -> 1/tan
                    Node<Token> inside1 = (Node<Token>) child1.getChildren().get(0);
                    Node<Token> inside2 = (Node<Token>) child2.getChildren().get(0);
                    if (isBranchesEqual(inside1, inside2)) {
                        Node<Token> div = new Node<Token>(OperatorFactory.makeDivide());
                        Node<Token> tan = new Node<Token>(FunctionFactory.makeTan());
                        tan.addChild(inside1);
                        div.addChild(new Node<Token>(new NumberToken(1)));
                        div.addChild(tan);
                        return div;
                    }
                }
            }
        }
        return root;
    }

    /**
     * Simplifies fractions that have identical Num and Denoms to 1.
     *
     * @param root The root of the tree to apply this rule to
     * @return The new root of the tree
     */
    private static Node<Token> simplifyIdenticalNumDenom(Node<Token> root) {
        if (root.getContent() instanceof OperatorToken && (root.getContent().getType() == OperatorToken.DIV || root.getContent().getType() == OperatorToken.FRACTION)) {
            Node<Token> child1 = root.getChildren().get(0);
            Node<Token> child2 = root.getChildren().get(1);
            if (isBranchesEqual(child1, child2)) {
                return new Node(new NumberToken(1));
            } else {
                return root;
            }
        } else {
            return root;
        }
    }

    /**
     * Determines if both nodes contain the exact same expression
     *
     * @param root1 The root of the first tree
     * @param root2 The root of the second tree
     * @return If the first and second tree contains the same expression
     */
    private static boolean isBranchesEqual(Node<Token> root1, Node<Token> root2) {
        if (root1 == null && root2 == null) { //Base case
            return true;
        } else if (root1 == null || root2 == null) { //Trees are different side
            return false;
        } else {
            boolean equal = root1.getContent().getClass().equals(root2.getContent().getClass()) && root1.getContent().getType() == root2.getContent().getType()
                    && (root1.getContent().getType() != -1 || ((NumberToken) root1.getContent()).getDoubleValue() == ((NumberToken) root2.getContent()).getDoubleValue());
            if (equal && root1.getNumOfChildren() == root2.getNumOfChildren()) { //Same number of subbranches
                if (root1.getNumOfChildren() == 1) {
                    equal = equal && isBranchesEqual(root1.getChildren().get(0), root2.getChildren().get(0));
                } else if (root1.getNumOfChildren() == 2) {
                    equal = equal && ((isBranchesEqual(root1.getChildren().get(0), root2.getChildren().get(0)) && isBranchesEqual(root1.getChildren().get(1), root2.getChildren().get(1)))
                            || (root1.getContent() instanceof OperatorToken && (root1.getContent().getType() == OperatorToken.ADD || root1.getContent().getType() == OperatorToken.MUL)
                            && (isBranchesEqual(root1.getChildren().get(1), root2.getChildren().get(0)) && isBranchesEqual(root1.getChildren().get(0), root2.getChildren().get(1)))));
                }
                return equal;
            } else {
                return false;
            }
        }
    }

    /**
     * Cleans up the expression and renders it more human-readable by removing
     * redundancies.
     *
     * @param expression The expression to clean up
     * @return The cleaned up expression
     */
    private static ArrayList<Token> cleanupExpression(ArrayList<Token> expression) {
        expression = removeNegatives(expression);
        expression = removeMultiplicationsOfOne(expression);
        expression = removeDivisionsOfOne(expression);
        expression = addFractionalBrackets(expression);
        return expression;
    }

    private static ArrayList<Token> addFractionalBrackets(ArrayList<Token> expression) {
        ArrayList<Token> newExp = new ArrayList<>();
        for (int i = 0; i < expression.size(); i++) {
            Token t = expression.get(i);
            if (t instanceof OperatorToken && (t.getType() == OperatorToken.FRACTION || t.getType() == OperatorToken.DIV)) {
                //Finds the numerator first
                ArrayList<Token> num = new ArrayList<>();
                int bracketCount = 0;
                int j = newExp.size() - 1;
                while (j >= 0 && !((bracketCount == 0 && newExp.get(j) instanceof OperatorToken && ((OperatorToken) newExp.get(j)).getPrecedence() < 3) || bracketCount < 0)) {
                    Token token = newExp.get(j);
                    if (token instanceof BracketToken) {
                        BracketToken b = (BracketToken) token;
                        if (b.getType() == BracketToken.PARENTHESES_OPEN || b.getType() == BracketToken.SUPERSCRIPT_OPEN) {
                            bracketCount--;
                        } else if (b.getType() == BracketToken.PARENTHESES_CLOSE || b.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
                            bracketCount++;
                        }
                    }
                    if (bracketCount >= 0) {
                        num.add(0, newExp.remove(j));
                    }
                    j--;
                }
                //Now denom
                j = i + 1;
                bracketCount = 0;
                ArrayList<Token> denom = new ArrayList<>();
                while (j < expression.size() && !((bracketCount == 0 && expression.get(j) instanceof OperatorToken && ((OperatorToken) expression.get(j)).getPrecedence() <= 3) || bracketCount < 0)) {
                    Token token = expression.get(j);
                    if (token instanceof BracketToken) {
                        BracketToken b = (BracketToken) token;
                        if (b.getType() == BracketToken.PARENTHESES_OPEN || b.getType() == BracketToken.SUPERSCRIPT_OPEN) {
                            bracketCount++;
                        } else if (b.getType() == BracketToken.PARENTHESES_CLOSE || b.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
                            bracketCount--;
                        }
                    }
                    if (bracketCount >= 0) {
                        denom.add(expression.get(j));
                        i++;
                    }
                    j++;
                }

                //Removes any brackets encompassing the num and denom, if any
                if (num.get(0) instanceof BracketToken && num.get(0).getType() == BracketToken.PARENTHESES_OPEN && num.get(num.size() - 1) instanceof BracketToken
                        && num.get(num.size() - 1).getType() == BracketToken.PARENTHESES_CLOSE) { //Num
                    num.remove(0);
                    num.remove(num.size() - 1);
                }
                if (denom.get(0) instanceof BracketToken && denom.get(0).getType() == BracketToken.PARENTHESES_OPEN && denom.get(denom.size() - 1) instanceof BracketToken
                        && denom.get(denom.size() - 1).getType() == BracketToken.PARENTHESES_CLOSE) { //Denom
                    denom.remove(0);
                    denom.remove(denom.size() - 1);
                }
                //And now joins the numerator and denom
                newExp.add(BracketFactory.makeNumeratorOpen());
                newExp.addAll(num);
                newExp.add(BracketFactory.makeNumeratorClose());
                newExp.add(OperatorFactory.makeFraction());
                newExp.add(BracketFactory.makeDenominatorOpen());
                newExp.addAll(denom);
                newExp.add(BracketFactory.makeDenominatorClose());
            } else {
                newExp.add(t);
            }
        }
        return newExp;
    }

    /**
     * Simplifies the given binary expression tree by applying rules.
     *
     * @param root The root of the tree
     * @return The root of the new expanded tree
     */
    private static Node<Token> simplify(Node<Token> root) {
        if (root.getNumOfChildren() > 0) { //Not a leaf
            Node<Token> n1 = root.getNumOfChildren() > 0 ? root.getChildren().get(0) : null;
            Node<Token> n2 = root.getNumOfChildren() > 1 ? root.getChildren().get(1) : null;
            //Applies rules to the subtrees first, if any are found
            if ((n1 != null && n1.getNumOfChildren() != 0) || (n2 != null && n2.getNumOfChildren() != 0)) { //The children are not leaves
                //Evaluates the subtrees first
                n1 = simplify(n1);
                if (n2 != null) {
                    n2 = simplify(n2);
                }
                //Adds the new subtrees to the root
                root.getChildren().clear();
                root.addChild(n1);
                if (n2 != null) {
                    root.addChild(n2);
                }
                //Now applies rules to this branch
                root = applySimplifyingRules(root);
                return root;
            } else { //Children are leaves; applies the rules
                root = applySimplifyingRules(root);
                return root;
            }
        } else { //Base case: the root is a leaf
            return root;
        }
    }

    /**
     * Applies a set of rules recursively that makes the expression better.
     *
     * @param root The root of the tree
     * @return The root of the new expanded tree
     */
    private static Node<Token> jFok(Node<Token> root) {
        if (root.getNumOfChildren() > 0) { //Not a leaf
            Node<Token> n1 = root.getNumOfChildren() > 0 ? root.getChildren().get(0) : null;
            Node<Token> n2 = root.getNumOfChildren() > 1 ? root.getChildren().get(1) : null;
            //Applies rules to the subtrees first, if any are found
            if ((n1 != null && n1.getNumOfChildren() != 0) || (n2 != null && n2.getNumOfChildren() != 0)) { //The children are not leaves
                //Evaluates the subtrees first
                n1 = jFok(n1);
                if (n2 != null) {
                    n2 = jFok(n2);
                }
                //Adds the new subtrees to the root
                root.getChildren().clear();
                root.addChild(n1);
                if (n2 != null) {
                    root.addChild(n2);
                }
                //Now applies rules to this branch
                root = applyJFokRules(root);
                return root;
            } else { //Children are leaves; applies the rules
                root = applyJFokRules(root);
                return root;
            }
        } else { //Base case: the root is a leaf
            return root;
        }
    }

    /**
     * To be called by jFok() to apply the relevants rules to sub-expression.
     *
     * @param node The sub-expression to apply the rules to
     * @return The new sub-expression
     */
    private static Node<Token> applyJFokRules(Node<Token> node) {
        Command<Node<Token>, Node<Token>> command = new Command<Node<Token>, Node<Token>>() {
            @Override
            public Node<Token> execute(Node<Token> o) {
                return jFok(o);
            }
        };
        node = evaluateConstants(node, true, true);
        node = removeNegativeExponents(node);
        node = addSqrts(node);
        node = removeMultiplicationsOfOne(node);
        node = multiplyFractions(node, command);
        node = processMultipleFractions(node, command);
        node = removeExponentsOfOne(node);
        return node;
    }

    /**
     * Searches for exponents of ones and removes them (eg. (x+1)^1 -> (x+1))
     *
     * @param node The node to apply this rule
     * @return The node with the rule applied
     */
    private static Node<Token> removeExponentsOfOne(Node<Token> node) {
        Token t = node.getContent();
        if (t instanceof OperatorToken && t.getType() == OperatorToken.POWER) {
            Node<Token> base = node.getChildren().get(0);
            Node<Token> exp = node.getChildren().get(1);
            if (getValue(exp) == 1) {
                return jFok(base);
            }
        }
        return node;
    }

    /**
     * A tree version of removing mutliplications of one (e * 1) -> e
     *
     * @param node The original root
     * @return The root of the new tree with the rule applied
     */
    public static Node<Token> removeMultiplicationsOfOne(Node<Token> node) {
        if (node.getContent() instanceof OperatorToken && node.getContent().getType() == OperatorToken.MUL) {
            Node<Token> child1 = node.getChildren().get(0);
            Node<Token> child2 = node.getChildren().get(1);
            if (child2.getContent() instanceof NumberToken && ((NumberToken) child2.getContent()).getDoubleValue() == 1) {
                return applyJFokRules(child1);
            } else if (child1.getContent() instanceof NumberToken && ((NumberToken) child1.getContent()).getDoubleValue() == 1) {
                return applyJFokRules(child2);
            }
        }
        return node;
    }

    /**
     * Replaces all instances of e ^ (1/2) to SQRT e
     *
     * @param node The node to apply this rule
     * @return The node with the rule applied
     */
    private static Node<Token> addSqrts(Node<Token> node) {
        if (node.getContent() instanceof OperatorToken && node.getContent().getType() == OperatorToken.POWER) {
            Node<Token> child1 = node.getChildren().get(0);
            Node<Token> child2 = node.getChildren().get(1);
            if (getValue(child2) == 0.5f) { //e ^ 0.5
                Node<Token> newRoot = new Node<Token>(FunctionFactory.makeSqrt());
                newRoot.addChild(child1);
                return newRoot;
            }
        }
        return node;
    }

    /**
     * @return The numerical value of this sub-expression, or Float.NaN is there is none (eg. has a variable)
     */
    private static float getValue(Node<Token> child2) {
        ArrayList<Token> expression = Utility.convertToRPN(traverseTree(child2));
        try {
            return (float) Utility.evaluateRPN(expression);
        } catch (IllegalArgumentException | EmptyStackException e) {
            return Float.NaN;
        }
    }

    /**
     * Changes the negative exponents into recipricols.
     *
     * @param node The node to apply this rule
     * @return The node with the negative exponents, if any, removed
     */
    private static Node<Token> removeNegativeExponents(Node<Token> node) {
        if (node.getContent() instanceof OperatorToken && node.getContent().getType() == OperatorToken.POWER) {
            Node<Token> exp = node.getChildren().get(1);
            if (isNegative(exp)) {
                node.getChildren().remove(exp);
                Node<Token> newExp = new Node<Token>(OperatorFactory.makeMultiply());
                newExp.addChild(new Node<Token>(new NumberToken(-1)));
                newExp.addChild(exp);
                node.addChild(newExp);
                return jFok(reciprocal(node));
            }
        }
        return node;
    }

    /**
     * Determines if the given sub-expression is negative (only for
     * single Variables and Numbers and purely numerical expressions only).
     *
     * @param node The root of the sub-expression
     * @return Whether or not the sub-expression is negative
     */
    private static boolean isNegative(Node<Token> node) {
        try { //Tries to doCalculate it as a purely numerical expression
            return getValue(node) < 0;
        } catch (IllegalArgumentException | EmptyStackException e) { //Not a purely numerical expression; tries doing it individually
            Token t = node.getContent();
            if (t instanceof VariableToken) {
                return ((VariableToken) t).isNegative();
            } else if (t instanceof NumberToken) {
                return ((NumberToken) t).getDoubleValue() < 0;
            }
            return false;
        }
    }

    /**
     * Applies specific rules using the commutative and associative properties
     * to prepare the tree for evaluateConstants().
     *
     * @param root The root of the tree
     * @return The root of the new tree
     */
    private static Node<Token> applyCommutativeProperty(Node<Token> root) {
        Token token = root.getContent();
        if (token instanceof OperatorToken) {
            OperatorToken o = (OperatorToken) token;
            Node<Token> childNode1 = root.getChildren().get(0);
            Node<Token> childNode2 = root.getChildren().get(1);
            Node<Token> interiorExpression;
            Token child1 = childNode1.getContent();
            Token child2 = childNode2.getContent();
            OperatorToken expression; //Operator of the interior subclass
            NumberToken n1;
            NumberToken n2;
            Token expressionChild1;
            Token expressionChild2;
            Node<Token> expressionNode1;
            Node<Token> expressionNode2;
            if (child1 instanceof OperatorToken && (child1.getType() == OperatorToken.ADD || child1.getType() == OperatorToken.SUB) && child2 instanceof NumberToken) {
                expression = (OperatorToken) child1;
                n1 = (NumberToken) child2;
                expressionNode1 = childNode1.getChildren().get(0);
                expressionNode2 = childNode1.getChildren().get(1);
                expressionChild1 = expressionNode1.getContent();
                expressionChild2 = expressionNode2.getContent();
            } else if (child2 instanceof OperatorToken && (child2.getType() == OperatorToken.ADD || child2.getType() == OperatorToken.SUB) && child1 instanceof NumberToken) {
                expression = (OperatorToken) child2;
                n1 = (NumberToken) child1;
                expressionNode1 = childNode2.getChildren().get(0);
                expressionNode2 = childNode2.getChildren().get(1);
                expressionChild1 = expressionNode1.getContent();
                expressionChild2 = expressionNode2.getContent();

            } else { //No rules apply
                return root;
            }
            //Finds the second number, if there is one
            if (expressionChild1 instanceof NumberToken || expressionChild2 instanceof NumberToken) {
                n2 = expressionChild1 instanceof NumberToken ? (NumberToken) expressionChild1 : (NumberToken) expressionChild2;
                interiorExpression = expressionChild1 instanceof NumberToken ? expressionNode2 : expressionNode1;
            } else { //Rule deos not apply
                return root;
            }
            if (o.getType() == OperatorToken.ADD) {
                //Now applies the appropriate rule
                if (expression.getType() == OperatorToken.ADD) { //(e + N1) + N2 -> e + (N1 + N2)
                    root = new Node<Token>(OperatorFactory.makeAdd());
                    //Creates the new subtree (N1 + N2)
                    Node<Token> addition = new Node<Token>(OperatorFactory.makeAdd());
                    addition.addChild(new Node<Token>(n1));
                    addition.addChild(new Node<Token>(n2));
                    //Evaluates the expression
                    addition = evaluateConstants(addition, false, true);
                    //Adds both the the root and sends it off
                    root.addChild(interiorExpression);
                    root.addChild(addition);
                    return root;
                } else if (expression.getType() == OperatorToken.SUB) { //(e - N1) + N2 -> e + (N2 - N1)
                    root = new Node<Token>(OperatorFactory.makeAdd());
                    //Creates the new subtree (N1 + N2)
                    Node<Token> subtract = new Node<Token>(OperatorFactory.makeSubtract());
                    subtract.addChild(new Node<Token>(n1));
                    subtract.addChild(new Node<Token>(n2));
                    //Evaluates the expression
                    subtract = evaluateConstants(subtract, false, true);
                    //Adds both the the root and sends it off
                    root.addChild(interiorExpression);
                    root.addChild(subtract);
                    return root;
                } else { //No rules apply
                    return root;
                }
            } else if (o.getType() == OperatorToken.SUB) { //(e + N1) - N2 -> e + (N1 - N2)
                //Now applies the appropriate rule
                if (expression.getType() == OperatorToken.ADD) { //(e + N1) - N2 -> e + (N1 - N2)
                    root = new Node<Token>(OperatorFactory.makeAdd());
                    //Creates the new subtree (N1 + N2)
                    Node<Token> subtract = new Node<Token>(OperatorFactory.makeSubtract());
                    subtract.addChild(new Node<Token>(n2));
                    subtract.addChild(new Node<Token>(n1));
                    //Evaluates the expression
                    subtract = evaluateConstants(subtract, false, true);
                    //Adds both the the root and sends it off
                    root.addChild(interiorExpression);
                    root.addChild(subtract);
                    return root;
                } else if (expression.getType() == OperatorToken.SUB) { //(e - N1) - N2 -> e - (N2 + N1)
                    root = new Node<Token>(OperatorFactory.makeSubtract());
                    //Creates the new subtree (N1 + N2)
                    Node<Token> addition = new Node<Token>(OperatorFactory.makeAdd());
                    addition.addChild(new Node<Token>(n1));
                    addition.addChild(new Node<Token>(n2));
                    //Evaluates the expression
                    addition = evaluateConstants(addition, false, true);
                    //Adds both the the root and sends it off
                    root.addChild(interiorExpression);
                    root.addChild(addition);
                    return root;
                } else { //No rules apply
                    return root;
                }
            } else { //No rules apply
                return root;
            }
        } else { //No rules apply
            return root;
        }
    }

    /**
     * Evaluate any constants such that any numerical expressions, as well as
     * functions, would be calculated.
     *
     * @param root                   The root of the subtree
     * @param exactValue             If true, this will not doCalculate square roots
     * @param doNotEvaluateFunctions If true, functions will not be evaluated unless they return an integer
     * @return The new root of the subtree
     * @throws IllegalArgumentException Invalid Expression
     */
    private static Node<Token> evaluateConstants(Node<Token> root, boolean exactValue, boolean doNotEvaluateFunctions) {
        Token token = root.getContent();
        //Evaluates this node
        if (root.getNumOfChildren() == 2) { //Operator
            //Now checks this node after the subtrees has been checked
            Token t1 = root.getChildren().get(0).getContent();
            Token t2 = root.getChildren().get(1).getContent();
            if (t1 instanceof NumberToken && t2 instanceof NumberToken && (!(token.getType() == OperatorToken.DIV || token.getType() == OperatorToken.FRACTION) || !exactValue)) { //Rule applies (deos not do divisions)
                //Both numbers, rule can be applied
                double result = ((OperatorToken) token).operate(((NumberToken) t1).getDoubleValue(), ((NumberToken) t2).getDoubleValue());
                return new Node<Token>(new NumberToken(result));
            } else { //Rule deos not apply
                return root;
            }
        } else if (root.getNumOfChildren() == 1) { //Function
            Token child = root.getChildren().get(0).getContent();
            //Changes PI and e to their numbers
            if (child instanceof VariableToken && child.getType() == VariableToken.PI) {
                child = new NumberToken(Math.PI);
            } else if (child instanceof VariableToken && child.getType() == VariableToken.e) {
                child = new NumberToken(Math.E);
            }
            FunctionToken f = (FunctionToken) token;
            if (child instanceof NumberToken) {
                double result = ((FunctionToken) token).perform(((NumberToken) child).getDoubleValue()).doubleValue();
                if (!doNotEvaluateFunctions || result % 1 == 0) {
                    return new Node<Token>(new NumberToken(result));
                } else {
                    return root;
                }
            } else {
                return root;
            }
        } else if (root.getNumOfChildren() == 0) { //Leaf
            return root;
        } else {
            throw new IllegalArgumentException(); //Invalid Expression
        }
    }

    /**
     * Applies all square root rules to any square roots found within the
     * function/
     *
     * @param root The root of the tree / subtree
     * @return The root of the resulting tree
     */
    private static Node<Token> applySquareRootRules(Node<Token> root) {
        //TODO: ALLOW IT TO APPLY RULES FOR MULTIPLES OF ROOTS (EG. 2SQRT2 * 3SQRT 3 -> 6SQRT6)
        Token token = root.getContent();
        if (token instanceof FunctionToken && token.getType() == FunctionToken.SQRT) { //A square root; rules may apply
            Token child = root.getChildren().get(0).getContent();
            if (child instanceof NumberToken) {
                double value = ((NumberToken) child).getDoubleValue();
                //Rules specificly for Numbers
                double result = ((FunctionToken) token).perform(value).doubleValue();
                if (result % 1 == 0) { //The result is a integer
                    return new Node<Token>(new NumberToken(result));
                } else { //Simplifies the square root
                    if (value % 1 == 0) { //Integer
                        int outside = 1;
                        int inside = (int) value;
                        int factor = 2;
                        while (factor * factor <= inside) {
                            if (inside % (factor * factor) == 0) {
                                inside /= factor * factor;
                                outside *= factor;
                            } else {
                                factor++;
                            }
                        }
                        if (outside != 1) { //It would be redundant to multiply it by 1
                            root = new Node<Token>(OperatorFactory.makeMultiply());
                            Node<Token> squareRoot = new Node<Token>(FunctionFactory.makeSqrt());
                            Node<Token> outsideNode = new Node<Token>(new NumberToken(outside));
                            Node<Token> insideNode = new Node<Token>(new NumberToken(inside));
                            squareRoot.addChild(insideNode);
                            root.addChild(outsideNode);
                            root.addChild(squareRoot);
                        }
                        return root;
                    } else {
                        //Decimal inside the square roots; just calculates it
                        return new Node<Token>(new NumberToken(((FunctionToken) token).perform(value)));
                    }
                }
            } else { //Root of a variable / expression
                //TODO: Add support to this
                return root;
            }
        } else {
            //Condenses square roots multiplying each other
            if (token instanceof OperatorToken && token.getType() == OperatorToken.MUL) {
                Node<Token> child1 = root.getChildren().get(0);
                Node<Token> child2 = root.getChildren().get(1);
                if (child1.getContent() instanceof FunctionToken && child1.getContent().getType() == FunctionToken.SQRT && child2.getContent() instanceof FunctionToken
                        && child2.getContent().getType() == FunctionToken.SQRT) { //Two square roots under a multiplication; Rule can be applied
                    Node<Token> node1 = child1.getChildren().get(0);
                    Node<Token> node2 = child2.getChildren().get(0);
                    root = new Node<Token>(FunctionFactory.makeSqrt());
                    Node<Token> multiply = new Node<Token>(OperatorFactory.makeMultiply());
                    multiply.addChild(node1);
                    multiply.addChild(node2);
                    multiply = evaluateConstants(multiply, true, true);
                    root.addChild(multiply);
                    root = applySquareRootRules(root); //In case it can be furthur simplified
                    return root;
                } else {
                    return root;
                }
            } else { //No changed made
                return root;
            }
        }
    }

    /**
     * Converts the given expression in postfix notation to a binary expression
     * tree.
     *
     * @param expression The expression in postfix
     * @return The root node of the tree
     * @throws IllegalArgumentException The given expression is invalid
     */
    public static Node<Token> setupTree(ArrayList<Token> expression) {
        Stack<Node<Token>> stack = new Stack<>();
        for (Token token : expression) {
            Node<Token> node = new Node<>(token);
            if (token instanceof VariableToken || token instanceof NumberToken) {
                stack.push(node);
            } else if (token instanceof OperatorToken) {
                Node<Token> node1 = stack.pop();
                Node<Token> node2 = stack.pop();
                node.addChild(node2);
                node.addChild(node1);
                stack.push(node);
            } else if (token instanceof FunctionToken) {
                Node<Token> child = stack.pop();
                node.addChild(child);
                stack.push(node);
            } else {
                //This should never be reached
                throw new IllegalArgumentException();
            }
        }
        if (stack.size() == 1) {
            return stack.pop();
        } else {
            //Invalid expression
            throw new IllegalArgumentException();
        }
    }

    /**
     * Alternative to traverse tree which won't create unnecessary brackets
     * Traverses the expression tree to return a infix expression.
     *
     * @param root The root node of the tree
     * @return The infix expression
     * @throws IllegalArgumentException Invalid tree
     */
    public static ArrayList<Token> traverseTree(Node<Token> root) {
        ArrayList<Token> toReturn = new ArrayList<>();
        if (root.getNumOfChildren() == 2) { //Tree not empty
            if (root.getContent() instanceof OperatorToken) { //Beginning of a sub-expression
                if (root.getChildren().get(0).getContent() instanceof OperatorToken && ((OperatorToken) root.getContent()).getPrecedence() > ((OperatorToken) root.getChildren().get(0).getContent()).getPrecedence()) {
                    toReturn.add(BracketFactory.makeOpenParentheses());
                    toReturn.addAll(traverseTree(root.getChildren().get(0)));
                    toReturn.add(BracketFactory.makeCloseParentheses());
                } else if (root.getChildren().get(0).getContent() instanceof OperatorToken && ((OperatorToken) root.getContent()).getPrecedence() == ((OperatorToken) root.getChildren().get(0).getContent()).getPrecedence()) {
                    if ((root.getContent().getType() == OperatorToken.DIV || root.getContent().getType() == OperatorToken.FRACTION) && root.getChildren().get(0).getContent().getType() == OperatorToken.MUL) {
                        toReturn.add(BracketFactory.makeOpenParentheses());
                        toReturn.addAll(traverseTree(root.getChildren().get(0)));
                        toReturn.add(BracketFactory.makeCloseParentheses());
                    } else {
                        toReturn.addAll(traverseTree(root.getChildren().get(0)));
                    }
                } else {
                    toReturn.addAll(traverseTree(root.getChildren().get(0)));
                }
                toReturn.add(root.getContent());
                if (root.getChildren().get(1).getContent() instanceof OperatorToken && ((OperatorToken) root.getContent()).getPrecedence() > ((OperatorToken) root.getChildren().get(1).getContent()).getPrecedence()) {
                    if (root.getContent().getType() == OperatorToken.POWER) {
                        toReturn.add(BracketFactory.makeSuperscriptOpen());
                        toReturn.addAll(traverseTree(root.getChildren().get(1)));
                        toReturn.add(BracketFactory.makeSuperscriptClose());
                    } else {
                        toReturn.add(BracketFactory.makeOpenParentheses());
                        toReturn.addAll(traverseTree(root.getChildren().get(1)));
                        toReturn.add(BracketFactory.makeCloseParentheses());
                    }
                } else if (root.getChildren().get(1).getContent() instanceof OperatorToken && ((OperatorToken) root.getContent()).getPrecedence() == ((OperatorToken) root.getChildren().get(1).getContent()).getPrecedence()) {
                    if ((root.getContent().getType() == OperatorToken.DIV || root.getContent().getType() == OperatorToken.FRACTION) && root.getChildren().get(1).getContent().getType() == OperatorToken.MUL) {
                        if (root.getContent().getType() == OperatorToken.POWER) {
                            toReturn.add(BracketFactory.makeSuperscriptOpen());
                            toReturn.addAll(traverseTree(root.getChildren().get(1)));
                            toReturn.add(BracketFactory.makeSuperscriptClose());
                        } else {
                            toReturn.add(BracketFactory.makeOpenParentheses());
                            toReturn.addAll(traverseTree(root.getChildren().get(1)));
                            toReturn.add(BracketFactory.makeCloseParentheses());
                        }
                    } else {
                        if (root.getContent().getType() == OperatorToken.POWER) {
                            toReturn.add(BracketFactory.makeSuperscriptOpen());
                            toReturn.addAll(traverseTree(root.getChildren().get(1)));
                            toReturn.add(BracketFactory.makeSuperscriptClose());
                        } else {
                            toReturn.addAll(traverseTree(root.getChildren().get(1)));
                        }
                    }
                } else {
                    if (root.getContent().getType() == OperatorToken.POWER) {
                        toReturn.add(BracketFactory.makeSuperscriptOpen());
                        toReturn.addAll(traverseTree(root.getChildren().get(1)));
                        toReturn.add(BracketFactory.makeSuperscriptClose());
                    } else {
                        toReturn.addAll(traverseTree(root.getChildren().get(1)));
                    }
                }
            }
            return toReturn;
        } else if (root.getNumOfChildren() == 1) { //Function
            toReturn.add(root.getContent());
            toReturn.add(BracketFactory.makeOpenParentheses());
            toReturn.addAll(traverseTree(root.getChildren().get(0)));
            toReturn.add(BracketFactory.makeCloseParentheses());
            return toReturn;
        } else if (root.getNumOfChildren() == 0) {
            toReturn.add(root.getContent());
            return toReturn;
        } else { //This should not happen
            throw new IllegalArgumentException();
        }
    }

    /**
     * Converts an expression tree in binary tree format into a multi-branch
     * tree
     *
     * @param root The original binary tree
     * @return An equivalent multi-branch tree
     */
    public static Node<Token> convToMultiBranch(Node<Token> root) {
        Node<Token> newRoot = new Node<Token>(root.getContent());
        if (root.getContent() instanceof OperatorToken && ((OperatorToken) root.getContent()).isCommutative()) {
            for (int i = 0; i < root.getNumOfChildren(); i++) {
                if (root.getChildren().get(i).getContent() instanceof OperatorToken && root.getChildren().get(i).getContent().getType() == root.getContent().getType()) {
                    newRoot.addChildren(convToMultiBranch(root.getChildren().get(i)).getChildren());
                } else if (root.getChildren().get(i).getContent() instanceof OperatorToken && root.getChildren().get(i).getContent().getType() == OperatorToken.SUB) {
                    newRoot.addChild(convToMultiBranch(root.getChildren().get(i)));
                } else {
                    newRoot.addChild(root.getChildren().get(i));
                }
            }
        } else if (root.getContent() instanceof OperatorToken && ((OperatorToken) root.getContent()).isAntiCommutative()) {
            if (root.getContent().getType() == OperatorToken.SUB) {
                newRoot = new Node<Token>(OperatorFactory.makeAdd());
                newRoot.addChild(convToMultiBranch(root.getChildren().get(0)));
                Node<Token> temp = new Node<Token>(OperatorFactory.makeMultiply());
                temp.addChild(new Node<Token>(new NumberToken(-1)));
                temp.addChild(convToMultiBranch(root.getChildren().get(1)));
                if (temp.getChildren().get(1).getContent() instanceof NumberToken) {
                    temp = new Node<Token>(new NumberToken(-1 * ((NumberToken) temp.getChildren().get(1).getContent()).getDoubleValue()));
                } else if (temp.getChildren().get(1).getContent() instanceof OperatorToken
                        && temp.getChildren().get(1).getContent().getType() == OperatorToken.MUL) {
                    Node<Token> temp1 = temp.getChildren().get(1);
                    Node<Token> temp1_0 = temp1.getChildren().get(0);
                    if (temp1_0.getContent() instanceof NumberToken) {
                        Node<Token> temp2 = new Node<Token>(new NumberToken(-1 * ((NumberToken) temp1_0.getContent()).getDoubleValue()));
                        temp = new Node<Token>(OperatorFactory.makeMultiply());
                        temp.addChild(temp2);
                        temp.addChild(temp1.getChildren().get(1));
                        if (temp.getChildren().get(0).getContent() instanceof NumberToken
                                && ((NumberToken) temp.getChildren().get(0).getContent()).getDoubleValue() == 1) {
                            temp = temp.getChildren().get(1);
                        }
                    }
                }

                newRoot.addChild(temp);
                return convToMultiBranch(newRoot);
            }
        } else if (root.getChildren().size() > 0) {
            for (int i = 0; i < root.getNumOfChildren(); i++) {
                newRoot.addChild(convToMultiBranch(root.getChildren().get(i)));
            }
        } else {
            newRoot = root;
        }
        return newRoot;
    }

    public static Node<Token> simplifyMultiBranch(Node<Token> root) {
        return addLikeTerms(groupLikeTerms(convToMultiBranch(root)));
    }

    /**
     * Converts an expression tree in multi-branch/general tree format into a
     * binary tree
     *
     * @param root The original multi-branch/general tree
     * @return An equivalent binary tree
     */
    public static Node<Token> convToBinary(Node<Token> root) {
        Node<Token> newRoot = new Node<>(root.getContent());
        if (root.getNumOfChildren() == 2) {
            newRoot.addChild(convToBinary(root.getChildren().get(0)));
            newRoot.addChild(convToBinary(root.getChildren().get(1)));
        } else if (root.getNumOfChildren() == 1) {
            if (root.getContent() instanceof OperatorToken) {
                newRoot = root.getChildren().get(0);
            } else {
                newRoot.addChild(convToBinary(root.getChildren().get(0)));
            }
        } else if (root.getNumOfChildren() == 0) {
            if (root.getContent() instanceof OperatorToken && root.getContent().getType() == OperatorToken.ADD) {
                newRoot = new Node<Token>(new NumberToken(0));
            } else if (root.getContent() instanceof OperatorToken && root.getContent().getType() == OperatorToken.MUL) {
                newRoot = new Node<Token>(new NumberToken(1));
            } else if (root.getContent() instanceof NumberToken) {
                return root;
            }
        } else {
            if (root.getContent() instanceof OperatorToken && (root.getContent().getType() == OperatorToken.ADD || root.getContent().getType() == OperatorToken.MUL)) {
                Node<Token> temp;
                if (root.getNumOfChildren() % 2 == 0) {
                    temp = new Node<>(root.getContent());
                    for (int i = 0; i < root.getNumOfChildren() / 2; i++) {
                        temp.addChild(root.getChildren().get(i));
                    }
                    newRoot.addChild(convToBinary(temp));
                    temp = new Node<>(root.getContent());
                    for (int i = root.getNumOfChildren() / 2; i < root.getNumOfChildren(); i++) {
                        temp.addChild(root.getChildren().get(i));
                    }
                    newRoot.addChild(convToBinary(temp));
                } else if (root.getNumOfChildren() % 2 != 0) {
                    temp = new Node<>(root.getContent());
                    Node<Token> temp2 = new Node<>(root.getContent());
                    temp2.addChild(convToBinary(root.getChildren().get(0)));
                    temp2.addChild(convToBinary(root.getChildren().get(1)));
                    temp.addChild(temp2);
                    int midpoint = (int) Math.floor(root.getNumOfChildren() / 2);
                    for (int i = 2; i < midpoint; i++) {
                        temp.addChild(root.getChildren().get(i));
                    }
                    newRoot.addChild(convToBinary(temp));
                    temp = new Node<>(root.getContent());
                    for (int i = midpoint + 1; i < root.getNumOfChildren(); i++) {
                        temp.addChild(root.getChildren().get(i));
                    }
                    newRoot.addChild(convToBinary(temp));
                }
            }
        }
        return newRoot;
    }

    /**
     * Removes unnecessary negatives beside addition symbols and rewrites them
     * as subtractions. Examples: 1 + -2 * X -> 1 - 2*X ; -2 / -3 -> 2/3
     *
     * @param expression The expression to remove negatives
     * @return The expression with negatives removed
     */
    private static ArrayList<Token> removeNegatives(ArrayList<Token> expression) {
        for (int i = 0; i < expression.size(); i++) {
            Token t = expression.get(i);
            Token before = i > 0 ? expression.get(i - 1) : null;
            if (t instanceof NumberToken && ((NumberToken) t).getDoubleValue() < 0 && before != null && before instanceof OperatorToken) { //Current token is a negative Number with Operator before it
                Token beforePrevious = i > 1 ? expression.get(i - 2) : null; //Before the previous
                NumberToken absVal = new NumberToken(Math.abs(((NumberToken) expression.get(i)).getDoubleValue()));
                if (before.getType() == OperatorToken.ADD) { //e + -N -> e - N
                    expression.set(i, absVal);
                    expression.set(i - 1, OperatorFactory.makeSubtract());
                } else if (before.getType() == OperatorToken.SUB) { //e - -N -> e + N
                    expression.set(i, absVal);
                    expression.set(i - 1, OperatorFactory.makeAdd());
                } else if (before.getType() == OperatorToken.MUL && beforePrevious != null && beforePrevious instanceof NumberToken
                        && ((NumberToken) beforePrevious).getDoubleValue() < 0) { // -N * -N -> N * N
                    NumberToken absVal2 = new NumberToken(Math.abs(((NumberToken) beforePrevious).getDoubleValue()));
                    expression.set(i - 2, absVal2);
                    expression.set(i, absVal);
                } else if (before.getType() == OperatorToken.MUL && beforePrevious != null && beforePrevious instanceof VariableToken
                        && ((VariableToken) beforePrevious).isNegative()) { // -N * -V -> N * V
                    ((VariableToken) beforePrevious).setNegative(false);
                    expression.set(i, absVal);
                }
            } else if (t instanceof VariableToken && ((VariableToken) t).isNegative() && before != null && before instanceof OperatorToken) {     //Rules for variables
                Token beforePrevious = i > 1 ? expression.get(i - 2) : null; //Before the previous
                if (before.getType() == OperatorToken.ADD) { //e + -N -> e - N
                    ((VariableToken) t).setNegative(false);
                    expression.set(i - 1, OperatorFactory.makeSubtract());
                } else if (before.getType() == OperatorToken.SUB) { //e - -N -> e + N
                    ((VariableToken) t).setNegative(false);
                    expression.set(i - 1, OperatorFactory.makeAdd());
                } else if (before.getType() == OperatorToken.MUL && beforePrevious != null && beforePrevious instanceof NumberToken
                        && ((NumberToken) beforePrevious).getDoubleValue() < 0) { // -V * -N -> V * N
                    NumberToken absVal = new NumberToken(Math.abs(((NumberToken) beforePrevious).getDoubleValue()));
                    expression.set(i - 2, absVal);
                    ((VariableToken) t).setNegative(false);
                } else if (before.getType() == OperatorToken.MUL && beforePrevious != null && beforePrevious instanceof VariableToken
                        && ((VariableToken) beforePrevious).isNegative()) { // -V * -V -> V * V
                    ((VariableToken) beforePrevious).setNegative(false);
                    ((VariableToken) t).setNegative(false);
                }
            }
        }
        return expression;
    }

    /**
     * Simplifies the given fraction such that there will be no common factors
     * between the numerator and the denominator.
     *
     * @param root The root of the tree of the fraction
     * @return The root of the simplified tree
     */
    private static Node<Token> simplifyFraction(Node<Token> root) {
        Node<Token> newRoot = root;
        if (root.getNumOfChildren() == 2) { //Tree not empty
            if (root.getContent() instanceof OperatorToken && (root.getContent().getType() == OperatorToken.DIV || root.getContent().getType() == OperatorToken.FRACTION)) { //Beginning of a sub-expression
                Node<Token> num = root.getChildren().get(0);
                Node<Token> denom = root.getChildren().get(1);
                Node<Token> temp;
                newRoot = new Node<Token>(OperatorFactory.makeDivide());
                if (num.getContent() instanceof NumberToken && denom.getContent() instanceof NumberToken) {
                    if (((NumberToken) num.getContent()).getDoubleValue() % 1 == 0 && ((NumberToken) denom.getContent()).getDoubleValue() % 1 == 0) {//checks to make sure num and denom are integers
                        double gcd = gcd(((NumberToken) num.getContent()).getDoubleValue(), ((NumberToken) denom.getContent()).getDoubleValue());
                        double numerator = ((NumberToken) num.getContent()).getDoubleValue() / gcd;
                        double denominator = ((NumberToken) denom.getContent()).getDoubleValue() / gcd;
                        num = new Node<Token>(new NumberToken(numerator));
                        denom = new Node<Token>(new NumberToken(denominator));
                    }
                } else if (num.getContent() instanceof OperatorToken && num.getContent().getType() == OperatorToken.MUL && denom.getContent() instanceof NumberToken) {
                    double gcd;
                    double numerator = 1;
                    double denominator = ((NumberToken) denom.getContent()).getDoubleValue();
                    int childNum;//which child branch was a number: -1 both, 0 the first, 1 the second, -2 neither
                    if (num.getChildren().get(0).getContent() instanceof NumberToken && num.getChildren().get(1).getContent() instanceof NumberToken) {
                        double product = ((NumberToken) num.getChildren().get(0).getContent()).getDoubleValue() * ((NumberToken) num.getChildren().get(1).getContent()).getDoubleValue();
                        if (product % 1 == 0) {
                            numerator = product;
                        }
                        childNum = -1;
                    } else if (num.getChildren().get(0).getContent() instanceof NumberToken && (((NumberToken) num.getChildren().get(0).getContent()).getDoubleValue() % 1 == 0)) {
                        numerator = ((NumberToken) num.getChildren().get(0).getContent()).getDoubleValue();
                        childNum = 0;
                    } else if (num.getChildren().get(1).getContent() instanceof NumberToken && (((NumberToken) num.getChildren().get(1).getContent()).getDoubleValue() % 1 == 0)) {
                        numerator = ((NumberToken) num.getChildren().get(1).getContent()).getDoubleValue();
                        childNum = 1;
                    } else {
                        childNum = -2;
                    }

                    gcd = gcd(numerator, denominator);
                    numerator /= gcd;
                    denominator /= gcd;
                    temp = num;
                    if (childNum == -1) {
                        num = new Node<Token>(new NumberToken(numerator));
                        /*
                         for(int i=0; i<temp.getNumOfChildren();i++){
                         num.addChild(temp.getChildren().get(i));
                         }*/
                    } else if (childNum == 0) {
                        num = new Node<Token>(OperatorFactory.makeMultiply());
                        num.addChild(new Node<Token>(new NumberToken(numerator)));
                        num.addChild(temp.getChildren().get(1));
                    } else if (childNum == 1) {
                        num = new Node<Token>(OperatorFactory.makeMultiply());
                        num.addChild(temp.getChildren().get(0));
                        num.addChild(new Node<Token>(new NumberToken(numerator)));
                    }
                    temp = denom;
                    denom = new Node<Token>(new NumberToken(denominator));
                    /*
                     for(int i=0; i<temp.getNumOfChildren();i++){
                     denom.addChild(temp.getChildren().get(i));
                     }*/
                } else if (denom.getContent() instanceof OperatorToken && denom.getContent().getType() == OperatorToken.MUL && num.getContent() instanceof NumberToken) {
                    double gcd;
                    double denominator = 1;
                    double numerator = ((NumberToken) num.getContent()).getDoubleValue();
                    double childNum;//which child branch was a number: -1 both, 0 the first, 1 the second, -2 neither
                    if (denom.getChildren().get(0).getContent() instanceof NumberToken && denom.getChildren().get(1).getContent() instanceof NumberToken) {
                        double product = ((NumberToken) denom.getChildren().get(0).getContent()).getDoubleValue() * ((NumberToken) denom.getChildren().get(1).getContent()).getDoubleValue();
                        if (product % 1 == 0) {
                            denominator = (int) product;
                        }
                        childNum = -1;
                    } else if (denom.getChildren().get(0).getContent() instanceof NumberToken && (((NumberToken) denom.getChildren().get(0).getContent()).getDoubleValue() % 1 == 0)) {
                        denominator = ((NumberToken) denom.getChildren().get(0).getContent()).getDoubleValue();
                        childNum = 0;
                    } else if (denom.getChildren().get(1).getContent() instanceof NumberToken && (((NumberToken) denom.getChildren().get(1).getContent()).getDoubleValue() % 1 == 0)) {
                        denominator = ((NumberToken) denom.getChildren().get(1).getContent()).getDoubleValue();
                        childNum = 1;
                    } else {
                        childNum = -2;
                    }
                    gcd = gcd(numerator, denominator);
                    numerator /= gcd;
                    denominator /= gcd;
                    temp = denom;
                    if (childNum == -1) {
                        denom = new Node<Token>(new NumberToken(denominator));
                        /*
                         for(int i=0; i<temp.getNumOfChildren();i++){
                         num.addChild(temp.getChildren().get(i));
                         }*/
                    } else if (childNum == 0) {
                        denom = new Node<Token>(OperatorFactory.makeMultiply());
                        denom.addChild(new Node<Token>(new NumberToken(denominator)));
                        denom.addChild(temp.getChildren().get(1));
                    } else if (childNum == 1) {
                        denom = new Node<Token>(OperatorFactory.makeMultiply());
                        denom.addChild(temp.getChildren().get(0));
                        denom.addChild(new Node<Token>(new NumberToken(denominator)));
                    }
                    temp = num;
                    num = new Node<Token>(new NumberToken(numerator));
                    /*
                     for(int i=0; i<temp.getNumOfChildren();i++){
                     num.addChild(temp.getChildren().get(i));
                     }*/
                } else if (denom.getContent() instanceof OperatorToken && denom.getContent().getType() == OperatorToken.MUL && num.getContent() instanceof OperatorToken && num.getContent().getType() == OperatorToken.MUL) {
                    for (int i = 0; i < denom.getNumOfChildren(); i++) {
                        for (int j = 0; j < num.getNumOfChildren(); j++) {
                            if (denom.getChildren().get(i) == num.getChildren().get(j)) {

                                temp = num;
                                for (int k = 0; k < temp.getNumOfChildren(); k++) {
                                    if (k != j) {
                                        num = temp.getChildren().get(k);
                                    }
                                }

                                temp = denom;
                                for (int k = 0; k < temp.getNumOfChildren(); k++) {
                                    if (k != i) {
                                        denom = temp.getChildren().get(k);
                                    }
                                }
                            }
                        }
                    }
                }
                newRoot.addChild(num);
                newRoot.addChild(denom);
            }
        } else if (root.getNumOfChildren() == 1) { //Function
            newRoot = new Node<>(root.getContent());
            newRoot.addChild(simplifyFraction(root.getChildren().get(0)));
        } else if (root.getNumOfChildren() == 0) {
            newRoot = root;
        } else { //This should not happen
            throw new IllegalArgumentException();
        }
        return newRoot;
    }

    /**
     * Determines the greatest common factor of two integers
     *
     * @param num   The numerator of a fraction
     * @param denom The denominator of a fraction
     * @return The greatest common factor of num and denom
     */
    private static double gcd(double num, double denom) {
        return denom == 0 ? num : gcd(denom, num % denom);
    }

    /**
     * Processes multiple fractions (such as 1/2/3) and re-writes it so that
     * there will be no more than one consecutive division. Example: 1 / 2 / 2
     * -> 1 / 4
     *
     * @param root      The root of the original expression
     * @param recursive The method to call if this rule is applied, (which method to recurse to)
     * @return The root of the simplified expression
     */
    private static Node<Token> processMultipleFractions(Node<Token> root, Command<Node<Token>, Node<Token>> recursive) {
        Node<Token> newRoot = root;
        if (root.getNumOfChildren() == 2) { //Tree not empty
            if (root.getContent() instanceof OperatorToken && (root.getContent().getType() == OperatorToken.DIV || root.getContent().getType() == OperatorToken.FRACTION)) { //main operation is division
                if (root.getChildren().get(1).getContent() instanceof OperatorToken && (root.getChildren().get(1).getContent().getType() == OperatorToken.DIV || root.getChildren().get(1).getContent().getType() == OperatorToken.FRACTION)) {//divisor(denominator) is a fraction
                    newRoot = new Node<Token>(OperatorFactory.makeMultiply());
                    newRoot.addChild(root.getChildren().get(0));
                    newRoot.addChild(reciprocal(root.getChildren().get(1)));
                    newRoot = recursive.execute(newRoot);
                } else if (root.getChildren().get(0).getContent() instanceof OperatorToken && ((root.getChildren().get(0).getContent().getType() == OperatorToken.DIV || root.getChildren().get(0).getContent().getType() == OperatorToken.FRACTION))) {//dividend(numerator) is a fraction
                    newRoot = new Node<Token>(OperatorFactory.makeDivide());
                    Node<Token> oldDividend = root.getChildren().get(0);
                    Node<Token> newDivisor = new Node<Token>(OperatorFactory.makeMultiply());
                    newDivisor.addChild(oldDividend.getChildren().get(1));
                    newDivisor.addChild(root.getChildren().get(1));
                    newRoot.addChild(oldDividend.getChildren().get(0));
                    newRoot.addChild(newDivisor);
                    newRoot = recursive.execute(newRoot);
                }
            }
        } else if (root.getNumOfChildren() == 1) { //Function
            newRoot = new Node<>(root.getContent());
            newRoot.addChild(processMultipleFractions(root.getChildren().get(0), recursive));
        } else if (root.getNumOfChildren() == 0) { //Just a number or a variable
            newRoot = root;
        } else { //This should not happen
            throw new IllegalArgumentException();
        }
        return newRoot;
    }

    /**
     * Converts a fraction into its reciprocal
     *
     * @param root The root of the original tree
     * @return The root of the new tree
     */
    private static Node<Token> reciprocal(Node<Token> root) {
        Node<Token> newRoot = new Node<Token>(OperatorFactory.makeDivide());
        if (root.getContent() instanceof OperatorToken && (root.getContent().getType() == OperatorToken.DIV || root.getContent().getType() == OperatorToken.FRACTION)) {
            newRoot.addChild(root.getChildren().get(1));
            newRoot.addChild(root.getChildren().get(0));
        } else {
            newRoot.addChild(new Node<Token>(new NumberToken(1)));
            newRoot.addChild(root);
        }
        return newRoot;
    }

    /**
     * Multiplies two expressions if one expression is a fraction and the other
     * may or may not be one.
     *
     * @param root      The root of the original tree
     * @param recursive A Command implementation that contains the method this will recurse to if the rule has been applied
     * @return The new root of the multiplied fraction tree
     */
    public static Node<Token> multiplyFractions(Node<Token> root, Command<Node<Token>, Node<Token>> recursive) {
        Token content = root.getContent();
        if (content instanceof OperatorToken && content.getType() == OperatorToken.MUL) {
            Node<Token> child1 = root.getChildren().get(0);
            Node<Token> child2 = root.getChildren().get(1);
            Token childT1 = child1.getContent();
            Token childT2 = child2.getContent();
            //N1/D1 * N2/D2 -> (N1 * N2) / (D1 * D2)
            if (childT1 instanceof OperatorToken && (childT1.getType() == OperatorToken.FRACTION || childT1.getType() == OperatorToken.DIV)
                    && childT2 instanceof OperatorToken && (childT2.getType() == OperatorToken.FRACTION || childT2.getType() == OperatorToken.DIV)) {
                Node<Token> n1 = child1.getChildren().get(0);
                Node<Token> n2 = child2.getChildren().get(0);
                Node<Token> d1 = child1.getChildren().get(1);
                Node<Token> d2 = child2.getChildren().get(1);

                Node<Token> newRoot = new Node<Token>(OperatorFactory.makeFraction());
                Node<Token> newNum = new Node<Token>(OperatorFactory.makeMultiply());
                Node<Token> newDenom = new Node<Token>(OperatorFactory.makeMultiply());

                newNum.addChild(n1);
                newNum.addChild(n2);
                newNum = recursive.execute(newNum);
                newDenom.addChild(d1);
                newDenom.addChild(d2);
                newDenom = recursive.execute(newDenom);

                newRoot.addChild(newNum);
                newRoot.addChild(newDenom);
                newRoot = simplifyFraction(newRoot);
                return newRoot;
                //N/D * e -> (e * N) / D
            } else if (childT1 instanceof OperatorToken && (childT1.getType() == OperatorToken.FRACTION || childT1.getType() == OperatorToken.DIV)) {
                Node<Token> n = child1.getChildren().get(0);
                Node<Token> d = child1.getChildren().get(1);
                Node<Token> e = child2;

                Node<Token> newRoot = new Node<Token>(OperatorFactory.makeFraction());
                Node<Token> newNum = new Node<Token>(OperatorFactory.makeMultiply());

                newNum.addChild(n);
                newNum.addChild(e);
                newNum = recursive.execute(newNum);

                newRoot.addChild(newNum);
                newRoot.addChild(d);
                newRoot = simplifyFraction(newRoot);
                return newRoot;
                //e * N/D -> (e * N) / D
            } else if (childT2 instanceof OperatorToken && (childT2.getType() == OperatorToken.FRACTION || childT2.getType() == OperatorToken.DIV)) {
                Node<Token> n = child2.getChildren().get(0);
                Node<Token> d = child2.getChildren().get(1);
                Node<Token> e = child1;

                Node<Token> newRoot = new Node<Token>(OperatorFactory.makeFraction());
                Node<Token> newNum = new Node<Token>(OperatorFactory.makeMultiply());

                newNum.addChild(n);
                newNum.addChild(e);
                newNum = recursive.execute(newNum);

                newRoot.addChild(newNum);
                newRoot.addChild(d);
                newRoot = simplifyFraction(newRoot);
                return newRoot;
            }
        }
        return root;
    }

    /**
     * Applies multiplication rules for multiplying terms/constants with
     * polynomials. Examples of such include 2 * (x + 1) -> 2 * x + 2 * 1, as
     * well as x * (x + 1) -> x * x + 1 * x. It should also work with division
     * as well, but treats all division as multiplication of fractions. Example:
     * (x + 1) / 2 -> 1/2 * x + 1/2 * 1 -> 1/2 * x + 1/2.
     *
     * @param root The root of the original tree
     * @return The new root of the new tree
     */
    private static Node<Token> multiplyTerms(Node<Token> root) {//TODO:Add support for negatives and functions
        Node<Token> newRoot = new Node<Token>(OperatorFactory.makeAdd());
        if (root.getContent() instanceof OperatorToken && (root.getContent().getType() == OperatorToken.DIV || root.getContent().getType() == OperatorToken.FRACTION)) {
            if (root.getChildren().get(0).getNumOfChildren() == 2) {
                Token child = root.getChildren().get(1).getContent();
                if (child instanceof NumberToken) {
                    //Rewrites E1 / E2 to E1 * 1/ E2
                    Node<Token> multiply = new Node<Token>(OperatorFactory.makeMultiply());
                    multiply.addChild(new Node<>(child));
                    Node<Token> divide = new Node<Token>(OperatorFactory.makeDivide());
                    divide.addChild(new Node<Token>(new NumberToken(1)));
                    divide.addChild(root.getChildren().get(1));
                    multiply.addChild(divide);
                    return multiplyTerms(multiply);
                }
            }
            return root;
        } else if (root.getContent() instanceof OperatorToken && root.getContent().getType() == OperatorToken.MUL) {
            if ((root.getChildren().get(0).getNumOfChildren() == 0 || root.getChildren().get(0).getNumOfChildren() == 1) && root.getChildren().get(1).getNumOfChildren() == 2
                    && root.getChildren().get(1).getContent() instanceof OperatorToken && (root.getChildren().get(1).getContent().getType() == OperatorToken.ADD
                    || root.getChildren().get(1).getContent().getType() == OperatorToken.SUB)) { //Ensures that one child is a single number and the other is a polynomial
                Node<Token> temp = root.getChildren().get(1);
                Node<Token> n1 = new Node<Token>(OperatorFactory.makeMultiply());
                n1.addChild(root.getChildren().get(0));
                n1.addChild(temp.getChildren().get(0));
                Node<Token> n2 = new Node<Token>(OperatorFactory.makeMultiply());
                n2.addChild(root.getChildren().get(0));
                n2.addChild(temp.getChildren().get(1));
                newRoot.addChild(n1);
                newRoot.addChild(n2);
                newRoot = expand(newRoot);
                return newRoot;
            } else if ((root.getChildren().get(1).getNumOfChildren() == 0 || root.getChildren().get(1).getNumOfChildren() == 1) && root.getChildren().get(0).getNumOfChildren() == 2
                    && root.getChildren().get(0).getContent() instanceof OperatorToken && (root.getChildren().get(0).getContent().getType() == OperatorToken.ADD
                    || root.getChildren().get(0).getContent().getType() == OperatorToken.SUB)) { //Ensures that one child is a single number and the other is a polynomial) {
                Node<Token> temp = root.getChildren().get(0);
                Node<Token> n1 = new Node<Token>(OperatorFactory.makeMultiply());
                n1.addChild(temp.getChildren().get(0));
                n1.addChild(root.getChildren().get(1));
                Node<Token> n2 = new Node<Token>(OperatorFactory.makeMultiply());
                n2.addChild(temp.getChildren().get(1));
                n2.addChild(root.getChildren().get(1));
                newRoot.addChild(n1);
                newRoot.addChild(n2);
                newRoot = expand(newRoot);
                return newRoot;
            } else if (root.getChildren().get(0).getNumOfChildren() == 2 && root.getChildren().get(1).getNumOfChildren() == 2) {
                return multiplyPolynomials(root);
            }
        }
        return root;
    }

    /**
     * Applies multiplication rules for multiple polynomials. These include
     * expressions such as (x - 1) * (x - 2) -> x * (x - 1) - 2 * (x - 2). It
     * should also treat divisions as multiplications of fractions.
     *
     * @param root The root of the original subtree
     * @return The new root of the new subtree
     */
    private static Node<Token> multiplyPolynomials(Node<Token> root) {
        Token rootToken = root.getContent();
        if (rootToken instanceof OperatorToken && rootToken.getType() == OperatorToken.MUL) { //Multiplication at top
            Node<Token> n1 = root.getChildren().get(0);
            Node<Token> n2 = root.getChildren().get(1);
            if (n1.getContent() instanceof OperatorToken && (n1.getContent().getType() == OperatorToken.ADD || n1.getContent().getType() == OperatorToken.SUB) //(T +- T) * (T +- T)
                    && n2.getContent() instanceof OperatorToken && (n2.getContent().getType() == OperatorToken.ADD || n2.getContent().getType() == OperatorToken.SUB)) {
                Node<Token> head = n1; //Keeps track on where the expression is being read from
                Node<Token> expression = n2;
                Node<Token> newExpression = new Node<>(head.getContent()); //Tracks the start of the expression
                Node<Token> newHead = newExpression; //Tracks where the current head for building the expression is
                boolean done = false;
                do {
                    Node<Token> child1 = head.getChildren().get(0);
                    Node<Token> child2 = head.getChildren().get(1);
                    Token t1 = child1.getContent();
                    Token t2 = child2.getContent();
                    if (t1 instanceof OperatorToken && (t1.getType() == OperatorToken.ADD || t1.getType() == OperatorToken.SUB)
                            && t2 instanceof OperatorToken && (t2.getType() == OperatorToken.ADD || t2.getType() == OperatorToken.SUB)) { //((T1A +- T1B) + (T2A +- T2B))
                        Node<Token> n = new Node<>(head.getContent());
                        //Rewrites ((T1 +- T2) O (T3 +- T4)) * (e) -> (T1 +- T2) * (e) O (T3 +- T4) * (e) (Distributive property)
                        Node<Token> multiply1 = new Node<Token>(OperatorFactory.makeMultiply());
                        Node<Token> multiply2 = new Node<Token>(OperatorFactory.makeMultiply());
                        multiply1.addChild(expression);
                        multiply1.addChild(child1);
                        multiply2.addChild(expression);
                        multiply2.addChild(child2);
                        //Calls itself again to expand the polynomials
                        n.addChild(multiplyPolynomials(multiply2));
                        n.addChild(multiplyPolynomials(multiply1));
                        //Replaces newHead with n
                        if (newHead.getParent() != null) {
                            Node parent = newHead.getParent();
                            parent.getChildren().remove(newHead);
                            parent.addChild(n);
                        } else {
                            newExpression = n;
                        }
                    } else if (t1 instanceof OperatorToken && (t1.getType() == OperatorToken.ADD || t1.getType() == OperatorToken.SUB)) { // (T1A +- T1B) +- T2
                        //Makes the subtree T2 * e
                        Node<Token> multiply = new Node<Token>(OperatorFactory.makeMultiply());
                        multiply.addChild(expression);
                        multiply.addChild(child2);
                        head = child1;
                        newHead.addChild(multiply);
                        Node<Token> futureHead = new Node<>(head.getContent());
                        newHead.addChild(futureHead);
                        newHead = futureHead;
                    } else if (t2 instanceof OperatorToken && (t2.getType() == OperatorToken.ADD || t2.getType() == OperatorToken.SUB)) { // (T2A +- T2B) +- T1
                        //Makes the subtree T1 * e
                        Node<Token> multiply = new Node<Token>(OperatorFactory.makeMultiply());
                        multiply.addChild(expression);
                        multiply.addChild(child1);
                        head = child2;
                        newHead.addChild(multiply);
                        Node<Token> futureHead = new Node<>(head.getContent());
                        newHead.addChild(futureHead);
                        newHead = futureHead;
                    } else {
                        Node<Token> multiply1 = new Node<Token>(OperatorFactory.makeMultiply());
                        Node<Token> multiply2 = new Node<Token>(OperatorFactory.makeMultiply());
                        multiply1.addChild(child1);
                        multiply1.addChild(expression);
                        multiply2.addChild(child2);
                        multiply2.addChild(expression);
                        newHead.addChild(multiply2);
                        newHead.addChild(multiply1);
                        root = newExpression;
                        root = expand(root); //Re-evaluates the expression as the entirety has changed
                        done = true;
                    }
                } while (!done);
            }
        }
        return root;
    }

    /**
     * Removes redundant multiplications of one, such as 1 * x -> x.
     *
     * @param expression The expression to remove the multiplications of one
     * @return The expression with multiplications of one removed
     */
    private static ArrayList<Token> removeMultiplicationsOfOne(ArrayList<Token> expression) {
        ArrayList<Token> newExpression = new ArrayList<>();
        for (int i = 0; i < expression.size(); i++) {
            //Safely assigns the Token variables to do ACCEPT_FRACTION searching
            Token before = i - 1 < 0 ? null : expression.get(i - 1);
            Token after = i + 1 > expression.size() - 1 ? null : expression.get(i + 1);
            Token current = expression.get(i);
            if (current instanceof OperatorToken && current.getType() == OperatorToken.MUL) { //Multiplication token found
                if (before instanceof NumberToken) { //1 * e -> e
                    if (((NumberToken) before).getDoubleValue() == 1) {
                        newExpression.remove(before);
                        //Removes the 1 Token and deos not add the * Token to the new expression
                    } else if (((NumberToken) before).getDoubleValue() == -1) {
                        newExpression.remove(before); //Replaces -1 * with -
                        newExpression.add(DigitFactory.makeNegative());
                    } else {
                        newExpression.add(current);
                    }
                } else if (after instanceof NumberToken) { //e * 1 -> e
                    if (((NumberToken) after).getDoubleValue() == 1) {
                        i++; //Skips the current * and the next 1
                    } else if (((NumberToken) after).getDoubleValue() == -1) {
                        int j = i - 1;
                        while (j >= 0 && (newExpression.get(j) instanceof OperatorToken || newExpression.get(j) instanceof FunctionToken
                                || (j > 0 && newExpression.get(j - 1) instanceof BracketToken && (newExpression.get(j - 1).getType() == BracketToken.PARENTHESES_OPEN || newExpression.get(j - 1).getType() == BracketToken.DENOMINATOR_OPEN
                                || newExpression.get(j - 1).getType() == BracketToken.NUMERATOR_OPEN || newExpression.get(j - 1).getType() == BracketToken.SUPERSCRIPT_OPEN)))) {
                            j--;
                        }
                        newExpression.add(j, DigitFactory.makeNegative()); //Adds a negative in front
                        i++; //Skips adding the * and the -1
                    } else {
                        newExpression.add(current);
                    }
                } else {
                    newExpression.add(current);
                }
            } else {
                newExpression.add(current);
            }
        }
        return newExpression;
    }

    /**
     * Removes redundant divisions of one, such as x / 1 -> x.
     *
     * @param expression The expression to remove the divisions of one
     * @return The expression with divisions of one removed
     */
    private static ArrayList<Token> removeDivisionsOfOne(ArrayList<Token> expression) {
        ArrayList<Token> newExpression = new ArrayList<>();
        for (int i = 0; i < expression.size(); i++) {
            //Safely assigns the Token variables to do ACCEPT_FRACTION searching
            Token before = i - 1 < 0 ? null : expression.get(i - 1);
            Token after = i + 1 > expression.size() - 1 ? null : expression.get(i + 1);
            Token current = expression.get(i);
            if (current instanceof OperatorToken && (current.getType() == OperatorToken.DIV || current.getType() == OperatorToken.FRACTION)) { //Div / Frac found
                if (after instanceof NumberToken) { //e / 1 -> e
                    if (((NumberToken) after).getDoubleValue() == 1) {
                        i++; //Skips the next 1
                    } else if (((NumberToken) after).getDoubleValue() == -1) {
                        int j = i - 1;
                        while (j >= 0 && (newExpression.get(j) instanceof OperatorToken || newExpression.get(j) instanceof FunctionToken
                                || (j > 0 && newExpression.get(j - 1) instanceof BracketToken && (newExpression.get(j - 1).getType() == BracketToken.PARENTHESES_OPEN || newExpression.get(j - 1).getType() == BracketToken.DENOMINATOR_OPEN
                                || newExpression.get(j - 1).getType() == BracketToken.NUMERATOR_OPEN || newExpression.get(j - 1).getType() == BracketToken.SUPERSCRIPT_OPEN)))) {
                            j--;
                        }
                        newExpression.add(j, DigitFactory.makeNegative()); //Adds a negative in front
                        i++; //Skips adding the * and the -1
                    } else {
                        newExpression.add(current);
                    }
                } else {
                    newExpression.add(current);
                }
            } else {
                newExpression.add(current);
            }
        }
        return newExpression;
    }

    /**
     * Groups like terms so that all like terms will be children of the same
     * parent
     *
     * @param root The root node of the expression tree (in general tree form)
     * @return Returns the root of a new expression tree with grouped like terms
     */
    public static Node<Token> groupLikeTerms(Node<Token> root) {
        Node<Token> newRoot = new Node<>(root.getContent());
        Node<Token> temp = new Node<>(root.getContent());
        Node<Token> first;
        for (int i = 0; i < root.getNumOfChildren(); i++) {
            first = root.getChildren().get(i).copy();
            for (int j = 0; j < root.getNumOfChildren(); j++) {
                if (areLikeTerms(first, root.getChildren().get(j))) {
                    temp.addChild(root.getChildren().get(j));
                    root.delChild(j);
                    j--;
                }
            }
            newRoot.addChild(temp);
            temp = new Node<Token>(OperatorFactory.makeAdd());
        }
        if (newRoot.getNumOfChildren() == 1) {
            return newRoot.getChildren().get(0);
        } else {
            return newRoot;
        }
    }

    /**
     * Checks if two nodes represent like terms
     *
     * @param term1 the first term
     * @param term2 the second term
     * @return Returns true if term1 and term2 are like terms
     */
    private static boolean areLikeTerms(Node<Token> term1, Node<Token> term2) {
        if (term1.getContent() instanceof OperatorToken && term1.getContent().getType() == OperatorToken.MUL
                && term2.getContent() instanceof OperatorToken && term2.getContent().getType() == OperatorToken.MUL) {
            for (int i = 0; i < term1.getNumOfChildren(); i++) {
                for (int j = 0; j < term2.getNumOfChildren(); j++) {
                    if (traverseTree(term1.getChildren().get(i)).equals(traverseTree(term2.getChildren().get(j)))) {
                        return true;
                    }
                }
            }
        } else if (term1.getContent() instanceof OperatorToken && term1.getContent().getType() == OperatorToken.MUL) {
            for (int i = 0; i < term1.getNumOfChildren(); i++) {
                if (traverseTree(term1.getChildren().get(i)).equals(traverseTree(term2))) {
                    return true;
                }
            }
        } else if (term2.getContent() instanceof OperatorToken && term2.getContent().getType() == OperatorToken.MUL) {
            for (int i = 0; i < term2.getNumOfChildren(); i++) {
                if (traverseTree(term2.getChildren().get(i)).equals(traverseTree(term1))) {
                    return true;
                }
            }
        } else if (term1.getContent() instanceof NumberToken && term2.getContent() instanceof NumberToken) {
            return true;
        }
        return term1.getContent() instanceof VariableToken && term2.getContent() instanceof VariableToken
                && term1.getContent().getType() == term2.getContent().getType();
    }

    /**
     * Adds the two given like terms together
     *
     * @param term1 The root of the first term
     * @param term2 The root of the second term
     * @return The root of the expression with the like terms added
     */
    private static Node<Token> add2LikeTerms(Node<Token> term1, Node<Token> term2) {
        Node<Token> newRoot = null;
        if (term1.getContent() instanceof OperatorToken && term1.getContent().getType() == OperatorToken.MUL) {
            newRoot = new Node<Token>(OperatorFactory.makeMultiply());
            int e1Pos = -1, e2Pos = -1;
            Node<Token> temp1 = null;
            Node<Token> temp2 = null;
            Node<Token> temp = new Node<Token>(OperatorFactory.makeAdd());
            Node<Token> exp = new Node<Token>(new NumberToken(1));
            for (int i = 0; i < term1.getNumOfChildren(); i++) {
                for (int j = 0; j < term2.getNumOfChildren(); j++) {
                    if (traverseTree(term1.getChildren().get(i)).equals(traverseTree(term2.getChildren().get(j))) && i != j) {
                        exp = term1.getChildren().get(i);
                        e1Pos = i;
                        e2Pos = j;
                        i += term1.getNumOfChildren();
                        j += term2.getNumOfChildren();//stops loop
                    }
                }
            }
            if (e1Pos < 0 || e2Pos < 0) {
                throw new IllegalArgumentException("Not like terms");
            }
            for (int i = 0; i < term1.getNumOfChildren(); i++) {
                if (i != e1Pos) {
                    temp1 = new Node<Token>(OperatorFactory.makeMultiply());
                    temp1.addChild(term1.getChildren().get(i));
                }
            }
            for (int i = 0; i < term2.getNumOfChildren(); i++) {
                if (i != e2Pos) {
                    temp2 = new Node<Token>(OperatorFactory.makeMultiply());
                    temp2.addChild(term2.getChildren().get(i));
                }
            }
            temp.addChild(temp1);
            temp.addChild(temp2);
            newRoot.addChild(temp);
            newRoot.addChild(exp);
        } else if (term1.getContent() instanceof NumberToken && term2.getContent() instanceof NumberToken) {
            double sum = ((NumberToken) term1.getContent()).getDoubleValue() + ((NumberToken) term2.getContent()).getDoubleValue();
            newRoot = new Node<Token>(new NumberToken(sum));
        } else if (term1.getNumOfChildren() == 1) {
            return add2LikeTerms(term1.getChildren().get(0), term2);
        } else if (term2.getNumOfChildren() == 1) {
            return add2LikeTerms(term1, term2.getChildren().get(0));
        }
        return newRoot;
    }

    /**
     * Adds together the like terms in an expression. Example: x + x -> 2 * x or
     * 2 * x ^ 3 + 3 * x ^ 3 -> 5 * x ^ 3.
     *
     * @param root The root of a multi-branch/general tree representing the
     *             expression
     * @return The root of the expression with the like terms added
     */
    public static Node<Token> addLikeTerms(Node<Token> root) {//TODO: rewrite -> just add adjacent terms recursively
        Node<Token> newRoot = new Node<>(root.getContent());
        Node<Token> first = root.getChildren().get(0);
        Node<Token> rest = new Node<>(root.getContent());
        rest.addChildren(new ArrayList<>(root.getChildren().subList(1, root.getNumOfChildren())));
        if (root.getContent() instanceof OperatorToken && root.getContent().getType() == OperatorToken.ADD) {
            if (rest.getNumOfChildren() == 1) {
                return rest.getChildren().get(0);
            } else if (rest.getNumOfChildren() == 2) {
                return add2LikeTerms(first, add2LikeTerms(rest.getChildren().get(0), rest.getChildren().get(1)));
            } else {
                return add2LikeTerms(first, addLikeTerms(rest));
            }
        } else {
            return root;
        }
    }

    /**
     * Adds all fractions in a given expression together.
     *
     * @param root The expression with the fractions to sum
     * @return The resulting expression
     */
    public static Node<Token> addLikeFractions(Node<Token> root) {
        Token t = root.getContent();
        if (t instanceof OperatorToken && (t.getType() == OperatorToken.ADD || t.getType() == OperatorToken.SUB)) {
            Node<Token> o = root;
            Node<Token> child1 = o.getChildren().get(0);
            Node<Token> child2 = o.getChildren().get(1);
            Token content1 = child1.getContent();
            Token content2 = child2.getContent();
            //CASE 1: N1/D1 +- N2/D2 -> (N1*D2+N2*D1)/(D1*D2)
            if (content1 instanceof OperatorToken && (content1.getType() == OperatorToken.DIV || content1.getType() == OperatorToken.FRACTION)
                    && content2 instanceof OperatorToken && (content2.getType() == OperatorToken.DIV || content2.getType() == OperatorToken.FRACTION)) {
                Node<Token> num1 = child1.getChildren().get(0);
                Node<Token> num2 = child2.getChildren().get(0);
                Node<Token> denom1 = child1.getChildren().get(1);
                Node<Token> denom2 = child2.getChildren().get(1);

                Node<Token> newFrac = new Node<Token>(OperatorFactory.makeFraction());
                Node<Token> newNum = new Node<Token>(o.getContent());
                Node<Token> newDenom = new Node<Token>(OperatorFactory.makeMultiply());

                Node<Token> mult1 = new Node<Token>(OperatorFactory.makeMultiply());
                Node<Token> mult2 = new Node<Token>(OperatorFactory.makeMultiply());
                mult1.addChild(num1);
                mult1.addChild(denom2);
                mult1 = simplify(mult1);
                mult2.addChild(num2);
                mult2.addChild(denom1);
                mult2 = simplify(mult2);
                newNum.addChild(mult1);
                newNum.addChild(mult2);
                newNum = simplify(newNum);

                newDenom.addChild(denom1);
                newDenom.addChild(denom2);
                newDenom = simplify(newDenom);

                newFrac.addChild(newNum);
                newFrac.addChild(newDenom);
                newFrac = simplifyFraction(newFrac);
                return newFrac;
                //CASE 2: N/D+-e -> (N+-e*D)/D
            } else if (content1 instanceof OperatorToken && (content1.getType() == OperatorToken.DIV || content1.getType() == OperatorToken.FRACTION)) {
                Node<Token> frac = child1;
                Node<Token> expression = child2;
                Node<Token> num = frac.getChildren().get(0);
                Node<Token> denom = frac.getChildren().get(1);

                Node<Token> newFrac = new Node<Token>(OperatorFactory.makeFraction());
                Node<Token> newNum = new Node<>(o.getContent());
                Node<Token> mult = new Node<Token>(OperatorFactory.makeMultiply());

                mult.addChild(expression);
                mult.addChild(denom);
                mult = simplify(mult);
                newNum.addChild(num);
                newNum.addChild(mult);
                newNum = simplify(newNum);

                newFrac.addChild(newNum);
                newFrac.addChild(denom);
                newFrac = simplifyFraction(newFrac);
                return newFrac;
                //CASE 3: e+-N/D -> (e*D+-N)/D
            } else if (content2 instanceof OperatorToken && (content2.getType() == OperatorToken.DIV || content2.getType() == OperatorToken.FRACTION)) {
                Node<Token> frac = child2;
                Node<Token> expression = child1;
                Node<Token> num = frac.getChildren().get(0);
                Node<Token> denom = frac.getChildren().get(1);

                Node<Token> newFrac = new Node<Token>(OperatorFactory.makeFraction());
                Node<Token> newNum = new Node<>(o.getContent());
                Node<Token> mult = new Node<Token>(OperatorFactory.makeMultiply());

                mult.addChild(expression);
                mult.addChild(denom);
                mult = simplify(mult);
                newNum.addChild(num);
                newNum.addChild(mult);
                newNum = simplify(newNum);

                newFrac.addChild(newNum);
                newFrac.addChild(denom);
                newFrac = simplifyFraction(newFrac);
                return newFrac;
            } else {
                return root;
            }
        }
        return root;
    }

    /**
     * Multiplies and divides variables and transforms them into powers instead.
     * Example: x * x -> x ^ 2.
     *
     * @param root The root of the original subtree
     * @return The new subtree
     */
    private static Node<Token> multiplyVariables(Node<Token> root) {
        Token current = root.getContent();
        if (current instanceof OperatorToken) {
            OperatorToken o = (OperatorToken) current;
            Node<Token> childNode1 = root.getChildren().get(0);
            Node<Token> childNode2 = root.getChildren().get(1);
            Token child1 = childNode1.getContent();
            Token child2 = childNode2.getContent();
            if (child1 instanceof VariableToken && child2 instanceof VariableToken) { //VARVAR
                VariableToken v1 = (VariableToken) child1;
                VariableToken v2 = (VariableToken) child2;
                if (v1.getType() == v2.getType()) { //Checks to make sure it's the same type
                    if (o.getType() == OperatorToken.MUL) {
                        //Applies rule V * V -> V ^ 2
                        Node<Token> newRoot = new Node<Token>(OperatorFactory.makePower());
                        newRoot.addChild(new Node<Token>(v1));
                        newRoot.addChild(new Node<Token>(new NumberToken(2)));
                        return newRoot;
                    } else if (o.getType() == OperatorToken.DIV || o.getType() == OperatorToken.FRACTION) {
                        //Applies rule V/V -> 1
                        return new Node<Token>(new NumberToken(1));
                    } else { //No rules apply
                        return root;
                    }
                } else { //Not the same variable; rule deos not apply
                    return root;
                }
            } else if (child1 instanceof OperatorToken && child1.getType() == OperatorToken.POWER && child2 instanceof OperatorToken
                    && child2.getType() == OperatorToken.POWER) { //EXPEXP
                Node<Token> node1 = childNode1.getChildren().get(0);
                Node<Token> node2 = childNode2.getChildren().get(1);
                Token exp1Child2 = childNode1.getChildren().get(0).getContent();
                Token exp2Child2 = childNode2.getChildren().get(1).getContent();
                VariableToken v1 = exp1Child2 instanceof VariableToken ? (VariableToken) exp1Child2 : null;
                VariableToken v2 = exp2Child2 instanceof VariableToken ? (VariableToken) exp2Child2 : null;
                if (v1 != null && v2 != null && v1.getType() == v2.getType()) { //Rule applies
                    if (o.getType() == OperatorToken.DIV || o.getType() == OperatorToken.FRACTION) {
                        //Applies rule (V ^ E1) / (V ^ E2) -> V ^ (E1 - E2)
                        Node<Token> head = new Node<Token>(OperatorFactory.makePower());
                        Node<Token> subtract = new Node<Token>(OperatorFactory.makeSubtract());
                        subtract.addChild(node1);
                        subtract.addChild(node2);
                        head.addChild(new Node<Token>(v1));
                        head.addChild(subtract);
                        return head;
                    } else if (o.getType() == OperatorToken.MUL) {
                        //Applies rule (V ^ E1) * (V ^ E2) -> V ^ (E1 + E2)
                        Node<Token> head = new Node<Token>(OperatorFactory.makePower());
                        Node<Token> add = new Node<Token>(OperatorFactory.makeAdd());
                        add.addChild(node1);
                        add.addChild(node2);
                        head.addChild(new Node<Token>(v1));
                        head.addChild(add);
                        return head;
                    } else { //No rules apply
                        return root;
                    }
                } else { //No rules apply
                    return root;
                }
            } else if (child1 instanceof VariableToken && child2 instanceof OperatorToken && ((OperatorToken) child2).getType() == OperatorToken.POWER) { //VAREXP
                VariableToken v = (VariableToken) child1;
                Node<Token> exponent = childNode2;
                Token t = exponent.getChildren().get(0).getContent();
                if (t instanceof VariableToken && v.getType() == t.getType()) { //Rule applies
                    Node<Token> node = exponent.getChildren().get(1);
                    if (o.getType() == OperatorToken.DIV || o.getType() == OperatorToken.FRACTION) {
                        //Applies rule V / V ^ e -> V ^ (1 - e)
                        Node<Token> head = new Node<Token>(OperatorFactory.makePower());
                        head.addChild(new Node<Token>(v));
                        Node<Token> subtract = new Node<Token>(OperatorFactory.makeSubtract());
                        subtract.addChild(new Node<Token>(new NumberToken(1)));
                        subtract.addChild(node);
                        head.addChild(subtract);
                        head = expand(head);
                        return head;
                    } else if (o.getType() == OperatorToken.MUL) {
                        //Applies rule V * V ^ e -> V ^ (1 + e)
                        Node<Token> head = new Node<Token>(OperatorFactory.makePower());
                        head.addChild(new Node<Token>(v));
                        Node<Token> add = new Node<Token>(OperatorFactory.makeAdd());
                        add.addChild(new Node<Token>(new NumberToken(1)));
                        add.addChild(node);
                        head.addChild(add);
                        head = expand(head);
                        return head;
                    } else { //No rules applies
                        return root;
                    }
                } else { //Rule deos not apply
                    return root;
                }
            } else if (child2 instanceof VariableToken && child1 instanceof OperatorToken && ((OperatorToken) child1).getType() == OperatorToken.POWER) { //EXPVAR
                VariableToken v = (VariableToken) child2;
                Node<Token> exponent = childNode1;
                Token t = (exponent.getChildren().get(0)).getContent();
                if (t instanceof VariableToken && v.getType() == t.getType()) { //Rule applies
                    Node<Token> node = exponent.getChildren().get(1);
                    if (o.getType() == OperatorToken.DIV || o.getType() == OperatorToken.FRACTION) {
                        //Applies rule V / V ^ e -> V ^ (1 - e)
                        Node<Token> head = new Node<Token>(OperatorFactory.makePower());
                        head.addChild(new Node<Token>(v));
                        Node<Token> subtract = new Node<Token>(OperatorFactory.makeSubtract());
                        subtract.addChild(new Node<Token>(new NumberToken(1)));
                        subtract.addChild(node);
                        head.addChild(subtract);
                        head = expand(head);
                        return head;
                    } else if (o.getType() == OperatorToken.MUL) {
                        //Applies rule V * V ^ e -> V ^ (1 + e)
                        Node<Token> head = new Node<Token>(OperatorFactory.makePower());
                        head.addChild(new Node<Token>(v));
                        Node<Token> add = new Node<Token>(OperatorFactory.makeAdd());
                        add.addChild(new Node<Token>(new NumberToken(1)));
                        add.addChild(node);
                        head.addChild(add);
                        head = expand(head);
                        return head;
                    } else { //No rules applies
                        return root;
                    }
                } else { //Rule deos not apply
                    return root;
                }
            } else { //No rules apply
                return root;
            }
        } else {
            return root;
        }
    }

    /**
     * Applies powers to polynomial expression and transforms them into
     * multiplications. Example: (x + 1) ^ 2 -> (x + 1) * (x + 1). This also
     * takes into consideration powers of zeros and negative exponents as well.
     *
     * @param root The root of the original expression
     * @return The new root of the expression
     */
    private static Node<Token> applyPowers(Node<Token> root) {
        Token current = root.getContent();
        Token child1 = root.getChildren().get(1).getContent();
        Token child2 = root.getChildren().get(0).getContent();
        if (current instanceof OperatorToken && current.getType() == OperatorToken.POWER && child1 instanceof NumberToken && child2 instanceof OperatorToken) { //O ^ N rule applies
            double n = ((NumberToken) child1).getDoubleValue();
            Node<Token> expression = root.getChildren().get(0);
            if (n % 1 != 0) {
                //TODO: Must be an integer; FIND A WAY TO HAVE THIS WORKING
                throw new IllegalArgumentException("Power must be an integer");
            }
            if (n < 0) {
                Node<Token> head = new Node<Token>(OperatorFactory.makeDivide());
                head.addChild(new Node<Token>(new NumberToken(1)));
                n *= -1;
                if (n == 1) {
                    head.addChild(expression);
                } else { //n > 1
                    n -= 2;
                    Node<Token> head2 = new Node<Token>(OperatorFactory.makeMultiply());
                    head2.addChild(expression); //NOTE: No need to clone
                    head2.addChild(expression);
                    while (n > 0) {
                        Node<Token> newHead = new Node<Token>(OperatorFactory.makeMultiply());
                        newHead.addChild(expression);
                        newHead.addChild(head2);
                        head2 = newHead;
                        n--;
                    }
                    head.addChild(head2);
                }
                return head;
            } else if (n == 0) {
                return new Node<Token>(new NumberToken(1)); //Anything ^ 0 = 1
            } else if (n == 1) {
                return expression; //Simply removes the ^ 1
            } else { //n > 2
                n -= 2;
                Node<Token> head = new Node<Token>(OperatorFactory.makeMultiply());
                head.addChild(expression); //NOTE: No need to clone
                head.addChild(expression);
                while (n > 0) {
                    Node<Token> newHead = new Node<Token>(OperatorFactory.makeMultiply());
                    newHead.addChild(expression);
                    newHead.addChild(head);
                    head = newHead;
                    n--;
                }
                return head;
            }
        } else {
            return root;
        }
    }

    /**
     * Uses the distributive property to group similar types of token that are
     * multiplied or divided together to set them up for multiplyTerms(),
     * evaluateConstants(), multiplyPolynomials() and multiplyVariables().
     *
     * @param root The root of the original function
     * @return The root of the new function
     */
    private static Node<Token> applyDistributiveProperty(Node<Token> root) {
        ArrayList<Node<Token>> multiplications = new ArrayList<>();
        ArrayList<Node> divisions = new ArrayList<>();
        Stack<Node<Token>> stack = new Stack<>(); //Things to read for multiplications / divisions
        stack.push(root);
        //Fills the lists of multiplications and divisions
        do {
            Node<Token> readHead = stack.pop();
            if (readHead.getContent() instanceof OperatorToken) {
                OperatorToken o = (OperatorToken) readHead.getContent();
                if (o.getType() == OperatorToken.MUL) {
                    Node<Token> child1 = readHead.getChildren().get(0);
                    Node<Token> child2 = readHead.getChildren().get(1);
                    Token t1 = child1.getContent();
                    Token t2 = child2.getContent();
                    if (t1 instanceof OperatorToken && (t1.getType() == OperatorToken.MUL || (t1.getType() == OperatorToken.DIV || t1.getType() == OperatorToken.FRACTION))
                            && t2 instanceof OperatorToken && (t2.getType() == OperatorToken.MUL || (t2.getType() == OperatorToken.DIV || t2.getType() == OperatorToken.FRACTION))) { //Both children have multiplication / divisions
                        stack.push(child1);
                        stack.push(child2);
                    } else if (t1 instanceof OperatorToken && (t1.getType() == OperatorToken.MUL || (t1.getType() == OperatorToken.DIV || t1.getType() == OperatorToken.FRACTION))) { //More multiplications / divisions on T1
                        multiplications.add(child2);
                        stack.push(child1);
                    } else if (t2 instanceof OperatorToken && (t2.getType() == OperatorToken.MUL || (t2.getType() == OperatorToken.DIV || t2.getType() == OperatorToken.FRACTION))) { //More multiplications / divisions on T2
                        multiplications.add(child1);
                        stack.push(child2);
                    } else { //No more multiplications or divisions
                        multiplications.add(child1);
                        multiplications.add(child2);
                    }
                } else if (o.getType() == OperatorToken.DIV || o.getType() == OperatorToken.FRACTION) { //TODO: APPLY SPECIAL CASES FOR DIVISIONS
                    Node<Token> child1 = readHead.getChildren().get(0);
                    Node<Token> child2 = readHead.getChildren().get(1);
                    Token t1 = child1.getContent();
                    Token t2 = child2.getContent();
                    if (t1 instanceof OperatorToken && (t1.getType() == OperatorToken.MUL || (t1.getType() == OperatorToken.DIV || t1.getType() == OperatorToken.FRACTION))
                            && t2 instanceof OperatorToken && (t2.getType() == OperatorToken.MUL || (t2.getType() == OperatorToken.DIV || t2.getType() == OperatorToken.FRACTION))) { //Both children have multiplication / divisions
                    } else if (t1 instanceof OperatorToken && (t1.getType() == OperatorToken.MUL || (t1.getType() == OperatorToken.DIV || t1.getType() == OperatorToken.FRACTION))) { //More multiplications / divisions on T1
                    } else if (t2 instanceof OperatorToken && (t2.getType() == OperatorToken.MUL || (t2.getType() == OperatorToken.DIV || t2.getType() == OperatorToken.FRACTION))) { //More multiplications / divisions on T2
                    } else { //No more multiplications or divisions
                    }
                } else {
                    //Do nothing; NOT a multiplication or division
                }
            } else {
                ///Do nothing; NOT an operator
            }
        } while (!stack.isEmpty());
        if (!multiplications.isEmpty() && !divisions.isEmpty()) {
            //Now builds the sorts the multiplications according to type
            ArrayList<Node<Token>> variables = new ArrayList<>();
            ArrayList<Node<Token>> constants = new ArrayList<>();
            ArrayList<Node<Token>> polynomials = new ArrayList<>();
            ArrayList<Node<Token>> others = new ArrayList<>();
            for (Node<Token> node : multiplications) {
                if (node.getContent() instanceof VariableToken) {
                    variables.add(node);
                } else if (node.getContent() instanceof NumberToken) {
                    constants.add(node);
                } else if (node.getContent() instanceof OperatorToken) {
                    polynomials.add(node);
                } else { //Group the others together
                    others.add(node);
                }
            }
        } else {
            return root; //No multiplications of divisions found
        }
        return root;
    }
}
