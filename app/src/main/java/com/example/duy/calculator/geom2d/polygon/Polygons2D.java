/**
 *
 */

package com.example.duy.calculator.geom2d.polygon;

import com.example.duy.calculator.geom2d.Point2D;

import java.util.Collection;

import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;

/**
 * Several utility functions for working on polygons, including polygon
 * creation, and basic computations such as polygon area or centroid.
 *
 * @author dlegland
 */
public final class Polygons2D {

    /**
     * Creates a new polygon representing a rectangle with edges parallel to
     * the main directions, and having the two specified opposite corners.
     *
     * @since 0.10.3
     */
    public final static SimplePolygon2D createRectangle(Point2D p1, Point2D p2) {
        // corners coordinates
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();

        return createRectangle(x1, y1, x2, y2);
    }

    /**
     * Creates a new polygon representing a rectangle with edges parallel to
     * the main directions, and having the two specified opposite corners.
     *
     * @since 0.10.3
     */
    public final static SimplePolygon2D createRectangle(double x1, double y1,
                                                        double x2, double y2) {
        // extremes coordinates
        double xmin = min(x1, x2);
        double xmax = max(x1, x2);
        double ymin = min(y1, y2);
        double ymax = max(y1, y2);

        // create result polygon
        return new SimplePolygon2D(
                new Point2D(xmin, ymin),
                new Point2D(xmax, ymin),
                new Point2D(xmax, ymax),
                new Point2D(xmin, ymax));
    }

    /**
     * Creates a new polygon representing a rectangle centered around a point.
     * Rectangle sides are parallel to the main axes. The function returns an
     * instance of SimplePolygon2D.
     *
     * @since 0.9.1
     */
    public final static SimplePolygon2D createCenteredRectangle(Point2D center,
                                                                double length, double width) {
        // extract rectangle parameters
        double xc = center.x();
        double yc = center.y();
        double len = length / 2;
        double wid = width / 2;

        // coordinates of corners
        double x1 = xc - len;
        double y1 = yc - wid;
        double x2 = xc + len;
        double y2 = yc + wid;

        // create result polygon
        return new SimplePolygon2D(new Point2D(x1, y1),
                new Point2D(x2, y1),
                new Point2D(x2, y2),
                new Point2D(x1, y2));
    }

    /**
     * Creates a new polygon representing an oriented rectangle centered
     * around a point.
     * The function returns an instance of SimplePolygon2D.
     *
     * @since 0.9.1
     */
    public final static SimplePolygon2D createOrientedRectangle(Point2D center,
                                                                double length, double width, double theta) {
        // extract rectangle parameters
        double xc = center.x();
        double yc = center.y();
        double len = length / 2;
        double wid = width / 2;

        // Pre-compute angle quantities
        double cot = cos(theta);
        double sit = sin(theta);

        // Create resulting rotated rectangle
        return new SimplePolygon2D(new Point2D(-len * cot + wid * sit + xc, -len * sit - wid * cot + yc),
                new Point2D(len * cot + wid * sit + xc, len * sit - wid * cot + yc),
                new Point2D(len * cot - wid * sit + xc, len * sit + wid * cot + yc),
                new Point2D(-len * cot - wid * sit + xc, -len * sit + wid * cot + yc));
    }

    /**
     * Computes the centroid of the given polygon.
     *
     * @since 0.9.1
     */
    public final static Point2D computeCentroid(Polygon2D polygon) {
        // process case of simple polygon
        if (polygon instanceof SimplePolygon2D) {
            LinearRing2D ring = ((SimplePolygon2D) polygon).getRing();
            return computeCentroid(ring);
        }

        double xc = 0;
        double yc = 0;
        double area;
        double cumArea = 0;
        Point2D centroid;

        for (LinearRing2D ring : polygon.contours()) {
            area = computeArea(ring);
            centroid = computeCentroid(ring);
            xc += centroid.x() * area;
            yc += centroid.y() * area;
            cumArea += area;
        }

        xc /= cumArea;
        yc /= cumArea;
        return new Point2D(xc, yc);
    }

