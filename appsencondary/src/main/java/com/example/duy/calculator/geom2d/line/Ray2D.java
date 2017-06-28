/* File Ray2D.java 
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

package com.example.duy.calculator.geom2d.line;

import android.graphics.Path;

import com.example.duy.calculator.geom2d.util.EqualUtils;
import com.example.duy.calculator.geom2d.GeometricObject2D;
import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.util.Shape2D;
import com.example.duy.calculator.geom2d.util.UnboundedShape2DException;
import com.example.duy.calculator.geom2d.Vector2D;
import com.example.duy.calculator.geom2d.polygon.LinearCurve2D;


// Imports

/**
 * Ray, or half-line, defined from an origin and a direction vector. It is
 * composed of all points satisfying the parametric equation:
 * <p>
 * <code>x(t) = x0+t*dx<code><br>
 * <code>y(t) = y0+t*dy<code></p>
 * With <code>t<code> comprised between 0 and +INFINITY.
 */
public class Ray2D extends AbstractLine2D {

    // ===================================================================
    // Static constructors

    /**
     * Empty constructor for Ray2D. Default is ray starting at origin, and
     * having a slope of 1*dx and 0*dy.
     */
    public Ray2D() {
        this(0, 0, 1, 0);
    }

    /**
     * Creates a new Ray2D, originating from
     * <code>point1<\code>, and going in the
     * direction of <code>point2<\code>.
     */
    public Ray2D(Point2D point1, Point2D point2) {
        this(point1.x(), point1.y(),
                point2.x() - point1.x(),
                point2.y() - point1.y());
    }


    // ===================================================================
    // constructors

    /**
     * Creates a new Ray2D, originating from point
     * <code>(x1,y1)<\code>, and going
     * in the direction defined by vector <code>(dx, dy)<\code>.
     */
    public Ray2D(double x1, double y1, double dx, double dy) {
        super(x1, y1, dx, dy);
    }

    /**
     * Creates a new Ray2D, originating from point <code>point<\code>, and going
     * in the direction defined by vector <code>(dx,dy)<\code>.
     */
    public Ray2D(Point2D point, double dx, double dy) {
        this(point.x(), point.y(), dx, dy);
    }

    /**
     * Creates a new Ray2D, originating from point <code>point<\code>, and going
     * in the direction specified by <code>vector<\code>.
     */
    public Ray2D(Point2D point, Vector2D vector) {
        this(point.x(), point.y(), vector.x(), vector.y());
    }

    /**
     * Creates a new Ray2D, originating from point <code>point<\code>, and going
     * in the direction specified by <code>angle<\code> (in radians).
     */
    public Ray2D(Point2D point, double angle) {
        this(point.x(), point.y(), Math.cos(angle), Math.sin(angle));
    }

    /**
     * Creates a new Ray2D, originating from point
     * <code>(x, y)<\code>, and going
     * in the direction specified by <code>angle<\code> (in radians).
     */
    public Ray2D(double x, double y, double angle) {
        this(x, y, Math.cos(angle), Math.sin(angle));
    }

    /**
     * Define a new Ray, with same characteristics as given object.
     */
    public Ray2D(LinearShape2D line) {
        super(line.origin(), line.direction());
    }

    /**
     * Static factory for creating a new ray.
     *
     * @deprecated since 0.11.1
     */
    @Deprecated
    public static Ray2D create(Point2D origin, Vector2D direction) {
        return new Ray2D(origin, direction);
    }

    /**
     * Static factory for creating a new ray, originating from
     * <code>origin<\code>, and going in the
     * direction of <code>target<\code>.
     *
     * @deprecated since 0.11.1
     */
    @Deprecated
    public static Ray2D create(Point2D origin, Point2D target) {
        return new Ray2D(origin, target);
    }

    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

