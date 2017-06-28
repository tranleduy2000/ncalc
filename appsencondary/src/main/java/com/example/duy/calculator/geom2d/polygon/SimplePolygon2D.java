/* File SimplePolygon2D.java 
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

package com.example.duy.calculator.geom2d.polygon;

// Imports

import android.graphics.Path;

import com.example.duy.calculator.geom2d.util.AffineTransform2D;
import com.example.duy.calculator.geom2d.GeometricObject2D;
import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.line.LineSegment2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Represent a polygonal domain whose boundary is a single closed polyline.
 */
public class SimplePolygon2D implements Polygon2D {

    // ===================================================================
    // Static constructors

    /**
     * The inner ordered list of vertices. The last point is connected to the
     * first one.
     */
    protected ArrayList<Point2D> vertices;

    /**
     * Empty constructor: no vertex.
     */
    public SimplePolygon2D() {
        vertices = new ArrayList<Point2D>();
    }


    // ===================================================================
    // class variables

    /**
     * Constructor from an array of points
     *
     * @param vertices the vertices stored in an array of Point2D
     */
    public SimplePolygon2D(Point2D... vertices) {
        this.vertices = new ArrayList<Point2D>(vertices.length);
        Collections.addAll(this.vertices, vertices);
    }

    // ===================================================================
    // constructors

    /**
     * Constructor from two arrays, one for each coordinate.
     *
     * @param xcoords the x coordinate of each vertex
     * @param ycoords the y coordinate of each vertex
     */
    public SimplePolygon2D(double[] xcoords, double[] ycoords) {
        vertices = new ArrayList<Point2D>(xcoords.length);
        for (int i = 0; i < xcoords.length; i++)
            vertices.add(new Point2D(xcoords[i], ycoords[i]));
    }

    public SimplePolygon2D(Collection<? extends Point2D> points) {
        this.vertices = new ArrayList<Point2D>(points.size());
        this.vertices.addAll(points);
    }

    /**
     * Ensure the polygon has enough memory for storing the required number of
     * vertices.
     */
    public SimplePolygon2D(int nVertices) {
        vertices = new ArrayList<Point2D>(nVertices);
    }

    /**
     * Creates a simple polygon with the given linear ring representing its
     * boundary.
     *
     * @param ring the boundary of the polygon
     */
    public SimplePolygon2D(LinearRing2D ring) {
        this.vertices = new ArrayList<Point2D>(ring.vertexNumber());
        this.vertices.addAll(ring.vertices());
    }

    public SimplePolygon2D(SimplePolygon2D poly) {
        this.vertices = new ArrayList<Point2D>(poly.vertexNumber());
        this.vertices.addAll(poly.vertices);
    }

    /**
     * Static factory for creating a new SimplePolygon2D from a collection of
     * points.
     *
     * @since 0.8.1
     */
    public static SimplePolygon2D create(Collection<? extends Point2D> points) {
        return new SimplePolygon2D(points);
    }

    /**
     * Static factory for creating a new SimplePolygon2D from an array of
     * points.
     *
     * @since 0.8.1
     */
    public static SimplePolygon2D create(Point2D... points) {
        return new SimplePolygon2D(points);
    }

    // ===================================================================
    // methods specific to SimplePolygon2D

    /**
     * Computes the winding number of the polygon. Algorithm adapted from
     * http://www.geometryalgorithms.com/Archive/algorithm_0103/algorithm_0103.htm
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return the number of windings of the curve around the point
     */
    public int getWindingNumber(double x, double y) {
        return Polygons2D.windingNumber(vertices, new Point2D(x, y));
    }

    /**
     * Returns a simplified version of this polygon, by using Douglas-Peucker
     * algorithm.
     */
    public SimplePolygon2D simplify(double distMax) {
        return new SimplePolygon2D(Polylines2D.simplifyClosedPolyline(this.vertices, distMax));
    }

