/* file : Circle2D.java
 * 
 * Project : geometry
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
 * 
 * Created on 30 avr. 2006
 *
 */

package com.example.duy.calculator.geom2d.conic;

import android.graphics.Path;
import android.util.Log;

import com.example.duy.calculator.geom2d.util.Angle2D;
import com.example.duy.calculator.geom2d.util.ColinearPoints2DException;
import com.example.duy.calculator.geom2d.GeometricObject2D;
import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.util.Shape2D;
import com.example.duy.calculator.geom2d.Vector2D;
import com.example.duy.calculator.geom2d.curve.AbstractSmoothCurve2D;
import com.example.duy.calculator.geom2d.line.AbstractLine2D;
import com.example.duy.calculator.geom2d.line.LineSegment2D;
import com.example.duy.calculator.geom2d.line.LinearShape2D;
import com.example.duy.calculator.geom2d.line.StraightLine2D;
import com.example.duy.calculator.geom2d.polygon.LinearCurve2D;
import com.example.duy.calculator.geom2d.util.EqualUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * A circle in the plane, defined as the set of points located at an equal
 * distance from the circle center. A circle is a particular ellipse, with first
 * and second axis length equal.
 *
 * @author dlegland
 */
public class Circle2D extends AbstractSmoothCurve2D
        implements CircularShape2D {

    // ===================================================================
    // Static methods

    private static String TAG = Circle2D.class.getName();
    /**
     * Coordinate of center.
     */
    protected double xc;
    protected double yc;
    /**
     * the radius of the circle.
     */
    protected double r = 0;
    /**
     * Directed circle or not
     */
    protected boolean direct = true;
    /**
     * Orientation of major semi-axis, in radians, between 0 and 2*PI.
     */
    protected double theta = 0;

    /**
     * Empty constructor: center 0,0 and radius 0.
     */
    public Circle2D() {
        this(0, 0, 0, true);
    }

    /**
     * Create a new circle with specified point center and radius
     */
    public Circle2D(Point2D center, double radius) {
        this(center.x(), center.y(), radius, true);
    }

    /**
     * Create a new circle with specified center, radius and orientation
     */
    public Circle2D(Point2D center, double radius, boolean direct) {
        this(center.x(), center.y(), radius, direct);
    }

    /**
     * Create a new circle with specified center and radius
     */
    public Circle2D(double xcenter, double ycenter, double radius) {
        this(xcenter, ycenter, radius, true);
    }


    // ===================================================================
    // Class variables

    /**
     * Create a new circle with specified center, radius and orientation.
     */
    public Circle2D(double xcenter, double ycenter, double radius,
                    boolean direct) {
        this.xc = xcenter;
        this.yc = ycenter;
        this.r = radius;
        this.direct = direct;
    }

    /**
     * Creates a circle from a center and a radius.
     *
     * @deprecated since 0.11.1
     */
    @Deprecated
    public static Circle2D create(Point2D center, double radius) {
        return new Circle2D(center, radius);
    }

    /**
     * Creates a circle from a center, a radius, and a flag indicating
     * orientation.
     *
     * @deprecated since 0.11.1
     */
    @Deprecated
    public static Circle2D create(Point2D center, double radius, boolean direct) {
        return new Circle2D(center, radius, direct);
    }

    /**
     * Creates a circle containing 3 points.
     *
     * @deprecated replaced by createCircle(Point2D, Point2D, Point2D) (0.11.1)
     */
    @Deprecated
    public static Circle2D create(Point2D p1, Point2D p2, Point2D p3) {
        if (Point2D.isColinear(p1, p2, p3))
            throw new ColinearPoints2DException(p1, p2, p3);

        // create two median lines
        StraightLine2D line12 = StraightLine2D.createMedian(p1, p2);
        StraightLine2D line23 = StraightLine2D.createMedian(p2, p3);

        // check medians are not parallel
        assert !AbstractLine2D.isParallel(line12, line23) :
                "If points are not colinear, medians should not be parallel";

        // Compute intersection of the medians, and circle radius
        Point2D center = AbstractLine2D.getIntersection(line12, line23);
        double radius = Point2D.distance(center, p2);

        // return the created circle
        return new Circle2D(center, radius);
    }

    /**
     * @deprecated replaced by circlesIntersections(Circle2D, Circle2D) (0.11.1)
     */
    @Deprecated
    public static Collection<Point2D> getIntersections(Circle2D circle1,
                                                       Circle2D circle2) {
        ArrayList<Point2D> intersections = new ArrayList<Point2D>(2);

        // extract center and radius of each circle
        Point2D center1 = circle1.center();
        Point2D center2 = circle2.center();
        double r1 = circle1.radius();
        double r2 = circle2.radius();

        double d = Point2D.distance(center1, center2);

        // case of no intersection
        if (d < abs(r1 - r2) || d > (r1 + r2))
            return intersections;

        // Angle of line from center1 to center2
        double angle = Angle2D.horizontalAngle(center1, center2);

        // position of intermediate point
        double d1 = d / 2 + (r1 * r1 - r2 * r2) / (2 * d);
        Point2D tmp = Point2D.createPolar(center1, d1, angle);

        // Add the 2 intersection points
        double h = sqrt(r1 * r1 - d1 * d1);
        intersections.add(Point2D.createPolar(tmp, h, angle + PI / 2));
        intersections.add(Point2D.createPolar(tmp, h, angle - PI / 2));

        return intersections;
    }


    // ===================================================================
    // Constructors

    /**
     * Computes intersections of a circle with a line. Returns an array of
     * Point2D, of size 0, 1 or 2 depending on the distance between circle and
     * line. If there are 2 intersections points, the first one in the array is
     * the first one on the line.
     *
     * @deprecated replaced by lineCircleIntersections(LinearShape2D, CircularShape2D) (0.11.1)
     */
    @Deprecated
    public static Collection<Point2D> getIntersections(
            Circle2D circle,
            LineSegment2D line) {
        // initialize array of points (maximum 2 intersections)
        ArrayList<Point2D> intersections = new ArrayList<Point2D>(2);

        // extract parameters of the circle
        Circle2D parent = circle.supportingCircle();
        Point2D center = parent.center();
        double radius = parent.radius();

        // Compute line perpendicular to the test line, and going through the
        // circle center
        StraightLine2D perp = StraightLine2D.createPerpendicular(line, center);

        // Compute distance between line and circle center
        Point2D inter = perp.intersection(new StraightLine2D(line));
        assert (inter != null);
        double dist = inter.distance(center);

        // if the distance is the radius of the circle, return the
        // intersection point
        if (abs(dist - radius) < Shape2D.ACCURACY) {
            if (line.contains(inter) && circle.contains(inter))
                intersections.add(inter);
            return intersections;
        }

        // compute angle of the line, and distance between 'inter' point and
        // each intersection point
        double angle = line.horizontalAngle();
        double d2 = sqrt(radius * radius - dist * dist);

        // Compute position and angle of intersection points
        Point2D p1 = Point2D.createPolar(inter, d2, angle + Math.PI);
        Point2D p2 = Point2D.createPolar(inter, d2, angle);

        // add points to the array only if they belong to the line
        if (line.contains(p1) && circle.contains(p1))
            intersections.add(p1);
        if (line.contains(p2) && circle.contains(p2))
            intersections.add(p2);

        // return the result
        return intersections;
    }

    /**
     * Computes the circumscribed circle of the 3 input points.
     *
     * @return the circle that contains the three input points
     * @throws ColinearPoints2DException if the 3 points are colinear
     */
    public static Circle2D circumCircle(Point2D p1, Point2D p2, Point2D p3) {
        // Computes circum center, possibly throwing ColinearPoints2DException
        Point2D center = circumCenter(p1, p2, p3);

        // compute radius
        double radius = Point2D.distance(center, p2);

        // return the created circle
        return new Circle2D(center, radius);
    }

    /**
     * Computes the center of the circumscribed circle of the three input points.
     *
     * @throws ColinearPoints2DException if the 3 points are colinear
     */
    public static Point2D circumCenter(Point2D p1, Point2D p2, Point2D p3) {
        if (Point2D.isColinear(p1, p2, p3))
            throw new ColinearPoints2DException(p1, p2, p3);

        // create two median lines
        StraightLine2D line12 = StraightLine2D.createMedian(p1, p2);
        StraightLine2D line23 = StraightLine2D.createMedian(p2, p3);

        // check medians are not parallel
        assert !AbstractLine2D.isParallel(line12, line23) :
                "If points are not colinear, medians should not be parallel";

        // Compute intersection of the medians, and circle radius
        Point2D center = AbstractLine2D.getIntersection(line12, line23);

        // return the center
        return center;
    }

    /**
     * Computes the intersections points between two circles or circular shapes.
     *
     * @param circle1 an instance of circle or circle arc
     * @param circle2 an instance of circle or circle arc
     * @return a collection of 0, 1 or 2 intersection points
     */
    public static Collection<Point2D> circlesIntersections(Circle2D circle1,
                                                           Circle2D circle2) {
        // extract center and radius of each circle
        Point2D center1 = circle1.center();
        Point2D center2 = circle2.center();
        double r1 = circle1.radius();
        double r2 = circle2.radius();

        double d = Point2D.distance(center1, center2);

        // case of no intersection
        if (d < abs(r1 - r2) || d > (r1 + r2))
            return new ArrayList<Point2D>(0);

        // Angle of line from center1 to center2
        double angle = Angle2D.horizontalAngle(center1, center2);

        if (d == abs(r1 - r2) || d == (r1 + r2)) {
            Collection<Point2D> r = new ArrayList<>(1);
            r.add(Point2D.createPolar(center1, r1, angle));
            return r;
        }

        // position of intermediate point
        double d1 = d / 2 + (r1 * r1 - r2 * r2) / (2 * d);
        Point2D tmp = Point2D.createPolar(center1, d1, angle);

        // distance between intermediate point and each intersection
        double h = sqrt(r1 * r1 - d1 * d1);

        // create empty array
        ArrayList<Point2D> intersections = new ArrayList<Point2D>(2);

        // Add the 2 intersection points
        Point2D p1 = Point2D.createPolar(tmp, h, angle + PI / 2);
        intersections.add(p1);
        Point2D p2 = Point2D.createPolar(tmp, h, angle - PI / 2);
        intersections.add(p2);

        return intersections;
    }

    /**
     * Computes intersections of a circle with a line. Returns an array of
     * Point2D, of size 0, 1 or 2 depending on the distance between circle and
     * line. If there are 2 intersections points, the first one in the array is
     * the first one on the line.
     *
     * @return a collection of intersection points
     * @since 0.11.1
     */
    public static Collection<Point2D> lineCircleIntersections(
            LineSegment2D line, CircleArc2D circle) {
        // initialize array of points (maximum 2 intersections)
        ArrayList<Point2D> intersections = new ArrayList<Point2D>(2);

        // extract parameters of the circle
        Circle2D parent = circle.supportingCircle();
        Point2D center = parent.center();
        double radius = parent.radius();

        // Compute line perpendicular to the test line, and going through the
        // circle center
        StraightLine2D perp = StraightLine2D.createPerpendicular(line, center);

        // Compute distance between line and circle center
        Point2D inter = perp.intersection(new StraightLine2D(line));
        if (inter == null) {
            throw new RuntimeException("Could not compute intersection point when computing line-cicle intersection");
        }
        double dist = inter.distance(center);

        // if the distance is the radius of the circle, return the
        // intersection point
        if (abs(dist - radius) < Shape2D.ACCURACY) {
            if (line.contains(inter) && circle.contains(inter))
                intersections.add(inter);
            return intersections;
        }

        // compute angle of the line, and distance between 'inter' point and
        // each intersection point
        double angle = line.horizontalAngle();
        double d2 = sqrt(radius * radius - dist * dist);

        // Compute position and angle of intersection points
        Point2D p1 = Point2D.createPolar(inter, d2, angle + Math.PI);
        Point2D p2 = Point2D.createPolar(inter, d2, angle);

        // add points to the array only if they belong to the line
        if (line.contains(p1) && circle.contains(p1))
            intersections.add(p1);
        if (line.contains(p2) && circle.contains(p2))
            intersections.add(p2);

        // return the result
        return intersections;
    }

    public static Collection<Point2D> lineCircleIntersections(
            LineSegment2D line, Circle2D circle) {
        // initialize array of points (maximum 2 intersections)
        ArrayList<Point2D> intersections = new ArrayList<Point2D>(2);

        // extract parameters of the circle
        Circle2D parent = circle.supportingCircle();
        Point2D center = parent.center();
        double radius = parent.radius();

        // Compute line perpendicular to the test line, and going through the
        // circle center
        StraightLine2D perp = StraightLine2D.createPerpendicular(line, center);

        // Compute distance between line and circle center
        Point2D inter = perp.intersection(new StraightLine2D(line));
        if (inter == null) {
            throw new RuntimeException("Could not compute intersection point when computing line-circle intersection");
        }
        double dist = inter.distance(center);

        // if the distance is the radius of the circle, return the
        // intersection point
        if (abs(dist - radius) < Shape2D.ACCURACY) {
            if (line.contains(inter) && circle.contains(inter))
                intersections.add(inter);
            return intersections;
        }

        // compute angle of the line, and distance between 'inter' point and
        // each intersection point
        double angle = line.horizontalAngle();
        double d2 = sqrt(radius * radius - dist * dist);

        // Compute position and angle of intersection points
        Point2D p1 = Point2D.createPolar(inter, d2, angle + Math.PI);
        Point2D p2 = Point2D.createPolar(inter, d2, angle);

        // add points to the array only if they belong to the line
        if (line.contains(p1) && circle.contains(p1))
            intersections.add(p1);
        if (line.contains(p2) && circle.contains(p2))
            intersections.add(p2);

        // return the result
        return intersections;
    }


    // ===================================================================
    // methods specific to class Circle2D

    /**
     * Computes the radical axis of the two circles.
     *
     * @return the radical axis of the two circles.
     * @throws IllegalArgumentException if the two circles have same center
     * @since 0.11.1
     */
    public static StraightLine2D radicalAxis(Circle2D circle1,
                                             Circle2D circle2) {

        // extract center and radius of each circle
        double r1 = circle1.radius();
        double r2 = circle2.radius();
        Point2D p1 = circle1.center();
        Point2D p2 = circle2.center();

        // compute horizontal angle of joining line
        double angle = Angle2D.horizontalAngle(p1, p2);

        // distance between centers
        double dist = p1.distance(p2);
        if (dist < Shape2D.ACCURACY) {
            throw new IllegalArgumentException("Input circles must have distinct centers");
        }

        // position of the radical axis on the joining line
        double d = (dist * dist + r1 * r1 - r2 * r2) * .5 / dist;

        // pre-compute trigonometric functions
        double cot = Math.cos(angle);
        double sit = Math.sin(angle);

        // compute parameters of the line
        double x0 = p1.x() + d * cot;
        double y0 = p1.y() + d * sit;
        double dx = -sit;
        double dy = cot;

        // update state of current line
        return new StraightLine2D(x0, y0, dx, dy);
    }

    /**
     * Returns the radius of the circle.
     */
    public double radius() {
        return r;
    }

    // ===================================================================
    // methods implementing CircularShape2D interface

    /**
     * Returns the intersection points with another circle. The result is a
     * collection with 0, 1 or 2 points.
     */
    public Collection<Point2D> intersections(Circle2D circle) {
        return Circle2D.circlesIntersections(this, circle);
    }


    // ===================================================================
    // Methods implementing the Ellipse2D interface

    /**
     * Returns the circle itself.
     */
    public Circle2D supportingCircle() {
        return this;
    }

    /**
     * Returns true if circle has a direct orientation.
     */
    public boolean isDirect() {
        return direct;
    }

    /**
     * Returns center of the circle.
     */
    public Point2D center() {
        return new Point2D(xc, yc);
    }

    /**
     * Returns the first direction vector of the circle, in the direction of
     * the major axis.
     */
    public Vector2D vector1() {
        return new Vector2D(cos(theta), sin(theta));
    }

    /**
     * Returns the second direction vector of the circle, in the direction of
     * the minor axis.
     */
    public Vector2D vector2() {
        if (direct)
            return new Vector2D(-sin(theta), cos(theta));
        else
            return new Vector2D(sin(theta), -cos(theta));
    }

    /**
     * Returns the angle of the circle main axis with the Ox axis.
     */
    public double angle() {
        return theta;
    }

    /**
     * Returns the first focus, which for a circle is the same point as the
     * center.
     */
    public Point2D focus1() {
        return new Point2D(xc, yc);
    }

    /**
     * Returns the second focus, which for a circle is the same point as the
     * center.
     */
    public Point2D focus2() {
        return new Point2D(xc, yc);
    }

    public boolean isCircle() {
        return true;
    }


    // ===================================================================
    // methods implementing the Conic2D interface

    /**
     * Converts this circle to an instance of Ellipse2D.
     *
     * @return a new instance of Ellipse2D that corresponds to this circle
     */
    public Ellipse2D asEllipse() {
        return new Ellipse2D(this.xc, this.yc, this.r, this.r, this.theta, this.direct);
    }

    public Conic2D.Type conicType() {
        return Conic2D.Type.CIRCLE;
    }

    /**
     * Returns Cartesian equation of the circle:
     * <p/>
     * <code>(x-xc)^2 + (y-yc)^2 = r^2</code>, giving:
     * <p/>
     * <code>x^2 + 0*x*y + y^2 -2*xc*x -2*yc*y + xc*xc+yc*yc-r*r = 0</code>.
     */
    public double[] conicCoefficients() {
        return new double[]{
                1, 0, 1, -2 * xc, -2 * yc,
                xc * xc + yc * yc - r * r};
    }

    /**
     * Returns 0, which is the eccentricity of a circle by definition.
     */
    public double eccentricity() {
        return 0;
    }

    /**
     * Returns the parallel circle located at a distance d from this circle.
     * For direct circle, distance is positive outside of the circle,
     * and negative inside. This is the contrary for indirect circles.
     */
    public Circle2D parallel(double d) {
        double rp = max(direct ? r + d : r - d, 0);
        return new Circle2D(xc, yc, rp, direct);
    }

    /**
     * Returns perimeter of the circle (equal to 2*PI*radius).
     */
    public double length() {
        return PI * 2 * r;
    }

    public double length(double pos) {
        return pos * this.r;
    }


    // methods implementing OrientedCurve2D interface

    /* (non-Javadoc)
     * @see CirculinearCurve2D#position(double)
     */
    public double position(double length) {
        return length / this.r;
    }


    // ===================================================================
    // methods of SmoothCurve2D interface

    /**
     * Return either 0, 2*PI or -2*PI, depending whether the point is located
     * inside the interior of the ellipse or not.
     */
    public double windingAngle(Point2D point) {
        if (this.signedDistance(point) > 0)
            return 0;
        else
            return direct ? PI * 2 : -PI * 2;
    }

    public Vector2D tangent(double t) {
        if (!direct)
            t = -t;
        double cot = cos(theta);
        double sit = sin(theta);
        double cost = cos(t);
        double sint = sin(t);

        if (direct)
            return new Vector2D(
                    -r * sint * cot - r * cost * sit,
                    -r * sint * sit + r * cost * cot);
        else
            return new Vector2D(
                    r * sint * cot + r * cost * sit,
                    r * sint * sit - r * cost * cot);
    }

    /**
     * Returns the inverse of the circle radius.
     * If the circle is indirect, the curvature is negative.
     */
    public double curvature(double t) {
        double k = 1 / r;
        return direct ? k : -k;
    }

    // ===================================================================
    // methods of ContinuousCurve2D interface

    @Override
    public LinearCurve2D asPolyline(int n) {
        return null;
    }

    /**
     * Returns true, as an ellipse is always closed.
     */
    public boolean isClosed() {
        return true;
    }

    @Override
    public Vector2D leftTangent(double t) {
        return null;
    }


    // ===================================================================
    // methods of OrientedCurve2D interface

    @Override
    public Vector2D rightTangent(double t) {
        return null;
    }

    /**
     * Test whether the point is inside the circle. The test is performed by
     * translating the point, and re-scaling it such that its coordinates are
     * expressed in unit circle basis.
     */
    public boolean isInside(Point2D point) {
        double xp = (point.x() - this.xc) / this.r;
        double yp = (point.y() - this.yc) / this.r;
        return (xp * xp + yp * yp < 1) ^ !direct;
    }

    public double signedDistance(Point2D point) {
        return signedDistance(point.x(), point.y());
    }

    // ===================================================================
    // methods of Curve2D interface

    public double signedDistance(double x, double y) {
        if (direct)
            return Point2D.distance(xc, yc, x, y) - r;
        else
            return r - Point2D.distance(xc, yc, x, y);
    }

    /**
     * Always returns true.
     */
    public boolean isBounded() {
        return true;
    }

    /**
     * Always returns false.
     */
    public boolean isEmpty() {
        return false;
    }

    /**
     * Returns the parameter of the first point of the ellipse, set to 0.
     */
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
     * Returns the parameter of the last point of the ellipse, set to 2*PI.
     */
    public double t1() {
        return 2 * PI;
    }

    /**
     * @deprecated replaced by t1() (since 0.11.1).
     */
    @Deprecated
    public double getT1() {
        return t1();
    }

    /**
     * Get the position of the curve from internal parametric representation,
     * depending on the parameter t. This parameter is between the two limits 0
     * and 2*Math.PI.
     */
    public Point2D point(double t) {
        double angle = theta + t;
        if (!direct)
            angle = theta - t;
        return new Point2D(xc + r * cos(angle), yc + r * sin(angle));
    }

    /**
     * Get the first point of the circle, which is the same as the last point.
     *
     * @return the first point of the curve
     */
    public Point2D firstPoint() {
        return new Point2D(xc + r * cos(theta), yc + r * sin(theta));
    }

    /**
     * Get the last point of the circle, which is the same as the first point.
     *
     * @return the last point of the curve.
     */
    public Point2D lastPoint() {
        return new Point2D(xc + r * cos(theta), yc + r * sin(theta));
    }

    public double position(Point2D point) {
        double angle = Angle2D.horizontalAngle(xc, yc, point.x(), point.y());
        if (direct)
            return Angle2D.formatAngle(angle - theta);
        else
            return Angle2D.formatAngle(theta - angle);
    }

    /**
     * Computes the projection position of the point on the circle,
     * by computing angle with horizonrtal
     */
    public double project(Point2D point) {
        double xp = point.x() - this.xc;
        double yp = point.y() - this.yc;

        // compute angle
        return Angle2D.horizontalAngle(xp, yp);
    }

    @Override
    public Collection<Point2D> intersections(LinearShape2D line) {
        return null;
    }

    /**
     * Returns the circle with same center and same radius, but with the
     * opposite orientation.
     */
    public Circle2D reverse() {
        return new Circle2D(this.xc, this.yc, this.r, !this.direct);
    }


    // ===================================================================
    // methods of Shape2D interface

    /**
     * Returns a new CircleArc2D. t0 and t1 are position on circle.
     */
    public CircleArc2D subCurve(double t0, double t1) {
        double startAngle, extent;
        if (this.direct) {
            startAngle = t0;
            extent = Angle2D.formatAngle(t1 - t0);
        } else {
            extent = -Angle2D.formatAngle(t1 - t0);
            startAngle = Angle2D.formatAngle(-t0);
        }
        return new CircleArc2D(this, startAngle, extent);
    }

    public double distance(Point2D point) {
        return Math.abs(Point2D.distance(xc, yc, point.x(), point.y()) - r);
    }

    public double distance(double x, double y) {
        return Math.abs(Point2D.distance(xc, yc, x, y) - r);
    }


    // ===================================================================
    // methods of Shape interface

    /**
     * Computes intersections of the circle with a line. Return an array of
     * Point2D, of size 0, 1 or 2 depending on the distance between circle and
     * line. If there are 2 intersections points, the first one in the array is
     * the first one on the line.
     */
    public Collection<Point2D> intersections(LineSegment2D line) {
        return Circle2D.lineCircleIntersections(line, this);
    }

    /**
     * Returns true if the point p lies on the ellipse, with precision given
     * by Shape2D.ACCURACY.
     */
    public boolean contains(Point2D p) {
        return contains(p.x(), p.y());
    }

    /**
     * Returns true if the point (x, y) lies exactly on the circle.
     */
    public boolean contains(double x, double y) {
        return abs(distance(x, y)) <= Shape2D.ACCURACY;
    }


    // ===================================================================
    // methods implementing GeometricObject2D interface

    public Path appendPath(Path path) {
        double cot = cos(theta);
        double sit = sin(theta);
        double cost, sint;

        if (direct) {
            // Counter-clockwise circle
            for (double t = .1; t < PI * 2; t += .1) {
                cost = cos(t);
                sint = sin(t);
                path.lineTo(
                        (float) (xc + r * cost * cot - r * sint * sit),
                        (float) (yc + r * cost * sit + r * sint * cot));
            }
        } else {
            // Clockwise circle
            for (double t = .1; t < PI * 2; t += .1) {
                cost = cos(t);
                sint = sin(t);
                path.lineTo(
                        (float) (xc + r * cost * cot + r * sint * sit),
                        (float) (yc + r * cost * sit - r * sint * cot));
            }
        }

        // line to first point
        path.lineTo((float) (xc + r * cot), (float) (yc + r * sit));

        return path;
    }


    // ===================================================================
    // methods of Object interface

    /* (non-Javadoc)
     * @see GeometricObject2D#almostEquals(GeometricObject2D, double)
     */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
        if (!(obj instanceof Circle2D))
            return false;

        Circle2D circle = (Circle2D) obj;

        if (abs(circle.xc - xc) > eps)
            return false;
        if (abs(circle.yc - yc) > eps)
            return false;
        if (abs(circle.r - r) > eps)
            return false;
        if (circle.direct != direct)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "Circle2D(%7.2f,%7.2f,%7.2f,%s)",
                xc, yc, r, direct ? "true" : "false");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Circle2D) {
            Circle2D that = (Circle2D) obj;

            // Compare each field
            if (!EqualUtils.areEqual(this.xc, that.xc))
                return false;
            if (!EqualUtils.areEqual(this.yc, that.yc))
                return false;
            if (!EqualUtils.areEqual(this.r, that.r))
                return false;
            if (this.direct != that.direct)
                return false;

            return true;
        }
        return super.equals(obj);
    }

    @Override
    public Circle2D clone() {
        return new Circle2D(xc, yc, r, direct);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + center().hashCode();
        hash = hash * 31 + Double.valueOf(radius()).hashCode();
        return hash;
    }

    /**
     * tính diện tính hình tròn pi*r^2
     *
     * @return double
     */
    public double getArea() {
        return Math.PI * Math.pow(radius(), 2.0);
    }

    /**
     * công thức: (x - a) ^ 2 + (y-b)^ 2 = R^2
     *
     * @return phương trình đường tròn
     */
    public String getEquationCircle() {
        StringBuilder equation = new StringBuilder();
        equation.append("(x");
        if (xc > 0) {
            equation.append("-");
            equation.append(xc);
        } else if (xc < 0) {
            equation.append("+");
            equation.append(xc);
        }
        equation.append(")^2 + (y");
        if (yc > 0) {
            equation.append("-");
            equation.append(yc);
        } else if (yc < 0) {
            equation.append("+");
            equation.append(yc);
        }
        equation.append(")^2 = ");
        equation.append(Math.pow(r, 2.0d));

        return equation.toString();
    }

    /**
     * công thức (x0 - a)(x - x0) + (y0 - b)(y - y0) = 0;
     *
     * @param point2D - điểm
     * @return phương trình tiếp tuyến
     */
    public String getEquationTangent(Point2D point2D) {
        if (!contains(point2D)) {
            throw new RuntimeException("point not contain in circle");
        }
        //extract
        double x0 = point2D.x();
        double y0 = point2D.y();
        StringBuilder equation = new StringBuilder();

        double mX = x0 - xc;
        if (mX > 0 || mX < 0) {
            equation.append(mX);
            equation.append("x");
        }
        Log.d(TAG, String.valueOf(mX));

        double mY = y0 - yc;
        if (mY > 0 || mY < 0) {
            equation.append(mY);
            equation.append("y");
        }
        Log.d(TAG, String.valueOf(mY));

        double mC = -x0 * (x0 - xc) - y0 * (y0 - yc);
        if (mC > 0) {
            equation.append(" +");
            equation.append(mC);
        } else if (mC < 0) {
            equation.append(mC);
        }
        Log.d(TAG, String.valueOf(mC));

        equation.append(" = 0");
        return equation.toString();
    }

    @Override
    public StraightLine2D supportingLine() {
        return null;
    }

    @Override
    public double horizontalAngle() {
        return 0;
    }

    @Override
    public Point2D origin() {
        return null;
    }

    @Override
    public Vector2D direction() {
        return null;
    }


    @Override
    public boolean containsProjection(Point2D point) {
        return false;
    }

}
