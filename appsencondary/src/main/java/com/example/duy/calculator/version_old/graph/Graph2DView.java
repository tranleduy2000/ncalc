/*
 * Copyright (C) 2010 Andrew P McSherry
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.duy.calculator.version_old.graph;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.duy.calculator.math_eval.DecimalFactory;

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

    public Bitmap graphImage;
    public String[] functions, paramX, paramY;
    public boolean[] graphable = new boolean[]{false, false, false, false, false, false};
    public Context context;
    public double minX = -3, maxX = 3, minY = -5, maxY = 5, scaleX = 1,
            scaleY = 1, initX = 0, initY = 0, startPolar = -1 * Math.PI, endPolar = Math.PI,
            startT = -20, endT = 20;
    public GraphHelper helper;
    public float startX, startY, pinchDist;
    public int width, height, interA, interB, mode;
    private Paint axisPaint = new Paint();
    private Paint textHintAxis = new Paint();
    private double tracexVal, traceyVal, traceDeriv;
    private int traceFun = -1;
    private boolean allowMove;
    private boolean trace = false;
    private boolean deriv = false;
    private boolean choose = false;
    private boolean rect = true;

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
            helper = new GraphHelper(this);

            axisPaint.setColor(Color.WHITE);
            axisPaint.setStrokeWidth(4f);

            textHintAxis = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            textHintAxis.setColor(Color.WHITE);
            textHintAxis.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

            helper = new GraphHelper(this);
            graphImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        }
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
        graphImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
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
        helper = new GraphHelper(this);
        invalidate();
    }

    private void drawFunction(Canvas canvas) {

        Paint paint = new Paint();
        paint.setStrokeWidth(3f);
        for (int i = 0; i < 6; i++) {
            if (graphable[i]) {
                paint.setColor(Color.rgb(colors[i][0], colors[i][1], colors[i][2]));
                double y1 = helper.getVal(i, minX);
                double y2 = y1;
                for (int j = 0; j < width; j++) {
                    double k = j;
                    y1 = y2;
                    y2 = helper.getVal(i, minX + (k + 1) * (maxX - minX) / width);
                    if (y1 != Double.POSITIVE_INFINITY && y2 != Double.POSITIVE_INFINITY && (y1 >= 0 || y1 < 0) && (y2 >= 0 || y2 < 0)) {
                        if (!((y1 > 20) && (y2 < -20)) && !((y1 < -20) && (y2 > 20))) {
                            canvas.drawLine(j, getyPixel(y1), j + 1, getyPixel(y2), paint);
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
                canvas.drawLine(getxPixel(tracexVal - (traceyVal - minY)
                        / traceDeriv), height, getxPixel(tracexVal
                        + (maxY - traceyVal) / traceDeriv), 0, paint);

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
        return new double[]{minX, maxX, minY, maxY, scaleX, scaleY};
    }

    /*
     * Converts an android x-coordinate to its corresponding x-value
     */
    private double getX(float x) {
        return (x / width) * (maxX - minX) + minX;
    }

    /*
     * Converts an x-value to its corresponding android x-coordinate
     */
    private int getxPixel(double x) {
        return (int) (width * (x - minX) / (maxX - minX));
    }

    /*
     * Converts an android y-coordinate to its corresponding y-value
     */
    private double getY(float y) {
        return (height - y) * (maxY - minY) / height + minY;
    }

    /*
     * Converts a y-value to its corresponding android y-coordinate
     */
    private int getyPixel(double y) {
        return (int) (height - height * (y - minY) / (maxY - minY));
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
            double difX = (maxX - minX) * x / width;
            double difY = (maxY - minY) * y / height;
            minX += difX;
            maxX += difX;
            minY += difY;
            maxY += difY;
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
//        original.drawBitmap(graphImage, 0, 0, new Paint());
    }

    private void drawAxis(Canvas canvas) {
        //pre calc
        double d = (maxX - minX);
        double inc = Double.parseDouble(DecimalFactory.round(d / 8, 2));
        //draw x hint
        for (double i = minX; i < maxX; i += inc) {
            int y = getyPixel(0);
            int yText = 18;
            if (y < 0) {
                y = 0;
            } else if (y > height - 20) {
                y = height;
                yText = -18;
            }

            double x1Real = i;
            double x2Real = -i;
            int x1 = getxPixel(x1Real);
            int x2 = getxPixel(x2Real);

            canvas.drawLine(x1, 0, x1, height, textHintAxis);
//            canvas.drawLine(x2, 0, x2, height, textHintAxis);

//            Log.d("Graph", "drawAxis: " + x1Real + " ->  " + DecimalFactory.round(x1Real, 2) +
//                    " x1 = " + x1 + " x2 = " + x2 + " y = " + y + " yText = " + yText);
            canvas.drawText(DecimalFactory.round(x1Real, 2),
                    x1,
                    y + yText,
                    textHintAxis);

//            canvas.drawText(
//                    DecimalFactory.round(x1Real, 2),
//                    x2,
//                    y + yText,
//                    textHintAxis);
        }

        d = maxY - minY;
        inc = Double.parseDouble(DecimalFactory.round(d / 8, 2));
        for (double i = minY; i < maxY; i += inc) {
            int x = getxPixel(0);

            double y1Real = i;
            double y2Real = -i;

            int y1 = getyPixel(y1Real);
            int y2 = getyPixel(y2Real);
            int xText = 10;
            if (x < 0) {
                x = 0;
            } else if (x > width - 20) {
                x = width;
                xText = -20;
            }
            canvas.drawLine(0, y1, width, y1, textHintAxis);
//            canvas.drawLine(0, y2, width, y2, textHintAxis);

            canvas.drawText(

                    DecimalFactory.round(y1Real, 2), x + xText, y1, textHintAxis);
//            canvas.drawText("" + (new BigDecimal(y2Real)).round(new MathContext(4)).floatValue(), x + xText, y2, textHintAxis);
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
                    traceyVal = helper.getVal(traceFun, tracexVal);
                    traceDeriv = helper.getDerivative(traceFun, tracexVal);
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
                        traceyVal = helper.getVal(traceFun, tracexVal);
                        traceDeriv = helper.getDerivative(traceFun, tracexVal);
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
                        distance[i] = Math.abs(helper.getVal(i, x) - y);
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
        minX = minx;
        minY = miny;
        maxX = maxx;
        maxY = maxy;
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
        double difX = (maxX - minX) * x / width;
        double difY = (maxY - minY) * y / height;
        minX += difX;
        maxX += difX;
        minY += difY;
        maxY += difY;
        startX = event.getX();
        startY = event.getY();
        invalidate();
    }

    /*
     * Zooms the com.duy.example.com.duy.calculator.graph in and out
     */
    public void zoom(float perc) {
        double realWidth = maxX - minX;
        double realHeight = maxY - minY;
        maxX += realWidth * perc / 2;
        minX -= realWidth * perc / 2;
        minY -= realHeight * perc / 2;
        maxY += realHeight * perc / 2;
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