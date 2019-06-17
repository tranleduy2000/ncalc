/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * 
 */

package com.duy.ncalc.geom2d.line;


import com.duy.ncalc.geom2d.Point2D;
import com.duy.ncalc.geom2d.Vector2D;

/**
 * A curve that can be inscribed in a straight line, like a ray, a straight
 * line, or a line segment. Classes implementing this interface can be
 * discontinuous
 * 
 * @author dlegland
 */
public interface LinearShape2D  {

	/**
	 * Returns the straight line that contains this linear shape. 
	 * The direction is the same, and if possible the direction vector 
	 * should be the same. 
	 * @return the straight line that contains this linear shape 
	 */
    public abstract com.duy.ncalc.geom2d.line.StraightLine2D supportingLine();

    /**
     * Returns the angle with axis (O,i), counted counter-clockwise. Result 
     * is given between 0 and 2*pi.
     */
    public abstract double horizontalAngle();

    /**
     * Returns a point in the linear shape.
     * 
     * @return a point in the linear shape.
     */
    public abstract Point2D origin();

    /**
     * Return one direction vector of the linear shape.
     * 
     * @return a direction vector
     */
    public abstract Vector2D direction();


    /**
	 * Checks if the shape contains the orthogonal projection of the specified
	 * point. The result is always true for straight lines. For bounded line
	 * shapes, the result depends on the position of the point with respect to
	 * shape bounds.
	 * 
	 * @param point
	 *            a point in the plane
	 * @return true if the orthogonal projection of the point on the supporting
	 *         line belongs to the linear shape.
	 */
    public boolean containsProjection(Point2D point);

}
