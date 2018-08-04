/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.ncalc.graph;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.text.DecimalFormat;

import edu.hws.jcm.data.ParseError;
import edu.hws.jcm.data.Parser;


public class Graph2DView extends View implements OnTouchListener {

    public static final int[][] colors = new int[][]{
            {204, 0, 0},
            {102, 153, 255},
            {255, 102, 0},
            {0, 204, 0},
            {255, 204, 0},
            {0, 51, 153}};
    public static final String TAG = "Graph2D";
    public static Parser constantParser = new Parser(Parser.STANDARD_FUNCTIONS | Parser.OPTIONAL_PARENS
            | Parser.OPTIONAL_STARS | Parser.OPTIONAL_SPACES
            | Parser.BRACES | Parser.BRACKETS);

    static {
        GraphMath.setUpParser(constantParser);
    }

    private final DecimalFormat mDecimalFormat = new DecimalFormat("#.##");
    private final TextPaint mTextHintAxis = new TextPaint();
    private final Paint mFunctionPaint = new Paint();

    public String[] functions, paramX, paramY;
    public boolean[] graphable = new boolean[]{false, false, false, false, false, false};
    public Context context;
    public double mMinX = -3, mMaxX = 3, mMinY = -5, mMaxY = 5, scaleX = 1,
            scaleY = 1, initX = 0, initY = 0, startPolar = -1 * Math.PI, endPolar = Math.PI,
            startT = -20, endT = 20;
    public GraphHelper mGraphHelper;
    public float startX, startY, pinchDist;
    public int width, height, interA, interB, mode;
    private Paint axisPaint = new Paint();
    private double tracexVal, traceyVal, traceDeriv;
    private int traceFun = -1;
    private boolean allowMove;
    private boolean trace = false;
    private boolean deriv = false;
    private boolean choose = false;
    private boolean rect = true;

    private Paint.FontMetrics mTextAxisFontMetrics;

    public Graph2DView(Context context) {
        super(context);
        setUp(context);
    }


