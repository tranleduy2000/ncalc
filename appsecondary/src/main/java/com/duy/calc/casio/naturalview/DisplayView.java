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
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;

import com.duy.calc.casio.token.BracketToken;
import com.duy.calc.casio.token.DigitToken;
import com.duy.calc.casio.token.FunctionToken;
import com.duy.calc.casio.token.MatrixToken;
import com.duy.calc.casio.token.OperatorToken;
import com.duy.calc.casio.token.PlaceholderToken;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.tokenizer.TokenUtil;

import java.util.ArrayList;
import java.util.List;

import static com.duy.calc.casio.tokenizer.TokenUtil.getDenominatorEnd;

public class DisplayView extends NaturalView {
    private static final String TAG = "DisplayView";

    protected CursorState cursor = new CursorState();
    protected List<Token> expression = new ArrayList<>();
    protected ArrayList<Float> drawX;

    protected float cursorX;
    private Handler handler = new Handler();

    private float cursorPadding;
    private float startX = 0; //Tracks the starting x position at which the canvas will start drawing (allows side scrolling)
    private int cursorIndex = 0; //The index where the cursor is when shown on screen
    private int drawCount = 0;
    private float cursorY = 0;
    private float xPadding;
    private float maxY;
    private int mRealCursorIndex = 0; //The index of the cursor in the list of expression

    private Path path = new Path();

    public DisplayView(Context context, AttributeSet attr) {
        super(context, attr);
    }


    public void setFontSize(int fontSize) {
        super.setFontSize(fontSize);
        xPadding = mTextHeight / 3;
        cursorPadding = mTextHeight / 10;
    }

    public List<Token> getExpression() {
        return expression;
    }

    public void setExpression(List<Token> expression) {
        this.expression = expression;
    }

    /**
     * Displays the given mathematical expression on the view.
     *
     * @param expression The expression to mDisplay
     */
    public void display(List<Token> expression) {
        setExpression(expression);
        requestLayout();
        invalidate();
    }


    /**
     * Clears the input and output of the mDisplay.
     */
    public void clear() {
        expression.clear();
        requestLayout();
        invalidate();
    }

