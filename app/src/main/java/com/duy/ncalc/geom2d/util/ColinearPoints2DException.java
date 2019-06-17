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
 * File: 	ColinearPointsException.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 11 nov. 09
 */
package com.duy.ncalc.geom2d.util;


import com.duy.ncalc.geom2d.Point2D;

/**
 * Exception thrown when the assumption of non colinearity is not respected.
 * Methods are provided by retrieving the three incriminated points.
 * @author dlegland
 *
 */
public class ColinearPoints2DException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Point2D p1;
	protected Point2D p2;
	protected Point2D p3;
	
	public ColinearPoints2DException(Point2D p1, Point2D p2, Point2D p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public Point2D getP1() {
		return p1;
	}
	
	public Point2D getP2() {
		return p2;
	}
	
	public Point2D getP3() {
		return p3;
	}
}
