/* file : EllipseArc2D.java
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
 * Created on 24 avr. 2006
 *
 */

package com.example.duy.calculator.geom2d.conic;

import android.graphics.Path;

import com.example.duy.calculator.geom2d.util.Angle2D;
import com.example.duy.calculator.geom2d.GeometricObject2D;
import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.util.Shape2D;
import com.example.duy.calculator.geom2d.Vector2D;
import com.example.duy.calculator.geom2d.curve.AbstractSmoothCurve2D;
import com.example.duy.calculator.geom2d.line.LinearShape2D;
import com.example.duy.calculator.geom2d.line.Ray2D;
import com.example.duy.calculator.geom2d.line.StraightLine2D;
import com.example.duy.calculator.geom2d.polygon.Polyline2D;
import com.example.duy.calculator.geom2d.util.EqualUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;

/**
 * An arc of ellipse. It is defined by a supporting ellipse, a starting angle,
 * and a signed angle extent, both in radians. The ellipse arc is oriented
 * counter-clockwise if angle extent is positive, and clockwise otherwise.
 *
 * @author dlegland
 */
public class EllipseArc2D extends AbstractSmoothCurve2D {

    // ====================================================================
    // methods specific to EllipseArc2D

    /**
     * The supporting ellipse
     */
    protected Ellipse2D ellipse;
    /**
     * The starting position on ellipse, in radians between 0 and +2PI
     */
    protected double startAngle = 0;


    // ====================================================================
    // Class variables
    /**
     * The signed angle extent, in radians between -2PI and +2PI.
     */
    protected double angleExtent = PI;

    /**
     * Construct a default Ellipse arc, centered on (0,0), with radii equal to 1
     * and 1, orientation equal to 0, start angle equal to 0, and angle extent
     * equal to PI/2.
     */
    public EllipseArc2D() {
        this(0, 0, 1, 1, 0, 0, PI / 2);
    }

    /**
     * Specify supporting ellipse, start angle and angle extent.
     *
     * @param ell    the supporting ellipse
     * @param start  the starting angle (angle between 0 and 2*PI)
     * @param extent the angle extent (signed angle)
     */
    public EllipseArc2D(Ellipse2D ell, double start, double extent) {
        this(ell.xc, ell.yc, ell.r1, ell.r2, ell.theta, start, extent);
    }


    // ====================================================================
    // Constructors

    /**
     * Specify supporting ellipse, start angle and end angle, and a flag
     * indicating whether the arc is directed or not.
     *
     * @param ell    the supporting ellipse
     * @param start  the starting angle
     * @param end    the ending angle
     * @param direct flag indicating if the arc is direct
     */
    public EllipseArc2D(Ellipse2D ell, double start, double end, boolean direct) {
        this(ell.xc, ell.yc, ell.r1, ell.r2, ell.theta, start, end, direct);
    }

    /**
     * Specify parameters of supporting ellipse, start angle, and angle extent.
     */
    public EllipseArc2D(double xc, double yc, double a, double b, double theta,
                        double start, double extent) {
        this.ellipse = new Ellipse2D(xc, yc, a, b, theta);
        this.startAngle = start;
        this.angleExtent = extent;
    }

    /**
     * Specify parameters of supporting ellipse, bounding angles and flag for
     * direct ellipse.
     */
    public EllipseArc2D(double xc, double yc, double a, double b, double theta,
                        double start, double end, boolean direct) {
        this.ellipse = new Ellipse2D(xc, yc, a, b, theta);
        this.startAngle = start;
        this.angleExtent = Angle2D.formatAngle(end - start);
        if (!direct)
            this.angleExtent = this.angleExtent - PI * 2;
    }

    /**
     * Specify supporting ellipse, start angle and end angle, and a flag
     * indicating whether the arc is directed or not.
     *
     * @param ell    the supporting ellipse
     * @param start  the starting angle
     * @param extent the (signed) angle extent
     * @deprecated since 0.11.1
     */
    @Deprecated
    public static EllipseArc2D create(Ellipse2D ell, double start,
                                      double extent) {
        return new EllipseArc2D(ell.xc, ell.yc, ell.r1, ell.r2, ell.theta,
                start, extent);
    }