    /**
     * Overrides the default android drawing method for this View.
     * This is where all the drawing for the mDisplay is handled.
     *
     * @param canvas The canvas that will be drawn on
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawExpression(canvas);
    }

    protected float drawExpression(Canvas canvas) {
        mapHeights.clear();

        Log.d(TAG, "drawExpression() called with: canvas = [" + canvas + "]" + this);

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

        checkCursorIndex();

        //Counter and state variables
        float INITIAL_Y = -getTopNegative(expression) + mTextHeight * 1.5f;
        float y = INITIAL_Y;


        for (int i = 0; i < expression.size(); i++) {
            Token token = expression.get(i);

            //Calculates the x and y position of the draw position (modified later)
            y = calculateNextY(y, expression, i, INITIAL_Y);
            float x = drawX.get(i) + xModifier;

            //Saves the y coordinate
            heights.add(i, y);
//            canvas.drawPoint(x, y, mPathPaint); //for test

            if (token instanceof MatrixToken) {
                drawMatrix(canvas, x, y, i, token);
            } else if ((token instanceof FunctionToken && token.getType() == FunctionToken.SQRT)
                    || (token instanceof OperatorToken && token.getType() == OperatorToken.SURD)) {
                drawSqrtSign(canvas, x, y, i);
            } else if (token instanceof FunctionToken && token.getType() == FunctionToken.ABS) {
                drawAbsSign(canvas, x, y, i);
            } else if (token instanceof FunctionToken && token.getType() == FunctionToken.DERIVATIVE) {
                drawDerivative(canvas, x, y, i);
            } else if ((token instanceof BracketToken) && (token.getType() == BracketToken.PARENTHESES_OPEN)) {
                drawOpenBracket(canvas, x, y, i, BracketToken.PARENTHESES_OPEN, BracketToken.PARENTHESES_CLOSE);

            } else if (token instanceof BracketToken && token.getType() == BracketToken.DERIVATIVE_OPEN) {
                drawOpenBracket(canvas, x, y, i, BracketToken.DERIVATIVE_OPEN, BracketToken.DERIVATIVE_CLOSE);

            } else if (token instanceof BracketToken && token.getType() == BracketToken.LOGN_OPEN) {
                drawOpenBracket(canvas, x, y, i, BracketToken.LOGN_OPEN, BracketToken.LOGN_CLOSE);

            } else if ((token instanceof BracketToken) && (token.getType() == BracketToken.PARENTHESES_CLOSE)) {
                drawCloseBracket(canvas, x, y, i, BracketToken.PARENTHESES_OPEN, BracketToken.PARENTHESES_CLOSE);

            } else if (token instanceof BracketToken && token.getType() == BracketToken.DERIVATIVE_CLOSE) {
                drawCloseBracket(canvas, x, y, i, BracketToken.DERIVATIVE_OPEN, BracketToken.DERIVATIVE_CLOSE);

            } else if (token instanceof BracketToken && token.getType() == BracketToken.LOGN_CLOSE) {
                drawCloseBracket(canvas, x, y, i, BracketToken.LOGN_OPEN, BracketToken.LOGN_CLOSE);

            } else if (token instanceof PlaceholderToken) {
                drawBaseBlock(canvas, x, y, i, token);
            } else if (token instanceof OperatorToken && token.getType() == OperatorToken.FRACTION) {
                drawFractionSign(canvas, x, y, i);
            } else if (token instanceof DigitToken && ((DigitToken) token).getValue() <= -2) {
                mTextPaint.setColor(mBaseTextColor);
                drawString(canvas, x, y, i, token);
                mTextPaint.setColor(mTextColor);
            } else {
                drawString(canvas, x, y, i, token);
                if (token instanceof BracketToken && token.getType() == BracketToken.SUBSCRIPT_CLOSE) {
                    y -= superscriptYOffset;
                }
            }
            //Updates maxY
            if (y > maxY) {
                maxY = y;
            }

            drawCursorIfNeed(canvas, i, token, x, heights.get(i));
        }
        drawCursorInSpecialPosition(canvas, xModifier, y);
        return maxY;
    }

    private void drawCloseBracket(Canvas canvas, float x, float y, int i, int typeOpen, int typeClose) {
        List<Token> groupBracket = TokenUtil.getInsideGroupFromEnd(expression, i, typeOpen, typeClose);
        //get max height of expression into sqrt function
        float realHeight = getHeight(groupBracket);
        float mY;

        mY = realHeight + getTopNegative(groupBracket) - mTextHeight + y;

        float width = mTextWidth * 0.75f;

        path.reset();
        path.moveTo(x + mUnitPadding, mY - realHeight);
        path.lineTo(x + width / 2, mY - realHeight + mUnitPadding);
        path.lineTo(x + width / 2, mY - mUnitPadding);
        path.lineTo(x + mUnitPadding, mY);
        canvas.drawPath(path, mPathPaint);
    }

    private void drawOpenBracket(Canvas canvas, float x, float y, int i, int openType, int closeType) {
        List<Token> groupBracket = TokenUtil.getInsideGroupFromStart(expression, i, openType, closeType);
        float height = getHeight(groupBracket);
        float mY;

        mY = height + getTopNegative(groupBracket) - mTextHeight + y;

        //width of parentheses = 0.75 * mTextWidth
        float width = mTextWidth * 0.75f;

        path.reset();
        path.moveTo(x + width - mUnitPadding, mY - height);
        path.lineTo(x + width / 2, mY - height + mUnitPadding);
        path.lineTo(x + width / 2, mY - mUnitPadding);
        path.lineTo(x + width - mUnitPadding, mY);
        canvas.drawPath(path, mPathPaint);
    }

    /**
     * draw d/dx
     *
     * @param x - x coordinate
     * @param y - y coordinate
     * @param i
     */
    private void drawDerivative(Canvas canvas, float x, float y, int i) {
        canvas.drawText("d", x + mTextWidth * 0.5f, y - mTextHeight * 0.5f, mTextPaint);
        canvas.drawLine(x, y - mUnitPadding * 2,
                x + mTextWidth * 2, y - mUnitPadding * 2, mPathPaint);
        canvas.drawText("dx", x, y + mTextHeight * 0.5f + mUnitPadding * 2, mTextPaint);
    }

