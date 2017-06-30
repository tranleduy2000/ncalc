/**
 *
 */

package com.example.duy.calculator.geom2d.polygon;

import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.line.LineSegment2D;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Some utility functions for manipulating Polyline2D.
 *
 * @author dlegland
 * @since 0.6.3
 */
public abstract class Polylines2D {

    static ArrayList<Point2D> simplifyPolyline(ArrayList<Point2D> vertices,
                                               double distMax) {
        // index of last vertex
        int last = vertices.size() - 1;
        int[] inds = recurseSimplify(vertices, 0, last, distMax);

        ArrayList<Point2D> newVerts = new ArrayList<Point2D>(inds.length + 2);
        newVerts.add(vertices.get(0));
        for (int i : inds)
            newVerts.add(vertices.get(i));
        newVerts.add(vertices.get(last));

        return newVerts;
    }

    static ArrayList<Point2D> simplifyClosedPolyline(ArrayList<Point2D> vertices,
                                                     double distMax) {


        // index of last vertex
        int nv = vertices.size();

        // first find index of farthest vertex from origin
        int indMax = 0;
        double maxDist = 0;
        Point2D p0 = vertices.get(0);
        for (int i = 1; i < vertices.size(); i++) {
            double dist = vertices.get(i).distance(p0);
            if (dist > maxDist) {
                maxDist = dist;
                indMax = i;
            }
        }

        int[] inds1 = recurseSimplify(vertices, 0, indMax, distMax);
        int[] inds2 = recurseSimplify(vertices, indMax, nv, distMax);

        int[] inds = concatInds(inds1, indMax, inds2);


        ArrayList<Point2D> newVerts = new ArrayList<Point2D>(inds.length + 2);
        newVerts.add(vertices.get(0));
        for (int i : inds)
            newVerts.add(vertices.get(i));

        return newVerts;
    }

    /**
     * Used for constructing simplified polylines.
     */
    private static int[] recurseSimplify(ArrayList<Point2D> vertices, int first,
                                         int last, double distMax) {
        if (last - first < 2) {
            return new int[0];
        }

        int nv = vertices.size();
        Point2D p0 = vertices.get(first);
        Point2D p1 = vertices.get(last % nv);
        LineSegment2D line = new LineSegment2D(p0, p1);

        double dist;
        double midDist = 0;
        int indMid = 0;
        for (int ind = first; ind < last; ind++) {
            dist = line.distance(vertices.get(ind));
            if (dist > midDist) {
                midDist = dist;
                indMid = ind;
            }
        }

        if (midDist < distMax) {
            return new int[]{};
        }

        // Recursively subdivide each of the resulting pieces
        int[] inds1 = recurseSimplify(vertices, first, indMid, distMax);
        int[] inds2 = recurseSimplify(vertices, indMid, last, distMax);

        int[] res = concatInds(inds1, indMid, inds2);

        // return concatenated indices
        return res;
    }

    private static int[] concatInds(int[] inds1, int indMid, int[] inds2) {
        int n = inds1.length + inds2.length + 1;

        int[] res = new int[n];

        System.arraycopy(inds1, 0, res, 0, inds1.length);
        res[inds1.length] = indMid;
        System.arraycopy(inds2, inds1.length + 1 - (inds1.length + 1), res, inds1.length + 1, n - (inds1.length + 1));

        return res;
    }


    /**
     * Return all intersection points between the 2 polylines.
     * This method implements a naive algorithm, that tests all possible edge
     * couples.
     * It is supposed that only one point is returned by intersection.
     *
     * @param poly1 a first polyline
     * @param poly2 a second polyline
     * @return the set of intersection points
     */
    public static Collection<Point2D> intersect(
            LinearCurve2D poly1, LinearCurve2D poly2) {
        // array for storing intersections
        ArrayList<Point2D> points = new ArrayList<Point2D>();

        // iterate on edge couples
        Point2D point;
        for (LineSegment2D edge1 : poly1.edges()) {
            for (LineSegment2D edge2 : poly2.edges()) {
                // if the intersection is not empty, add it to the set
                point = edge1.intersection(edge2);
                if (point != null) {
                    // we keep only one intersection by couple
                    if (!points.contains(point))
                        points.add(point);
                }
            }
        }

        return points;
    }
}
