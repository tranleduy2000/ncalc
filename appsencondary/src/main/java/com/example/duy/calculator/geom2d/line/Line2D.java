/* File Line2D.java 
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

import com.example.duy.calculator.geom2d.GeometricObject2D;
import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.Vector2D;
import com.example.duy.calculator.geom2d.curve.AbstractSmoothCurve2D;
import com.example.duy.calculator.geom2d.util.Shape2D;

import java.util.Collection;

public class Line2D extends AbstractSmoothCurve2D
        implements com.example.duy.calculator.geom2d.line.LinearShape2D, Cloneable {


    /**
     * The origin point.
     */
    public Point2D p1;

    /**
     * The destination point.
     */
    public Point2D p2;

    /**
     * Define a new Line2D with two extremities.
     */
    public Line2D(Point2D point1, Point2D point2) {
        this.p1 = point1;
        this.p2 = point2;
    }

    /**
     * Define a new Line2D with two extremities.
     */
    public Line2D(double x1, double y1, double x2, double y2) {
        p1 = new Point2D(x1, y1);
        p2 = new Point2D(x2, y2);
    }

    /**
     * Copy constructor.
     */
    public Line2D(Line2D line) {
        this(line.getPoint1(), line.getPoint2());
    }

    public Line2D(StraightLine2D mLine) {
        this(mLine.x0, mLine.y0, mLine.dx, mLine.dx);
    }

    /**
     * Checks if two line intersect. Uses the
     * {@link com.example.duy.calculator.geom2d.Point2D#ccw(Point2D, Point2D, Point2D) Point2D.ccw}
     * method, which is based on Sedgewick algorithm.
     *
     * @param line1 a Line2D object
     * @param line2 a Line2D object
     * @return true if the 2 lines intersect
     */
    public static boolean intersects(Line2D line1, Line2D line2) {
        Point2D e1p1 = line1.firstPoint();
        Point2D e1p2 = line1.lastPoint();
        Point2D e2p1 = line2.firstPoint();
        Point2D e2p2 = line2.lastPoint();

        boolean b1 = Point2D.ccw(e1p1, e1p2, e2p1)
                * Point2D.ccw(e1p1, e1p2, e2p2) <= 0;
        boolean b2 = Point2D.ccw(e2p1, e2p2, e1p1)
                * Point2D.ccw(e2p1, e2p2, e1p2) <= 0;
        return b1 && b2;
    }


    /**
     * Static factory for creating a new Line2D, starting from p1
     * and finishing at p2.
     *
     * @deprecated since 0.11.1
     */
    @Deprecated
    public static Line2D create(Point2D p1, Point2D p2) {
        return new Line2D(p1, p2);
    }

    public static Point2D getMidPoint(Line2D line2D) {
        return Point2D.midPoint(line2D.p1, line2D.p2);
    }

    /**
     * Parametric equation
     *
     * @param line2D - obj line
     */
    public static String getEquationParameter(Line2D line2D) {
        //extract
        Point2D point1 = line2D.getPoint1();
        Point2D point2 = line2D.getPoint2();

        Vector2D vecPhoenix = new Vector2D(point1, point2);
        double u1 = vecPhoenix.getX();
        double u2 = vecPhoenix.getY();
        double x0 = point1.getX();
        double y0 = point1.getY();

        return "x = " +
                x0 +
                "+" +
                u1 +
                "t" +
                "\n" +
                "y = " +
                y0 +
                "+" +
                u2 +
                "t";
    }

    /**
     * Formula: a(x - x0) + b(y - y0) = 0
     * vector n(a;b) and point p1(x0;y0)
     *
     * @return general equation
     */
    public static String getGeneralEquation(Line2D line2D) {
        Vector2D vecPhoenix = new Vector2D(line2D.getPoint1(), line2D.getPoint2());
        Vector2D vectorTangent = vecPhoenix.getOrthogonal();
        double a = vectorTangent.getX();
        double b = vectorTangent.getY();
        double x0 = line2D.p1.getX();
        double y0 = line2D.p1.getY();
        if (Math.pow(a, 2d) + Math.pow(b, 2d) < ACCURACY) {
            throw new RuntimeException("do not exist general equation");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(a);
        stringBuilder.append("x");
        stringBuilder.append("+");
        stringBuilder.append(b);
        stringBuilder.append("y");
        stringBuilder.append("+");
        stringBuilder.append(-a * x0 - b * y0);
        stringBuilder.append(" = 0");

        return stringBuilder.toString();
    }

    /**
     * @return Parameter equation
     */
    public String getEquationParameter() {
        Vector2D vecPhoenix = new Vector2D(p1, p2);
        double u1 = vecPhoenix.getX();
        double u2 = vecPhoenix.getY();
        double x0 = p1.getX();
        double y0 = p1.getY();

        return "x = " + x0 + "+" + u1 + "t" + "\n" +
                "y = " + y0 + "+" + u2 + "t";
    }

    /**
     * Return the first point of the edge. It corresponds to getPoint(0).
     *
     * @return the first point.
     */
    public Point2D getPoint1() {
        return p1;
    }

    public void setPoint1(Point2D point) {
        p1 = point;
    }

    public Point2D getPoint2() {
        return p2;
    }

    public void setPoint2(Point2D point) {
        p2 = point;
    }

    public double getX1() {
        return p1.x();
    }

    public double getY1() {
        return p1.y();
    }

    public double getX2() {
        return p2.x();
    }

    public double getY2() {
        return p2.y();
    }

    /**
     * Return the opposite vertex of the edge.
     *
     * @param point : one of the vertices of the edge
     * @return the other vertex
     */
    public Point2D getOtherPoint(Point2D point) {
        if (point.equals(p1))
            return p2;
        if (point.equals(p2))
            return p1;
        return null;
    }

    public boolean isColinear(com.example.duy.calculator.geom2d.line.LinearShape2D line) {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).isColinear(line);
    }

    /**
     * Test if the this object is parallel to the given one. This method is
     * overloaded to update parameters before computation.
     */
    public boolean isParallel(com.example.duy.calculator.geom2d.line.LinearShape2D line) {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).isParallel(line);
    }

    @Override
    public boolean containsProjection(Point2D point) {
        double pos = new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).project(point);
        return pos > (0 - Shape2D.ACCURACY) && pos < (1 + Shape2D.ACCURACY);
    }

    public Line2D parallel(double d) {
        double x0 = getX1();
        double y0 = getY1();
        double dx = getX2() - x0;
        double dy = getY2() - y0;
        double d2 = d / Math.hypot(dx, dy);
        return new Line2D(
                x0 + dy * d2, y0 - dx * d2,
                x0 + dx + dy * d2, y0 + dy - dx * d2);
    }

    public double length() {
        return p1.distance(p2);
    }

    public double length(double pos) {
        double dx = p2.x() - p1.x();
        double dy = p2.y() - p1.y();
        return pos * Math.hypot(dx, dy);
    }

    public double position(double length) {
        double dx = p2.x() - p1.x();
        double dy = p2.y() - p1.y();
        return length / Math.hypot(dx, dy);
    }

    public double[][] parametric() {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).parametric();
    }

    // ===================================================================
    // methods implementing the LinearShape2D interface

    public double[] cartesianEquation() {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).cartesianEquation();
    }

    public double[] polarCoefficients() {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).polarCoefficients();
    }

    public double[] polarCoefficientsSigned() {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).polarCoefficientsSigned();
    }

    public double horizontalAngle() {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).horizontalAngle();
    }

    /*
     * @see LinearShape2D#intersection(LinearShape2D)
     */
    public Point2D intersection(com.example.duy.calculator.geom2d.line.LinearShape2D line) {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).intersection(line);
    }

    /*
     * @see LinearShape2D#origin()
     */
    public Point2D origin() {
        return p1;
    }

    /*
     * @see LinearShape2D#supportingLine()
     */
    public com.example.duy.calculator.geom2d.line.StraightLine2D supportingLine() {
        return new com.example.duy.calculator.geom2d.line.StraightLine2D(p1, p2);
    }

    /*
     * @see LinearShape2D#direction()
     */
    public Vector2D direction() {
        return new Vector2D(p1, p2);
    }

    public double signedDistance(Point2D p) {
        return signedDistance(p.x(), p.y());
    }

    public double signedDistance(double x, double y) {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).signedDistance(x, y);
    }

    /**
     * Returns false.
     *
     * @see com.example.duy.calculator.geom2d.curve.ContinuousCurve2D#isClosed()
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

    /**
     * Returns the distance of the point <code>p</code> to this edge.
     */
    public double distance(Point2D p) {
        return distance(p.x(), p.y());
    }
    // ===================================================================
    // methods inherited from SmoothCurve2D interface

    /**
     * Returns the distance of the point (x, y) to this edge.
     */
    public double distance(double x, double y) {
        // project the point on the support line
        com.example.duy.calculator.geom2d.line.StraightLine2D support = new com.example.duy.calculator.geom2d.line.StraightLine2D(p1, p2);
        Point2D proj = support.projectedPoint(x, y);

        // if this line contains the projection, return orthogonal distance
        if (contains(proj))
            return proj.distance(x, y);

        // return distance to closest extremity
        double d1 = Math.hypot(p1.x() - x, p1.y() - y);
        double d2 = Math.hypot(p2.x() - x, p2.y() - y);
        return Math.min(d1, d2);
    }

    /**
     * Creates a straight line parallel to this object, and passing through
     * the given point.
     *
     * @param point the point to go through
     * @return the parallel through the point
     */
    public com.example.duy.calculator.geom2d.line.StraightLine2D parallel(Point2D point) {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).parallel(point);
    }

    /**
     * Creates a straight line perpendicular to this object, and passing
     * through the given point.
     *
     * @param point the point to go through
     * @return the perpendicular through point
     */
    public com.example.duy.calculator.geom2d.line.StraightLine2D perpendicular(Point2D point) {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).perpendicular(point);
    }

    // ===================================================================
    // methods inherited from OrientedCurve2D interface

    public Vector2D tangent(double t) {
        return new Vector2D(p1, p2);
    }

    /**
     * Returns 0 as every linear shape.
     */
    public double curvature(double t) {
        return 0.0;
    }

    @Override
    public com.example.duy.calculator.geom2d.polygon.LinearCurve2D asPolyline(int n) {
        return null;
    }

    public double windingAngle(Point2D point) {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).windingAngle(point);
    }

    public boolean isInside(Point2D point) {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).signedDistance(point) < 0;
    }

    /**
     * Returns 0.
     */
    public double t0() {
        return 0.0;
    }

    /**
     * @deprecated replaced by t0() (since 0.11.1).
     */
    @Deprecated
    public double getT0() {
        return t0();
    }

    /**
     * Returns 1.
     */
    public double t1() {
        return 1.0;
    }

    @Deprecated
    public double getT1() {
        return t1();
    }

    public Point2D point(double t) {
        t = Math.min(Math.max(t, 0), 1);
        double x = p1.x() * (1 - t) + p2.x() * t;
        double y = p1.y() * (1 - t) + p2.y() * t;
        return new Point2D(x, y);
    }

    /**
     * Get the first point of the curve.
     *
     * @return the first point of the curve
     */
    @Override
    public Point2D firstPoint() {
        return p1;
    }

    /**
     * Get the last point of the curve.
     *
     * @return the last point of the curve.
     */
    @Override
    public Point2D lastPoint() {
        return p2;
    }

    /**
     * Returns the position of the point on the line. If point belongs to the
     * line, this position is defined by the ratio:
     * <p>
     * <code> t = (xp - x0)/dx <\code>, or equivalently :<p>
     * <code> t = (yp - y0)/dy <\code>.<p>
     * If point does not belong to edge, return Double.NaN. The current implementation
     * uses the direction with the biggest derivative, in order to avoid divisions
     * by zero.
     */
    public double position(Point2D point) {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).position(point);
    }

    public double project(Point2D point) {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).project(point);
    }

    /**
     * Returns a new Line2D, which is the portion of the line delimited by
     * parameters t0 and t1.
     */
    public Line2D subCurve(double t0, double t1) {
        if (t0 > t1)
            return null;
        t0 = Math.max(t0, t0());
        t1 = Math.min(t1, t1());
        return new Line2D(this.point(t0), this.point(t1));
    }

    /*
     * @see Curve2D#intersections(LinearShape2D)
     */
    public Collection<Point2D> intersections(com.example.duy.calculator.geom2d.line.LinearShape2D line) {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).intersections(line);
    }

    /**
     * Returns true if the point (x, y) lies on the line, with precision given
     * by Shape2D.ACCURACY.
     */
    public boolean contains(double x, double y) {
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(p1, p2).contains(x, y);
    }

    /**
     * Returns true if the point p lies on the line, with precision given by
     * Shape2D.ACCURACY.
     */
    public boolean contains(Point2D p) {
        return contains(p.x(), p.y());
    }

    /**
     * Returns true
     */
    public boolean isBounded() {
        return true;
    }

    /**
     * Returns false
     */
    public boolean isEmpty() {
        return false;
    }

    public Path getGeneralPath() {
        Path path = new Path();
        path.moveTo((float) p1.x(), (float) p1.y());
        path.lineTo((float) p2.x(), (float) p2.y());
        return path;
    }

    public Path appendPath(Path path) {
        path.lineTo((float) p2.x(), (float) p2.y());
        return path;
    }


    public boolean almostEquals(GeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        // check class
        if (!(obj instanceof Line2D))
            return false;

        // cast class, and compare members
        Line2D edge = (Line2D) obj;
        return p1.almostEquals(edge.p1, eps) && p2.almostEquals(edge.p2, eps);
    }

    /**
     * get mid point of the line
     */
    public Point2D getMidPoint() {
        return Point2D.midPoint(p1, p2);
    }

    @Override
    public String toString() {
        return "Line2D(" + p1 + ")-(" + p2 + ")";
    }

    /**
     * Two Line2D are equals if the share the two same points,
     * in the same order.
     *
     * @param obj the edge to compare to.
     * @return true if extremities of both edges are the same.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Line2D))
            return false;

        // cast class, and compare members
        Line2D edge = (Line2D) obj;
        return p1.equals(edge.p1) && p2.equals(edge.p2);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + p1.hashCode();
        hash = hash * 31 + p2.hashCode();
        return hash;
    }

    @Override
    public Line2D clone() {
        return new Line2D(p1, p2);
    }

    /**
     * Formular a(x - x0) + b(y - y0) = 0
     * @return general equation
     */
    public String getGeneralEquation() throws Exception {
        Vector2D vecPhoenix = new Vector2D(p1, p2);
        Vector2D vectorTangent = vecPhoenix.getOrthogonal();
        double a = vectorTangent.getX();
        double b = vectorTangent.getY();
        double x0 = p1.getX();
        double y0 = p1.getY();
        if (Math.pow(a, 2d) + Math.pow(b, 2d) < ACCURACY) {
            throw new Exception("does not exist general equation");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(a);
        stringBuilder.append("x");
        stringBuilder.append("+");
        stringBuilder.append(b);
        stringBuilder.append("y");
        stringBuilder.append("+");
        stringBuilder.append(-a * x0 - b * y0);
        stringBuilder.append(" = 0");

        return stringBuilder.toString();
    }
}
