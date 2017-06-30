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

package com.duy.calc.casio.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import com.duy.calc.casio.evaluator.Utility;
import com.duy.calc.casio.token.Token;

import java.util.ArrayList;

/**
 * A custom view that will show the graph of a function.
 *
 * @author Alston Lin, Jason Fok
 * @version 3.0
 */
public class GraphView extends View {

    private static final int NUM_OF_POINTS = 10000;
    private static final int NUM_OF_GRIDELINES = 10; //NOTE: NOT EXACT; IT WILL BE WITHIN AN ORDER OF MAGNITUDE
    private static final int MAX_INCREMENT_TEXTS = 15;
    private PopupWindow popupWindow;
    private ArrayList<Token> function;
    private RectF exitRect; //The rectangle of tne exit button
    private Paint axisPaint;
    private Paint exitPaint;
    private Paint gridPaint;
    private Paint incrementPaint;
    private Paint blackPaint;
    private Paint backgroundPaint;
    private double originX = 0, originY = 0; //The origin of the graph
    private int width;
    private int height;
    //The bounds for the graph
    private float lowerX = 0;
    private float lowerY = 0;
    private float upperX = 0;
    private float upperY = 0;

    private OnTouchListener listener = new OnTouchListener() {

        /**
         * Whenever a touch occurs, this is the method that will be called.
         *
         * @param v The view that was touched (should be this)
         * @param event Information regarding the touch
         * @return If the touch was consumed (if false, Android will use its default touch
         * reactions)
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            //Checks if user touched the exit Button
            if (exitRect.contains(x, y)) {
                exit();
            }
            return true;
        }
    };

    /**
     * Constructor for a Graph View, a custom view with the sole purpose
     * of graphing a function.
     *
     * @param context The context which this view will be in
     * @param attrs   The attribute set
     */
    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Sets up paints
        axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisPaint.setColor(Color.RED);
        axisPaint.setStrokeWidth(3);

        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(Color.GREEN);
        gridPaint.setStrokeWidth(1);

        incrementPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        incrementPaint.setColor(Color.BLUE);
        incrementPaint.setStrokeWidth(2);
        incrementPaint.setTextSize(18);

        blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStrokeWidth(5);

        exitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        exitPaint.setColor(Color.BLACK);
        exitPaint.setTextSize(52);
        exitPaint.setFakeBoldText(true);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);

