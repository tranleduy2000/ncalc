package com.example.duy.calculator.geom2d.conic;

import com.example.duy.calculator.geom2d.line.StraightLine2D;

import static com.example.duy.calculator.R.string.dx;
import static com.example.duy.calculator.R.string.dy;

public class ConicStraightLine2D extends StraightLine2D implements Conic2D {

    double[] coefs = new double[]{0, 0, 0, 1, 0, 0};

    public ConicStraightLine2D(StraightLine2D line) {
        super(line);
        coefs = new double[]{0, 0, 0, dy, -dx, dx * y0 - dy * x0};
    }

    public ConicStraightLine2D(double a, double b, double c) {
        super(StraightLine2D.createCartesian(a, b, c));
        coefs = new double[]{0, 0, 0, a, b, c};
    }

    public double[] conicCoefficients() {
        return coefs;
    }

    public Type conicType() {
        return Type.STRAIGHT_LINE;
    }

    /**
     * Return NaN.
     */
    public double eccentricity() {
        return Double.NaN;
    }
}