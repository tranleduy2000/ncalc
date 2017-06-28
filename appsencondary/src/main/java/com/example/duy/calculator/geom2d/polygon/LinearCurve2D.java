/**
 *
 */
package com.example.duy.calculator.geom2d.polygon;

import android.graphics.Path;

import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.util.Shape2D;
import com.example.duy.calculator.geom2d.Vector2D;
import com.example.duy.calculator.geom2d.curve.ContinuousCurve2D;
import com.example.duy.calculator.geom2d.line.LinearShape2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Abstract class that is the base implementation of Polyline2D and LinearRing2D.
 *
 * @author dlegland
 */
public abstract class LinearCurve2D implements ContinuousCurve2D {

    // ===================================================================
    // class variables

    public ArrayList<Point2D> vertices;


    // ===================================================================
    // Contructors

    public LinearCurve2D() {
        this.vertices = new ArrayList<Point2D>();
    }

    /**
     * Creates a new linear curve by allocating enough memory for the
     * specified number of vertices.
     *
     * @param nVertices
     */
    public LinearCurve2D(int nVertices) {
        this.vertices = new ArrayList<Point2D>(nVertices);
    }

    public LinearCurve2D(Point2D... vertices) {
        this.vertices = new ArrayList<Point2D>(vertices.length);
        Collections.addAll(this.vertices, vertices);
    }

    public LinearCurve2D(Collection<? extends Point2D> vertices) {
        this.vertices = new ArrayList<Point2D>(vertices.size());
        this.vertices.addAll(vertices);
    }

    public LinearCurve2D(double[] xcoords, double[] ycoords) {
        this.vertices = new ArrayList<Point2D>(xcoords.length);
        int n = xcoords.length;
        this.vertices.ensureCapacity(n);
        for (int i = 0; i < n; i++)
            vertices.add(new Point2D(xcoords[i], ycoords[i]));
    }


    // ===================================================================
    // Methods specific to LinearCurve2D

    /**
     * Returns a simplified version of this linear curve. Sub classes may
     * override this method to return a more specialized type.
     */
    public abstract LinearCurve2D simplify(double distMax);

    /**
     * Returns an iterator on the collection of points.
     */
    public Iterator<Point2D> vertexIterator() {
        return vertices.iterator();
    }

    /**
     * Returns the collection of vertices as an array of Point2D.
     *
     * @return an array of Point2D
     */
    public Point2D[] vertexArray() {
        return this.vertices.toArray(new Point2D[]{});
    }

    /**
     * Adds a vertex at the end of this polyline.
     *
     * @return true if the vertex was correctly added
     * @since 0.9.3
     */
    public boolean addVertex(Point2D vertex) {
        return vertices.add(vertex);
    }

    /**
     * Inserts a vertex at a given position in the polyline.
     *
     * @since 0.9.3
     */
    public void insertVertex(int index, Point2D vertex) {
        vertices.add(index, vertex);
    }

    /**
     * Removes the first instance of the given vertex from this polyline.
     *
     * @param vertex the position of the vertex to remove
     * @return true if the vertex was actually removed
     * @since 0.9.3
     */
    public boolean removeVertex(Point2D vertex) {
        return vertices.remove(vertex);
    }

    /**
     * Removes the vertex specified by the index.
     *
     * @param index the index of the vertex to remove
     * @return the position of the vertex removed from the polyline
     * @since 0.9.3
     */
    public Point2D removeVertex(int index) {
        return this.vertices.remove(index);
    }

    /**
     * Changes the position of the i-th vertex.
     *
     * @since 0.9.3
     */
    public void setVertex(int index, Point2D position) {
        this.vertices.set(index, position);
    }

    public void clearVertices() {
        vertices.clear();
    }

    /**
     * Returns the vertices of the polyline.
     */
    public Collection<Point2D> vertices() {
        return vertices;
    }

    /**
     * Returns the i-th vertex of the polyline.
     *
     * @param i index of the vertex, between 0 and the number of vertices
     */
    public Point2D vertex(int i) {
        return vertices.get(i);
    }

