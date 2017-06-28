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
