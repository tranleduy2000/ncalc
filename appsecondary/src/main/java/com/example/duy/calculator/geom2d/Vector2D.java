/* File Point2D.java 
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

package com.example.duy.calculator.geom2d;

import com.example.duy.calculator.geom2d.util.Angle2D;
import com.example.duy.calculator.geom2d.util.EqualUtils;
import com.example.duy.calculator.geom2d.util.Shape2D;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.sin;

// Imports

/**
 * A vector in the 2D plane. Provides methods to compute cross product and dot
 * product, addition and subtraction of vectors.
 */
public class Vector2D implements GeometricObject2D, Cloneable {

    // ===================================================================
    // static functions

    /**
     * the x-coordinate of the vector
     */
    public double x;
    /**
     * the y-coordinate of the vector
     */
    public double y;

    /**
     * Constructs a new Vectors initialized with x=1 and y=0.
     */
    public Vector2D() {
        this(1, 0);
    }

    /**
     * Constructs a new vector with the given coordinates.
     * Consider creating a new Vector using static factory.
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new vector between the origin and the given point.
     */
    public Vector2D(Point2D point) {
        this(point.x, point.y);
    }

    /**
     * Constructs a new vector between two points
     */
    public Vector2D(Point2D point1, Point2D point2) {
        this(point2.x - point1.x, point2.y - point1.y);
    }


    /**
     * Creates a new vector by specifying the distance to the origin, and the
     * angle with the horizontal.
     */
    public static Vector2D createPolar(double rho, double theta) {
        return new Vector2D(rho * cos(theta), rho * sin(theta));
    }


    // ===================================================================
    // constructors

