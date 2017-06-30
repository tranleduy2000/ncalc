package com.duy.calc.casio.view;

import android.graphics.Paint;

/**
 * Created by Duy on 22-Jun-17.
 */
public class XCoordinate {
    /**
     * x coordinate
     */
    private float x;
    private boolean onSubscript;
    private boolean onSuperScript;
    private Paint paint;

    public XCoordinate(float x, boolean onSuperScript) {
        this.x = x;
        this.onSuperScript = onSuperScript;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public boolean isOnSubscript() {
        return onSubscript;
    }

    public void setOnSubscript(boolean onSubscript) {
        this.onSubscript = onSubscript;
    }

    public boolean isOnSuperScript() {
        return onSuperScript;
    }

    public void setOnSuperScript(boolean onSuperScript) {
        this.onSuperScript = onSuperScript;
    }

    public boolean onSubOrSuperScript() {
        return onSuperScript || onSubscript;
    }
}