    public Graph2DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp(context);

    }

    public Graph2DView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp(context);
    }

    public void setUp(Context c) {
        if (!isInEditMode()) {
            context = c;
            setDisplay();
            setMode();
            importFunctions();
            mGraphHelper = new GraphHelper(this);

            axisPaint.setColor(Color.WHITE);
            axisPaint.setStrokeWidth(4f);

            mTextHintAxis.setAntiAlias(true);
            mTextHintAxis.setTypeface(Typeface.MONOSPACE);
            mTextHintAxis.setColor(Color.WHITE);
            mTextHintAxis.setTextSize(spTpPx(10));
            mTextAxisFontMetrics = mTextHintAxis.getFontMetrics();

            mFunctionPaint.setStrokeWidth(dpTpPx(2));
            mFunctionPaint.setAntiAlias(true);

            mGraphHelper = new GraphHelper(this);
        }
    }

    private float spTpPx(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, getResources().getDisplayMetrics());
    }

    private float dpTpPx(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    private void setDisplay() {
        width = getWidth();
        height = getHeight();
        if (width == 0) width = 1;
        if (height == 0) height = 1;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        if (width == 0) width = 1;
        if (height == 0) height = 1;
    }

    public void importFunctions() {
        Log.d(TAG, "importFunctions: ");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        functions = new String[6];
        try {
            initX = constantParser.parse(sp.getString("rkX", "")).getVal();
            initY = constantParser.parse(sp.getString("rkY", "")).getVal();
        } catch (ParseError e) {
            initX = 0;
            initY = 0;
        }
        for (int i = 0; i < 6; i++) {
            functions[i] = sp.getString("f" + (i + 1), "");
        }
    }

    /*
     * Redraws the com.duy.example.com.duy.calculator.graph
     */
    public void drawGraph() {
        Log.d(TAG, "drawGraph");
        setDisplay();
        importFunctions();
        mGraphHelper = new GraphHelper(this);
        invalidate();
    }

    private void drawFunction(Canvas canvas) {
        for (int i = 0; i < 6; i++) {
            if (graphable[i]) {
                mFunctionPaint.setColor(Color.rgb(colors[i][0], colors[i][1], colors[i][2]));
                double y1 = mGraphHelper.getVal(i, mMinX);
                double y2 = y1;
                for (int j = 0; j < width; j++) {
                    y1 = y2;
                    y2 = mGraphHelper.getVal(i, mMinX + ((double) j + 1) * (mMaxX - mMinX) / width);
                    if (y1 != Double.POSITIVE_INFINITY && y2 != Double.POSITIVE_INFINITY) {
                        if ((y1 >= 0 || y1 < 0) && (y2 >= 0 || y2 < 0)) {
                            if (!((y1 > 20) && (y2 < -20)) && !((y1 < -20) && (y2 > 20))) {
                                canvas.drawLine(j, getyPixel(y1), j + 1, getyPixel(y2), mFunctionPaint);
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * Draws a solution to a differential equation to the com.duy.example.com.duy.calculator.graph using the
     * classical Runge-Kutta method
     */
    private void drawRK(Canvas canvas) {
//        // Set paint Color
//        Paint paint = new Paint();
//        paint.setARGB(255, 128, 0, 128);
//
//        double[] k = new double[4];
//        double y = initY;
//
//        // Draws the portion of the com.duy.example.com.duy.calculator.graph between x=0 and the right edge
//        for (int i = getxPixel(initX); i < width; i++) {
//            k[0] = (maxX - minX) / width
//                    * helper.getRKVal(getX(i), y);
//            k[1] = (maxX - minX)
//                    / width
//                    * helper.getRKVal(getX((float) (i + .5)), y
//                    + .5 * k[0]);
//            k[2] = (maxX - minX)
//                    / width
//                    * helper.getRKVal(getX((float) (i + .5)), y
//                    + .5 * k[1]);
//            k[3] = (maxX - minX) / width
//                    * helper.getRKVal(getX(i + 1), y + k[2]);
//            double tempY = y + (k[0] + 2 * k[1] + 2 * k[2] + k[3]) / 6;
//            canvas.drawLine(i, getyPixel(y), i + 1, getyPixel(tempY), paint);
//            y = tempY;
//        }
//        y = initY;
//
//        // Draws the portion of the com.duy.example.com.duy.calculator.graph between x=0 and the left edge
//        for (int i = getxPixel(initX); i > 0; i--) {
//            k[0] = (maxX - minX) / width
//                    * helper.getRKVal(getX(i), y);
//            k[1] = (maxX - minX)
//                    / width
//                    * helper.getRKVal(getX((float) (i - .5)), y
//                    + .5 * k[0]);
//            k[2] = (maxX - minX)
//                    / width
//                    * helper.getRKVal(getX((float) (i - .5)), y
//                    + .5 * k[1]);
//            k[3] = (maxX - minX) / width
//                    * helper.getRKVal(getX(i - 1), y + k[2]);
//            double tempY = y - (k[0] + 2 * k[1] + 2 * k[2] + k[3]) / 6;
//            canvas.drawLine(i, getyPixel(y), i - 1, getyPixel(tempY), paint);
//            y = tempY;
//        }

    }

    /*
     * Draws either the trace or derivative values to the com.duy.example.com.duy.calculator.graph
     */
    private void drawTrace(Canvas canvas) {
        if (traceFun != -1) {
            // Set color to the same as the function
            Paint paint = new Paint();
            paint.setColor(Color.rgb(colors[traceFun][0], colors[traceFun][1], colors[traceFun][2]));
            paint.setTextSize((width + height) / 70);

            // Rounds values for more concise display to the screen
            double roundX = Math.round(tracexVal * 1000) / 1000.0;
            double roundY = Math.round(traceyVal * 10000) / 10000.0;
            double roundD = Math.round(traceDeriv * 10000) / 10000.0;
            if (trace && !choose) {
                // Draws a small x at the point
                canvas.drawLine(getxPixel(tracexVal) - 8,
                        getyPixel(traceyVal) - 8, getxPixel(tracexVal) + 8,
                        getyPixel(traceyVal) + 8, paint);
                canvas.drawLine(getxPixel(tracexVal) + 8,
                        getyPixel(traceyVal) - 8, getxPixel(tracexVal) - 8,
                        getyPixel(traceyVal) + 8, paint);

                paint.setColor(Color.rgb(colors[traceFun][0], colors[traceFun][1],
                        colors[traceFun][2]));
                // Draws text in the upper left for the function value
                canvas.drawText("f(" + roundX + ")=" + roundY, 20, height / 2, paint);
            } else if (deriv && !choose) {
                // Draws a line tangent to the curve at the proper point
                canvas.drawLine(getxPixel(tracexVal - (traceyVal - mMinY)
                        / traceDeriv), height, getxPixel(tracexVal
                        + (mMaxY - traceyVal) / traceDeriv), 0, paint);

                paint.setColor(Color.rgb(colors[traceFun][0], colors[traceFun][1],
                        colors[traceFun][2]));
                // Draws text in the upper right for the function value
                canvas.drawText("f'(" + roundX + ")=" + roundD, 20, height / 2, paint);
            }
        }
    }

    private void setMode() {
        rect = true;
    }

    public double getParamEnd() {
        return endT;
    }

    public double getParamStart() {
        return startT;
    }

    public double getPolarEnd() {
        return endPolar;
    }

    public double getPolarStart() {
        return startPolar;
    }

    public float getPolarX(double theta, double r) {
        while (theta > Math.PI) {
            theta -= Math.PI;
        }
        while (theta < -1 * Math.PI) {
            theta += Math.PI;
        }
        double xCord = r * Math.cos(theta);
        return getxPixel(xCord);
    }

    public float getPolarY(double theta, double r) {
        while (theta > Math.PI) {
            theta -= Math.PI;
        }
        while (theta < -1 * Math.PI) {
            theta += Math.PI;
        }
        double yCord = r * Math.sin(theta);
        return getyPixel(yCord);
    }

    /*
     * Returns the values of all window variables
     */
    public double[] getWindow() {
        return new double[]{mMinX, mMaxX, mMinY, mMaxY, scaleX, scaleY};
    }

    /*
     * Converts an android x-coordinate to its corresponding x-value
     */
    private double getX(float x) {
        return (x / width) * (mMaxX - mMinX) + mMinX;
    }

    /*
     * Converts an x-value to its corresponding android x-coordinate
     */
    private int getxPixel(double x) {
        return (int) (width * (x - mMinX) / (mMaxX - mMinX));
    }

    /*
     * Converts an android y-coordinate to its corresponding y-value
     */
    private double getY(float y) {
        return (height - y) * (mMaxY - mMinY) / height + mMinY;
    }

    /*
     * Converts a y-value to its corresponding android y-coordinate
     */
    private int getyPixel(double y) {
        return (int) (height - height * (y - mMinY) / (mMaxY - mMinY));
    }

    public boolean isEmpty() {
        for (boolean b : graphable) {
            if (b) return false;
        }
        return true;
    }

    /*
     * Called when the user makes a multi-touch gesture in api 5+ Currently only
     * does pinch to zoom
     */
    private void multiTouchMove(MotionEvent event) {
        try {
            float x = startX - event.getX();
            float y = event.getY() - startY;
            double difX = (mMaxX - mMinX) * x / width;
            double difY = (mMaxY - mMinY) * y / height;
            mMinX += difX;
            mMaxX += difX;
            mMinY += difY;
            mMaxY += difY;
            startX = event.getX();
            startY = event.getY();
            invalidate();
        } catch (Throwable t) {

        }
    }

    /*
     * Called when the invalidate is called
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(255, 0, 0, 0);
        drawAxis(canvas);
        drawFunction(canvas);

        drawRK(canvas);
        if (!choose) {
            if (trace || deriv) {
                drawTrace(canvas);
            }
        }
    }


    private void drawAxis(Canvas canvas) {
        //pre calc
        final double lengthX = (mMaxX - mMinX);
        final double step = (long) (lengthX / 8) / 100d;
        final double minX = (Math.round(mMinX / step)) * step;
        final double maxX = Math.round(mMaxX / step) * step;

        int fontHeight = (int) (mTextAxisFontMetrics.descent - mTextAxisFontMetrics.ascent);
        int fontWidth = (int) mTextHintAxis.measureText(" ");

        //draw x hint
        for (double i = minX; i < maxX; i += step) {
            int y = getyPixel(0);
            int yText = fontHeight;
            if (y < 0) {
                y = 0;
            } else if (y > height - 20) {
                y = height;
                yText = -fontHeight;
            }

            int xValue = getxPixel(i);

            canvas.drawLine(xValue, 0, xValue, height, mTextHintAxis);
            String text = mDecimalFormat.format(i);
            canvas.drawText(text, xValue - fontWidth * text.length() / 2, y + yText, mTextHintAxis);
        }

        final double minY = Math.round(mMinY / step) * step;
        final double maxY = Math.round(mMaxY / step) * step;
        for (double i = minY; i < maxY; i += step) {
            int x = getxPixel(0);
            int y1 = getyPixel(i);
            int xText = fontWidth;
            if (x < 0) {
                x = 0;
            } else if (x > width - 20) {
                x = width;
                xText = -fontWidth;
            }
            canvas.drawLine(0, y1, width, y1, mTextHintAxis);
            String text = mDecimalFormat.format(i);
            canvas.drawText(text, x + xText, y1, mTextHintAxis);
        }

        // Draws the axis
        int x0 = getxPixel(0);
        int y0 = getyPixel(0);
        canvas.drawLine(x0, 0, x0, height, axisPaint);
        canvas.drawLine(0, y0, width, y0, axisPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return this.onTouchEvent(event);
    }

    /*
     * Called when the user touches the screen
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // On the initial press
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // If the trace or derivative is turned on
            if (trace || deriv) {
                if (!choose) {
                    tracexVal = getX(event.getX());
                    traceyVal = mGraphHelper.getVal(traceFun, tracexVal);
                    traceDeriv = mGraphHelper.getDerivative(traceFun, tracexVal);
                    invalidate();
                }
            } else {
                startX = event.getX();
                startY = event.getY();
                allowMove = true;
            }
            return true;
            // If the user has moved their finger
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // Moves the trace or derivative
            if (trace || deriv) {
                if (!choose) {
                    if (rect) {
                        tracexVal = getX(event.getX());
                        traceyVal = mGraphHelper.getVal(traceFun, tracexVal);
                        traceDeriv = mGraphHelper.getDerivative(traceFun, tracexVal);
                    }
                    invalidate();
                }
            } else {
                touchMove(event);
            }
            return true;
            //Resets values when users finishes their gesture
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            pinchDist = -1;
            allowMove = false;
            if (choose) {
                double x = getX(event.getX());
                double y = getY(event.getY());
                double[] distance = new double[6];
                distance[0] = Double.MAX_VALUE;
                for (int i = 0; i < 6; i++) {
                    if (graphable[i]) {
                        distance[i] = Math.abs(mGraphHelper.getVal(i, x) - y);
                    }
                }
                int smallest = 0;
                for (int i = 1; i < 6; i++) {
                    if (graphable[i]) {
                        if (distance[smallest] > distance[i]) {
                            smallest = i;
                        }
                    }
                }
                traceFun = smallest;
            }
            choose = false;
        }

        return false;
    }

    /*
     * Turns the trace derivative feature on/off
     */
    public boolean setDeriv(boolean dr) {
        if (!isEmpty()) {
            deriv = dr;
            choose = true;
            invalidate();
            return true;
        }
        deriv = false;
        return false;
    }

    public void setParamBounds(double start, double end) {
        startT = start;
        endT = end;
    }

    public void setPolarBounds(double start, double end) {
        startPolar = start;
        endPolar = end;
    }

    /*
     * Sets the trace feature on/off
     */
    public boolean setTrace(boolean tr) {
        if (!isEmpty()) {
            trace = tr;
            choose = true;
            invalidate();
            return true;
        }
        trace = false;
        return false;
    }

    /*
     * Changes the window size and scale
     */
    public void setWindow(double minx, double miny, double maxx, double maxy,
                          double scalex, double scaley) {
        mMinX = minx;
        mMinY = miny;
        mMaxX = maxx;
        mMaxY = maxy;
        scaleX = scalex;
        scaleY = scaley;
    }

    /*
     * Returns the distance between the users fingers on a multi-touch
     */
    public float spacing(MotionEvent event) {
        try {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } catch (Throwable t) {
            return Float.NaN;
        }
    }

    /*
     * Moves the com.duy.example.com.duy.calculator.graph
     */
    private void touchMove(MotionEvent event) {
        float x = startX - event.getX();
        float y = event.getY() - startY;
        double difX = (mMaxX - mMinX) * x / width;
        double difY = (mMaxY - mMinY) * y / height;
        mMinX += difX;
        mMaxX += difX;
        mMinY += difY;
        mMaxY += difY;
        startX = event.getX();
        startY = event.getY();
        invalidate();
    }

    /*
     * Zooms the com.duy.example.com.duy.calculator.graph in and out
     */
    public void zoom(float perc) {
        double realWidth = mMaxX - mMinX;
        double realHeight = mMaxY - mMinY;
        mMaxX += realWidth * perc / 2;
        mMinX -= realWidth * perc / 2;
        mMinY -= realHeight * perc / 2;
        mMaxY += realHeight * perc / 2;
        scaleX += scaleX * perc;
        scaleY += scaleY * perc;
        invalidate();
    }

    public int getNumberGraph() {
        int count = 0;
        for (int i = 0; i < functions.length; i++) {
            if (graphable[i]) count++;
        }
        return count;
    }

    public boolean isGraphable() {
        for (int i = 0; i < graphable.length; i++) {
            if (graphable[i]) return true;
        }
        return false;
    }

}