    /**
     * Get the dot product of the two vectors, defined by :
     * <p/>
     * <code> dx1*dy2 + dx2*dy1</code>
     * <p/>
     * Dot product is zero if the vectors defined by the 2 vectors are
     * orthogonal. It is positive if vectors are in the same direction, and
     * negative if they are in opposite direction.
     */
    public static double dot(Vector2D v1, Vector2D v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    /**
     * Get the cross product of the two vectors, defined by :
     * <p/>
     * <code> dx1*dy2 - dx2*dy1</code>
     * <p/>
     * Cross product is zero for colinear vectors. It is positive if angle
     * between vector 1 and vector 2 is comprised between 0 and PI, and negative
     * otherwise.
     */
    public static double cross(Vector2D v1, Vector2D v2) {
        return v1.x * v2.y - v2.x * v1.y;
    }

    /**
     * Tests if the two vectors are colinear
     *
     * @return true if the vectors are colinear
     */
    public static boolean isColinear(Vector2D v1, Vector2D v2) {
        v1 = v1.normalize();
        v2 = v2.normalize();
        return abs(v1.x * v2.y - v1.y * v2.x) < Shape2D.ACCURACY;
    }

    /**
     * Tests if the two vectors are orthogonal
     *
     * @return true if the vectors are orthogonal
     */
    public static boolean isOrthogonal(Vector2D v1, Vector2D v2) {
        v1 = v1.normalize();
        v2 = v2.normalize();
        return abs(v1.x * v2.x + v1.y * v2.y) < Shape2D.ACCURACY;
    }

    // ===================================================================
    // accessors

    /**
     * Returns the x coordinates of this vector.
     */
    public double x() {
        return this.x;
    }

    /**
     * Returns the x coordinates of this vector.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y coordinates of this vector.
     */
    public double y() {
        return this.y;
    }

    /**
     * Returns the y coordinates of this vector.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the opposite vector v2 of this, such that the sum of this and v2
     * equals the null vector.
     *
     * @return the vector opposite to <code>this</code>.
     */
    public Vector2D opposite() {
        return new Vector2D(-this.x, -this.y);
    }

    /**
     * Computes the norm of the vector
     *
     * @return the euclidean norm of the vector
     */
    public double norm() {
        return hypot(x, y);
    }

    /**
     * Returns the angle with the horizontal axis, in radians.
     *
     * @return the horizontal angle of the vector
     */
    public double angle() {
        return Angle2D.horizontalAngle(this);
    }

    /**
     * Returns the vector with same direction as this one, but with norm equal
     * to 1.
     */
    public Vector2D normalize() {
        double r = hypot(this.x, this.y);
        return new Vector2D(this.x / r, this.y / r);
    }

    // ===================================================================
    // compare with other vectors

    /**
     * test if the two vectors are colinear
     *
     * @return true if the vectors are colinear
     */
    public boolean isColinear(Vector2D v) {
        return Vector2D.isColinear(this, v);
    }

    /**
     * test if the two vectors are orthogonal
     *
     * @return true if the vectors are orthogonal
     */
    public boolean isOrthogonal(Vector2D v) {
        return Vector2D.isOrthogonal(this, v);
    }

    // ===================================================================
    // operations between vectors

    /**
     * Get the dot product with point <code>p</code>. Dot product id defined
     * by:
     * <p/>
     * <code> x1*y2 + x2*y1</code>
     * <p>.
     * <p/>
     * Dot product is zero if the vectors defined by the 2 points are
     * orthogonal. It is positive if vectors are in the same direction, and
     * negative if they are in opposite direction.
     */
    public double dot(Vector2D v) {
        return x * v.x + y * v.y;
    }

    /**
     * Get the cross product with point <code>p</code>. Cross product is
     * defined by :
     * <p/>
     * <code> x1*y2 - x2*y1</code>
     * <p>.
     * <p/>
     * Cross product is zero for colinear vector. It is positive if angle
     * between vector 1 and vector 2 is comprised between 0 and PI, and negative
     * otherwise.
     */
    public double cross(Vector2D v) {
        return x * v.y - v.x * y;
    }

    /**
     * Returns the sum of current vector with vector given as parameter. Inner
     * fields are not modified.
     */
    public Vector2D plus(Vector2D v) {
        return new Vector2D(x + v.x, y + v.y);
    }

    /**
     * Returns the subtraction of current vector with vector given as
     * parameter. Inner fields are not modified.
     */
    public Vector2D minus(Vector2D v) {
        return new Vector2D(x - v.x, y - v.y);
    }

    /**
     * Multiplies the vector by a scalar amount. Inner fields are not
     *
     * @param k the scale factor
     * @return the scaled vector
     * @since 0.7.0
     */
    public Vector2D times(double k) {
        return new Vector2D(this.x * k, this.y * k);
    }

    /**
     * Rotates the vector by the given angle.
     *
     * @param theta the angle of rotation, in radians counter-clockwise
     */
    public Vector2D rotate(double theta) {
        double cot = cos(theta);
        double sit = sin(theta);
        double x2 = x * cot - y * sit;
        double y2 = x * sit + y * cot;
        return new Vector2D(x2, y2);
    }


    /**
     * Test whether this object is the same as another vector, with respect
     * to a given threshold.
     */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof Vector2D))
            return false;
        Vector2D v = (Vector2D) obj;

        if (Math.abs(this.x - v.x) > eps)
            return false;
        if (Math.abs(this.y - v.y) > eps)
            return false;

        return true;
    }

    /**
     * Test whether this object is exactly the same as another vector.
     *
     * @see #almostEquals
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Vector2D))
            return false;
        Vector2D that = (Vector2D) obj;

        // Compare each field
        if (!EqualUtils.areEqual(this.x, that.x))
            return false;
        if (!EqualUtils.areEqual(this.y, that.y))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + Double.valueOf(this.x).hashCode();
        hash = hash * 31 + Double.valueOf(this.y).hashCode();
        return hash;
    }

    /**
     * Display the coordinates of the vector. Typical output is:
     * <code>x=3 y=4</code>.
     */
    @Override
    public String toString() {
        return "Vector(" + x + "; " + y + ")";
    }

    public Vector2D getOrthogonal() {
        return new Vector2D(-y, x);
    }


    public double getScalar(Vector2D vector) {
        return (Math.sqrt(x * x + y * y))
                * Math.sqrt(Math.pow(vector.getX(), 2.0d) + Math.pow(vector.getY(), 2.0d))
                * cos(Angle2D.angle(this, vector));
    }
}