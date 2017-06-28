/* file : LinearRing2D.java
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
 * Created on 16 avr. 2007
 *
 */

package com.example.duy.calculator.geom2d.polygon;

import android.graphics.Path;

import com.example.duy.calculator.geom2d.GeometricObject2D;
import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.util.Shape2D;
import com.example.duy.calculator.geom2d.line.LineSegment2D;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 * A LinearRing2D is a Polyline2D whose last point is connected to the first one.
 * This is typically the boundary of a SimplePolygon2D.
 * </p>
 * <p>
 * The name 'LinearRing2D' was used for 2 reasons:
 * <ul><li>it is short</li> <li>it is consistent with the JTS name</li></ul>
 * </p>
 *
 * @author dlegland
 */
public class LinearRing2D extends LinearCurve2D {

    // ===================================================================
    // Static methods

    public LinearRing2D() {
        super();
    }

    public LinearRing2D(int n) {
        super(n);
    }


    // ===================================================================
    // Constructors

    public LinearRing2D(Point2D... vertices) {
        super(vertices);
    }

    public LinearRing2D(double[] xcoords, double[] ycoords) {
        super(xcoords, ycoords);
    }

    public LinearRing2D(Collection<? extends Point2D> points) {
        super(points);
    }

    public LinearRing2D(LinearCurve2D lineString) {
        super(lineString.vertices);
    }

    /**
     * Static factory for creating a new LinearRing2D from a collection of
     * points.
     *
     * @since 0.8.1
     */
    public static LinearRing2D create(Collection<? extends Point2D> points) {
        return new LinearRing2D(points);
    }

    /**
     * Static factory for creating a new LinearRing2D from an array of
     * points.
     *
     * @since 0.8.1
     */
    public static LinearRing2D create(Point2D... vertices) {
        return new LinearRing2D(vertices);
    }

    // ===================================================================
    // Methods specific to ClosedPolyline2D

    /**
     * Computes the signed area of the linear ring. Algorithm is taken from page:
     * <a href="http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/">
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/</a>. Signed are
     * is positive if polyline is oriented counter-clockwise, and negative
     * otherwise. Result is wrong if polyline is self-intersecting.
     *
     * @return the signed area of the polyline.
     */
    public double area() {
        // start from edge joining last and first vertices
        Point2D prev = this.vertices.get(this.vertices.size() - 1);

        // Iterate over all couples of adjacent vertices
        double area = 0;
        for (Point2D point : this.vertices) {
            // add area of elementary parallelogram
            area += prev.x() * point.y() - prev.y() * point.x();
            prev = point;
        }

        // divides by 2 to consider only elementary triangles
        return area /= 2;
    }

    // ===================================================================
    // Methods specific to LinearCurve2D

    /**
     * Returns a simplified version of this linear ring, by using
     * Douglas-Peucker algorithm.
     */
    public LinearRing2D simplify(double distMax) {
        return new LinearRing2D(Polylines2D.simplifyClosedPolyline(this.vertices, distMax));
    }

    /**
     * Returns an array of LineSegment2D. The number of edges is the same as
     * the number of vertices.
     *
     * @return the edges of the polyline
     */
    @Override
    public Collection<LineSegment2D> edges() {
        // create resulting array
        int n = vertices.size();
        ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>(n);

        // do not process empty polylines
        if (n < 2)
            return edges;

        // create one edge for each couple of vertices
        for (int i = 0; i < n - 1; i++)
            edges.add(new LineSegment2D(vertices.get(i), vertices.get(i + 1)));

        // add a supplementary edge at the end, but only if vertices differ
        Point2D p0 = vertices.get(0);
        Point2D pn = vertices.get(n - 1);

        // TODO: should not make the test...
        if (pn.distance(p0) > Shape2D.ACCURACY)
            edges.add(new LineSegment2D(pn, p0));

        // return resulting array
        return edges;
    }

    public int edgeNumber() {
        int n = vertices.size();
        if (n > 1)
            return n;
        return 0;
    }

    public LineSegment2D edge(int index) {
        int i2 = (index + 1) % vertices.size();
        return new LineSegment2D(vertices.get(index), vertices.get(i2));
    }

    /**
     * Returns the last edge of this linear ring. The last edge connects the
     * last vertex with the first one.
     */
    public LineSegment2D lastEdge() {
        int n = vertices.size();
        if (n < 2)
            return null;
        return new LineSegment2D(vertices.get(n - 1), vertices.get(0));
    }

    // ===================================================================
    // Methods inherited from interface OrientedCurve2D

    /*
     * (non-Javadoc)
     * 
     * @see com.example.duy.calculator.geom2d.OrientedCurve2D#windingAngle(Point2D)
     */
    public double windingAngle(Point2D point) {
        int wn = Polygons2D.windingNumber(this.vertices, point);
        return wn * 2 * Math.PI;
    }

