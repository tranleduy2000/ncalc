/* File LineSegment2D.java 
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

import com.example.duy.calculator.geom2d.util.Angle2D;
import com.example.duy.calculator.geom2d.util.EqualUtils;
import com.example.duy.calculator.geom2d.GeometricObject2D;
import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.util.Shape2D;
import com.example.duy.calculator.geom2d.Vector2D;
import com.example.duy.calculator.geom2d.polygon.LinearCurve2D;

import java.util.Collection;


/**
 * Line segment, defined as the set of points located between the two end
 * points.
 */
public class LineSegment2D extends AbstractLine2D implements Cloneable {

    // ===================================================================
    // static constructors

    /**
     * Defines a new Edge with two extremities.
     */
    public LineSegment2D(Point2D point1, Point2D point2) {
        this(point1.x(), point1.y(), point2.x(), point2.y());
    }

    /**
     * Defines a new Edge with two extremities.
     */
    public LineSegment2D(double x1, double y1, double x2, double y2) {
        super(x1, y1, x2 - x1, y2 - y1);
    }

    /**
     * Static factory for creating a new line segment between two points.
     *
     * @deprecated since 0.11.1
     */
    @Deprecated
    public static LineSegment2D create(Point2D p1, Point2D p2) {
        return new LineSegment2D(p1, p2);
    }

    /**
     * Returns the straight line that is the median of the edge extremities.
     */
    public static StraightLine2D getMedian(LineSegment2D edge) {
        return new StraightLine2D(
                edge.x0 + edge.dx * .5,
                edge.y0 + edge.dy * .5,
                -edge.dy, edge.dx);
    }


    // ===================================================================
    // constructors

    /**
     * Returns angle between two edges sharing one vertex.
     */
    public static double getEdgeAngle(LineSegment2D edge1, LineSegment2D edge2) {
        double x0, y0, x1, y1, x2, y2;

        if (Math.abs(edge1.x0 - edge2.x0) < Shape2D.ACCURACY
                && Math.abs(edge1.y0 - edge2.y0) < Shape2D.ACCURACY) {
            x0 = edge1.x0;
            y0 = edge1.y0;
            x1 = edge1.x0 + edge1.dx;
            y1 = edge1.y0 + edge1.dy;
            x2 = edge2.x0 + edge2.dx;
            y2 = edge2.y0 + edge2.dy;
        } else if (Math.abs(edge1.x0 + edge1.dx - edge2.x0) < Shape2D.ACCURACY
                && Math.abs(edge1.y0 + edge1.dy - edge2.y0) < Shape2D.ACCURACY) {
            x0 = edge1.x0 + edge1.dx;
            y0 = edge1.y0 + edge1.dy;
            x1 = edge1.x0;
            y1 = edge1.y0;
            x2 = edge2.x0 + edge2.dx;
            y2 = edge2.y0 + edge2.dy;
        } else if (Math.abs(edge1.x0 + edge1.dx - edge2.x0 - edge2.dx) < Shape2D.ACCURACY
                && Math.abs(edge1.y0 + edge1.dy - edge2.y0 - edge2.dy) < Shape2D.ACCURACY) {
            x0 = edge1.x0 + edge1.dx;
            y0 = edge1.y0 + edge1.dy;
            x1 = edge1.x0;
            y1 = edge1.y0;
            x2 = edge2.x0;
            y2 = edge2.y0;
        } else if (Math.abs(edge1.x0 - edge2.x0 - edge2.dx) < Shape2D.ACCURACY
                && Math.abs(edge1.y0 - edge2.y0 - edge2.dy) < Shape2D.ACCURACY) {
            x0 = edge1.x0;
            y0 = edge1.y0;
            x1 = edge1.x0 + edge1.dx;
            y1 = edge1.y0 + edge1.dy;
            x2 = edge2.x0;
            y2 = edge2.y0;
        } else {// no common vertex -> return NaN
            return Double.NaN;
        }

        return Angle2D.angle(new Vector2D(x1 - x0, y1 - y0), new Vector2D(x2
                - x0, y2 - y0));
    }

    /**
     * Checks if two line segment intersect. Uses the Point2D.ccw() method,
     * which is based on Sedgewick algorithm.
     *
     * @param edge1 a line segment
     * @param edge2 a line segment
     * @return true if the 2 line segments intersect
     */
    public static boolean intersects(LineSegment2D edge1, LineSegment2D edge2) {
        Point2D e1p1 = edge1.firstPoint();
        Point2D e1p2 = edge1.lastPoint();
        Point2D e2p1 = edge2.firstPoint();
        Point2D e2p2 = edge2.lastPoint();

        boolean b1 = Point2D.ccw(e1p1, e1p2, e2p1)
                * Point2D.ccw(e1p1, e1p2, e2p2) <= 0;
        boolean b2 = Point2D.ccw(e2p1, e2p2, e1p1)
                * Point2D.ccw(e2p1, e2p2, e1p2) <= 0;
        return b1 && b2;
    }

    // ===================================================================
    // Methods specific to LineSegment2D

    /**
     * Returns the opposite vertex of the edge.
     *
     * @param point one of the vertices of the edge
     * @return the other vertex, or null if point is nor a vertex of the edge
     */
    public Point2D opposite(Point2D point) {
        if (point.equals(new Point2D(x0, y0)))
            return new Point2D(x0 + dx, y0 + dy);
        if (point.equals(new Point2D(x0 + dx, y0 + dy)))
            return new Point2D(x0, y0);
        return null;
    }

