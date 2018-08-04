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

package com.duy.ncalc.geom2d.conic;

import com.duy.ncalc.geom2d.line.StraightLine2D;


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