    /**
     * Computes the centroid of the given linear ring.
     *
     * @since 0.9.1
     */
    public final static Point2D computeCentroid(LinearRing2D ring) {
        double xc = 0;
        double yc = 0;

        double x, y;
        double xp, yp;
        double tmp = 0;

        // number of vertices
        int n = ring.vertexNumber();

        // initialize with the last vertex
        Point2D prev = ring.vertex(n - 1);
        xp = prev.x();
        yp = prev.y();

        // iterate on vertices
        for (Point2D point : ring.vertices()) {
            x = point.x();
            y = point.y();
            tmp = xp * y - yp * x;
            xc += (x + xp) * tmp;
            yc += (y + yp) * tmp;

            prev = point;
            xp = x;
            yp = y;
        }

        double denom = computeArea(ring) * 6;
        return new Point2D(xc / denom, yc / denom);
    }


    /**
     * Computes the signed area of the polygon. Algorithm is taken from page: <a
     * href="http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/">
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/</a>. Signed area
     * is positive if polygon is oriented counter-clockwise, and negative
     * otherwise. Result is wrong if polygon is self-intersecting.
     *
     * @return the signed area of the polygon.
     * @since 0.9.1
     */
    public final static double computeArea(Polygon2D polygon) {
        double area = 0;
        for (LinearRing2D ring : polygon.contours()) {
            area += computeArea(ring);
        }
        return area;
    }

    /**
     * Computes the signed area of the linear ring. Algorithm is taken from page: <a
     * href="http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/">
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/</a>. Signed area
     * is positive if linear ring is oriented counter-clockwise, and negative
     * otherwise. Result is wrong if linear ring is self-intersecting.
     *
     * @return the signed area of the polygon.
     * @since 0.9.1
     */
    public static double computeArea(LinearRing2D ring) {
        double area = 0;

        // number of vertices
        int n = ring.vertexNumber();

        // initialize with the last vertex
        Point2D prev = ring.vertex(n - 1);

        // iterate on edges
        for (Point2D point : ring.vertices()) {
            area += prev.x() * point.y() - prev.y() * point.x();
            prev = point;
        }

        return area /= 2;
    }


    /**
     * Computes the winding number of the polygon. Algorithm adapted from
     * http://www.geometryalgorithms.com/Archive/algorithm_0103/algorithm_0103.htm
     * http://softsurfer.com/Archive/algorithm_0103/algorithm_0103.htm
     *
     * @param vertices the vertices of a polygon
     * @param point    the reference point
     * @return the number of windings of the curve around the point
     */
    public static int windingNumber(Collection<Point2D> vertices,
                                    Point2D point) {
        int wn = 0; // the winding number counter

        // Extract the last point of the collection
        Point2D previous = null;
        for (Point2D vertex : vertices)
            previous = vertex;
        double y1 = previous.y();
        double y2;

        // keep y-coordinate of test point
        double y = point.y();

        // Iterate on couple of vertices, starting from couple (last,first)
        for (Point2D current : vertices) {
            // second vertex of current edge
            y2 = current.y();

            if (y1 <= y) {
                if (y2 > y) // an upward crossing
                    if (isLeft(previous, current, point) > 0)
                        wn++;
            } else {
                if (y2 <= y) // a downward crossing
                    if (isLeft(previous, current, point) < 0)
                        wn--;
            }

            // for next iteration
            y1 = y2;
            previous = current;
        }

        return wn;
    }

    /**
     * Tests if a point is Left|On|Right of an infinite line.
     * Input:  three points P0, P1, and P2
     * Return: >0 for P2 left of the line through P0 and P1
     * =0 for P2 on the line
     * <0 for P2 right of the line
     * See: the January 2001 Algorithm "Area of 2D and 3D Triangles and Polygons"
     */
    private final static int isLeft(Point2D p1, Point2D p2, Point2D pt) {
        double x = p1.x();
        double y = p1.y();
        return (int) Math.signum(
                (p2.x() - x) * (pt.y() - y) - (pt.x() - x) * (p2.y() - y));
    }


}
