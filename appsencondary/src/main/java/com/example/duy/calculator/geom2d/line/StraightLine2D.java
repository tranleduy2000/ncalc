/* File StraightLine2D.java 
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

//Imports

import android.graphics.Path;

import com.example.duy.calculator.geom2d.util.Angle2D;
import com.example.duy.calculator.geom2d.util.EqualUtils;
import com.example.duy.calculator.geom2d.GeometricObject2D;
import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.util.Shape2D;
import com.example.duy.calculator.geom2d.util.UnboundedShape2DException;
import com.example.duy.calculator.geom2d.Vector2D;
import com.example.duy.calculator.geom2d.curve.Curve2D;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implementation of a straight line. Such a line can be constructed using two
 * points, a point and a parallel line or straight object, or with coefficient
 * of the Cartesian equation.
 */
public class StraightLine2D extends AbstractLine2D implements
        Cloneable, Curve2D {
    public final static double ACCURACY = 1e-12;

    /**
     * Empty constructor: a straight line corresponding to horizontal axis.
     */
    public StraightLine2D() {
        this(0, 0, 1, 0);
    }

    /**
     * Defines a new Straight line going through the two given points.
     */
    public StraightLine2D(Point2D point1, Point2D point2) {
        this(point1, new Vector2D(point1, point2));
    }

    /**
     * Defines a new Straight line going through the given point, and with the
     * specified direction vector.
     */
    public StraightLine2D(Point2D point, Vector2D direction) {
        this(point.x(), point.y(), direction.x(), direction.y());
    }

    /**
     * Defines a new Straight line going through the given point, and with the
     * specified direction vector.
     */
    public StraightLine2D(Point2D point, double dx, double dy) {
        this(point.x(), point.y(), dx, dy);
    }

    /**
     * Defines a new Straight line going through the given point, and with the
     * specified direction given by angle.
     */
    public StraightLine2D(Point2D point, double angle) {
        this(point.x(), point.y(), Math.cos(angle), Math.sin(angle));
    }

    /*
     * Defines a new Straight line going through the point (xp, yp) and with
     * the direction dx, dy.
     */
    public StraightLine2D(double xp, double yp, double dx, double dy) {
        super(xp, yp, dx, dy);
    }

    /**
     * Copy constructor:
     * Defines a new Straight line at the same position and with the same
     * direction than an other straight object (line, edge or ray).
     */
    public StraightLine2D(LinearShape2D line) {
        this(line.origin(), line.direction());
    }

    /**
     * Defines a new Straight line, parallel to another straigth object (ray,
     * straight line or edge), and going through the given point.
     */
    public StraightLine2D(LinearShape2D line, Point2D point) {
        this(point, line.direction());
    }

    /**
     * Defines a new straight line, from the coefficients of the cartesian
     * equation. The starting point of the line is then the point of the line
     * closest to the origin, and the direction vector has unit norm.
     */
    public StraightLine2D(double a, double b, double c) {
        this(0, 0, 1, 0);
        double d = a * a + b * b;
        x0 = -a * c / d;
        y0 = -b * c / d;
        double theta = Math.atan2(-a, b);
        dx = Math.cos(theta);
        dy = Math.sin(theta);
    }

    public static Point2D getMidPoint(Line2D line2D) {
        return Point2D.midPoint(line2D.p1, line2D.p2);
    }

    /**
     * Creates a straight line going through a point and with a given angle.
     *
     * @deprecated since 0.11.1
     */
    @Deprecated
    public static StraightLine2D create(Point2D point, double angle) {
        return new StraightLine2D(point.x(), point.y(), Math.cos(angle),
                Math.sin(angle));
    }

    /**
     * Creates a straight line through 2 points.
     *
     * @deprecated since 0.11.1
     */
    @Deprecated
    public static StraightLine2D create(Point2D p1, Point2D p2) {
        return new StraightLine2D(p1, p2);
    }

    /**
     * Creates a straight line through a point and with a given direction
     * vector.
     *
     * @deprecated since 0.11.1
     */
    @Deprecated
    public static StraightLine2D create(Point2D origin, Vector2D direction) {
        return new StraightLine2D(origin, direction);
    }

    /**
     * Creates a vertical straight line through the given point.
     *
     * @since 0.10.3
     */
    public static StraightLine2D createHorizontal(Point2D origin) {
        return new StraightLine2D(origin, new Vector2D(1, 0));
    }

    // ===================================================================
    // constructors

    /**
     * Creates a vertical straight line through the given point.
     *
     * @since 0.10.3
     */
    public static StraightLine2D createVertical(Point2D origin) {
        return new StraightLine2D(origin, new Vector2D(0, 1));
    }

    /**
     * Creates a median between 2 points.
     *
     * @param p1 one point
     * @param p2 another point
     * @return the median of points p1 and p2
     * @since 0.6.3
     */
    public static StraightLine2D createMedian(Point2D p1, Point2D p2) {
        Point2D mid = Point2D.midPoint(p1, p2);
        StraightLine2D line = StraightLine2D.create(p1, p2);
        return StraightLine2D.createPerpendicular(line, mid);
    }

    /**
     * Returns a new Straight line, parallel to another straight object (ray,
     * straight line or edge), and going through the given point.
     *
     * @since 0.6.3
     */
    public static StraightLine2D createParallel(LinearShape2D line,
                                                Point2D point) {
        return new StraightLine2D(line, point);
    }

    /**
     * Returns a new Straight line, parallel to another straight object (ray,
     * straight line or edge), and going through the given point.
     *
     * @since 0.6.3
     */
    public static StraightLine2D createParallel(LinearShape2D linear,
                                                double d) {
        StraightLine2D line = linear.supportingLine();
        double d2 = d / Math.hypot(line.dx, line.dy);
        return new StraightLine2D(
                line.x0 + line.dy * d2, line.y0 - line.dx * d2,
                line.dx, line.dy);
    }

    /**
     * Returns a new Straight line, perpendicular to a straight object (ray,
     * straight line or edge), and going through the given point.
     *
     * @since 0.6.3
     */
    public static StraightLine2D createPerpendicular(
            LinearShape2D linear, Point2D point) {
        StraightLine2D line = linear.supportingLine();
        return new StraightLine2D(point, -line.dy, line.dx);
    }

    /**
     * Returns a new Straight line, with the given coefficient of the cartesian
     * equation (a*x + b*y + c = 0).
     */
    public static StraightLine2D createCartesian(double a, double b,
                                                 double c) {
        return new StraightLine2D(a, b, c);
    }

    /**
     * Computes the intersection point of the two (infinite) lines going through
     * p1 and p2 for the first one, and p3 and p4 for the second one. Returns
     * null if two lines are parallel.
     */
    public static Point2D getIntersection(Point2D p1, Point2D p2, Point2D p3,
                                          Point2D p4) {
        StraightLine2D line1 = new StraightLine2D(p1, p2);
        StraightLine2D line2 = new StraightLine2D(p3, p4);
        return line1.intersection(line2);
    }


    // ===================================================================
    // methods specific to StraightLine2D

    /**
     * Returns a new Straight line, parallel to another straight object (ray,
     * straight line or edge), and going through the given point.
     */
    public StraightLine2D parallel(Point2D point) {
        return new StraightLine2D(point, dx, dy);
    }

    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

    /**
     * Returns the parallel line located at a distance d from the line.
     * Distance is positive in the 'right' side of the line (outside of the
     * limiting half-plane), and negative in the 'left' of the line.
     *
     * @throws DegeneratedLine2DException if line direction vector is null
     */
    public StraightLine2D parallel(double d) {
        double d2 = Math.hypot(this.dx, this.dy);
        if (Math.abs(d2) < Shape2D.ACCURACY)
            throw new DegeneratedLine2DException(
                    "Can not compute parallel of degenerated line", this);
        d2 = d / d2;
        return new StraightLine2D(x0 + dy * d2, y0 - dx * d2, dx, dy);
    }

    /**
     * Returns a new Straight line, parallel to another straight object (ray,
     * straight line or edge), and going through the given point.
     */
    @Override
    public StraightLine2D perpendicular(Point2D point) {
        return new StraightLine2D(point, -dy, dx);
    }


    // ===================================================================
    // methods specific to OrientedCurve2D interface

    /* (non-Javadoc)
     * @see OrientedCurve2D#windingAngle(Point2D)
     */
    @Override
    public double windingAngle(Point2D point) {

        double angle1 = Angle2D.horizontalAngle(-dx, -dy);
        double angle2 = Angle2D.horizontalAngle(dx, dy);

        if (this.isInside(point)) {
            if (angle2 > angle1)
                return angle2 - angle1;
            else
                return 2 * Math.PI - angle1 + angle2;
        } else {
            if (angle2 > angle1)
                return angle2 - angle1 - 2 * Math.PI;
            else
                return angle2 - angle1;
        }
    }


    // ===================================================================
    // methods implementing the ContinuousCurve2D interface

    @Override
    public Vector2D leftTangent(double t) {
        return null;
    }

    @Override
    public Vector2D rightTangent(double t) {
        return null;
    }

    /**
     * Throws an exception when called.
     */
    @Override
    public com.example.duy.calculator.geom2d.polygon.LinearCurve2D asPolyline(int n) {
        throw new UnboundedShape2DException(this);
    }


    // ===================================================================
    // methods implementing the Curve2D interface

    /**
     * Throws an infiniteShapeException
     */
    @Override
    public Point2D firstPoint() {
        throw new UnboundedShape2DException(this);
    }

    /**
     * Throws an infiniteShapeException
     */
    @Override
    public Point2D lastPoint() {
        throw new UnboundedShape2DException(this);
    }

    /**
     * Returns an empty list of points.
     */
    @Override
    public Collection<Point2D> singularPoints() {
        return new ArrayList<Point2D>(0);
    }

    /**
     * Returns false, whatever the position.
     */
    @Override
    public boolean isSingular(double pos) {
        return false;
    }

    @Override
    public Curve2D clone() {
        return null;
    }

    /**
     * Returns the parameter of the first point of the line, which is always
     * Double.NEGATIVE_INFINITY.
     */
    public double t0() {
        return Double.NEGATIVE_INFINITY;
    }

    /**
     * @deprecated replaced by t0() (since 0.11.1).
     */
    @Deprecated
    public double getT0() {
        return t0();
    }

    /**
     * Returns the parameter of the last point of the line, which is always
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
        return this.t1();
    }

    /**
     * Returns the point specified with the parametric representation of the
     * line.
     */
    public Point2D point(double t) {
        return new Point2D(x0 + dx * t, y0 + dy * t);
    }


    /**
     * Returns the straight line with same origin but with opposite direction
     * vector.
     */
    public StraightLine2D reverse() {
        return new StraightLine2D(this.x0, this.y0, -this.dx, -this.dy);
    }

    public Path appendPath(Path path) {
        throw new UnboundedShape2DException(this);
    }


    // ===================================================================
    // methods implementing the Shape2D interface

    /**
     * Always returns false, because a line is not bounded.
     */
    public boolean isBounded() {
        return false;
    }

    /**
     * Returns the distance of the point (x, y) to this straight line.
     */
    @Override
    public double distance(double x, double y) {
        Point2D proj = super.projectedPoint(x, y);
        return proj.distance(x, y);
    }


    // ===================================================================
    // methods implementing the Shape interface

    /**
     * Returns true if the point (x, y) lies on the line, with precision given
     * by Shape2D.ACCURACY.
     */
    public boolean contains(double x, double y) {
        return super.supportContains(x, y);
    }

    /**
     * Returns true if the point p lies on the line, with precision given by
     * Shape2D.ACCURACY.
     */
    @Override
    public boolean contains(Point2D p) {
        return super.supportContains(p.x(), p.y());
    }

    /**
     * Throws an infiniteShapeException
     */
    public Path getPath() {
        throw new UnboundedShape2DException(this);
    }

    // ===================================================================
    // methods implementing the GeometricObject2D interface

    /* (non-Javadoc)
     * @see GeometricObject2D#almostEquals(GeometricObject2D, double)
     */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof StraightLine2D))
            return false;
        StraightLine2D line = (StraightLine2D) obj;

        if (Math.abs(x0 - line.x0) > eps)
            return false;
        if (Math.abs(y0 - line.y0) > eps)
            return false;
        if (Math.abs(dx - line.dx) > eps)
            return false;
        if (Math.abs(dy - line.dy) > eps)
            return false;

        return true;
    }


    // ===================================================================
    // methods overriding the Object class

    @Override
    public String toString() {
        return "StraightLine2D(" + x0 + "," + y0 + "," +
                dx + "," + dy + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof StraightLine2D))
            return false;
        StraightLine2D that = (StraightLine2D) obj;

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


    @Override
    public boolean isInside(Point2D pt) {
        return false;
    }

}