    private void drawString(Canvas canvas, float x, float y, int i, Token token) {
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
                    Paint paint = mTextPaint;
                    if (onSubscript) {
                        paint = mSubscriptPaint;
                    }
                    canvas.drawText(Character.toString(c), currentX, y, paint);
                    currentX += paint.measureText(Character.toString(c));
                }
            }
        }
    }

    private void drawBaseBlock(Canvas canvas, float x, float y, int i, Token token) {
        if (token instanceof PlaceholderToken && token.getType() == PlaceholderToken.COMMA) {
            drawString(canvas, x, y, i, token);
        } else {
            canvas.drawRect(x + mUnitPadding, y - mTextHeight,
                    x + mTextWidth - mUnitPadding, y, mPathPaint);
        }
    }

    private void drawAbsSign(Canvas canvas, float x, float y, int i) {
        Pair<Integer, Integer> index = getGroupAbsIndex(expression, i);
        List<Token> absFunc = this.expression.subList(index.first, index.second);
        //get max height of expression into sqrt function
        float realHeight = getHeight(absFunc);
        float mY = realHeight + getTopNegative(absFunc) - mTextHeight + y;
        float width = getWidth(index.first, index.second, expression, drawX);

        path.reset();
        path.moveTo(x + mUnitPadding, mY - realHeight);
        path.lineTo(x + mUnitPadding, mY);
        canvas.drawPath(path, mPathPaint);
        path.reset();
        path.moveTo(x + width + mUnitPadding, mY - realHeight);
        path.lineTo(x + width + mUnitPadding, mY);
        canvas.drawPath(path, mPathPaint);
    }

    private void drawMatrix(Canvas canvas, float x, float y, int i, Token token) {
        float matrixY = drawMatrix(x, y, canvas, (MatrixToken) token, mTextPaint);
        if (matrixY > maxY) {
            maxY = matrixY;
        }
    }

    private void drawFractionSign(Canvas canvas, float x, float y, int i) {
        int denominatorEnd = getDenominatorEnd(i, expression);
        float width = getWidth(i, denominatorEnd, expression, drawX);
        canvas.drawLine(x - mUnitPadding, y + mUnitPadding,
                x + width + mUnitPadding, y + mUnitPadding, mPathPaint);
    }

    private void drawCursorIfNeed(Canvas canvas, int i, Token token, float x, float y) {
        //Draws cursor
        if (i == mRealCursorIndex && cursor.isVisible()) {
            drawCursor(canvas, x, y, token, i);
        }
    }

    private void drawSqrtSign(Canvas canvas, float x, float y, int i) {
        Pair<Integer, Integer> index = getGroupSqrtIndex(expression, i + 1);
        List<Token> sqrtFunction;
        if (index != null) {
            sqrtFunction = this.expression.subList(index.first, index.second);
        } else {
            return;
        }
        //get max height of expression into sqrt function
        float realHeight = getHeight(sqrtFunction);
        float mY = realHeight + getTopNegative(sqrtFunction) - mTextHeight + y + mUnitPadding;

        float nextX = x + mTextWidth;
        float width = getWidth(index.first, index.second, this.expression, drawX);

        path.reset();
        path.moveTo(x + mUnitPadding, mY - realHeight * 0.5f);
        path.lineTo(x + mTextWidth * 0.5f, mY);
        path.lineTo(nextX, mY - realHeight - mUnitPadding);
        path.lineTo(nextX + width + mUnitPadding, mY - realHeight - mUnitPadding);
//        path.lineTo(nextX + width + mUnitPadding, mY - realHeight);

        canvas.drawPath(path, mPathPaint);

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
        canvas.drawText("|", cursorX, cursorY, mPathPaint);
    }

    private void drawCursorInSpecialPosition(Canvas canvas, float xModifier, float y) {
        if (!cursor.isVisible()) return;

        try {
            //Draws cursor in special cases
            if (expression.size() == 0) { //No expression
                canvas.drawText("|", xPadding, y + mTextHeight, mPathPaint);
            } else if (mRealCursorIndex == expression.size()) { //Last index (or the cursor index is larger than the draw count
                //Moves the cursor up if its superscript
                cursorY = y;
                cursorX = mMaxX + xModifier - cursorPadding;
                canvas.drawText("|", cursorX, cursorY, mPathPaint);
                mRealCursorIndex = expression.size();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
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
            if ((token instanceof PlaceholderToken)
                    && ((token.getType() == PlaceholderToken.SUPERSCRIPT_BLOCK)
                    || (token.getType() == PlaceholderToken.BASE_BLOCK)
                    || (token.getType() == PlaceholderToken.SUBSCRIPT_BLOCK))) {
                doNotCountNext = true;
            } else if ((token instanceof OperatorToken && token.getType() == OperatorToken.SURD) ||
                    (token instanceof BracketToken && token.getType() == BracketToken.SUPERSCRIPT_OPEN) ||
                    (token instanceof BracketToken && token.getType() == BracketToken.DENOMINATOR_OPEN) ||
                    (token instanceof BracketToken && token.getType() == BracketToken.NUMERATOR_OPEN) ||
                    (token instanceof BracketToken && token.getType() == BracketToken.FRACTION_CLOSE) ||
                    (token instanceof BracketToken && token.getType() == BracketToken.SUBSCRIPT_OPEN) ||
                    (token instanceof BracketToken && token.getType() == BracketToken.SQRT_OPEN) ||
                    (token instanceof OperatorToken && token.getType() == OperatorToken.FRACTION) ||
                    (token instanceof BracketToken && token.getType() == BracketToken.ABS_OPEN) ||
                    (token instanceof BracketToken && token.getType() == BracketToken.LOGN_OPEN)) {
                doNotCount = true;
            } else if ((token instanceof BracketToken && token.getType() == BracketToken.PARENTHESES_OPEN)) {
                if (i > 0) {
                    Token next = expression.get(i - 1);
                    if (next instanceof FunctionToken) {
                        doNotCount = true;
                    }
                }
            }

            //Draws cursor
            if (!doNotCount) {
                if (drawCount == cursorIndex) {
                    mRealCursorIndex = i;
                }
                drawCount++;
            }
        }
        //Draws cursor in special cases
        if (expression.size() == 0) { //Nothing there
            mRealCursorIndex = 0;
        } else if (cursorIndex >= drawCount) { //Last index (or the cursor index is larger than the draw count
            cursorIndex = drawCount;
            mRealCursorIndex = expression.size();
        }
    }

    /**
     * Checks if the cursor is shown on the screen. If not, it will redraw the entire canvas through
     * onDraw() again.
     */
    private float getCursorX() {
        float cursorX;
        if (mRealCursorIndex > expression.size()) {
            cursorX = drawX.get(drawX.size() - 1); //Last index
        } else {
            cursorX = drawX.get(mRealCursorIndex);
        }
        return cursorX;
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
        float maxY = drawExpression(new Canvas());
        int height = expression.size() == 0 ? (int) mTextHeight : (int) (maxY + mTextHeight);
        this.setMeasuredDimension(parentWidth, height);
    }

    /**
     * Scrolls the mDisplay left if possible.
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
    public void resetPosition() {
        startX = 0;
        cursorIndex = 0;
        mRealCursorIndex = 0;
    }

    /**
     * Scrolls the mDisplay right if possible.
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
        return mRealCursorIndex;
    }

    public void setRealCursorIndex(int mRealCursorIndex) {
        this.mRealCursorIndex = mRealCursorIndex;
    }

    public int getCursorIndex() {
        return cursorIndex;
    }

    public void setCursorIndex(int index) {
        cursorIndex = index;
    }

    public CursorState getCursor() {
        return cursor;
    }
}