    /**
     * Returns the median of the edge, that is the locus of points located at
     * equal distance of each vertex.
     */
    public StraightLine2D getMedian() {
        // initial point is the middle of the edge -> x = x0+.5*dx
        // direction vector is the initial direction vector rotated by pi/2.
        return new StraightLine2D(x0 + dx * .5, y0 + dy * .5, -dy, dx);
    }


    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

    /**
     * Returns the length of the line segment.
     */
    public double length() {
        return Math.hypot(dx, dy);
    }

    /* (non-Javadoc)
     * @see CirculinearCurve2D#parallel(double)
     */
    public LineSegment2D parallel(double d) {
        double d2 = Math.hypot(dx, dy);
        if (Math.abs(d2) < Shape2D.ACCURACY)
            throw new DegeneratedLine2DException(
                    "Can not compute parallel of degnerated edge", this);
        d2 = d / d2;
        return new LineSegment2D(
                x0 + dy * d2, y0 - dx * d2,
                x0 + dx + dy * d2, y0 + dy - dx * d2);
    }


    // ===================================================================
    // Methods implementing the OrientedCurve2D interface

    @Override
    public double windingAngle(Point2D point) {
        return 0;
    }

    @Override
    public double signedDistance(Point2D point) {
        return 0;
    }

    /* (non-Javadoc)
         * @see OrientedCurve2D#signedDistance(Point2D)
         */
    @Override
    public double signedDistance(double x, double y) {
        Point2D proj = super.projectedPoint(x, y);
        if (contains(proj))
            return super.signedDistance(x, y);

        double d = this.distance(x, y);
        return super.signedDistance(x, y) > 0 ? d : -d;
    }

    @Override
    public boolean isInside(Point2D pt) {
        return false;
    }


    // ===================================================================
    // Methods implementing the Curve2D interface

    /**
     * Returns the first point of the edge.
     *
     * @return the first point of the edge
     */
    @Override
    public Point2D firstPoint() {
        return new Point2D(x0, y0);
    }

    /**
     * Returns the last point of the edge.
     *
     * @return the last point of the edge
     */
    @Override
    public Point2D lastPoint() {
        return new Point2D(x0 + dx, y0 + dy);
    }

    @Override
    public Collection<Point2D> intersections(LinearShape2D line) {
        return null;
    }

    /**
     * Returns the parameter of the first point of the edge, equals to 0.
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
     * Returns the parameter of the last point of the edge, equals to 1.
     */
    public double t1() {
        return 1.0;
    }

    /**
     * @deprecated replaced by t1() (since 0.11.1).
     */
    @Deprecated
    public double getT1() {
        return t1();
    }

    public Point2D point(double t) {
        t = Math.min(Math.max(t, 0), 1);
        return new Point2D(x0 + dx * t, y0 + dy * t);
    }

    /**
     * Returns the LineSegment which start from last point of this line segment,
     * and which ends at the fist point of this last segment.
     */
    public LineSegment2D reverse() {
        return new LineSegment2D(x0 + dx, y0 + dy, x0, y0);
    }

    // ===================================================================
    // Methods implementing the Shape2D interface

    /**
     * Returns true
     */
    public boolean isBounded() {
        return true;
    }

    public boolean contains(double xp, double yp) {
        if (!super.supportContains(xp, yp))
            return false;

        // compute position on the line
        double t = positionOnLine(xp, yp);

        if (t < -Shape2D.ACCURACY)
            return false;
        if (t - 1 > Shape2D.ACCURACY)
            return false;

        return true;
    }

    /**
     * Get the distance of the point (x, y) to this edge.
     */
    @Override
    public double distance(double x, double y) {
        // compute position on the line
        StraightLine2D line = this.supportingLine();
        double t = line.positionOnLine(x, y);

        // clamp with parameterization bounds of edge
        t = Math.max(Math.min(t, 1), 0);
        t = Math.min(t, 1);

        // compute position of projected point on the edge
        Point2D proj = line.point(t);

        // return distance to projected point
        return proj.distance(x, y);
    }


    // =================================
    // Methods implementing the Shape interface

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

    /**
     * Appends a line to the current path.
     *
     * @param path the path to modify
     * @return the modified path
     */
    public Path appendPath(Path path) {
        path.lineTo((float) (x0 + dx), (float) (y0 + dy));
        return path;
    }

    /**
     * deprecated
     */
    public Path getGeneralPath() {
        Path path = new Path();
        path.moveTo((float) x0, (float) y0);
        path.lineTo((float) (x0 + dx), (float) (y0 + dy));
        return path;
    }


    // ===================================================================
    // methods implementing the GeometricObject2D interface

    /*
     * (non-Javadoc)
     *
     * @see
     * GeometricObject2D#almostEquals(GeometricObject2D,
     * double)
     */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof LineSegment2D))
            return false;
        LineSegment2D edge = (LineSegment2D) obj;

        if (Math.abs(x0 - edge.x0) > eps)
            return false;
        if (Math.abs(y0 - edge.y0) > eps)
            return false;
        if (Math.abs(dx - edge.dx) > eps)
            return false;
        if (Math.abs(dy - edge.dy) > eps)
            return false;

        return true;
    }

    // ===================================================================
    // Methods implementing the Object interface

    @Override
    public String toString() {
        return "LineSegment2D[(" + x0 + "," + y0 + ")-(" + (x0 + dx)
                + "," + (y0 + dy) + ")]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof LineSegment2D))
            return false;
        LineSegment2D that = (LineSegment2D) obj;

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
    public LineSegment2D clone() {
        return new LineSegment2D(x0, y0, x0 + dx, y0 + dy);
    }
}
