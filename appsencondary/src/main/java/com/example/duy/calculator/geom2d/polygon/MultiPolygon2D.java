package com.example.duy.calculator.geom2d.polygon;

import com.example.duy.calculator.geom2d.GeometricObject2D;
import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.line.LineSegment2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * A polygonal domain whose boundary is composed of several disjoint continuous
 * LinearRing2D.
 *
 * @author dlegland
 */
public class MultiPolygon2D implements Polygon2D {

    // ===================================================================
    // Static constructors

    ArrayList<LinearRing2D> rings = new ArrayList<LinearRing2D>(1);


    // ===================================================================
    // class members

    /**
     * Ensures the inner buffer has enough capacity for storing the required
     * number of rings.
     */
    public MultiPolygon2D(int nRings) {
        this.rings.ensureCapacity(nRings);
    }


    // ===================================================================
    // Constructors

    public MultiPolygon2D(LinearRing2D... rings) {
        Collections.addAll(this.rings, rings);
    }


    public MultiPolygon2D(Collection<LinearRing2D> lines) {
        rings.addAll(lines);
    }

    public static MultiPolygon2D create(Collection<LinearRing2D> rings) {
        return new MultiPolygon2D(rings);
    }

    public static MultiPolygon2D create(LinearRing2D... rings) {
        return new MultiPolygon2D(rings);
    }

    // ===================================================================
    // Management of rings

    public void addRing(LinearRing2D ring) {
        rings.add(ring);
    }

    public void insertRing(int index, LinearRing2D ring) {
        rings.add(index, ring);
    }

    public void removeRing(LinearRing2D ring) {
        rings.remove(ring);
    }

    public void clearRings() {
        rings.clear();
    }

    public LinearRing2D getRing(int index) {
        return rings.get(index);
    }

    public void setRing(int index, LinearRing2D ring) {
        rings.set(index, ring);
    }

    public int ringNumber() {
        return rings.size();
    }


    // ===================================================================
    // methods implementing the Polygon2D interface

    /**
     * Computes the signed area of the polygon.
     *
     * @return the signed area of the polygon.
     * @since 0.9.1
     */
    public double area() {
        return Polygons2D.computeArea(this);
    }

    /**
     * Computes the centroid (center of mass) of the polygon.
     *
     * @return the centroid of the polygon
     * @since 0.9.1
     */
    public Point2D centroid() {
        return Polygons2D.computeCentroid(this);
    }

    public Collection<LineSegment2D> edges() {
        int nEdges = edgeNumber();
        ArrayList<LineSegment2D> edges = new ArrayList<LineSegment2D>(nEdges);
        for (LinearRing2D ring : rings)
            edges.addAll(ring.edges());
        return edges;
    }

    public int edgeNumber() {
        int count = 0;
        for (LinearRing2D ring : rings)
            count += ring.vertexNumber();
        return count;
    }

    public Collection<Point2D> vertices() {
        int nv = vertexNumber();
        ArrayList<Point2D> points = new ArrayList<Point2D>(nv);
        for (LinearRing2D ring : rings)
            points.addAll(ring.vertices());
        return points;
    }

    /**
     * Returns the i-th vertex of the polygon.
     *
     * @param i index of the vertex, between 0 and the number of vertices minus one
     */
    public Point2D vertex(int i) {
        int count = 0;
        LinearRing2D boundary = null;

        for (LinearRing2D ring : rings) {
            int nv = ring.vertexNumber();
            if (count + nv > i) {
                boundary = ring;
                break;
            }
            count += nv;
        }

        if (boundary == null)
            throw new IndexOutOfBoundsException();

        return boundary.vertex(i - count);
    }

    /**
     * Sets the position of the i-th vertex of this polygon.
     *
     * @param i index of the vertex, between 0 and the number of vertices
     */
    public void setVertex(int i, Point2D point) {
        int count = 0;
        LinearRing2D boundary = null;

        for (LinearRing2D ring : rings) {
            int nv = ring.vertexNumber();
            if (count + nv > i) {
                boundary = ring;
                break;
            }
            count += nv;
        }

        if (boundary == null)
            throw new IndexOutOfBoundsException();

        boundary.setVertex(i - count, point);
    }

    /**
     * Adds a vertex at the end of the last ring of this polygon.
     *
     * @throws RuntimeException if this MultiPolygon does not contain any ring
     */
    public void addVertex(Point2D position) {
        // get the last ring
        if (rings.size() == 0) {
            throw new RuntimeException("Can not add a vertex to a multipolygon with no ring");
        }
        LinearRing2D ring = rings.get(rings.size() - 1);
        ring.addVertex(position);
    }