        width = getWidth();
        height = getHeight();
        setOnTouchListener(listener);
    }

    /**
     * Sets what function this will graph.
     *
     * @param function The function to graph
     */
    public void setFunction(ArrayList<Token> function) {
        this.function = Utility.setupExpression(Utility.condenseDigits(function));
    }

    /**
     * Sets the popup window that this view will show in.
     *
     * @param popupWindow The popup window.
     */
    public void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    /**
     * Sets the bounds of what this graph will show.
     *
     * @param lowerX The minimum x value
     * @param upperX The maximum x value
     * @param lowerY The minimum y value
     * @param upperY The maximum y value
     */
    public void setBounds(float lowerX, float upperX, float lowerY, float upperY) {
        this.lowerX = lowerX;
        this.upperX = upperX;
        this.lowerY = lowerY;
        this.upperY = upperY;
    }

    /**
     * Overrides the default Android draw command to manually draw the screen.
     *
     * @param canvas The canvas to draw on
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (function != null && upperY != lowerY) { //When the function and bounds have been defined
            //Draws the background
            canvas.drawRect(0, 0, width, height, backgroundPaint);
            drawExit(canvas);
            drawGraph(canvas);
            drawAxis(canvas);
        }
    }

    /**
     * Main graphing algorithm for the buttonMode. Takes the tokens
     * given and draws it onto the given canvas.
     *
     * @param canvas The canvas to draw on
     */
    private void drawGraph(Canvas canvas) {
        final int NULL = 0, POSITIVE = 1, NEGATIVE = 2;

        ArrayList<Double> xValues = new ArrayList<>();
        ArrayList<Double> yValues = new ArrayList<>();

        //Calculates important values that will adjust how the graph would look
        double xRange = upperX - lowerX;
        double xMultiplier = width / xRange;
        double yMultiplier = height / (upperY - lowerY);

        //Plots the 100 points across the screen
        for (int i = 0; i <= NUM_OF_POINTS; i++) {
            double x = lowerX + i * (xRange / NUM_OF_POINTS);
            double y = Utility.valueAt(function, x);
            xValues.add(x);
            yValues.add(y);
        }

        //Keeps tracks on the slopes
        ArrayList<Double> slopes = findSlopes(yValues, lowerX, xRange / NUM_OF_POINTS);
        int beforeLastSlopePositive = NULL;
        int lastSlopePositive = NULL;
        //Now draws a line in between each point
        for (int i = 0; i < NUM_OF_POINTS; i++) {
            Double startX = xValues.get(i);
            Double endX = xValues.get(i + 1);
            Double startY = yValues.get(i);
            Double endY = yValues.get(i + 1);
            int slopeIsPositive;
            if (startY != Integer.MAX_VALUE && endY != Integer.MAX_VALUE) { //Does not graph points that DNE or outside the screen
                slopeIsPositive = i + 1 >= slopes.size() ? NULL : slopes.get(i + 1) > 0 ? POSITIVE : NEGATIVE; //Checks after
                //Checks for conditions for asympotes: +-+ or -+- slope
                boolean hasAsymptote = (beforeLastSlopePositive == POSITIVE && lastSlopePositive == NEGATIVE && slopeIsPositive == POSITIVE) ||
                        (beforeLastSlopePositive == NEGATIVE && lastSlopePositive == POSITIVE && slopeIsPositive == NEGATIVE);
                if (!hasAsymptote) {
                    startX = (startX - lowerX) * xMultiplier;
                    endX = (endX - lowerX) * xMultiplier;
                    startY = (startY - lowerY) * yMultiplier;
                    endY = (endY - lowerY) * yMultiplier;
                    canvas.drawLine(startX.floatValue(), height - startY.floatValue(), endX.floatValue(), (float) (height - endY), blackPaint);
                } else {
                    slopeIsPositive = NULL;
                }
            } else { //Either does not matter or points DNE
                slopeIsPositive = NULL;
            }
            beforeLastSlopePositive = lastSlopePositive;
            lastSlopePositive = slopeIsPositive;
            //Saves the origin coordinate
            originX = -lowerX * xMultiplier;
            originY = -lowerY * yMultiplier;
        }
    }

    /**
     * Finds th slopes in between each points on the given list.
     *
     * @param yValues    The list of y values
     * @param lowerX     The starting X value
     * @param increments Distance between each point
     * @return The slopes in between the points
     */
    private ArrayList<Double> findSlopes(ArrayList<Double> yValues, double lowerX, double increments) {
        ArrayList<Double> slopes = new ArrayList<>();
        double startX = lowerX;
        for (int i = 0; i < NUM_OF_POINTS; i++) {
            double endX = lowerX + (i + 1) * increments;
            Double startY = yValues.get(i);
            Double endY = yValues.get(i + 1);
            double slope = Integer.MAX_VALUE; //Default value of MAX_VALUE
            if (startY != Integer.MAX_VALUE && endY != Integer.MAX_VALUE) { //Both points exists
                slope = (endY - startY) / (endX - startX);
            }
            slopes.add(slope);
            startX = endX;
        }
        return slopes;
    }

    /**
     * Draws the axises for the graph. This will draw the x and y axises
     * as well as the incrementations along it.
     *
     * @param canvas The canvas to draw on
     */
    private void drawAxis(Canvas canvas) {
        final double TOLERANCE = 1e-3f;
        double xRange = (upperX - lowerX);
        double yRange = (upperY - lowerY);
        double xMultiplier = width / xRange;
        double yMultiplier = height / yRange;
        //X axis
        canvas.drawLine(0, height - (float) originY, width, height - (float) originY, axisPaint);
        //Calculates when the increments should be drawn
        float approximateDistance = (upperX - lowerX) / NUM_OF_GRIDELINES;
        int ordersOfMag = (int) Math.floor(Math.log10(approximateDistance)); //Finds how many orders of magnitudes the dist is
        float distBetweenGridlines = (float) Math.pow(10, ordersOfMag);
        int totalNumLines = (int) (xRange / distBetweenGridlines);
        int gridlinesBetweenTexts = totalNumLines / MAX_INCREMENT_TEXTS; //Number of gridlines between each text
        //Just in case
        if (gridlinesBetweenTexts == 0) {
            gridlinesBetweenTexts = 1;
        }
        int textCounter = 0;
        for (float x = 0; x <= xRange; x += distBetweenGridlines) { //Draws the gridlines and increments
            if (Math.abs((x + lowerX) / distBetweenGridlines) > TOLERANCE) { //Does not include origin
                canvas.drawLine((float) (x * xMultiplier), 0, (float) (x * xMultiplier), height, gridPaint);
                //Draws the texts for increments
                textCounter++;
                if (textCounter == gridlinesBetweenTexts) {
                    String incrementText = Double.toString(Utility.round(x + lowerX, 3)); //Max 3 decimals
                    incrementText = !incrementText.contains(".") ? incrementText : incrementText.replaceAll("0*$", "").replaceAll("\\.$", "");
                    canvas.drawText(incrementText, (float) (x * xMultiplier), height - (float) originY, incrementPaint);
                    textCounter = 0;
                }
            }
        }
        //Y axis
        canvas.drawLine((float) originX, 0, (float) originX, height, axisPaint);
        //Calculates when the increments should be drawn
        approximateDistance = (upperY - lowerY) / NUM_OF_GRIDELINES;
        ordersOfMag = (int) Math.floor(Math.log10(approximateDistance)); //Finds how many orders of magnitudes the dist is
        distBetweenGridlines = (float) Math.pow(10, ordersOfMag);
        totalNumLines = (int) (yRange / distBetweenGridlines);
        gridlinesBetweenTexts = totalNumLines / MAX_INCREMENT_TEXTS; //Number of gridlines between each text
        //Just in case
        if (gridlinesBetweenTexts == 0) {
            gridlinesBetweenTexts = 1;
        }
        textCounter = 0;
        for (float y = 0; y <= yRange; y += distBetweenGridlines) { //Draws the gridlines and increments
            if (Math.abs((y + lowerY) / distBetweenGridlines) > TOLERANCE) { //Does not include origin
                canvas.drawLine(0, (float) (y * yMultiplier), width, (float) (y * yMultiplier), gridPaint);
                //Draws the texts for increments
                textCounter++;
                if (textCounter == gridlinesBetweenTexts) {
                    String incrementText = Double.toString(Utility.round(y + lowerY, 5)); //Max 5 decimals
                    incrementText = !incrementText.contains(".") ? incrementText : incrementText.replaceAll("0*$", "").replaceAll("\\.$", "");
                    canvas.drawText(incrementText, (float) originX, height - (float) (y * yMultiplier), incrementPaint);
                    textCounter = 0;
                }
            }
        }
    }

    /**
     * Draws the exit rectangle.
     *
     * @param canvas The canvas to draw on
     */
    private void drawExit(Canvas canvas) {
        //Exit rectangle
        exitRect = new RectF(width / 1.1f, 0, width, height / 20f);
        canvas.drawRect(exitRect, backgroundPaint); //Invisible rectangle; only used to sense touches
        canvas.drawText("X", width / 1.065f, height / 25f, exitPaint);
    }

    /**
     * Called by Android whenver the screen size changes
     *
     * @param w    New screen width
     * @param h    New screen height
     * @param oldw Old screen width
     * @param oldh Old screen height
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
    }

    /**
     * Exits the graphing window.
     */
    private void exit() {
        popupWindow.dismiss();
    }

}
