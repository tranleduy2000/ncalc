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

package com.duy.ncalc.geom2d.util;

/**
 * Exception thrown when an unbounded shape is involved in an operation
 * that assumes a bounded shape. 
 * @author dlegland
 */
public class UnboundedShape2DException extends RuntimeException {

	private Shape2D shape;
	
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public UnboundedShape2DException(Shape2D shape) {
    	this.shape = shape;
    }

    public Shape2D getShape() {
    	return shape;
    }
}
