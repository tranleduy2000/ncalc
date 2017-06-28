/**
 * File: 	CircularShape2D.java
 * Project: javaGeom
 * <p>
 * Distributed under the LGPL License.
 * <p>
 * Created: 17 mai 09
 */
package com.example.duy.calculator.geom2d.conic;


/**
 * Tagging interface for grouping Circle2D and CircleArc2D.
 * @author dlegland
 *
 */
public interface CircularShape2D {
    public Circle2D supportingCircle();

}