    /**
     * Specify supporting ellipse, start angle and end angle, and a flag
     * indicating whether the arc is directed or not.
     *
     * @param ell    the supporting ellipse
     * @param start  the starting angle
     * @param end    the ending angle
     * @param direct flag indicating if the arc is direct
     * @deprecated since 0.11.1
     */
    @Deprecated
    public static EllipseArc2D create(Ellipse2D ell, double start, double end,
                                      boolean direct) {
        return new EllipseArc2D(ell.xc, ell.yc, ell.r1, ell.r2, ell.theta,
                start, end, direct);
    }

    // ====================================================================
    // methods specific to EllipseArc2D

    /**
     * btan computes the length (k) of the control segments at
     * the beginning and end of a cubic Bezier that approximates
     * a segment of an arc with extent less than or equal to
     * 90 degrees.  This length (k) will be used to generate the
     * 2 Bezier control points for such a segment.
     * <p/>
     * Assumptions:
     * a) arc is centered on 0,0 with radius of 1.0
     * b) arc extent is less than 90 degrees
     * c) control points should preserve tangent
     * d) control segments should have equal length
     * <p/>
     * Initial data:
     * start angle: ang1
     * end angle:   ang2 = ang1 + extent
     * start point: P1 = (x1, y1) = (cos(ang1), sin(ang1))
     * end point:   P4 = (x4, y4) = (cos(ang2), sin(ang2))
     * <p/>
     * Control points:
     * P2 = (x2, y2)
     * | x2 = x1 - k * sin(ang1) = cos(ang1) - k * sin(ang1)
     * | y2 = y1 + k * cos(ang1) = sin(ang1) + k * cos(ang1)
     * <p/>
     * P3 = (x3, y3)
     * | x3 = x4 + k * sin(ang2) = cos(ang2) + k * sin(ang2)
     * | y3 = y4 - k * cos(ang2) = sin(ang2) - k * cos(ang2)
     * <p/>
     * The formula for this length (k) can be found using the
     * following derivations:
     * <p/>
     * Midpoints:
     * a) Bezier (t = 1/2)
     * bPm = P1 * (1-t)^3 +
     * 3 * P2 * t * (1-t)^2 +
     * 3 * P3 * t^2 * (1-t) +
     * P4 * t^3 =
     * = (P1 + 3P2 + 3P3 + P4)/8
     * <p/>
     * b) arc
     * aPm = (cos((ang1 + ang2)/2), sin((ang1 + ang2)/2))
     * <p/>
     * Let angb = (ang2 - ang1)/2; angb is half of the angle
     * between ang1 and ang2.
     * <p/>
     * Solve the equation bPm == aPm
     * <p/>
     * a) For xm coord:
     * x1 + 3*x2 + 3*x3 + x4 = 8*cos((ang1 + ang2)/2)
     * <p/>
     * cos(ang1) + 3*cos(ang1) - 3*k*sin(ang1) +
     * 3*cos(ang2) + 3*k*sin(ang2) + cos(ang2) =
     * = 8*cos((ang1 + ang2)/2)
     * <p/>
     * 4*cos(ang1) + 4*cos(ang2) + 3*k*(sin(ang2) - sin(ang1)) =
     * = 8*cos((ang1 + ang2)/2)
     * <p/>
     * 8*cos((ang1 + ang2)/2)*cos((ang2 - ang1)/2) +
     * 6*k*sin((ang2 - ang1)/2)*cos((ang1 + ang2)/2) =
     * = 8*cos((ang1 + ang2)/2)
     * <p/>
     * 4*cos(angb) + 3*k*sin(angb) = 4
     * <p/>
     * k = 4 / 3 * (1 - cos(angb)) / sin(angb)
     * <p/>
     * b) For ym coord we derive the same formula.
     * <p/>
     * Since this formula can generate "NaN" values for small
     * angles, we will derive a safer form that does not involve
     * dividing by very small values:
     * (1 - cos(angb)) / sin(angb) =
     * = (1 - cos(angb))*(1 + cos(angb)) / sin(angb)*(1 + cos(angb)) =
     * = (1 - cos(angb)^2) / sin(angb)*(1 + cos(angb)) =
     * = sin(angb)^2 / sin(angb)*(1 + cos(angb)) =
     * = sin(angb) / (1 + cos(angb))
     * <p/>
     * Function taken from java.awt.geom.ArcIterator.
     */
    private static double btan(double increment) {
        increment /= 2.0;
        return 4.0 / 3.0 * sin(increment) / (1.0 + cos(increment));
    }

