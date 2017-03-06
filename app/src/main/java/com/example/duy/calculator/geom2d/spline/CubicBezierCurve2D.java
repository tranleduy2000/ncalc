/* File CubicBezierCurve2D.java 
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
 */

package com.example.duy.calculator.geom2d.spline;

import android.graphics.Path;

import com.example.duy.calculator.geom2d.GeometricObject2D;
import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.util.Shape2D;
import com.example.duy.calculator.geom2d.Vector2D;
import com.example.duy.calculator.geom2d.curve.AbstractSmoothCurve2D;
import com.example.duy.calculator.geom2d.curve.Curve2D;
import com.example.duy.calculator.geom2d.curve.SmoothCurve2D;
import com.example.duy.calculator.geom2d.line.LinearShape2D;
import com.example.duy.calculator.geom2d.line.StraightLine2D;
import com.example.duy.calculator.geom2d.polygon.Polyline2D;

import java.util.Collection;

/**
 * A cubic bezier curve, defined by 4 control points.
 * The curve passes through the first and the last control points.
 * The second and the third control points defines the tangents at the
 * extremities of the curve.
 * <p>
 * From javaGeom 0.8.0, this shape does not extends
 * java.awt.geom.CubicCurve2D.Double anymore
 *
 * @author Legland
 */
