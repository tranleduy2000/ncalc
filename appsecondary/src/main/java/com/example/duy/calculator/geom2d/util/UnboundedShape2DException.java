/**
 *
 */

package com.example.duy.calculator.geom2d.util;

/**
 * Exception thrown when an unbounded shape is involved in an operation
 * that assumes a bounded shape.
 *
 * @author dlegland
 */
public class UnboundedShape2DException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Shape2D shape;

    public UnboundedShape2DException(Shape2D shape) {
        this.shape = shape;
    }

    public Shape2D getShape() {
        return shape;
    }
}