    /**
     * Returns the linear ring that composes the boundary of this polygon.
     *
     * @since 0.9.3
     */
    public LinearRing2D getRing() {
        return new LinearRing2D(this.vertices);
    }

    // ===================================================================
    // management of vertex list

    /**
     * Adds a point as the last vertex.
     */
    public void addVertex(Point2D point) {
        this.vertices.add(point);
    }

    /**
     * Adds a point as the last vertex.
     *
     * @since 0.9.3
     */
    public void insertVertex(int index, Point2D point) {
        this.vertices.add(index, point);
    }

    /**
     * Changes the position of the i-th vertex.
     */
    public void setVertex(int index, Point2D position) {
        this.vertices.set(index, position);
    }

    /**
     * Removes a vertex of the polygon.
     *
     * @param point the vertex to be removed.
     */
    public boolean removeVertex(Point2D point) {
        return this.vertices.remove(point);
    }

    /**
     * Removes a vertex of the polygon specified by its index.
     *
     * @since 0.9.3
     */
    public void removeVertex(int index) {
        this.vertices.remove(index);
    }

    /**
     * Removes all the vertices of the polygon.
     */
    public void clearVertices() {
        this.vertices.clear();
    }

    /**
     * Computes the index of the closest vertex to the input point.
     */
    public int closestVertexIndex(Point2D point) {
        double minDist = Double.POSITIVE_INFINITY;
        int index = -1;

        for (int i = 0; i < vertices.size(); i++) {
            double dist = vertices.get(i).distance(point);
            if (dist < minDist) {
                index = i;
                minDist = dist;
            }
        }

        return index;
    }

    /**
     * Computes the signed area of the polygon. Algorithm is taken from page: <a
     * href="http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/">
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/</a>. Signed are
     * is positive if polygon is oriented counter-clockwise, and negative
     * otherwise. Result is wrong if polygon is self-intersecting.
     *
     * @return the signed area of the polygon.
     */
    public double area() {
        return Polygons2D.computeArea(this);
    }

    /**
     * Computes the centroid (center of mass) of the polygon. Algorithm is taken
     * from page: <a
     * href="http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/">
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/</a>.
     *
     * @return the centroid of the polygon
     */
    public Point2D centroid() {
        return Polygons2D.computeCentroid(this);
    }

    /**
     * Returns the points of the polygon. The result is a pointer to the inner
     * collection of vertices.
     */
    public Collection<Point2D> vertices() {
        return vertices;
    }

    /**
     * Returns the i-th vertex of the polygon.
     *
     * @param i index of the vertex, between 0 and the number of vertices
     */
    public Point2D vertex(int i) {
        return vertices.get(i);
    }

    /**
     * Returns the number of vertices of the polygon.
     *
     * @since 0.6.3
     */
    public int vertexNumber() {
        return vertices.size();
    }

    /**
     * Returns the set of edges, as a collection of LineSegment2D.
     */
    public Collection<LineSegment2D> edges() {

        int nPoints = this.vertices.size();
        ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>(nPoints);

        if (nPoints == 0)
            return edges;

        for (int i = 0; i < nPoints - 1; i++)
            edges.add(new LineSegment2D(vertices.get(i), vertices.get(i + 1)));

        edges.add(new LineSegment2D(vertices.get(nPoints - 1), vertices.get(0)));

        return edges;
    }

    /**
     * Returns the number of edges. For a simple polygon, this equals the
     * number of vertices.
     */
    public int edgeNumber() {
        return vertices.size();
    }

    // ===================================================================
    // methods inherited from Domain2D interface

    /* (non-Javadoc)
     * @see Domain2D#asPolygon(int)
     */
    public Polygon2D asPolygon(int n) {
        return this;
    }

    /* (non-Javadoc)
     * @see Domain2D#contours()
     */
    public Collection<LinearRing2D> contours() {
        ArrayList<LinearRing2D> rings = new ArrayList<LinearRing2D>(1);
        rings.add(new LinearRing2D(vertices));
        return rings;
    }

