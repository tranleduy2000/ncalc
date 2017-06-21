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
import android.util.AttributeSet;

import com.duy.casiofx.util.Utility;
import com.duy.casiofx.token.Token;

import java.util.ArrayList;

/**
 * Modified DisplayView to display outputs.
 *
 * @author Alston Lin
 * @version 3.0
 */
public class OutputView extends NaturalView {

    private ArrayList<Token> expression = new ArrayList<Token>();

    public OutputView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    /**
     * Displays the given mathematical expression on the view.
     *
     * @param expression The expression to display
     */
    public void display(ArrayList<Token> expression) {
        this.expression = Utility.cleanupExpressionForReading(expression);
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
        drawExpression(expression, canvas, 0);
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
        //Finds max X
        ArrayList<Float> drawX = calculateDrawX(expression);
        if (drawX.size() > 1) {
            mMaxX = drawX.get(drawX.size() - 1);
        }
        float maxY = drawExpression(expression, new Canvas(), 0);
        int width = (int) (mMaxX + mTextHeight);
        int height = expression.size() == 0 ? (int) mTextHeight : (int) (maxY + mTextHeight);
        this.setMeasuredDimension(width, height);
    }

}