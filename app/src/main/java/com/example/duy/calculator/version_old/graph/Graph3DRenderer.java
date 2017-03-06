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
import android.opengl.GLSurfaceView;
import android.preference.PreferenceManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.hws.jcm.data.Expression;
import edu.hws.jcm.data.Parser;
import edu.hws.jcm.data.Variable;

public class Graph3DRenderer implements GLSurfaceView.Renderer {

    private final float[] axis = new float[]{
            0, 0, 0, 0, 0, 2,
            0, 0, 0, 0, 2, 0,
            0, 0, 0, 2, 0, 0};
    public float scale;
    public boolean dirty;
    private FloatBuffer[][][] vertexBuffer;
    private FloatBuffer axisBuffer;
    private long milliseconds;
    private boolean userRotate = false;
    private float newScale;
    private Parser p = new Parser(Parser.STANDARD_FUNCTIONS | Parser.OPTIONAL_PARENS
            | Parser.OPTIONAL_STARS | Parser.OPTIONAL_SPACES
            | Parser.BRACES | Parser.BRACKETS | Parser.BOOLEANS);
    private Variable x = new Variable("x"), y = new Variable("y");
    private Expression f;
    private ArrayList<String> functions;
    private Context context;
    private float alpha = 0, beta = 0, gamma = 0;


    public Graph3DRenderer(Context c) {
        context = c;
        scale = 3;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glTranslatef(0, 0, -3.0f);
        gl.glRotatef(gamma, 0, 1f, 0);
        gl.glRotatef(alpha, 1f, 1f, 0);
        gl.glRotatef(beta, 0, 0, 1f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        if (dirty) {
            setUpArray();
            dirty = false;
        }

        gl.glLineWidth(1.0f);
        for (int k = 0; k < functions.size(); k++) {
            int colors[] = Graph2DView.colors[k];
            gl.glColor4f((float) colors[0] / 255, (float) colors[1] / 255, (float) colors[2], 1);
            for (int i = 0; i <= 30; i++) {
                vertexBuffer[k][i][1].position(0);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer[k][i][0]);
                gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 31);

                vertexBuffer[k][i][1].position(0);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer[k][i][1]);
                gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 31);
            }
        }

        gl.glLineWidth(3.0f);
        gl.glColor4f(1, 1, 1, 1);
        axisBuffer.position(0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, axisBuffer);
        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, axis.length);

        gl.glRotatef(gamma * 2.0f, 0, 0, 1);
        gl.glTranslatef(0.5f, 0.5f, 0.5f);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        long newSeconds = System.currentTimeMillis();

        if (!userRotate) {
            gamma += .04f * (newSeconds - milliseconds);
        }
        milliseconds = newSeconds;

    }

    public void move(float xMove, float yMove) {
        float tempAlpha = alpha, tempBeta = beta;
        tempAlpha += yMove;// * (float)(Math.cos(gamma/180*Math.PI));
        tempBeta += yMove;// * (float)(Math.sin(gamma/180*Math.PI));
        gamma += xMove + yMove * (float) (Math.abs(Math.cos(gamma / 180 * Math.PI) / 2));
        beta = tempBeta;
        alpha = tempAlpha;
    }

    public void setUpArray() {
        vertexBuffer = new FloatBuffer[functions.size()][31][2];
        for (int k = 0; k < functions.size(); k++) {
            f = p.parse(functions.get(k));
            for (int i = 0; i <= 30; i++) {
                float[] temp = getDataPoints(true, i);
                ByteBuffer bb = ByteBuffer.allocateDirect(temp.length * 4);
                bb.order(ByteOrder.nativeOrder());
                FloatBuffer fbVertices = bb.asFloatBuffer();
                for (int j = 0; j < temp.length; j++) {
                    fbVertices.put(j, temp[j]);
                }
                vertexBuffer[k][i][0] = fbVertices;
                temp = getDataPoints(false, i);
                bb = ByteBuffer.allocateDirect(temp.length * 4);
                bb.order(ByteOrder.nativeOrder());
                fbVertices = bb.asFloatBuffer();
                for (int j = 0; j < temp.length; j++) {
                    fbVertices.put(j, temp[j]);
                }
                vertexBuffer[k][i][1] = fbVertices;
            }
        }
        ByteBuffer bb = ByteBuffer.allocateDirect(72);
        bb.order(ByteOrder.nativeOrder());
        axisBuffer = bb.asFloatBuffer();
        axisBuffer.put(axis);
    }

    public void setScale(float s) {
        newScale = s;
    }

    public void stopRotate() {
        userRotate = true;
    }

    public void spin(float a) {
        gamma += a;
    }

    public float[] getDataPoints(boolean isX, int num) {
        float[] vertices = new float[93];
        if (isX) {
            float xVal = (float) (-1.25 + num * .08333333333);
            x.setVal(xVal * scale);
            for (int j = 0; j <= 30; j++) {
                vertices[3 * j] = xVal;
                vertices[3 * j + 2] = (float) (-1.25 + j * .083333333);
                y.setVal(vertices[3 * j + 2] * scale);
                float z = (float) f.getVal() / scale;
                vertices[3 * j + 1] = z;
            }
        } else {
            float yVal = (float) (-1.25 + num * .0833333333);
            y.setVal(yVal * scale);
            for (int j = 0; j <= 30; j++) {
                vertices[3 * j + 2] = yVal;
                vertices[3 * j] = (float) (-1.25 + j * .083333333);
                x.setVal(vertices[3 * j] * scale);
                float z = (float) f.getVal() / scale;
                vertices[3 * j + 1] = z;
            }
        }
        return vertices;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        p = new Parser(Parser.STANDARD_FUNCTIONS | Parser.OPTIONAL_PARENS
                | Parser.OPTIONAL_STARS | Parser.OPTIONAL_SPACES
                | Parser.BRACES | Parser.BRACKETS);
        x = new Variable("x");
        y = new Variable("y");
        p.add(x);
        p.add(y);
        GraphMath.setUpParser(p);
        milliseconds = System.currentTimeMillis();
        importFunctions();
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glClearColor(0, 0, 0, 1);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
    }

    public void importFunctions() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        functions = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String s = sp.getString("f" + (i + 1), "thisshouldntparse");
            if (GraphMath.isValid(s, new String[]{"x", "y"})) {
                functions.add(s);
            }
        }
        setUpArray();
    }
}
