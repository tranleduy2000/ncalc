/* File Ellipse2D.java 
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

package com.example.duy.calculator.geom2d.conic;

import android.graphics.Path;

import com.example.duy.calculator.geom2d.util.AffineTransform2D;
import com.example.duy.calculator.geom2d.util.Angle2D;
import com.example.duy.calculator.geom2d.GeometricObject2D;
import com.example.duy.calculator.geom2d.Point2D;
import com.example.duy.calculator.geom2d.util.Shape2D;
import com.example.duy.calculator.geom2d.Vector2D;
import com.example.duy.calculator.geom2d.curve.AbstractSmoothCurve2D;
import com.example.duy.calculator.geom2d.line.LineSegment2D;
import com.example.duy.calculator.geom2d.line.LinearShape2D;
import com.example.duy.calculator.geom2d.line.StraightLine2D;
import com.example.duy.calculator.geom2d.polygon.LinearCurve2D;
import com.example.duy.calculator.geom2d.util.EqualUtils;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

// Imports

/**
 * An ellipse in the plane. It is defined by the center, the orientation angle,
 * and the lengths of the two axis. No convention is taken about lengths of
 * semiaxis: the second semi axis can be greater than the first one.
 */
public class Ellipse2D extends AbstractSmoothCurve2D
        implements Cloneable {

    // ===================================================================
    // Static factories

    /**
     * Coordinate of center.
     */
    protected double xc;
    protected double yc;
    /**
     * Length of major semi-axis. Should be always positive.
     */
    protected double r1;
    /**
     * Length of minor semi-axis. Should be always positive.
     */
    protected double r2;
    /**
     * Orientation of major semi-axis, in radians, between 0 and 2*PI.
     */
    protected double theta = 0;


    // ===================================================================
    // Static methods
    /**
     * Directed ellipse or not
     */
    protected boolean direct = true;

    /**
     * Empty constructor, define ellipse centered at origin with both major and
     * minor semi-axis with length equal to 1.
     */
    public Ellipse2D() {
        this(0, 0, 1, 1, 0, true);
    }

    /**
     * Main constructor: define center by a point plus major and minor semi axis
     */
    public Ellipse2D(Point2D center, double l1, double l2) {
        this(center.x(), center.y(), l1, l2, 0, true);
    }


    // ===================================================================
    // class variables

    /**
     * Define center by coordinate, plus major and minor semi axis
     */
    public Ellipse2D(double xc, double yc, double l1, double l2) {
        this(xc, yc, l1, l2, 0, true);
    }

    /**
     * Define center by point, major and minor semi axis lengths, and
     * orientation angle.
     */
    public Ellipse2D(Point2D center, double l1, double l2, double theta) {
        this(center.x(), center.y(), l1, l2, theta, true);
    }

    /**
     * Define center by coordinate, major and minor semi axis lengths, and
     * orientation angle.
     */
    public Ellipse2D(double xc, double yc, double l1, double l2, double theta) {
        this(xc, yc, l1, l2, theta, true);
    }

    /**
     * Define center by coordinate, major and minor semi axis lengths,
     * orientation angle, and boolean flag for directed ellipse.
     */
    public Ellipse2D(double xc, double yc, double l1, double l2, double theta,
                     boolean direct) {
        this.xc = xc;
        this.yc = yc;

        r1 = l1;
        r2 = l2;

        this.theta = theta;
        this.direct = direct;
    }


    /**
     * Create a new Ellipse by specifying the two focii, and the length of the
     * chord. The chord equals the sum of distances between a point of the
     * ellipse and each focus.
     *
     * @param focus1 the first focus
     * @param focus2 the second focus
     * @param chord  the sum of distances to focii
     * @return a new instance of Ellipse2D
     */
    public static Ellipse2D create(Point2D focus1, Point2D focus2,
                                   double chord) {
        double x1 = focus1.x();
        double y1 = focus1.y();
        double x2 = focus2.x();
        double y2 = focus2.y();

        double xc = (x1 + x2) / 2;
        double yc = (y1 + y2) / 2;
        double theta = Angle2D.horizontalAngle(x1, y1, x2, y2);

        double dist = focus1.distance(focus2);
//        if (dist < Shape2D.ACCURACY)
//            return new Circle2D(xc, yc, chord / 2);

        double r1 = chord / 2;
        double r2 = sqrt(chord * chord - dist * dist) / 2;

        return new Ellipse2D(xc, yc, r1, r2, theta);
    }

    // ===================================================================
    // constructors

    /**
     * Main constructor: define center by a point plus major and minor semi axis
     *
     * @deprecated since 0.11.1
     */
    @Deprecated
    public static Ellipse2D create(Point2D center, double l1, double l2) {
        return new Ellipse2D(center.x(), center.y(), l1, l2, 0, true);
    }

    /**
     * Define center by point, major and minor semi axis lengths, and
     * orientation angle.
     *
     * @deprecated since 0.11.1
     */
    @Deprecated
    public static Ellipse2D create(Point2D center, double l1, double l2,
                                   double theta) {
        return new Ellipse2D(center.x(), center.y(), l1, l2, theta, true);
    }

    /**
     * Define center by point, major and minor semi axis lengths,
     * orientation angle, and boolean flag for direct ellipse.
     */
    public static Ellipse2D create(Point2D center, double l1, double l2,
                                   double theta, boolean direct) {
        return new Ellipse2D(center.x(), center.y(), l1, l2, theta, direct);
    }


    public final static Ellipse2D inertiaEllipse(Collection<Point2D> points) {
        double xc = 0;
        double yc = 0;

        for (Point2D p : points) {
            xc += p.getX();
            yc += p.getY();
        }

        int np = points.size();
        xc /= np;
        yc /= np;

        double Ixx = 0;
        double Iyy = 0;
        double Ixy = 0;

        for (Point2D p : points) {
            // re-centered point
            double x = p.getX() - xc;
            double y = p.getY() - yc;
            Ixx += x * x;
            Iyy += y * y;
            Ixy += x * y;
        }

        // normalize by point number
        Ixx /= np;
        Ixy /= np;
        Iyy /= np;

        // Compute ellipse semi-axis length
        double diff = Ixx - Iyy;
        double common = sqrt(diff * diff + 4 * Ixy * Ixy);
        double r1 = sqrt(2) * sqrt(Ixx + Iyy + common);
        double r2 = sqrt(2) * sqrt(Ixx + Iyy - common);

        // ellipse orientation
        double theta = atan2(2 * Ixy, Ixx - Iyy) / 2;

        // create ellipse object
        return new Ellipse2D(xc, yc, r1, r2, theta);
    }


    // ===================================================================
    // Methods specific to Ellipse2D

    /**
     * Return the RHO parameter, in a polar representation of the ellipse,
     * centered at the center of ellipse.
     *
     * @param angle : angle from horizontal
     * @return distance of ellipse from ellipse center in direction theta
     */
    public double getRho(double angle) {
        double cot = cos(angle - theta);
        double sit = cos(angle - theta);
        return r1 * r2 / hypot(r2 * cot, r1 * sit);
    }

    public Point2D projectedPoint(Point2D point) {
        Vector2D polar = this.projectedVector(point, Shape2D.ACCURACY);
        return new Point2D(point.x() + polar.x(), point.y() + polar.y());
    }

    /**
     * Compute projection of a point onto an ellipse. Return the polar vector
     * representing the translation from point <code>point</point> to its
     * projection on the ellipse, with the direction parallel to the local
     * normal to the ellipse. The parameter <code>rho</code> of the
     * PolarVector2D is positive if point lies
     * Refs : <p>
     * http://www.spaceroots.org/documents/distance/distance-to-ellipse.pdf,
     * http://www.spaceroots.org/downloads.html
     *
     * @param point
     * @param eMax
     * @return the projection vector
     */
    public Vector2D projectedVector(Point2D point, double eMax) {

        double ot = 1.0 / 3.0;

        // center the ellipse
        double x = point.x() - xc;
        double y = point.y() - yc;

        double la, lb, theta;
        if (r1 >= r2) {
            la = r1;
            lb = r2;
            theta = this.theta;
        } else {
            la = r2;
            lb = r1;
            theta = this.theta + PI / 2;
            double tmp = x;
            x = -y;
            y = tmp;
        }

        double cot = cos(theta);
        double sit = sin(theta);
        double tmpx = x, tmpy = y;
        x = tmpx * cot - tmpy * sit;
        y = tmpx * sit + tmpy * cot;

        double ae = la;
        double f = 1 - lb / la;
        double e2 = f * (2.0 - f);
        double g = 1.0 - f;
        double g2 = g * g;
        double ae2 = ae * ae;

        // compute some miscellaneous variables outside of the loop
        double z = y;
        double z2 = y * y;
        double r = x;
        double r2 = x * x;
        double g2r2ma2 = g2 * (r2 - ae2);
        double g2r2ma2pz2 = g2r2ma2 + z2;
        double dist = sqrt(r2 + z2);
        boolean inside = g2r2ma2pz2 <= 0;

        // point at the center
        if (dist < (1.0e-10 * ae)) {
            System.out.println("point at the center");
            return Vector2D.createPolar(r, 0);
        }

        double cz = r / dist;
        double sz = z / dist;
        double t = z / (dist + r);

        // distance to the ellipse along the current line
        // as the smallest root of a 2nd degree polynom :
        // a k^2 - 2 b k + c = 0
        double a = 1.0 - e2 * cz * cz;
        double b = g2 * r * cz + z * sz;
        double c = g2r2ma2pz2;
        double b2 = b * b;
        double ac = a * c;
        double k = c / (b + sqrt(b2 - ac));
        // double lambda =atan2(cart.y, cart.x);
        double phi = atan2(z - k * sz, g2 * (r - k * cz));

        // point on the ellipse
        if (abs(k) < (1.0e-10 * dist)) {
            // return new Ellipsoidic(lambda, phi, k);
            return Vector2D.createPolar(k, phi);
        }

        for (int iterations = 0; iterations < 100; ++iterations) {

            // 4th degree normalized polynom describing
            // circle/ellipse intersections
            // tau^4 + b tau^3 + c tau^2 + d tau + e = 0
            // (there is no need to compute e here)
            a = g2r2ma2pz2 + g2 * (2.0 * r + k) * k;
            b = -4.0 * k * z / a;
            c = 2.0 * (g2r2ma2pz2 + (1.0 + e2) * k * k) / a;
            double d = b;

            // reduce the polynom to degree 3 by removing
            // the already known real root
            // tau^3 + b tau^2 + c tau + d = 0
            b += t;
            c += t * b;
            d += t * c;

            // find the other real root
            b2 = b * b;
            double Q = (3.0 * c - b2) / 9.0;
            double R = (b * (9.0 * c - 2.0 * b2) - 27.0 * d) / 54.0;
            double D = Q * Q * Q + R * R;
            double tildeT, tildePhi;
            if (D >= 0) {
                double rootD = sqrt(D);
                double rMr = R - rootD;
                double rPr = R + rootD;
                tildeT = ((rPr > 0) ? pow(rPr, ot) : -pow(-rPr, ot))
                        + ((rMr > 0) ? pow(rMr, ot) : -pow(-rMr, ot))
                        - b * ot;
                double tildeT2 = tildeT * tildeT;
                double tildeT2P1 = 1.0 + tildeT2;
                tildePhi = atan2(z * tildeT2P1 - 2 * k * tildeT,
                        g2 * (r * tildeT2P1 - k * (1.0 - tildeT2)));
            } else {
                Q = -Q;
                double qRoot = sqrt(Q);
                double alpha = acos(R / (Q * qRoot));
                tildeT = 2 * qRoot * cos(alpha * ot) - b * ot;
                double tildeT2 = tildeT * tildeT;
                double tildeT2P1 = 1.0 + tildeT2;
                tildePhi = atan2(z * tildeT2P1 - 2 * k * tildeT,
                        g2 * (r * tildeT2P1 - k * (1.0 - tildeT2)));
                if ((tildePhi * phi) < 0) {
                    tildeT = 2 * qRoot * cos((alpha + 2 * PI) * ot) - b * ot;
                    tildeT2 = tildeT * tildeT;
                    tildeT2P1 = 1.0 + tildeT2;
                    tildePhi = atan2(z * tildeT2P1 - 2 * k * tildeT, g2
                            * (r * tildeT2P1 - k * (1.0 - tildeT2)));
                    if (tildePhi * phi < 0) {
                        tildeT = 2 * qRoot * cos((alpha + 4 * PI) * ot) - b * ot;
                        tildeT2 = tildeT * tildeT;
                        tildeT2P1 = 1.0 + tildeT2;
                        tildePhi = atan2(z * tildeT2P1 - 2 * k * tildeT, g2
                                * (r * tildeT2P1 - k * (1.0 - tildeT2)));
                    }
                }
            }

            // midpoint on the ellipse
            double dPhi = abs(0.5 * (tildePhi - phi));
            phi = 0.5 * (phi + tildePhi);
            double cPhi = cos(phi);
            double sPhi = sin(phi);
            double coeff = sqrt(1.0 - e2 * sPhi * sPhi);

//            // Eventually display result of iterations
//            System.out.println(iterations+": phi = "+Math.toDegrees(phi)
//            		+" +/- "+Math.toDegrees(dPhi)+", k = "+k);

            b = ae / coeff;
            double dR = r - cPhi * b;
            double dZ = z - sPhi * b * g2;
            k = hypot(dR, dZ);
            if (inside) {
                k = -k;
            }
            t = dZ / (k + dR);

            if (dPhi < 1.0e-14) {
                if (this.r1 >= this.r2)
                    return Vector2D.createPolar(-k, phi + theta);
                else
                    return Vector2D.createPolar(-k, phi + theta - PI / 2);
            }
        }

        System.out.println("Ellipse.getProjectedVector() did not converge");
        return Vector2D.createPolar(k, phi);
    }

    /**
     * Return the parallel ellipse located at a distance d from this ellipse.
     * For direct ellipse, distance is positive outside of the ellipse, and
     * negative inside
     */
    public Ellipse2D parallel(double d) {
        return new Ellipse2D(xc, yc, abs(r1 + d), abs(r2 + d), theta, direct);
    }


    // ===================================================================
    // accessors to basic characteristics of Ellipse

    /**
     * Returns true if ellipse has a direct orientation.
     */
    public boolean isDirect() {
        return direct;
    }

    /**
     * Returns true if this ellipse is similar to a circle, i.e. has same
     * length for both r1 and r2.
     */
    public boolean isCircle() {
        return abs(r1 - r2) < Shape2D.ACCURACY;
    }

    /**
     * Returns the length of the major semi-axis of the ellipse.
     */
    public double semiMajorAxisLength() {
        return r1;
    }

    /**
     * Returns the length of the minor semi-axis of the ellipse.
     */
    public double semiMinorAxisLength() {
        return r2;
    }

    /**
     * Returns center of the ellipse.
     */
    public Point2D center() {
        return new Point2D(xc, yc);
    }

    /**
     * Return the first focus. It is defined as the first focus on the Major
     * axis, in the direction given by angle theta.
     */
    public Point2D focus1() {
        double a, b, theta;
        if (r1 > r2) {
            a = r1;
            b = r2;
            theta = this.theta;
        } else {
            a = r2;
            b = r1;
            theta = this.theta + PI / 2;
        }
        return Point2D.createPolar(xc, yc, sqrt(a * a - b * b), theta + PI);
    }

    /**
     * Returns the second focus. It is defined as the second focus on the Major
     * axis, in the direction given by angle theta.
     */
    public Point2D focus2() {
        double a, b, theta;
        if (r1 > r2) {
            a = r1;
            b = r2;
            theta = this.theta;
        } else {
            a = r2;
            b = r1;
            theta = this.theta + PI / 2;
        }
        return Point2D.createPolar(xc, yc, sqrt(a * a - b * b), theta);
    }

    /**
     * Returns the first direction vector of the ellipse, in the direction of
     * the major axis.
     */
    public Vector2D vector1() {
        return new Vector2D(cos(theta), sin(theta));
    }

    /**
     * Returns the second direction vector of the ellipse, in the direction of
     * the minor axis.
     */
    public Vector2D vector2() {
        if (direct)
            return new Vector2D(-sin(theta), cos(theta));
        else
            return new Vector2D(sin(theta), -cos(theta));
    }

    /**
     * Returns the angle of the ellipse first axis with the Ox axis.
     */
    public double angle() {
        return theta;
    }

    // ===================================================================
    // methods implementing Conic2D interface

    public Conic2D.Type conicType() {
        if (Math.abs(this.r1 - this.r2) < Shape2D.ACCURACY)
            return Conic2D.Type.CIRCLE;
        return Conic2D.Type.ELLIPSE;
    }

    /**
     * Returns the conic coefficients of the ellipse. Algorithm taken from
     * http://tog.acm.org/GraphicsGems/gemsv/ch2-6/conmat.c
     */
    public double[] conicCoefficients() {

        // common coefficients
        double r1Sq = this.r1 * this.r1;
        double r2Sq = this.r2 * this.r2;

        // angle of ellipse, and trigonometric formulas
        double sint = sin(this.theta);
        double cost = cos(this.theta);
        double sin2t = 2.0 * sint * cost;
        double sintSq = sint * sint;
        double costSq = cost * cost;

        // coefs from ellipse center
        double xcSq = xc * xc;
        double ycSq = yc * yc;
        double r1SqInv = 1.0 / r1Sq;
        double r2SqInv = 1.0 / r2Sq;

        /*
         * Compute the coefficients. These formulae are the transformations on
         * the unit circle written out long hand
         */

        double a = costSq / r1Sq + sintSq / r2Sq;
        double b = (r2Sq - r1Sq) * sin2t / (r1Sq * r2Sq);
        double c = costSq / r2Sq + sintSq / r1Sq;
        double d = -yc * b - 2 * xc * a;
        double e = -xc * b - 2 * yc * c;
        double f = -1.0 + (xcSq + ycSq) * (r1SqInv + r2SqInv) / 2.0
                + (costSq - sintSq) * (xcSq - ycSq) * (r1SqInv - r2SqInv) / 2.0
                + xc * yc * (r1SqInv - r2SqInv) * sin2t;

        // Return array of results
        return new double[]{a, b, c, d, e, f};
    }

    /**
     * Computes eccentricity of ellipse, depending on the lengths of the
     * semi-axes. Eccentricity is 0 for a circle (r1==r2), and tends to 1 when
     * ellipse elongates.
     */
    public double eccentricity() {
        double a = max(r1, r2);
        double b = min(r1, r2);
        double r = b / a;
        return sqrt(1 - r * r);
    }


    // ===================================================================
    // methods implementing the Boundary2D interface


    // ===================================================================
    // methods implementing OrientedCurve2D interface

    /**
     * Return either 0, 2*PI or -2*PI, depending whether the point is located
     * inside the interior of the ellipse or not.
     */
    public double windingAngle(Point2D point) {
        if (this.signedDistance(point) > 0)
            return 0;
        else
            return direct ? PI * 2 : -PI * 2;
    }

    /**
     * Test whether the point is inside the ellipse. The test is performed by
     * rotating the ellipse and the point to align with axis, rescaling in each
     * direction, then computing distance to origin.
     */
    public boolean isInside(Point2D point) {
        AffineTransform2D rot = AffineTransform2D.createRotation(this.xc,
                this.yc, -this.theta);
        Point2D pt = rot.transform(point);
        double xp = (pt.x() - this.xc) / this.r1;
        double yp = (pt.y() - this.yc) / this.r2;
        return (xp * xp + yp * yp < 1) ^ !direct;
    }

    /**
     * Returns an approximation of the signed distance to the ellipse.
     * In the current implementation, the ellipse is converted to a polyline.
     */
    public double signedDistance(Point2D point) {
        double dist = this.asPolyline(180).distance(point);
        return isInside(point) ? -dist : dist;
    }

    public double signedDistance(double x, double y) {
        return signedDistance(new Point2D(x, y));
    }

    // ===================================================================
    // methods of SmoothCurve2D interface

    public Vector2D tangent(double t) {
        if (!direct)
            t = -t;
        double cot = cos(theta);
        double sit = sin(theta);

        if (direct)
            return new Vector2D(
                    -r1 * sin(t) * cot - r2 * cos(t) * sit,
                    -r1 * sin(t) * sit + r2 * cos(t) * cot);
        else
            return new Vector2D(
                    r1 * sin(t) * cot + r2 * cos(t) * sit,
                    r1 * sin(t) * sit - r2 * cos(t) * cot);
    }

    /**
     * Returns the curvature of the ellipse.
     */
    public double curvature(double t) {
        if (!direct)
            t = -t;
        double cot = cos(t);
        double sit = sin(t);
        double k = r1 * r2 / pow(hypot(r2 * cot, r1 * sit), 3);
        return direct ? k : -k;
    }

    // ===================================================================
    // methods of ContinuousCurve2D interface

    /**
     * Returns true, as an ellipse is always closed.
     */
    public boolean isClosed() {
        return true;
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
    public LinearCurve2D asPolyline(int n) {
        return null;
    }


    /**
     * Always returns true.
     */
    public boolean isBounded() {
        return true;
    }

    /**
     * Always returns false.
     */
    public boolean isEmpty() {
        return false;
    }

    /**
     * Returns the parameter of the first point of the ellipse, set to 0.
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
     * Returns the parameter of the last point of the ellipse, set to 2*PI.
     */
    public double t1() {
        return 2 * PI;
    }

    /**
     * @deprecated replaced by t1() (since 0.11.1).
     */
    @Deprecated
    public double getT1() {
        return t1();
    }

    /**
     * get the position of the curve from internal parametric representation,
     * depending on the parameter t. This parameter is between the two limits 0
     * and 2*PI.
     */
    public Point2D point(double t) {
        if (!direct)
            t = -t;
        double cot = cos(theta);
        double sit = sin(theta);
        return new Point2D(
                xc + r1 * cos(t) * cot - r2 * sin(t) * sit,
                yc + r1 * cos(t) * sit + r2 * sin(t) * cot);
    }

    /**
     * Returns the first point of the ellipse, which is the same as the last
     * point.
     *
     * @return the first point of the ellipse
     */
    @Override
    public Point2D firstPoint() {
        return new Point2D(xc + r1 * cos(theta), yc + r1 * sin(theta));
    }

    /**
     * Returns the last point of the ellipse, which is the same as the first
     * point.
     *
     * @return the last point of the ellipse.
     */
    @Override
    public Point2D lastPoint() {
        return new Point2D(xc + r1 * cos(theta), yc + r1 * sin(theta));
    }

    /**
     * Transforms the coordinates of the given point to the coordinate system
     * if the ellipse were transformed to a unit circle.
     *
     * @param point the point to transform
     * @return the coordinates of points in unit circle coordinate system
     */
    private Point2D toUnitCircle(Point2D point) {
        // extract coordinates
        double xp = point.x();
        double yp = point.y();

        // translate
        xp = xp - this.xc;
        yp = yp - this.yc;

        // rotate
        double cot = cos(theta);
        double sit = sin(theta);
        double xp1 = xp * cot + yp * sit;
        double yp1 = -xp * sit + yp * cot;
        xp = xp1;
        yp = yp1;

        // scale
        xp = xp / this.r1;
        yp = yp / this.r2;

        // manage orientation
        if (!direct)
            yp = -yp;

        return new Point2D(xp, yp);
    }

    public double position(Point2D point) {
        Point2D p2 = toUnitCircle(point);
        double xp = p2.x();
        double yp = p2.y();

        // compute angle
        double angle = Angle2D.horizontalAngle(xp, yp);

        if (abs(hypot(xp, yp) - 1) < Shape2D.ACCURACY)
            return angle;
        else
            return Double.NaN;
    }

    /**
     * Computes the approximate projection position of the point on the ellipse.
     * The ellipse is first converted to a unit circle, then the angular
     * position of the point is computed in the transformed basis.
     */
    public double project(Point2D point) {
        Point2D p2 = toUnitCircle(point);
        double xp = p2.x();
        double yp = p2.y();

        // compute angle
        double angle = Angle2D.horizontalAngle(xp, yp);

        return angle;
    }

    @Override
    public Collection<Point2D> intersections(LinearShape2D line) {
        return null;
    }

    /**
     * Returns the ellipse with same center and same radius, but with the other
     * orientation.
     */
    public Ellipse2D reverse() {
        return new Ellipse2D(xc, yc, r1, r2, theta, !direct);
    }


    /**
     * return a new EllipseArc2D.
     */
    public EllipseArc2D subCurve(double t0, double t1) {
        double startAngle, extent;
        if (this.direct) {
            startAngle = t0;
            extent = Angle2D.formatAngle(t1 - t0);
        } else {
            extent = -Angle2D.formatAngle(t1 - t0);
            startAngle = Angle2D.formatAngle(-t0);
        }
        return new EllipseArc2D(this, startAngle, extent);
    }


    // ===================================================================
    // methods of Shape2D interface

    /**
     * Computes distance using a polyline approximation.
     */
    public double distance(Point2D point) {
        // PolarVector2D vector = this.getProjectedVector(point, 1e-10);
        // return abs(vector.getRho());
        return this.asPolyline(180).distance(point);
    }

    public double distance(double x, double y) {
        return distance(new Point2D(x, y));
    }


    /**
     * Compute intersections of the ellipse with a straight object (line, line
     * segment, ray...).
     * <p/>
     * Principle of the algorithm is to transform line and ellipse such that
     * ellipse becomes a circle, then using the intersections computation from
     * circle.
     */
    public Collection<Point2D> intersections(LineSegment2D line) {


        // transform the line accordingly
        LinearShape2D line2 = line;

        // The list of intersections
        Collection<Point2D> points;

        // Compute intersection points with circle
        Circle2D circle = new Circle2D(0, 0, 1);
        points = circle.intersections(line2);
        if (points.size() == 0)
            return points;

        // convert points on circle as angles
        ArrayList<Point2D> res = new ArrayList<Point2D>(points.size());
        for (Point2D point : points)
            res.add(this.point(circle.position(point)));

        // return the result
        return res;
    }


    // ===================================================================
    // methods implementing the Shape interface

    /**
     * Returns true if the point p lies on the ellipse, with precision given
     * by Shape2D.ACCURACY.
     */
    public boolean contains(Point2D p) {
        return contains(p.x(), p.y());
    }

    /**
     * Returns true if the point (x, y) lies on the ellipse, with precision
     * given by Shape2D.ACCURACY.
     */
    public boolean contains(double x, double y) {
        return this.distance(x, y) < Shape2D.ACCURACY;
    }

    public Path getGeneralPath() {
        // precompute cosine and sine of angle
        double cot = cos(theta);
        double sit = sin(theta);

        // create new path
        Path path = new Path();

        // move to the first point
        path.moveTo((float) (xc + r1 * cot), (float) (yc + r1 * sit));

        // return path after adding curve
        return this.appendPath(path);
    }

    /**
     * Add the path of the ellipse to the given path.
     *
     * @param path the path to be completed
     * @return the completed path
     */
    public Path appendPath(Path path) {
        double cot = cos(theta);
        double sit = sin(theta);

        // draw each line of the boundary
        if (direct)
            for (double t = .1; t <= 2 * PI; t += .1)
                path.lineTo(
                        (float) (xc + r1 * cos(t) * cot - r2 * sin(t) * sit),
                        (float) (yc + r2 * sin(t) * cot + r1 * cos(t) * sit));
        else
            for (double t = .1; t <= 2 * PI; t += .1)
                path.lineTo(
                        (float) (xc + r1 * cos(t) * cot + r2 * sin(t) * sit),
                        (float) (yc - r2 * sin(t) * cot + r1 * cos(t) * sit));

        // loop to the first/last point
        path.lineTo((float) (xc + r1 * cot), (float) (yc + r1 * sit));

        return path;
    }


    // ===================================================================
    // methods implementing GeometricObject2D interface

    /* (non-Javadoc)
     * @see GeometricObject2D#almostEquals(GeometricObject2D, double)
     */
    public boolean almostEquals(GeometricObject2D obj, double eps) {
        if (this == obj)
            return true;

        if (!(obj instanceof Ellipse2D))
            return false;

        Ellipse2D ell = (Ellipse2D) obj;

        if (!ell.center().almostEquals(this.center(), eps))
            return false;
        if (abs(ell.r1 - this.r1) > eps)
            return false;
        if (abs(ell.r2 - this.r2) > eps)
            return false;
        if (!Angle2D.almostEquals(ell.angle(), this.angle(), eps))
            return false;
        if (ell.isDirect() != this.isDirect())
            return false;
        return true;
    }


    // ===================================================================
    // methods of Object superclass

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Ellipse2D))
            return false;

        Ellipse2D that = (Ellipse2D) obj;

        // Compare each field
        if (!EqualUtils.areEqual(this.xc, that.xc))
            return false;
        if (!EqualUtils.areEqual(this.yc, that.yc))
            return false;
        if (!EqualUtils.areEqual(this.r1, that.r1))
            return false;
        if (!EqualUtils.areEqual(this.r2, that.r2))
            return false;
        if (!EqualUtils.areEqual(this.theta, that.theta))
            return false;
        if (this.direct != that.direct)
            return false;

        return true;
    }

    /**
     * @deprecated use copy constructor instead (0.11.2)
     */
    @Deprecated
    @Override
    public Ellipse2D clone() {
        return new Ellipse2D(xc, yc, r1, r2, theta, direct);
    }

    @Override
    public String toString() {
        return String.format("Ellipse2D(%f,%f,%f,%f,%f,%s)",
                xc, yc, r1, r2, theta, direct ? "true" : "false");
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
