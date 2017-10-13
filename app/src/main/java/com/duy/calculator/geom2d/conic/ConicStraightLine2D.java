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

package com.duy.calculator.geom2d.conic;

import com.duy.calculator.geom2d.line.StraightLine2D;


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