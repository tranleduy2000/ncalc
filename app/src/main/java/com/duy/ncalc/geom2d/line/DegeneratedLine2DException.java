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
 * File: 	DegeneratedLine2DException.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 19 ao�t 2010
 */
package com.duy.ncalc.geom2d.line;


/**
 * A degenerated line, whose direction vector is undefined, had been
 * encountered.
 * This kind of exception can occur during polygon or polylines algorithms,
 * when polygons have multiple vertices. 
 * @author dlegland
 * @since 0.9.0
 */
public class DegeneratedLine2DException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected com.duy.ncalc.geom2d.line.LinearShape2D line;
	
	/**
	 * @param msg the error message
	 * @param line the degenerated line
	 */
	public DegeneratedLine2DException(String msg, com.duy.ncalc.geom2d.line.LinearShape2D line) {
		super(msg);
		this.line = line;
	}
	
	/**
	 * @param line the degenerated line
	 */
	public DegeneratedLine2DException(com.duy.ncalc.geom2d.line.LinearShape2D line) {
		super();
		this.line = line;
	}

	public com.duy.ncalc.geom2d.line.LinearShape2D getLine() {
		return line;
	}
}
