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

package com.duy.casiofx.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;

import com.duy.casiofx.factory.BracketFactory;
import com.duy.casiofx.factory.FunctionToken;
import com.duy.casiofx.token.BracketToken;
import com.duy.casiofx.token.MatrixToken;
import com.duy.casiofx.token.OperatorToken;
import com.duy.casiofx.token.Token;
import com.duy.casiofx.util.Utility;
import com.duy.naturalview.R;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Contains the framework of a View that displays the given mathematical expression with
 * superscripts and fraction supports.
 * <p>
 * This View also supports text modifications within the Token's String itself. Use the following
 * Unicode symbols to toggle it (eg.  N☺A☺ for a subscript A)
 * ☺  - Subscript
 *
 * @author Alston Lin
 * @version 3.0
 */
public abstract class NaturalView extends View {

    //Constant ratios of variables to the text height value
    public static final float RESERVED_TO_ACTUAL_TEXT_HEIGHT = 5 / 4f; // The ratio of the height of the space reserved for text to actually used
    public static final float X_PADDING_TO_TEXT_HEIGHT = 1 / 3f;
    public static final float FRAC_PADDING_TO_TEXT_HEIGHT = 1 / 8f;
    public static final float SUPERSCRIPT_OFFSET_TO_TEXT_HEIGHT = 1 / 2f;
    public static final float MATRIX_PADDING_TO_TEXT_HEIGHT = 0f;
    public static final float SUBSCRIPT_TO_TEXT_HEIGHT = 1 / 2f;

    public static final float PATH_FACTOR = 1.0f / 2.0f;
    public static final float PATH_HEIGHT = 2f; //2 pixel
    private static final String TAG = "NaturalView";

    private static int lastColor = Color.BLACK; //The last color that any display has shown; used as a backup in case attribute can not be resolved
    protected float mUnitPadding; //The padding between the bottom of the numerator of a fraction and the fraction line
    protected float mTextHeight; //The height of the area reserved (not actually used) for text; Note - Change this to change space between a num and denom of a fraction

    //Variables Used while drawing
    protected float mMaxX;
    protected ArrayList<Float> heights = new ArrayList<Float>();
    protected Paint mTextPaint, mPathPaint, mSubscriptPaint;
    protected float mTextWidth = 0;
    //Drawing Modifier Variables
    private float mPaddingX; //The padding at the start and end of the display (x)
    private float superscriptYOffset; //The y offset up that a superscript would have
    private float mMatrixPadding; //The padding between Matrix entries
    private float paddingAfterMatrix; //The padding after a Matrix
    private ArrayList<Boolean> sqrtCalculated = new ArrayList<>();