    /**
     * Inserts a vertex at the given position
     *
     * @throws RuntimeException         if this polygon has no ring
     * @throws IllegalArgumentException if index is not smaller than vertex number
     */
    public void insertVertex(int index, Point2D point) {
        // check number of rings
        if (rings.size() == 0) {
            throw new RuntimeException("Can not add a vertex to a multipolygon with no ring");
        }

        // Check number of vertices
        int nv = this.vertexNumber();
        if (nv <= index) {
            throw new IllegalArgumentException("Can not insert vertex at position " +
                    index + " (max is " + nv + ")");
        }

        // Find the ring that correspond to index
        int count = 0;
        LinearRing2D boundary = null;

        for (LinearRing2D ring : rings) {
            nv = ring.vertexNumber();
            if (count + nv > index) {
                boundary = ring;
                break;
            }
            count += nv;
        }

        if (boundary == null)
            throw new IndexOutOfBoundsException();

        boundary.insertVertex(index - count, point);
    }

    /**
     * Returns the i-th vertex of the polygon.
     *
     * @param i index of the vertex, between 0 and the number of vertices minus one
     */
    public void removeVertex(int i) {
        int count = 0;
        LinearRing2D boundary = null;

        for (LinearRing2D ring : rings) {
            int nv = ring.vertexNumber();
            if (count + nv > i) {
                boundary = ring;
                break;
            }
            count += nv;
        }

        if (boundary == null)
            throw new IndexOutOfBoundsException();

        boundary.removeVertex(i - count);
    }

    /**
     * Returns the total number of vertices in this polygon.
     * The total number is computed as the sum of vertex number in each ring
     * of the polygon.
     */
    public int vertexNumber() {
        int count = 0;
        for (LinearRing2D ring : rings)
            count += ring.vertexNumber();
        return count;
    }

    /**
     * Computes the index of the closest vertex to the input point.
     */
    public int closestVertexIndex(Point2D point) {
        double minDist = Double.POSITIVE_INFINITY;
        int index = -1;

        int i = 0;
        for (LinearRing2D ring : this.rings) {
            for (Point2D vertex : ring.vertices()) {
                double dist = vertex.distance(point);
                if (dist < minDist) {
                    index = i;
                    minDist = dist;
                }
                i++;
            }

        }

        return index;
    }


    public Polygon2D asPolygon(int n) {
        return this;
    }


    /* (non-Javadoc)
     * @see Domain2D#contours()
     */
    public Collection<LinearRing2D> contours() {
        return Collections.unmodifiableList(rings);
    }

    public Polygon2D complement() {
        // allocate memory for array of reversed rings
        ArrayList<LinearRing2D> reverseLines =
                new ArrayList<LinearRing2D>(rings.size());

        // reverse each ring
        for (LinearRing2D ring : rings)
            reverseLines.add(ring.reverse());

        // create the new MultiMpolygon2D with set of reversed rings
        return new MultiPolygon2D(reverseLines);
    }


    /**
     * The MultiPolygon2D is empty either if it contains no ring, or if all
     * rings are empty.
     */
    public boolean isEmpty() {
        // return true if at least one ring is not empty
        for (LinearRing2D ring : rings)
            if (!ring.isEmpty())
                return false;
        return true;
    }


    public boolean contains(Point2D point) {
        double angle = 0;
        for (LinearRing2D ring : this.rings)
            angle += ring.windingAngle(point);

        double area = this.area();
        if (area > 0) {
            return angle > Math.PI;
        } else {
            return angle > -Math.PI;
        }
    }

    public boolean contains(double x, double y) {
        return this.contains(new com.example.duy.calculator.geom2d.Point2D(x, y));
    }

    // ===================================================================
    // methods implementing the GeometricObject2D interface

    /* (non-Javadoc)
     * @see GeometricObject2D#almostEquals(GeometricObject2D, double)
     */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof MultiPolygon2D))
            return false;
        MultiPolygon2D polygon = (MultiPolygon2D) obj;

        // check if the two objects have same number of rings
        if (polygon.rings.size() != this.rings.size())
            return false;

        // check each couple of ring
        for (int i = 0; i < rings.size(); i++)
            if (!this.rings.get(i).almostEquals(polygon.rings.get(i), eps))
                return false;

        return true;
    }

    // ===================================================================
    // methods overriding the Object class

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof MultiPolygon2D))
            return false;

        // check if the two objects have same number of rings
        MultiPolygon2D polygon = (MultiPolygon2D) obj;
        if (polygon.rings.size() != this.rings.size())
            return false;

        // check each couple of ring
        for (int i = 0; i < rings.size(); i++)
            if (!this.rings.get(i).equals(polygon.rings.get(i)))
                return false;

        return true;
    }

    /**
     * @deprecated use copy constructor instead (0.11.2)
     */
    @Deprecated
    public MultiPolygon2D clone() {
        // allocate memory for new ring array
        ArrayList<LinearRing2D> array = new ArrayList<LinearRing2D>(rings.size());

        // clone each ring
        for (LinearRing2D ring : rings)
            array.add(new LinearRing2D(ring));

        // create a new polygon with cloned rings
        return new MultiPolygon2D(array);
    }
}
