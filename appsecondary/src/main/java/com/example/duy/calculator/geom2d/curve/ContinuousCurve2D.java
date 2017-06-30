/* File ContinuousCurve2D.java 
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

import android.graphics.Path;

import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.Vector2D;
import com.example.duy.calculator.geom2d.polygon.LinearCurve2D;

import com.example.duy.calculator.geom2d.Vector2D;
import com.example.duy.calculator.geom2d.polygon.LinearCurve2D;

/**
 * Interface for all curves which can be drawn with one stroke. This includes
 * closed curves (ellipses, polygon boundaries...), infinite curves (straight
 * lines, parabolas, ...), and 'finite' curves, such as polylines, conic arcs,
 * line segments, splines... Note that an hyperbola is compound of 2 continuous
 * curves.
 * <p>
 * Such curves accept parametric representation, in the form :
 * <code>p(t)={x(t),y(t)}</code>, with <code>t</code> contained in
 * appropriate domain. Bounds of domain of definition can be obtained by methods
 * <code>t0()</code> and <code>t1()</code>.
 * <p>
 */

public interface ContinuousCurve2D extends Curve2D {
    public abstract boolean contains(Point2D p);

    // ===================================================================
    // constants

    // ===================================================================
    // general methods

    /**
     * Returns true if the curve makes a loop, that is come back to starting
     * point after covering the path.
     */
    public abstract boolean isClosed();

    /**
     * Computes the left tangent at the given position.
     * If the curve is smooth at position <code>t</code>, the result is the
     * same as the tangent computed for the corresponding smooth curve, and
     * is equal to the result of rightTangent(double).
     * If the position <code>t</code> corresponds to a singular point, the
     * tangent of the smooth portion before <code>t</code> is computed.
     *
     * @param t the position on the curve
     * @return the left tangent vector at the curve for position t
     */
    public Vector2D leftTangent(double t);

    /**
     * Computes the right tangent at the given position.
     * If the curve is smooth at position <code>t</code>, the result is the
     * same as the tangent computed for the corresponding smooth curve, and
     * is equal to the result of leftTangent(double).
     * If the position <code>t</code> corresponds to a singular point, the
     * tangent of the smooth portion after <code>t</code> is computed.
     *
     * @param t the position on the curve
     * @return the right tangent vector at the curve for position t
     */
    public Vector2D rightTangent(double t);

    /**
     * Computes the curvature at the given position. The curvature is finite
     * for positions <code>t</code> that correspond to smooth parts, and is
     * infinite for singular points.
     *
     * @param t the position on the curve
     * @return the curvature of the curve for position t
     */
    public abstract double curvature(double t);


    /**
     * Returns an approximation of the curve as a polyline with <code>n</code>
     * line segments. If the curve is closed, the method should return an
     * instance of LinearRing2D. Otherwise, it returns an instance of
     * Polyline2D.
     *
     * @param n the number of line segments
     * @return a polyline with <code>n</code> line segments.
     */
    public abstract LinearCurve2D asPolyline(int n);

    /**
     * Append the path of the curve to the given path.
     *
     * @param path a path to modify
     * @return the modified path
     */
    public abstract Path appendPath(Path path);
}