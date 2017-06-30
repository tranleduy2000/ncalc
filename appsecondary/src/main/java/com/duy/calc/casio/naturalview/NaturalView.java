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

package com.duy.calc.casio.naturalview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;

import com.duy.calc.casio.token.BracketToken;
import com.duy.calc.casio.token.FunctionToken;
import com.duy.calc.casio.token.MatrixToken;
import com.duy.calc.casio.token.OperatorToken;
import com.duy.calc.casio.token.PlaceholderToken;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.tokenizer.MachineExpressionConverter;
import com.duy.calc.casio.tokenizer.TokenUtil;
import com.example.duy.calculator.R;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import static com.duy.calc.casio.tokenizer.MachineExpressionConverter.printExpression;
import static com.duy.calc.casio.tokenizer.TokenUtil.getDenominator;
import static com.duy.calc.casio.tokenizer.TokenUtil.getMaxLine;
import static com.duy.calc.casio.tokenizer.TokenUtil.getNumerator;
import static com.duy.calc.casio.tokenizer.TokenUtil.getSubScriptGroupStartIndex;


public abstract class NaturalView extends View {

    //Constant ratios of variables to the text height value
    public static final float RESERVED_TO_ACTUAL_TEXT_HEIGHT = 5 / 4f; // The ratio of the height of the space reserved for text to actually used
    public static final float X_PADDING_TO_TEXT_HEIGHT = 1 / 2f;
    public static final float SUPERSCRIPT_OFFSET_TO_TEXT_HEIGHT = 1 / 2f;
    public static final float MATRIX_PADDING_TO_TEXT_HEIGHT = 0f;
    public static final float SUBSCRIPT_TO_TEXT_HEIGHT = 1 / 2f;

    public static final float PATH_FACTOR = 1.0f / 2.0f;
    private static final String TAG = "NaturalView";
    public float mUnitPadding; //The padding between the bottom of the numerator of a fraction and the fraction line
    public float mTextHeight; //The height of the area reserved (not actually used) for text; Note - Change this to change space between a num and denom of a fraction
    //Variables Used while drawing
    public float mMaxX;
    public ArrayList<Float> heights = new ArrayList<>();
    public Paint mTextPaint, mPathPaint, mSubscriptPaint;
    public float mTextWidth = 0;
    protected int mTextColor = Color.WHITE; //The last color that any mDisplay has shown; used as a backup in case attribute can not be resolved
    //Drawing Modifier Variables
    protected float mPaddingX; //The padding at the start and end of the mDisplay (x)
    protected float superscriptYOffset; //The y offset up that a superscript would have
    protected float mMatrixPadding; //The padding between Matrix entries
    protected float paddingAfterMatrix; //The padding after a Matrix


