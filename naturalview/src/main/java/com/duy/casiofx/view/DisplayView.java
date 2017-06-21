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
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.WindowManager;

import com.duy.casiofx.factory.FunctionToken;
import com.duy.casiofx.token.BracketToken;
import com.duy.casiofx.token.MatrixToken;
import com.duy.casiofx.token.OperatorToken;
import com.duy.casiofx.token.PlaceholderToken;
import com.duy.casiofx.token.Token;
import com.duy.naturalview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the NaturalView with a cursor which can scroll through the expression.
 *
 * @author Alston Lin
 * @version 3.0
 */
public class DisplayView extends NaturalView {
    private static final String TAG = "DisplayView";
    float cursorX;
    private Cursor cursor = new Cursor();
    private Handler handler = new Handler();
    private final Runnable mBlinkCursor = new Runnable() {
        @Override
        public void run() {
            cursor.toggle();
            invalidate();
            handler.postDelayed(this, cursor.getDelayTime());
        }
    };
    private float cursorPadding;
    private float startX = 0; //Tracks the starting x position at which the canvas will start drawing (allows side scrolling)
    private int cursorIndex = 0; //The index where the cursor is when shown on screen
    private int drawCount = 0;
    private float cursorY = 0;
    private float xPadding;
    private float maxY;
    private int realCursorIndex = 0; //The index of the cursor in the list of tokens
    private boolean functionMode = false; //If this display is for function mode
    private Paint mCursorPaint;
    private ArrayList<Token> expression = new ArrayList<Token>();
    @Nullable
    private OutputView output;
    private ArrayList<Float> drawX;
    private Path sqrtPath = new Path();

    public DisplayView(Context context, AttributeSet attr) {
        super(context, attr);

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.buttonTextColor, typedValue, true);
        int displayColor = typedValue.data;

        mCursorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCursorPaint.setColor(displayColor);
//        handler.post(mBlinkCursor);
    }

    public void setOutput(OutputView o) {
        output = o;
    }

    public void setFontSize(int fontSize) {
        super.setFontSize(fontSize);

        if (output != null) {
            output.setFontSize(fontSize);
        }
        mCursorPaint.setTextSize(fontSize);


        xPadding = mTextHeight / 3;
        cursorPadding = mTextHeight / 10;
    }

    public ArrayList<Token> getExpression() {
        return expression;
    }

    /**
     * Displays the given mathematical expression on the view.
     *
     * @param expression The expression to display
     */
    public void displayInput(ArrayList<Token> expression) {
        this.expression = expression;
        requestLayout();
        invalidate();
    }

    /**
     * Displays the given output to the display.
     *
     * @param tokens The tokens to display
     */
    public void displayOutput(ArrayList<Token> tokens) {
        if (output != null) {
            output.display(tokens);
        }
    }

    /**
     * Clears the input and output of the display.
     */
    public void clear() {
        expression.clear();
        if (output != null) {
            output.display(new ArrayList<Token>());
        }
        requestLayout();
        invalidate();
    }

    /**
     * Overrides the default android drawing method for this View.
     * This is where all the drawing for the display is handled.
     *
     * @param canvas The canvas that will be drawn on
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ArrayList<Float> drawX = prepareDrawing(expression);
        this.drawX = drawX;
        maxY = 0;

        //Cursor stuff
        calculateRealCursorIndex();
        cursorX = getCursorX();
        if (cursorX < startX) {
            startX = cursorX - cursorPadding;
        } else if (cursorX > startX + getWidth()) {
            startX = cursorX - getWidth() + cursorPadding;
        }

        float xModifier = -startX;

        //Special case for Function mode
        if (functionMode) {
            final String s = "f(x)=";
            canvas.drawText(s, xPadding, mTextHeight, mTextPaint);
            xModifier += mTextPaint.measureText(s);
        }

        checkCursorIndex();

        //Counter and state variables
        float INITIAL_Y = -getTopNegative(expression) + mTextHeight;
        float y = INITIAL_Y;


        for (int i = 0; i < expression.size(); i++) {
            Token token = expression.get(i);

            //Calculates the x and y position of the draw position (modified later)
            y = calculateNextY(y, expression, i, INITIAL_Y);
            float x = drawX.get(i) + xModifier;

            //Saves the y coordinate
            heights.add(i, y);
            canvas.drawPoint(x, y, mPathPaint);

            //Draws the text
            if (token instanceof MatrixToken) {
                float matrixY = drawMatrix(x, y, canvas, (MatrixToken) token, mTextPaint);
                if (matrixY > maxY) {
                    maxY = matrixY;
                }
            } else if (token instanceof FunctionToken && token.getType() == FunctionToken.SQRT) {
                Pair<Integer, Integer> index = getGroupSqrt(expression, i + 1);
                List<Token> sqrtFunction = this.expression.subList(index.first, index.second);
                //get max height of expression into sqrt function
                float realHeight = getHeight(sqrtFunction, true);
                float mY = realHeight + getTopNegative(sqrtFunction) - mTextHeight + y;
                Log.d(TAG, "onDraw: " + realHeight);
                sqrtPath.reset();
                sqrtPath.moveTo(x, mY - realHeight / 2.0f);
                sqrtPath.lineTo(x + mTextWidth / 2.0f, mY);

                float nextX = x + mTextWidth;

                sqrtPath.lineTo(nextX, mY - realHeight - mUnitPadding);
                sqrtPath.lineTo(nextX + getWidth(index.first, index.second, this.expression, drawX),
                        mY - realHeight - mUnitPadding);
                sqrtPath.lineTo(nextX + getWidth(index.first, index.second, this.expression, drawX)
                        + mUnitPadding, mY - realHeight);
                canvas.drawPath(sqrtPath, mPathPaint);
                float mostNeg = getTopNegative(sqrtFunction);
                Log.d(TAG, "onDraw: mostNeg " + mostNeg);
//                heights.set(i, y + realHeight);
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
                canvas.drawLine(
                        x - mUnitPadding, //x start
                        y + mUnitPadding, //y start
                        drawX.get(getDenominatorEnd(i, expression)) + xModifier, //x end
                        y + mUnitPadding, //y end
                        mPathPaint);
            }

            //Draws cursor
            if (i == realCursorIndex) {
                drawCursor(canvas, x, y, token, i);
            }

        }
        drawCursorInSpecialPosition(canvas, xModifier, y);
    }

    private void drawCursor(Canvas canvas, float x, float y, Token currentToken, int tokenIndex) {
        if (!cursor.isVisible()) return;
        //Superscripts the cursor if needed
        cursorY = y;
        cursorX = x - cursorPadding;
        if (currentToken instanceof BracketToken && currentToken.getType() == BracketToken.SUPERSCRIPT_CLOSE) {
            cursorY = heights.get(tokenIndex - 1);
        } else if (currentToken instanceof BracketToken && currentToken.getType() == BracketToken.DENOMINATOR_CLOSE) {
            cursorY = heights.get(tokenIndex - 1);
        }
        canvas.drawText("|", cursorX, cursorY, mCursorPaint);
    }

    private void drawCursorInSpecialPosition(Canvas canvas, float xModifier, float y) {
        if (!cursor.isVisible()) return;
        try {
            //Draws cursor in special cases
            if (expression.size() == 0) { //No expression
                canvas.drawText("|", xPadding, y, mCursorPaint);
            } else if (realCursorIndex == expression.size()) { //Last index (or the cursor index is larger than the draw count
                //Moves the cursor up if its superscript
                cursorY = y;
                cursorX = mMaxX + xModifier - cursorPadding;
                canvas.drawText("|", cursorX, cursorY, mCursorPaint);
                realCursorIndex = expression.size();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //When rendering in Android Studio (Android bug)
        }
    }

    /**
     * Calculates the value of realCursorX based on cursorX.
     */
    private void calculateRealCursorIndex() {
        boolean doNotCountNext = false;
        drawCount = 0;
        for (int i = 0; i < expression.size(); i++) {
            Token token = expression.get(i);

            //If the cursor should count the current token
            boolean doNotCount = false;

            //Handle do not counts
            if (doNotCountNext) {
                doNotCountNext = false;
                doNotCount = true;
            }

            //SPECIAL CASES FOR DO NOT COUNTS
            if (token instanceof PlaceholderToken && (token.getType() == PlaceholderToken.SUPERSCRIPT_BLOCK
                    || token.getType() == PlaceholderToken.BASE_BLOCK)) {
                doNotCountNext = true;
            } else if ((token instanceof OperatorToken && token.getType() == OperatorToken.VARROOT) ||
                    (token instanceof BracketToken && token.getType() == BracketToken.SUPERSCRIPT_OPEN) ||
                    (token instanceof BracketToken && token.getType() == BracketToken.DENOMINATOR_OPEN) ||
                    (token instanceof BracketToken && token.getType() == BracketToken.NUMERATOR_OPEN) ||
                    (token instanceof BracketToken && token.getType() == BracketToken.FRACTION_CLOSE) ||
                    (token instanceof OperatorToken && token.getType() == OperatorToken.FRACTION)) {
                doNotCount = true;
            }

            //Draws cursor
            if (!doNotCount) {
                if (drawCount == cursorIndex) {
                    realCursorIndex = i;
                }
                drawCount++;
            }
        }
        //Draws cursor in special cases
        if (expression.size() == 0) { //Nothing there
            realCursorIndex = 0;
        } else if (cursorIndex >= drawCount) { //Last index (or the cursor index is larger than the draw count
            cursorIndex = drawCount;
            realCursorIndex = expression.size();
        }
    }

    /**
     * Checks if the cursor is shown on the screen. If not, it will redraw the entire canvas through
     * onDraw() again.
     */
    private float getCursorX() {
        float cursorX;
        if (realCursorIndex > expression.size()) {
            cursorX = drawX.get(drawX.size() - 1); //Last index
        } else {
            cursorX = drawX.get(realCursorIndex);
        }
        return cursorX;
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

    /**
     * Checks the index of the cursor to make sure it is valid.
     */
    private void checkCursorIndex() {
        if (expression.size() == 0) {
            cursorIndex = 0;
        } else if (cursorIndex > expression.size()) {
            cursorIndex = expression.size();
        }
    }

    /**
     * Overrides the superclass' onMeasure to set the dimensions of the View
     * according to the parent's.
     *
     * @param widthMeasureSpec  given by the parent class
     * @param heightMeasureSpec given by the parent class
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Canvas canvas = new Canvas();
        this.draw(canvas); //Lazy way to calculate maxX and maxY

        //Finds max X
        calculateDrawX(expression);
        if (drawX.size() > 1) {
            mMaxX = drawX.get(drawX.size() - 1);
        }

        int height = expression.size() == 0 ? (int) mTextHeight : (int) (maxY + mTextHeight);
        this.setMeasuredDimension(parentWidth, height);
    }

    /**
     * Scrolls the display left if possible.
     */
    public void scrollLeft() {
        if (cursorIndex > 0) {
            setCursorIndex(cursorIndex - 1);
        } else {
            setCursorIndex(drawCount);
        }
    }

    /**
     * Resets the scrolling (to the initial position)
     */
    public void reset() {
        startX = 0;
        cursorIndex = 0;
    }

    /**
     * Scrolls the display right if possible.
     */
    public void scrollRight() {
        if (cursorIndex < drawCount) {
            setCursorIndex(cursorIndex + 1);
        } else if (cursorIndex == drawCount) { //Wraps around
            setCursorIndex(0);
        } else {
            throw new IllegalStateException("Cursor Index is greater than draw count");
        }
    }

    /**
     * @return The index of the cursor
     */
    public int getRealCursorIndex() {
        return realCursorIndex;
    }

    public int getCursorIndex() {
        return cursorIndex;
    }

    public void setCursorIndex(int index) {
        cursorIndex = index;
        invalidate();
    }
}