    /**
     * Returns the polygon created by reversing the order of the vertices.
     */
    public SimplePolygon2D complement() {
        int nPoints = this.vertices.size();

        Point2D[] res = new Point2D[nPoints];

        if (nPoints > 0)
            res[0] = this.vertices.get(0);

        for (int i = 1; i < nPoints; i++) {
            res[i] = this.vertices.get(nPoints - i);
        }
        return new SimplePolygon2D(res);
    }


    /**
     * Returns true if polygon is oriented counter-clockwise, false otherwise.
     */
    public boolean isBounded() {
        return this.area() > 0;
    }

    public boolean isEmpty() {
        return vertices.size() == 0;
    }

    /**
     * Returns the new Polygon created by an affine transform of this polygon.
     * If the transform is not direct, the order of vertices is reversed.
     */
    public SimplePolygon2D transform(AffineTransform2D trans) {
        int nPoints = this.vertices.size();

        Point2D[] array = new Point2D[nPoints];
        Point2D[] res = new Point2D[nPoints];

        for (int i = 0; i < nPoints; i++) {
            array[i] = this.vertices.get(i);
            res[i] = new Point2D();
        }
        trans.transform(array, res);

        SimplePolygon2D poly = new SimplePolygon2D(res);
        if (!trans.isDirect())
            poly = poly.complement();

        return poly;
    }

    // ===================================================================
    // methods inherited from Shape interface

    /**
     * Returns true if the point p lies inside the polygon, with precision
     * given by Shape2D.ACCURACY.
     */
    public boolean contains(Point2D p) {
        return contains(p.x(), p.y());
    }

    /**
     * Returns true if the point (x, y) lies inside the polygon, with precision
     * given by Shape2D.ACCURACY.
     */
    public boolean contains(double x, double y) {
        double area = this.area();
        int winding = this.getWindingNumber(x, y);
        if (area > 0) {
            return winding == 1;
        } else {
            return winding == 0;
        }
    }

    /**
     * Returns a general path iterator.
     */
    public Path getGeneralPath() {
        Path path = new Path();
        if (vertices.size() < 2)
            return path;

        // move to first point
        Point2D point = vertices.get(0);
        path.moveTo((float) (point.x()), (float) (point.y()));

        // line to each point
        for (int i = 0; i < vertices.size(); i++) {
            point = vertices.get(i);
            path.lineTo((float) (point.x()), (float) (point.y()));
        }

        // close polygon
        point = vertices.get(0);
        path.lineTo((float) (point.x()), (float) (point.y()));
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

        if (!(obj instanceof SimplePolygon2D))
            return false;
        SimplePolygon2D polygon = (SimplePolygon2D) obj;

        int nv = this.vertexNumber();
        if (polygon.vertexNumber() != nv)
            return false;

        for (int i = 0; i < nv; i++) {
            if (!this.vertex(i).almostEquals(polygon.vertex(i), eps))
                return false;
        }

        return true;
    }

    // ===================================================================
    // methods inherited from Object interface

    /**
     * Tests if the two polygons are equal. Test first the number of vertices,
     * then the bounding boxes, then if each vertex of the polygon is contained
     * in the vertices array of this polygon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof SimplePolygon2D))
            return false;

        SimplePolygon2D polygon = (SimplePolygon2D) obj;

        int nv = this.vertexNumber();
        if (polygon.vertexNumber() != nv)
            return false;

        for (int i = 0; i < nv; i++) {
            if (!this.vertex(i).equals(polygon.vertex(i)))
                return false;
        }

        return true;
    }

    /**
     * @deprecated use copy constructor instead (0.11.2)
     */
    @Deprecated
    @Override
    public SimplePolygon2D clone() {
        ArrayList<Point2D> array = new ArrayList<Point2D>(vertices.size());
        for (Point2D point : vertices)
            array.add(point);
        return new SimplePolygon2D(array);
    }

}