    public boolean isInside(double x, double y) {
        return this.isInside(new Point2D(x, y));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.duy.calculator.geom2d.OrientedCurve2D#isInside(Point2D)
     */
    public boolean isInside(Point2D point) {
        // TODO: choose convention for points on the boundary
        if (this.contains(point))
            return true;

        double area = this.area();
        int winding = Polygons2D.windingNumber(this.vertices, point);
        if (area > 0) {
            return winding == 1;
        } else {
            return winding == 0;
        }

    }

    // ===================================================================
    // Methods inherited from interface ContinuousCurve2D

    /**
     * Returns true, by definition of linear ring.
     */
    public boolean isClosed() {
        return true;
    }

    @Override
    public LinearCurve2D asPolyline(int n) {
        return null;
    }

    // ===================================================================
    // Methods inherited from interface Curve2D

    /**
     * Returns point from position as double. Position t can be from 0 to n,
     * with n equal to the number of vertices of the linear ring.
     */
    public com.example.duy.calculator.geom2d.Point2D point(double t) {
        // format position to stay between limits
        double t0 = this.t0();
        double t1 = this.t1();
        t = Math.max(Math.min(t, t1), t0);

        int n = vertices.size();

        // index of vertex before point
        int ind0 = (int) Math.floor(t + Shape2D.ACCURACY);
        double tl = t - ind0;

        if (ind0 == n)
            ind0 = 0;
        Point2D p0 = vertices.get(ind0);

        // check if equal to a vertex
        if (Math.abs(t - ind0) < Shape2D.ACCURACY)
            return p0;

        // index of vertex after point
        int ind1 = ind0 + 1;
        if (ind1 == n)
            ind1 = 0;
        Point2D p1 = vertices.get(ind1);

        // position on line;
        double x0 = p0.x();
        double y0 = p0.y();
        double dx = p1.x() - x0;
        double dy = p1.y() - y0;

        return new Point2D(x0 + tl * dx, y0 + tl * dy);
    }

    /**
     * Returns the number of points in the linear ring.
     */
    public double t1() {
        return vertices.size();
    }

    /**
     * @deprecated replaced by t1() (since 0.11.1).
     */
    @Deprecated
    public double getT1() {
        return this.t1();
    }

    /**
     * Returns the first point, as this is the same as the last point.
     */
    @Override
    public Point2D lastPoint() {
        if (vertices.size() == 0)
            return null;
        return vertices.get(0);
    }

    /**
     * Returns the linear ring with same points taken in reverse order. The
     * first points is still the same. Points of reverse curve are the same as
     * the original curve (same references).
     */
    public LinearRing2D reverse() {
        Point2D[] points2 = new Point2D[vertices.size()];
        int n = vertices.size();
        if (n > 0)
            points2[0] = vertices.get(0);

        for (int i = 1; i < n; i++)
            points2[i] = vertices.get(n - i);

        return new LinearRing2D(points2);
    }

    /**
     * Return an instance of Polyline2D. If t1 is lower than t0, the returned
     * Polyline contains the origin of the curve.
     */
    public Polyline2D subCurve(double t0, double t1) {
        // code adapted from CurveSet2D

        Polyline2D res = new Polyline2D();

        // number of points in the polyline
        int indMax = this.vertexNumber();

        // format to ensure t is between T0 and T1
        t0 = Math.min(Math.max(t0, 0), indMax);
        t1 = Math.min(Math.max(t1, 0), indMax);

        // find curves index
        int ind0 = (int) Math.floor(t0 + Shape2D.ACCURACY);
        int ind1 = (int) Math.floor(t1 + Shape2D.ACCURACY);

        // need to subdivide only one line segment
        if (ind0 == ind1 && t0 < t1) {
            // extract limit points
            res.addVertex(this.point(t0));
            res.addVertex(this.point(t1));
            // return result
            return res;
        }

        // add the point corresponding to t0
        res.addVertex(this.point(t0));

        if (ind1 > ind0) {
            // add all the whole points between the 2 cuts
            for (int n = ind0 + 1; n <= ind1; n++)
                res.addVertex(vertices.get(n));
        } else {
            // add all points until the end of the set
            for (int n = ind0 + 1; n < indMax; n++)
                res.addVertex(vertices.get(n));

            // add all points from the beginning of the set
            for (int n = 0; n <= ind1; n++)
                res.addVertex(vertices.get(n));
        }

        // add the last point
        res.addVertex(this.point(t1));

        // return the curve set
        return res;
    }

    // ===================================================================
    // Methods inherited from interface Shape2D


    /*
     * (non-Javadoc)
     * 
     * @see com.example.duy.calculator.geom2d.ContinuousCurve2D#appendPath(Path)
     */
    public Path appendPath(Path path) {

        if (vertices.size() < 2)
            return path;

        // move to last first point of the curve (then a line will be drawn to
        // the first point)
        Point2D p0 = this.lastPoint();
        path.moveTo((float) p0.x(), (float) p0.y());

        // process each point
        for (Point2D point : vertices)
            path.lineTo((float) point.x(), (float) point.y());

        // close the path, even if the path is already at the right position
        path.close();

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

        if (!(obj instanceof LinearRing2D))
            return false;
        LinearRing2D ring = (LinearRing2D) obj;

        if (vertices.size() != ring.vertices.size())
            return false;

        for (int i = 0; i < vertices.size(); i++)
            if (!(vertices.get(i)).almostEquals(ring.vertices.get(i), eps))
                return false;
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LinearRing2D))
            return false;
        LinearRing2D ring = (LinearRing2D) object;

        if (vertices.size() != ring.vertices.size())
            return false;
        for (int i = 0; i < vertices.size(); i++)
            if (!(vertices.get(i)).equals(ring.vertices.get(i)))
                return false;
        return true;
    }


    // ===================================================================
    // methods implementing the Object interface

    /**
     * @deprecated use copy constructor instead (0.11.2)
     */
    @Deprecated
    @Override
    public LinearRing2D clone() {
        ArrayList<Point2D> array = new ArrayList<Point2D>(vertices.size());
        for (Point2D point : vertices)
            array.add(point);
        return new LinearRing2D(array);
    }
}
