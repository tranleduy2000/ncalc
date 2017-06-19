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

package com.duy.calculator.geom2d.util;

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