    /**
     * Should only be called by a subclass.
     *
     * @param context The context of the app
     * @param attr    Attributes sent by the XML
     */
    public NaturalView(Context context, AttributeSet attr) {
        super(context, attr);
        int displayColor = getDisplayColor(context);
        setupPaints(displayColor);
        setWillNotDraw(false);
    }

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
        while (bracketCount > 0) {
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
     * @param expression The expression where the denom is located
     * @param i          The index of the fraction Token
     * @return The denom of the fraction
     */
    protected static ArrayList<Token> getDenominator(List<Token> expression, int i) {
        if (!(expression.get(i) instanceof OperatorToken && expression.get(i).getType() == OperatorToken.FRACTION)) {
            throw new IllegalArgumentException("Given index of the expression is not a Fraction Token.");
        }
        ArrayList<Token> denom = new ArrayList<>();
        //Now denom
        int bracketCount = 1;
        int j = i + 2;
        while (bracketCount > 0) {
            Token token = expression.get(j);
            if (token instanceof BracketToken && token.getType() == BracketToken.DENOMINATOR_OPEN) {
                bracketCount++;
            } else if (token instanceof BracketToken && token.getType() == BracketToken.DENOMINATOR_CLOSE) {
                bracketCount--;
            }
            denom.add(token);
            j++;
        }
        denom.remove(denom.size() - 1); //Takes out the close bracket
        return denom;
    }

    /**
     * Finds the index where the denominator of the fraction Token at index i ends.
     *
     * @param i          The index of the fraction Token
     * @param expression The expression to find the denominator end
     * @return The index of the end of the denominator
     */
    protected static int getDenominatorEnd(int i, ArrayList<Token> expression) {
        int j = i + 2;
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

    protected static int getSqrtEnd(ArrayList<Token> expr, int start) {
        int j = start + 2;
        int bracketCount = 1;
        while (bracketCount > 0) {
            Token t = expr.get(j);
            if (t instanceof BracketToken && t.getType() == BracketToken.SQRT_CLOSE) {
                bracketCount--;
            } else if (t instanceof BracketToken && t.getType() == BracketToken.SQRT_OPEN) {
                bracketCount++;
            }
            j++;
        }
        return j;
    }

    protected static ArrayList<Token> getSqrtGroupEnd(List<Token> expr, int start) {
        Token t = expr.get(start);
        if (t instanceof BracketToken && t.getType() == BracketToken.SQRT_OPEN) {
        } else {
            return new ArrayList<>();
//            throw new InvalidExpressionException();
        }
        ArrayList<Token> list = new ArrayList<>();
        int j = start + 1;
        int bracketCount = 1;
        while (bracketCount > 0 && j < expr.size()) {
            t = expr.get(j);
            if (t instanceof BracketToken && t.getType() == BracketToken.SQRT_CLOSE) {
                bracketCount--;
            } else if (t instanceof BracketToken && t.getType() == BracketToken.SQRT_OPEN) {
                bracketCount++;
            }
            list.add(t);
            j++;
        }
        list.remove(list.size() - 1);
        return list;
    }

    protected static ArrayList<Token> getSqrtGroupStart(List<Token> expr, int start) {
        Token t = expr.get(start);
        if (!(t instanceof BracketToken) || t.getType() != BracketToken.SQRT_OPEN) {
            throw new InvalidParameterException();
        }
        ArrayList<Token> list = new ArrayList<>();
        int j = start - 1;
        int bracketCount = 1;
        while (bracketCount > 0 && j >= 0) {
            t = expr.get(j);
            if (t instanceof BracketToken && t.getType() == BracketToken.SQRT_CLOSE) {
                bracketCount--;
            } else if (t instanceof BracketToken && t.getType() == BracketToken.SQRT_OPEN) {
                bracketCount++;
            }
            list.add(t);
            j--;
        }
        list.remove(list.size() - 1);
        return list;
    }

    /**
     * Finds the sum of the given array of widths
     *
     * @param widths The array to sum
     * @return The sum
     */
    private static float sum(float[] widths) {
        float widthSum = 0;
        for (int j = 0; j < widths.length; j++) {
            widthSum += widths[j];
        }
        return widthSum;
    }

    /**
     * Finds the max number of lines of text (vertically) there are in the expression
     *
     * @param expression The expression to find the height
     * @return The maximum height of a fraction in the given expression
     */
    protected static int getMaxLinesHeight(List<Token> expression) {
        int maxFracHeight = 1;
        int numBracketCount = 0;
        int denomBracketCount = 0;
        boolean inExponent = false;
        int expBracketCount = 0;
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

            if (numBracketCount == 0 && denomBracketCount == 0 && !inExponent) { //Cannot be in a numerator or denom or an exponent
                if (t instanceof OperatorToken && t.getType() == OperatorToken.FRACTION) {
                    ArrayList<Token> num = getNumerator(expression, i);
                    ArrayList<Token> denom = getDenominator(expression, i);
                    //And adds the height of both + 1
                    int height = getMaxLinesHeight(num) + getMaxLinesHeight(denom);
                    if (height > maxFracHeight) {
                        maxFracHeight = height;
                    }
                }
            }
        }
        return maxFracHeight;
    }

    /**
     * Gets the Display Color of the current Theme.
     *
     * @param context The context of the app
     * @return The color integer of the display color
     */
    private int getDisplayColor(Context context) {
        TypedValue typedValue = new TypedValue();
        boolean resolved = context.getTheme().resolveAttribute(R.attr.buttonTextColor, typedValue, true);
        if (resolved) {
            lastColor = typedValue.data; //Saves it
            return typedValue.data;
        } else {
            return lastColor;
        }
    }

    /**
     * Sets up the Paints.
     *
     * @param displayColor The color of the paints
     */
    public void setupPaints(int displayColor) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(displayColor);
        mTextPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "RobotoMono-Regular.ttf"));

        Log.d(TAG, "setupPaints() called with: displayColor = [" + displayColor + "]" + mTextPaint.getTextSize());

        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setColor(Color.YELLOW);
        mPathPaint.setStrokeWidth(mTextPaint.measureText("0") * 0.2f * PATH_FACTOR);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);

        mSubscriptPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSubscriptPaint.setColor(displayColor);

    }

    /**
     * Sets the font size and updates all the variables that depend on the font size.
     *
     * @param fontSize The new font size.
     */
    public void setFontSize(int fontSize) {
        Log.d(TAG, "setFontSize() called with: fontSize = [" + fontSize + "]");

        mTextPaint.setTextSize(fontSize);
        mPathPaint.setStrokeWidth(mTextPaint.measureText("0") * 0.2f * PATH_FACTOR);

        mSubscriptPaint.setTextSize(fontSize * SUBSCRIPT_TO_TEXT_HEIGHT);
        calculateAttributes();
    }

    /**
     * Calculates specific attributes of the NaturalView
     */
    private void calculateAttributes() {
        //Calculates the height occupied by text
        Rect textRect = new Rect();
        mTextPaint.getTextBounds("1", 0, 1, textRect);

        //Recalculates based on ratio constants
        mTextHeight = textRect.height()/* * RESERVED_TO_ACTUAL_TEXT_HEIGHT*/;
        mTextWidth = textRect.width();

        mPaddingX = X_PADDING_TO_TEXT_HEIGHT * mTextHeight;

        superscriptYOffset = SUPERSCRIPT_OFFSET_TO_TEXT_HEIGHT * mTextHeight;
        paddingAfterMatrix = MATRIX_PADDING_TO_TEXT_HEIGHT * mTextHeight;

        mMatrixPadding = mTextPaint.measureText("  ");
        mUnitPadding = mTextPaint.measureText("0") * 0.2f;
    }

    /**
     * Draws the expression onto the canvas at a vertical offset.
     *
     * @param expression The expression to draw
     * @param canvas     The canvas that will be drawn on
     * @param offset     The vertical offset that the top of the expression will be from the top of the View
     * @return The y value of the bottommost pixel of the expression
     */
    public float drawExpression(ArrayList<Token> expression, Canvas canvas, float offset) {
        float INITIAL_Y = -getTopNegative(expression) + offset + mTextHeight;
        float maxY = offset;
        float y = INITIAL_Y;
        maxY = 0;

        //Calculates the x coordinates to draw each Token
        ArrayList<Float> drawX = prepareDrawing(expression);

        for (int i = 0; i < expression.size(); i++) {
            Token token = expression.get(i);

            float x = drawX.get(i);
            //Calculates the y position to draw at and saves it
            y = calculateNextY(y, expression, i, INITIAL_Y);
            heights.add(i, y);

            //Draws the text
            if (token instanceof MatrixToken) {
                float matrixY = drawMatrix(x, y, canvas, (MatrixToken) token, mTextPaint);
                if (matrixY > maxY) {
                    maxY = matrixY;
                }
            } else {
                String s = token.getSymbol();
                if (s.length() < 3) { //For efficiency
                    canvas.drawText(s, x, y, mTextPaint);
                } else { //May contain a text modification
                    //Goes through each letter and writes with the appropriate text modification
                    boolean onSubscript = false;
                    float currentX = x;
                    for (int index = 0; index < s.length(); index++) {
                        char c = s.charAt(index);
                        if (c == '☺') { //Toggles subscript
                            onSubscript = !onSubscript;
                        } else { //Draws the character
                            float[] width = new float[1];
                            Paint paint = mTextPaint;
                            if (onSubscript) {
                                paint = mSubscriptPaint;
                            }
                            canvas.drawText(Character.toString(c), currentX, y, paint);
                            paint.getTextWidths(Character.toString(c), width);
                            currentX += width[0];
                        }
                    }
                }
            }

            //Updates maxY
            if (y > maxY) {
                maxY = y;
            }

            //Draws fraction sign
            if (token instanceof OperatorToken && token.getType() == OperatorToken.FRACTION) {
                canvas.drawLine(x, y + mUnitPadding, drawX.get(getDenominatorEnd(i, expression)), y + mUnitPadding, mPathPaint);
            }

        }
        return maxY;
    }

    /**
     * Calculates the next y coordinate to draw at based on the current y coordinate and given token and expression.
     *
     * @param y          The current y coordinate
     * @param expression The expression to draw
     * @param i          The i of the next Token to find the y coordinate
     * @param INITIAL_Y  The initial y position
     * @return The next y coordinate
     */
    protected float calculateNextY(float y, ArrayList<Token> expression, int i, final float INITIAL_Y) {
        Token token = expression.get(i);
        if (token instanceof BracketToken) {
            switch (token.getType()) {
                case BracketToken.SUPERSCRIPT_OPEN: {
                    //Extract the exponent expression
                    ArrayList<Token> exponent = new ArrayList<>();
                    int j = i + 1;
                    int scriptCount = 1;
                    while (scriptCount != 0) {
                        Token t = expression.get(j);
                        if (t instanceof BracketToken && t.getType() == BracketToken.SUPERSCRIPT_OPEN) {
                            scriptCount++;
                        } else if (t instanceof BracketToken && t.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
                            scriptCount--;
                        }
                        exponent.add(t);
                        j++;
                    }
                    exponent.remove(exponent.size() - 1); //Removes the SUPERSCRIPT_CLOSE Bracket
                    y -= superscriptYOffset + (getMaxLinesHeight(exponent) == 1 ? 0 : getHeight(exponent, false) / 2);
                    break;
                }
                case BracketToken.SUPERSCRIPT_CLOSE: {
                    //Finds the height of the SUPERSCRIPT_OPEN
                    int bracketCount = 1;
                    int j = i - 1;
                    while (bracketCount > 0) {
                        Token t = expression.get(j);
                        if (t instanceof BracketToken && t.getType() == BracketToken.SUPERSCRIPT_OPEN) {
                            bracketCount--;
                        } else if (t instanceof BracketToken && t.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
                            bracketCount++;
                        }
                        j--;
                    }

                    if (j == -1) { //Some idiot did ^E (with no base)
                        y = INITIAL_Y;
                    } else {
                        y = heights.get(j);
                    }
                    break;
                }
                case BracketToken.NUMERATOR_OPEN: {
                    int j = i + 1;
                    ArrayList<Token> num = new ArrayList<>();
                    int bracketCount = 1;
                    while (bracketCount != 0) {
                        Token t = expression.get(j);
                        if (t instanceof BracketToken && t.getType() == BracketToken.NUMERATOR_OPEN) {
                            bracketCount++;
                        } else if (t instanceof BracketToken && t.getType() == BracketToken.NUMERATOR_CLOSE) {
                            bracketCount--;
                        }
                        num.add(t);
                        j++;
                    }
                    num.remove(num.size() - 1); //Removes the NUMERATOR_CLOSE Bracket

                    //Generates an expression containing the fraction
                    ArrayList<Token> fraction = new ArrayList<>();
                    fraction.add(BracketFactory.makeNumeratorOpen());
                    fraction.addAll(num);
                    fraction.add(expression.get(j - 1)); //NUMERATOR_CLOSE Bracket
                    fraction.add(expression.get(j)); //FRACTION Operator
                    fraction.add(BracketFactory.makeDenominatorOpen());
                    fraction.addAll(getDenominator(expression, j)); //Adds the entire denominator
                    fraction.add(BracketFactory.makeDenominatorClose());

                    if (getMaxLinesHeight(num) == 1) {
                        y += -getHeight(fraction, true) / 4; //HEREEEEE!!!!!
                    } else {
                        y += -getHeight(fraction, true) / 2 + getHeight(num, true) / 2;
                    }
                    break;
                }
                case BracketToken.DENOMINATOR_OPEN: {
                    ArrayList<Token> denom = getDenominator(expression, i - 1);
                    y += -getTopNegative(denom) + mTextHeight
                            + mUnitPadding //fraction sign
                            + mUnitPadding; //padding
                    break;
                }
                case BracketToken.DENOMINATOR_CLOSE: {
                    int bracketCount = 1;
                    int j = i - 1;
                    while (bracketCount > 0) {
                        Token t = expression.get(j);
                        if (t instanceof BracketToken && t.getType() == BracketToken.DENOMINATOR_OPEN) {
                            bracketCount--;
                        } else if (t instanceof BracketToken && t.getType() == BracketToken.DENOMINATOR_CLOSE) {
                            bracketCount++;
                        }
                        j--;
                    }

                    //Now j is at the i of the fraction. Looking for the height of the NUMERATOR_OPEN bracket
                    bracketCount = 1;
                    j -= 2;
                    while (bracketCount > 0) {
                        Token t = expression.get(j);
                        if (t instanceof BracketToken && t.getType() == BracketToken.NUMERATOR_OPEN) {
                            bracketCount--;
                        } else if (t instanceof BracketToken && t.getType() == BracketToken.NUMERATOR_CLOSE) {
                            bracketCount++;
                        }
                        j--;
                    }

                    //Changes height to the height of the Token before the NUMERATOR_OPEN
                    if (j >= 0) {
                        y = heights.get(j);
                    } else { //Very first token; cannot check token before it
                        y = INITIAL_Y;
                    }
                    break;
                }
            }
        } else if (token instanceof OperatorToken && token.getType() == OperatorToken.FRACTION) {

            //Finds the max height in the numerator
            ArrayList<Token> numerator = getNumerator(expression, i);
            float maxY = Float.NEGATIVE_INFINITY;
            for (Token t : numerator) {
                float zY = heights.get(expression.indexOf(t));
                if (zY > maxY) {
                    maxY = zY;
                }
            }
            y = maxY;
        } else if (token instanceof FunctionToken && token.getType() == FunctionToken.SQRT) {

        }
        return y;
    }

    /**
     * Draws the given Matrix on the given Canvas.
     *
     * @param x      The left-most x coordinate of the Matrix
     * @param y      The y coordinate of the middle of the Matrix that would be drawn
     * @param canvas The canvas to draw the Matrix on
     * @param matrix The Matrix to draw
     * @param paint  The Paint to draw the Matrix with
     * @return The bottommost y coordinate drawn at
     */
    protected float drawMatrix(float x, float y, Canvas canvas, MatrixToken matrix, Paint paint) {
        ArrayList<Token>[][] entries = matrix.getEntries();
        y -= (entries.length - 1) * mTextHeight / 2; //Starts at the top
        //Calculates at what x value to start drawing each column
        float[] columnX = new float[entries[0].length + 1];
        columnX[0] = x;
        for (int j = 1; j < columnX.length; j++) {
            float maxWidth = 0;
            for (int k = 0; k < entries.length; k++) {
                float width = paint.measureText(Utility.printExpression(entries[k][j - 1]));
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }
            columnX[j] = columnX[j - 1] + maxWidth;
        }

        //Draws all the Matrix entries
        for (int j = 0; j < entries.length; j++) {
            for (int k = 0; k < entries[j].length; k++) {
                String str = Utility.printExpression(entries[j][k]);
                //Centers the text (determines what x value to print it at
                float currentWidth = paint.measureText(str);
                float targetWidth = columnX[k + 1] - columnX[k];
                float padding = (targetWidth - currentWidth) / 2; //Padding on each side
                float drawMatrixX = columnX[k] + k * mMatrixPadding;
                drawMatrixX += padding;
                canvas.drawText(str, drawMatrixX, y, paint);
            }
            y += mTextHeight;
        }
        y -= mTextHeight;
        return y;
    }

    /**
     * Prepares the variables for drawing.
     *
     * @param expression The expression to prepare to draw
     * @return A list of Floats that indicate the x coordinates to draw at
     */
    protected ArrayList<Float> prepareDrawing(ArrayList<Token> expression) {
        ArrayList<Float> drawX = calculateDrawX(expression); //Stores the width of each counted symbol
        heights.clear();

        adjustCenterFractions(expression, drawX);
        if (drawX.size() > 1) {
            mMaxX = drawX.get(drawX.size() - 1);
        }
        return drawX;
    }

    /**
     * Calculates all the x positions of the Tokens for the given expression to draw.
     *
     * @param expression The expression to calculate
     * @return drawX The drawX values for the expression
     */
    protected ArrayList<Float> calculateDrawX(ArrayList<Token> expression) {
        ArrayList<Float> drawX = new ArrayList<>();
        Paint paint;
        int scriptLevel = 0; //superscript = 1, any additional levels would +1
        int scriptBracketCount = 0; //Counts the brackets for any exponents
        Stack<Float> fractionStarts = new Stack<>(); //The indices where fractions start
        float x = mPaddingX;

        for (int i = 0; i < expression.size(); i++) {
            Token token = expression.get(i);
            //Stores the width of this draw count into the array
            drawX.add(x);

            if (token instanceof BracketToken && token.getType() == BracketToken.SUPERSCRIPT_OPEN) {
                scriptLevel++;
                scriptBracketCount++;
            } else if (token instanceof BracketToken && token.getType() == BracketToken.NUMERATOR_OPEN) {
                x += mUnitPadding;
                fractionStarts.push(x);
            } else if (token instanceof BracketToken && token.getType() == BracketToken.DENOMINATOR_CLOSE) {
                //Finds index where the numerator end
                int j = i - 1;
                int bracketCount = 1;
                float paddingCount = 1;
                //DENOMINATOR
                while (bracketCount > 0) {
                    Token t = expression.get(j);
                    if (t instanceof BracketToken && t.getType() == BracketToken.DENOMINATOR_OPEN) {
                        bracketCount--;
                    } else if (t instanceof BracketToken && t.getType() == BracketToken.DENOMINATOR_CLOSE) {
                        bracketCount++;
                        paddingCount++;
                    }
                    j--;
                }
                //NUM
                j -= 1;
                int endNum = j;
                float tmpX = drawX.get(endNum) + mUnitPadding * paddingCount;
                x = x > tmpX ? x : tmpX;
            } else if (token instanceof BracketToken && token.getType() == BracketToken.SQRT_CLOSE) {
                x += mUnitPadding;
            } else if (scriptLevel > 0) { //Counts brackets if its writing in superscript
                if (token instanceof BracketToken) {
                    BracketToken b = (BracketToken) token;
                    if (b.getType() == BracketToken.SUPERSCRIPT_OPEN) {
                        scriptBracketCount++;
                    } else if (b.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
                        scriptBracketCount--;
                        if (scriptBracketCount == scriptLevel - 1) { //No longer superscript
                            scriptLevel--;
                        }
                    }
                }
            }

            if (token instanceof MatrixToken) {
                ArrayList<Token>[][] entries = ((MatrixToken) token).getEntries();
                //Calculates at what x value to start drawing each column
                float[] columnX = new float[entries[0].length + 1];
                columnX[0] = x;
                for (int j = 1; j < columnX.length; j++) {
                    float maxWidth = 0;
                    for (int k = 0; k < entries.length; k++) {
                        float width = mTextPaint.measureText(Utility.printExpression(entries[k][j - 1]));
                        if (width > maxWidth) {
                            maxWidth = width;
                        }
                    }
                    columnX[j] = columnX[j - 1] + maxWidth;
                }
                x = columnX[columnX.length - 1] + (entries[0].length - 1) * mMatrixPadding + paddingAfterMatrix;
            } else if (token instanceof FunctionToken && token.getType() == FunctionToken.SQRT) {
                x += mTextWidth;
            } else {
                paint = mTextPaint;
                //Determines the width of the symbol in text
                String s = token.getSymbol();
                if (s.length() < 3) { //For efficiency
                    x += paint.measureText(token.getSymbol());
                } else {
                    //May contain a text modification
                    //Goes through each letter and writes with the appropriate text modification
                    boolean onSubscript = false;
                    for (int index = 0; index < s.length(); index++) {
                        char c = s.charAt(index);
                        if (c == '☺') { //Toggles subscript
                            onSubscript = !onSubscript;
                        } else { //Draws the character
                            paint = mTextPaint;
                            if (onSubscript) {
                                paint = mSubscriptPaint;
                            }
                            x += paint.measureText(Character.toString(c));
                        }
                    }
                }
            }

            if (token instanceof BracketToken && token.getType() == BracketToken.NUMERATOR_CLOSE) {
                if (!fractionStarts.isEmpty()) {
                    x = fractionStarts.pop(); //reset position
                }
            }
        }
        drawX.add(x);
        return drawX;
    }

    /**
     * Determines what would be the most negative pixel drawn, assuming the expression that drawing at zero.
     *
     * @param expression The expression to draw
     * @return The most negative y coordinate drawn on
     */
    protected float getTopNegative(List<Token> expression) {

        float mostNeg = Float.POSITIVE_INFINITY;
        float yModifier = 0;
        ArrayList<Float> heights = new ArrayList<>();
        for (int i = 0; i < expression.size(); i++) {
            Token token = expression.get(i);
            if (token instanceof BracketToken) {
                switch (token.getType()) {
                    case BracketToken.SUPERSCRIPT_OPEN: {
                        //Extract the exponent expression
                        ArrayList<Token> exponent = new ArrayList<>();
                        int j = i + 1;
                        int scriptCount = 1;
                        while (scriptCount != 0) {
                            Token t = expression.get(j);
                            if (t instanceof BracketToken && t.getType() == BracketToken.SUPERSCRIPT_OPEN) {
                                scriptCount++;
                            } else if (t instanceof BracketToken && t.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
                                scriptCount--;
                            }
                            exponent.add(t);
                            j++;
                        }
                        exponent.remove(exponent.size() - 1); //Removes the SUPERSCRIPT_CLOSE Bracket
                        yModifier -= superscriptYOffset + (getMaxLinesHeight(exponent) == 1 ? 0 : getHeight(exponent, false) / 2);
                        break;
                    }
                    case BracketToken.SUPERSCRIPT_CLOSE: {
                        yModifier += superscriptYOffset;
                        break;
                    }
                    case BracketToken.NUMERATOR_OPEN: {
                        int j = i + 1;
                        ArrayList<Token> num = new ArrayList<Token>();
                        int bracketCount = 1;
                        while (bracketCount != 0) {
                            Token t = expression.get(j);
                            if (t instanceof BracketToken && t.getType() == BracketToken.NUMERATOR_OPEN) {
                                bracketCount++;
                            } else if (t instanceof BracketToken && t.getType() == BracketToken.NUMERATOR_CLOSE) {
                                bracketCount--;
                            }
                            num.add(t);

                            j++;
                        }
                        num.remove(num.size() - 1); //Removes the NUMERATOR_CLOSE Bracket

                        //Generates an expression containing the fraction
                        ArrayList<Token> fraction = new ArrayList<Token>();
                        fraction.add(BracketFactory.makeNumeratorOpen());
                        fraction.addAll(num);
                        fraction.add(expression.get(j - 1)); //NUMERATOR_CLOSE Bracket
                        fraction.add(expression.get(j)); //FRACTION Operator
                        fraction.add(BracketFactory.makeDenominatorOpen());
                        fraction.addAll(getDenominator(expression, j)); //Adds the entire denom
                        fraction.add(BracketFactory.makeDenominatorClose());

                        if (getMaxLinesHeight(num) == 1) {
                            yModifier += -getHeight(fraction, true) / 4; //HEREEEEE!!!!!
                        } else {
                            yModifier += -getHeight(fraction, true) / 2 + getHeight(num, true) / 2;
                        }
                        break;
                    }
                    case BracketToken.DENOMINATOR_OPEN: {
                        ArrayList<Token> denom = getDenominator(expression, i - 1);
                        if (getMaxLinesHeight(denom) == 1) {
                            yModifier += getHeight(denom, true);
                        } else {
                            yModifier += getHeight(denom, true) / 2;
                        }
                        break;
                    }
                    case BracketToken.DENOMINATOR_CLOSE: {
                        int bracketCount = 1;
                        int j = i - 1;
                        while (bracketCount > 0) {
                            Token t = expression.get(j);
                            if (t instanceof BracketToken && t.getType() == BracketToken.DENOMINATOR_OPEN) {
                                bracketCount--;
                            } else if (t instanceof BracketToken && t.getType() == BracketToken.DENOMINATOR_CLOSE) {
                                bracketCount++;
                            }
                            j--;
                        }

                        //Now j is at the index of the fraction. Looking for the height of the NUMERATOR_OPEN bracket
                        bracketCount = 1;
                        j -= 2;
                        while (bracketCount > 0) {
                            Token t = expression.get(j);
                            if (t instanceof BracketToken && t.getType() == BracketToken.NUMERATOR_OPEN) {
                                bracketCount--;
                            } else if (t instanceof BracketToken && t.getType() == BracketToken.NUMERATOR_CLOSE) {
                                bracketCount++;
                            }
                            j--;
                        }

                        //Changes height to the height of the Token before the NUMERATOR_OPEN
                        if (j >= 0) {
                            yModifier = heights.get(j);
                        } else { //Very first token; cannot check token before it
                            yModifier = 0;
                        }
                        break;
                    }
                }
            } else if (token instanceof OperatorToken && token.getType() == OperatorToken.FRACTION) {

                //Finds the max height in the numerator
                ArrayList<Token> numerator = getNumerator(expression, i);
                float maxHeight = Float.NEGATIVE_INFINITY;
                for (Token t : numerator) {
                    float height = heights.get(expression.indexOf(t));
                    if (height > maxHeight) {
                        maxHeight = height;
                    }
                }
                yModifier = maxHeight;
            } else if (token instanceof FunctionToken && token.getType() == FunctionToken.SQRT) {
                ArrayList<Token> sqrtGroup = getSqrtGroupEnd(expression, i + 1);
                yModifier -= +mUnitPadding * 3;
            }
            if (token instanceof MatrixToken) {
                float negY = -(mTextHeight * (((MatrixToken) token).getNumOfRows() - 1)) / 2 + yModifier;
                heights.add(negY);
                if (negY < mostNeg) {
                    mostNeg = negY;
                }
            } else {
                //Sets the most neg if it is lower than current
                if (yModifier < mostNeg) {
                    mostNeg = yModifier;
                }
                heights.add(yModifier);
            }
        }
        Log.d(TAG, "getTopNegative() returned: " + mostNeg);
        return mostNeg;
    }

    /**
     * Centers the fractions through modification of the drawX list.
     *
     * @param expression The expression to center
     * @param drawX      The uncentered drawing x coordinates
     * @return The centered x coordinates for the expression
     */
    private List<Float> adjustCenterFractions(List<Token> expression, List<Float> drawX) {
        for (int i = 0; i < expression.size(); i++) {
            Token t = expression.get(i);
            if (t instanceof OperatorToken && t.getType() == OperatorToken.FRACTION) {
                ArrayList<Token> numerator = getNumerator(expression, i);
                ArrayList<Token> denominator = getDenominator(expression, i);
                float numeratorWidth = getWidth(i - numerator.size() - 1, i - 1, expression, drawX);
                float denominatorWidth = getWidth(i + 1, denominator.size() + i + 1, expression, drawX);

                if (numeratorWidth > denominatorWidth) {
                    float adjust = (numeratorWidth - denominatorWidth) / 2;
                    for (int j = i + 1; j <= denominator.size() + i + 2; j++) {
                        drawX.add(j, drawX.remove(j) + adjust);
                    }
                } else if (numeratorWidth < denominatorWidth) {
                    float adjust = (denominatorWidth - numeratorWidth) / 2;
                    for (int j = i - numerator.size() - 2; j < i; j++) {
                        drawX.add(j, drawX.remove(j) + adjust);
                    }
                } else {
                    //Equals
                    //Nothing happens
                }
            }
        }
        return drawX;
    }

    /**
     * Determines the width of a given expression from the drawX list.
     *
     * @param start      The start index on the drawX
     * @param end        The end index on the drawX
     * @param expression The expression to center
     * @param drawX      The uncentered drawing x coordinates
     * @return The width of the expression
     */
    protected float getWidth(int start, int end, List<Token> expression, List<Float> drawX) {
        //Counts to the END for the expression (last pixel drawn)
        String symb = expression.get(end).getSymbol();
        String filtered = "";
        //Filters out the subscript markers
        for (int i = 0; i < symb.length(); i++) {
            char c = symb.charAt(i);
            if (c != '☺') {
                filtered += c;
            }
        }
        float[] widths = new float[filtered.length()];
        mTextPaint.getTextWidths(filtered, widths);
        return drawX.get(end) + sum(widths) - drawX.get(start);
    }

    /**
     * Determines the height, in pixels, of the expression
     *
     * @param expression        The expression
     * @param countEndExponents If exponents at the end of the expression should count
     * @return The height of the given expression, in pixels
     */
    protected float getHeight(List<Token> expression, boolean countEndExponents) {
        Log.d(TAG, "getHeight() called with: expression = " + expression);

        float maxHeight = mTextHeight;
        float temp = 0;
        for (int index = 0; index < expression.size(); index++) {
            Token t = expression.get(index);
            //Handles parts of fractions separately
            if (t instanceof BracketToken) {
                switch (t.getType()) {
                    case BracketToken.NUMERATOR_CLOSE: {
                        ArrayList<Token> numerator = getNumerator(expression, index + 1);
                        float z = getHeight(numerator, true) /*+ mUnitPadding * 1*/; //padding top
                        Log.d(TAG, "getHeight: numerator h = " + z);
                        temp += z;
                        temp += mUnitPadding; //fraction sign
                        break;
                    }
                    case BracketToken.DENOMINATOR_OPEN: { //numerator close
                        ArrayList<Token> denominator = getDenominator(expression, index - 1);
                        float v = getHeight(denominator, true) + mUnitPadding * 1;
                        Log.d(TAG, "getHeight: denominator v = " + v);
                        temp += v;
                        if (temp > maxHeight) {
                            maxHeight = temp;
                        }
                        temp = 0;
                        break;
                    }

                }
            } else if (t instanceof OperatorToken && t.getType() == OperatorToken.EXPONENT) {
                ArrayList<Token> exponent = new ArrayList<>();
                int j = index + 2;
                int scriptCount = 1;
                while (scriptCount != 0) {
                    Token token = expression.get(j);
                    if (token instanceof BracketToken && token.getType() == BracketToken.SUPERSCRIPT_OPEN) {
                        scriptCount++;
                    } else if (token instanceof BracketToken && token.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
                        scriptCount--;
                    }
                    exponent.add(token);
                    j++;
                }
                exponent.remove(exponent.size() - 1); //Removes the SUPERSCRIPT_CLOSE Bracket
                if (!countEndExponents) {
                    //Takes out all cases where there is a ^(E) and the end
                    while (exponent.size() > 1 && exponent.get(exponent.size() - 1) instanceof BracketToken && exponent.get(exponent.size() - 1).getType() == BracketToken.SUPERSCRIPT_CLOSE) {
                        int k = exponent.size() - 2;
                        exponent.remove(k + 1);
                        int bracketCount = 1;
                        while (bracketCount != 0) { //Keeps removing until the end exponents begins
                            Token token = exponent.get(k);
                            if (token instanceof BracketToken && token.getType() == BracketToken.SUPERSCRIPT_OPEN) {
                                bracketCount--;
                            } else if (token instanceof BracketToken && token.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
                                bracketCount++;
                            }
                            exponent.remove(k);
                            k--;
                        }
                        exponent.remove(k); //Removes the exponent
                    }
                }
                temp = (countEndExponents ? superscriptYOffset : 0) + getHeight(exponent, countEndExponents);
                if (temp > maxHeight) {
                    maxHeight = temp;
                }
                temp = 0;
            } else if (t instanceof MatrixToken) {
                temp = mTextHeight * (((MatrixToken) t).getNumOfRows() - 1);
                if (temp > maxHeight) {
                    maxHeight = temp;
                }
                temp = 0;
            } else if (t instanceof FunctionToken && t.getType() == FunctionToken.SQRT) {
                ArrayList<Token> sqrtGroup = getSqrtGroupEnd(expression, index + 1);
                temp = getHeight(sqrtGroup, true) + mUnitPadding * 3;
                Log.d(TAG, "getHeight: sqrt func = " + temp);
                if (temp > maxHeight) {
                    maxHeight = temp;
                }
                temp = 0;
            }
        }
        Log.d(TAG, "getHeight() returned: " + maxHeight);
        return maxHeight;
    }

    @Nullable
    protected Pair<Integer, Integer> getGroupSqrt(List<Token> expr, int from) {
        if (!(expr.get(from) instanceof BracketToken && expr.get(from).getType() == BracketToken.SQRT_OPEN)) {
            return null;
        }
        int index = from;
        index++; //group
        int bracketCount = 1;
        while (index < expr.size() && bracketCount > 0) {
            Token token = expr.get(index);
            if (token instanceof BracketToken && token.getType() == BracketToken.SQRT_OPEN) {
                bracketCount++;
            } else if (token instanceof BracketToken && token.getType() == BracketToken.SQRT_CLOSE) {
                bracketCount--;
            }
            index++;
        }
        return new Pair<>(from, index - 1);
    }
}