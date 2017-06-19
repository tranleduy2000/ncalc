/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 */

package com.duy.calculator.geom2d.line;


import com.duy.calculator.geom2d.Point2D;
import com.duy.calculator.geom2d.Vector2D;

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
    public abstract com.duy.calculator.geom2d.line.StraightLine2D supportingLine();

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