public class CubicBezierCurve2D extends AbstractSmoothCurve2D
        implements SmoothCurve2D {

    // ===================================================================
    // static methods

    protected double x1, y1;
    protected double ctrlx1, ctrly1;


    // ===================================================================
    // class variables
    protected double ctrlx2, ctrly2;
    protected double x2, y2;

    public CubicBezierCurve2D() {
        this(0, 0, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Build a new Bezier curve from its array of coefficients. The array must
     * have size 2*4.
     *
     * @param coefs the coefficients of the CubicBezierCurve2D.
     */
    public CubicBezierCurve2D(double[][] coefs) {
        this(coefs[0][0], coefs[1][0],
                coefs[0][0] + coefs[0][1] / 3.0,
                coefs[1][0] + coefs[1][1] / 3.0,
                coefs[0][0] + 2 * coefs[0][1] / 3.0 + coefs[0][2] / 3.0,
                coefs[1][0] + 2 * coefs[1][1] / 3.0 + coefs[1][2] / 3.0,
                coefs[0][0] + coefs[0][1] + coefs[0][2] + coefs[0][3],
                coefs[1][0] + coefs[1][1] + coefs[1][2] + coefs[1][3]);
    }


    // ===================================================================
    // constructors

    /**
     * Build a new Bezier curve of degree 3 by specifying position of extreme
     * points and position of 2 control points. The resulting curve is totally
     * contained in the convex polygon formed by the 4 control points.
     *
     * @param p1    first point
     * @param ctrl1 first control point
     * @param ctrl2 second control point
     * @param p2    last point
     */
    public CubicBezierCurve2D(Point2D p1, Point2D ctrl1, Point2D ctrl2,
                              Point2D p2) {
        this(p1.x(), p1.y(), ctrl1.x(), ctrl1.y(),
                ctrl2.x(), ctrl2.y(), p2.x(), p2.y());
    }

    /**
     * Build a new Bezier curve of degree 3 by specifying position and tangent
     * of first and last points.
     *
     * @param p1 first point
     * @param v1 first tangent vector
     * @param p2 position of last point
     * @param v2 last tangent vector
     */
    public CubicBezierCurve2D(Point2D p1, Vector2D v1, Point2D p2, Vector2D v2) {
        this(p1.x(), p1.y(),
                p1.x() + v1.x() / 3, p1.y() + v1.y() / 3,
                p2.x() - v2.x() / 3, p2.y() - v2.y() / 3,
                p2.x(), p2.y());
    }

    /**
     * Build a new Bezier curve of degree 3 by specifying position of extreme
     * points and position of 2 control points. The resulting curve is totally
     * containe in the convex polygon formed by the 4 control points.
     */
    public CubicBezierCurve2D(double x1, double y1, double xctrl1, double yctrl1,
                              double xctrl2, double yctrl2, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.ctrlx1 = xctrl1;
        this.ctrly1 = yctrl1;
        this.ctrlx2 = xctrl2;
        this.ctrly2 = yctrl2;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * @deprecated since 0.11.1
     */
    @Deprecated
    public final static CubicBezierCurve2D create(Point2D p1, Point2D c1,
                                                  Point2D c2, Point2D p2) {
        return new CubicBezierCurve2D(p1, c1, c2, p2);
    }

    /**
     * @deprecated since 0.11.1
     */
    @Deprecated
    public final static CubicBezierCurve2D create(Point2D p1, Vector2D v1,
                                                  Point2D p2, Vector2D v2) {
        return new CubicBezierCurve2D(p1, v1, p2, v2);
    }


    // ===================================================================
    // methods specific to CubicBezierCurve2D

    public Point2D getControl1() {
        return new Point2D(ctrlx1, ctrly1);
    }

    public Point2D getControl2() {
        return new Point2D(ctrlx2, ctrly2);
    }

    public Point2D getP1() {
        return this.firstPoint();
    }

    public Point2D getP2() {
        return this.lastPoint();
    }

    public Point2D getCtrlP1() {
        return this.getControl1();
    }

    public Point2D getCtrlP2() {
        return this.getControl2();
    }

    /**
     * Returns the matrix of parametric representation of the line. Result has
     * the form :
     * <p>
     * <code>[ x0  dx dx2 dx3] </code>
     * <p>
     * <code>[ y0  dy dy2 dy3] </code>
     * <p>
     * Coefficients are from the parametric equation : x(t) = x0 + dx*t +
     * dx2*t^2 + dx3*t^3 y(t) = y0 + dy*t + dy2*t^2 + dy3*t^3
     */
    public double[][] getParametric() {
        double[][] tab = new double[2][4];
        tab[0][0] = x1;
        tab[0][1] = 3 * ctrlx1 - 3 * x1;
        tab[0][2] = 3 * x1 - 6 * ctrlx1 + 3 * ctrlx2;
        tab[0][3] = x2 - 3 * ctrlx2 + 3 * ctrlx1 - x1;

        tab[1][0] = y1;
        tab[1][1] = 3 * ctrly1 - 3 * y1;
        tab[1][2] = 3 * y1 - 6 * ctrly1 + 3 * ctrly2;
        tab[1][3] = y2 - 3 * ctrly2 + 3 * ctrly1 - y1;
        return tab;
    }


    // ===================================================================
    // methods from OrientedCurve2D interface

    /**
     * Use winding angle of approximated polyline
     *
     */
    public double windingAngle(Point2D point) {
        return this.asPolyline(100).windingAngle(point);
    }

    /**
     * Returns true if the point is 'inside' the domain bounded by the curve.
     * Uses a polyline approximation.
     *
     * @param pt a point in the plane
     * @return true if the point is on the left side of the curve.
     */
    public boolean isInside(Point2D pt) {
        return this.asPolyline(100).isInside(pt);
    }

    public double signedDistance(Point2D point) {
        if (isInside(point))
            return -distance(point.x(), point.y());
        else
            return distance(point.x(), point.y());
    }
    public double signedDistance(double x, double y) {
        if (isInside(new Point2D(x, y)))
            return -distance(x, y);
        else
            return distance(x, y);
    }

    // ===================================================================
    // methods from SmoothCurve2D interface

    public Vector2D tangent(double t) {
        double[][] c = getParametric();
        double dx = c[0][1] + (2 * c[0][2] + 3 * c[0][3] * t) * t;
        double dy = c[1][1] + (2 * c[1][2] + 3 * c[1][3] * t) * t;
        return new Vector2D(dx, dy);
    }

    @Override
    public Vector2D normal(double t) {
        return null;
    }

    /**
     * Returns the curvature of the Curve.
     */
    public double curvature(double t) {
        double[][] c = getParametric();
        double xp = c[0][1] + (2 * c[0][2] + 3 * c[0][3] * t) * t;
        double yp = c[1][1] + (2 * c[1][2] + 3 * c[1][3] * t) * t;
        double xs = 2 * c[0][2] + 6 * c[0][3] * t;
        double ys = 2 * c[1][2] + 6 * c[1][3] * t;

        return (xp * ys - yp * xs) / Math.pow(Math.hypot(xp, yp), 3);
    }

    // ===================================================================
    // methods from ContinousCurve2D interface

    /**
     * The cubic curve is never closed.
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

    /* (non-Javadoc)
     * @see ContinuousCurve2D#asPolyline(int)
     */
    public Polyline2D asPolyline(int n) {

        // compute increment value
        double dt = 1.0 / n;

        // allocate array of points, and compute each value.
        // Computes also value for last point.
        Point2D[] points = new Point2D[n + 1];
        for (int i = 0; i < n + 1; i++)
            points[i] = this.point(i * dt);

        return new Polyline2D(points);
    }


    // ===================================================================
    // methods from Curve2D interface

    /**
     * returns 0, as Bezier curve is parameterized between 0 and 1.
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
     * Returns 1, as Bezier curve is parameterized between 0 and 1.
     */
    public double t1() {
        return 1;
    }

    /**
     * @deprecated replaced by t1() (since 0.11.1).
     */
    @Deprecated
    public double getT1() {
        return t1();
    }

    /**
     * Use approximation, by replacing Bezier curve with a polyline.
     *
     * @see Curve2D#intersections(LinearShape2D)
     */
    public Collection<Point2D> intersections(LinearShape2D line) {
        return this.asPolyline(100).intersections(line);
    }

    /**
     * @see Curve2D#point(double)
     */
    public Point2D point(double t) {
        t = Math.min(Math.max(t, 0), 1);
        double[][] c = getParametric();
        double x = c[0][0] + (c[0][1] + (c[0][2] + c[0][3] * t) * t) * t;
        double y = c[1][0] + (c[1][1] + (c[1][2] + c[1][3] * t) * t) * t;
        return new Point2D(x, y);
    }

    /**
     * Returns the first point of the curve.
     *
     * @return the first point of the curve
     */
    @Override
    public Point2D firstPoint() {
        return new Point2D(this.x1, this.y1);
    }

    /**
     * Returns the last point of the curve.
     *
     * @return the last point of the curve.
     */
    @Override
    public Point2D lastPoint() {
        return new Point2D(this.x2, this.y2);
    }

    /**
     * Computes position by approximating cubic spline with a polyline.
     */
    public double position(Point2D point) {
        int N = 100;
        return this.asPolyline(N).position(point) / (N);
    }

    /**
     * Computes position by approximating cubic spline with a polyline.
     */
    public double project(Point2D point) {
        int N = 100;
        return this.asPolyline(N).project(point) / (N);
    }

    /**
     * Returns the Bezier curve given by control points taken in reverse
     * order.
     */
    public CubicBezierCurve2D reverse() {
        return new CubicBezierCurve2D(
                this.lastPoint(), this.getControl1(),
                this.getControl2(), this.firstPoint());
    }

    /**
     * Computes portion of BezierCurve. If t1<t0, returns null.
     */
    public CubicBezierCurve2D subCurve(double t0, double t1) {
        t0 = Math.max(t0, 0);
        t1 = Math.min(t1, 1);
        if (t0 > t1)
            return null;

        double dt = t1 - t0;
        Vector2D v0 = tangent(t0).times(dt);
        Vector2D v1 = tangent(t1).times(dt);
        return new CubicBezierCurve2D(point(t0), v0, point(t1), v1);
    }

    // ===================================================================
    // methods from Shape2D interface

    /* (non-Javadoc)
     * @see Shape2D#contains(double, double)
     */
    public boolean contains(double x, double y) {
        return this.asPolyline(180).contains(x, y);
    }

    /* (non-Javadoc)
     * @see Shape2D#contains(Point2D)
     */
    public boolean contains(Point2D p) {
        return this.contains(p.x(), p.y());
    }

    /**
     * @see Shape2D#distance(Point2D)
     */
    public double distance(Point2D p) {
        return this.distance(p.x(), p.y());
    }

    /**
     * Compute approximated distance, computed on a polyline.
     *
     * @see Shape2D#distance(double, double)
     */
    public double distance(double x, double y) {
        return this.asPolyline(100).distance(x, y);
    }

    /**
     * Returns true, a cubic Bezier Curve is always bounded.
     */
    public boolean isBounded() {
        return true;
    }

    public boolean isEmpty() {
        return false;
    }


    public Path appendPath(Path path) {
        path.moveTo((float) x1, (float) y1);

        path.rCubicTo((float) ctrlx1, (float) ctrly1, (float) ctrlx2, (float) ctrly2, (float) x2, (float) y2);
        return path;
    }

    public Path getGeneralPath() {
        Path path = new Path();
        path.moveTo((float) x1, (float) y1);

        path.rCubicTo((float) ctrlx1, (float) ctrly1, (float) ctrlx2, (float) ctrly2, (float) x2, (float) y2);

        return path;
    }


    // ===================================================================
    // methods implementing the GeometricObject2D interface

    /* (non-Javadoc)
     * @see GeometricObject2D#almostEquals(GeometricObject2D, double)
     */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof CubicBezierCurve2D))
            return false;

        // class cast
        CubicBezierCurve2D bezier = (CubicBezierCurve2D) obj;

        // compare each field
        if (Math.abs(this.x1 - bezier.x1) > eps)
            return false;
        if (Math.abs(this.y1 - bezier.y1) > eps)
            return false;
        if (Math.abs(this.ctrlx1 - bezier.ctrlx1) > eps)
            return false;
        if (Math.abs(this.ctrly1 - bezier.ctrly1) > eps)
            return false;
        if (Math.abs(this.ctrlx2 - bezier.ctrlx2) > eps)
            return false;
        if (Math.abs(this.ctrly2 - bezier.ctrly2) > eps)
            return false;
        if (Math.abs(this.x2 - bezier.x2) > eps)
            return false;
        if (Math.abs(this.y2 - bezier.y2) > eps)
            return false;

        return true;
    }

    // ===================================================================
    // methods overriding the class Object

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof CubicBezierCurve2D))
            return false;

        // class cast
        CubicBezierCurve2D bezier = (CubicBezierCurve2D) obj;

        // compare each field
        if (Math.abs(this.x1 - bezier.x1) > Shape2D.ACCURACY) return false;
        if (Math.abs(this.y1 - bezier.y1) > Shape2D.ACCURACY) return false;
        if (Math.abs(this.ctrlx1 - bezier.ctrlx1) > Shape2D.ACCURACY) return false;
        if (Math.abs(this.ctrly1 - bezier.ctrly1) > Shape2D.ACCURACY) return false;
        if (Math.abs(this.ctrlx2 - bezier.ctrlx2) > Shape2D.ACCURACY) return false;
        if (Math.abs(this.ctrly2 - bezier.ctrly2) > Shape2D.ACCURACY) return false;
        if (Math.abs(this.x2 - bezier.x2) > Shape2D.ACCURACY) return false;
        if (Math.abs(this.y2 - bezier.y2) > Shape2D.ACCURACY) return false;

        return true;
    }

    /**
     * @deprecated not necessary to clone immutable objects (0.11.2)
     */
    @Deprecated
    @Override
    public CubicBezierCurve2D clone() {
        return new CubicBezierCurve2D(
                x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
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