    protected Hashtable<List<Token>, Float> mapHeights = new Hashtable<>(); //faster
    protected int mBaseTextColor;

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
     * Gets the Display Color of the current Theme.
     *
     * @param context The context of the app
     * @return The color integer of the mDisplay color
     */
    private int getDisplayColor(Context context) {
        mBaseTextColor = ContextCompat.getColor(context, R.color.base_color);

        TypedValue typedValue = new TypedValue();
        boolean resolved = context.getTheme().resolveAttribute(R.attr.buttonTextColor, typedValue, true);
        if (resolved) {
            mTextColor = typedValue.data; //Saves it
            return typedValue.data;
        } else {
            return mTextColor;
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
        mTextPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "courrier_new.ttf"));

        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setColor(displayColor);
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
    @CallSuper
    public void setFontSize(int fontSize) {
        mTextPaint.setTextSize(fontSize);
        mPathPaint.setTextSize(fontSize);
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


        mTextHeight = textRect.height();
        mTextWidth = mTextPaint.measureText("M");
        mPaddingX = mTextHeight * X_PADDING_TO_TEXT_HEIGHT;

        superscriptYOffset = mTextHeight * SUPERSCRIPT_OFFSET_TO_TEXT_HEIGHT;
        paddingAfterMatrix = mTextHeight * MATRIX_PADDING_TO_TEXT_HEIGHT;

        mMatrixPadding = mTextPaint.measureText(" ");
        mUnitPadding = mTextWidth * 0.2f;
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
    public float calculateNextY(float y, List<Token> expression, int i, final float INITIAL_Y) {
        Token token = expression.get(i);
        if (token instanceof BracketToken) {
            switch (token.getType()) {
                case BracketToken.SUPERSCRIPT_OPEN: {
                    //Extract the exponent expression
                    ArrayList<Token> exponent = TokenUtil.getInsideGroupFromStart(expression, i,
                            BracketToken.SUPERSCRIPT_OPEN, BracketToken.SUPERSCRIPT_CLOSE);

                    float height = getHeight(exponent);
                    float topNegative = getTopNegative(exponent);
                    y -= height + topNegative - superscriptYOffset;//accept

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
                case BracketToken.SUBSCRIPT_OPEN: {
                    //Extract the exponent expression
                    ArrayList<Token> subscript = new ArrayList<>();
                    int j = i + 1;
                    int scriptCount = 1;
                    while (scriptCount != 0) {
                        Token t = expression.get(j);
                        if (t instanceof BracketToken && t.getType() == BracketToken.SUBSCRIPT_OPEN) {
                            scriptCount++;
                        } else if (t instanceof BracketToken && t.getType() == BracketToken.SUBSCRIPT_CLOSE) {
                            scriptCount--;
                        }
                        subscript.add(t);
                        j++;
                    }
                    subscript.remove(subscript.size() - 1); //Removes the SUPERSCRIPT_CLOSE Bracket
                    int line = getMaxLine(subscript);
                    if (line == 1) {
                        float topNegative = getTopNegative(subscript);
                        y += -topNegative + superscriptYOffset;//accept
                    } else {
                        float topNegative = getTopNegative(subscript);
                        y += -topNegative + superscriptYOffset;//accept
                    }
                    break;
                }
                case BracketToken.SUBSCRIPT_CLOSE: {
                    //Finds the height of the SUBSCRIPT_OPEN
                    int bracketCount = 1;
                    int j = i - 1;
                    while (bracketCount > 0) {
                        Token t = expression.get(j);
                        if (t instanceof BracketToken && t.getType() == BracketToken.SUBSCRIPT_OPEN) {
                            bracketCount--;
                        } else if (t instanceof BracketToken && t.getType() == BracketToken.SUBSCRIPT_CLOSE) {
                            bracketCount++;
                        }
                        j--;
                    }
                    j++;
                    if (j == -1) {
                        y = INITIAL_Y;
                    } else {
                        y = heights.get(j);
                    }
                    break;
                }
                case BracketToken.NUMERATOR_OPEN: {
                    ArrayList<Token> num = TokenUtil.getInsideGroupFromStart(expression, i,
                            BracketToken.NUMERATOR_OPEN, BracketToken.NUMERATOR_CLOSE);

                    int line = getMaxLine(num);
                    if (line == 1) {
                        y -= mTextHeight * 0.5f;
                    } else {
                        float numNeg = getTopNegative(num);//accept
                        float numHeight = getHeight(num);//accept
                        y -= numHeight + numNeg - mTextHeight * 0.5f;//accept
                    }
                    break;
                }
                case BracketToken.DENOMINATOR_OPEN: {
                    ArrayList<Token> denominator = getDenominator(expression, i - 1);
                    float topNegative = getTopNegative(denominator);
                    float height = getHeight(denominator);
                    y += -topNegative + mTextHeight + mUnitPadding * 2;
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
                    } else { //Very first token; cannot checkAll token before it
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
        } else if (token instanceof FunctionToken && token.getType() == FunctionToken.ABS) {
//            y += mUnitPadding;
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
    public float drawMatrix(float x, float y, Canvas canvas, MatrixToken matrix, Paint paint) {
        ArrayList<Token>[][] entries = matrix.getEntries();
        y -= (entries.length - 1) * mTextHeight / 2; //Starts at the top
        //Calculates at what x value to start drawing each column
        float[] columnX = new float[entries[0].length + 1];
        columnX[0] = x;
        for (int j = 1; j < columnX.length; j++) {
            float maxWidth = 0;
            for (int k = 0; k < entries.length; k++) {
                float width = paint.measureText(printExpression(entries[k][j - 1]));
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }
            columnX[j] = columnX[j - 1] + maxWidth;
        }

        //Draws all the Matrix entries
        for (int j = 0; j < entries.length; j++) {
            for (int k = 0; k < entries[j].length; k++) {
                String str = printExpression(entries[j][k]);
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
    public ArrayList<Float> prepareDrawing(List<Token> expression) {
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
    public ArrayList<Float> calculateDrawX(List<Token> expression) {
        ArrayList<Float> drawX = new ArrayList<>();
        Stack<Float> fractionStack = new Stack<>(); //The indices where fractions start
        float x = mPaddingX;

        for (int i = 0; i < expression.size(); i++) {
            Token token = expression.get(i);
            //Stores the width of this draw count into the array
            drawX.add(x);
            if (token instanceof BracketToken && token.getType() == BracketToken.SUPERSCRIPT_OPEN) {
                x += 0;
            } else if (token instanceof BracketToken && token.getType() == BracketToken.NUMERATOR_OPEN) {
                fractionStack.push(x);
            } else if (token instanceof BracketToken && token.getType() == BracketToken.DENOMINATOR_CLOSE) {
                //Finds index where the numerator end
                int j = i - 1;
                int bracketCount = 1;
                //DENOMINATOR
                while (bracketCount > 0 && j > -1) {
                    Token t = expression.get(j);
                    if (t instanceof BracketToken && t.getType() == BracketToken.DENOMINATOR_OPEN) {
                        bracketCount--;
                    } else if (t instanceof BracketToken && t.getType() == BracketToken.DENOMINATOR_CLOSE) {
                        bracketCount++;
                    }
                    j--;
                }
                //NUM
                j -= 1;
                int endNum = j;
                float tmpX = drawX.get(endNum);
                x = x > tmpX ? x : tmpX;
            } else if (token instanceof BracketToken
                    && (token.getType() == BracketToken.SQRT_CLOSE || token.getType() == BracketToken.SQRT_OPEN)) {
                if (token.getType() == BracketToken.SQRT_OPEN) {
                    x += mUnitPadding;
                } else {
                    x += mUnitPadding * 2;
                }
            } else if (token instanceof FunctionToken
                    && token.getType() == FunctionToken.DERIVATIVE) {
                x += mTextWidth * 2;
            } else if (token instanceof MatrixToken) {
                ArrayList<Token>[][] entries = ((MatrixToken) token).getEntries();
                //Calculates at what x value to start drawing each column
                float[] columnX = new float[entries[0].length + 1];
                columnX[0] = x;
                for (int j = 1; j < columnX.length; j++) {
                    float maxWidth = 0;
                    for (ArrayList<Token>[] entry : entries) {
                        float width = printExpression(entry[j - 1]).length() * mTextWidth;
                        if (width > maxWidth) {
                            maxWidth = width;
                        }
                    }
                    columnX[j] = columnX[j - 1] + maxWidth;
                }
                x = columnX[columnX.length - 1] + (entries[0].length - 1) * mMatrixPadding + paddingAfterMatrix;
            } else if (((token instanceof FunctionToken) && (token.getType() == FunctionToken.SQRT))
                    || (token instanceof OperatorToken && token.getType() == OperatorToken.SURD)) {
                x += mTextWidth;
            } else if (token instanceof FunctionToken && token.getType() == FunctionToken.ABS) {
                //don't required
                x += 0;
            } else if ((token instanceof BracketToken) //parentheses
                    && ((token.getType() == BracketToken.PARENTHESES_OPEN) || (token.getType() == BracketToken.PARENTHESES_CLOSE)
                    || token.getType() == BracketToken.DERIVATIVE_OPEN || token.getType() == BracketToken.DERIVATIVE_CLOSE
                    || token.getType() == BracketToken.LOGN_OPEN || token.getType() == BracketToken.LOGN_CLOSE)) {
                x += mTextWidth * 0.75f;
            } else if ((token instanceof BracketToken) &&
                    (token.getType() == BracketToken.FRACTION_OPEN || token.getType() == BracketToken.FRACTION_CLOSE)) {
                x += mUnitPadding * 2;
            } else if ((token instanceof BracketToken) &&
                    (token.getType() == BracketToken.PERM_CLOSE) || (token.getType() == BracketToken.PERM_OPEN)) {
                x += mUnitPadding;
            } else if ((token instanceof BracketToken) &&
                    ((token.getType() == BracketToken.COMP_CLOSE) || (token.getType() == BracketToken.COMP_OPEN))) {
                x += mUnitPadding;
            } else if ((token instanceof BracketToken)
                    && (token.getType() == BracketToken.ABS_OPEN || token.getType() == BracketToken.ABS_CLOSE)) {
                x += mUnitPadding * 2;
            } else if (token instanceof BracketToken && token.getType() == BracketToken.NUMERATOR_CLOSE) {
                if (!fractionStack.isEmpty()) {
                    x = fractionStack.pop(); //reset position
                }
            } else if (token instanceof PlaceholderToken) {
                x += mTextWidth; // include comma token
            } else {
                //Determines the width of the symbol in text
                String s = token.getSymbol();
                if (s.length() < 3) { //For efficiency
                    x += mTextPaint.measureText(s);
                } else {
                    //May contain a text modification
                    //Goes through each letter and writes with the appropriate text modification
                    boolean onSubscript = false;
                    for (int index = 0; index < s.length(); index++) {
                        char c = s.charAt(index);
                        if (c == '☺') { //Toggles subscript
                            onSubscript = !onSubscript;
                        } else { //Draws the character
                            x += mTextWidth;
                        }
                    }
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
    public float getTopNegative(List<Token> expression) {
        float mostNeg = 0;
        float yModifier = 0;
        ArrayList<Float> heights = new ArrayList<>();
        for (int i = 0; i < expression.size(); i++) {
            Token token = expression.get(i);
            if (token instanceof BracketToken) {
                switch (token.getType()) {
                    case BracketToken.SUPERSCRIPT_OPEN: {
                        ArrayList<Token> exponent = TokenUtil.getInsideGroupFromStart(expression,
                                i, BracketToken.SUPERSCRIPT_OPEN, BracketToken.SUPERSCRIPT_CLOSE);
                        float height = getHeight(exponent);
                        float topNegative = getTopNegative(exponent);
                        yModifier -= height + topNegative - superscriptYOffset;
                        break;
                    }
                    case BracketToken.SUPERSCRIPT_CLOSE: {
                        yModifier += superscriptYOffset;
                        break;
                    }
                    case BracketToken.SUBSCRIPT_OPEN: {
                        int end = TokenUtil.getSubScriptGroupEndIndex(expression, i);
                        i = end + 1;
                        break;
                    }
                    case BracketToken.SUBSCRIPT_CLOSE: {
                        int startIndex = getSubScriptGroupStartIndex(expression, i);
                        if (startIndex > 0) {
                            yModifier = heights.get(startIndex);
                        } else {
                            yModifier = 0;
                        }
                        Log.d(TAG, "getTopNegative: SUBSCRIPT_CLOSE " + yModifier);
                        break;
                    }
                    case BracketToken.NUMERATOR_OPEN: {
                        int j = i + 1;
                        ArrayList<Token> num = new ArrayList<>();
                        int bracketCount = 1;
                        while (bracketCount != 0 && j < expression.size()) {
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

                        int line = getMaxLine(num);
                        if (line == 1) {
                            yModifier -= mTextHeight * 0.5f; //HEREEEEE!!!!!
                        } else {
                            float numNeg = getTopNegative(num);
                            float numHeight = getHeight(num);
                            yModifier -= numHeight + numNeg - mTextHeight * 0.5f;
                        }
                        break;
                    }
                    case BracketToken.DENOMINATOR_OPEN: {
                        ArrayList<Token> denominator = getDenominator(expression, i - 1);
                        if (getMaxLine(denominator) == 1) {
                            yModifier += getHeight(denominator);
                        } else {
                            yModifier += getHeight(denominator) / 2;
                        }
                        break;
                    }
                    case BracketToken.DENOMINATOR_CLOSE: {
                        int bracketCount = 1;
                        int j = i - 1;
                        while (bracketCount > 0 && j > -1) {
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
                        while (bracketCount > 0 && j > -1) {
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
                        } else { //Very first token; cannot checkAll token before it
                            yModifier = 0;
                        }
                        break;
                    }
                    case BracketToken.SQRT_OPEN: {
                        yModifier -= mUnitPadding;
                        break;
                    }
                    case BracketToken.SQRT_CLOSE: {
                        yModifier += mUnitPadding;
                        break;
                    }
                    case BracketToken.ABS_OPEN: {
                        yModifier -= mUnitPadding;
                        break;
                    }
                    case BracketToken.ABS_CLOSE: {
                        yModifier += mUnitPadding;
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
            } else if (token instanceof FunctionToken && token.getType() == FunctionToken.DERIVATIVE) {
                yModifier = -mTextHeight / 2;
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
        Log.d(TAG, "getTopNegative() " + MachineExpressionConverter.toString(expression) + "  returned: " + mostNeg);
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
    public float getWidth(int start, int end, List<Token> expression, List<Float> drawX) {
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
        float v = mTextWidth * filtered.length();
        return drawX.get(end) + v - drawX.get(start);
    }


    /**
     * Determines the height, in pixels of the expression
     *
     * @param expression The expression
     * @return The height of the given expression, in pixels
     */
    public float getHeight(List<Token> expression) {
        float maxHeight = mTextHeight;
        float temp;

        int maxLineNumerator = 1; //maximum line numerator of the fraction
        int maxLineDenominator = 0; //maximum line denominator of the fraction

        float maxHeightNumerator = 0;
        float maxHeightDenominator = 0;

        for (int index = 0; index < expression.size(); index++) {
            Token token = expression.get(index);
            if (token instanceof OperatorToken && token.getType() == OperatorToken.FRACTION) {
                //get numerator of fraction
                ArrayList<Token> numerator = getNumerator(expression, index);
                //get denominator of fraction
                ArrayList<Token> denominator = getDenominator(expression, index);

                //get line of numerator
                int lineN = getMaxLine(numerator);
                //get line of denominator
                int lineD = getMaxLine(denominator);


                if (maxLineNumerator <= lineN || maxLineDenominator <= lineD) {

                    //get height numerator
                    float heightNum = getHeight(numerator);
                    //get height denominator
                    float heightDem = getHeight(denominator);

                    //update max line
                    maxLineNumerator = Math.max(maxLineNumerator, lineN);
                    maxLineDenominator = Math.max(maxLineDenominator, lineD);

                    //update max height of denominator and numerator
                    maxHeightNumerator = Math.max(maxHeightNumerator, heightNum);
                    maxHeightDenominator = Math.max(maxHeightDenominator, heightDem);

                    float newHeight = maxHeightNumerator + maxHeightDenominator + mUnitPadding * 2;

                    //update max height
                    maxHeight = Math.max(maxHeight, newHeight);
                }
            } else if (token instanceof BracketToken && token.getType() == BracketToken.SUPERSCRIPT_OPEN) {

                temp = getHeightSuperscript(expression, index);
                maxHeight = Math.max(temp, maxHeight);

            } else if (token instanceof BracketToken && token.getType() == BracketToken.SUBSCRIPT_OPEN) {

                temp = getHeightSubscript(expression, index);
                maxHeight = Math.max(temp, maxHeight);

            } else if (token instanceof MatrixToken) {

                temp = mTextHeight * (((MatrixToken) token).getNumOfRows() - 1);

                maxHeight = Math.max(temp, maxHeight);

            } else if ((token instanceof FunctionToken && token.getType() == FunctionToken.SQRT)
                    || (token instanceof OperatorToken && token.getType() == OperatorToken.SURD)) {

                ArrayList<Token> sqrtGroup = TokenUtil.getInsideGroupFromStart(expression, index + 1,
                        BracketToken.SQRT_OPEN, BracketToken.SQRT_CLOSE);
                temp = getHeight(sqrtGroup) + mUnitPadding;
                maxHeight = Math.max(temp, maxHeight);

            } else if (token instanceof FunctionToken && token.getType() == FunctionToken.ABS) {

                ArrayList<Token> absGroup = TokenUtil.getInsideGroupFromStart(expression, index + 1,
                        BracketToken.ABS_OPEN, BracketToken.ABS_CLOSE);
                temp = getHeight(absGroup) + mUnitPadding * 2;
                maxHeight = Math.max(temp, maxHeight);

            } else if (token instanceof FunctionToken && token.getType() == FunctionToken.DERIVATIVE) {
                temp = mTextHeight * 2 + mUnitPadding * 3;
                maxHeight = Math.max(temp, maxHeight);
            }
        }
        Log.d(TAG, "getHeight() " + expression + " returned: " + maxHeight);
        return maxHeight;
    }

    private float getHeightSuperscript(List<Token> expression, int index) {

        ArrayList<Token> exponent = TokenUtil.getInsideGroupFromStart(expression, index,
                BracketToken.SUPERSCRIPT_OPEN, BracketToken.SUPERSCRIPT_CLOSE);

        return superscriptYOffset + getHeight(exponent);
    }

    private float getHeightSubscript(List<Token> expression, int index) {

        ArrayList<Token> subscript = TokenUtil.getInsideGroupFromStart(expression, index,
                BracketToken.SUBSCRIPT_OPEN, BracketToken.SUBSCRIPT_CLOSE);

        return superscriptYOffset + getHeight(subscript);
    }

    @Nullable
    public Pair<Integer, Integer> getGroupSqrtIndex(List<Token> expr, int from) {
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

    @Nullable
    public Pair<Integer, Integer> getGroupAbsIndex(List<Token> expr, int i) {
        if (!(expr.get(i) instanceof FunctionToken) || expr.get(i).getType() != FunctionToken.ABS) {
            throw new InvalidParameterException("i");
        }
        int index = i + 2;
        int bracketCount = 1;
        while (index < expr.size() && bracketCount > 0) {
            Token token = expr.get(index);
            if (token instanceof BracketToken && token.getType() == BracketToken.ABS_OPEN) {
                bracketCount++;
            } else if (token instanceof BracketToken && token.getType() == BracketToken.ABS_CLOSE) {
                bracketCount--;
            }
            index++;
        }
        return new Pair<>(i, index - 1);
    }
}