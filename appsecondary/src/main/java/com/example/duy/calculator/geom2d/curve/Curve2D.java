/* File Curve2D.java 
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


import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.util.Shape2D;
import com.example.duy.calculator.geom2d.line.LinearShape2D;

import java.util.Collection;

public interface Curve2D extends Shape2D {

    /**
     * Get value of parameter t for the first point of the curve. It can be
     * -Infinity, in this case the piece of curve is not bounded.
     */
    double t0();

    /**
     * @deprecated replaced by t0() (since 0.11.1).
     */
    @Deprecated
    double getT0();

    /**
     * Get value of parameter t for the last point of the curve. It can be
     * +Infinity, in this case the piece of curve is not bounded.
     */
    double t1();

    /**
     * @deprecated replaced by t1() (since 0.11.1).
     */
    @Deprecated
    double getT1();

    /**
     * Returns the point located at the given position on the curve. If the
     * parameter lies outside the definition range, the parameter corresponding
     * to the closest bound is used instead. This method can be used to draw an
     * approximated outline of a curve, by selecting multiple values for t and
     * drawing lines between them.
     */
    Point2D point(double t);

    /**
     * Returns the first point of the curve. It must returns the same result as
     * <code>point(t0())</code>.
     *
     * @return the first point of the curve
     * @see #t0()
     * @see #point(double)
     */
    Point2D firstPoint();

    /**
     * Returns the last point of the curve. It must returns the same result as
     * <code>this.point(this.t1())</code>.
     *
     * @return the last point of the curve.
     * @see #t1()
     * @see #point(double)
     */
    Point2D lastPoint();

    /**
     * Returns a set of singular points, i. e. which do not locally admit
     * derivative.
     *
     * @return a collection of Point2D.
     * @see #vertices()
     */
    Collection<Point2D> singularPoints();

    /**
     * Returns the set of vertices for this curve. Vertices can be either
     * singular points, or extremities.
     *
     * @return a collection of Point2D.
     * @see #singularPoints()
     */
    Collection<Point2D> vertices();

    /**
     * Checks if a point is singular.
     *
     * @param pos the position of the point on the curve
     * @return true if the point at this location is singular
     */
    boolean isSingular(double pos);

    /**
     * Computes the position of the point on the curve. If the point does not
     * belong to the curve, return Double.NaN. It is complementary to the
     * <code>point(double)</code> method.
     *
     * @param point a point belonging to the curve
     * @return the position of the point on the curve
     * @see #point(double)
     */
    double position(Point2D point);

    /**
     * Returns the position of the closest orthogonal projection of the point on
     * the curve, or of the closest singular point. This function should always
     * returns a valid value.
     *
     * @param point a point to project
     * @return the position of the closest orthogonal projection
     */
    double project(Point2D point);

    /**
     * Returns the intersection points of the curve with the specified line. The
     * length of the result array is the number of intersection points.
     */
    Collection<Point2D> intersections(LinearShape2D line);
}