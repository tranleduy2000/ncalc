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
 * File: 	GeometricObject2D.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 26 sept. 2010
 */
package com.duy.ncalc.geom2d;


/**
 * Grouping interface for all objects operating on Euclidean plane. This
 * includes shapes, boxes, transforms, vectors...
 * @author dlegland
 *
 */
public interface GeometricObject2D {

	/**
	 * Checks if the two objects are similar up to a given threshold value.
	 * This method can be used to compare the results of geometric
	 * computations, that introduce errors due to numerical computations.
	 *
	 * @param obj the object to compare
	 * @param eps a threshold value, for example the minimal coordinate difference
	 * @return true if both object have the same value up to the threshold
	 */
	public boolean almostEquals(GeometricObject2D obj, double eps);
}