    /**
     * Returns the number of vertices.
     *
     * @return the number of vertices
     */
    public int vertexNumber() {
        return vertices.size();
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

    // ===================================================================
    // Management of edges

    /**
     * Returns the i-th edge of this linear curve.
     */
    public abstract com.example.duy.calculator.geom2d.line.LineSegment2D edge(int index);

    /**
     * Returns the number of edges of this linear curve.
     */
    public abstract int edgeNumber();

    /**
     * Returns a collection of LineSegment2D that represent the individual
     * edges of this linear curve.
     *
     * @return the edges of the polyline
     */
    public abstract Collection<com.example.duy.calculator.geom2d.line.LineSegment2D> edges();

    public com.example.duy.calculator.geom2d.line.LineSegment2D firstEdge() {
        if (vertices.size() < 2)
            return null;
        return new com.example.duy.calculator.geom2d.line.LineSegment2D(vertices.get(0), vertices.get(1));
    }

    public abstract com.example.duy.calculator.geom2d.line.LineSegment2D lastEdge();


    // ===================================================================
    // methods implementing the CirculinearCurve2D interface

    /* (non-Javadoc)
     * @see CirculinearCurve2D#length()
     */
    public double length() {
        double sum = 0;
        for (com.example.duy.calculator.geom2d.line.LineSegment2D edge : this.edges())
            sum += edge.length();
        return sum;
    }

    /* (non-Javadoc)
     * @see CirculinearCurve2D#length(double)
     */
    public double length(double pos) {
        //init
        double length = 0;

        // add length of each curve before current curve
        int index = (int) Math.floor(pos);
        for (int i = 0; i < index; i++)
            length += this.edge(i).length();

        // add portion of length for last curve
        if (index < vertices.size() - 1) {
            double pos2 = pos - index;
            length += this.edge(index).length(pos2);
        }

        // return computed length
        return length;
    }

    /* (non-Javadoc)
     * @see CirculinearCurve2D#position(double)
     */
    public double position(double length) {

        // position to compute
        double pos = 0;

        // index of current curve
        int index = 0;

        // cumulative length
        double cumLength = this.length(this.t0());

        // iterate on all curves
        for (com.example.duy.calculator.geom2d.line.LineSegment2D edge : edges()) {
            // length of current curve
            double edgeLength = edge.length();

            // add either 2, or fraction of length
            if (cumLength + edgeLength < length) {
                cumLength += edgeLength;
                index++;
            } else {
                // add local position on current curve
                double pos2 = edge.position(length - cumLength);
                pos = index + pos2;
                break;
            }
        }

        // return the result
        return pos;
    }


    // ===================================================================
    // Methods implementing OrientedCurve2D interface

    /*
     * (non-Javadoc)
     * 
     * @see com.example.duy.calculator.geom2d.OrientedCurve2D#signedDistance(Point2D)
     */
    public double signedDistance(Point2D point) {
        double dist = this.distance(point.x(), point.y());
        if (isInside(point))
            return -dist;
        else
            return dist;
    }

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

    /*
     * (non-Javadoc)
     * 
     * @see com.example.duy.calculator.geom2d.OrientedCurve2D#signedDistance(double, double)
     */
    public double signedDistance(double x, double y) {
        double dist = this.distance(x, y);
        if (isInside(new Point2D(x, y)))
            return -dist;
        else
            return dist;
    }


    // ===================================================================
    // Methods inherited from ContinuousCurve2D

    /* (non-Javadoc)
     * @see ContinuousCurve2D#leftTangent(double)
     */
    public Vector2D leftTangent(double t) {
        int index = (int) Math.floor(t);
        if (Math.abs(t - index) < Shape2D.ACCURACY)
            index--;
        return this.edge(index).tangent(0);
    }

    /* (non-Javadoc)
     * @see ContinuousCurve2D#rightTangent(double)
     */
    public Vector2D rightTangent(double t) {
        int index = (int) Math.ceil(t);
        return this.edge(index).tangent(0);
    }

    /* (non-Javadoc)
     * @see ContinuousCurve2D#curvature(double)
     */
    public double curvature(double t) {
        double index = Math.round(t);
        if (Math.abs(index - t) > Shape2D.ACCURACY)
            return 0;
        else
            return Double.POSITIVE_INFINITY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.duy.calculator.geom2d.ContinuousCurve2D#smoothPieces()
     */
    public Collection<? extends com.example.duy.calculator.geom2d.line.LineSegment2D> smoothPieces() {
        return edges();
    }


    // ===================================================================
    // Methods inherited from interface Curve2D

    /**
     * Returns 0.
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
     * Returns the first point of the linear curve.
     */
    public Point2D firstPoint() {
        if (vertices.size() == 0)
            return null;
        return vertices.get(0);
    }

    public Collection<Point2D> singularPoints() {
        return vertices;
    }

    public boolean isSingular(double pos) {
        if (Math.abs(pos - Math.round(pos)) < Shape2D.ACCURACY)
            return true;
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.duy.calculator.geom2d.Curve2D#position(Point2D)
     */
    public double position(Point2D point) {
        int ind = 0;
        double dist, minDist = Double.POSITIVE_INFINITY;
        double x = point.x();
        double y = point.y();

        int i = 0;
        com.example.duy.calculator.geom2d.line.LineSegment2D closest = null;
        for (com.example.duy.calculator.geom2d.line.LineSegment2D edge : this.edges()) {
            dist = edge.distance(x, y);
            if (dist < minDist) {
                minDist = dist;
                ind = i;
                closest = edge;
            }
            i++;
        }

        return closest.position(point) + ind;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.duy.calculator.geom2d.Curve2D#intersections(com.example.duy.calculator.geom2d.LinearShape2D)
     */
    public Collection<Point2D> intersections(LinearShape2D line) {
        ArrayList<Point2D> list = new ArrayList<Point2D>();

        // extract intersections with each edge, and add to a list
        Point2D point;
        for (com.example.duy.calculator.geom2d.line.LineSegment2D edge : this.edges()) {
            // do not process edges parallel to intersection line
            if (edge.isParallel(line))
                continue;

            point = edge.intersection(line);
            if (point != null)
                if (!list.contains(point))
                    list.add(point);
        }

        // return result
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.duy.calculator.geom2d.Curve2D#position(Point2D)
     */
    public double project(Point2D point) {
        double dist, minDist = Double.POSITIVE_INFINITY;
        double x = point.x();
        double y = point.y();
        double pos = Double.NaN;

        int i = 0;
        for (com.example.duy.calculator.geom2d.line.LineSegment2D edge : this.edges()) {
            dist = edge.distance(x, y);
            if (dist < minDist) {
                minDist = dist;
                pos = edge.project(point) + i;
            }
            i++;
        }

        return pos;
    }


    // ===================================================================
    // Methods inherited from interface Shape2D

    /*
     * (non-Javadoc)
     * 
     * @see Shape2D#distance(double, double)
     */
    public double distance(double x, double y) {
        double dist = Double.MAX_VALUE;
        for (com.example.duy.calculator.geom2d.line.LineSegment2D edge : this.edges()) {
            if (edge.length() == 0)
                continue;
            dist = Math.min(dist, edge.distance(x, y));
        }
        return dist;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Shape2D#distance(Point2D)
     */
    public double distance(Point2D point) {
        return distance(point.x(), point.y());
    }

    /**
     * Returns true if the polyline does not contain any point.
     */
    public boolean isEmpty() {
        return vertices.size() == 0;
    }

    /**
     * Always returns true, because a linear curve is always bounded.
     */
    public boolean isBounded() {
        return true;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#contains(double, double)
     */
    public boolean contains(double x, double y) {
        for (com.example.duy.calculator.geom2d.line.LineSegment2D edge : this.edges()) {
            if (edge.length() == 0)
                continue;
            if (edge.contains(x, y))
                return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Shape#contains(Point2D)
     */
    public boolean contains(Point2D point) {
        return this.contains(point.x(), point.y());
    }


    /**
     * Returns a general path iterator.
     */
    public Path asGeneralPath() {
        Path path = new Path();
        if (vertices.size() < 2)
            return path;
        return this.appendPath(path);
    }

}