    public Ellipse2D getSupportingEllipse() {
        return ellipse;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public double getAngleExtent() {
        return angleExtent;
    }

    /**
     * Returns true if the ellipse arc is direct, i.e. if the angle extent is
     * positive (or zero).
     */
    public boolean isDirect() {
        return angleExtent >= 0;
    }

    public boolean containsAngle(double angle) {
        return Angle2D.containsAngle(
                startAngle, startAngle + angleExtent, angle, angleExtent > 0);
    }

    // ====================================================================
    // methods from interface OrientedCurve2D

    /**
     * Returns the angle associated with the given position
     */
    public double getAngle(double position) {
        if (position < 0)
            position = 0;
        if (position > abs(angleExtent))
            position = abs(angleExtent);
        if (angleExtent < 0)
            position = -position;
        return Angle2D.formatAngle(startAngle + position);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jst.geom.geom2d.OrientedCurve2D#windingAngle(Point2D)
     */
    public double windingAngle(Point2D point) {
        Point2D p1 = point(0);
        Point2D p2 = point(abs(angleExtent));

        // compute angle of point with extreme points
        double angle1 = Angle2D.horizontalAngle(point, p1);
        double angle2 = Angle2D.horizontalAngle(point, p2);

        // test on which 'side' of the arc the point lie
        boolean b1 = (new StraightLine2D(p1, p2)).isInside(point);
        boolean b2 = ellipse.isInside(point);

        if (angleExtent > 0) {
            if (b1 || b2) { // inside of ellipse arc
                if (angle2 > angle1)
                    return angle2 - angle1;
                else
                    return 2 * PI - angle1 + angle2;
            } else { // outside of ellipse arc
                if (angle2 > angle1)
                    return angle2 - angle1 - 2 * PI;
                else
                    return angle2 - angle1;
            }
        } else {
            if (!b1 || b2) {
                if (angle1 > angle2)
                    return angle2 - angle1;
                else
                    return angle2 - angle1 - 2 * PI;
            } else {
                if (angle1 > angle2)
                    return angle2 - angle1 + 2 * PI;
                else
                    return angle2 - angle1;
            }
        }
    }

    public boolean isInside(Point2D p) {
        return signedDistance(p.x(), p.y()) < 0;
    }

    public double signedDistance(Point2D p) {
        return signedDistance(p.x(), p.y());
    }

    // ====================================================================
    // methods from interface SmoothCurve2D

    /*
     * (non-Javadoc)
     *
     * @see Shape2D#signedDistance(Point2D)
     */
    public double signedDistance(double x, double y) {
        boolean direct = angleExtent >= 0;

        double dist = distance(x, y);
        Point2D point = new Point2D(x, y);

        boolean inside = ellipse.isInside(point);
        if (inside)
            return angleExtent > 0 ? -dist : dist;

        Point2D p1 = point(startAngle);
        double endAngle = startAngle + angleExtent;
        Point2D p2 = point(endAngle);
        boolean onLeft = (new StraightLine2D(p1, p2)).isInside(point);

        if (direct && !onLeft)
            return dist;
        if (!direct && onLeft)
            return -dist;


        Ray2D ray = new Ray2D(p1, -sin(startAngle), cos(startAngle));
        boolean left1 = ray.isInside(point);
        if (direct && !left1)
            return dist;
        if (!direct && left1)
            return -dist;

        ray = new Ray2D(p2, -sin(endAngle), cos(endAngle));
        boolean left2 = ray.isInside(point);
        if (direct && !left2)
            return dist;
        if (!direct && left2)
            return -dist;

        if (direct)
            return -dist;
        else
            return dist;
    }

    public Vector2D tangent(double t) {
        // format between min and max admissible values
        t = min(max(0, t), abs(angleExtent));

        // compute tangent vector depending on position
        if (angleExtent < 0) {
            // need to invert vector for indirect arcs
            return ellipse.tangent(startAngle - t).times(-1);
        } else {
            return ellipse.tangent(startAngle + t);
        }
    }

    // ====================================================================
    // methods from interface ContinuousCurve2D

    /**
     * Returns the curvature of the ellipse arc.
     * Curvature is negative if the arc is indirect.
     */
    public double curvature(double t) {
        // convert position to angle
        if (angleExtent < 0)
            t = startAngle - t;
        else
            t = startAngle + t;
        double kappa = ellipse.curvature(t);
        return this.isDirect() ? kappa : -kappa;
    }

    /**
     * Returns false, as an ellipse arc is never closed.
     */
    public boolean isClosed() {
        return false;
    }

    @Override
    public Vector2D leftTangent(double t) {
        return null;
    }

    @Override
    public Vector2D rightTangent(double t) {
        return null;
    }

    // ====================================================================
    // methods from interface Curve2D

    /* (non-Javadoc)
     * @see ContinuousCurve2D#asPolyline(int)
     */
    public Polyline2D asPolyline(int n) {

        // compute increment value
        double dt = Math.abs(this.angleExtent) / n;

        // allocate array of points, and compute each value.
        // Computes also value for last point.
        Point2D[] points = new Point2D[n + 1];
        for (int i = 0; i < n + 1; i++)
            points[i] = this.point(i * dt);

        return new Polyline2D(points);
    }

    /**
     * Always returns 0
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
     * Always returns the absolute value of the angle extent
     */
    public double t1() {
        return abs(angleExtent);
    }

    /**
     * @deprecated replaced by t1() (since 0.11.1).
     */
    @Deprecated
    public double getT1() {
        return t1();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jst.geom.geom2d.Curve2D#point(double, Point2D)
     */
    public Point2D point(double t) {
        // check bounds
        t = max(t, 0);
        t = min(t, abs(angleExtent));

        // convert position to angle
        if (angleExtent < 0)
            t = startAngle - t;
        else
            t = startAngle + t;

        // return corresponding point
        return ellipse.point(t);
    }

    @Override
    public Point2D firstPoint() {
        return null;
    }

    @Override
    public Point2D lastPoint() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jst.geom.geom2d.Curve2D#position(Point2D)
     */
    public double position(Point2D point) {
        double angle = Angle2D.horizontalAngle(ellipse.center(), point);
        if (this.containsAngle(angle))
            if (angleExtent > 0)
                return Angle2D.formatAngle(angle - startAngle);
            else
                return Angle2D.formatAngle(startAngle - angle);

        // If the point is not contained in the arc, return NaN.
        return Double.NaN;
    }

    public double project(Point2D point) {
        double angle = ellipse.project(point);

        // Case of an angle contained in the ellipse arc
        if (this.containsAngle(angle)) {
            if (angleExtent > 0)
                return Angle2D.formatAngle(angle - startAngle);
            else
                return Angle2D.formatAngle(startAngle - angle);
        }

        // return either 0 or T1, depending on which extremity is closer.
        double d1 = this.firstPoint().distance(point);
        double d2 = this.lastPoint().distance(point);
        return d1 < d2 ? 0 : abs(angleExtent);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jst.geom.geom2d.Curve2D#intersections(com.jst.geom.geom2d.LinearShape2D)
     */
    public Collection<Point2D> intersections(LinearShape2D line) {

        // check point contained in it
        ArrayList<Point2D> array = new ArrayList<Point2D>();
        for (Point2D point : ellipse.intersections(line))
            if (contains(point))
                array.add(point);

        return array;
    }

    /**
     * Returns the ellipse arc which refers to the reversed parent ellipse, with
     * same start angle, and with opposite angle extent.
     */
    public EllipseArc2D reverse() {
        double newStart = Angle2D.formatAngle(startAngle + angleExtent);
        return new EllipseArc2D(ellipse, newStart, -angleExtent);
    }


    // ====================================================================
    // methods from interface Shape2D

    /**
     * Returns a new EllipseArc2D.
     */
    public EllipseArc2D subCurve(double t0, double t1) {
        // convert position to angle
        t0 = Angle2D.formatAngle(startAngle + t0);
        t1 = Angle2D.formatAngle(startAngle + t1);

        // check bounds of angles
        if (!Angle2D.containsAngle(startAngle, startAngle + angleExtent, t0,
                angleExtent > 0))
            t0 = startAngle;
        if (!Angle2D.containsAngle(startAngle, startAngle + angleExtent, t1,
                angleExtent > 0))
            t1 = angleExtent;

        // create new arc
        return new EllipseArc2D(ellipse, t0, t1, angleExtent > 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see Shape2D#distance(Point2D)
     */
    public double distance(Point2D point) {
        return distance(point.x(), point.y());
    }

    /*
     * (non-Javadoc)
     *
     * @see Shape2D#distance(double, double)
     */
    public double distance(double x, double y) {
        Point2D p = point(project(new Point2D(x, y)));
        return p.distance(x, y);
    }

    /**
     * Always return true: an ellipse arc is bounded by definition
     */
    public boolean isBounded() {
        return true;
    }

    /**
     * Returns false.
     */
    public boolean isEmpty() {
        return false;
    }


    /*
     * (non-Javadoc)
     *
     * @see java.awt.Shape#contains(double, double)
     */
    public boolean contains(double x, double y) {
        return distance(x, y) > Shape2D.ACCURACY;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.Shape#contains(Point2D)
     */
    public boolean contains(Point2D point) {
        return contains(point.x(), point.y());
    }

    public Path appendPath(Path path) {
        // number of curves to approximate the arc
        int nSeg = (int) ceil(abs(angleExtent) / (PI / 2));
        nSeg = min(nSeg, 4);

        // angular extent of each curve
        double ext = angleExtent / nSeg;

        // compute coefficient
        double k = btan(abs(ext));

        for (int i = 0; i < nSeg; i++) {
            // position of the two extremities
            double ti0 = abs(i * ext);
            double ti1 = abs((i + 1) * ext);

            // extremity points
            Point2D p1 = this.point(ti0);
            Point2D p2 = this.point(ti1);

            // tangent vectors, multiplied by appropriate coefficient
            Vector2D v1 = this.tangent(ti0).times(k);
            Vector2D v2 = this.tangent(ti1).times(k);

            // append a cubic curve to the path
            path.rCubicTo((float) (p1.x() + v1.x()), (float) (p1.y() + v1.y()),
                    (float) (p2.x() - v2.x()), (float) (p2.y() - v2.y()),
                    (float) p2.x(), (float) p2.y());
        }
        return path;
    }

    public Path getGeneralPath() {
        // create new path
        Path path = new Path();

        // move to the first point
        Point2D point = this.firstPoint();
        path.moveTo((float) point.x(), (float) point.y());

        // append the curve
        path = this.appendPath(path);

        // return the final path
        return path;
    }


    // ===================================================================
    // methods implementing GeometricObject2D interface

    /* (non-Javadoc)
     * @see GeometricObject2D#almostEquals(GeometricObject2D, double)
     */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof EllipseArc2D))
            return false;
        EllipseArc2D arc = (EllipseArc2D) obj;

        // test whether supporting ellipses have same support
        if (Math.abs(ellipse.xc - arc.ellipse.xc) > eps)
            return false;
        if (Math.abs(ellipse.yc - arc.ellipse.yc) > eps)
            return false;
        if (Math.abs(ellipse.r1 - arc.ellipse.r1) > eps)
            return false;
        if (Math.abs(ellipse.r2 - arc.ellipse.r2) > eps)
            return false;
        if (Math.abs(ellipse.theta - arc.ellipse.theta) > eps)
            return false;

        // test if angles are the same
        if (!Angle2D.equals(startAngle, arc.startAngle))
            return false;
        if (!Angle2D.equals(angleExtent, arc.angleExtent))
            return false;

        return true;
    }

    // ====================================================================
    // methods from interface Object

    @Override
    public String toString() {
        Point2D center = ellipse.center();
        return String.format(Locale.US,
                "EllipseArc2D(%7.2f,%7.2f,%7.2f,%7.2f,%7.5f,%7.5f,%7.5f)",
                center.x(), center.y(),
                ellipse.r1, ellipse.r2, ellipse.theta,
                startAngle, angleExtent);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof EllipseArc2D))
            return false;
        EllipseArc2D that = (EllipseArc2D) obj;

        // test whether supporting ellipses have same support
        if (!this.ellipse.equals(that.ellipse))
            return false;

        // test if angles are the same
        if (!EqualUtils.areEqual(startAngle, that.startAngle))
            return false;
        if (!EqualUtils.areEqual(angleExtent, that.angleExtent))
            return false;

        return true;
    }

    @Override
    public EllipseArc2D clone() {
        return new EllipseArc2D(ellipse, startAngle, angleExtent);
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
