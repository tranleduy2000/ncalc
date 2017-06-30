/* File SmoothCurve2D.java 
 *
 * Project : Java Geometry Library
 *
 * ===========================================
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. if not, write to :
 * The Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */

// package

package com.example.duy.calculator.geom2d.curve;

// Imports

import com.example.duy.calculator.geom2d.Vector2D;

/**
 * Interface for smooth and continuous curves. Such curves accept first and
 * second derivatives at every point, and can be drawn with a parametric
 * representation for every values of t comprised between T0 and T1.
 * Every instance of Curve2D is a compound of several SmoothCurve2D.
 */
public interface SmoothCurve2D extends ContinuousCurve2D {

    /**
     * Returns the tangent of the curve at the given position.
     *
     * @param t a position on the curve
     * @return the tangent vector computed for position t
     * @see #normal(double)
     */
    public abstract Vector2D tangent(double t);

    /**
     * Returns the normal vector of the curve at the given position.
     *
     * @param t a position on the curve
     * @return the normal vector computed for position t
     * @see #tangent(double)
     */
    public abstract Vector2D normal(double t);

}
