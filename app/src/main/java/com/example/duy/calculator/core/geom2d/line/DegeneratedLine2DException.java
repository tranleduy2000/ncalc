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
 * File: 	DegeneratedLine2DException.java
 * Project: javaGeom
 * 
 * Distributed under the LGPL License.
 *
 * Created: 19 ao�t 2010
 */
package com.example.duy.calculator.core.geom2d.line;


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

	protected com.example.duy.calculator.core.geom2d.line.LinearShape2D line;
	
	/**
	 * @param msg the error message
	 * @param line the degenerated line
	 */
	public DegeneratedLine2DException(String msg, com.example.duy.calculator.core.geom2d.line.LinearShape2D line) {
		super(msg);
		this.line = line;
	}
	
	/**
	 * @param line the degenerated line
	 */
	public DegeneratedLine2DException(com.example.duy.calculator.core.geom2d.line.LinearShape2D line) {
		super();
		this.line = line;
	}

	public com.example.duy.calculator.core.geom2d.line.LinearShape2D getLine() {
		return line;
	}
}
