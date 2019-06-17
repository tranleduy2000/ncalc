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
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class Graph3DView extends GLSurfaceView {

    private Context context;
    private float startX = -1;
    private float startY = -1;
    private float pinchDist = 0;
    private int width, height;
    private Graph3DRenderer renderer;

    public Graph3DView(Context c, AttributeSet attrs) {
        super(c, attrs);
        if (!isInEditMode()) {
            context = c;
            renderer = new Graph3DRenderer(context);
            setDisplay();
            setRenderer(renderer);
        }
    }

    public Graph3DView(Context c) {
        super(c);
        if (!isInEditMode()) {
            context = c;
            renderer = new Graph3DRenderer(context);
            setDisplay();
            setRenderer(renderer);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestRender();

    }

    /*
     * Returns the distance between the users fingers on a multi-touch
     */
    public float spacing(MotionEvent event) {
        try {
            Integer arg0 = 0;
            Integer arg1 = 1;
            float x = event.getX(arg0) - event.getX(arg1);
            float y = event.getY(arg0) - event.getY(arg1);
            return (float) Math.sqrt(x * x + y * y);
        } catch (Throwable t) {
            return Float.NaN;
        }
    }

    private void setDisplay() {
        width = getWidth();
        height = getHeight();
        if (width == 0) width = 1;
        if (height == 0) height = 1;
    }

    public void zoom(float z) {
        final float zoom = z;
        queueEvent(new Runnable() {
            @Override
            public void run() {
                renderer.scale *= (1 + zoom);
                renderer.dirty = true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            renderer.stopRotate();
            startX = event.getX();
            startY = event.getY();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // Moves the trace or derivative

            renderer.move((event.getX() - startX) / width * 360, (event.getY() - startY) / height * 360);
            startX = event.getX();
            startY = event.getY();
            return true;
            //Resets values when users finishes their gesture
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            pinchDist = -1;
            startX = -1;
            startY = -1;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        if (width == 0) width = 1;
        if (height == 0) height = 1;
    }

    public void drawGraph() {
        setDisplay();
//        if (renderer != null) renderer.importFunctions();
        invalidate();
    }
}
