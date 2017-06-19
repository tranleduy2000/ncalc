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

/**
 * File: 	AbstractSmoothCurve2D.java
 * Project: javaGeom
 * <p>
 * Distributed under the LGPL License.
 * <p>
 * Created: 21 mai 09
 */
package com.duy.calculator.geom2d.curve;

import com.duy.calculator.geom2d.Point2D;
import com.duy.calculator.geom2d.line.LinearShape2D;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Provides a base implementation for smooth curves.
 *
 * @author dlegland
 */
public abstract class AbstractSmoothCurve2D implements LinearShape2D, ContinuousCurve2D, Cloneable {


    /**
     * Returns an empty set of Point2D, as a smooth curve does not have
     * singular points by definition.
     *
     * @see Curve2D#singularPoints()
     */
    public Collection<Point2D> singularPoints() {
        return new ArrayList<>(0);
    }

    /**
     * Returns a set of Point2D, containing the extremities of the curve
     * if they are not infinite.
     *
     * @see Curve2D#vertices()
     */
    public Collection<Point2D> vertices() {
        ArrayList<Point2D> array = new ArrayList<Point2D>(2);
        if (!Double.isInfinite(this.t0()))
            array.add(this.firstPoint());
        if (!Double.isInfinite(this.t1()))
            array.add(this.lastPoint());
        return array;
    }

    /**
     * Returns always false, as a smooth curve does not have singular points
     * by definition.
     *
     * @see Curve2D#isSingular(double)
     */
    public boolean isSingular(double pos) {
        return false;
    }


}