    /**
     * Returns a new ray parallel to this one, at the given relative distance.
     */
    public Ray2D parallel(double d) {
        double dd = Math.hypot(dx, dy);
        return new Ray2D(x0 + dy * d / dd, y0 - dx * d / dd, dx, dy);
    }

    // ===================================================================
    // methods implementing the ContinuousCurve2D interface


    /**
     * Throws an infiniteShapeException
     */
    public Path getPath() {
        throw new UnboundedShape2DException(this);
    }

    // ===================================================================
    // methods implementing the Curve2D interface

    @Override
    public Point2D firstPoint() {
        return new Point2D(x0, y0);
    }

    @Override
    public Point2D lastPoint() {
        return null;
    }

    public Point2D point(double t) {
        t = Math.max(t, 0);
        return new Point2D(x0 + t * dx, y0 + t * dy);
    }

    public double t0() {
        return 0;
    }

    /**
     * @deprecated replaced by t0() (since 0.11.1).
     */
    @Deprecated
    public double getT0() {
        return t0();
    }

    /**
     * Returns the position of the last point of the ray, which is always
     * Double.POSITIVE_INFINITY.
     */
    public double t1() {
        return Double.POSITIVE_INFINITY;
    }

    /**
     * @deprecated replaced by t1() (since 0.11.1).
     */
    @Deprecated
    public double getT1() {
        return t1();
    }

    /**
     * Reverses this curve, and return the result as an instance of
     * InvertedRay2D.
     *
     * @see InvertedRay2D#reverse()
     */
    public InvertedRay2D reverse() {
        return new InvertedRay2D(x0, y0, -dx, -dy);
    }

    // ===================================================================
    // methods implementing the Shape2D interface

    /**
     * Always returns false, because a ray is not bounded.
     */
    public boolean isBounded() {
        return false;
    }

    public boolean contains(double x, double y) {
        if (!this.supportContains(x, y))
            return false;
        double t = this.positionOnLine(x, y);
        return t > -Shape2D.ACCURACY;
    }


    // ===================================================================
    // methods implementing the GeometricObject2D interface

    /* (non-Javadoc)
     * @see GeometricObject2D#almostEquals(GeometricObject2D, double)
     */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof Ray2D))
            return false;
        Ray2D ray = (Ray2D) obj;

        if (Math.abs(x0 - ray.x0) > eps)
            return false;
        if (Math.abs(y0 - ray.y0) > eps)
            return false;
        if (Math.abs(dx - ray.dx) > eps)
            return false;
        if (Math.abs(dy - ray.dy) > eps)
            return false;

        return true;
    }

    // ===================================================================
    // methods implementing the Object interface

    @Override
    public String toString() {
        return "Ray2D(" + x0 + "," + y0 + "," +
                dx + "," + dy + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Ray2D))
            return false;
        Ray2D that = (Ray2D) obj;

        // Compare each field
        if (!EqualUtils.areEqual(this.x0, that.x0))
            return false;
        if (!EqualUtils.areEqual(this.y0, that.y0))
            return false;
        if (!EqualUtils.areEqual(this.dx, that.dx))
            return false;
        if (!EqualUtils.areEqual(this.dy, that.dy))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + Double.valueOf(this.x0).hashCode();
        hash = hash * 31 + Double.valueOf(this.y0).hashCode();
        hash = hash * 31 + Double.valueOf(this.dx).hashCode();
        hash = hash * 31 + Double.valueOf(this.dy).hashCode();
        return hash;
    }

    /**
     * @deprecated use copy constructor instead (0.11.2)
     */
    @Deprecated
    @Override
    public Ray2D clone() {
        return new Ray2D(x0, y0, dx, dy);

    }

    @Override
    public Vector2D leftTangent(double t) {
        return null;
    }

    @Override
    public Vector2D rightTangent(double t) {
        return null;
    }

    @Override
    public LinearCurve2D asPolyline(int n) {
        return null;
    }

    @Override
    public Path appendPath(Path path) {
        return path;
    }
}